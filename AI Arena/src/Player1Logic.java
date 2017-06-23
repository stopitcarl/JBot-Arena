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

	// Funçoes são sempre chamadas no estilo: se a função for turn() , será
	// chamada bot1.turn();
	//
	// Funçoes úteis para navegação do bot:
	// move(int) - onde int é nº inteiro
	// turn(int) - o que está em cima
	// shoot() - se ele já tiver esperado o suficiente, dispara
	// getTimeToShoot() - devolve o tempo que ele precisa de esperar p/ shoot()
	// mas n precisas de usar, o shoot so acontece quando getTimeToShoot() = 0;
	// getDistanceRight() - devolve a distância do bot até a right border
	// getDistanceLeft() - lê o que diz em cima so que agora é aesqquerda
	// getDistanceDown() - ^^^^
	// getDistanceUp() - ^^^^
	//
	// as funções matemáticas são chamadas: Se usares cos (função de cosseno) é
	// Math.cos(34); (pro cosseno de 34) Math.random() - dá nº random de 0 a 1.
	// Escreves Math. fazes Ctrl+Espaço e ele da-te as opções.

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
