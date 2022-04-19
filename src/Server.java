import javax.sound.sampled.Port;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

// Con implements Runneable possiamo instanziare la classe Server su un thread
public class Server implements Runnable {
    // Array byte per i dati del pacchetto
    private byte[] data = new byte[256];
    // Oggetto socket per l'invio e la ricezione dei pacchetti
    private DatagramSocket socket;
    private boolean isRunning;

    // Costruttore di default
    public Server() throws SocketException {
        socket = new DatagramSocket(5678);
    }


    @Override // del metodo run dell'interfaccia Runneable
    public void run() {
        isRunning = true;

        // Ciclo loop per l'esecuzione del server
        while (isRunning) {
            // Creazione di un nuovo pacchetto
            DatagramPacket packet = new DatagramPacket(data, data.length);

            // Try e catch per la ricezione del pacchetto
            try {
                // .receive(arg) è un metodo blocking, ovvero che blocca
                // l'esecuzione del programma finchè non riceve un pacchetto
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Creazione e stampa della stringa contenente il messaggio
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.print("L'utente ha scritto: " + message);

            // Reinvio della stringa al client aggiungendoci la scritta "Server: "
            // e convertendola in byte[]
            InetAddress ip = packet.getAddress();
            int port = packet.getPort();
            message = "Server: " + message;
            data = message.getBytes(StandardCharsets.UTF_8);

            // Creazione e invio del pacchetto al client
            packet = new DatagramPacket(data, data.length, ip, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static void main(String[] args) {
        try {
            new Server().run();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
