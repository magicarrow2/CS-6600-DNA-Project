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
import java.util.ArrayList;
import java.util.HashMap;
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
    private final String singleStrandTestFilename = "SST";
    private final String combinationTestFilename = "CT";
    
    public StrandTest() {
        bin = System.getProperty("user.dir") + "/nupack3.0.4/bin";
        home = System.getProperty("user.dir") + "/nupack3.0.4";
    }
    
    public TestResults runAllTests(List<DNAStrand> strands) throws IOException {
        double overallNoncombiningProbability;
        ArrayList<Double> straightnessProbability = new ArrayList<>();
        String secondaryStructure;
        
        overallNoncombiningProbability = getCombinationProbability(strands);
        secondaryStructure = getSecondaryStructure(strands);
        for(DNAStrand strand : strands) {
            straightnessProbability.add(getStraightnessProbability(strand));
        }
        
        TestResults results = new TestResults(overallNoncombiningProbability, straightnessProbability, secondaryStructure);
        return results;
    }
    
    /**Determines the probability of getting a straight strand
     * 
     * @param strand Single strand to test for straightness
     * @return
     * @throws IOException 
     */
    public double getStraightnessProbability(DNAStrand strand) throws IOException {
        //Put input strand in a file
        BufferedWriter out = null;
        try
        {
            FileWriter fstream = new FileWriter(bin + "/" + singleStrandTestFilename + ".in");
            out = new BufferedWriter(fstream);
            
            //Use indistinguishable strand to make sure it won't react with itself?
            //Write out single strand in -multi option format
            out.write("1\n"); //Number of strands
            out.write(strand.toString()); //Strand to be tested
            out.write("\n");
            out.write("1\n"); //Strand numberical identifier
            for(int i=0; i<strand.size(); i++){
                out.write(".");  //Desired strand secondary structure
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if(out != null){
                out.close();
            }
        }
        
        //Get the probability
        return runProbabilityTest(singleStrandTestFilename);
    }
    
    /**Determines the probability of strands combining with each other.
     * 
     * @param strands
     * @return
     * @throws IOException 
     */
    public double getCombinationProbability(List<DNAStrand> strands) throws IOException {
        //Write to file
        writeInputFile(strands, combinationTestFilename);
        
        //Run probability test and return results
        return runProbabilityTest(combinationTestFilename);
    }
    
    /**Takes a ".in" test file and executes the "prob" executable from the NuPack3.0.4 package against it.
     * Uses the "-multi" and "-material dna" options in the call.
     * 
     * @param inputFilenameStem Name of the input file to be tested without the ".in" file extension.
     * @return Probability (0.0000 to 1.0000) of strands creating the structure in question.
     * @throws IOException 
     */
    private double runProbabilityTest(String inputFilenameStem) throws IOException {
        //Process the strand
        File tempFile = new File(inputFilenameStem + ".txt");
        tempFile.createNewFile();
        tempFile.setReadable(true);
        tempFile.setWritable(true);
        ProcessBuilder proc = new ProcessBuilder("./prob", "-material", "dna", "-multi",  inputFilenameStem);
        proc.environment().put("NUPACKHOME", home);
        proc.redirectOutput(tempFile);
        proc.directory(new File(bin));
        Process p = proc.start();
        try {
            p.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(StrandTest.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("Straightness probability testing interrupted.\n");
        }
        
        //Get probability from file
        FileReader inStream = new FileReader(tempFile);
        BufferedReader in = new BufferedReader(inStream);
        while(!in.readLine().contains("% Probability:"));  //Skip past header stuff
        String probString = in.readLine();
        double probability;
        if(probString.contains("nan")) {
            probability = 1.0;
        } else {
            BigDecimal dec = new BigDecimal(probString);
            probability = dec.doubleValue();
        }
        
        return probability;
    }
    
    public String getSecondaryStructure(List<DNAStrand> strands) throws IOException {
        String filename = combinationTestFilename + "mfe";
        
        //Find the secondary structure
        writeInputFile(strands, filename);
        ProcessBuilder proc = new ProcessBuilder("./mfe", "-pseudo", "-material", "dna", "-multi", filename);
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
        FileReader inStream = new FileReader(bin + "/" + filename + ".mfe");
        BufferedReader in = new BufferedReader(inStream);
        while(!in.readLine().startsWith("% %%%%%%%%"));  //Skip past header stuff
        in.readLine();
        in.readLine();
        String structureString = in.readLine();
        
        return structureString;
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
            for(DNAStrand strand : strands) { //Strands themselves
                out.write(strand.toString() + "\n");
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
