
public class PrevRowEstimator implements Estimator {
	private final int val;
	private static int DEFAULT_VAL = 127;
	
	public PrevRowEstimator() {
		this(DEFAULT_VAL);
	}
	
	public PrevRowEstimator(int val) {
		this.val = val;
	}
	
	@Override
	public int estimate(int x, int y, int frame, int[][][] frames) {
		if (x == 0) {
			return val;
		} else {
			return frames[frame][y][x-1];
		}
	}
}
