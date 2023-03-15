import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.swing.*;
import java.awt.*;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
public class Hunter {
    JLabel puntuacion = new JLabel("Tu puntuación actual es: 0");
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject1 = "Monstruos";
    private static String subject2 = "Estado";
    private long tiempoTotal = 0;
    private int id;
    public void setID(int aID){
        id = aID;
    }
    public int getId(){
        return id;
    }

    public void getMessages(long[] unaLista) {

        boolean goodByeReceived = false;

        try {
            //crea la conexión y la sesion
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false /*Transacter*/, Session.AUTO_ACKNOWLEDGE);
            //nombre de Tópicos
            Destination destination1 = session.createTopic(subject1);
            Destination destination2 = session.createTopic(subject2);
            //consumidores a destinos
            MessageConsumer messageConsumer1 = session.createConsumer(destination1);
            MessageConsumer messageConsumer2 = session.createConsumer(destination2);


            //consume topicos de manera asíncrona
            messageConsumer1.setMessageListener(new MessListener());

            messageConsumer2.setMessageListener(new MessListener2());
            connection.start();
            Thread.sleep(8000);
            int _id = getId();
            unaLista[_id] = tiempoTotal/5;

            //termina la conexion

            messageConsumer1.close();
            messageConsumer2.close();
            session.close();
            connection.close();
            //int _id = getId();
            //unaLista[_id] = tiempoTotal;

        } catch (JMSException e) {
            e.printStackTrace();

        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //hacer otro message listener para cuando reciba mensaje del segundo tópico
    public class MessListener implements MessageListener {
        @Override
        public void onMessage(Message message) {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.print("Ataque en la casilla: ");
                System.out.println(textMessage.getText());
                System.out.println();
                Socket s = null;
                int serverPort = 49153;
                try {
                    s = new Socket("localhost", serverPort);
                    //s = new Socket("127.0.0.1", serverPort);
                    DataInputStream in = new DataInputStream(s.getInputStream());
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    long startTime = System.currentTimeMillis();
                    out.writeUTF(String.valueOf(id));            // UTF is a string encoding
                    String data = in.readUTF();
                    long spentTime = System.currentTimeMillis() - startTime;
                    System.out.println(spentTime);
                    tiempoTotal =tiempoTotal+  spentTime;
                    System.out.println(data);
                } catch (UnknownHostException ex) {
                    System.out.println("Sock:" + ex.getMessage());
                } catch (EOFException ex) {
                    System.out.println("EOF:" + ex.getMessage());
                } catch (IOException ex) {
                    System.out.println("IO:" + ex.getMessage());
                } finally {
                    if (s != null) try {
                        s.close();
                    } catch (IOException ex) {
                        System.out.println("close:" + ex.getMessage());
                    }
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
    public class MessListener2 implements MessageListener {
        @Override
        public void onMessage(Message message) {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println(textMessage.getText());
                System.out.println();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public void crearHunter(int aID, long[] unaLista) throws InterruptedException {
        Hunter h = new Hunter();
        h.setID(aID);
        Socket s = null;
        try {
            int serverPort= 49152;
            s = new Socket("localhost", serverPort);
            //s = new Socket("127.0.0.1", serverPort);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            int _ID = h.getId();
            out.writeUTF(String.valueOf(_ID));            // UTF is a string encoding
            String data = in.readUTF();
            System.out.println(data);
        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            if (s != null) try {
                s.close();
            } catch (IOException e) {
                System.out.println("close:" + e.getMessage());
            }
        }
        //h.constructorGUI();
        h.getMessages(unaLista);
    }
    /*public void reconnect(int aID) throws InterruptedException{
        Hunter h = new Hunter();
        h.setID(aID);
        h.getMessages();
    }*/
}
