public class PolárGenerátor {
    boolean nincsTárolt = true;
    double tárolt;
    public PolárGenerátor() {

        nincsTárolt = true;

    }
    public double következő() {
        if(nincsTárolt) {
            double u1, u2, v1, v2, w;
            do {
                u1 = Math.random();
                u2 = Math.random();

                v1 = 2*u1 - 1;
                v2 = 2*u2 - 1;

                w = v1*v1 + v2*v2;

            } while(w > 1);

            double r = Math.sqrt((-2*Math.log(w))/w);

            tárolt = r*v2;
            nincsTárolt = !nincsTárolt;

            return r*v1;

        } else {
            nincsTárolt = !nincsTárolt;
            return tárolt;
        }
    }

    public static void main(String[] args) {
        PolárGenerátor g = new PolárGenerátor();
        for(int i=0; i<10; ++i)
            System.out.println(g.következő());
    }
}
