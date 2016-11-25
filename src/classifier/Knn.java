package classifier;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import extractor.Extractable;
import extractor.Image;
import extractor.TextureImage;

public class Knn<T> {

	private class Result {
		private int originalLabel;
		private int classifiedLabel;

		public int getOriginalLabel() {
			return originalLabel;
		}

		public void setOriginalLabel(int originalLabel) {
			this.originalLabel = originalLabel;
		}

		public int getClassifiedLabel() {
			return classifiedLabel;
		}

		public void setClassifiedLabel(int classifiedLabel) {
			this.classifiedLabel = classifiedLabel;
		}

		public Result(int o, int c) {
			this.originalLabel = o;
			this.classifiedLabel = c;
		}
	}

	private ArrayList<Extractable> testData;
	private ArrayList<Extractable> trainData;
	private ArrayList<Result> finalResults;

	private String[] tempResults;

	private int[][] confusionMatrix;

	public Knn(ArrayList<Extractable> test, ArrayList<Extractable> train) {
		this.testData = test;
		this.trainData = train;
		this.finalResults = new ArrayList<Result>();
	}

	public void calculateEuclideanDistances(int objectIndex, int numberOfNeighbours) {
		ArrayList<DistanceMetric> tmpEuclid = new ArrayList<>();
		double eDistance = 0.0;
		for (int j = 0; j < trainData.size(); j++) {
			eDistance = Math.pow(
					Math.pow(testData.get(objectIndex).getFeature1() - trainData.get(j).getFeature1(), 2.0)
							+ Math.pow(testData.get(objectIndex).getFeature2() - trainData.get(j).getFeature2(), 2.0)
							+ Math.pow(testData.get(objectIndex).getFeature3() - trainData.get(j).getFeature3(), 2.0)
							+ Math.pow(testData.get(objectIndex).getFeature4() - trainData.get(j).getFeature4(), 2.0)
							+ Math.pow(testData.get(objectIndex).getFeature5() - trainData.get(j).getFeature5(), 2.0),
					0.5);
			tmpEuclid.add(new DistanceMetric(eDistance, trainData.get(j).getImageId()));
		}

		findNNearestNeighbours(numberOfNeighbours, tmpEuclid);
		findDominantClass(objectIndex);
	}

	public void calculateMinkovskyDistances(int objectIndex, int numberOfNeighbours) {
		ArrayList<DistanceMetric> tmpMinkovsky = new ArrayList<>();
		double mDistance = 0.0;
		for (int j = 0; j < trainData.size(); j++) {
			mDistance = Math.pow(
					Math.pow(Math.abs(testData.get(objectIndex).getFeature1() - trainData.get(j).getFeature1()), 3.0)
							+ Math.pow(
									Math.abs(testData.get(objectIndex).getFeature2() - trainData.get(j).getFeature2()),
									3.0)
							+ Math.pow(
									Math.abs(testData.get(objectIndex).getFeature3() - trainData.get(j).getFeature3()),
									3.0)
							+ Math.pow(
									Math.abs(testData.get(objectIndex).getFeature4() - trainData.get(j).getFeature4()),
									3.0)
							+ Math.pow(
									Math.abs(testData.get(objectIndex).getFeature5() - trainData.get(j).getFeature5()),
									3.0),
					0.3333);
			tmpMinkovsky.add(new DistanceMetric(mDistance, trainData.get(j).getImageId()));
		}

		findNNearestNeighbours(numberOfNeighbours, tmpMinkovsky);
		findDominantClass(objectIndex);

	}

	public void calculateChebyshevDistances(int objectIndex, int numberOfNeighbours) {
		ArrayList<DistanceMetric> tmpChebyshev = new ArrayList<>();
		double cDistance = 0.0;
		for (int j = 0; j < trainData.size(); j++) {

			cDistance = Math.abs(Math.max(
					testData.get(objectIndex).getFeature1() - trainData.get(objectIndex).getFeature1(),
					Math.max(testData.get(objectIndex).getFeature2() - trainData.get(j).getFeature2(),
							Math.max(testData.get(objectIndex).getFeature3() - trainData.get(j).getFeature3(), Math.max(
									testData.get(objectIndex).getFeature4() - trainData.get(j).getFeature4(),
									testData.get(objectIndex).getFeature5() - trainData.get(j).getFeature5())))));
			tmpChebyshev.add(new DistanceMetric(cDistance, trainData.get(j).getImageId()));
		}

		findNNearestNeighbours(numberOfNeighbours, tmpChebyshev);
		findDominantClass(objectIndex);

	}

	public void calculateTaxiDistances(int objectIndex, int numberOfNeighbours) {

		ArrayList<DistanceMetric> tmpTaxi = new ArrayList<>();
		double tDistance = 0.0;
		for (int j = 0; j < trainData.size(); j++) {
			tDistance = Math.abs(testData.get(objectIndex).getFeature1() - trainData.get(j).getFeature1())
					+ Math.abs(testData.get(objectIndex).getFeature2() - trainData.get(j).getFeature2())
					+ Math.abs(testData.get(objectIndex).getFeature3() - trainData.get(j).getFeature3())
					+ Math.abs(testData.get(objectIndex).getFeature4() - trainData.get(j).getFeature4())
					+ Math.abs(testData.get(objectIndex).getFeature5() - trainData.get(j).getFeature5());
			tmpTaxi.add(new DistanceMetric(tDistance, trainData.get(j).getImageId()));
		}
		findNNearestNeighbours(numberOfNeighbours, tmpTaxi);
		findDominantClass(objectIndex);
	}

