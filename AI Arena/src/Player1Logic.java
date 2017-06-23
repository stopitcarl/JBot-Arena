import java.awt.Color;
import java.awt.geom.Point2D;

public class Player1Logic extends LogicThinking {
	MainBot bot1 = null;

	public Player1Logic(Point2D pos, double degree) {

		Color body_color = new Color(223, 127, 143); // Bot color, change it to
														// your liking
		String name = "Admin"; // Bot name, change it

		// Dont change what's below
		bot1 = new MainBot(pos, degree, body_color, name);
	}

	// Fun�oes s�o sempre chamadas no estilo: se a fun��o for turn() , ser�
	// chamada bot1.turn();
	//
	// Fun�oes �teis para navega��o do bot:
	// move(int) - onde int � n� inteiro
	// turn(int) - o que est� em cima
	// shoot() - se ele j� tiver esperado o suficiente, dispara
	// getTimeToShoot() - devolve o tempo que ele precisa de esperar p/ shoot()
	// mas n precisas de usar, o shoot so acontece quando getTimeToShoot() = 0;
	// getDistanceRight() - devolve a dist�ncia do bot at� a right border
	// getDistanceLeft() - l� o que diz em cima so que agora � aesqquerda
	// getDistanceDown() - ^^^^
	// getDistanceUp() - ^^^^
	//
	// as fun��es matem�ticas s�o chamadas: Se usares cos (fun��o de cosseno) �
	// Math.cos(34); (pro cosseno de 34) Math.random() - d� n� random de 0 a 1.
	// Escreves Math. fazes Ctrl+Espa�o e ele da-te as op��es.

	public void decide() {

		bot1.move(5);

		if (bot1.getDistanceRight() < 50 || bot1.getDistanceLeft() < 50 || bot1.getDistanceDown() < 50
				|| bot1.getDistanceUp() < 50) {
			bot1.turn(3);
		} else if (Math.random() > 0.939) {
			bot1.turn(((Math.random() - 0.5) * 90));
		}
		ArenaGUI.drawS(bot1.centerX, bot1.centerY, true);
		bot1.shoot();

	}

}
