public class PortScan {

    public static void main(String[] args) {
        for(int i=0; i<65535; ++i)
            try {
                java.net.Socket socket = new java.net.Socket(args[0], i);
                System.out.println(i + " figyeli");
                socket.close();

            } catch (Exception e) {
                System.out.println(i + " nem figyeli");
            }
    }
}
