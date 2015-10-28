/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
/**
 *
 * @author jared
 */
public class Sat {
    //Data
    private int numUniqueVariables;
    private HashSet<String> variableNames;
    private ArrayList<SatClause> clauses;
    
    public Sat(String[] clauses) throws IOException {
        //Store clauses
        variableNames = new HashSet();
        this.clauses = new ArrayList<>();
        for (String clause : clauses) {
            //Do some string trimming to get just the variable names
            String T1 = clause.replace('(', ' ');
            String T2 = T1.replace(')', ' ');
            String[] T3 = T2.split("v");
            if (T3.length != 3)
                throw new IOException("Problem not in CNF format.\n");
            this.clauses.add(new SatClause(T3[0].trim(),T3[1].trim(),T3[2].trim()));
            
            //Work on count
            variableNames.add(T3[0].replace('~',' ').trim());
            variableNames.add(T3[1].replace('~',' ').trim());
            variableNames.add(T3[2].replace('~',' ').trim());
        }
        
        //Count number of unique variables and store value
       numUniqueVariables = variableNames.size();
    }
    
    public int getNumUniqueVariables() {
        return numUniqueVariables;
    }
    
    public HashSet<String> getVariableList() {
        return (HashSet<String>) variableNames.clone();
    }
    
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (SatClause clause : clauses) {
            String T1 = " ^ ("+clause.v1+" v "+clause.v2+" v "+clause.v3+")";
            str.append(T1);
        }
        str.delete(0, 3);
        return str.toString();
    }
    
    /**This inner class is a triplet that represents a 3-SAT clause
     * 
     */
    private class SatClause {
        public String v1;
        public String v2;
        public String v3;
        
        public SatClause() {}
        public SatClause(String v1, String v2, String v3){
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }
    }
}
