/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

/**This enumeration is used to determine the type of hill-climbing algorithm to
 * use.  
 * 
 * Vanilla refers to a regular hill-climbing algorithm that only accepts solutions
 * that improve the score.
 * 
 * Annealing refers to a hill-climbing algorithm with simulated annealing that
 * accepts solutions that improve, but also worse solutions with a decaying probability
 * of acceptance.
 * 
 * Relaxed refers to a hill-climbing algorithm that accepts all proposed solutions
 * regardless if it improves the score or not.
 *
 * @author jared
 */
public enum AlgorithmType {
    Vanilla, 
    Annealing, 
    Relaxed;
}
