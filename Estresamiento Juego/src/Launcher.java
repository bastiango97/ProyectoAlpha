import java.util.Arrays;

public class Launcher {
    private static double mean(long[] list) {
        double sum = 0.0;
        double num = 0.0;
        for (int i = 0; i < list.length; i++)
            sum += list[i];
        double mean = sum / list.length;
        return mean;
    }
    private static double stdDev(long[] list) {
        double sum = 0.0;
        double num = 0.0;
        for (int i = 0; i < list.length; i++)
            sum += list[i];
        double mean = sum / list.length;
        for (int i = 0; i < list.length; i++)
            num += Math.pow((list[i] - mean), 2);
        return Math.sqrt(num / list.length);
    }
    public static void main(String[] args) throws InterruptedException {

        int numeroPruebas =40;
        int numeroRondas = 5;
        long[] lista = new long[numeroPruebas];

        for(int i = 0; i<numeroPruebas; i++) {
            ClientThread clientThread = new ClientThread(i, lista);
            clientThread.start();
        }


        Thread.sleep(15000);

        System.out.println(Arrays.toString(lista));
        double mean = mean(lista);
        double std = stdDev(lista);
        System.out.println("mean: " +  mean);
        System.out.println("std: " + std);
    }
}
