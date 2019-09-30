/*
 * Sejtautomata.java
 *
 * DIGIT 2005, Javat tanítok
 * Bátfai Norbert, nbatfai@inf.unideb.hu
 *
 */
/**
 * Sejtautomata osztály.
 *
 * @author Bátfai Norbert, nbatfai@inf.unideb.hu
 * @version 0.0.1
 */
public class Sejtautomata extends java.awt.Frame implements Runnable {
    /** Egy sejt lehet élõ */
    public static final boolean ÉLÕ = true;
    /** vagy halott */
    public static final boolean HALOTT = false;
    /** Két rácsot használunk majd, az egyik a sejttér állapotát
     * a t_n, a másik a t_n+1 idõpillanatban jellemzi. */
    protected boolean [][][] rácsok = new boolean [2][][];
    /** Valamelyik rácsra mutat, technikai jellegû, hogy ne kelljen a
     * [2][][]-ból az elsõ dimenziót használni, mert vagy az egyikre
     * állítjuk, vagy a másikra. */
    protected boolean [][] rács;
    /** Megmutatja melyik rács az aktuális: [rácsIndex][][] */
    protected int rácsIndex = 0;
    /** Pixelben egy cella adatai. */
    protected int cellaSzélesség = 20;
    protected int cellaMagasság = 20;
    /** A sejttér nagysága, azaz hányszor hány cella van? */
    protected int szélesség = 20;
    protected int magasság = 10;
    /** A sejttér két egymást követõ t_n és t_n+1 diszkrét idõpillanata
     közötti valós idõ. */  
    protected int várakozás = 1000;
    // Pillanatfelvétel készítéséhez
    private java.awt.Robot robot;
    /** Készítsünk pillanatfelvételt? */
    private boolean pillanatfelvétel = false;
    /** A pillanatfelvételek számozásához. */
    private static int pillanatfelvételSzámláló = 0;
    /**
     * Létrehoz egy <code>Sejtautomata</code> objektumot.
     *
     * @param      szélesség    a sejttér szélessége.
     * @param      magasság     a sejttér szélessége.
     */
    public Sejtautomata(int szélesség, int magasság) {
        this.szélesség = szélesség;
        this.magasság = magasság;
        // A két rács elkészítése
        rácsok[0] = new boolean[magasság][szélesség];
        rácsok[1] = new boolean[magasság][szélesség];
        rácsIndex = 0;
        rács = rácsok[rácsIndex];
        // A kiinduló rács minden cellája HALOTT
        for(int i=0; i<rács.length; ++i)
            for(int j=0; j<rács[0].length; ++j)
                rács[i][j] = HALOTT;
        // A kiinduló rácsra "élõlényeket" helyezünk
        //sikló(rács, 2, 2);
        siklóKilövõ(rács, 5, 60);
        // Az ablak bezárásakor kilépünk a programból.
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                setVisible(false);
                System.exit(0);
            }
        });
        // A billentyûzetrõl érkezõ események feldolgozása
        addKeyListener(new java.awt.event.KeyAdapter() {
            // Az 'k', 'n', 'l', 'g' és 's' gombok lenyomását figyeljük
            public void keyPressed(java.awt.event.KeyEvent e) {
                if(e.getKeyCode() == java.awt.event.KeyEvent.VK_K) {
                    // Felezük a cella méreteit:
                    cellaSzélesség /= 2;
                    cellaMagasság /= 2;
                    setSize(Sejtautomata.this.szélesség*cellaSzélesség,
                            Sejtautomata.this.magasság*cellaMagasság);
                    validate();
                } else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_N) {
                    // Duplázzuk a cella méreteit:
                    cellaSzélesség *= 2;
                    cellaMagasság *= 2;
                    setSize(Sejtautomata.this.szélesség*cellaSzélesség,
                            Sejtautomata.this.magasság*cellaMagasság);
                    validate();
                } else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_S)
                    pillanatfelvétel = !pillanatfelvétel;
                else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_G)
                    várakozás /= 2;
                else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_L)
                    várakozás *= 2;
                repaint();
            }
        });
        // Egér kattintó események feldolgozása:
        addMouseListener(new java.awt.event.MouseAdapter() {
            // Egér kattintással jelöljük ki a nagyítandó területet
            // bal felsõ sarkát vagy ugyancsak egér kattintással
            // vizsgáljuk egy adott pont iterációit:
            public void mousePressed(java.awt.event.MouseEvent m) {
                // Az egérmutató pozíciója
                int x = m.getX()/cellaSzélesség;
                int y = m.getY()/cellaMagasság;
                rácsok[rácsIndex][y][x] = !rácsok[rácsIndex][y][x];
                repaint();
            }
        });
        // Egér mozgás események feldolgozása:
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            // Vonszolással jelöljük ki a négyzetet:
            public void mouseDragged(java.awt.event.MouseEvent m) {
                int x = m.getX()/cellaSzélesség;
                int y = m.getY()/cellaMagasság;
                rácsok[rácsIndex][y][x] = ÉLÕ;
                repaint();
            }
        });
        // Cellaméretek kezdetben
        cellaSzélesség = 10;
        cellaMagasság = 10;
        // Pillanatfelvétel készítéséhez:
        try {
            robot = new java.awt.Robot(
                    java.awt.GraphicsEnvironment.
                    getLocalGraphicsEnvironment().
                    getDefaultScreenDevice());
        } catch(java.awt.AWTException e) {
            e.printStackTrace();
        }
        // A program ablakának adatai:
        setTitle("Sejtautomata");
        setResizable(false);
        setSize(szélesség*cellaSzélesség,
                magasság*cellaMagasság);
        setVisible(true);
        // A sejttér életrekeltése:
        new Thread(this).start();
    }
    /** A sejttér kirajzolása. */
    public void paint(java.awt.Graphics g) {
        // Az aktuális
        boolean [][] rács = rácsok[rácsIndex];
        // rácsot rajzoljuk ki:
        for(int i=0; i<rács.length; ++i) { // végig lépked a sorokon
            for(int j=0; j<rács[0].length; ++j) { // s az oszlopok
                // Sejt cella kirajzolása
                if(rács[i][j] == ÉLÕ)
                    g.setColor(java.awt.Color.BLACK);
                else
                    g.setColor(java.awt.Color.WHITE);
                g.fillRect(j*cellaSzélesség, i*cellaMagasság,
                        cellaSzélesség, cellaMagasság);
                // Rács kirajzolása
                g.setColor(java.awt.Color.LIGHT_GRAY);
                g.drawRect(j*cellaSzélesség, i*cellaMagasság,
                        cellaSzélesség, cellaMagasság);
            }
        }
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
                    szélesség*cellaSzélesség,
                    magasság*cellaMagasság)));
        }
    }
    /**
     * Az kérdezett állapotban lévõ nyolcszomszédok száma.
     *
     * @param   rács    a sejttér rács
     * @param   sor     a rács vizsgált sora
     * @param   oszlop  a rács vizsgált oszlopa
     * @param   állapor a nyolcszomszédok vizsgált állapota
     * @return int a kérdezett állapotbeli nyolcszomszédok száma.
     */
    public int szomszédokSzáma(boolean [][] rács,
            int sor, int oszlop, boolean állapot) {        
        int állapotúSzomszéd = 0;
        // A nyolcszomszédok végigzongorázása:
        for(int i=-1; i<2; ++i)
            for(int j=-1; j<2; ++j)
                // A vizsgált sejtet magát kihagyva:
                if(!((i==0) && (j==0))) {
            // A sejttérbõl szélének szomszédai
            // a szembe oldalakon ("periódikus határfeltétel")
            int o = oszlop + j;
            if(o < 0)
                o = szélesség-1;
            else if(o >= szélesség)
                o = 0;
            
            int s = sor + i;
            if(s < 0)
                s = magasság-1;
            else if(s >= magasság)
                s = 0;
            
            if(rács[s][o] == állapot)
                ++állapotúSzomszéd;
                }
        
        return állapotúSzomszéd;
    }
    /**
     * A sejttér idõbeli fejlõdése a John H. Conway féle
     * életjáték sejtautomata szabályai alapján történik.
     * A szabályok részletes ismertetését lásd például a
     * [MATEK JÁTÉK] hivatkozásban (Csákány Béla: Diszkrét
     * matematikai játékok. Polygon, Szeged 1998. 171. oldal.)
     */
    public void idõFejlõdés() {
        
        boolean [][] rácsElõtte = rácsok[rácsIndex];
        boolean [][] rácsUtána = rácsok[(rácsIndex+1)%2];
        
        for(int i=0; i<rácsElõtte.length; ++i) { // sorok
            for(int j=0; j<rácsElõtte[0].length; ++j) { // oszlopok
                
                int élõk = szomszédokSzáma(rácsElõtte, i, j, ÉLÕ);
                
                if(rácsElõtte[i][j] == ÉLÕ) {
                /* Élõ élõ marad, ha kettõ vagy három élõ
                 szomszedja van, különben halott lesz. */
                    if(élõk==2 || élõk==3)
                        rácsUtána[i][j] = ÉLÕ;
                    else
                        rácsUtána[i][j] = HALOTT;
                }  else {
                /* Halott halott marad, ha három élõ
                 szomszedja van, különben élõ lesz. */
                    if(élõk==3)
                        rácsUtána[i][j] = ÉLÕ;
                    else
                        rácsUtána[i][j] = HALOTT;
                }
            }
        }
        rácsIndex = (rácsIndex+1)%2;
    }
    /** A sejttér idõbeli fejlõdése. */
    public void run() {
        
        while(true) {
            try {
                Thread.sleep(várakozás);
            } catch (InterruptedException e) {}
            
            idõFejlõdés();
            repaint();
        }
    }
    /**
     * A sejttérbe "élõlényeket" helyezünk, ez a "sikló".
     * Adott irányban halad, másolja magát a sejttérben.
     * Az élõlény ismertetését lásd például a
     * [MATEK JÁTÉK] hivatkozásban (Csákány Béla: Diszkrét
     * matematikai játékok. Polygon, Szeged 1998. 172. oldal.)
     *
     * @param   rács    a sejttér ahová ezt az állatkát helyezzük
     * @param   x       a befoglaló tégla bal felsõ sarkának oszlopa
     * @param   y       a befoglaló tégla bal felsõ sarkának sora
     */
    public void sikló(boolean [][] rács, int x, int y) {
        
        rács[y+ 0][x+ 2] = ÉLÕ;
        rács[y+ 1][x+ 1] = ÉLÕ;
        rács[y+ 2][x+ 1] = ÉLÕ;
        rács[y+ 2][x+ 2] = ÉLÕ;
        rács[y+ 2][x+ 3] = ÉLÕ;
        
    }
    /**
     * A sejttérbe "élõlényeket" helyezünk, ez a "sikló ágyú".
     * Adott irányban siklókat lõ ki.
     * Az élõlény ismertetését lásd például a
     * [MATEK JÁTÉK] hivatkozásban /Csákány Béla: Diszkrét
     * matematikai játékok. Polygon, Szeged 1998. 173. oldal./,
     * de itt az ábra hibás, egy oszloppal told még balra a 
     * bal oldali 4 sejtes négyzetet. A helyes ágyú rajzát 
     * lásd pl. az [ÉLET CIKK] hivatkozásban /Robert T. 
     * Wainwright: Life is Universal./ (Megemlíthetjük, hogy
     * mindkettõ tartalmaz két felesleges sejtet is.)
     *
     * @param   rács    a sejttér ahová ezt az állatkát helyezzük
     * @param   x       a befoglaló tégla bal felsõ sarkának oszlopa
     * @param   y       a befoglaló tégla bal felsõ sarkának sora
     */    
    public void siklóKilövõ(boolean [][] rács, int x, int y) {
        
        rács[y+ 6][x+ 0] = ÉLÕ;
        rács[y+ 6][x+ 1] = ÉLÕ;
        rács[y+ 7][x+ 0] = ÉLÕ;
        rács[y+ 7][x+ 1] = ÉLÕ;
        
        rács[y+ 3][x+ 13] = ÉLÕ;
        
        rács[y+ 4][x+ 12] = ÉLÕ;
        rács[y+ 4][x+ 14] = ÉLÕ;
        
        rács[y+ 5][x+ 11] = ÉLÕ;
        rács[y+ 5][x+ 15] = ÉLÕ;
        rács[y+ 5][x+ 16] = ÉLÕ;
        rács[y+ 5][x+ 25] = ÉLÕ;
        
        rács[y+ 6][x+ 11] = ÉLÕ;
        rács[y+ 6][x+ 15] = ÉLÕ;
        rács[y+ 6][x+ 16] = ÉLÕ;
        rács[y+ 6][x+ 22] = ÉLÕ;
        rács[y+ 6][x+ 23] = ÉLÕ;
        rács[y+ 6][x+ 24] = ÉLÕ;
        rács[y+ 6][x+ 25] = ÉLÕ;
        
        rács[y+ 7][x+ 11] = ÉLÕ;
        rács[y+ 7][x+ 15] = ÉLÕ;
        rács[y+ 7][x+ 16] = ÉLÕ;
        rács[y+ 7][x+ 21] = ÉLÕ;
        rács[y+ 7][x+ 22] = ÉLÕ;
        rács[y+ 7][x+ 23] = ÉLÕ;
        rács[y+ 7][x+ 24] = ÉLÕ;
        
        rács[y+ 8][x+ 12] = ÉLÕ;
        rács[y+ 8][x+ 14] = ÉLÕ;
        rács[y+ 8][x+ 21] = ÉLÕ;
        rács[y+ 8][x+ 24] = ÉLÕ;
        rács[y+ 8][x+ 34] = ÉLÕ;
        rács[y+ 8][x+ 35] = ÉLÕ;
        
        rács[y+ 9][x+ 13] = ÉLÕ;
        rács[y+ 9][x+ 21] = ÉLÕ;
        rács[y+ 9][x+ 22] = ÉLÕ;
        rács[y+ 9][x+ 23] = ÉLÕ;
        rács[y+ 9][x+ 24] = ÉLÕ;
        rács[y+ 9][x+ 34] = ÉLÕ;
        rács[y+ 9][x+ 35] = ÉLÕ;
        
        rács[y+ 10][x+ 22] = ÉLÕ;
        rács[y+ 10][x+ 23] = ÉLÕ;
        rács[y+ 10][x+ 24] = ÉLÕ;
        rács[y+ 10][x+ 25] = ÉLÕ;
        
        rács[y+ 11][x+ 25] = ÉLÕ;
        
    }
    /** Pillanatfelvételek készítése. */
    public void pillanatfelvétel(java.awt.image.BufferedImage felvetel) {
        // A pillanatfelvétel kép fájlneve
        StringBuffer sb = new StringBuffer();
        sb = sb.delete(0, sb.length());
        sb.append("sejtautomata");
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
    // Ne villogjon a felület (mert a "gyári" update()
    // lemeszelné a vászon felületét).    
    public void update(java.awt.Graphics g) {
        paint(g);
    }    
    /**
     * Példányosít egy Conway-féle életjáték szabályos
     * sejttér obektumot.
     */    
    public static void main(String[] args) {
        // 100 oszlop, 75 sor mérettel:
        new Sejtautomata(100, 75);
    }
}                
