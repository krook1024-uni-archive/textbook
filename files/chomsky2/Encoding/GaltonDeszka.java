/*
 * GaltonDeszka.java
 *
 * DIGIT 2005, Javat tanitok
 * Batfai Norbert, nbatfai@inf.unideb.hu
 *
 */
/**
 * A Galton deszka kiserletet szimulalo osztaly.
 * A kiserlet leirasat lasd a [RENYI VALSEG KONYV] (Renyi
 * Alfred: Valoszinusegszamitas, Tankonyvkiado, 1973, 144 o.) 
 * konyvben.
 *
 * @author Batfai Norbert, nbatfai@inf.unideb.hu
 * @version 0.0.1
 */
public class GaltonDeszka extends java.awt.Frame implements Runnable {
    /** Melyik oszlopban van eppen az eso golyo? */
    private int oszlop = 0;
    /** Melyik sorban van eppen az eso golyo? */
    private int sor = 0;
    /** Hova hany golyo esett, az i. helyre hisztogram[i] */
    private int [] hisztogram;
    /** Hany pixel magas legyen egy deszkasor. */
    private int sorMagassag;
    /** Hany pixel szeles legyen a kiserleti elrendezes ablaka? */
    private int ablakSzelesseg;
    /** Hany pixel magas legyen a kiserleti elrendezes ablaka? */
    private int ablakMagassag;
    // Veletlenszam generator
    private java.util.Random random = new java.util.Random();
    // Pillanatfelvetel keszitesehez
    private java.awt.Robot robot;
    /** Keszitsunk pillanatfelvetelt? */
    private boolean pillanatfelvetel = false;
    /** A pillanatfelvetelek szamozasahoz. */
    private static int pillanatfelvetelSzamlalo = 0;
    /**
     * Letrehoz egy Galton deszka kiserleti elrendezest
     * absztrahalo <code>GaltonDeszka</code> objektumot.
     *
     * @param      magassag       a deszkasorok szama.
     * @param      sorMagassag    a deszkasorok magassaga pixelben.
     */
    public GaltonDeszka(int magassag, int sorMagassag) {
        // Hova hany golyo esett, az i. helyre hisztogram[i]
        hisztogram = new int [magassag];
        // Nullazzuk a hisztogram elemeit (nem lenne szukseges, de ez a
        // biztonsagos taktika)
        for(int i=0; i<hisztogram.length; ++i)
            hisztogram[i] = 0;
        this.sorMagassag = sorMagassag;
        // Az ablak bezarasakor kilepunk a programbol.
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                setVisible(false);
                System.exit(0);
            }
        });
        // Az s gomb benyomasaval ki/bekapcsoljuk a 
        // pillanatfelvetel keszitest a kiserletrol:
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if(e.getKeyCode() == java.awt.event.KeyEvent.VK_S)
                    pillanatfelvetel = !pillanatfelvetel;
            }
        });
        // Pillanatfelvetel keszitesehez:
        try {
            robot = new java.awt.Robot(
                    java.awt.GraphicsEnvironment.
                    getLocalGraphicsEnvironment().
                    getDefaultScreenDevice());
        } catch(java.awt.AWTException e) {
            e.printStackTrace();
        }
        // Ablak tulajdonsagai
        setTitle("Galton deszka kiserlet");
        setResizable(false);
        ablakSzelesseg = magassag*sorMagassag*2;
        ablakMagassag = magassag*sorMagassag+400;                
        setSize(ablakSzelesseg, ablakMagassag);
        setVisible(true);
        // A kiserlet indul:
        new Thread(this).start();
    }
    /**
     * A kiserlet aktualis allapotanak kirajzolasa.
     */
    public void paint(java.awt.Graphics g) {
        // A deszkasorok es a golyo kirajzolasa
        for(int i=0; i<hisztogram.length; ++i) {
            // Deszkak kirajzolasa
            g.setColor(java.awt.Color.BLACK);
            for(int j=0; j<i; ++j)
                g.fillRect(getWidth()/2
                        -((i-1)*sorMagassag+sorMagassag/2)
                        +j*2*sorMagassag+sorMagassag/3,
                        50+i*sorMagassag, sorMagassag/3, sorMagassag);
            // Minden lehetseges poziciora egy feher
            // golyo kirajzolasa (torli a korabbi piros golyokat)
            g.setColor(java.awt.Color.WHITE);
            for(int j=0; j<=i; ++j)
                g.fillArc(getWidth()/2
                        -(i*sorMagassag+sorMagassag/2)+j*2*sorMagassag,
                        50+i*sorMagassag,
                        sorMagassag,
                        sorMagassag, 0, 360);
            // A most eppen alahullo golyo kirajzolasa
            if(i == sor) {
                g.setColor(java.awt.Color.RED);
                g.fillArc(getWidth()/2
                        -(i*sorMagassag+sorMagassag/2)+oszlop*2*sorMagassag,
                        50+i*sorMagassag, sorMagassag, sorMagassag, 0, 360);
            }
        }
        // A hisztogram kirajzolasa
        g.setColor(java.awt.Color.GREEN);
        for(int j=0; j<hisztogram.length; ++j)
            g.fillRect(getWidth()/2
                    -((hisztogram.length-1)*sorMagassag
                    +sorMagassag/2)+j*2*sorMagassag,
                    50+hisztogram.length*sorMagassag,
                    sorMagassag, sorMagassag*hisztogram[j]/10);
        // Keszitunk pillanatfelvetelt?
        if(pillanatfelvetel) {            
            // a biztonsag kedveert egy kep keszitese utan
            // kikapcsoljuk a pillanatfelvetelt, hogy a 
            // programmal ismerkedo Olvaso ne irja tele a 
            // fajlrendszeret a pillanatfelvetelekkel        
            pillanatfelvetel = false;
            pillanatfelvetel(robot.createScreenCapture
                    (new java.awt.Rectangle
                    (getLocation().x, getLocation().y, 
                    ablakSzelesseg, ablakMagassag)));                
        }
    }
    // Ne villogjon a felulet (mert a "gyari" update()
    // lemeszelne a vaszon feluletet).
    public void update(java.awt.Graphics g) {
        paint(g);
    }
    /**
     * Pillanatfelvetelek keszitese.
     */
    public void pillanatfelvetel(java.awt.image.BufferedImage felvetel) {
        
        // A pillanatfelvetel kep fajlneve
        StringBuffer sb = new StringBuffer();
        sb = sb.delete(0, sb.length());
        sb.append("GaltonDeszkaKiserlet");
        sb.append(++pillanatfelvetelSzamlalo);
        sb.append(".png");
        // png formatumu kepet mentunk
        try {
            javax.imageio.ImageIO.write(felvetel, "png",
                    new java.io.File(sb.toString()));
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * A kiserlet idobeli fejlodesenek vezerlese.
     */
    public void run() {
        // Vegtelen ciklus, azaz vegtelen sok golyot
        // dobunk le a deszkak kozott.
        while(true) {
            // Kezdetben a golyo a legfelso deszka felett.
            oszlop = 0;
            // A ciklus minden iteracioja egy deszkasornyi
            // esest jelent a golyo eleteben
            for(int i=0; i<hisztogram.length; ++i) {
                // Melyik sorban van eppen az eso golyo?
                sor = i;
                // Ha novelni akarjuk a sebesseget (a
                // latvany rovasara) akkor kommentezzuk be
                // ezt a varakozo try blokkot (de ekkor
                // ne felejtsuk el a hisztogram oszlopainak
                // magassagat sorMagassag*hisztogram[j]/10-rol
                // peldaul sorMagassag*hisztogram[j]/10000-re allitani).
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {}
                
                // Az tetejen a golyo az elso deszka felett
                if(i>0)
                    // ha nem a tetejen, akkor 50%-50%, hogy
                    // jobbra vagy balra esik tovabb.
                    // Melyik oszlopban van eppen az eso golyo?
                    oszlop = oszlop + random.nextInt(2);
                // Rajzoljuk ki a kiserlet aktualis allapotat!
                repaint();
            }
            // Ha kilep a golyo a ciklusbol, akkor
            // vegig esett a deszkasorokon es valamelyik
            // taroloba esett
            ++hisztogram[oszlop];
        }
    }
    /**
     * Peldanyosit egy Galton deszkas kiserleti
     * elrendezes obektumot.
     */
    public static void main(String[] args) {
        // Legyen 30 sor, soronkent 10 pixellel
        new GaltonDeszka(30, 10);
    }
}                
