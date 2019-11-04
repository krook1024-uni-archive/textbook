/*
 * Sejtautomata.java
 *
 * DIGIT 2005, Javat tanitok
 * Batfai Norbert, nbatfai@inf.unideb.hu
 *
 */
/**
 * Sejtautomata osztaly.
 *
 * @author Batfai Norbert, nbatfai@inf.unideb.hu
 * @version 0.0.1
 */
public class Sejtautomata extends java.awt.Frame implements Runnable {
    /** Egy sejt lehet elo */
    public static final boolean ELO = true;
    /** vagy halott */
    public static final boolean HALOTT = false;
    /** Ket racsot hasznalunk majd, az egyik a sejtter allapotat
     * a t_n, a masik a t_n+1 idopillanatban jellemzi. */
    protected boolean [][][] racsok = new boolean [2][][];
    /** Valamelyik racsra mutat, technikai jellegu, hogy ne kelljen a
     * [2][][]-bol az elso dimenziot hasznalni, mert vagy az egyikre
     * allitjuk, vagy a masikra. */
    protected boolean [][] racs;
    /** Megmutatja melyik racs az aktualis: [racsIndex][][] */
    protected int racsIndex = 0;
    /** Pixelben egy cella adatai. */
    protected int cellaSzelesseg = 20;
    protected int cellaMagassag = 20;
    /** A sejtter nagysaga, azaz hanyszor hany cella van? */
    protected int szelesseg = 20;
    protected int magassag = 10;
    /** A sejtter ket egymast koveto t_n es t_n+1 diszkret idopillanata
     kozotti valos ido. */  
    protected int varakozas = 1000;
    // Pillanatfelvetel keszitesehez
    private java.awt.Robot robot;
    /** Keszitsunk pillanatfelvetelt? */
    private boolean pillanatfelvetel = false;
    /** A pillanatfelvetelek szamozasahoz. */
    private static int pillanatfelvetelSzamlalo = 0;
    /**
     * Letrehoz egy <code>Sejtautomata</code> objektumot.
     *
     * @param      szelesseg    a sejtter szelessege.
     * @param      magassag     a sejtter szelessege.
     */
    public Sejtautomata(int szelesseg, int magassag) {
        this.szelesseg = szelesseg;
        this.magassag = magassag;
        // A ket racs elkeszitese
        racsok[0] = new boolean[magassag][szelesseg];
        racsok[1] = new boolean[magassag][szelesseg];
        racsIndex = 0;
        racs = racsok[racsIndex];
        // A kiindulo racs minden cellaja HALOTT
        for(int i=0; i<racs.length; ++i)
            for(int j=0; j<racs[0].length; ++j)
                racs[i][j] = HALOTT;
        // A kiindulo racsra "elolenyeket" helyezunk
        //siklo(racs, 2, 2);
        sikloKilovo(racs, 5, 60);
        // Az ablak bezarasakor kilepunk a programbol.
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                setVisible(false);
                System.exit(0);
            }
        });
        // A billentyuzetrol erkezo esemenyek feldolgozasa
        addKeyListener(new java.awt.event.KeyAdapter() {
            // Az 'k', 'n', 'l', 'g' es 's' gombok lenyomasat figyeljuk
            public void keyPressed(java.awt.event.KeyEvent e) {
                if(e.getKeyCode() == java.awt.event.KeyEvent.VK_K) {
                    // Felezuk a cella mereteit:
                    cellaSzelesseg /= 2;
                    cellaMagassag /= 2;
                    setSize(Sejtautomata.this.szelesseg*cellaSzelesseg,
                            Sejtautomata.this.magassag*cellaMagassag);
                    validate();
                } else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_N) {
                    // Duplazzuk a cella mereteit:
                    cellaSzelesseg *= 2;
                    cellaMagassag *= 2;
                    setSize(Sejtautomata.this.szelesseg*cellaSzelesseg,
                            Sejtautomata.this.magassag*cellaMagassag);
                    validate();
                } else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_S)
                    pillanatfelvetel = !pillanatfelvetel;
                else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_G)
                    varakozas /= 2;
                else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_L)
                    varakozas *= 2;
                repaint();
            }
        });
        // Eger kattinto esemenyek feldolgozasa:
        addMouseListener(new java.awt.event.MouseAdapter() {
            // Eger kattintassal jeloljuk ki a nagyitando teruletet
            // bal felso sarkat vagy ugyancsak eger kattintassal
            // vizsgaljuk egy adott pont iteracioit:
            public void mousePressed(java.awt.event.MouseEvent m) {
                // Az egermutato pozicioja
                int x = m.getX()/cellaSzelesseg;
                int y = m.getY()/cellaMagassag;
                racsok[racsIndex][y][x] = !racsok[racsIndex][y][x];
                repaint();
            }
        });
        // Eger mozgas esemenyek feldolgozasa:
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            // Vonszolassal jeloljuk ki a negyzetet:
            public void mouseDragged(java.awt.event.MouseEvent m) {
                int x = m.getX()/cellaSzelesseg;
                int y = m.getY()/cellaMagassag;
                racsok[racsIndex][y][x] = ELO;
                repaint();
            }
        });
        // Cellameretek kezdetben
        cellaSzelesseg = 10;
        cellaMagassag = 10;
        // Pillanatfelvetel keszitesehez:
        try {
            robot = new java.awt.Robot(
                    java.awt.GraphicsEnvironment.
                    getLocalGraphicsEnvironment().
                    getDefaultScreenDevice());
        } catch(java.awt.AWTException e) {
            e.printStackTrace();
        }
        // A program ablakanak adatai:
        setTitle("Sejtautomata");
        setResizable(false);
        setSize(szelesseg*cellaSzelesseg,
                magassag*cellaMagassag);
        setVisible(true);
        // A sejtter eletrekeltese:
        new Thread(this).start();
    }
    /** A sejtter kirajzolasa. */
    public void paint(java.awt.Graphics g) {
        // Az aktualis
        boolean [][] racs = racsok[racsIndex];
        // racsot rajzoljuk ki:
        for(int i=0; i<racs.length; ++i) { // vegig lepked a sorokon
            for(int j=0; j<racs[0].length; ++j) { // s az oszlopok
                // Sejt cella kirajzolasa
                if(racs[i][j] == ELO)
                    g.setColor(java.awt.Color.BLACK);
                else
                    g.setColor(java.awt.Color.WHITE);
                g.fillRect(j*cellaSzelesseg, i*cellaMagassag,
                        cellaSzelesseg, cellaMagassag);
                // Racs kirajzolasa
                g.setColor(java.awt.Color.LIGHT_GRAY);
                g.drawRect(j*cellaSzelesseg, i*cellaMagassag,
                        cellaSzelesseg, cellaMagassag);
            }
        }
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
                    szelesseg*cellaSzelesseg,
                    magassag*cellaMagassag)));
        }
    }
    /**
     * Az kerdezett allapotban levo nyolcszomszedok szama.
     *
     * @param   racs    a sejtter racs
     * @param   sor     a racs vizsgalt sora
     * @param   oszlop  a racs vizsgalt oszlopa
     * @param   allapor a nyolcszomszedok vizsgalt allapota
     * @return int a kerdezett allapotbeli nyolcszomszedok szama.
     */
    public int szomszedokSzama(boolean [][] racs,
            int sor, int oszlop, boolean allapot) {        
        int allapotuSzomszed = 0;
        // A nyolcszomszedok vegigzongorazasa:
        for(int i=-1; i<2; ++i)
            for(int j=-1; j<2; ++j)
                // A vizsgalt sejtet magat kihagyva:
                if(!((i==0) && (j==0))) {
            // A sejtterbol szelenek szomszedai
            // a szembe oldalakon ("periodikus hatarfeltetel")
            int o = oszlop + j;
            if(o < 0)
                o = szelesseg-1;
            else if(o >= szelesseg)
                o = 0;
            
            int s = sor + i;
            if(s < 0)
                s = magassag-1;
            else if(s >= magassag)
                s = 0;
            
            if(racs[s][o] == allapot)
                ++allapotuSzomszed;
                }
        
        return allapotuSzomszed;
    }
    /**
     * A sejtter idobeli fejlodese a John H. Conway fele
     * eletjatek sejtautomata szabalyai alapjan tortenik.
     * A szabalyok reszletes ismerteteset lasd peldaul a
     * [MATEK JATEK] hivatkozasban (Csakany Bela: Diszkret
     * matematikai jatekok. Polygon, Szeged 1998. 171. oldal.)
     */
    public void idoFejlodes() {
        
        boolean [][] racsElotte = racsok[racsIndex];
        boolean [][] racsUtana = racsok[(racsIndex+1)%2];
        
        for(int i=0; i<racsElotte.length; ++i) { // sorok
            for(int j=0; j<racsElotte[0].length; ++j) { // oszlopok
                
                int elok = szomszedokSzama(racsElotte, i, j, ELO);
                
                if(racsElotte[i][j] == ELO) {
                /* Elo elo marad, ha ketto vagy harom elo
                 szomszedja van, kulonben halott lesz. */
                    if(elok==2 || elok==3)
                        racsUtana[i][j] = ELO;
                    else
                        racsUtana[i][j] = HALOTT;
                }  else {
                /* Halott halott marad, ha harom elo
                 szomszedja van, kulonben elo lesz. */
                    if(elok==3)
                        racsUtana[i][j] = ELO;
                    else
                        racsUtana[i][j] = HALOTT;
                }
            }
        }
        racsIndex = (racsIndex+1)%2;
    }
    /** A sejtter idobeli fejlodese. */
    public void run() {
        
        while(true) {
            try {
                Thread.sleep(varakozas);
            } catch (InterruptedException e) {}
            
            idoFejlodes();
            repaint();
        }
    }
    /**
     * A sejtterbe "elolenyeket" helyezunk, ez a "siklo".
     * Adott iranyban halad, masolja magat a sejtterben.
     * Az eloleny ismerteteset lasd peldaul a
     * [MATEK JATEK] hivatkozasban (Csakany Bela: Diszkret
     * matematikai jatekok. Polygon, Szeged 1998. 172. oldal.)
     *
     * @param   racs    a sejtter ahova ezt az allatkat helyezzuk
     * @param   x       a befoglalo tegla bal felso sarkanak oszlopa
     * @param   y       a befoglalo tegla bal felso sarkanak sora
     */
    public void siklo(boolean [][] racs, int x, int y) {
        
        racs[y+ 0][x+ 2] = ELO;
        racs[y+ 1][x+ 1] = ELO;
        racs[y+ 2][x+ 1] = ELO;
        racs[y+ 2][x+ 2] = ELO;
        racs[y+ 2][x+ 3] = ELO;
        
    }
    /**
     * A sejtterbe "elolenyeket" helyezunk, ez a "siklo agyu".
     * Adott iranyban siklokat lo ki.
     * Az eloleny ismerteteset lasd peldaul a
     * [MATEK JATEK] hivatkozasban /Csakany Bela: Diszkret
     * matematikai jatekok. Polygon, Szeged 1998. 173. oldal./,
     * de itt az abra hibas, egy oszloppal told meg balra a 
     * bal oldali 4 sejtes negyzetet. A helyes agyu rajzat 
     * lasd pl. az [ELET CIKK] hivatkozasban /Robert T. 
     * Wainwright: Life is Universal./ (Megemlithetjuk, hogy
     * mindketto tartalmaz ket felesleges sejtet is.)
     *
     * @param   racs    a sejtter ahova ezt az allatkat helyezzuk
     * @param   x       a befoglalo tegla bal felso sarkanak oszlopa
     * @param   y       a befoglalo tegla bal felso sarkanak sora
     */    
    public void sikloKilovo(boolean [][] racs, int x, int y) {
        
        racs[y+ 6][x+ 0] = ELO;
        racs[y+ 6][x+ 1] = ELO;
        racs[y+ 7][x+ 0] = ELO;
        racs[y+ 7][x+ 1] = ELO;
        
        racs[y+ 3][x+ 13] = ELO;
        
        racs[y+ 4][x+ 12] = ELO;
        racs[y+ 4][x+ 14] = ELO;
        
        racs[y+ 5][x+ 11] = ELO;
        racs[y+ 5][x+ 15] = ELO;
        racs[y+ 5][x+ 16] = ELO;
        racs[y+ 5][x+ 25] = ELO;
        
        racs[y+ 6][x+ 11] = ELO;
        racs[y+ 6][x+ 15] = ELO;
        racs[y+ 6][x+ 16] = ELO;
        racs[y+ 6][x+ 22] = ELO;
        racs[y+ 6][x+ 23] = ELO;
        racs[y+ 6][x+ 24] = ELO;
        racs[y+ 6][x+ 25] = ELO;
        
        racs[y+ 7][x+ 11] = ELO;
        racs[y+ 7][x+ 15] = ELO;
        racs[y+ 7][x+ 16] = ELO;
        racs[y+ 7][x+ 21] = ELO;
        racs[y+ 7][x+ 22] = ELO;
        racs[y+ 7][x+ 23] = ELO;
        racs[y+ 7][x+ 24] = ELO;
        
        racs[y+ 8][x+ 12] = ELO;
        racs[y+ 8][x+ 14] = ELO;
        racs[y+ 8][x+ 21] = ELO;
        racs[y+ 8][x+ 24] = ELO;
        racs[y+ 8][x+ 34] = ELO;
        racs[y+ 8][x+ 35] = ELO;
        
        racs[y+ 9][x+ 13] = ELO;
        racs[y+ 9][x+ 21] = ELO;
        racs[y+ 9][x+ 22] = ELO;
        racs[y+ 9][x+ 23] = ELO;
        racs[y+ 9][x+ 24] = ELO;
        racs[y+ 9][x+ 34] = ELO;
        racs[y+ 9][x+ 35] = ELO;
        
        racs[y+ 10][x+ 22] = ELO;
        racs[y+ 10][x+ 23] = ELO;
        racs[y+ 10][x+ 24] = ELO;
        racs[y+ 10][x+ 25] = ELO;
        
        racs[y+ 11][x+ 25] = ELO;
        
    }
    /** Pillanatfelvetelek keszitese. */
    public void pillanatfelvetel(java.awt.image.BufferedImage felvetel) {
        // A pillanatfelvetel kep fajlneve
        StringBuffer sb = new StringBuffer();
        sb = sb.delete(0, sb.length());
        sb.append("sejtautomata");
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
    // Ne villogjon a felulet (mert a "gyari" update()
    // lemeszelne a vaszon feluletet).    
    public void update(java.awt.Graphics g) {
        paint(g);
    }    
    /**
     * Peldanyosit egy Conway-fele eletjatek szabalyos
     * sejtter obektumot.
     */    
    public static void main(String[] args) {
        // 100 oszlop, 75 sor merettel:
        new Sejtautomata(100, 75);
    }
}                
