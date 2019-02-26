import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import ac.ArithmeticEncoder;
import app.FreqCountIntegerSymbolModel;
import io.OutputStreamBitSink;

public class Encoder {
	private static int RANGE_COUNT = 512;
	
	public static byte[] encode(byte[] message, int width, int height, Estimator estimator) throws IOException {
		int pxCount = width*height;
		int frameCount = message.length / pxCount;
		
		int[][][] frames = new int[frameCount][height][width];
		
		int[] errors = new int[message.length];
		
		
		int estimate;
		int cur;
		
		int[] symbol_counts = new int[RANGE_COUNT];	
		Arrays.fill(symbol_counts, 0);
		
		Integer[] symbols = new Integer[RANGE_COUNT];
//		for (int i=0; i<RANGE_COUNT; i++) {
//			symbols[i] = i;
//		}
		int sym;
//		Path encodeLog = Paths.get("encode.log");
//		Files.deleteIfExists(encodeLog);
//		Files.createFile(encodeLog);
//		StringBuilder sb = new StringBuilder();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (OutputStreamBitSink osbs = new OutputStreamBitSink(baos)) {
			int idx = 0;
			for(int frame = 0; frame < frameCount; frame++) {
//				System.out.println("Encode: " + frame);
				for(int y = 0; y < height; y++) {
					for(int x = 0; x < width; x++, idx++) {
						cur = message[idx] & 0xFF;
						frames[frame][y][x] = cur;
						estimate = estimator.estimate(x, y, frame, frames);
//						System.out.println(cur + " " + estimate);
						int signedSym = (cur - estimate);
						sym = (cur - estimate) & 0x1FF;
						symbol_counts[sym] ++;
						errors[idx] = sym;
//						sb.append(estimate + " " + cur + " " + sym + " " + signedSym + "\n");
//						osbs.write(cur - estimate, RANGE_WIDTH);
					}
				}
			}
//			Files.write(encodeLog, sb.toString().getBytes());

			// Next byte is the width of the range registers
			osbs.write(RANGE_COUNT, 16);
			
			int minCount = symbol_counts[0];
			int minCountSym = 0;
			// First 256 * 4 bytes are the frequency counts 
			for (int i=0; i<RANGE_COUNT; i++) {
				symbols[i] = i;
				osbs.write(symbol_counts[i], 32);
				if (symbol_counts[i] != 0 && (symbol_counts[i] < minCount | minCount == 0)) {
					minCount = symbol_counts[i];
					minCountSym = i;
				}
			}
			double minPInv = ((double)message.length) / minCount;
//			System.out.println(minPInv + " " + Math.log(minPInv) + " " + Math.log(2.) + " " + (Math.log(minPInv) / Math.log(2.)) + " " + Math.ceil(Math.log(minPInv) / Math.log(2.)));
			int codeWidth = (int)Math.ceil(Math.log(minPInv) / Math.log(2.))+1;
			osbs.write(codeWidth, 8);
			// Next 4 bytes are the number of symbols encoded
			osbs.write(errors.length, 32);		

			
			for(int i = 0; i < symbol_counts.length; i ++) {
				sym = i;
				sym <<= 23;
				sym >>= 23;
//				if (((sym >> 8) & 1) == 1) {
//					sym |= 0xFFFFFE00;
//				}
				System.out.println(sym + ": " + symbol_counts[i]);
			}
			
			FreqCountIntegerSymbolModel model = new FreqCountIntegerSymbolModel(symbols, symbol_counts);

//			sb.setLength(0);
			ArithmeticEncoder<Integer> encoder = new ArithmeticEncoder<Integer>(codeWidth);
			for (int i=0; i<errors.length; i++) {
				sym = errors[i];
//				if (((sym >> 8) & 1) == 1) {
//					sym |= 0xFFFFFE00;
//				}
				sym <<= 23;
				sym >>= 23;
//				sb.append(errors[i]).append(" ").append(sym).append("\n");
				encoder.encode(errors[i], model, osbs);
			}
//			Path encodeLog2 = Paths.get("encode2.log");
//			Files.deleteIfExists(encodeLog2);
//			Files.createFile(encodeLog2);
//			Files.write(encodeLog2, sb.toString().getBytes());
			encoder.emitMiddle(osbs);
		}
		
		
		return baos.toByteArray();
	}
	
	
}
