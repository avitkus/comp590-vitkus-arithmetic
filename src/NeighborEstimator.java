
public class NeighborEstimator implements Estimator {

	private final int val;
	private static int DEFAULT_VAL = 127;
	
	public NeighborEstimator() {
		this(DEFAULT_VAL);
	}
	
	public NeighborEstimator(int val) {
		this.val = val;
	}
	
	@Override
	public int estimate(int x, int y, int frame, int[][][] frames) {
		if (frame == 0) {
			if (y == 0) {
				if (x == 0) {
					return val;
				} else if (x == 1){
					return frames[0][0][0];
				} else {
					return est12(frames[0][0][x-1], frames[0][0][x-2]);
				}
			} else if (y == frames[0].length - 1) {
				if (x == 0) {
					return est12(frames[0][y-1][0], frames[0][y-1][1]);
				} else if (x == frames[0][0].length - 1) {
					return est121(frames[0][y-1][x], frames[0][y-1][x-1], frames[0][y][x-1]);
				} else {
					return est2121(frames[0][y-1][x-1], frames[0][y-1][x], frames[0][y-1][x+1], frames[0][y][x-1]);
				}
			} else {
				if (x == 0) {
					return est12(frames[0][y-1][x], frames[0][y-1][x+1]);
				} else if (x == frames[0][0].length - 1) {
					return est211(frames[0][y-1][x-1], frames[0][y-1][x], frames[0][y][x-1]);
				} else {
					return est2121(frames[0][y-1][x-1], frames[0][y-1][x], frames[0][y-1][x+1], frames[0][y][x-1]);
				}
			}
		} else {
			if (y == 0) {
				if (x == 0) {
					return est1223(frames[frame-1][0][0], frames[frame-1][0][1], frames[frame-1][1][0], frames[frame-1][1][1]);
				} else if (x == frames[0].length - 1) {
					return est21321(frames[frame-1][0][x-1], frames[frame-1][0][x], frames[frame-1][1][x-1], frames[frame-1][1][x], frames[frame][0][x-1]);
				} else {
					return est2123231(frames[frame-1][0][x-1], frames[frame-1][0][x], frames[frame-1][0][x+1], frames[frame-1][1][x-1], frames[frame-1][1][x], frames[frame-1][1][x+1], frames[frame][0][x-1]);
				}
			} else if (y == frames[0].length - 1) {
				if (x == 0) {
					return est231212(frames[frame-1][y-1][0], frames[frame-1][y-1][1], frames[frame-1][y][0], frames[frame-1][y][1], frames[frame][y-1][0], frames[frame][y-1][1]);
				} else if (x == frames[0][0].length - 1) {
					return est3221211(frames[frame-1][y-1][x-1], frames[frame-1][y-1][x], frames[frame-1][y][x-1], frames[frame-1][y][x], frames[frame][y-1][x-1], frames[frame][y-1][x], frames[frame][y][x-1]);
				} else {
					return est3232122121(frames[frame-1][y-1][x-1], frames[frame-1][y-1][x], frames[frame-1][y-1][x+1], frames[frame-1][y][x-1], frames[frame-1][y][x], frames[frame-1][y][x+1], frames[frame][y-1][x-1], frames[frame][y-1][x], frames[frame][y-1][x+1], frames[frame][y][x-1]);
				}
			} else { 
				if (x == 0) {
					return est23122312(frames[frame-1][y-1][0], frames[frame-1][y-1][1], frames[frame-1][y][0], frames[frame-1][y][1], frames[frame-1][y+1][0], frames[frame-1][y+1][1], frames[frame][y-1][0], frames[frame][y-1][1]);
				} else if (x == frames[0][0].length - 1) {
					return est322132211(frames[frame-1][y-1][x-1], frames[frame-1][y-1][x], frames[frame-1][y][x-1], frames[frame-1][y][x], frames[frame-1][y+1][x-1], frames[frame-1][y+1][x], frames[frame][y-1][x-1], frames[frame][y-1][x], frames[frame][y][x-1]);
				} else {
					return est3232123232121(frames[frame-1][y-1][x-1], frames[frame-1][y-1][x], frames[frame-1][y-1][x+1], frames[frame-1][y][x-1], frames[frame-1][y][x], frames[frame-1][y][x+1], frames[frame-1][y+1][x-1], frames[frame-1][y+1][x], frames[frame-1][y+1][x+1], frames[frame][y-1][x-1], frames[frame][y-1][x], frames[frame][y-1][x+1], frames[frame][y][x-1]);
				}
			}
		}
	}
	
	private static int est12(int a, int b) {
		return (int)Math.round(2.*a/3. + b/3.);
	}
	
	private static int est121(int a, int b, int c) {
		return (int)Math.round(2.*a/5. + b/5. + 2.*c/5.);
	}
	
	private static int est211(int a, int b, int c) {
		return est121(b,a,c);
	}
	
	private static int est2121(int a, int b, int c, int d) {
		return (int)Math.round(a/6. + b/3. + c/6. + d/3.);
	}

	private static int est1223(int a, int b, int c, int d) {
		return (int)Math.round(a/3. + b/6. + c/6. + d/3.);
	}
	
	private static int est21321(int a, int b, int c, int d, int e) {
		return (int)Math.round(9.*a/60. + 18.*b/60. + 6.*c/60. + 9.*d/60. + 18.*e/60.);
	}
	
	private static int est231212(int a, int b, int c, int d, int e, int f) {
		return (int)Math.round(18.*a/138. + 12.*b/138. + 36.*c/138. + 18.*d/138. + 36.*e/138. + 18.*f/138.);
	}

	private static int est2123231(int a, int b, int c, int d, int e, int f, int g) {
		return (int)Math.round(18.*a/150. + 36.*b/150. + 18.*c/150. + 12.*d/150. + 18.*e/150. + 12.*f/150. + 36.*g/150.);
	}
	
	private static int est3221211(int a, int b, int c, int d, int e, int f, int g) {
		return (int)Math.round(12.*a/174. + 18.*b/174. + 18.*c/174. + 36.*d/174. + 18.*e/174. + 36.*f/174. + 36.*g/174.);
	}
	
	private static int est23122312(int a, int b, int c, int d, int e, int f, int g, int h) {
		return (int)Math.round(9.*a/84. + 6.*b/84. + 18.*c/84. + 9.*d/84. + 9.*e/84. + 6.*f/84. + 18.*g/84. + 9.*h/84.);
	}
	
	private static int est322132211(int a, int b, int c, int d, int e, int f, int g, int h, int i) {
		return (int)Math.round(6.*a/102. + 9.*b/102. + 9.*c/102. + 18.*d/102. + 6.*e/102. + 9.*f/102. + 9.*g/102. + 18.*h/102. + 18.*i/102.);
	}
	
	private static int est3232122121(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j) {
		return (int)Math.round(12.*a/222. + 18.*b/222. + 12.*c/222. + 18.*d/222. + 36.*e/222. + 18.*f/222. + 18.*g/222. + 36.*h/222. + 18.*i/222. + 36.*j/222.);
	}
	 
	private static int est3232123232121(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j, int k, int l, int m) {
		return (int)Math.round(12.*a/264. + 18.*b/264. + 12.*c/264. + 18*d/264. + 36.*e/264. + 18.*f/264. + 12.*g/264. + 18.*h/264. + 12.*i/264. + 18.*j/264. + 36.*k/264. + 18.*l/264. + 36.*m/264.);
	}

}
