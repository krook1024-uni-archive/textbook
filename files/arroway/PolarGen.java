class PolarGen {

    boolean stored = false;
    double val;

    public PolarGen() {
        stored = false;
    }

    public double next() {
        if ( ! stored) {
            double u1, u2, v1, v2, w;
            do {
                u1 = Math.random();
                u2 = Math.random();
                v1 = 2 * u1 - 1;
                v2 = 2 * u2 - 1;
                w = v1 * v1 + v2 * v2;
            } while (w > 1);
            double r = Math.sqrt((-2 * Math.log(w)) / w);
            val = r * v2;
            stored = ! stored;
            return r * v1;
        } else {
            stored = ! stored;
            return val;
        }
    }

    public static void main(String[] args) {
        PolarGen g = new PolarGen();

        for (int i=0; i < 10; i++)
            System.out.println(g.next());
    }
}

