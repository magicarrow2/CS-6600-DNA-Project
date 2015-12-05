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
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import stickermodeljava.DNAStrand;
import stickermodeljava.HillClimber;
import stickermodeljava.SATHandler;
import stickermodeljava.Sat;
import stickermodeljava.TestResults;

/**
 *
 * @author Jared
 */
public class StickerModelExecutor extends SwingWorker<String,String> implements PropertyChangeListener{
    private final String inputFile;
    private final String outputFile;
    private final int timeLimit;
    private final double probabilityThreshold = 0.85;
    private final int strandLength = 5;
    private final JTextArea textbox;

    public StickerModelExecutor(String inputFile, String outputFile, int timeLimit, JTextArea outputTextbox) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.timeLimit = timeLimit;
        this.textbox = outputTextbox;
    }
    
    @Override
    protected String doInBackground() throws Exception {
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
            HillClimber climber = new HillClimber(sat);
            climber.addPropertyChangeListener(this);
            ArrayList<DNAStrand> strands;
            strands = climber.run(timeLimit, probabilityThreshold, strandLength);

            //Write the results to a file
            FileWriter outFile;
            outFile = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(outFile);
            out.write("Probability of straightness and not combining: " + climber.getLastRunCombinationProbablility() + "\n");
            out.write("Strands:\n");
            for(DNAStrand strand : strands) {
                out.write(strand.toString() + "\n");
            }
            out.close();
            
            //Write the results to the screen
            System.out.print("\nNumber of iterations: " + climber.getLastNumIterations() + "\n");
            System.out.print("Probability of straightness and not combining: " + climber.getLastRunCombinationProbablility() + "\n");
            System.out.print("Strands:\n");
            for(DNAStrand strand : strands) {
                System.out.print(strand.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.print(e.getMessage());
            return "IOError";
        }
        return "Done";
    }
    
    @Override
    protected void done() {
        try {
            System.out.print(get());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
    
//    @Override
//    protected void process(List<String> chunks) {
//        textbox.setText(null);
//        for (String chunk : chunks) {
//            textbox.append(chunk);
//        }
//    }

    /**This callback function handles the event when the hill climbing algorithm
     * runs a new iteration.
     * 
     * @param evt 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PropertyChangeSupport pcs = this.getPropertyChangeSupport();
        pcs.firePropertyChange(evt);
        
//        //Set new strand structure in text box
//        if(evt.getPropertyName().equals("lastRunTestResults")) {
//            textbox.setText(null);
//            TestResults results = (TestResults)evt.getNewValue();
//            
//            //Format for output by putting strands on seperate lines
//            String[] strands = results.getSecondaryStructure().split("\\+");
//            for(String strand : strands)
//                textbox.append(strand + "\n");
//        }
    }
}
