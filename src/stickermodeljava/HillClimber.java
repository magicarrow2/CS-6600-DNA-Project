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
    private String runExitCriteria;  //Contains exit criteria for the most recent run
    private int runTime;    //Contains run time of most recent run
    private double nonDefectPercentage;   //Contains non-combination probability of most recent run iteration
    private TestResults runTestResults; //Contains test results of most recent run iteration
    private int numIterations;  //Contains number of iterations in the last run
    private final Random random = new Random(System.currentTimeMillis());
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public HillClimber(Sat sat){
        this.sat = sat;
        runExitCriteria = "Never been run.";
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
    
    public ArrayList<DNAStrand> run(int timeLimitSeconds, double minNonComboProbability, int strandSize) throws IOException {
        double nonDefectPercentage;
        //Create initial DNA strand list
        int number = sat.getNumUniqueVariables() * 2;  //Need a strand for true and false states of each variable.
        ArrayList<DNAStrand> strands = new ArrayList<>();
        ArrayList<DNAStrand> oldStrands;
        for(int i = 0; i<number; i++){
            strands.add(new DNAStrand(strandSize));
        }
        oldStrands = cloneList(strands);
        
        //Test list
        StrandTest tester = new StrandTest();
        runTestResults = tester.runAllTests(strands);
        TestResults oldTestResults = runTestResults.clone();
        nonDefectPercentage = runTestResults.getOverallNoncombiningProbability();
        
        //Climb the hill with annealing
        long start = java.time.Instant.now().getEpochSecond();
        long time=0;
        //double annealingProbability = 0.2;
        //double annealingProbability = 0.8;
        double annealingProbability = 1/strands.size();
        pcs.firePropertyChange("annealingProbability", 0.0, annealingProbability);
        double temperature = 1;
        double coolingRate = 0.05;
        double s;
        double s_prime;
        double delta_s;
        boolean useNewStrand;
        boolean timedOut = false;
        
        numIterations = 1;
        while((time < timeLimitSeconds || timeLimitSeconds < 0) && nonDefectPercentage < minNonComboProbability) {
            //Figure out which strand is the most problematic
            String structure = runTestResults.getSecondaryStructure();
            int index = findProblemStrand(structure);
            if(index < 0) break;
            
            //Randomly (with decaying probability) replace a whole DNA strand with a 
            //  random strand or fix a part of a strand.  This is the annealing part.
            useNewStrand = random.nextDouble() <= annealingProbability;
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
            
            //Test again and fix new temperature and annealing probability
            //TODO: Fit this into new algorithm
            TestResults oldTest = runTestResults;
            runTestResults = tester.runAllTests(strands);
            pcs.firePropertyChange("lastRunTestResults", oldTest, runTestResults);
            
            int defects = findNumDefects(structure);
            structure = runTestResults.getSecondaryStructure();
            
            
            s = 1-((double)defects/(strandSize*strands.size()));
            defects = findNumDefects(structure);
            s_prime = 1-((double)defects/(strandSize*strands.size()));
            double oldPercentage = nonDefectPercentage;
            nonDefectPercentage = s_prime;
            pcs.firePropertyChange("nonDefectPercentage", oldPercentage, nonDefectPercentage);
            
            delta_s = s - s_prime;
            //temperature = (delta_s)/log(annealingProbability);
            temperature *= 1-coolingRate;
            double temp = Math.pow(Math.E, (-delta_s)/temperature);
            if (temp < annealingProbability) {
                double oldProb = annealingProbability;
           //     annealingProbability = temp;
                pcs.firePropertyChange("annealingProbability", oldProb, annealingProbability);
            }
        
            //Exit when timeLimitSeconds is hit or minNonComboProbability is hit
            time = java.time.Instant.now().getEpochSecond() - start;
            if(time > timeLimitSeconds && timeLimitSeconds > 0) timedOut = true;
            numIterations++;
            pcs.firePropertyChange("lastNumIterations",numIterations-1,numIterations);
            
            //Save info from last iteration
            oldStrands = cloneList(strands);
            oldTestResults = runTestResults;
        }
        
        //Record run stats
        runTime = (int)time;
        this.nonDefectPercentage = nonDefectPercentage;
        if(timedOut){
            runExitCriteria = "Run timed out.";
        } else {
            runExitCriteria = "Threshold combination probability met.";
        }
        return strands;
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
    
    public String getLastRunExitCriteria() {
        return runExitCriteria;
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
