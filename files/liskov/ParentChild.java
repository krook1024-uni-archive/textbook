class Szulo {
    public void print(String s) {
        System.out.println(s);
    }
}

class Gyermek extends Szulo {
    public void echo(String s) {
        System.out.println(s);
        System.out.println(s);
    }
}

public class ParentChild {
    public static void main(String[] args) {
        Szulo sz = new Szulo();
        Szulo sz2 = new Gyermek();

        // Ez az, ami nem megy
        sz2.echo("ballalalalalala");
    }
}
