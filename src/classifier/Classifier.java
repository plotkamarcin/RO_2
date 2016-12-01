package classifier;

import java.util.ArrayList;

import extractor.Extractable;
import extractor.FeatureProcessor;
import extractor.Image;
import extractor.Output;
import extractor.TextureImage;

public class Classifier {

	ArrayList<Image> testData;
	ArrayList<Image> trainData;

	
	public static void main(String[] args) {
 		ArrayList<Extractable> trainData = new Loader().loadData("trainTextures.ser");
		ArrayList<Extractable> testData = new Loader().loadData("testFisrtImage.ser");

		
		NaiveBayesian Bayes = new NaiveBayesian(testData, trainData);
		Bayes.classifyItems();
		Bayes.saveImage("testbayes.png");
		
		
		Knn starClassifier = new Knn(testData, trainData);
		for (int i = 0; i <testData.size(); i++) {
			starClassifier.calculateEuclideanDistances(i, 5);
		}
       starClassifier.saveImage("test1.png");
       starClassifier.showConfusionMatrix();
	}
}
