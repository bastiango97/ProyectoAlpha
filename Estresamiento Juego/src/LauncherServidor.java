public class LauncherServidor {
    public static void main(String[] args) throws InterruptedException {
        int numeroJugadores =  40;
        int numeroRondas = 5;
        new Servidor().main(numeroJugadores, numeroRondas);
    }
}
