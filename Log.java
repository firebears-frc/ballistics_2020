import java.text.NumberFormat;

public class Log {
	static private final NumberFormat F = NumberFormat.getInstance();
	static {
		F.setMinimumFractionDigits(3);
		F.setMaximumFractionDigits(3);
	}

	static void log(double v) {
		System.out.print(F.format(v));
	}

	static void log(String v) {
		System.out.print(v);
	}

	static void log() {
		System.out.println();
	}
}
