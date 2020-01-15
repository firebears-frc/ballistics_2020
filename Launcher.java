public class Launcher {

	/** Elevation of launcher (m) */
	static private final double ELEVATION_M = 0.6;

	/** Launch elevation (m) */
	public final double elevationM;

	/** Launch angle (radians) */
	public final double angle;

	/** Range from launch to target (m) */
	public final double rangeM;

	/** Create a new launcher */
	public Launcher(double a, double r) {
		elevationM = ELEVATION_M;
		angle = Math.toRadians(a);
		rangeM = r;
	}

	/** Launch a power cell with velocity (m) */
	public PowerCell launch(double v) {
		double x = -rangeM;
		double y = elevationM;
		double vx = v * Math.cos(angle);
		double vy = v * Math.sin(angle);
		return new PowerCell(x, y, vx, vy);
	}
}
