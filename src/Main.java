import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.InsufficientBitsLeftException;

public class Main {
	private static final Path SOURCE_FILE = Paths.get("out.dat");
	private static final Path COMPRESSED_FILE = Paths.get("new.dat");
	private static final Path RAW_FILE = Paths.get("raw.dat");
	
	private static final int HEIGHT = 64;
	private static final int WIDTH = 64;
	
	private static final Estimator estimator = new PrevFrameNeighborEstimator(94);
	
	public static void main(String[] args) {
		try {
			compress(); // compress out.txt into new.dat
			decompress(); // decompress new.dat file into raw.txt
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void decompress() throws IOException, InsufficientBitsLeftException {
		byte[] data = Files.readAllBytes(COMPRESSED_FILE);
		byte[] raw = Decoder.decode(data, WIDTH, HEIGHT, estimator);
		Files.deleteIfExists(RAW_FILE);
		Files.createFile(RAW_FILE);
		Files.write(RAW_FILE, raw);
	}
	
	private static void compress() throws IOException {
		byte[] raw = Files.readAllBytes(SOURCE_FILE);
		byte[] data = Encoder.encode(raw, WIDTH, HEIGHT, estimator);
		Files.deleteIfExists(COMPRESSED_FILE);
		Files.createFile(COMPRESSED_FILE);
		Files.write(COMPRESSED_FILE, data);
	}

}
