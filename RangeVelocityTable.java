import java.io.BufferedReader;
import java.io.FileReader;

/** Table of range to power cell velocity for a specific launch angle */
public class RangeVelocityTable {

    /** Table of range in cm to power cell velocity (optimal, max, min) */
    private final double[][] range_velocity = new double[1000][3];

    /** Get velocity for a given range (m) */
    private double getValue(double range, int col) {
        int cm = (int) Math.round(range * 100.0);
        if (cm > 0 && cm < 1000)
            return range_velocity[cm][col];
        else
            return 0.0;
    }

    /** Get optimal velocity for a given range (m) */
    public double getOptimal(double range) {
        return getValue(range, 0);
    }

    /** Get maximum velocity for a given range (m) */
    public double getMax(double range) {
        return getValue(range, 1);
    }

    /** Get minimum velocity for a given range (m) */
    public double getMin(double range) {
        return getValue(range, 2);
    }

    /** Create a new range velocity table from a file */
    private RangeVelocityTable(String file) throws Exception {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        for (int i = 1; i < 1000; i++) {
            String line = br.readLine();
            String[] v = line.split(",");
            double optimal = Double.parseDouble(v[0]);
            double max = Double.parseDouble(v[1]);
            double min = Double.parseDouble(v[2]);
            range_velocity[i][0] = optimal;
            range_velocity[i][1] = max;
            range_velocity[i][2] = min;
        }
    }

    /** Create a new range velocity table for a given angle */
    private RangeVelocityTable(int angle) throws Exception {
        this("range_velocity_" + angle + ".csv");
    }

    /** Create a new empty range velocity table */
    private RangeVelocityTable() {
        // empty
    }

    /** Load a range velocity table */
    static public RangeVelocityTable load(int angle) {
        try {
            return new RangeVelocityTable(angle);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new RangeVelocityTable();
        }
    }

    static public void main(String[] args) {
        int angle = Integer.parseInt(args[0]);
        RangeVelocityTable table = load(angle);
        for (int i = 0; i < 1000; i++) {
            double range = i / 100.0;
            double optimal = table.getOptimal(range);
            double max = table.getMax(range);
            if (optimal > 0.0) {
                System.out.println("" + range + "," + optimal + "," + max);
            }
        }
    }
}
