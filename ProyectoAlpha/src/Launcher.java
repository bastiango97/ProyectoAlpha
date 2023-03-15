
public class Launcher {
    public static void main(String[] args) throws InterruptedException {
        //crea partida con los parametros
        int numeroJugadores =  3;
        int numeroRondas = 5;


        //crea los threads de cada jugador, se duermen 3 segundos para darle tiempo al server de iniciar
        //se inicia el server
        ClientThread clientThread1 = new ClientThread(0);
        clientThread1.start();
        ClientThread clientThread2 = new ClientThread(1);
        clientThread2.start();
        ClientThread clientThread3 = new ClientThread(2);
        clientThread3.start();
        new Servidor().main(numeroJugadores, numeroRondas);
        Thread.sleep(4000);
        System.exit(0);
    }
}
