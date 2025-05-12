package OOP;

import java.io.*;
import java.util.*;

interface KisGepjarmu {
    boolean haladhatItt(int sebesseg);
}

abstract class Jarmu {
    private String rendszam;
    protected int aktualisSebesseg;

    public Jarmu(String rendszam, int aktualisSebesseg) {
        this.rendszam = rendszam;
        this.aktualisSebesseg = aktualisSebesseg;
    }

    public abstract boolean gyorshajtottE(int sebessegkorlat);

    @Override
    public String toString() {
        return rendszam + " - " + aktualisSebesseg + " km/h";
    }

    public String getRendszam() {
        return rendszam;
    }
}

class Robogo extends Jarmu implements KisGepjarmu {
    private int maxSebesseg;

    public Robogo(String rendszam, int aktualisSebesseg, int maxSebesseg) {
        super(rendszam, aktualisSebesseg);
        this.maxSebesseg = maxSebesseg;
    }

    @Override
    public boolean gyorshajtottE(int sebessegkorlat) {
        return aktualisSebesseg > sebessegkorlat;
    }

    @Override
    public boolean haladhatItt(int sebesseg) {
        return maxSebesseg <= sebesseg;
    }

    @Override
    public String toString() {
        return "Robogo: " + super.toString();
    }
}

class AudiS8 extends Jarmu {
    private boolean lezerBlokkolo;

    public AudiS8(String rendszam, int aktualisSebesseg, boolean lezerBlokkolo) {
        super(rendszam, aktualisSebesseg);
        this.lezerBlokkolo = lezerBlokkolo;
    }

    @Override
    public boolean gyorshajtottE(int sebessegkorlat) {
        if (lezerBlokkolo) {
            return false;
        }
        return aktualisSebesseg > sebessegkorlat;
    }

    @Override
    public String toString() {
        return "Audi: " + super.toString();
    }
}

public class OOP {
    static List<Jarmu> jarmuvek = new ArrayList<>();

    public static void jarmuvekJonnek(String fajlNev) {
        try (BufferedReader br = new BufferedReader(new FileReader(fajlNev))) {
            String sor;
            while ((sor = br.readLine()) != null) {
                try {
                    String[] adatok = sor.split(";");
                    String tipus = adatok[0];
                    String rendszam = adatok[1];
                    int sebesseg = Integer.parseInt(adatok[2]);

                    if (tipus.equals("Robogo")) {
                        int maxSebesseg = Integer.parseInt(adatok[3]);
                        jarmuvek.add(new Robogo(rendszam, sebesseg, maxSebesseg));
                    } else if (tipus.equals("Audi")) {
                        boolean lezer = Boolean.parseBoolean(adatok[3]);
                        jarmuvek.add(new AudiS8(rendszam, sebesseg, lezer));
                    }
                } catch (Exception e) {
                    System.out.println("Hibás sor: " + sor + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Hiba a fájl beolvasása közben: " + e.getMessage());
        }
    }

    public static void kiketMertunkBe() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("buntetes.txt"))) {
            for (Jarmu j : jarmuvek) {
                pw.println(j.toString());

                if (j instanceof AudiS8) {
                    pw.println("Gyorshajtott: " + j.gyorshajtottE(90));
                } else if (j instanceof Robogo) {
                    Robogo r = (Robogo) j;
                    pw.println("Haladhat itt: " + r.haladhatItt(90));
                }

                pw.println(); // üres sor
            }
        } catch (IOException e) {
            System.out.println("Hiba a buntetes.txt írása közben: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        jarmuvekJonnek("adatok.txt");
        kiketMertunkBe();
    }
}
