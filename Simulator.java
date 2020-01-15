public class Simulator {
	/** Simulation interval (s) */
	static private final double INTERVAL = 0.00001;

	/** Bisector to choose to replace high or low shot */
	static interface Bisector {
		/** Choose shot */
		boolean choose(Shot shot);
	}

	/** Power cell launcher */
	private final Launcher launcher;

	/** Create a new power cell launch simulator */
	private Simulator(double angle, double range) {
		launcher = new Launcher(angle, range);
	}

	/** Simulate motion of power cell.
	 * @return Shot value. */
	private Shot simulate(double v) {
		PowerCell cell = launcher.launch(v);
		double dur; // time from launch
		// Simulate for up to 5 seconds
		for (dur = 0.0; dur < 5.0; dur += INTERVAL) {
			cell.advance(INTERVAL);
			if (cell.checkCollision() || cell.past(Port.INNER))
				break;
		}
		return new Shot(launcher.rangeM, v, dur, cell);
	}

	/** Get low shot */
	private Shot lowShot() {
		// Use simple parabola with apex at center of inner port
		Trajectory tr = new Trajectory(launcher, Port.INNER.bottom());
		Shot shot = simulate(tr.velocityMS);
		if (shot.value() < 0.0)
			return shot;	
		tr = new Trajectory(launcher, Port.INNER.elevationM);
		shot = simulate(tr.velocityMS);
		if (shot.value() < 0.0)
			return shot;	
		tr = new Trajectory(launcher, Port.INNER.top());
		shot = simulate(tr.velocityMS);
		if (shot.value() < 0.0)
			return shot;	
		tr = new Trajectory(launcher, Port.OUTER.top());
		return simulate(tr.velocityMS);
	}

	/** Get high shot */
	private Shot highShot() {
		// Use simple parabola with apex at top of outer port
		Trajectory tr = new Trajectory(launcher, Port.OUTER.top());
		Shot shot = simulate(tr.velocityMS);
		if (shot.value() > 0.0)
			return shot;
		tr = new Trajectory(launcher, Port.INNER.top());
		shot = simulate(tr.velocityMS);
		if (shot.value() > 0.0)
			return shot;
		tr = new Trajectory(launcher, Port.INNER.elevationM);
		return simulate(tr.velocityMS);
	}

	/** Bisect low/high shots until within threshold */
	private Shot bisectShots(Bisector bisector, Shot lo, Shot hi) {
		for (int i = 0; i < 32; i++) {
			double v = (lo.velocity + hi.velocity) / 2.0;
			Shot shot = simulate(v);
			if (bisector.choose(shot))
				hi = shot;
			else
				lo = shot;
			if (hi.velocity - lo.velocity < 0.0001)
				return shot;
		}
		return null;
	}

	/** Bisect between low and high shots */
	private Shot bisect(Bisector bisector) {
		Shot lo = lowShot();
		Shot hi = highShot();
		if (lo.value() < 0.0 && hi.value() > 0.0)
			return bisectShots(bisector, lo, hi);
		else
			return null;
	}

	/** Find optimal shot for an angle and range */
	private Shot findOptimalShot() {
		return bisect(new Bisector() {
			public boolean choose(Shot shot) {
				return shot.value() > 0;
			}
		});
	}

	/** Find highest shot for an angle and range */
	private Shot findHighestShot() {
		return bisect(new Bisector() {
			public boolean choose(Shot shot) {
				return !(shot.isOuterGoal() ||
				         shot.value() < 0);
			}
		});
	}

	/** Find lowest shot for an angle and range */
	private Shot findLowestShot() {
		return bisect(new Bisector() {
			public boolean choose(Shot shot) {
				return shot.isOuterGoal() ||
				       shot.value() > 0;
			}
		});
	}

	private void findSpeedsForRange() {
		Shot shot = findOptimalShot();
		if (shot != null) {
			Shot lo = findLowestShot();
			Shot hi = findHighestShot();
			Log.log(shot.range);
			Log.log(",");
			Log.log(lo.velocity);
			Log.log(",");
			Log.log(shot.velocity);
			Log.log(",");
			Log.log(hi.velocity);
			Log.log();
		}
	}

	/** Find optimal speed for all ranges */
	static private void findSpeedsForAngle(double angle) {
		Log.log("range,lo,optimal,hi");
		Log.log();
		// Test 1 to 1000 cm ranges (0.01 to 10.00 meters)
		for (int range = 1; range <= 1000; range++ ) {
			Simulator sim = new Simulator(angle, range / 100.0);
			sim.findSpeedsForRange();
		}
	}

	static public void main(String[] args) {
		double angle = Double.parseDouble(args[0]);
		findSpeedsForAngle(angle);
	}
}
