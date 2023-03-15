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
    JFrame frame = new JFrame("Hunter");
    JCheckBox hole1 = new JCheckBox("Hole 1");
    JCheckBox hole2 = new JCheckBox("Hole 2");
    JCheckBox hole3 = new JCheckBox("Hole 3");
    JCheckBox hole4 = new JCheckBox("Hole 4");
    JCheckBox hole5 = new JCheckBox("Hole 5");
    JCheckBox hole6 = new JCheckBox("Hole 6");
    JCheckBox hole7 = new JCheckBox("Hole 7");
    JCheckBox hole8 = new JCheckBox("Hole 8");
    JCheckBox hole9 = new JCheckBox("Hole 9");
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject1 = "Monstruos";
    private static String subject2 = "Estado";
    private int id;
    public void setID(int aID){
        id = aID;
    }
    public int getId(){
        return id;
    }
    int globalVar = 0;
    public void constructorGUI(){
        frame.setTitle("Hunter" + id);
        frame.add(puntuacion);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setSize(220, 170);
        frame.setLocationRelativeTo(null);
        frame.add(hole1);
        frame.add(hole2);
        frame.add(hole3);
        frame.add(hole4);
        frame.add(hole5);
        frame.add(hole6);
        frame.add(hole7);
        frame.add(hole8);
        frame.add(hole9);
        frame.setVisible(true);
    }

    public void getMessages() {

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

            messageConsumer2.setMessageListener(new MessListener());
            connection.start();
            //Thread.sleep(600000);

            //termina la conexion
            /*
            messageConsumer1.close();
            messageConsumer2.close();
            session.close();
            connection.close();
*/
        } catch (JMSException e) {
            e.printStackTrace();

        }/* catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public class MessListener implements MessageListener {
        @Override
        public void onMessage(Message message) {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.print("Ataque en la casilla: ");
                System.out.println(textMessage.getText());
                System.out.println();
                atacar(textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
    public void whack(JCheckBox hole){
        int INTERVAL = 20; // 1 second
        final int TIMEOUT = 2000; // 2 seconds
        Timer timer2 = new Timer(INTERVAL, null);
        timer2.addActionListener(new ActionListener() {
            int elapsedTime = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!hole.isSelected()) {
                    System.out.println("Atacaste al monstruo en le tiempo indicado");
                    //envía respuesta TCP
                    Socket s = null;
                    int serverPort = 49153;
                    try {
                        s = new Socket("localhost", serverPort);
                        //s = new Socket("127.0.0.1", serverPort);
                        DataInputStream in = new DataInputStream(s.getInputStream());
                        DataOutputStream out = new DataOutputStream(s.getOutputStream());
                        out.writeUTF(String.valueOf(id));            // UTF is a string encoding
                        String data = in.readUTF();
                        puntuacion.setText("Hunter: " + id + ". " + data);
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
                    timer2.stop();
                }
                elapsedTime += INTERVAL;
                if (elapsedTime >= TIMEOUT) {
                    System.out.println("No atacaste al monstruo en el tiempo indicado");
                    timer2.stop();
                    hole.setSelected(false);
                }
            }
        });
        timer2.start();
    }
    public void limpiarCheckboxes(){
        hole1.setSelected(false);
        hole2.setSelected(false);
        hole3.setSelected(false);
        hole4.setSelected(false);
        hole5.setSelected(false);
        hole6.setSelected(false);
        hole7.setSelected(false);
        hole8.setSelected(false);
        hole9.setSelected(false);
    }
    public void atacar(String n){
        if (n == null){
            return;
        } else {
            switch(n){
                case "1":{
                    hole1.setSelected(true);
                    whack(hole1);
                    break;
                }
                case "2":{
                    hole2.setSelected(true);
                    whack(hole2);
                    break;
                }
                case "3":{
                    hole3.setSelected(true);
                    whack(hole3);
                    break;
                }
                case "4":{
                    hole4.setSelected(true);
                    whack(hole4);
                    break;
                }
                case "5":{
                    hole5.setSelected(true);
                    whack(hole5);
                    break;
                }
                case "6":{
                    hole6.setSelected(true);
                    whack(hole6);
                    break;
                }
                case "7":{
                    hole7.setSelected(true);
                    whack(hole7);
                    break;
                }
                case "8":{
                    hole8.setSelected(true);
                    whack(hole8);
                    break;
                }
                case "9":{
                    hole9.setSelected(true);
                    whack(hole9);
                    break;
                }
            }
        }
    }
    Socket s = null;

    public Socket getSocekt(){
        return s;
    }
    public void crearHunter(int aID, int[] unaLista) {
        Hunter h = new Hunter();
        h.setID(aID);
        Socket s = null;
        int _ID = h.getId();
        long spentTime= 100;
        try {
            int serverPort= 49152;
            s = new Socket("localhost", serverPort);
            //s = new Socket("127.0.0.1", serverPort);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            long startTime = System.currentTimeMillis();
            out.writeUTF(String.valueOf(_ID));            // UTF is a string encoding
            String data = in.readUTF();
            spentTime = System.currentTimeMillis() - startTime;
            unaLista[_ID] = 1;
            //System.out.println(spentTime);
            //System.out.println(data);
        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        }

        finally {
            if (s != null) try {
                s.close();
            } catch (IOException e) {
                System.out.println("close:" + e.getMessage());
            }
        }
        //h.constructorGUI();
        //h.getMessages();
    }
    public void reconnect(int aID) throws InterruptedException{
        Hunter h = new Hunter();
        h.setID(aID);
        h.constructorGUI();
        h.getMessages();
    }
}
