import java.util.concurrent.atomic.AtomicBoolean;

public class Governor
{
    static volatile boolean paused;
	static volatile boolean quit;
	static final int DIFFICULTY = 20;
	static volatile AtomicBoolean updatePending = new AtomicBoolean(false);
	static volatile AtomicBoolean scoreUpdatePending = new AtomicBoolean(false);
	static volatile AtomicBoolean checkWord = new AtomicBoolean(false);
}
