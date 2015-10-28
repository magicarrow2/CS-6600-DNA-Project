/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jared
 */
public class Test {
    private final String bin = "../nupack3.0.4/bin";
    private final String singleStrandTestFilename = "SST";
    private final String combinationTestFilename = "CT";
    
    public boolean isStraightStrand(DNAStrand strand) throws IOException {
        //Put input strand in a file
        BufferedWriter out = null;
        try
        {
            FileWriter fstream = new FileWriter(bin + singleStrandTestFilename + ".in");
            out = new BufferedWriter(fstream);
            
            //Use indistinguishable strand to make sure it won't react with itself
            out.write(strand.toString());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if(out != null){
                out.close();
            }
        }
        
        //Process the strand
        ProcessBuilder proc = new ProcessBuilder("complexes", singleStrandTestFilename, "-mfe", "-degenerate");
        proc.environment().put("NUPACKHOME", "../nupack3.0.4");
        proc.directory(new File(bin));
        proc.start();
        
        //Get the output structure
        //Check for non-periods in the structure string
        return true;
    }
    
    public boolean dontCombine(List<DNAStrand> strands) throws IOException {
        //Put input strands in a file
        BufferedWriter out = null;
        try
        {
            FileWriter fstream = new FileWriter(bin + combinationTestFilename + ".in");
            out = new BufferedWriter(fstream);
            
            //Write file
            out.write(strands.size() + "\n"); //Number of strands
            for(DNAStrand strand : strands) { //Strands themselves
                out.write(strand.toString() + "\n");
            }
            
            for(int i=0; i<strands.size(); i++) {
                out.write(i + " ");  //Number designator of each strand (no indistinguishable strands)
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if(out != null){
                out.close();
            }
        }
        //Process the strands
        //Get the output complexes
        //Check for unique strands
        //Check for combination structures?
        return true;
    }
}
