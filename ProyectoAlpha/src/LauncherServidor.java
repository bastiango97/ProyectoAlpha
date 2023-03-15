public class LauncherServidor {
    public static void main(String[] args) throws InterruptedException {
        int numeroJugadores =  3;
        int numeroRondas = 5;
        new Servidor().main(numeroJugadores, numeroRondas);
    }
}
