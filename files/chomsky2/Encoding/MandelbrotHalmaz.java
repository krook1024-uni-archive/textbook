/*
 * MandelbrotHalmaz.java
 *
 * DIGIT 2005, Javat tanítok
 * Bátfai Norbert, nbatfai@inf.unideb.hu
 *
 */
/**
 * A Mandelbrot halmazt kiszámoló és kirajzoló osztály.
 *
 * @author Bátfai Norbert, nbatfai@inf.unideb.hu
 * @version 0.0.2
 */
public class MandelbrotHalmaz extends java.awt.Frame implements Runnable {
    /** A komplex sík vizsgált tartománya [a,b]x[c,d]. */
    protected double a, b, c, d;
    /** A komplex sík vizsgált tartományára feszített
     * háló szélessége és magassága. */
    protected int szélesség, magasság;
    /** A komplex sík vizsgált tartományára feszített hálónak megfelelõ kép.*/
    protected java.awt.image.BufferedImage kép;
    /** Max. hány lépésig vizsgáljuk a z_{n+1} = z_n * z_n + c iterációt?
     * (tk. most a nagyítási pontosság) */
    protected int iterációsHatár = 255;
    /** Jelzi, hogy éppen megy-e a szamítás? */
    protected boolean számításFut = false;
    /** Jelzi az ablakban, hogy éppen melyik sort számoljuk. */
    protected int sor = 0;
    /** A pillanatfelvételek számozásához. */
    protected static int pillanatfelvételSzámláló = 0;
    /**
     * Létrehoz egy a Mandelbrot halmazt a komplex sík
     * [a,b]x[c,d] tartománya felett kiszámoló
     * <code>MandelbrotHalmaz</code> objektumot.
     *
     * @param      a              a [a,b]x[c,d] tartomány a koordinátája.
     * @param      b              a [a,b]x[c,d] tartomány b koordinátája.
     * @param      c              a [a,b]x[c,d] tartomány c koordinátája.
     * @param      d              a [a,b]x[c,d] tartomány d koordinátája.
     * @param      szélesség      a halmazt tartalmazó tömb szélessége.
     * @param      iterációsHatár a számítás pontossága.
     */
    public MandelbrotHalmaz(double a, double b, double c, double d,
            int szélesség, int iterációsHatár) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.szélesség = szélesség;
        this.iterációsHatár = iterációsHatár;
        // a magasság az (b-a) / (d-c) = szélesség / magasság
        // arányból kiszámolva az alábbi lesz:
        this.magasság = (int)(szélesség * ((d-c)/(b-a)));
        // a kép, amire rárajzoljuk majd a halmazt
        kép = new java.awt.image.BufferedImage(szélesség, magasság,
                java.awt.image.BufferedImage.TYPE_INT_RGB);
        // Az ablak bezárásakor kilépünk a programból.
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                setVisible(false);
                System.exit(0);
            }
        });
        // A billentyûzetrõl érkezõ események feldolgozása
        addKeyListener(new java.awt.event.KeyAdapter() {
            // Az 's', 'n' és 'm' gombok lenyomását figyeljük
            public void keyPressed(java.awt.event.KeyEvent e) {
                if(e.getKeyCode() == java.awt.event.KeyEvent.VK_S)
                    pillanatfelvétel();
                // Az 'n' gomb benyomásával pontosabb számítást végzünk.
                else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_N) {
                    if(számításFut == false) {
                        MandelbrotHalmaz.this.iterációsHatár += 256;
                        // A számítás újra indul:
                        számításFut = true;
                        new Thread(MandelbrotHalmaz.this).start();
                    }
                    // Az 'm' gomb benyomásával pontosabb számítást végzünk,
                    // de közben sokkal magasabbra vesszük az iterációs
                    // határt, mint az 'n' használata esetén
                } else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_M) {
                    if(számításFut == false) {
                        MandelbrotHalmaz.this.iterációsHatár += 10*256;
                        // A számítás újra indul:
                        számításFut = true;
                        new Thread(MandelbrotHalmaz.this).start();
                    }
                }
            }
        });
        // Ablak tulajdonságai
        setTitle("A Mandelbrot halmaz");
        setResizable(false);
        setSize(szélesség, magasság);
        setVisible(true);
        // A számítás indul:
        számításFut = true;
        new Thread(this).start();
    }
    /** A halmaz aktuális állapotának kirajzolása. */
    public void paint(java.awt.Graphics g) {
        // A Mandelbrot halmaz kirajzolása
        g.drawImage(kép, 0, 0, this);
        // Ha éppen fut a számítás, akkor egy vörös
        // vonallal jelöljük, hogy melyik sorban tart:
        if(számításFut) {
            g.setColor(java.awt.Color.RED);
            g.drawLine(0, sor, getWidth(), sor);
        }
    }
    // Ne villogjon a felület (mert a "gyári" update()
    // lemeszelné a vászon felületét).
    public void update(java.awt.Graphics g) {
        paint(g);
    }
    /** Pillanatfelvételek készítése. */
    public void pillanatfelvétel() {
        // Az elmentendõ kép elkészítése:
        java.awt.image.BufferedImage mentKép =
                new java.awt.image.BufferedImage(szélesség, magasság,
                java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics g = mentKép.getGraphics();
        g.drawImage(kép, 0, 0, this);
        g.setColor(java.awt.Color.BLUE);
        g.drawString("a=" + a, 10, 15);
        g.drawString("b=" + b, 10, 30);
        g.drawString("c=" + c, 10, 45);
        g.drawString("d=" + d, 10, 60);
        g.drawString("n=" + iterációsHatár, 10, 75);
        g.dispose();
        // A pillanatfelvétel képfájl nevének képzése:
        StringBuffer sb = new StringBuffer();
        sb = sb.delete(0, sb.length());
        sb.append("MandelbrotHalmaz_");
        sb.append(++pillanatfelvételSzámláló);
        sb.append("_");
        // A fájl nevébe belevesszük, hogy melyik tartományban
        // találtuk a halmazt:
        sb.append(a);
        sb.append("_");
        sb.append(b);
        sb.append("_");
        sb.append(c);
        sb.append("_");
        sb.append(d);
        sb.append(".png");
        // png formátumú képet mentünk
        try {
            javax.imageio.ImageIO.write(mentKép, "png",
                    new java.io.File(sb.toString()));
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }
    /** 
     * A Mandelbrot halmaz számítási algoritmusa.
     * Az algoritmus részletes ismertetését lásd például a
     * [BARNSLEY KÖNYV] (M. Barnsley: Fractals everywhere, 
     * Academic Press, Boston, 1986) hivatkozásban vagy 
     * ismeretterjesztõ szinten a [CSÁSZÁR KÖNYV] hivatkozásban.
     */
    public void run() {
        // A [a,b]x[c,d] tartományon milyen sûrû a
        // megadott szélesség, magasság háló:
        double dx = (b-a)/szélesség;
        double dy = (d-c)/magasság;
        double reC, imC, reZ, imZ, ujreZ, ujimZ;
        int rgb;
        // Hány iterációt csináltunk?
        int iteráció = 0;
        // Végigzongorázzuk a szélesség x magasság hálót:
        for(int j=0; j<magasság; ++j) {
            sor = j;
            for(int k=0; k<szélesség; ++k) {
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
                while(reZ*reZ + imZ*imZ < 4 && iteráció < iterációsHatár) {
                    // z_{n+1} = z_n * z_n + c
                    ujreZ = reZ*reZ - imZ*imZ + reC;
                    ujimZ = 2*reZ*imZ + imC;
                    reZ = ujreZ;
                    imZ = ujimZ;
                    
                    ++iteráció;
                    
                }
                // ha a < 4 feltétel nem teljesült és a
                // iteráció < iterációsHatár sérülésével lépett ki, azaz
                // feltesszük a c-rõl, hogy itt a z_{n+1} = z_n * z_n + c
                // sorozat konvergens, azaz iteráció = iterációsHatár
                // ekkor az iteráció %= 256 egyenlõ 255, mert az esetleges
                // nagyítasok során az iteráció = valahány * 256 + 255
                iteráció %= 256;
                // így a halmaz elemeire 255-255 értéket használjuk,
                // azaz (Red=0,Green=0,Blue=0) fekete színnel:
                rgb = (255-iteráció)|
                        ((255-iteráció) << 8) |
                        ((255-iteráció) << 16);
                // rajzoljuk a képre az éppen vizsgált pontot:
                kép.setRGB(k, j, rgb);
            }
            repaint();
        }
        számításFut = false;
    }
    /** Az aktuális Mandelbrot halmaz [a,b]x[c,d] adatai.
     * @return double a */
    public double getA() {
        return a;
    }
    /** Az aktuális Mandelbrot halmaz [a,b]x[c,d] adatai.
     * @return double b */
    public double getB() {
        return b;
    }
    /** Az aktuális Mandelbrot halmaz [a,b]x[c,d] adatai.
     * @return double c */
    public double getC() {
        return c;
    }
    /** Az aktuális Mandelbrot halmaz [a,b]x[c,d] adatai.
     * @return double d */
    public double getD() {
        return d;
    }
    /** Az aktuális Mandelbrot halmaz feletti rács adatai.
     * @return int szélesség */    
    public int getSz() {
        return szélesség;
    }
    /** Az aktuális Mandelbrot halmaz feletti rács adatai.
     * @return int magasság */    
    public int getM() {
        return magasság;
    }
    /** Az aktuális Mandelbrot halmazt tartalmazó kép.
     * @return BufferedImage kép */    
    public java.awt.image.BufferedImage kép() {
        return kép;
    }
    /** Példányosít egy Mandelbrot halmazt kiszámoló obektumot. */
    public static void main(String[] args) {
        // A halmazt a komplex sík [-2.0, .7]x[-1.35, 1.35] tartományában
        // keressük egy 400x400-as hálóval:
        new MandelbrotHalmaz(-2.0, .7, -1.35, 1.35, 600, 255);
    }
}                    
