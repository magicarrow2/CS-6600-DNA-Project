/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StickerModelGui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.SwingWorker;
import stickermodeljava.DNAStrand;
import stickermodeljava.HillClimber;
import stickermodeljava.SATHandler;
import stickermodeljava.Sat;
import stickermodeljava.AlgorithmType;

/**
 *
 * @author Jared
 */
public class StickerModelExecutor extends SwingWorker<String,String> implements PropertyChangeListener{
    private final String inputFile;
    private final String outputFile;
    private final int timeLimit;
    private final double probabilityThreshold = 0.9999;
    private final int strandLength;
    private final AlgorithmType algorithmType;

    public StickerModelExecutor(String inputFile, String outputFile, int timeLimit, int strandSize, AlgorithmType algorithmType) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.timeLimit = timeLimit;
        this.strandLength = strandSize;
        this.algorithmType = algorithmType;
    }
    
    @Override
    protected String doInBackground() throws Exception {
        HillClimber climber = null;
        ArrayList<DNAStrand> strands = null;
        try {
            //Get 3-SAT equation from input file
            Sat sat = SATHandler.get3SatFromInputFile(inputFile);

            //Give feedback to user
            System.out.print(sat.toString() + "\n");
            System.out.print("Num variables " + sat.getNumUniqueVariables() + "\n");
            System.out.print("Variables: \n");
            HashSet<String> set = sat.getVariableList();
            set.forEach((var) -> {System.out.print(var + " ");});
            System.out.print ("\n\n");

            //Run HillClimber to get a set of acceptable strands
            climber = new HillClimber(sat);
            climber.addPropertyChangeListener(this);
            
            strands = climber.run(timeLimit, probabilityThreshold, strandLength, algorithmType);
            
            //Write the results to a file
            FileWriter outFile;
            outFile = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(outFile);
            out.write(sat.toString() + "\n");
            out.write("Num variables " + sat.getNumUniqueVariables() + "\n");
            out.write("Variables: \n");
            Iterator<String> itr = set.iterator();
            while(itr.hasNext()) {
                out.write(itr.next() + " ");
            }
            out.write ("\n\n");
            out.write("Number of iterations: " + climber.getLastNumIterations() + "\n");
            out.write("Completion Time: " + climber.getLastRunTime() + " seconds \n");
            out.write("Percentage of defective nucleotides: " + (1-climber.getLastRunNonDefectPercentage())*100 + "%\n");
            out.write("Minimum Free Energy (MFE): " + climber.getLastRunTestResults().getMFE() + "\n");
            out.write("Strands:\n");
            for(DNAStrand strand : strands) {
                out.write(strand.toString() + "\n");
            }
            out.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
            return "IOError\n";
        } finally {
            if(climber != null && strands != null) {
                //Write the results to the screen
                System.out.print("\nNumber of iterations: " + climber.getLastNumIterations() + "\n");
                System.out.print("Completion Time: " + climber.getLastRunTime() + " seconds \n");
                System.out.print("Percentage of defective nucleotides: " + (1-climber.getLastRunNonDefectPercentage())*100 + "%\n");
                System.out.print("Minimum Free Energy (MFE): " + climber.getLastRunTestResults().getMFE() + "\n");
                System.out.print("Strands:\n");
                for(DNAStrand strand : strands) {
                    System.out.print(strand.toString() + "\n");
                }
            }
        }
        return " ";
    }
    
    @Override
    protected void done() {
        try {
            System.out.print(get());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /**This callback function handles the event when the hill climbing algorithm
     * runs a new iteration.
     * 
     * @param evt 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PropertyChangeSupport pcs = this.getPropertyChangeSupport();
        pcs.firePropertyChange(evt);
    }
}
