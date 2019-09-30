/*
 * MandelbrotIterációk.java
 *
 * DIGIT 2005, Javat tanítok
 * Bátfai Norbert, nbatfai@inf.unideb.hu
 *
 */
/**
 * A nagyított Mandelbrot halmazok adott pontjában képes
 * nyomonkövetni az z_{n+1} = z_n * z_n + c iterációt.
 *
 * @author Bátfai Norbert, nbatfai@inf.unideb.hu
 * @version 0.0.1
 */
public class MandelbrotIterációk implements Runnable{
    /** Mennyi idõt várakozzunk két iteráció bemutatása között? */
    private int várakozás;
    // Kissé igaz redundánsan, s nem szépen, de kényelmesen:
    private MandelbrotHalmazNagyító nagyító;
    private int j, k;
    private double a, b, c, d;
    private  int szélesség, magasság;
    private java.awt.image.BufferedImage kép;
    /**
     * Létrehoz egy iterációkat vizsgáló <code>MandelbrotIterációk</code>
     * szál objektumot egy adott <code>MandelbrotHalmaznagyító</code>
     * objektumhoz.
     *
     * @param      nagyító      egy <code>MandelbrotHalmazNagyító</code> objektum
     * @param      várakozás    várakozási idõ
     */
    public MandelbrotIterációk(MandelbrotHalmazNagyító nagyító, int várakozás) {        
        this.nagyító = nagyító;
        this.várakozás = várakozás;
        j = nagyító.getY();
        k = nagyító.getX();
        a = nagyító.getA();
        b = nagyító.getB();
        c = nagyító.getC();
        d = nagyító.getD();
        kép = nagyító.kép();
        szélesség  = nagyító.getSz();
        magasság = nagyító.getM();
    }
    /** Az vizsgált pontból induló iterációk bemutatása. */
    public void run() {
        /* Az alábbi kód javarészt a MandelbrotHalmaz.java számolást 
         végzõ run() módszerébõl származik, hiszen ugyanazt csináljuk,
         csak most nem a hálón megyünk végig, hanem a háló adott a
         példányosításunkkor az egérmutató mutatta csomópontjában (ennek
         felel meg a c kompelx szám) számolunk, tehát a két külsõ for 
         ciklus nem kell. */
        // A [a,b]x[c,d] tartományon milyen sûrû a
        // megadott szélesség, magasság háló:
        double dx = (b-a)/szélesség;
        double dy = (d-c)/magasság;
        double reC, imC, reZ, imZ, ujreZ, ujimZ;
        // Hány iterációt csináltunk?
        int iteráció = 0;
        // c = (reC, imC) a háló rácspontjainak
        // megfelelõ komplex szám
        reC = a+k*dx;
        imC = d-j*dy;
        // z_0 = 0 = (reZ, imZ)
        reZ = 0;
        imZ = 0;
        iteráció = 0;
        // z_{n+1} = z_n * z_n + c iterációk
        // számítása, amíg |z_n| < 2 vagy még
        // nem értük el a 255 iterációt, ha
        // viszont elértük, akkor úgy vesszük,
        // hogy a kiinduláci c komplex számra
        // az iteráció konvergens, azaz a c a
        // Mandelbrot halmaz eleme
        while(reZ*reZ + imZ*imZ < 4 && iteráció < 255) {
            // z_{n+1} = z_n * z_n + c
            ujreZ = reZ*reZ - imZ*imZ + reC;
            ujimZ = 2*reZ*imZ + imC;
         
            // az iteráció (reZ, imZ) -> (ujreZ, ujimZ)
            // ezt az egyenest kell kirajzolnunk, de most
            // a komplex számokat vissza kell transzformálnunk
            // a rács oszlop, sor koordinátájává:
            java.awt.Graphics g = kép.getGraphics();
            g.setColor(java.awt.Color.WHITE);
            g.drawLine(
                    (int)((reZ - a)/dx),
                    (int)((d - imZ)/dy),
                    (int)((ujreZ - a)/dx),
                    (int)((d - ujimZ)/dy)
                    );
            g.dispose();
            nagyító.repaint();
            
            reZ = ujreZ;
            imZ = ujimZ;
            
            ++iteráció;
            // Várakozunk, hogy közben csodálhassuk az iteráció
            // látványát:
            try {
                Thread.sleep(várakozás);
            } catch (InterruptedException e) {}
        }
    }    
}                    
