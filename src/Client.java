import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Client {
    // Dichiarazione attributi della classe Client
    private DatagramSocket socket;
    private InetAddress ip; //InetAddress è il tipo per gli indirizzi ip

    private byte[] data;

    // Costruttore della classe Client
    public Client() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        // Utilizziamo indirizzo ip della macchina locale
        ip = InetAddress.getByName("localhost");
    }

    // Metodo per mandare un pacchetto contentente il nostro messaggio
    // Resta in attesa per la risposta del server
    public void sendPacket(String msg) throws IOException {
        data = msg.getBytes(); // Convertire da stringa a byte[]
        // Creazione del pacchetto con ip, porta e il messaggio
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, 5678);
        // Invio del pacchetto
        socket.send(packet);

        packet = new DatagramPacket(data, data.length);
        // Metodo bloccante per ricevere il pacchetto
        socket.receive(packet);

        // COnvertire da byte[] a String e stampa
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println(received);

    }

    // Metodo per chiudere la socket
    public void close() {
        socket.close();
    }

    public static void main(String[] args) {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader read = new BufferedReader(input);
        String msg = "";
        try {
            // Invocazione del metodo per inviare il pacchetto
            msg = read.readLine();
            new Client().sendPacket(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
