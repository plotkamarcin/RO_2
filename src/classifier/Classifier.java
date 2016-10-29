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
		ArrayList<Extractable> trainData = new Loader().loadData("train_image_data.ser");
		ArrayList<Extractable> testData = new Loader().loadData("test_image_data.ser");
		ArrayList<Extractable> starTrainData=new Loader().loadData("train_starImages.ser");
		ArrayList<Extractable> starPlainData=new Loader().loadData("plain_starImages.ser");
		ArrayList<Extractable> starLightData=new Loader().loadData("light_starImages.ser");
		ArrayList<Extractable> star30degData=new Loader().loadData("30degSet_starImages.ser");
		Knn classifier = new Knn(testData, trainData);
		for (int i = 0; i <testData.size(); i++) {
			//classifier.calculateEuclideanDistances(i, 100);
			//classifier.calculateMinkovskyDistances(i, 100);
			//classifier.calculateChebyshevDistances(i, 100);
			//classifier.calculateTaxiDistances(i, 100);
		}
		classifier.showConfusionMatrix();
		System.out.println("");
		
		Knn starClassifier = new Knn(star30degData, starTrainData);
		for (int i = 0; i <starPlainData.size(); i++) {
			starClassifier.calculateEuclideanDistances(i, 5);
			//starClassifier.calculateMinkovskyDistances(i, 5);
			//starClassifier.calculateChebyshevDistances(i, 5);
			//starClassifier.calculateTaxiDistances(i, 5);
		}
		starClassifier.showConfusionMatrix();
		System.out.println("");
	}
}
