package game.safe;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {

    static final int number_of_thieves = 2;

    public static final int WAITING_TIME = 50;
    public static final int INIT_WAITING_TIME = 100;

    private AtomicBoolean gameOver = new AtomicBoolean(false);
    private AtomicInteger caught_count = new AtomicInteger(0);

    private String founder;

    public int getLuckyNumber() {
        return lucky_number;
    }

    private static final int lucky_number;

    static {
        lucky_number = (new Random()).nextInt(100) + 1;
        System.out.println("Safe number is " + lucky_number);
    }

    public void luckyFoundSafe(String name) {
        this.gameOver.getAndSet(true);
        founder = name;
    }

    public void thiefGotCaught() {
        int counter = caught_count.incrementAndGet();

        if (counter == number_of_thieves) {
            gameOver.getAndSet(true);
        }
    }

    public boolean isGameOver() {
        return gameOver.get();
    }

}
