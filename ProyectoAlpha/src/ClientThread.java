public class ClientThread extends Thread{
    private volatile boolean isRunning = true;
    private int ID;
    public ClientThread(int aID){
        ID = aID;
    }
    public void run(){
        Hunter h = new Hunter();
        try {
            h.crearHunter(ID);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
