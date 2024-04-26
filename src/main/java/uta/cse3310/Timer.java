package uta.cse3310;

public class Timer {
    private long startTime;
    private long durationInMillis;
    private boolean isRunning;
    private Runnable onTimeoutCallback;

    public Timer() {
        this.startTime = 0;
        this.durationInMillis = 30000; 
    }

    public void setTime(long timeInMillis) {
        this.durationInMillis = timeInMillis;
    }

    public void startTime() {
        this.startTime = System.currentTimeMillis();
        this.isRunning = true;
    }

   // public void start() {
    //    this.startTime = System.currentTimeMillis();
    //    this.isRunning = true;
 //   }

    public void setOnTimeoutCallback(Runnable callback) {
        this.onTimeoutCallback = callback;
    }

    public boolean isTimeUp() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        if (isRunning && elapsedTime >= durationInMillis) {
            isRunning = false;

            if (onTimeoutCallback != null) {
                onTimeoutCallback.run();
            }
            return true;
        }
        return false;
    }

    public void reset() {
        startTime = System.currentTimeMillis();
        isRunning = true;
    }
}
