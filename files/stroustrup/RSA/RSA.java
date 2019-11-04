public class RSA {
    public static void main(String[] args) {
        int meretBitekben =
            (int)
            (700 * (int) (java.lang.Math.log( (double) 10)) /
                java.lang.Math.log((double) 2));

        System.out.println("Meret bitekben: ");
        System.out.println(meretBitekben);

        java.math.BigInteger p_i =
            new java.math.BigInteger(meretBitekben, 100,
                    new java.util.Random());

        System.out.println("p_i");
        System.out.println(p_i);

        System.out.println("p_i hexa");
        System.out.println(p_i.toString(16));

        java.math.BigInteger q_i =
            new java.math.BigInteger(meretBitekben, 100,
                    new java.util.Random());

        System.out.println("q_i");
        System.out.println(q_i);

        java.math.BigInteger m_i =p_i.multiply(q_i);

        System.out.println("m_i");
        System.out.println(m_i);

        java.math.BigInteger z_i =
            p_i.subtract(java.math.BigInteger.ONE)
               .multiply(q_i.subtract(java.math.BigInteger.ONE));

        System.out.println("z_i");
        System.out.println(z_i);
        java.math.BigInteger d_i;

        do {
            do {
                d_i =
                    new java.math.BigInteger(meretBitekben,
                            new java.util.Random());
            } while(d_i.equals(java.math.BigInteger.ONE));
        } while( ! z_i.gcd(d_i).equals(java.math.BigInteger.ONE));

        System.out.println("d_i");
        System.out.println(d_i);

        java.math.BigInteger e_i = d_i.modInverse(z_i);

        System.out.println("e_i");
        System.out.println(e_i);
    }
}
