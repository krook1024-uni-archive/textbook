/*
 * GaltonDeszka.java
 *
 * DIGIT 2005, Javat tanítok
 * Bátfai Norbert, nbatfai@inf.unideb.hu
 *
 */
/**
 * A Galton deszka kísérletet szimuláló osztály.
 * A kísérlet leírását lásd a [RÉNYI VALSÉG KÖNYV] (Rényi
 * Alfréd: Valószínûségszámítás, Tankönyvkiadó, 1973, 144 o.) 
 * könyvben.
 *
 * @author Bátfai Norbert, nbatfai@inf.unideb.hu
 * @version 0.0.1
 */
public class GaltonDeszka extends java.awt.Frame implements Runnable {
    /** Melyik oszlopban van éppen az esõ golyó? */
    private int oszlop = 0;
    /** Melyik sorban van éppen az esõ golyó? */
    private int sor = 0;
    /** Hová hány golyó esett, az i. helyre hisztogram[i] */
    private int [] hisztogram;
    /** Hány pixel magas legyen egy deszkasor. */
    private int sorMagasság;
    /** Hány pixel széles legyen a kísérleti elrendezés ablaka? */
    private int ablakSzélesség;
    /** Hány pixel magas legyen a kísérleti elrendezés ablaka? */
    private int ablakMagasság;
    // Véletlenszám generátor
    private java.util.Random random = new java.util.Random();
    // Pillanatfelvétel készítéséhez
    private java.awt.Robot robot;
    /** Készítsünk pillanatfelvételt? */
    private boolean pillanatfelvétel = false;
    /** A pillanatfelvételek számozásához. */
    private static int pillanatfelvételSzámláló = 0;
    /**
     * Létrehoz egy Galton deszka kísérleti elrendezést
     * absztraháló <code>GaltonDeszka</code> objektumot.
     *
     * @param      magasság       a deszkasorok száma.
     * @param      sorMagasság    a deszkasorok magassága pixelben.
     */
    public GaltonDeszka(int magasság, int sorMagasság) {
        // Hová hány golyó esett, az i. helyre hisztogram[i]
        hisztogram = new int [magasság];
        // Nullázzuk a hisztogram elemeit (nem lenne szükséges, de ez a
        // biztonságos taktika)
        for(int i=0; i<hisztogram.length; ++i)
            hisztogram[i] = 0;
        this.sorMagasság = sorMagasság;
        // Az ablak bezárásakor kilépünk a programból.
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                setVisible(false);
                System.exit(0);
            }
        });
        // Az s gomb benyomásával ki/bekapcsoljuk a 
        // pillanatfelvétel készítést a kísérletrõl:
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if(e.getKeyCode() == java.awt.event.KeyEvent.VK_S)
                    pillanatfelvétel = !pillanatfelvétel;
            }
        });
        // Pillanatfelvétel készítéséhez:
        try {
            robot = new java.awt.Robot(
                    java.awt.GraphicsEnvironment.
                    getLocalGraphicsEnvironment().
                    getDefaultScreenDevice());
        } catch(java.awt.AWTException e) {
            e.printStackTrace();
        }
        // Ablak tulajdonságai
        setTitle("Galton deszka kísérlet");
        setResizable(false);
        ablakSzélesség = magasság*sorMagasság*2;
        ablakMagasság = magasság*sorMagasság+400;                
        setSize(ablakSzélesség, ablakMagasság);
        setVisible(true);
        // A kísérlet indul:
        new Thread(this).start();
    }
    /**
     * A kísérlet aktuális állapotának kirajzolása.
     */
    public void paint(java.awt.Graphics g) {
        // A deszkasorok és a golyó kirajzolása
        for(int i=0; i<hisztogram.length; ++i) {
            // Deszkák kirajzolása
            g.setColor(java.awt.Color.BLACK);
            for(int j=0; j<i; ++j)
                g.fillRect(getWidth()/2
                        -((i-1)*sorMagasság+sorMagasság/2)
                        +j*2*sorMagasság+sorMagasság/3,
                        50+i*sorMagasság, sorMagasság/3, sorMagasság);
            // Minden lehetséges pozícióra egy fehér
            // golyó kirajzolása (törli a korábbi piros golyókat)
            g.setColor(java.awt.Color.WHITE);
            for(int j=0; j<=i; ++j)
                g.fillArc(getWidth()/2
                        -(i*sorMagasság+sorMagasság/2)+j*2*sorMagasság,
                        50+i*sorMagasság,
                        sorMagasság,
                        sorMagasság, 0, 360);
            // A most éppen aláhulló golyó kirajzolása
            if(i == sor) {
                g.setColor(java.awt.Color.RED);
                g.fillArc(getWidth()/2
                        -(i*sorMagasság+sorMagasság/2)+oszlop*2*sorMagasság,
                        50+i*sorMagasság, sorMagasság, sorMagasság, 0, 360);
            }
        }
        // A hisztogram kirajzolása
        g.setColor(java.awt.Color.GREEN);
        for(int j=0; j<hisztogram.length; ++j)
            g.fillRect(getWidth()/2
                    -((hisztogram.length-1)*sorMagasság
                    +sorMagasság/2)+j*2*sorMagasság,
                    50+hisztogram.length*sorMagasság,
                    sorMagasság, sorMagasság*hisztogram[j]/10);
        // Készítünk pillanatfelvételt?
        if(pillanatfelvétel) {            
            // a biztonság kedvéért egy kép készítése után
            // kikapcsoljuk a pillanatfelvételt, hogy a 
            // programmal ismerkedõ Olvasó ne írja tele a 
            // fájlrendszerét a pillanatfelvételekkel        
            pillanatfelvétel = false;
            pillanatfelvétel(robot.createScreenCapture
                    (new java.awt.Rectangle
                    (getLocation().x, getLocation().y, 
                    ablakSzélesség, ablakMagasság)));                
        }
    }
    // Ne villogjon a felület (mert a "gyári" update()
    // lemeszelné a vászon felületét).
    public void update(java.awt.Graphics g) {
        paint(g);
    }
    /**
     * Pillanatfelvételek készítése.
     */
    public void pillanatfelvétel(java.awt.image.BufferedImage felvetel) {
        
        // A pillanatfelvétel kép fájlneve
        StringBuffer sb = new StringBuffer();
        sb = sb.delete(0, sb.length());
        sb.append("GaltonDeszkaKiserlet");
        sb.append(++pillanatfelvételSzámláló);
        sb.append(".png");
        // png formátumú képet mentünk
        try {
            javax.imageio.ImageIO.write(felvetel, "png",
                    new java.io.File(sb.toString()));
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * A kísérlet idõbeli fejlõdésének vezérlése.
     */
    public void run() {
        // Végtelen ciklus, azaz végtelen sok golyót
        // dobunk le a deszkák között.
        while(true) {
            // Kezdetben a golyó a legfelsõ deszka felett.
            oszlop = 0;
            // A ciklus minden iterációja egy deszkasornyi
            // esést jelent a golyó életében
            for(int i=0; i<hisztogram.length; ++i) {
                // Melyik sorban van éppen az esõ golyó?
                sor = i;
                // Ha növelni akarjuk a sebességet (a
                // látvány rovására) akkor kommentezzük be
                // ezt a várakozó try blokkot (de ekkor
                // ne felejtsük el a hisztogram oszlopainak
                // magasságát sorMagasság*hisztogram[j]/10-rõl
                // például sorMagasság*hisztogram[j]/10000-re állítani).
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {}
                
                // Az tetején a golyó az elsõ deszka felett
                if(i>0)
                    // ha nem a tetején, akkor 50%-50%, hogy
                    // jobbra vagy balra esik tovább.
                    // Melyik oszlopban van éppen az esõ golyó?
                    oszlop = oszlop + random.nextInt(2);
                // Rajzoljuk ki a kísérlet aktuális állapotát!
                repaint();
            }
            // Ha kilép a golyó a ciklusból, akkor
            // végig esett a deszkasorokon és valamelyik
            // tárolóba esett
            ++hisztogram[oszlop];
        }
    }
    /**
     * Példányosít egy Galton deszkás kísérleti
     * elrendezés obektumot.
     */
    public static void main(String[] args) {
        // Legyen 30 sor, soronként 10 pixellel
        new GaltonDeszka(30, 10);
    }
}                
