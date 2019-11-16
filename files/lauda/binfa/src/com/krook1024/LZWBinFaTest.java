package com.krook1024;

import static org.junit.jupiter.api.Assertions.*;
import com.krook1024.LZWBinFa;

class LZWBinFaTest {

    private LZWBinFa binfa = new LZWBinFa();
    private static final String testStr = "01111001001001000111";

    @org.junit.jupiter.api.Test
    void getMelyseg() {
        for (char i : testStr.toCharArray())
            binfa.addBit(i);
        double melyseg = binfa.getMelyseg();
        System.out.println("getMelyseg() teszt -- expecting: 4, got: " + melyseg);
        assertEquals(4, melyseg);
    }

    @org.junit.jupiter.api.Test
    void getAtlag() {
        for (char i : testStr.toCharArray())
            binfa.addBit(i);
        double atlag = binfa.getAtlag();
        System.out.println("getMelyseg() teszt -- expecting: 2.75, got: " + atlag);
        assertEquals(2.75, atlag);
    }

    @org.junit.jupiter.api.Test
    void getSzoras() {
        for (char i : testStr.toCharArray())
            binfa.addBit(i);
        double szoras = binfa.getSzoras();
        System.out.println("getSzoras() teszt -- expecting: 0.9574, got: " + szoras);
        assertEquals(0.9574, szoras, 0.001);
    }
}