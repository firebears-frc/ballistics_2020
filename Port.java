/** Port for Power Cells */
public class Port {

	/** Bottom port */
	static public final Port BOTTOM = new Port("BOTTOM", 0.5842, 0.254,
		0.0);

	/** Outer port */
	static public final Port OUTER = new Port("OUTER", 2.49555, 0.762,
		0.0);

	/** Inner port */
	static public final Port INNER = new Port("INNER", 2.49555, 0.3302,
		0.74295);

	/** Create a new port */
	private Port(String n, double elevation, double height,
		double recessed)
	{
		name = n;
		elevationM = elevation;
		heightM = height;
		recessedM = recessed;
	}

	/** Port name */
	public final String name;

	/** Elevation of center (m) */
	public final double elevationM;

	/** Port height (m) */
	public final double heightM;

	/** Recessed distance (m) */
	public final double recessedM;

	/** Get elevation at top of port */
	public double top() {
		return elevationM + heightM / 2.0;
	}

	/** Get elevation at bottom of port */
	public double bottom() {
		return elevationM - heightM / 2.0;
	}

	/** Check for port / power cell collision */
	public boolean checkCollision(PowerCell cell) {
		return (cell.maxX() >= recessedM && cell.minX() <= recessedM) &&
		       (cell.y < bottom() || cell.y > top() ||
		        cell.checkCollision(recessedM, top()) ||
		        cell.checkCollision(recessedM, bottom()));
	}

	/** Check if a power cell has passed the port, but is out of range */
	public boolean checkRange(PowerCell cell) {
		return cell.x >= recessedM &&
		       (cell.y < bottom() || cell.y > top());
	}
}
