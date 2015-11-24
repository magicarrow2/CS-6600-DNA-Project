/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;

/**This class handles the functions related to getting SAT info.
 *
 * @author jared
 */
public class SATHandler {
    public static Sat get3SatFromInputFile() throws IOException {
        
        //Get filename of 3-SAT problem.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter path or filename of 3-SAT input\n");
        String inputFilename = br.readLine();
        
        return get3SatFromInputFile(inputFilename); 
    }
    
    public static Sat get3SatFromInputFile(String inputFilename) throws IOException {
        //Validate file presence
        File inputFile = new File(inputFilename);
        if(!inputFile.exists()) 
            throw new IOException("3-SAT input file does not exist\n");

        //Read string from file
        BufferedReader in = new BufferedReader(new FileReader(inputFile));
        String raw3Sat;
        raw3Sat = in.readLine();

        //Parse and return
        return parse3Sat(raw3Sat);  
    }
    
    public static Sat get3SatFromString() throws IOException {
        //Get 3-SAT problem.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter 3-SAT equation\n");
        String raw3Sat = br.readLine();
        
        //Parse and return
        return parse3Sat(raw3Sat);
    }
    
    private static Sat parse3Sat(String raw3Sat) throws IOException {
        //Parse a string of characters into a Sat object.
        //Validate string format.
        String[] clauses = raw3Sat.split("\\^");
        if(clauses.length == 1)
            throw new IOException("Problem not in CNF format or has arbitrary answer.\n");
        return new Sat(clauses);
    }
}
