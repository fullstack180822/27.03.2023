package game.safe;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ThiefThread extends Thread {

    static Random random = new Random();

    protected volatile AtomicInteger random_number = new AtomicInteger(-1);

    protected volatile AtomicBoolean caught = new AtomicBoolean(false);
    private GameManager gameManager;

    public ThiefThread(String name, GameManager gameManager) {
        super(name);
        this.gameManager = gameManager;
    }

    public void policeGotToThisNumber(int number) {
        if (number == random_number.get()) {
            System.out.println("======================================");
            System.out.println("=========== police caught thief in " + number);
            System.out.println("======================================");
            caught.getAndSet(true);
            gameManager.thiefGotCaught();
        }
    }

    private void foundSafe() {
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$ " + getName() +
                " found safe !!! lucky number " +
                random_number.get());
        gameManager.luckyFoundSafe(getName());
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

    }

    @Override
    public void run() {
        while (!gameManager.isGameOver() && !caught.get()) {
            int next_random_number = random.nextInt(100 + 1);

            random_number.getAndSet(next_random_number);

            System.out.println(Thread.currentThread().getName() +
                    " random " + next_random_number);

            if (random_number.get() == gameManager.getLuckyNumber()) {
                foundSafe();
            }
            try {
                Thread.sleep(random.nextInt(GameManager.WAITING_TIME));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
