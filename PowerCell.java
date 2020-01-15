/** A power cell is a yellow ball used as a projectile */
public class PowerCell {

	/** Acceleration of gravity (m/s^2) */
	static public final double GRAVITY = 9.82498;

	/** Radius of the sphere is 3.5 inches */
	static public final double RADIUS_M = 0.0889;

	/** Mass density of air at 20 degC, 1016 hPa, dew point 10 degC */
	static private final double AIR_MASS_DENSITY = 1.2019;

	/** Cross-sectional area of power cell (square meters) */
	static private final double REFERENCE_AREA =
		Math.PI * (RADIUS_M * RADIUS_M);

	/** Drag coefficient FIXME */
	static private final double DRAG_COEFFICIENT = 0.5;

	/** Physical mass FIXME */
	static public final double MASS_KG = 0.2;

	/** Displacement from target, starting with negative range (m) */
	public double x;

	/** Elevation from floor, positive up (m) */
	public double y;

	/** Velocity in X direction (m/s) */
	public double vx;

	/** Velocity in Y direction (m/s) */
	public double vy;

	/** Create a power cell */
	public PowerCell(double x, double y, double vx, double vy) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
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
//		calculateDrag(t);
	}

	/** Calculate the aerodynamic drag */
	private void calculateDrag(double t) {
		double v = velocity();
		double drag = 0.5 * AIR_MASS_DENSITY * v * v * REFERENCE_AREA *
			DRAG_COEFFICIENT;
		// F = ma, so a = F / m
		double acc = drag / MASS_KG;
		double dv = acc * t; // delta velocity magnitude
		// Normalized acceleration vector
		double ax = vx / v;
		double ay = vy / v;
		vx -= ax * dv;
		vy -= ay * dv;
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
}
