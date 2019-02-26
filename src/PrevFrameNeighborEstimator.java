
public class PrevFrameNeighborEstimator implements Estimator {
	private final int val;
	private static int DEFAULT_VAL = 127;
	
	public PrevFrameNeighborEstimator() {
		this(DEFAULT_VAL);
	}
	
	public PrevFrameNeighborEstimator(int val) {
		this.val = val;
	}
	
	@Override
	public int estimate(int x, int y, int frame, int[][][] frames) {
		if (frame == 0) {
			return val;
		} else {
			if (y == 0) {
				if (x == 0) {
					return est1223(frames[frame-1][0][0], frames[frame-1][0][1], frames[frame-1][1][0], frames[frame-1][1][1]);
				} else if (x == frames[0].length - 1) {
					return est2132(frames[frame-1][0][x-1], frames[frame-1][0][x], frames[frame-1][1][x-1], frames[frame-1][1][x]);
				} else {
					return est212323(frames[frame-1][0][x-1], frames[frame-1][0][x], frames[frame-1][0][x+1], frames[frame-1][1][x-1], frames[frame-1][1][x], frames[frame-1][1][x+1]);
				}
			} else if (y == frames[0].length - 1) {
				if (x == 0) {
					return est2312(frames[frame-1][y-1][0], frames[frame-1][y-1][1], frames[frame-1][y][0], frames[frame-1][y][1]);
				} else if (x == frames[0][0].length - 1) {
					return est3221(frames[frame-1][y-1][x-1], frames[frame-1][y-1][x], frames[frame-1][y][x-1], frames[frame-1][y][x]);
				} else {
					return est323212(frames[frame-1][y-1][x-1], frames[frame-1][y-1][x], frames[frame-1][y-1][x+1], frames[frame-1][y][x-1], frames[frame-1][y][x], frames[frame-1][y][x+1]);
				}
			} else { 
				if (x == 0) {
					return est231223(frames[frame-1][y-1][0], frames[frame-1][y-1][1], frames[frame-1][y][0], frames[frame-1][y][1], frames[frame-1][y+1][0], frames[frame-1][y+1][1]);
				} else if (x == frames[0][0].length - 1) {
					return est322132(frames[frame-1][y-1][x-1], frames[frame-1][y-1][x], frames[frame-1][y][x-1], frames[frame-1][y][x], frames[frame-1][y+1][x-1], frames[frame-1][y+1][x]);
				} else {
					return est323212323(frames[frame-1][y-1][x-1], frames[frame-1][y-1][x], frames[frame-1][y-1][x+1], frames[frame-1][y][x-1], frames[frame-1][y][x], frames[frame-1][y][x+1], frames[frame-1][y+1][x-1], frames[frame-1][y+1][x], frames[frame-1][y+1][x+1]);
				}
			}
		}
	}

	private static int est1223(int a, int b, int c, int d) {
		return (int)Math.round(a/3. + b/6. + c/6. + d/3.);
	}
	
	private static int est2132(int a, int b, int c, int d) {
		return est1223(b, a, d, c);
	}

	private static int est2312(int a, int b, int c, int d) {
		return est1223(c, a, d, b);
	}

	private static int est3221(int a, int b, int c, int d) {
		return est1223(d, b, c, a);
	}
	
	private static int est212323(int a, int b, int c, int d, int e, int f) {
		return (int)Math.round(18.*a/114. + 36.*b/114. + 18.*c/114. + 12.*d/114. + 18.*e/114. + 12.*f/114.);
	}

	private static int est323212(int a, int b, int c, int d, int e, int f) {
		return est212323(d,e,f,a,b,c);
	}

	private static int est231223(int a, int b, int c, int d, int e, int f) {
		return est212323(a,c,d,b,e,f);
	}

	private static int est322132(int a, int b, int c, int d, int e, int f) {
		return est212323(b,d,c,a,f,e);
	}
	
	private static int est323212323(int a, int b, int c, int d, int e, int f, int g, int h, int i) {
		return (int)Math.round(6.*a/78. + 9.*b/78. + 6.*c/78. + 9.*d/78. + 18.*e/78. + 9.*f/78. + 6.*g/78. + 9.*h/78. + 6.*i/78.);
	}

}
