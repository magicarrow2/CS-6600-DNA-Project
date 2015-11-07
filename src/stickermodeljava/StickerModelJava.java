/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

import java.io.IOException;
import java.util.HashSet;
/**
 *
 * @author jared
 */
public class StickerModelJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Display the help information if in arguments
        if (args.length > 0 && args[0].contains("-h")) {
            ConsoleIO.displayHelp();
            System.exit(0);
        } 
        System.out.print("Executing from: " + System.getProperty("user.dir")+"\n");
        try {
           /* Get the 3-SAT string from the user by input argument file name,
              typed in file name, or typed in string from the user.
            */
            Sat sat;
            if (args.length > 0) {
                try {
                    System.out.print("Opening file: " + args[0] + "\n");
                    sat = ConsoleIO.get3SatFromInputFile(args[0]);
                } catch (IOException e) {
                    System.out.print("File not found\n");
                    sat = ConsoleIO.get3SatFromString();
                }
            } else {  //With no arguments, ask for the input from the user
                try {
                    sat = ConsoleIO.get3SatFromInputFile();
                } catch (IOException e) {
                    System.out.print("File not found\n");
                    sat = ConsoleIO.get3SatFromString();
                }
            }
            
            //Debug: display 3-SAT information
            System.out.print(sat.toString() + "\n");
            System.out.print("Num variables " + sat.getNumUniqueVariables() + "\n");
            System.out.print("Variables: \n");
            HashSet<String> set = sat.getVariableList();
            set.forEach((var) -> {System.out.print(var + " ");});
            System.out.print ("\n\n");
            
           /* Generate initial state of DNA strands.  Make twice as many strands
              as there are unique variables in the 3-SAT problem.
            */
            
            // Perform search for unique strands that are straight
            StrandTest tester = new StrandTest();
            double prob = tester.getStraightnessProbability(new DNAStrand(15));
            System.out.print("Probability: " + prob + "\n");
             
            /* Check to see if they will combine with other strands.  If so,
               then go back to searching for unique straight strands on the ones
               that stick.
            */
            
            
        } catch (IOException e) {
            System.out.print(e.getMessage());
            System.exit(-1);
        }
        
    }
    
}
