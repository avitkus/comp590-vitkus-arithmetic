
public class PrevColEstimator implements Estimator {
	private final int val;
	private static int DEFAULT_VAL = 127;
	
	public PrevColEstimator() {
		this(DEFAULT_VAL);
	}
	
	public PrevColEstimator(int val) {
		this.val = val;
	}
	
	@Override
	public int estimate(int x, int y, int frame, int[][][] frames) {
		if (y == 0) {
			return val;
		} else {
			return frames[frame][y-1][x];
		}
	}
}
