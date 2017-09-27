import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class MainBot {

	// Variables
	public Ellipse2D body = null;
	public Ellipse2D detect = null;
	public Line2D head = null;
	public Rectangle2D life = null;

	Color body_color = null;
	Color head_color = new Color(240, 40, 10);;
	Color radius_color = new Color(200, 200, 200, 100);
	Color life_color = Color.green;
	String name = "";
	long time_body = 0L;
	double cos = 0;
	double sin = 0;
	double centerX = 0;
	double centerY = 0;
	long delta = 0L;
	int life_height = 10;
	int life_spacing = life_height * 2;
	public int radius = 40;
	int life_left = radius * 2;
	int detect_radius = radius * 8;
	int height = 40;
	double degree = 0;
	double x = 0;
	double y = 0;
	double vel = 15;
	long time = 0;
	final int BULLET_RELOAD = 500;
	final int DAMAGE = 23;
	private double timeToShoot = 2000;
	List<Bullets> bullets = new ArrayList<>();
	// Enemy
	long enemy_last_detected = 0L;
	long enemy_detect_timeout = 1000L;
	private Ellipse2D enemy = null;
	boolean isEnemyinRange = false;
	public boolean autonomous = true;

	public MainBot(Point2D pos, double degree, Color col, String nam) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.degree = rad(degree);
		this.body_color = col;
		this.name = nam;
		body = new Ellipse2D.Double(x, y, radius * 2, radius * 2);
		detect = new Ellipse2D.Double(x, y, radius * detect_radius, radius * detect_radius);
		head = new Line2D.Double(body.getCenterX(), body.getCenterY(), getHeadX(), getHeadY());
		life = new Rectangle2D.Double(body.getMinX(), body.getMaxY(), radius, life_height);

		time = System.currentTimeMillis();
		time_body = System.currentTimeMillis();
		System.out.println("MainBot has been created");

	}

	double getHeadX() {
		return body.getCenterX() + (double) radius * Math.cos(degree);
	}

	double getHeadY() {
		return body.getCenterY() + (double) radius * Math.sin(degree);
	}

	double getTimeToShoot() {
		timeToShoot = BULLET_RELOAD - (System.currentTimeMillis() - time);
		if (timeToShoot < 0) {
			timeToShoot = 0;
		}
		return timeToShoot;
	}

	// Complex function, required: break down into smaller functions
	void move(int units) {
		if (units > 10)
			units = 10;
		else if (units < -10)
			units = -10;

		// Get delta time
		if (!autonomous)
			delta = 16;
		else
			delta = System.currentTimeMillis() - time_body;

		// Get x (cos) and y (sin) vectors
		cos = delta * Math.cos(degree);
		sin = delta * Math.sin(degree);
		centerX = x + radius;
		centerY = y + radius;

		// Check if hitting side borders
		/******* Left Border****************Right Border *******/
		if ((centerX < radius && cos < 0) || (centerX + radius > GUI.width && cos > 0)) {
			// System.out.println("Hitting " + x + " is not " +
			// body.getCenterX());
		} else {
			x += units / vel * cos;
		}

		if ((centerY < radius && sin < 0) || (centerY + radius > GUI.height && sin > 0)) {
			// System.out.println("Hitting " + y + " is not " +
			// body.getCenterY());
		} else {
			y += units / vel * sin;
		}
		time_body = System.currentTimeMillis();

	}

	void turn(double d) {
		degree += rad(d);

	}

	double rad(double d) {
		return Math.toRadians(d);
	}

	void shoot() {
		if (getTimeToShoot() == 0) {
			time = System.currentTimeMillis();
			// System.out.println("Shooting");
			// System.out.println("Create Bullet with: " + body.getCenterX() + "
			// " + body.getCenterY() + " " + degree);
			Bullets bullet = new Bullets(this.head.getX2(), this.head.getY2(), degree);
			bullets.add(bullet);

		}

	}

	void loseLife() {
		life_left -= DAMAGE;

	}

	boolean enemyDetect() {
		return isEnemyinRange;
	}

	void enemyDetected(Ellipse2D nemesis) {
		enemy = nemesis;
	}

	double getEnemyX() {
		return enemy.getCenterX();
	}

	double getEnemyY() {
		return enemy.getCenterY();
	}

	double getDistanceRight() {
		return GUI.width - this.body.getMaxX();
	}

	double getDistanceLeft() {
		return this.body.getMinX();
	}

	double getDistanceUp() {
		return this.body.getMinY();
	}

	double getDistanceDown() {
		return GUI.height - this.body.getMaxY();
	}

	// Functions below update figure's health bar, body and bullets coordinates
	void updateBot() {
		updateLife();
		updateFigure();
		updateBullets();
	}

	void updateLife() {
		life.setFrame(body.getMinX(), body.getMinY() - life_spacing, life_left, life_height);
	}

	void updateFigure() {
		// Fix bug #253.3
		// Making sure delta_time is functional when AI is OFF
		// and ON again
		if (!autonomous)
			time_body = System.currentTimeMillis();

		body.setFrame(x, y, radius * 2, radius * 2);
		head.setLine(body.getCenterX(), body.getCenterY(), getHeadX(), getHeadY());
		detect.setFrame(body.getCenterX() - detect_radius / 2, body.getCenterY() - detect_radius / 2, detect_radius,
				detect_radius);
	}

	void updateBullets() {

		for (int i = 0; i < bullets.size(); i++) {
			// System.out.println("Bullet position before: " + b.x);
			bullets.get(i).moveBullet();
			if (bullets.get(i).bul.getMaxX() < 0 || bullets.get(i).bul.x > GUI.width) {
				// System.out.println("Bullet has left the building");
				bullets.remove(i);

			} else if (bullets.get(i).bul.getMaxY() < 0 || bullets.get(i).bul.getMinY() > GUI.height) {
				// System.out.println("Bullet has left the building");
				bullets.remove(i);
			}
		}
		// System.out.println(bullets.size());

	}
}
