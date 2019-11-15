package com.krook1024;

public class LZWBinFa {
    private Csomopont fa = null;
    private int melyseg, atlagosszeg, atlagdb;
    private double szorasosszeg;
    private final Csomopont gyoker = new Csomopont('/');

    int maxMelyseg;
    double atlag, szoras;

    public LZWBinFa() {
        fa = gyoker;
    }

    public void addBit(char b) {
        if (b == '0') {
            if (null == fa.nullasGyermek()) {
                fa.ujNullasGyermek(new Csomopont('0'));
                fa = gyoker;
            } else {
                fa = fa.nullasGyermek();
            }
        } else {
            if (null == fa.egyesGyermek()) {
                fa.ujEgyesGyermek(new Csomopont('1'));
                fa = gyoker;
            } else {
                fa = fa.egyesGyermek();
            }
        }
    }

    public int getMelyseg() {
        melyseg = maxMelyseg = 0;
        rmelyseg(gyoker);
        return maxMelyseg - 1;
    }

    public double getAtlag() {
        melyseg = atlagosszeg = atlagdb = 0;
        ratlag(gyoker);
        atlag = ((double) atlagosszeg / atlagdb);
        return atlag;
    }

    public double getSzoras() {
        atlag = getAtlag();
        szorasosszeg = 0.0;
        melyseg = atlagdb = 0;
        rszoras(gyoker);

        return (atlagdb - 1 > 0) ? Math.sqrt(szorasosszeg / (atlagdb - 1))
                : Math.sqrt(szorasosszeg);
    }

    private void rmelyseg(Csomopont elem) {
        if (elem != null) {
            ++melyseg;
            if (melyseg > maxMelyseg) {
                maxMelyseg = melyseg;
            }

            rmelyseg(elem.egyesGyermek());
            rmelyseg(elem.nullasGyermek());
            --melyseg;
        }
    }

    private void ratlag(Csomopont elem) {
        if (elem != null) {
            ++melyseg;
            ratlag(elem.egyesGyermek());
            ratlag(elem.nullasGyermek());
            --melyseg;

            if (elem.egyesGyermek() == null && elem.nullasGyermek() == null) {
                ++atlagdb;
                atlagosszeg += melyseg;
            }
        }
    }

    private void rszoras(Csomopont elem) {
        if (elem != null) {
            ++melyseg;
            rszoras(elem.egyesGyermek());
            rszoras(elem.nullasGyermek());
            --melyseg;

            if (elem.egyesGyermek() == null && elem.nullasGyermek() == null) {
                ++atlagdb;
                szorasosszeg += Math.pow((melyseg - atlag), 2);
            }
        }
    }

    static class Csomopont {
        private Csomopont balNulla = null;
        private Csomopont jobbEgy = null;

        Csomopont(char b) {
            balNulla = null;
            jobbEgy = null;
        }

        Csomopont nullasGyermek() {
            return balNulla;
        }

        Csomopont egyesGyermek() {
            return jobbEgy;
        }

        void ujNullasGyermek(Csomopont gy) {
            balNulla = gy;
        }

        void ujEgyesGyermek(Csomopont gy) {
            jobbEgy = gy;
        }

    }

    ;

    public static void main(String[] args) {
        LZWBinFa binfa = new LZWBinFa();
        String in = "01111001001001000111";
        for (Character i : in.toCharArray()) {
            binfa.addBit(i);
        }
        System.out.println("in = " + in);
        System.out.println("melyseg = " + binfa.getMelyseg());
        System.out.println("atlag = " + binfa.getAtlag());
        System.out.println("szoras = " + binfa.getSzoras());
    }
}
