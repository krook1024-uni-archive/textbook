/*
 * MandelbrotHalmazNagyito.java
 *
 * DIGIT 2005, Javat tanitok
 * Batfai Norbert, nbatfai@inf.unideb.hu
 *
 */
/**
 * A Mandelbrot halmazt nagyito es kirajzolo osztaly.
 *
 * @author Batfai Norbert, nbatfai@inf.unideb.hu
 * @version 0.0.2
 */
public class MandelbrotHalmazNagyito extends MandelbrotHalmaz {
    /** A nagyitando kijelolt teruletet bal felso sarka. */
    private int x, y;
    /** A nagyitando kijelolt terulet szelessege es magassaga. */
    private int mx, my;
    /**
     * Letrehoz egy a Mandelbrot halmazt a komplex sik
     * [a,b]x[c,d] tartomanya felett kiszamolo es nygitani tudo
     * <code>MandelbrotHalmazNagyito</code> objektumot.
     *
     * @param      a              a [a,b]x[c,d] tartomany a koordinataja.
     * @param      b              a [a,b]x[c,d] tartomany b koordinataja.
     * @param      c              a [a,b]x[c,d] tartomany c koordinataja.
     * @param      d              a [a,b]x[c,d] tartomany d koordinataja.
     * @param      szelesseg      a halmazt tartalmazo tomb szelessege.
     * @param      iteraciosHatar a szamitas pontossaga.
     */
    public MandelbrotHalmazNagyito(double a, double b, double c, double d,
            int szelesseg, int iteraciosHatar) {
        // Az os osztaly konstruktoranak hivasa
        super(a, b, c, d, szelesseg, iteraciosHatar);
        setTitle("A Mandelbrot halmaz nagyitasai");
        // Eger kattinto esemenyek feldolgozasa:
        addMouseListener(new java.awt.event.MouseAdapter() {
            // Eger kattintassal jeloljuk ki a nagyitando teruletet
            // bal felso sarkat vagy ugyancsak eger kattintassal
            // vizsgaljuk egy adott pont iteracioit:
            public void mousePressed(java.awt.event.MouseEvent m) {
                // Az egermutato pozicioja
                x = m.getX();
                y = m.getY();
                // Az 1. eger gombbal a nagyitando terulet kijeloleset
                // vegezzuk:
                if(m.getButton() == java.awt.event.MouseEvent.BUTTON1 ) {
                    // A nagyitando kijelolt teruletet bal felso sarka: (x,y)
                    // es szelessege (majd a vonszolas noveli)
                    mx = 0;
                    my = 0;
                    repaint();
                } else {
                    // Nem az 1. eger gombbal az egermutato mutatta c
                    // komplex szambol inditott iteraciokat vizsgalhatjuk
                    MandelbrotIteraciok iteraciok =
                            new MandelbrotIteraciok(
                            MandelbrotHalmazNagyito.this, 50);
                    new Thread(iteraciok).start();
                }
            }
            // Vonszolva kijelolunk egy teruletet...
            // Ha felengedjuk, akkor a kijelolt terulet
            // ujraszamitasa indul:
            public void mouseReleased(java.awt.event.MouseEvent m) {
                if(m.getButton() == java.awt.event.MouseEvent.BUTTON1 ) {
                    double dx = (MandelbrotHalmazNagyito.this.b
                            - MandelbrotHalmazNagyito.this.a)
                            /MandelbrotHalmazNagyito.this.szelesseg;
                    double dy = (MandelbrotHalmazNagyito.this.d
                            - MandelbrotHalmazNagyito.this.c)
                            /MandelbrotHalmazNagyito.this.magassag;
                    // Az uj Mandelbrot nagyito objektum elkeszitese:
                    new MandelbrotHalmazNagyito(
                            MandelbrotHalmazNagyito.this.a+x*dx,
                            MandelbrotHalmazNagyito.this.a+x*dx+mx*dx,
                            MandelbrotHalmazNagyito.this.d-y*dy-my*dy,
                            MandelbrotHalmazNagyito.this.d-y*dy,
                            600,
                            MandelbrotHalmazNagyito.this.iteraciosHatar);
                }
            }
        });
        // Eger mozgas esemenyek feldolgozasa:
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            // Vonszolassal jeloljuk ki a negyzetet:
            public void mouseDragged(java.awt.event.MouseEvent m) {
                // A nagyitando kijelolt terulet szelessege es magassaga:
                mx = m.getX() - x;
                my = m.getY() - y;
                repaint();
            }
        });
    }
    /**
     * Pillanatfelvetelek keszitese.
     */
    public void pillanatfelvetel() {
        // Az elmentendo kep elkeszitese:
        java.awt.image.BufferedImage mentKep =
                new java.awt.image.BufferedImage(szelesseg, magassag,
                java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics g = mentKep.getGraphics();
        g.drawImage(kep, 0, 0, this);
        g.setColor(java.awt.Color.BLACK);
        g.drawString("a=" + a, 10, 15);
        g.drawString("b=" + b, 10, 30);
        g.drawString("c=" + c, 10, 45);
        g.drawString("d=" + d, 10, 60);
        g.drawString("n=" + iteraciosHatar, 10, 75);
        if(szamitasFut) {
            g.setColor(java.awt.Color.RED);
            g.drawLine(0, sor, getWidth(), sor);
        }
        g.setColor(java.awt.Color.GREEN);
        g.drawRect(x, y, mx, my);
        g.dispose();
        // A pillanatfelvetel kepfajl nevenek kepzese:
        StringBuffer sb = new StringBuffer();
        sb = sb.delete(0, sb.length());
        sb.append("MandelbrotHalmazNagyitas_");
        sb.append(++pillanatfelvetelSzamlalo);
        sb.append("_");
        // A fajl nevebe belevesszuk, hogy melyik tartomanyban
        // talaltuk a halmazt:
        sb.append(a);
        sb.append("_");
        sb.append(b);
        sb.append("_");
        sb.append(c);
        sb.append("_");
        sb.append(d);
        sb.append(".png");
        // png formatumu kepet mentunk
        try {
            javax.imageio.ImageIO.write(mentKep, "png",
                    new java.io.File(sb.toString()));
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * A nagyitando kijelolt teruletet jelzo negyzet kirajzolasa.
     */
    public void paint(java.awt.Graphics g) {
        // A Mandelbrot halmaz kirajzolasa
        g.drawImage(kep, 0, 0, this);
        // Ha eppen fut a szamitas, akkor egy voros
        // vonallal jeloljuk, hogy melyik sorban tart:
        if(szamitasFut) {
            g.setColor(java.awt.Color.RED);
            g.drawLine(0, sor, getWidth(), sor);
        }
        // A jelzo negyzet kirajzolasa:
        g.setColor(java.awt.Color.GREEN);
        g.drawRect(x, y, mx, my);
    }
    /**
     * Hol all az egermutato?
     * @return int a kijelolt pont oszlop pozicioja.
     */    
    public int getX() {
        return x;
    }
    /**
     * Hol all az egermutato?
     * @return int a kijelolt pont sor pozicioja.
     */    
    public int getY() {
        return y;
    }
    /**
     * Peldanyosit egy Mandelbrot halmazt nagyito obektumot.
     */
    public static void main(String[] args) {
        // A kiindulo halmazt a komplex sik [-2.0, .7]x[-1.35, 1.35]
        // tartomanyaban keressuk egy 600x600-as haloval es az
        // aktualis nagyitasi pontossaggal:
        new MandelbrotHalmazNagyito(-2.0, .7, -1.35, 1.35, 600, 255);
    }
}                    
