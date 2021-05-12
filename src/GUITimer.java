public class GUITimer {
    private int timer;
    private long startTime, currTime, timeChange, pauseTime;
    private int roundTime = 45;
    private boolean paused = false;

    public static GUITimer instance;

    private GUITimer() {

        startTime = System.currentTimeMillis();
    }

    public static GUITimer getInstance() {
        if (instance == null)
            instance = new GUITimer();
        return instance;
    }

    public int getTimer() {
        if (!paused)
            currTime = System.currentTimeMillis();
        timeChange = (currTime - startTime);
        timer = (int) ((roundTime - timeChange / 1000));
        return timer;

    }

    public void resetTimer() {
        startTime = System.currentTimeMillis();
    }

    public void pauseTimer() {
        if (!paused) {

            pauseTime = System.currentTimeMillis();
            paused = true;
        }
    }

    public void resumeTimer() {
        if (paused) {

            paused = false;
            currTime = System.currentTimeMillis();
            startTime += currTime - pauseTime;
        }

    }

    public boolean isPaused() {
        return paused;
    }

}
