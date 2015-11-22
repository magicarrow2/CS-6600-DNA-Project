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
        char nucleotide;
        int num;
        Random rand = new Random();
        //Random rand = new Random(System.currentTimeMillis());
        for (int i=0; i<length; i++) {
            num = rand.nextInt(3);
            switch(num) {
                case 0:
                    nucleotide = 'A';
                    break;
                case 1:
                    nucleotide = 'T';
                    break;
                default:
                    nucleotide = 'C';
                    break;
            }
            strand.add(i, nucleotide);
        }
    }
    
    /**Randomly generates a new nucleotide at the position specified.  Will not
 be the same nucleotide that was at the position before.
     * 
     * @param position Position of the nucleotide to be changed.  Numbering starts
 at zero.
     */
    public void newNucleotide(int position) throws IllegalArgumentException{
        if(position < 0 || position >= strand.size())
            throw new IllegalArgumentException("Nucleotide index out of strand array bounds.\n");
        char old = strand.get(position);
        char newChar;
        Random rand = new Random(System.currentTimeMillis());
        int num = rand.nextInt(2);
        if(old == 'A'){
            newChar = (num == 0)?('T'):('C');
        } else if (old == 'T') {
            newChar = (num == 0)?('A'):('C');
        } else { //'C'
            newChar = (num == 0)?('A'):('T');
        }
        strand.set(position, newChar);
    }
    
    /**Makes a deep copy of the DNAStrand
     * 
     * @param strand 
     */
    @Override
    public DNAStrand clone() {
        DNAStrand clone = new DNAStrand(this.strand.size());
        clone.strand.clear();
        for(char nucleotide : this.strand) {
            clone.strand.add(nucleotide);
        }
        return clone;
    }
    
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(char nucleotide : this.strand) {
            str.append(nucleotide);
        }
        return str.toString();
    }
    
    public int size() {
        return strand.size();
    }
}
