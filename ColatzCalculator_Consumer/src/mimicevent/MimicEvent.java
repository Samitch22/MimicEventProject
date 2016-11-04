/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mimicevent;

import java.io.Serializable;

/**
 * This class encapsulates the concept of a Mimic Event. It stores the binary
 * N to be calculated by the Collatz calculator.
 * @author Mitchell
 */
public class MimicEvent implements Serializable {
    
    private final String binaryN;
    
    /**
     * Constructs a Mimic Event taking in a binary string N.
     * @param binaryN
     */
    public MimicEvent(String binaryN) {
        this.binaryN = binaryN;
    }

    /**
     * Gets N as a binary string.
     * @return
     */
    public String getN() {
        return binaryN;
    }

    @Override
    public String toString() {
        return "MimicEvent{" + binaryN + '}';
    }

}
