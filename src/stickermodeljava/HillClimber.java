/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

import java.util.ArrayList;

/**
 *
 * @author Jared
 */
public class HillClimber {
    private final Sat sat;
    private String lastRunExitCriteria;
    private int lastRunTime;
    private double lastRunCombinationProbability;
    
    public HillClimber(Sat sat){
        this.sat = sat;
        lastRunExitCriteria = "Never been run.";
        lastRunTime = -1;
        lastRunCombinationProbability = -1.0;
    }
    
    public ArrayList<DNAStrand> run(int timeLimitSeconds, double minCombinationProbability, int strandSize) {
        double combinationProbability = 1.0;
        //Create initial DNA strand list
        int number = sat.getNumUniqueVariables() * 2;  //Need a strand for true and false states of each variable.
        ArrayList<DNAStrand> strands = new ArrayList<>();
        for(int i = 0; i<number; i++){
            strands.add(new DNAStrand(strandSize));
        }
        
        //Test list
        
        
        //Loop
        long start = java.time.Instant.now().getEpochSecond();
        long time=0;
        boolean timedOut = false;
        while(time <= timeLimitSeconds && combinationProbability > minCombinationProbability) {
            //Modify the problem areas
            //Test again
            //Randomly (with decaying probability) replace a whole DNA strand with a random strand.  This is the annealing part.
            //Test again
        
            //Exit when timeLimitSeconds is hit or minCombinationProbability is hit
            time = java.time.Instant.now().getEpochSecond() - start;
        }
        //Record run stats
        lastRunTime = (int)time;
        lastRunCombinationProbability = combinationProbability;
        if(timedOut){
            lastRunExitCriteria = "Run timed out.";
        } else {
            lastRunExitCriteria = "Threshold combination probability met.";
        }
        return strands;
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
}
