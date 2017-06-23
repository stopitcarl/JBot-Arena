import java.awt.Color;
import java.awt.geom.Ellipse2D;

public class Bullets {

	final int radius = 20;
	double vel = 1;
	private long time_bullet = 0L;
	private long delta = 0L;
	double degree = 0;
	static public final Color red = new Color(240, 10, 100);
	Ellipse2D.Double bul = null;

	public Bullets(double x1, double y1, double deg) {
		// System.out.println(x1 + " " + y1 + " " + deg);
		bul = new Ellipse2D.Double(x1, y1, radius, radius);
		degree = deg;
		// System.out.println("Created Bullet: " + bul.getCenterX() + " ; " +
		// bul.getCenterY());
	}

	void moveBullet() {
		this.bul.x += this.vel * 10 * Math.cos(degree);
		this.bul.y += this.vel * 10 * Math.sin(degree);
	}

	long getDelta() {
		delta = System.currentTimeMillis() - time_bullet;
		time_bullet = System.currentTimeMillis();
		return delta;
	}

}
