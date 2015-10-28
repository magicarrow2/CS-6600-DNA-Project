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
        if (args.length > 0 && args[0].contains("-h")) {
            ConsoleIO.displayHelp();
            System.exit(0);
        } 
        System.out.print("Executing from: " + System.getenv("PWD")+"\n");
        try {
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
            System.out.print(sat.toString() + "\n");
            System.out.print("Num variables " + sat.getNumUniqueVariables() + "\n");
            System.out.print("Variables: \n");
            HashSet<String> set = sat.getVariableList();
            set.forEach((var) -> {System.out.print(var + " ");});
            System.out.print ("\n\n");
        } catch (IOException e) {
            System.out.print(e.getMessage());
            System.exit(-1);
        }
        
    }
    
}
