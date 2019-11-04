/*
 * MandelbrotIteraciok.java
 *
 * DIGIT 2005, Javat tanitok
 * Batfai Norbert, nbatfai@inf.unideb.hu
 *
 */
/**
 * A nagyitott Mandelbrot halmazok adott pontjaban kepes
 * nyomonkovetni az z_{n+1} = z_n * z_n + c iteraciot.
 *
 * @author Batfai Norbert, nbatfai@inf.unideb.hu
 * @version 0.0.1
 */
public class MandelbrotIteraciok implements Runnable{
    /** Mennyi idot varakozzunk ket iteracio bemutatasa kozott? */
    private int varakozas;
    // Kisse igaz redundansan, s nem szepen, de kenyelmesen:
    private MandelbrotHalmazNagyito nagyito;
    private int j, k;
    private double a, b, c, d;
    private  int szelesseg, magassag;
    private java.awt.image.BufferedImage kep;
    /**
     * Letrehoz egy iteraciokat vizsgalo <code>MandelbrotIteraciok</code>
     * szal objektumot egy adott <code>MandelbrotHalmaznagyito</code>
     * objektumhoz.
     *
     * @param      nagyito      egy <code>MandelbrotHalmazNagyito</code> objektum
     * @param      varakozas    varakozasi ido
     */
    public MandelbrotIteraciok(MandelbrotHalmazNagyito nagyito, int varakozas) {        
        this.nagyito = nagyito;
        this.varakozas = varakozas;
        j = nagyito.getY();
        k = nagyito.getX();
        a = nagyito.getA();
        b = nagyito.getB();
        c = nagyito.getC();
        d = nagyito.getD();
        kep = nagyito.kep();
        szelesseg  = nagyito.getSz();
        magassag = nagyito.getM();
    }
    /** Az vizsgalt pontbol indulo iteraciok bemutatasa. */
    public void run() {
        /* Az alabbi kod javareszt a MandelbrotHalmaz.java szamolast 
         vegzo run() modszerebol szarmazik, hiszen ugyanazt csinaljuk,
         csak most nem a halon megyunk vegig, hanem a halo adott a
         peldanyositasunkkor az egermutato mutatta csomopontjaban (ennek
         felel meg a c kompelx szam) szamolunk, tehat a ket kulso for 
         ciklus nem kell. */
        // A [a,b]x[c,d] tartomanyon milyen suru a
        // megadott szelesseg, magassag halo:
        double dx = (b-a)/szelesseg;
        double dy = (d-c)/magassag;
        double reC, imC, reZ, imZ, ujreZ, ujimZ;
        // Hany iteraciot csinaltunk?
        int iteracio = 0;
        // c = (reC, imC) a halo racspontjainak
        // megfelelo komplex szam
        reC = a+k*dx;
        imC = d-j*dy;
        // z_0 = 0 = (reZ, imZ)
        reZ = 0;
        imZ = 0;
        iteracio = 0;
        // z_{n+1} = z_n * z_n + c iteraciok
        // szamitasa, amig |z_n| < 2 vagy meg
        // nem ertuk el a 255 iteraciot, ha
        // viszont elertuk, akkor ugy vesszuk,
        // hogy a kiindulaci c komplex szamra
        // az iteracio konvergens, azaz a c a
        // Mandelbrot halmaz eleme
        while(reZ*reZ + imZ*imZ < 4 && iteracio < 255) {
            // z_{n+1} = z_n * z_n + c
            ujreZ = reZ*reZ - imZ*imZ + reC;
            ujimZ = 2*reZ*imZ + imC;
         
            // az iteracio (reZ, imZ) -> (ujreZ, ujimZ)
            // ezt az egyenest kell kirajzolnunk, de most
            // a komplex szamokat vissza kell transzformalnunk
            // a racs oszlop, sor koordinatajava:
            java.awt.Graphics g = kep.getGraphics();
            g.setColor(java.awt.Color.WHITE);
            g.drawLine(
                    (int)((reZ - a)/dx),
                    (int)((d - imZ)/dy),
                    (int)((ujreZ - a)/dx),
                    (int)((d - ujimZ)/dy)
                    );
            g.dispose();
            nagyito.repaint();
            
            reZ = ujreZ;
            imZ = ujimZ;
            
            ++iteracio;
            // Varakozunk, hogy kozben csodalhassuk az iteracio
            // latvanyat:
            try {
                Thread.sleep(varakozas);
            } catch (InterruptedException e) {}
        }
    }    
}                    
