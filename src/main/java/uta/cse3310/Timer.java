package uta.cse3310;

public class Timer {
    private long startTime;
    private long durationInMillis;
    private boolean isRunning;
    private Runnable timeUpCallback; 

    public void setTime(long timeInMillis) {
        this.durationInMillis = timeInMillis;
    }

    
    public void startTime() {
        this.startTime = System.currentTimeMillis();
        this.isRunning = true;
    }

 
    public void setTimeUpCallback(Runnable callback) {
        this.timeUpCallback = callback;
    }

  
    public boolean isTimeUp() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        if (isRunning && elapsedTime >= durationInMillis) {
            isRunning = false;

          
            if (timeUpCallback != null) {
                timeUpCallback.run();
            }
            return true;
        }
        return false;
    }
}
