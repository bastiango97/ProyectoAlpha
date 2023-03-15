public class ClientThread extends Thread{
    private volatile boolean isRunning = true;
    private int ID;
    public int[] lista;
    public ClientThread(int aID, int[] unaLista){
        ID = aID;
        lista = unaLista;
    }
    public void run(){
        Hunter h = new Hunter();
        h.crearHunter(ID, lista);

    }

}
