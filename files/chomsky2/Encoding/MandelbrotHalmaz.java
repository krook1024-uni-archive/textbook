/*
 * MandelbrotHalmaz.java
 *
 * DIGIT 2005, Javat tanitok
 * Batfai Norbert, nbatfai@inf.unideb.hu
 *
 */
/**
 * A Mandelbrot halmazt kiszamolo es kirajzolo osztaly.
 *
 * @author Batfai Norbert, nbatfai@inf.unideb.hu
 * @version 0.0.2
 */
public class MandelbrotHalmaz extends java.awt.Frame implements Runnable {
    /** A komplex sik vizsgalt tartomanya [a,b]x[c,d]. */
    protected double a, b, c, d;
    /** A komplex sik vizsgalt tartomanyara feszitett
     * halo szelessege es magassaga. */
    protected int szelesseg, magassag;
    /** A komplex sik vizsgalt tartomanyara feszitett halonak megfelelo kep.*/
    protected java.awt.image.BufferedImage kep;
    /** Max. hany lepesig vizsgaljuk a z_{n+1} = z_n * z_n + c iteraciot?
     * (tk. most a nagyitasi pontossag) */
    protected int iteraciosHatar = 255;
    /** Jelzi, hogy eppen megy-e a szamitas? */
    protected boolean szamitasFut = false;
    /** Jelzi az ablakban, hogy eppen melyik sort szamoljuk. */
    protected int sor = 0;
    /** A pillanatfelvetelek szamozasahoz. */
    protected static int pillanatfelvetelSzamlalo = 0;
    /**
     * Letrehoz egy a Mandelbrot halmazt a komplex sik
     * [a,b]x[c,d] tartomanya felett kiszamolo
     * <code>MandelbrotHalmaz</code> objektumot.
     *
     * @param      a              a [a,b]x[c,d] tartomany a koordinataja.
     * @param      b              a [a,b]x[c,d] tartomany b koordinataja.
     * @param      c              a [a,b]x[c,d] tartomany c koordinataja.
     * @param      d              a [a,b]x[c,d] tartomany d koordinataja.
     * @param      szelesseg      a halmazt tartalmazo tomb szelessege.
     * @param      iteraciosHatar a szamitas pontossaga.
     */
    public MandelbrotHalmaz(double a, double b, double c, double d,
            int szelesseg, int iteraciosHatar) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.szelesseg = szelesseg;
        this.iteraciosHatar = iteraciosHatar;
        // a magassag az (b-a) / (d-c) = szelesseg / magassag
        // aranybol kiszamolva az alabbi lesz:
        this.magassag = (int)(szelesseg * ((d-c)/(b-a)));
        // a kep, amire rarajzoljuk majd a halmazt
        kep = new java.awt.image.BufferedImage(szelesseg, magassag,
                java.awt.image.BufferedImage.TYPE_INT_RGB);
        // Az ablak bezarasakor kilepunk a programbol.
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                setVisible(false);
                System.exit(0);
            }
        });
        // A billentyuzetrol erkezo esemenyek feldolgozasa
        addKeyListener(new java.awt.event.KeyAdapter() {
            // Az 's', 'n' es 'm' gombok lenyomasat figyeljuk
            public void keyPressed(java.awt.event.KeyEvent e) {
                if(e.getKeyCode() == java.awt.event.KeyEvent.VK_S)
                    pillanatfelvetel();
                // Az 'n' gomb benyomasaval pontosabb szamitast vegzunk.
                else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_N) {
                    if(szamitasFut == false) {
                        MandelbrotHalmaz.this.iteraciosHatar += 256;
                        // A szamitas ujra indul:
                        szamitasFut = true;
                        new Thread(MandelbrotHalmaz.this).start();
                    }
                    // Az 'm' gomb benyomasaval pontosabb szamitast vegzunk,
                    // de kozben sokkal magasabbra vesszuk az iteracios
                    // hatart, mint az 'n' hasznalata eseten
                } else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_M) {
                    if(szamitasFut == false) {
                        MandelbrotHalmaz.this.iteraciosHatar += 10*256;
                        // A szamitas ujra indul:
                        szamitasFut = true;
                        new Thread(MandelbrotHalmaz.this).start();
                    }
                }
            }
        });
        // Ablak tulajdonsagai
        setTitle("A Mandelbrot halmaz");
        setResizable(false);
        setSize(szelesseg, magassag);
        setVisible(true);
        // A szamitas indul:
        szamitasFut = true;
        new Thread(this).start();
    }
    /** A halmaz aktualis allapotanak kirajzolasa. */
    public void paint(java.awt.Graphics g) {
        // A Mandelbrot halmaz kirajzolasa
        g.drawImage(kep, 0, 0, this);
        // Ha eppen fut a szamitas, akkor egy voros
        // vonallal jeloljuk, hogy melyik sorban tart:
        if(szamitasFut) {
            g.setColor(java.awt.Color.RED);
            g.drawLine(0, sor, getWidth(), sor);
        }
    }
    // Ne villogjon a felulet (mert a "gyari" update()
    // lemeszelne a vaszon feluletet).
    public void update(java.awt.Graphics g) {
        paint(g);
    }
    /** Pillanatfelvetelek keszitese. */
    public void pillanatfelvetel() {
        // Az elmentendo kep elkeszitese:
        java.awt.image.BufferedImage mentKep =
                new java.awt.image.BufferedImage(szelesseg, magassag,
                java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics g = mentKep.getGraphics();
        g.drawImage(kep, 0, 0, this);
        g.setColor(java.awt.Color.BLUE);
        g.drawString("a=" + a, 10, 15);
        g.drawString("b=" + b, 10, 30);
        g.drawString("c=" + c, 10, 45);
        g.drawString("d=" + d, 10, 60);
        g.drawString("n=" + iteraciosHatar, 10, 75);
        g.dispose();
        // A pillanatfelvetel kepfajl nevenek kepzese:
        StringBuffer sb = new StringBuffer();
        sb = sb.delete(0, sb.length());
        sb.append("MandelbrotHalmaz_");
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
     * A Mandelbrot halmaz szamitasi algoritmusa.
     * Az algoritmus reszletes ismerteteset lasd peldaul a
     * [BARNSLEY KONYV] (M. Barnsley: Fractals everywhere, 
     * Academic Press, Boston, 1986) hivatkozasban vagy 
     * ismeretterjeszto szinten a [CSASZAR KONYV] hivatkozasban.
     */
    public void run() {
        // A [a,b]x[c,d] tartomanyon milyen suru a
        // megadott szelesseg, magassag halo:
        double dx = (b-a)/szelesseg;
        double dy = (d-c)/magassag;
        double reC, imC, reZ, imZ, ujreZ, ujimZ;
        int rgb;
        // Hany iteraciot csinaltunk?
        int iteracio = 0;
        // Vegigzongorazzuk a szelesseg x magassag halot:
        for(int j=0; j<magassag; ++j) {
            sor = j;
            for(int k=0; k<szelesseg; ++k) {
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
                while(reZ*reZ + imZ*imZ < 4 && iteracio < iteraciosHatar) {
                    // z_{n+1} = z_n * z_n + c
                    ujreZ = reZ*reZ - imZ*imZ + reC;
                    ujimZ = 2*reZ*imZ + imC;
                    reZ = ujreZ;
                    imZ = ujimZ;
                    
                    ++iteracio;
                    
                }
                // ha a < 4 feltetel nem teljesult es a
                // iteracio < iteraciosHatar serulesevel lepett ki, azaz
                // feltesszuk a c-rol, hogy itt a z_{n+1} = z_n * z_n + c
                // sorozat konvergens, azaz iteracio = iteraciosHatar
                // ekkor az iteracio %= 256 egyenlo 255, mert az esetleges
                // nagyitasok soran az iteracio = valahany * 256 + 255
                iteracio %= 256;
                // igy a halmaz elemeire 255-255 erteket hasznaljuk,
                // azaz (Red=0,Green=0,Blue=0) fekete szinnel:
                rgb = (255-iteracio)|
                        ((255-iteracio) << 8) |
                        ((255-iteracio) << 16);
                // rajzoljuk a kepre az eppen vizsgalt pontot:
                kep.setRGB(k, j, rgb);
            }
            repaint();
        }
        szamitasFut = false;
    }
    /** Az aktualis Mandelbrot halmaz [a,b]x[c,d] adatai.
     * @return double a */
    public double getA() {
        return a;
    }
    /** Az aktualis Mandelbrot halmaz [a,b]x[c,d] adatai.
     * @return double b */
    public double getB() {
        return b;
    }
    /** Az aktualis Mandelbrot halmaz [a,b]x[c,d] adatai.
     * @return double c */
    public double getC() {
        return c;
    }
    /** Az aktualis Mandelbrot halmaz [a,b]x[c,d] adatai.
     * @return double d */
    public double getD() {
        return d;
    }
    /** Az aktualis Mandelbrot halmaz feletti racs adatai.
     * @return int szelesseg */    
    public int getSz() {
        return szelesseg;
    }
    /** Az aktualis Mandelbrot halmaz feletti racs adatai.
     * @return int magassag */    
    public int getM() {
        return magassag;
    }
    /** Az aktualis Mandelbrot halmazt tartalmazo kep.
     * @return BufferedImage kep */    
    public java.awt.image.BufferedImage kep() {
        return kep;
    }
    /** Peldanyosit egy Mandelbrot halmazt kiszamolo obektumot. */
    public static void main(String[] args) {
        // A halmazt a komplex sik [-2.0, .7]x[-1.35, 1.35] tartomanyaban
        // keressuk egy 400x400-as haloval:
        new MandelbrotHalmaz(-2.0, .7, -1.35, 1.35, 600, 255);
    }
}                    
