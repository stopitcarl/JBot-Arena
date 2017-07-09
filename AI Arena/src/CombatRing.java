import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class CombatRing implements Runnable {

	// Combat variables
	boolean running = true;
	double velocity = 10;
	boolean isOver = false;
	long time;
	// player's variables
	BotManager botmgr;
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
	ArrayList<MainBot> botsList = new ArrayList<>();
	// GUI variables
	GUI gui = null;
	int fps = 60;
	int sleeptime = 10;
	int hits = 0;

	public CombatRing() {
		System.out.println("Score: " + bot1_score + " - " + bot2_score);
		// Start GUI
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

		// Create a bot manager which will hold player's object adn their bots.
		// (Copies bots object by "passing by value")
		botmgr = new BotManager();

		// Copying player 1's bot
		botsList.add(botmgr.getBot(1));
		// Copying player 2's bot
		botsList.add(botmgr.getBot(2));
		System.out.println("Created list with player's bots" + botsList.size());

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
		bot1_name = botsList.get(0).name;
		bot2_name = botsList.get(1).name;
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
		gui.center_panel.updateData(botsList.get(0), botsList.get(1));
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

		// a is victim, b is shooter. Check if a collides with b's bullets
		// If hit, a.loseLife(); and b.getPoints().
		for (int a = 0; a < botsList.size(); a++) {
			for (int b = 0; b < botsList.size(); b++) {
				
				if (a == b) // Avoid checking if bot hits itself.
					continue;
				
				time = System.currentTimeMillis();
				if (time - botsList.get(a).enemy_last_detected > botsList.get(a).enemy_detect_timeout) {
					for (int i = 0; i > botsList.get(b).bullets.size(); i++) {
						if ( colides(botsList.get(a), botsList.get(b).bullets.get(i)) );
							botsList.get(a).loseLife();
					}
				}

			}
		}

		if (botsList.get(0).detect.intersects(botsList.get(1).body.getFrame())) {
			botsList.get(0).enemyDetected(botsList.get(1).body);
			bot_1_detect = false;
			bot_1_detected = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() - bot_1_detected > bot_1_detect_timeout)
			bot_1_detect = true;
		// and now same for bot 2
		if (botsList.get(1).detect.intersects(botsList.get(0).body.getFrame())) {
			botsList.get(1).enemyDetected(botsList.get(0).body);
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
					continue;

				// if player_2 collides with player 1's bullets
				for (int i = 0; i < botsList.get(mainB).bullets.size(); i++) {
					if (colides(botsList.get(secB), botsList.get(mainB).bullets.get(i))) {
						System.out.println("Bot " + 2 + " intersects bullet nº " + i);
						botsList.get(mainB).bullets.remove(i);
						botsList.get(secB).loseLife();
					}
				}
			}
		}
		// if player_1 collides with player 2's bullets
		for (int i = 0; i < botsList.get(1).bullets.size(); i++) {
			if (colides(botsList.get(0), botsList.get(1).bullets.get(i))) {
				System.out.println("Bot " + 2 + " intersects bullet nº " + i);
				botsList.get(1).bullets.remove(i);
				botsList.get(0).loseLife();
			}
		}

	}

	@Override
	public void run() {

		System.out.println("ArenaGui is running");
		while (running) {
			try {
				// Check bot's life_left, if life_left < 0, game's over
				if (botsList.get(0).life_left < 0 || botsList.get(1).life_left < 0) {
					isOver = true;
					running = false;
					if (botsList.get(0).life_left < 0) {
						bot1_score++;
					} else {
						bot2_score++;
					}
					updateNames();
					gui.center_panel.displayGameOver();
					gatherData();
					break;
				} else {
					botmgr.updateBots();
					botsList.set(0, botmgr.getBot(1));
					botsList.set(1, botmgr.getBot(2));		
					for(MainBot bot : botsList){
						bot.updateBot();
					}
				}				
				gatherData();
				
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				System.out.println("CombatRing Thread can't sleep");
				e.printStackTrace();

			}
		}

	}

}
