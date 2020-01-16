/** Simple parabolic trajectory */
public class Trajectory {

	/** Power cell launcher */
	public final Launcher launcher;

	/** Height at apex (m) */
	public final double apexHeightM;

	/** Initial velocity (m/s) */
	public final double velocityMS;

	/** Create a new parabolic trajectory */
	public Trajectory(Launcher launcher, double e) {
		this.launcher = launcher;
		apexHeightM = e - launcher.elevationM;
		velocityMS = velocityFromHeight();
	}

	/** Calculate velocity from apex height */
	private double velocityFromHeight() {
		double a1 = 2.0 * PowerCell.GRAVITY * apexHeightM;
		double a2 = Math.sin(launcher.angle);
		return Math.sqrt(a1 / (a2 * a2));
	}

	/** Calculate time to port (s) */
	public double timeToPort(Port port) {
		double a1 = launcher.rangeM + port.recessedM;
		double a2 = velocityMS * Math.cos(launcher.angle);
		return a1 / a2;
	}

	/** Calculate power cell height (m) at given time */
	public double heightAtTime(double t) {
		double a1 = velocityMS * t * Math.sin(launcher.angle);
		double a2 = PowerCell.GRAVITY / 2.0 * t * t;
		return a1 - a2;
	}
}
