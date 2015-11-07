/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jared
 */
public class DNAStrand {
    private ArrayList<Character> strand;
    
    /**Generates a random DNAStrand using only A, T, and C
     * 
     * @param length Length of the strand to be created
     */
    public DNAStrand(int length) {
        strand = new ArrayList<>(length);
        char nucleatide;
        int num;
        Random rand = new Random(System.currentTimeMillis());
        for (int i=0; i<length; i++) {
            num = rand.nextInt(3);
            switch(num) {
                case 0:
                    nucleatide = 'A';
                    break;
                case 1:
                    nucleatide = 'T';
                    break;
                default:
                    nucleatide = 'C';
                    break;
            }
            strand.add(nucleatide);
        }
    }
    
    /**Makes a deep copy of the DNAStrand
     * 
     * @param strand 
     */
    @Override
    public DNAStrand clone() {
        DNAStrand clone = new DNAStrand(this.strand.size());
        clone.strand.clear();
        for(char nucleatide : this.strand) {
            clone.strand.add(nucleatide);
        }
        return clone;
    }
    
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(char nucleatide : this.strand) {
            str.append(nucleatide);
        }
        return str.toString();
    }
    
    public int size() {
        return strand.size();
    }
}
