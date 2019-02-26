
public class PrevFrameEstimator implements Estimator {
	private final int val;
	private static int DEFAULT_VAL = 127;
	
	public PrevFrameEstimator() {
		this(DEFAULT_VAL);
	}
	
	public PrevFrameEstimator(int val) {
		this.val = val;
	}
	
	@Override
	public int estimate(int x, int y, int frame, int[][][] frames) {
		if (frame == 0) {
			return val;
		} else {
			return frames[frame-1][y][x];
		}
	}
}
