import java.text.NumberFormat;

public class Shot {
	static private final NumberFormat DISTANCE = NumberFormat.getInstance();
	static {
		DISTANCE.setMinimumFractionDigits(3);
		DISTANCE.setMaximumFractionDigits(3);
	}

	/** Horizontal range (m) */
	public final double range;

	/** Initial velocity (m/s) */
	public final double velocity;

	/** Duration (s) */
	public final double duration;

	/** Final state of power cell */
	private final PowerCell cell;

	/** Create a new shot */
	public Shot(double r, double v, double d, PowerCell c) {
		range = r;
		velocity = v;
		duration = d;
		cell = c;
	}

	public boolean isInnerGoal() {
		return cell.past(Port.INNER) &&
		     !(cell.checkRange() || cell.checkCollision());
	}

	public boolean isOuterGoal() {
		return cell.past(Port.OUTER) &&
		     !(cell.checkRange() || cell.checkCollision());
	}

	public double value() {
		return cell.y - Port.INNER.elevationM;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("range: ");
		sb.append(DISTANCE.format(range));
		sb.append("  velocity: ");
		sb.append(DISTANCE.format(velocity));
		sb.append("  duration: ");
		sb.append(DISTANCE.format(duration));
		sb.append(" s");
		sb.append("  dy: ");
		sb.append(DISTANCE.format(value()));
		if (isInnerGoal())
			sb.append("  INNER GOAL!");
		else if (isOuterGoal())
			sb.append("  OUTER GOAL!");
		return sb.toString();
	}
}
