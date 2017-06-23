import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class CombatRing implements Runnable {

	// Combat variables
	boolean running = true;
	double velocity = 10;
	boolean isOver = false;
	// bots variables
	public static boolean bot_1_detect = true;
	long bot_1_detected = 0L;
	long bot_1_detect_timeout = 1000L;
	public static boolean bot_2_detect = true;
	long bot_2_detect_timeout = 1000L;
	long bot_2_detected = 0L;
	static int bot1_score = 0;
	static int bot2_score = 0;
	String bot1_name = "";
	String bot2_name = "";
	Point2D pos0 = new Point2D.Double(100, 100);
	Point2D pos1 = new Point2D.Double(GUI.width - 100, GUI.height - 100);
	ArrayList<LogicThinking> botsList = new ArrayList<>();
	// GUI variables
	GUI gui = null;
	int fps = 60;
	int sleeptime = 10;
	int hits = 0;

	public CombatRing() {
		System.out.println("Score: " + bot1_score + " - " + bot2_score);
		gui = new GUI();
		gui.start_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isOver) {
					reset();
				} else {
					reset();
				}

			}
		});

		// Creating 1st bot
		botsList.add(new Player1Logic(pos0, 0));
		// Creating 2nd bot
		botsList.add(new Player2Logic(pos1, Math.random() * 360));
		System.out.println("Created bots 1 and 2");

		startFight();
	}

	public void startFight() {
		System.out.println("Starting fight");
		updateNames();
	}

	void reset() {
		gui.dispose();
		botsList.clear();

		new Thread(new CombatRing()).start(); // Create Thread, CombatRing and
												// start() it

	}

	void updateNames() {
		bot1_name = botsList.get(0).player_1.bot1.name;
		bot2_name = botsList.get(1).player_2.bot1.name;
		gui.update_score(bot1_name, bot2_name, bot1_score, bot2_score);
	}

	public void stopFight() {
		System.out.println("Stopping thread: " + Thread.currentThread());
		if (Thread.currentThread() != null) {
			try {
				Thread.currentThread().join();
			} catch (Exception e) {
				System.out.println("Couldn't stop threads");
				e.printStackTrace();
			}

			System.out.println("bot1's and bot2's thread successfully stopped.");
		}
	}

	void gatherData() {
		collisionDetection();
		gui.center_panel.isBotReady = true;
		gui.center_panel.updateData(botsList.get(0).player_1.bot1, botsList.get(1).player_2.bot1);
	}

	boolean colides(MainBot bot, Bullets bullet) {
		double distance = Math.sqrt(Math.pow(bot.body.getCenterX() - bullet.bul.getCenterX(), 2)
				+ Math.pow(bot.body.getCenterY() - bullet.bul.getCenterY(), 2));
		if (distance < (bot.radius + bullet.radius - 10)) {
			return true;
		} else {
			return false;
		}

	}

	void collisionDetection() {

		// If detected, give that info to detector bot
		// and disable his detection radius for timeout
		if (botsList.get(0).player_1.bot1.detect.intersects(botsList.get(1).player_2.bot1.body.getFrame())) {
			botsList.get(0).player_1.bot1.enemyDetected(botsList.get(1).player_2.bot1.body);
			bot_1_detect = false;
			bot_1_detected = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() - bot_1_detected > bot_1_detect_timeout)
			bot_1_detect = true;
		// and now same for bot 2
		if (botsList.get(1).player_2.bot1.detect.intersects(botsList.get(0).player_1.bot1.body.getFrame())) {
			botsList.get(1).player_2.bot1.enemyDetected(botsList.get(0).player_1.bot1.body);
			bot_2_detect = false;
			bot_2_detected = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() - bot_2_detected > bot_2_detect_timeout)
			bot_2_detect = true;

		// Bullets collision
		// See if bot_n hits the other bot with a bullet
		for (int mainB = 0; mainB < botsList.size(); mainB++) {
			for (int secB = 0; secB < botsList.size(); secB++) {

				if (secB == mainB)
					break;

				// if player_2 collides with player 1's bullets
				for (int i = 0; i < botsList.get(mainB).player_1.bot1.bullets.size(); i++) {
					if (colides(botsList.get(secB).player_2.bot1, botsList.get(0).player_1.bot1.bullets.get(i))) {
						System.out.println("Bot " + 2 + " intersects bullet nº " + i);
						botsList.get(0).player_1.bot1.bullets.remove(i);
						botsList.get(1).player_2.bot1.loseLife();
					}
				}
			}
		}
		// if player_1 collides with player 2's bullets
		for (int i = 0; i < botsList.get(1).player_2.bot1.bullets.size(); i++) {
			if (colides(botsList.get(0).player_1.bot1, botsList.get(1).player_2.bot1.bullets.get(i))) {
				System.out.println("Bot " + 2 + " intersects bullet nº " + i);
				botsList.get(1).player_2.bot1.bullets.remove(i);
				botsList.get(0).player_1.bot1.loseLife();
			}
		}

	}

	@Override
	public void run() {

		System.out.println("ArenaGui is running");
		while (running) {
			try {
				if (botsList.get(0).player_1.bot1.life_left < 0 || botsList.get(1).player_2.bot1.life_left < 0) {
					isOver = true;
					running = false;
					if (botsList.get(0).player_1.bot1.life_left < 0) {
						bot1_score++;
					} else {
						bot2_score++;
					}
					updateNames();
					gui.center_panel.displayGameOver();
					break;
				} else {
					botsList.get(0).player_1.decide();
					botsList.get(1).player_2.decide();
				}
				botsList.get(0).player_1.bot1.updateBot();
				botsList.get(1).player_2.bot1.updateBot();
				gatherData();
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				System.out.println("CombatRing Thread can't sleep");
				e.printStackTrace();

			}
		}

	}

}
