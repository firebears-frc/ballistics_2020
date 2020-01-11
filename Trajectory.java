import java.text.NumberFormat;

/** Trajectory of one shot */
public class Trajectory {
	static private final NumberFormat DISTANCE = NumberFormat.getInstance();
	static {
		DISTANCE.setMinimumFractionDigits(2);
		DISTANCE.setMaximumFractionDigits(2);
	}

	/** Acceleration of gravity (m/s*s) */
	static private final double GRAVITY = 9.82498;

	/** Elevation of launcher (m) */
	static private final double LAUNCH_ELEVATION_M = 0.6;

	/** Launch angle (radians) */
	final double launchAngle;

	/** Range from launch to target (m) */
	final double rangeM;

	/** Height at apex (m) */
	final double apexHeightM;

	/** Initial velocity (m/s) */
	final double velocityMS;

	/** Create a new trajectory */
	public Trajectory(double a, double r, double h) {
		launchAngle = Math.toRadians(a);
		rangeM = r;
		apexHeightM = h;
		velocityMS = velocityFromHeight();
	}

	/** Calculate velocity from apex height */
	double velocityFromHeight() {
		double a1 = 2.0 * GRAVITY * apexHeightM;
		double a2 = Math.sin(launchAngle);
		return Math.sqrt(a1 / (a2 * a2));
	}

	/** Calculate time to port (s) */
	double timeToPort(Port port) {
		double a1 = rangeM + port.recessedM;
		double a2 = velocityMS * Math.cos(launchAngle);
		return a1 / a2;
	}

	/** Calculate power cell height (m) at given time */
	double heightAtTime(double t) {
		double a1 = velocityMS * t * Math.sin(launchAngle);
		double a2 = GRAVITY / 2.0 * t * t;
		return a1 - a2;
	}

	/** Check if a port is in range */
	boolean inRange(Port port) {
		double t = timeToPort(port);
		double h = heightAtTime(t);
		double cellM = LAUNCH_ELEVATION_M + h;
		return port.clearance(cellM) > 0;
	}

	/** Print a range for the given angle */
	static private void printRange(double angle, double range) {
		Trajectory l = new Trajectory(angle, range,
			Port.INNER.elevationM - LAUNCH_ELEVATION_M + 0.16);
		System.out.println();
		System.out.println("range: " + l.rangeM + " m");
		System.out.println("\t\tPower Cell\tPort\t\tClearance");

		double timeS = l.timeToPort(Port.OUTER);
		double heightM = l.heightAtTime(timeS);
		double cellM = LAUNCH_ELEVATION_M + heightM;

		Port.OUTER.printClearance(cellM);

		timeS = l.timeToPort(Port.INNER);
		heightM = l.heightAtTime(timeS);
		cellM = LAUNCH_ELEVATION_M + heightM;

		Port.INNER.printClearance(cellM);
	}

	/** Find range limits for a given launching angle */
	static private void findLimits(double angle) {
		double rmin = 0;
		double rmax = 0;
		double height = Port.INNER.elevationM - LAUNCH_ELEVATION_M
			+ 0.16;
		for (int r = 1; r < 1000; r++ ) {
			double range = r / 100.0;
			Trajectory l = new Trajectory(angle, range, height);
			if (l.inRange(Port.INNER) && l.inRange(Port.OUTER)) {
				if (rmin > 0)
					rmax = range;
				else
					rmin = range;
			}
		}
		printRange(angle, rmin);
		printRange(angle, rmax);
		System.out.println();
		System.out.println("launch angle: " + angle);
		Trajectory l = new Trajectory(angle, rmin,
			Port.INNER.elevationM - LAUNCH_ELEVATION_M);
		System.out.println("velocity: " +
			DISTANCE.format(l.velocityMS) + " m/s");
		System.out.println("range " + DISTANCE.format(rmin) +
			" to " + DISTANCE.format(rmax));
	}

	static public void main(String[] args) {
		double angle = Double.parseDouble(args[0]);
		findLimits(angle);
	}
}
