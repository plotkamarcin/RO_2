package extractor;

import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;

public class Extractor {

	public static void main(String[] args) throws IOException {
		ArrayList<BufferedImage> testImages = new ArrayList<BufferedImage>();
		
		ImageLoader textureTrainLoader = new ImageLoader();
		textureTrainLoader.loadSet("E:\\ro\\texture-train\\");
		FeatureProcessor trainTextureProcessor = new FeatureProcessor(textureTrainLoader);
		trainTextureProcessor.calculateImageFeatures();
		Output trainTextures = new Output();
		trainTextures.saveToFile("trainTextures.ser", trainTextureProcessor.getStarImages());

       ImageLoader textureTestLoader = new ImageLoader();
       textureTestLoader.loadSet("E:\\ro\\texture-test\\");
       ImageProcessor testTextureProcessor= new ImageProcessor(textureTestLoader);
       testTextureProcessor.processTexturedImage(1);
       Output testTextures = new Output();
       testTextures.saveToFile("testFisrtImage.ser", testTextureProcessor.getFirstImage());
       
		
	}
}
