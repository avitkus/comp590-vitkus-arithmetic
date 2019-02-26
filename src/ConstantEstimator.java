
public class ConstantEstimator implements Estimator {
	private final int val;
	private static int DEFAULT_VAL = 127;
	
	public ConstantEstimator() {
		this(DEFAULT_VAL);
	}
	
	public ConstantEstimator(int val) {
		this.val = val;
	}
	
	@Override
	public int estimate(int x, int y, int frame, int[][][] frames) {
		return val;
	}
}
