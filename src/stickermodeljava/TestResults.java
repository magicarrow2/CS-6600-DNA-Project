/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**This class is just a data structure container that holds all of the results
 * from the tests on a solution done with the NuPack software.
 *
 * @author Jared
 */
public class TestResults {
    private double overallNoncombiningProbability;
    private ArrayList<Double> straightnessProbability;
    private String secondaryStructure;
    //private HashMap<Integer,Integer[]> combinations;

    public TestResults(double overallNoncombiningProbability, ArrayList<Double> straightnessProbability, String secondaryStructure) {
        this.overallNoncombiningProbability = overallNoncombiningProbability;
        this.straightnessProbability = straightnessProbability;
        this.secondaryStructure = secondaryStructure;
        //this.combinations = combinations;
    }
    
    public double getOverallNoncombiningProbability() {
        return overallNoncombiningProbability;
    }

    public ArrayList<Double> getStraightnessProbability() {
        return straightnessProbability;
    }

    public String getSecondaryStructure() {
        return secondaryStructure;
    }

//    public HashMap<Integer, Integer[]> getCombinations() {
//        return combinations;
//    }
}
