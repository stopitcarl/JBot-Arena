import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JButton;

public class CombatRing implements Runnable {

	// Combat variables
	boolean running = true;
	double velocity = 10;
	boolean isOver = false;
	long time;
	// player's variables
	BotManager botmgr;
	public boolean isAI1 = false;
	public boolean isAI2 = false;
	private boolean[] controls = { false, false, false, false, false }; // Up,
																		// Right,
																		// Down,
																		// Left,
																		// Shoot
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
	int sleeptime = 1000 / 60;
	int hits = 0;

	public CombatRing() {
		System.out.println("Score: " + bot1_score + " - " + bot2_score);

		// Start GUI
		gui = new GUI();

		// Wire up events
		ButtonHandler buttonHandler = new ButtonHandler();
		gui.start_btn.addActionListener(buttonHandler);
		gui.isBot1Manual.addActionListener(buttonHandler);
		gui.reset_btn.addActionListener(buttonHandler);
		// Controls
		KeyHandler keyHandler = new KeyHandler();
		gui.addKeyListener(keyHandler);

		// Create a bot manager which will hold player's object and their bots.
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

	public void reset() {
		gui.dispose();
		botsList.clear();

		// Create thread, CombatRing and start thread it

		new Thread(new CombatRing()).start();

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
		repaint();
	}

	void repaint() {
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
						if (colides(botsList.get(a), botsList.get(b).bullets.get(i)))
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

	private void manualMove() {
		if (controls[0])
			botsList.get(0).move(6);
		if (controls[1])
			botsList.get(0).turn(6);
		if (controls[2])
			botsList.get(0).move(6);
		if (controls[3])
			botsList.get(0).turn(-6);
		if (controls[4])
			botsList.get(0).shoot();
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
					// System.out.println("A bot died");
					if (botsList.get(0).life_left < 0) {
						bot2_score++;
						System.out.println("Bot 1 Died");
					} else {
						bot1_score++;
						System.out.println("Bot 2 Died");
					}
					// System.out.println("updating bots");
					botmgr.updateBots();
					botsList.set(0, botmgr.getBot(1));
					botsList.set(1, botmgr.getBot(2));
					for (MainBot bot : botsList) {
						bot.updateBot();
					}
					updateNames();
					repaint();
					gui.center_panel.displayGameOver();
					break;
				} else {

					// Move manually
					if (!botsList.get(0).autonomous)
						manualMove();

					botmgr.updateBots();
					botsList.set(0, botmgr.getBot(1));
					botsList.set(1, botmgr.getBot(2));
					for (MainBot bot : botsList) {
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

	private class ButtonHandler implements ActionListener {
		Color green = new Color(0, 255, 0);
		Color red = new Color(255, 0, 0);

		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();

			if (source == gui.start_btn) {
				reset();
			}

			if (source == gui.reset_btn) {
				reset();
			}

			if (source == gui.isBot1Manual) {

				isAI1 = isAI1 ? false : true; // Toggle the bool value
				botsList.get(0).autonomous = isAI1;
				gui.isBot1Manual.setBackground(isAI1 ? green : red);
				System.out.println("The AI is " + (isAI1 ? "On" : "Off"));
			}

		}

	}

	private class KeyHandler implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			System.out.println("key typed");

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// System.out.println("key pressed");
			switch (e.getKeyCode()) {
			case 37: // Left
				controls[3] = true;
				break;
			case 38: // Up
				controls[0] = true;
				break;
			case 39: // Right
				controls[1] = true;
				break;
			case 40: // Down
				controls[2] = true;
				break;
			case 32:
				controls[4] = true;
			default:
				System.out.println("Code of input is:" + e.getKeyCode());
				break;

			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			System.out.println("released");

			switch (e.getKeyCode()) {
			case 37: // Left
				controls[3] = false;
				break;
			case 38: // Up
				controls[0] = false;
				break;
			case 39: // Right
				controls[1] = false;
				break;
			case 40: // Down
				controls[2] = false;
				break;
			case 32:
				controls[4] = false;
			default:
				System.out.println("Code of input is:" + e.getKeyCode());
				break;
			}
		}

	}
}
