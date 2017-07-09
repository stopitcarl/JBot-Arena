import java.awt.geom.Point2D;

public class BotManager {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	Point2D pos0 = new Point2D.Double(100, 100);
	Point2D pos1 = new Point2D.Double(GUI.width - 100, GUI.height - 100);
	MainBot play1;
	MainBot play2;
	Player1Logic player_1 = null;
	Player2Logic player_2 = null;

	public BotManager() {
		// Instantiate player objects
		player_1 = new Player1Logic(pos0, 0);
		player_2 = new Player2Logic(pos1, 180);

	}
	
	// Get player's bots
	MainBot getBot(int i) {
		switch (i) {
		case 1:
			return player_1.bot1;
		case 2:
			return player_2.bot1;
		default:
			System.out.println("Player nº " + i + " doesn't exist");
			break;
		}
		return null;

	}
	void updateBots(){
		player_1.decide();
		player_2.decide();
	}

}
