
public class Analyzer {
	public static void deps(int width, int height, byte[] data) {
		int pxCount = width*height;
		int frameCount = data.length / pxCount;
		
		int[][][] frames = new int[frameCount][height][width];
		
		for(int frame = 0, idx = 0; frame < frameCount; frame++) {
			for(int y = 0; y < height; y ++) {
				for(int x = 0; x < width; x ++, idx++) {
					frames[frame][y][x] = data[idx] & 0xFF;
				}
			}
		}
		
	}
}
