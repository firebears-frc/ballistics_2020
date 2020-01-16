/** A power cell is a yellow ball used as a projectile */
public class PowerCell {

	/** Acceleration of gravity (m/s^2) */
	static public final double GRAVITY = 9.82498;

	/** Radius of the sphere is 3.5 inches */
	static public final double RADIUS_M = 0.0889;

	/** Mass density of air at 20 degC, 1016 hPa, dew point 10 degC */
	static private final double AIR_MASS_DENSITY = 1.2019;

	/** Viscosity coefficient of air at 20 degC (Pa * s) */
	static private final double AIR_VISCOSITY_COEFFICIENT = 1.83e-5;

	/** Reference length of power cell (diameter) */
	static private final double REFERENCE_LENGTH = RADIUS_M * 2;

	/** Cross-sectional area of power cell (square meters) */
	static private final double REFERENCE_AREA =
		Math.PI * (RADIUS_M * RADIUS_M);

	/** Physical mass (about 5 ounces) */
	static public final double MASS_KG = 0.14174762;

	/** Table for estimating the drag coefficient from Reynolds number,
	 * based on a NASA page about drag on a sphere.  */
	static private final double[][] DRAG_TABLE = {
		{ 30, 1.5 },
		{ 100, 1.1 },
		{ 150, 1.0 },
		{ 500, 0.7 },
		{ 1000, 0.6 },
		{ 2000, 0.55 },
		{ 25000, 0.55 },
		{ 50000, 0.52 },
		{ 60000, 0.5 },
		{ 80000, 0.4 },
		{ 90000, 0.3 },
		{ 100000, 0.25 },
		{ 150000, 0.2 },
		{ 200000, 0.21 },
		{ 1000000, 0.35 },
		{ 2000000, 0.4 },
		{ 10000000, 0.46 },
	};

	/** Estimate the drag coefficient from reynolds number */
	static private double dragCoefficient(double rn) {
		double r0 = 0.0;
		double cd0 = 2.0;
		for (int i = 0; i < DRAG_TABLE.length; i++) {
			double r1 = DRAG_TABLE[i][0];
			double cd1 = DRAG_TABLE[i][1];
			if (rn <= r1) {
				// Linear interpolation
				double t = (rn - r0) / (r1 - r0);
				return cd0 + (cd1 - cd0) * t;
			}
			r0 = r1;
			cd0 = cd1;
		}
		// Shouldn't ever get to here
		return cd0;
	}

	/** Displacement from target, starting with negative range (m) */
	public double x;

	/** Elevation from floor, positive up (m) */
	public double y;

	/** Velocity in X direction (m/s) */
	public double vx;

	/** Velocity in Y direction (m/s) */
	public double vy;

	/** Tangential velocity (m/s) */
	private final double tv;

	/** Create a power cell */
	public PowerCell(double x, double y, double vx, double vy) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		// Approximate tangential velocity with no basis in fact
		tv = velocity() / 4.0;
	}

	/** Get the velocity (m/s) */
	public double velocity() {
		return Math.hypot(vx, vy);
	}

	/** Get minimum X position of power cell */
	public double minX() {
		return x - RADIUS_M;
	}

	/** Get maximum X position of power cell */
	public double maxX() {
		return x + RADIUS_M;
	}

	/** Get elevation at bottom of power cell */
	public double minY() {
		return y - RADIUS_M;
	}

	/** Get elevation at top of power cell */
	public double maxY() {
		return y + RADIUS_M;
	}

	/** Advance time */
	public void advance(double t) {
		x += vx * t;
		y += vy * t;
		vy -= GRAVITY * t;
		double v = velocity();
		double d = calculateDrag(t, v);
		double m = calculateMagnusEffect(t, v);
		applyDrag(v, d, t);
		applyMagnus(v, m, t);
	}

	/** Calculate the aerodynamic drag acceleration */
	private double calculateDrag(double t, double v) {
		double drag = 0.5 * AIR_MASS_DENSITY * v * v * REFERENCE_AREA *
			dragCoefficient(reynoldsNumber(v));
		// F = ma, so a = F / m
		return drag / MASS_KG;
	}

	/** Apply drag acceleration */
	private void applyDrag(double v, double d, double t) {
		double dv = d * t; // delta velocity magnitude
		// Normalized acceleration vector
		double ax = vx / v;
		double ay = vy / v;
		vx -= ax * dv;
		vy -= ay * dv;
	}

	/** Calculate the Reynolds number based on velocity */
	private double reynoldsNumber(double v) {
		return v * AIR_MASS_DENSITY * REFERENCE_LENGTH /
			AIR_VISCOSITY_COEFFICIENT;
	}

	/** Calculate the magnus effect */
	private double calculateMagnusEffect(double t, double v) {
		double lift = 0.5 * AIR_MASS_DENSITY * v * v * REFERENCE_AREA *
			liftCoefficient(v);
		// F = ma, so a = F / m
		return lift / MASS_KG;
	}

	/** Apply magnus acceleration */
	private void applyMagnus(double v, double m, double t) {
		double dv = m * t; // delta velocity magnitude
		// Normalized acceleration vector (backspin)
		double ax = vy / v;
		double ay = vx / v;
		vx -= ax * dv;
		vy += ay * dv;
	}

	/** Calculate the magnus lift coefficient */
	private double liftCoefficient(double v) {
		double spin = tv / v;
		return spin * 0.2;
	}

	/** Check collision with targets */
	public boolean checkCollision() {
		return Port.OUTER.checkCollision(this) ||
		       Port.INNER.checkCollision(this);
	}

	/** Check collision with a port edge */
	public boolean checkCollision(double px, double py) {
		double dx = Math.abs(x - px);
		double dy = Math.abs(y - py);
		double dist = Math.hypot(dx, dy);
		return dist < RADIUS_M;
	}

	/** Check if we made it past a port */
	public boolean past(Port port) {
		return x > port.recessedM;
	}

	/** Check if we're past a port, but out of range */
	public boolean checkRange() {
		return Port.OUTER.checkRange(this) ||
		       Port.INNER.checkRange(this);
	}

	/** Run this class to test drag coefficient function */
	static public void main(String[] args) {
		double rn = Double.parseDouble(args[0]);
		System.out.println("Reynolds number: " + rn);
		System.out.println("Drag coefficient: " + dragCoefficient(rn));
	}
}