	private void findNNearestNeighbours(int numberOfNeighbours, ArrayList<DistanceMetric> list) {
		tempResults = new String[numberOfNeighbours];
		Collections.sort(list, new DistanceMetricComparator());
		for (int i = 0; i < numberOfNeighbours; i++) {
			tempResults[i] = Integer.toString(list.get(i).getLabel());
			System.out.println("Item label: " + Double.toString(list.get(i).getLabel()) + " distance "
					+ Double.toString(list.get(i).getMetricValue()));
		}
	}

	private void findDominantClass(int objectIndex) {
		Set<String> dm = new HashSet<String>(Arrays.asList(tempResults));
		String[] uniqueValues = dm.toArray(new String[0]);
		int[] counts = new int[uniqueValues.length];
		for (int i = 0; i < uniqueValues.length; i++) {
			for (int j = 0; j < tempResults.length; j++) {
				if (tempResults[j].equals(uniqueValues[i])) {
					counts[i]++;
				}
			}
		}
		for (int i = 0; i < uniqueValues.length; i++)
			System.out.println("value :" + uniqueValues[i] + " occured: " + counts[i] + "times");

		int max = counts[0];
		for (int counter = 1; counter < counts.length; counter++) {
			if (counts[counter] > max) {
				max = counts[counter];
			}
		}
		// System.out.println("Dominant class is "+uniqueValues[0]+" with " +
		// max +" occurences");
		int freq = 0;
		for (int counter = 0; counter < counts.length; counter++) {
			if (counts[counter] == max) {
				freq++;
			}
		}

		// index of most freq value if we have only one mode
		int index = -1;
		if (freq == 1) {
			for (int counter = 0; counter < counts.length; counter++) {
				if (counts[counter] == max) {
					index = counter;
					break;
				}
			}
			// System.out.println("one majority class, index is: "+index);
			System.out.println("Dominant label is " + uniqueValues[index] + " with " + max + " occurences");
			System.out.println("original label: " + testData.get(objectIndex).getImageId());
			finalResults.add(new Result(testData.get(objectIndex).getImageId(), Integer.parseInt(uniqueValues[index])));
		} else {// we have multiple modes
			int[] ix = new int[freq];// array of indices of modes
			System.out.println("There are: " + freq + " classes with same distance");
			int ixi = 0;
			for (int counter = 0; counter < counts.length; counter++) {
				if (counts[counter] == max) {
					ix[ixi] = counter;// save index of each max count value
					ixi++; // increase index of ix array
				}
			}

			for (int counter = 0; counter < ix.length; counter++)
				System.out.println("class label: " + uniqueValues[ix[counter]]);

			// now choose one at random
			Random generator = new Random();
			// get random number 0 <= rIndex < size of ix
			int rIndex = generator.nextInt(ix.length);
			System.out.println("Choosing random index: " + rIndex);
			int nIndex = ix[rIndex];
			// return unique value at that index
			System.out.println("Picked label: " + uniqueValues[nIndex]);
			System.out.println("original label: " + testData.get(objectIndex).getImageId());
			finalResults
					.add(new Result(testData.get(objectIndex).getImageId(), Integer.parseInt(uniqueValues[nIndex])));
		}
	}

	public void showConfusionMatrix() {
		confusionMatrix = new int[11][11];

		System.out.println("\n ");

		for (Result r : finalResults) {
			Integer.toString(confusionMatrix[r.getOriginalLabel()][r.getClassifiedLabel()]++);
		}

		System.out.print("\t");
		for (int i = 0; i < 10; i++) {
			System.out.print(Integer.toString(i) + "\t");
		}
		System.out.print("Sum");
		System.out.println(" ");

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (i != j) {
					confusionMatrix[i][10] += confusionMatrix[i][j];
				}
			}
		}

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (i != j) {
					confusionMatrix[10][j] += confusionMatrix[i][j];
				}
			}
		}
		for (int i = 0; i < 10; i++) {
			System.out.print(i + "\t");
			for (int j = 0; j < 11; j++) {
				System.out.print(Integer.toString(confusionMatrix[i][j]) + "\t");
			}
			System.out.println(" ");
		}
		System.out.print("Sum\t");
		for (int i = 0; i < 10; i++) {
			System.out.print(Integer.toString(confusionMatrix[10][i]) + "\t");
		}
		double efficiency = 0.0;
		for (int j = 0; j < 10; j++) {
			efficiency += confusionMatrix[j][j];
		}
		System.out.println("\n\n ");
		DecimalFormat formatter = new DecimalFormat("#0.000");
		System.out.println("Efficiency: " + formatter.format((efficiency / testData.size() * 100)) + " %");
       
	}

	public void saveImage(String name) {
		int[] tmp = new int[512 * 512];
		for (int i = 0; i < 512 * 512; i++) {
			tmp[i] = finalResults.get(i).classifiedLabel;
		}

		BufferedImage img = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0,512,512,tmp,0,512);
		File outputfile = new File(name);
		try {
			ImageIO.write(img, "jpg", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
