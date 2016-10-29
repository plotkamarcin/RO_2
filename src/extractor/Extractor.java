 package extractor;

import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;


public class Extractor {

public static void main(String[] args) throws IOException {
	ImageLoader textureTrainLoader = new ImageLoader();
	textureTrainLoader.loadSet("E:\\ro\\texture-train\\");
	FeatureProcessor trainTextureProcessor = new FeatureProcessor(textureTrainLoader);
	trainTextureProcessor.calculateImageFeatures();
	
}

}
