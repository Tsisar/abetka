package ua.tsisar.abetka.game;

public class GameResult {
    private int answer;
    private int stars;
    private String date;

    public GameResult(int answer, int stars, String date) {
        this.answer = answer;
        this.stars = stars;
        this.date = date;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
