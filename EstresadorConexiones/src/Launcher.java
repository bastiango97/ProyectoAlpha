import java.util.Arrays;

public class Launcher {

    public static int countOnes(int[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1) {
                count++;
            }
        }
        return count;
    }
    public static void main(String[] args) throws InterruptedException {
        int numeroPruebas =500;
        int[] lista = new int[numeroPruebas];

        for(int i = 0; i<numeroPruebas; i++) {
            ClientThread clientThread = new ClientThread(i, lista);
            clientThread.start();
        }


        Thread.sleep(5000);
        System.out.println(Arrays.toString(lista));
        double ones = countOnes(lista);
        double length = lista.length;
        double finalValue = ones/length;
        System.out.println(finalValue);
    }

}
