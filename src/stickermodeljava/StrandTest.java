/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jared
 */
public class StrandTest {
    private final String bin;
    private final String home;
    private final String testFileName = "TempTestFile";
    
    public StrandTest() {
        bin = System.getProperty("user.dir") + "/nupack3.0.4/bin";
        home = System.getProperty("user.dir") + "/nupack3.0.4";
    }
    
    public TestResults runTests(List<DNAStrand> strands) throws IOException {
        //Find the secondary structure
        writeInputFile(strands, testFileName);
        ProcessBuilder proc = new ProcessBuilder("./mfe", "-material", "dna", "-multi", testFileName);
        proc.environment().put("NUPACKHOME", home);
        proc.directory(new File(bin));
        Process p = proc.start();
        try {
            p.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(StrandTest.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("Secondary structure query interrupted.\n");
        }
        
        //Parse the secondary structure information from the file
        FileReader inStream = new FileReader(bin + "/" + testFileName + ".mfe");
        BufferedReader in = new BufferedReader(inStream);
        String structureString;
        double minFreeEnergy;
        try {
            //Skip header stuff
            while(!in.readLine().startsWith("% %%%%%%%%"));
            in.readLine();
            
            //Get minimum free energy
            BigDecimal dec = new BigDecimal(in.readLine());
            minFreeEnergy = dec.doubleValue();
            
            //Get MFE structure
            structureString = in.readLine();
        } 
        //Catch disjoint strands case (meaning algorithm succeeded in finding non-combining strands)
        catch (NullPointerException e) { 
            StringBuilder str = new StringBuilder();
                for (int i=0; i<strands.size(); i++) {
                    for (int j=0; j<strands.get(0).size(); j++) {
                        str.append(".");
                    }
                    str.append("+");
                }
                str.deleteCharAt(str.length()-1);  //Delete extraneous "+" at end of structure
                structureString = str.toString();
                minFreeEnergy = 0.0;
        }
        
        //Return the results
        TestResults results = new TestResults(structureString, minFreeEnergy);
        return results;
    }
    
    private void writeInputFile(List<DNAStrand> strands, String FilenameStem) throws IOException {
        //Put input strands in a file
        BufferedWriter out = null;
        try
        {
            FileWriter fstream = new FileWriter(bin + "/" + FilenameStem + ".in");
            out = new BufferedWriter(fstream);
            
            //Write test file
            out.write(strands.size() + "\n"); //Number of strands
            for(int i=strands.size()-1; i>=0; i--) { //Strands themselves
                out.write(strands.get(i).toString() + "\n");
            }
            
            for(int i=strands.size(); i>0; i--) {
                out.write(i + " ");  //Number designator of each strand (no indistinguishable strands)
            }
            out.write("\n");
            
            //Write the structure to be tested against
            for(int i=0; i<strands.get(0).size(); i++){
                out.write(".");  //Write first structure
            }
            for(int i=1; i<strands.size(); i++) {
                out.write("+"); //Write separator
                for(int j=0; j<strands.get(i).size(); j++) {
                    out.write(".");  //Write next structure
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if(out != null){
                out.close();
            }
        }
    }
}
