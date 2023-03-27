package game.safe;

import java.util.Random;

public class Main {

    static Random random = new Random();

    private static void runGameWithPolice() {

        GameManager game = new GameManager();

        ThiefThread thiefThreadFirst = new ThiefThread("thief one", game);
        ThiefThread thiefThreadSecond = new ThiefThread("thief two", game);

        thiefThreadFirst.start();
        thiefThreadSecond.start();

        Thread.yield();

        while (!game.isGameOver()) {

            int next_random_number = random.nextInt(100 + 1);
            System.out.println("* Police got to this number " + next_random_number);

            if (thiefThreadFirst.isAlive()) {
                thiefThreadFirst.policeGotToThisNumber(next_random_number);
            }
            if (thiefThreadSecond.isAlive()) {
                thiefThreadSecond.policeGotToThisNumber(next_random_number);
            }
            try {
                Thread.sleep(random.nextInt(GameManager.WAITING_TIME));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        (new Thread(Main::runGameWithPolice)).start();

    }

}
