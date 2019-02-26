import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ac.ArithmeticDecoder;
import app.FreqCountIntegerSymbolModel;
import io.InputStreamBitSource;
import io.InsufficientBitsLeftException;

public class Decoder {
	
	public static byte[] decode(byte[] message, int width, int height, Estimator estimator) throws IOException, InsufficientBitsLeftException {
		byte[] ret;
		try (InputStreamBitSource isbs = new InputStreamBitSource(new ByteArrayInputStream(message));
				ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

			// Read in symbol counts and set up model
			
			// Read in number of symbols encoded


			// Read in range bit width and setup the decoder

			int rangeCount = isbs.next(16);

			int[] symbol_counts = new int[rangeCount];
			Integer[] symbols = new Integer[rangeCount];
			
			for (int i=0; i<rangeCount; i++) {
				symbol_counts[i] = isbs.next(32);
				symbols[i] = i;
			}
			int range_bit_width = isbs.next(8);

			int num_symbols = isbs.next(32);

			FreqCountIntegerSymbolModel model = new FreqCountIntegerSymbolModel(symbols, symbol_counts);
			
			ArithmeticDecoder<Integer> decoder = new ArithmeticDecoder<Integer>(range_bit_width);

			// Decode and produce output.
			
//			System.out.println("Uncompressing file: " + input_file_name);
//			System.out.println("Output file: " + output_file_name);
			System.out.println("Range Register Bit Width: " + range_bit_width);
			System.out.println("Number of symbols: " + num_symbols);
			
			int pxCount = width*height;
			int frameCount = num_symbols / pxCount;
			
			int[][][] frames = new int[frameCount][height][width];
			int estimate;
//			Path decodeLog = Paths.get("decode.log");
//			Files.deleteIfExists(decodeLog);
//			Files.createFile(decodeLog);
//			StringBuilder sb = new StringBuilder();
			for(int frame = 0; frame < frameCount; frame ++) {
				System.out.println("Decode: " + frame);
				for(int y = 0; y < height; y ++) {
					for(int x = 0; x < width; x ++) {
						int sym = decoder.decode(model, isbs);
						int rawSym = sym;
						// sign extend based on bit 9
						sym <<= 23;
						sym >>= 23;

						int signedSym = sym;
						
						estimate = estimator.estimate(x, y, frame, frames);
						sym += estimate;
						frames[frame][y][x] = sym;
						baos.write((byte)sym);
//						sb.append(estimate + " " + sym + " " + rawSym + " " + signedSym + "\n");
//						fos.write(sym);
					}
				}
			}
//			Files.write(decodeLog, sb.toString().getBytes());
			ret = baos.toByteArray();
		}
		return ret;
	}
}
