import java.text.NumberFormat;

/** Port for Power Cells */
public class Port {
	static private final NumberFormat FORMAT = NumberFormat.getInstance();
	static {
		FORMAT.setMaximumFractionDigits(4);
	}

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

	/** Get clearance of a power cell at given elevation (center) */
	public double clearance(double cellM) {
		double topM = cellM + PowerCell.RADIUS_M;
		double bottomM = cellM - PowerCell.RADIUS_M;
		return Math.min(top() - topM, bottomM - bottom());
	}

	/** Print clearance of a power cell at given elevation */
	public void printClearance(double cellM) {
		double topM = cellM + PowerCell.RADIUS_M;
		double bottomM = cellM - PowerCell.RADIUS_M;
		System.out.println(name + "\tTOP\t" +
			FORMAT.format(topM) + " m \t" +
			FORMAT.format(top()) + " m \t" +
			FORMAT.format(top() - topM) + " m");
		System.out.println(name + "\tBOTTOM\t" +
			FORMAT.format(bottomM) + " m \t" +
 			FORMAT.format(bottom()) + " m\t" +
			FORMAT.format(bottomM - bottom()) + " m");
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
