/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;


/**This class is just a data structure container that holds all of the results
 * from the tests on a solution done with the NuPack software.
 *
 * @author Jared
 */
public class TestResults {
    private final String secondaryStructure;
    private final double mfe;

    public TestResults(String secondaryStructure, double mfe) {
        this.secondaryStructure = secondaryStructure;
        this.mfe = mfe;
    }

    public String getSecondaryStructure() {
        return secondaryStructure;
    }

    public double getMFE() {
        return mfe;
    }
    
    @Override
    public TestResults clone() {
        return new TestResults(secondaryStructure, mfe);
    }
}
