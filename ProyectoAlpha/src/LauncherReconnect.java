public class LauncherReconnect {
    public static void main(String[] args) throws InterruptedException {
        Hunter hunter = new Hunter();
        hunter.reconnect(2);
    }
}
