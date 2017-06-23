import java.awt.geom.Point2D;

public class BotManager {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	
	Point2D pos0 = new Point2D.Double(100, 100);
	Point2D pos1 = new Point2D.Double(GUI.width - 100, GUI.height - 100);
	Player1Logic player_1 = null;
	Player2Logic player_2 = null;

	public BotManager(int i) {
		switch (i) {
		case 0:
			player_1 = new Player1Logic(pos0, 0);
			break;
		case 1:
			player_2 = new Player2Logic(pos1, 180);
			break;
		default:
			break;
		}

	}

	

}
