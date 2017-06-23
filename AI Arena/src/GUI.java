import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;

	ArenaGUI center_panel = null;
	Thread gui_thread = null;
	public static int width = 1000;
	public static int height = 700;
	private JTextField score_keeper;
	JButton start_btn;

	public GUI() {
		System.out.println("Starting Frame");

		// Jframe settings
		setLocation(new Point(0, 0));
		new JFrame();
		setTitle("BOT Arena");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		// Master JPanel
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		// Sub -Panels
		JPanel top_panel = new JPanel();
		top_panel.setBackground(Color.BLACK);
		top_panel.setPreferredSize(new Dimension(0, 35));
		panel.add(top_panel, BorderLayout.NORTH);
		top_panel.setLayout(new BoxLayout(top_panel, BoxLayout.X_AXIS));

		JLabel lblNewLabel = new JLabel("BOT FIGHT");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
		top_panel.add(lblNewLabel);

		score_keeper = new JTextField();
		score_keeper.setDisabledTextColor(Color.LIGHT_GRAY);
		score_keeper.setText("BOT  0 : 0  BOT");
		score_keeper.setFont(new Font("Tahoma", Font.PLAIN, 26));
		score_keeper.setHorizontalAlignment(SwingConstants.CENTER);
		score_keeper.setToolTipText("Bots score");
		top_panel.add(score_keeper);
		score_keeper.setColumns(10);

		start_btn = new JButton("Start");
		start_btn.setMargin(new Insets(2, 2, 2, 2));
		start_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		start_btn.setFont(new Font("Tahoma", Font.PLAIN, 24));
		top_panel.add(start_btn);

		JButton start_btn = new JButton("Reset");
		start_btn.setMargin(new Insets(2, 2, 2, 2));
		start_btn.setFont(new Font("Tahoma", Font.PLAIN, 24));
		top_panel.add(start_btn);

		// Center Panel
		startGUI();

		panel.add(center_panel, BorderLayout.CENTER);
		pack();
		System.out.println("Frame has been created");

	}

	void update_score(String bot1, String bot2, int score_1, int score_2) {
		score_keeper.setText(bot1 + "  " + score_1 + " : " + score_2 + "  " + bot2);
	}

	public void startGUI() {
		center_panel = new ArenaGUI(width, height);
		center_panel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.RED, new Color(255, 51, 51),
				new Color(51, 51, 204), new Color(51, 51, 102)));
		System.out.println("GUI process successfully started.");
	}

}

class ArenaGUI extends JPanel {

	private static final long serialVersionUID = 1L;
	Color orange = new Color(123, 41, 42);
	boolean running = true;
	boolean isBotReady = false;
	boolean isOver = false;
	static boolean testering = false;
	static double testX = 0;
	static double testY = 0;
	String whoWon = "Game Over";
	MainBot bot_gui_1 = null;
	MainBot bot_gui_2 = null;
	List<Bullets> bullets1 = new ArrayList<>();
	List<Bullets> bullets2 = new ArrayList<>();
	BasicStroke stroke = new BasicStroke(3);

	Graphics2D g2 = null;

	public ArenaGUI(int width, int height) {
		new JPanel();
		setPreferredSize(new Dimension(width, height));
		updateUI();
	}

	int getW() {
		return getWidth();
	}

	public int getH() {
		return getHeight();
	}

	void updateData(MainBot bot1, MainBot bot2) {
		bot_gui_1 = bot1;
		bullets1 = bot1.bullets;
		bot_gui_2 = bot2;
		bullets2 = bot2.bullets;

		repaint();
	}

	public static void drawS(double x, double y, boolean bool) {
		testering = bool;
		testX = x;
		testY = y;

	}

	void displayGameOver() {
		isOver = true;
		whoWon = "Game Over";
		repaint();
	}

	// Get's variables from object and paints child objects
	@Override
	public void paintComponent(Graphics g) {
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// paint background
		// System.out.println("Frame: " + n_paint);
		g.setColor(Color.gray);
		g.fillRect(0, 0, getWidth(), getWidth());
		if (isBotReady) {
			// Paint detection radius
			if (CombatRing.bot_1_detect) {
				g2.setColor(bot_gui_1.radius_color);
				g2.fill(bot_gui_1.detect);
			}
			if (CombatRing.bot_2_detect) {
				g2.setColor(bot_gui_2.radius_color);
				g2.fill(bot_gui_2.detect);
			}

			// Painting Bot 1 (body & head)
			g2.setColor(bot_gui_1.body_color);
			g2.fill(bot_gui_1.body);
			g2.setStroke(stroke);
			g2.setColor(bot_gui_1.head_color);
			g2.draw(bot_gui_1.head);
			g2.setColor(bot_gui_1.life_color);
			g2.fill(bot_gui_1.life);

			if (!bullets1.isEmpty()) {
				g2.setColor(Bullets.red);

				for (int i = 0; i < bullets1.size(); i++) {
					g2.fill(bullets1.get(i).bul);
				}
			}

			// Painting Bot 2 (body & head)
			g2.setColor(bot_gui_2.body_color);
			g2.fill(bot_gui_2.body);
			g2.setStroke(stroke);
			g2.setColor(bot_gui_2.head_color);
			g2.draw(bot_gui_2.head);
			g2.setColor(bot_gui_2.life_color);
			g2.fill(bot_gui_2.life);

			if (!bullets2.isEmpty()) {
				g2.setColor(Bullets.red);

				for (int i = 0; i < bullets2.size(); i++) {
					g2.fill(bullets2.get(i).bul);
				}
			}

		}
		if (isOver) {

			FontRenderContext context = g2.getFontRenderContext();
			Font font = new Font("Arial", Font.BOLD, 78);
			TextLayout txt = new TextLayout(whoWon, font, context);

			Rectangle2D bounds = txt.getBounds();
			int x = (int) ((getWidth() - (int) bounds.getWidth()) / 2);
			int y = (int) ((getHeight() - (bounds.getHeight() - txt.getDescent())) / 2);
			y += txt.getAscent() - txt.getDescent();
			g2.setFont(font);
			g2.setColor(Color.orange);
			g2.drawString(whoWon, x, y);
			// g2.setFont(new Font("Arial Bold", Font.BOLD, 60));
			// g2.setColor(orange);
			// g2.("Game Over", GUI.width / 2, GUI.height / 2);
		}

		if (testering) {
			g2.drawRect((int) testX, (int) testY, 10, 10);
		}

	}

}
