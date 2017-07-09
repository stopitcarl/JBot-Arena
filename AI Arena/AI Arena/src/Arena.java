public class Arena {
	public static void main(String[] args) {

		CombatRing ring = new CombatRing();
		Thread combatThread = new Thread(ring);
		combatThread.start();

	}
}
