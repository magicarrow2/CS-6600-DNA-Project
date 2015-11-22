/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
            
            //Run HillClimber to get a set of acceptable strands
            HillClimber climber = new HillClimber(sat);
            ArrayList<DNAStrand> strands = climber.run(10, 0.85, 15);
                    
            /*
            // Generate initial state of DNA strands.  Make twice as many strands
            //  as there are unique variables in the 3-SAT problem.
            //
            ArrayList<DNAStrand> strands = new ArrayList<>();
            int count = sat.getNumUniqueVariables() * 2;
            for(int i = 0; i<count; i++) {
                strands.add(new DNAStrand(15));
            }
            
            // Perform search for unique strands that are straight
            StrandTest tester = new StrandTest();
            double prob = tester.getStraightnessProbability(strands.get(0));
            System.out.print("Probability: " + prob + "\n");
             
            // Check to see if they will combine with other strands.  If so,
            // then go back to searching for unique straight strands on the ones
            // that stick.
            //
            String structure = tester.getSecondaryStructure(strands);
            System.out.print("Structure: " + structure + "\n");
            */
            
            //Write the results to the screen
            System.out.print("\nNumber of iterations: " + climber.getLastNumIterations() + "\n");
            System.out.print("Probability of straightness and not combining: " + climber.getLastRunCombinationProbablility() + "\n");
            System.out.print("Strands:\n");
            for(DNAStrand strand : strands) {
                System.out.print(strand.toString() + "\n");
            }
            
            //Write the results to a file
            FileWriter outFile;
            if(args.length >= 2)
                outFile = new FileWriter(args[1]);
            else
                outFile = new FileWriter("Results.txt");
            BufferedWriter out = new BufferedWriter(outFile);
            out.write("Probability of straightness and not combining: " + climber.getLastRunCombinationProbablility() + "\n");
            out.write("Strands:\n");
            for(DNAStrand strand : strands) {
                out.write(strand.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.print(e.getMessage());
            System.exit(-1);
        }
        
    }
    
}
