/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

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
    private String lastRunExitCriteria;
    private int lastRunTime;
    private double lastRunCombinationProbability;
    private TestResults lastRunTestResults;
    private int lastNumIterations;

    public HillClimber(Sat sat){
        this.sat = sat;
        lastRunExitCriteria = "Never been run.";
        lastRunTime = -1;
        lastRunCombinationProbability = -1.0;
        lastNumIterations = 0;
    }
    
    public ArrayList<DNAStrand> run(int timeLimitSeconds, double minNonComboProbability, int strandSize) throws IOException {
        double NonComboProbability = 0.0;
        //Create initial DNA strand list
        int number = sat.getNumUniqueVariables() * 2;  //Need a strand for true and false states of each variable.
        ArrayList<DNAStrand> strands = new ArrayList<>();
        for(int i = 0; i<number; i++){
            strands.add(new DNAStrand(strandSize));
        }
        
        //Test list
        StrandTest tester = new StrandTest();
        lastRunTestResults = tester.runAllTests(strands);
        NonComboProbability = lastRunTestResults.getOverallNoncombiningProbability();
        
        //Climb the hill with annealing
        long start = java.time.Instant.now().getEpochSecond();
        long time=0;
        double annealingProbability = 0.2;
        double temperature;
        double s;
        double s_prime;
        double delta_s;
        boolean useNewStrand;
        //Random rand = new Random();
        Random rand = new Random(System.currentTimeMillis());
        boolean timedOut = false;
        
        lastNumIterations = 1;
        while(time <= timeLimitSeconds && NonComboProbability < minNonComboProbability) {
            //Figure out which strand is the most problematic
            String structure = lastRunTestResults.getSecondaryStructure();
            int index = findProblemStrand(structure);
            
            //Randomly (with decaying probability) replace a whole DNA strand with a 
            //  random strand or fix a part of a strand.  This is the annealing part.
            useNewStrand = rand.nextDouble() <= annealingProbability;
            if(useNewStrand) {
                strands.set(index, new DNAStrand(strandSize));
            } else {
                DNAStrand strand = strands.get(index);
                int nucleotideNum = getDefectiveNucleotide(structure, index);
                if (nucleotideNum > 0) strand.newNucleotide(nucleotideNum);
                strands.set(index, strand);
            }
            
            //Test again and fix new temperature and annealing probability
            lastRunTestResults = tester.runAllTests(strands);
            s = NonComboProbability;
            NonComboProbability = lastRunTestResults.getOverallNoncombiningProbability();
            s_prime = NonComboProbability;
            delta_s = s - s_prime;
            temperature = (-delta_s)/log(annealingProbability);
            annealingProbability = Math.pow(Math.E, (-delta_s)/temperature);
        
            //Exit when timeLimitSeconds is hit or minNonComboProbability is hit
            time = java.time.Instant.now().getEpochSecond() - start;
            if(time > timeLimitSeconds) timedOut = true;
            lastNumIterations++;
        }
        
        //Record run stats
        lastRunTime = (int)time;
        lastRunCombinationProbability = NonComboProbability;
        if(timedOut){
            lastRunExitCriteria = "Run timed out.";
        } else {
            lastRunExitCriteria = "Threshold combination probability met.";
        }
        return strands;
    }
    
    public TestResults getLastRunTestResults() {
        return lastRunTestResults;
    }
    
    public int getLastRunTime() {
        return lastRunTime;
    }
    
    public double getLastRunCombinationProbablility() {
        return lastRunCombinationProbability;
    }
    
    public String getLastRunExitCriteria() {
        return lastRunExitCriteria;
    }

    public int getLastNumIterations() {
        return lastNumIterations;
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
            String temp = strands[i].replaceAll("\\.", "");
            if (temp.length() > maxBondCount) index = i;
        }
        return index;
    }

    /**Finds the index of a nucleotide that has hybridized with another strand.
     * 
     * @param structure
     * @param index
     * @return 
     */
    private int getDefectiveNucleotide(String structure, int index) {
        String[] strands = structure.split("\\+");
        int nucleotideNum = strands[index].indexOf('(');
        if(nucleotideNum < 0) nucleotideNum = strands[index].indexOf(')');
        return nucleotideNum;
    }
}
