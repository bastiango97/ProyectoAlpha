import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
public class Servidor {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject1 = "Monstruos";
    private static String subject2 = "Estado";
    public void publicarTopicos(){
        MessageProducer messageProducer1;
        TextMessage textMessage1;
        MessageProducer messageProducer2;
        TextMessage textMessage2;

        try {
            //crear conexion
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination1 = session.createTopic(subject1);
            Destination destination2 = session.createTopic(subject2);

            messageProducer1 = session.createProducer(destination1);
            textMessage1 = session.createTextMessage();
            messageProducer2 = session.createProducer(destination2);
            textMessage2 = session.createTextMessage();
            int min = 1;
            int max = 9;
            int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
            textMessage1.setText(String.valueOf(randomNum));
            System.out.println("Sending monster in checkbox: " + textMessage1.getText());
            messageProducer1.send(textMessage1);

            /*
            Thread.sleep(3000);

            textMessage2.setText("Fin del Juego");
            System.out.println("Sending the following message: " + textMessage2.getText());
            messageProducer2.send(textMessage2);
            */

            //terminar conexión

            messageProducer1.close();
            messageProducer2.close();
            session.close();
            connection.close();


        } catch (JMSException e) {
            e.printStackTrace();

        } /*catch (InterruptedException e) {
        e.printStackTrace();
        }*/
    }
    public static void main(int numJugadores, int numRondas) throws InterruptedException {
        Partida unaPartida = new Partida(numJugadores, numRondas);
        try {
            int serverPort = 49152;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while(true) {
                System.out.println("Esperando a que se conecten los Hunters.");
                Socket clientSocket = listenSocket.accept();  // Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
                RegistroJugadores c = new RegistroJugadores(clientSocket, unaPartida);
                c.start();
            }
        } catch (IOException e) {
            System.out.println("Listen :" + e.getMessage());
        }
    }
}


class ConnectionTCP extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
    private Partida miPartida;

    public ConnectionTCP(Socket aClientSocket, Partida unaPartida) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            miPartida = unaPartida;
        } catch (IOException e) {
            System.out.println("Server.Connection:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String data = in.readUTF();
            System.out.println("El jugador: " + data + " contraatacó. Necesitamos mandar más fuerzas");
            miPartida.incrementarScore(Integer.parseInt(data));
            int scoreActual = miPartida.getScore(Integer.parseInt(data));
            String response = "Tu score actual es: " + scoreActual;
            out.writeUTF(response);
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

class RegistroJugadores extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
    private Partida miPartida;

    public RegistroJugadores(Socket aClientSocket, Partida unaPartida) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            miPartida = unaPartida;
        } catch (IOException e) {
            System.out.println("Server.Connection:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String data = in.readUTF();
            System.out.println("Hunter " + data + " conectado.");
            int scoreActual = miPartida.getScore(Integer.parseInt(data));
            String response = "Jugador con ID: " +data +". "+"Tu score actual es: " + scoreActual;
            out.writeUTF(response);
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}