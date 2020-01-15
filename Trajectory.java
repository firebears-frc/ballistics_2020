import java.text.NumberFormat;

/** Trajectory of one shot */
public class Trajectory {
	static private final NumberFormat DISTANCE = NumberFormat.getInstance();
	static {
		DISTANCE.setMinimumFractionDigits(2);
		DISTANCE.setMaximumFractionDigits(2);
	}

	/** Power cell launcher */
	public final Launcher launcher;

	/** Height at apex (m) */
	public final double apexHeightM;

	/** Initial velocity (m/s) */
	public final double velocityMS;

	/** Create a new trajectory */
	public Trajectory(Launcher launcher, double e) {
		this.launcher = launcher;
		apexHeightM = e - launcher.elevationM;
		velocityMS = velocityFromHeight();
	}

	/** Calculate velocity from apex height */
	double velocityFromHeight() {
		double a1 = 2.0 * PowerCell.GRAVITY * apexHeightM;
		double a2 = Math.sin(launcher.angle);
		return Math.sqrt(a1 / (a2 * a2));
	}

	/** Calculate time to port (s) */
	double timeToPort(Port port) {
		double a1 = launcher.rangeM + port.recessedM;
		double a2 = velocityMS * Math.cos(launcher.angle);
		return a1 / a2;
	}

	/** Calculate power cell height (m) at given time */
	double heightAtTime(double t) {
		double a1 = velocityMS * t * Math.sin(launcher.angle);
		double a2 = PowerCell.GRAVITY / 2.0 * t * t;
		return a1 - a2;
	}

	/** Check if a port is in range */
	boolean inRange(Port port) {
		double t = timeToPort(port);
		double h = heightAtTime(t);
		double cellM = launcher.elevationM + h;
		return port.clearance(cellM) > 0;
	}

	/** Print a range for the given angle */
	static private void printRange(double angle, double range) {
		Launcher launcher = new Launcher(angle, range);
		Trajectory l = new Trajectory(launcher,
			Port.INNER.elevationM + 0.16);
		System.out.println();
		System.out.println("range: " + launcher.rangeM + " m");
		System.out.println("\t\tPower Cell\tPort\t\tClearance");

		double timeS = l.timeToPort(Port.OUTER);
		double heightM = l.heightAtTime(timeS);
		double cellM = launcher.elevationM + heightM;

		Port.OUTER.printClearance(cellM);

		timeS = l.timeToPort(Port.INNER);
		heightM = l.heightAtTime(timeS);
		cellM = launcher.elevationM + heightM;

		Port.INNER.printClearance(cellM);
	}

	/** Find range limits for a given launching angle */
	static private void findLimits(double angle) {
		double rmin = 0;
		double rmax = 0;
		for (int r = 1; r < 1000; r++ ) {
			double range = r / 100.0;
			Launcher launcher = new Launcher(angle, range);
			double height = Port.INNER.elevationM + 0.16;
			Trajectory l = new Trajectory(launcher, height);
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
		Launcher launcher = new Launcher(angle, rmin);
		Trajectory l = new Trajectory(launcher, Port.INNER.elevationM);
		System.out.println("velocity: " +
			DISTANCE.format(l.velocityMS) + " m/s");
		System.out.println("range " + DISTANCE.format(rmin) +
			" to " + DISTANCE.format(rmax) + " m");
	}

	static public void main(String[] args) {
		double angle = Double.parseDouble(args[0]);
		findLimits(angle);
	}
}
