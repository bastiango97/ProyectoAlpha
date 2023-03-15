import java.io.Serializable;
public class Jugador {
    private int ID;
    private int score;
    public Jugador(){
        ID = 0;
        score = 0;
    }
    public Jugador(int aID, int aScore){
        ID = aID;
        score = aScore;
    }

    public int getID() {
        return ID;
    }

    public int getScore() {
        return score;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
