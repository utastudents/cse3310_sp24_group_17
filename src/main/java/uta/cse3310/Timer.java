package uta.cse3310;

public class Timer {
    private long startTime;
    private long durationInMillis;

    public void setTime(long durationInMillis) {
        this.durationInMillis = durationInMillis;
    }

    public void startTime(long start_time) {
        this.startTime = start_time;
    }

    public boolean isTimeUp() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - startTime) >= durationInMillis;
    }
}
