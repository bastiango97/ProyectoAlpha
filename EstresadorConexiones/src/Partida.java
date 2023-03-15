import java.util.Arrays;

public class Partida {


    private Jugador[] miPartida;
    private int size;
    private int rondas;
    public Partida(int n, int r){
        miPartida = new Jugador[n];
        size = n;
        fillPartida(n);
        rondas = r;
    }

    private void fillPartida(int n){
        for (int i = 0; i < size; i++) {
            miPartida[i] = new Jugador(i, 0);
        }
    }
    public int maxScore(){
        int score = 0;
        int n = miPartida.length;
        for(int i = 0; i<n; i++){
            if (miPartida[i].getScore()>=score){
                score = miPartida[i].getScore();
                score = score +1;
            }
        }
        return score;
    }
    public void incrementarScore(int n){
        int nuevoScore = miPartida[n].getScore() + 1;
        miPartida[n].setScore(nuevoScore);
    }
    public int getScore(int n){
        int scoreActual = 0;
        scoreActual = miPartida[n].getScore();
        return scoreActual;
    }
}
