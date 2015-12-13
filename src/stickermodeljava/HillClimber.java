/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import static java.lang.Math.log;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jared
 */
public class HillClimber {
    private final Sat sat;
    private boolean lastRunTimedOut;  //Contains exit criteria for the most recent run
    private int runTime;    //Contains run time of most recent run
    private double nonDefectPercentage;   //Contains percentage of non-defects of most recent run iteration
    private TestResults runTestResults; //Contains test results of most recent run iteration
    private int numIterations;  //Contains number of iterations in the last run
    private final Random random = new Random(System.currentTimeMillis());
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public HillClimber(Sat sat){
        this.sat = sat;
        lastRunTimedOut = false;
        runTime = -1;
        nonDefectPercentage = -1.0;
        numIterations = 0;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
    
    public ArrayList<DNAStrand> run(int timeLimitSeconds, double thresholdDefectPercentage, int strandSize, AlgorithmType algorithmType) throws IOException {
        //Create initial DNA strand list
        int number = sat.getNumUniqueVariables() * 2;  //Need a strand for true and false states of each variable.
        ArrayList<DNAStrand> strands = new ArrayList<>();
        ArrayList<DNAStrand> savedStrands;
        for(int i = 0; i<number; i++){
            strands.add(new DNAStrand(strandSize));
        }
        savedStrands = strands;
        
        //Get initial score for set of DNA strands
        StrandTest tester = new StrandTest();
        runTestResults = tester.runTests(strands);
        String structure = runTestResults.getSecondaryStructure();
        int defects = findNumDefects(structure);
        int savedDefects = defects;
        nonDefectPercentage = 1-((double)defects/(strandSize*strands.size()));
        
        //Climb the hill
        long start = java.time.Instant.now().getEpochSecond();
        long time=0;
        //double newRandomStrandProbability = 0.2;
        //double newRandomStrandProbability = 0.8;
        double newRandomStrandProbability = 1/strands.size();
        double acceptBadProb = 0.900000000000000000000;
        pcs.firePropertyChange("acceptBadProb", 0.0, acceptBadProb);
        //double temperature = 1;
        double temperature = (defects)/log(acceptBadProb);
        double coolingRate = 0.05;
        double s;
        double s_prime;
        double delta_s;
        double cumulativeMovingAverage = defects;
        boolean useNewStrand;
        
        numIterations = 1;
        while((time < timeLimitSeconds || timeLimitSeconds < 0) && nonDefectPercentage < thresholdDefectPercentage) {
            //Make mutable copy of savedStrands
            strands = cloneList(savedStrands);
            
            //Figure out which strand is the most problematic
            structure = runTestResults.getSecondaryStructure();
            int index = findProblemStrand(structure);
            if(index < 0) break;
            
            //Randomly replace a whole DNA strand with a random strand or fix 
            //  a part of a strand.  This mutation helps reduce local minima traps
            useNewStrand = random.nextDouble() <= newRandomStrandProbability;
            if(useNewStrand) {
                strands.set(index, new DNAStrand(strandSize));
            } else {
                DNAStrand strand = strands.get(index);
                int nucleotideNum = getDefectiveNucleotide(structure, index);
                if (nucleotideNum > 0) strand.newNucleotide(nucleotideNum);
                //Ensure uniqueness
                int pointer = getDuplicateIndex(strands);
                while(pointer >= 0) {
                    strands.set(pointer, new DNAStrand(strandSize));
                    pointer = getDuplicateIndex(strands);
                }
            }
            
            //Get new score
            TestResults oldTest = runTestResults;
            runTestResults = tester.runTests(strands);
            structure = runTestResults.getSecondaryStructure();
            defects = findNumDefects(structure);
            
            //Decide on acceptance
            if (defects < savedDefects || algorithmType.equals(AlgorithmType.Relaxed)) { //Relaxed accepts all solutions
                savedDefects = defects;
                savedStrands = strands;
                pcs.firePropertyChange("lastRunTestResults", oldTest, runTestResults);
            } else if (algorithmType.equals(AlgorithmType.Annealing)) { //Annealing accepts worse with decaying probability
                if(random.nextDouble() < acceptBadProb) {
                    savedDefects = defects;
                    savedStrands = strands;
                    pcs.firePropertyChange("lastRunTestResults", oldTest, runTestResults);
                }
            } //else (algorithmType.equals(AlgorithmType.Vanilla)) do nothing  //Vanilla rejects all worse solutions
            
            //Fix new temperature and probability of accepting worse solutions
            delta_s = Math.abs(savedDefects - defects);
            //cumulativeMovingAverage = (delta_s + numIterations*cumulativeMovingAverage)/(numIterations+1);
            double oldTemp = temperature;
            //temperature = (cumulativeMovingAverage)/log(acceptBadProb);
            temperature = (-2.0)/log(acceptBadProb);
            pcs.firePropertyChange("temperature", oldTemp, temperature);
            //temperature *= 1-coolingRate;
            //temperature -= delta_temperature;
            //acceptBadProb = Math.pow(Math.E, (-((double)delta_s))/temperature);
            double oldProb = acceptBadProb;
            acceptBadProb = Math.pow(Math.E, (-2.1)/temperature);
            pcs.firePropertyChange("acceptBadProb", oldProb, acceptBadProb);
            
            //Recalculate exit criteria
                //Figure out new nonDefectPercentage
            double oldPercentage = nonDefectPercentage;
            nonDefectPercentage = 1-((double)savedDefects/(strandSize*strands.size()));
            pcs.firePropertyChange("nonDefectPercentage", oldPercentage, nonDefectPercentage); //If solution was rejected, this does nothing
        
                //Figure out new elapsed time
            time = java.time.Instant.now().getEpochSecond() - start;
            
            //Update iteration count
            numIterations++;
            pcs.firePropertyChange("lastNumIterations",numIterations-1,numIterations);
        }
        
        //Record exit criterion
        lastRunTimedOut = (time > timeLimitSeconds && timeLimitSeconds > 0);
        
        //Record run stats
        runTime = (int)time;
        
        //Return set of strands that were found
        return savedStrands;
    }
    
    public TestResults getLastRunTestResults() {
        return runTestResults;
    }
    
    public int getLastRunTime() {
        return runTime;
    }
    
    public double getLastRunCombinationProbablility() {
        return nonDefectPercentage;
    }
    
    public boolean getLastRunExitCriteria() {
        return lastRunTimedOut;
    }

    public int getLastNumIterations() {
        return numIterations;
    }
    
    /**Parses the dnaStrands and finds the index of the strand with the most combinations
     * with other strands.  Just picks one if there is a tie.
     * 
     * @param dnaStrands Structure in dot-parens form of multiple strands together
     * @return Index of strand with most combinations with other strands.
     */
    private int findProblemStrand(String dnaStrands) {
        String[] strands = dnaStrands.split("\\+");
        int maxBondCount = 0;
        int index = -1;
        for(int i=0; i<strands.length; i++) {
            int count = 0;
            for(int j=0; j<strands[i].length(); j++) {
                char temp = strands[i].charAt(j);
                if(temp == '(' || temp == ')') count++;
            }
            if (count > maxBondCount) {
                maxBondCount = count;
                index = i;
            }
        }
        return index;
    }

    /**Finds the index of a nucleotide that has hybridized with another strand.
     * 
     * @param structure Structure in dot-parens form of multiple strands together
     * @param index Index representing which strand in the structure
     * @return Index of randomly chosen defective nucleotide in the selected strand
     */
    private int getDefectiveNucleotide(String structure, int index) {
        //Split structure into strands
        String[] strands = structure.split("\\+");
        
        //Make list of indicies of the hybridized nucleotides
        ArrayList<Integer> nucleotideIndicies = new ArrayList<>();
        for(int i=0; i<strands[index].length(); i++){
            char nucleotide = strands[index].charAt(i);
            if(nucleotide == '(' || nucleotide == ')')
                nucleotideIndicies.add(i);
        }
        
        //Get a random index from the list and return it.
        return nucleotideIndicies.get(random.nextInt(nucleotideIndicies.size()));
    }

    /**Checks that the list of strands contains no duplicate strands.
     * 
     * @param strands List of strands to be checked for uniqueness.
     * @return Unique or not unique.
     */
    private int getDuplicateIndex(ArrayList<DNAStrand> strands) {
        int duplicateStrand = -1;
        
        for(int i=0; i<strands.size()-1; i++) {
            for(int j=i+1; j<strands.size(); j++) {
                if(strands.get(i).toString().equals(strands.get(j).toString()))
                    duplicateStrand = i;
            }
        }
        
        return duplicateStrand;
    }

    private int findNumDefects(String structure) {
        String[] strands = structure.split("\\+");
        int bondCount = 0;
        for(int i=0; i<strands.length; i++) {
            int count = 0;
            for(int j=0; j<strands[i].length(); j++) {
                char temp = strands[i].charAt(j);
                if(temp == '(' || temp == ')') count++;
            }
            bondCount += count;
//            if (count > bondCount) {
//                bondCount = count;
//            }
        }
        return bondCount;
    }
    
    private ArrayList<DNAStrand> cloneList(ArrayList<DNAStrand> list) {
        ArrayList<DNAStrand> copy = new ArrayList<>();
        for (DNAStrand listItem : list) {
            copy.add(listItem.clone());
        }
        return copy;
    }
}
