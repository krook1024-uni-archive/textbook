public class RSA2 {
    static class KulcsPar {
        java.math.BigInteger d, e, m;

        // Allitsuk elo a nyilvanos es a publikus kulcsokat RSA algoritmussal
        public KulcsPar() {
            int meretBitekben = 700 * (int)
                (java.lang.Math.log((double) 10) /
                 java.lang.Math.log((double) 2));

            java.math.BigInteger p = new
                java.math.BigInteger(meretBitekben, 100, new
                        java.util.Random());

            java.math.BigInteger q = new
                java.math.BigInteger(meretBitekben, 100, new
                        java.util.Random());

            m = p.multiply(q);

            java.math.BigInteger z =
                p.subtract(java.math.BigInteger.ONE)
                .multiply(q.subtract(java.math.BigInteger.ONE));

            do {
                do {
                    d = new
                        java.math.BigInteger(meretBitekben,
                                new java.util.Random());
                } while (d.equals(java.math.BigInteger.ONE));
            } while ( ! z.gcd(d).equals(java.math.BigInteger.ONE));

            e = d.modInverse(z);
        }
    }

    public static void main(String[] args){
        KulcsPar jSzereplo = new KulcsPar();

        String tisztaSzoveg =
            "I'd just like to interject for a moment.  What you're referring to as Linux, " +
            "is in fact, GNU/Linux, or as I've recently taken to calling it, GNU plus Linux. " +
            "Linux is not an operating system unto itself, but rather another free component " +
            "of a fully functioning GNU system made useful by the GNU corelibs, shell " +
            "utilities and vital system components comprising a full OS as defined by POSIX. " +
            "\n" +
            "Many computer users run a modified version of the GNU system every day, " +
            "without realizing it.  Through a peculiar turn of events, the version of GNU " +
            "which is widely used today is often called \"Linux\", and many of its users are " +
            "not aware that it is basically the GNU system, developed by the GNU Project. " +
            "\n" +
            "There really is a Linux, and these people are using it, but it is just a " +
            "part of the system they use.  Linux is the kernel: the program in the system " +
            "that allocates the machine's resources to the other programs that you run. " +
            "The kernel is an essential part of an operating system, but useless by itself; " +
            "it can only function in the context of a complete operating system.  Linux is " +
            "normally used in combination with the GNU operating system: the whole system " +
            "is basically GNU with Linux added, or GNU/Linux.  All the so-called \"Linux\" " +
            "distributions are really distributions of GNU/Linux.";


        byte[] buffer = tisztaSzoveg.getBytes();
        java.math.BigInteger[] titkos = new java.math.BigInteger[buffer.length];

        // Konvertaljuk at a szoveget egy nagy szamma
        for(int i = 0; i < titkos.length; i++)
        {
            titkos[i] = new java.math.BigInteger(new byte[]{buffer[i]});
            titkos[i] = titkos[i].modPow(jSzereplo.e, jSzereplo.m);
        }

        // Fejtsuk vissza a szoveget, csak eppen a e^m helyett d^m
        for(int i = 0; i < titkos.length; i++)
        {
            titkos[i] = titkos[i].modPow(jSzereplo.d, jSzereplo.m);
            buffer[i] = titkos[i].byteValue();
        }

        // Kiirjuk az eredmenyt
        System.out.println(new String(buffer));
    }
}
