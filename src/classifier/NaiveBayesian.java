package classifier;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import extractor.Extractable;

public class NaiveBayesian {
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
	
	private class ErrorFunction {


	    // fractional error in math formula less than 1.2 * 10 ^ -7.
	    // although subject to catastrophic cancellation when z in very close to 0
	    // from Chebyshev fitting formula for erf(z) from Numerical Recipes, 6.2
	    public double erf(double z) {
	        double t = 1.0 / (1.0 + 0.5 * Math.abs(z));

	        // use Horner's method
	        double ans = 1 - t * Math.exp( -z*z   -   1.26551223 +
	                                            t * ( 1.00002368 +
	                                            t * ( 0.37409196 + 
	                                            t * ( 0.09678418 + 
	                                            t * (-0.18628806 + 
	                                            t * ( 0.27886807 + 
	                                            t * (-1.13520398 + 
	                                            t * ( 1.48851587 + 
	                                            t * (-0.82215223 + 
	                                            t * ( 0.17087277))))))))));
	        if (z >= 0) return  ans;
	        else        return -ans;
	    }

	    // fractional error less than x.xx * 10 ^ -4.
	    // Algorithm 26.2.17 in Abromowitz and Stegun, Handbook of Mathematical.
	    public double erf2(double z) {
	        double t = 1.0 / (1.0 + 0.47047 * Math.abs(z));
	        double poly = t * (0.3480242 + t * (-0.0958798 + t * (0.7478556)));
	        double ans = 1.0 - poly * Math.exp(-z*z);
	        if (z >= 0) return  ans;
	        else        return -ans;
	    }

	    // cumulative normal distribution
	    // See Gaussia.java for a better way to compute Phi(z)
	    public double Phi(double ro, double mi, double value) {
	        return 0.5 * (1.0 + erf((value-mi) / (ro*(Math.sqrt(2.0)))));

	    }
	}
	private ArrayList<Extractable> testData;
	private ArrayList<Extractable> trainData;
	private ArrayList<Result> finalResults;

	private String[] tempResults;
    private static double probability = 0.25;
	private int[][] confusionMatrix;
	private double meanValues [][];
	private double varianceValues [][];
	
	public NaiveBayesian(ArrayList<Extractable> test, ArrayList<Extractable> train){
		this.testData = test;
		this.trainData = train;
		this.finalResults = new ArrayList<Result>();
		meanValues = calculateMeanValueForClasses(train);
		varianceValues = calculateVarianceForClasses(meanValues, train);
	}
	
	public void classifyItems(){ // https://en.wikipedia.org/wiki/Naive_Bayes_classifier with gaussian distribution
		
		for(Extractable item:testData){
			
			double posterior1=calculateGaussianProbabilityFunction(varianceValues[0][0], meanValues[0][0], item.getFeature1())*calculateGaussianProbabilityFunction(varianceValues[0][1], meanValues[0][1], item.getFeature2())*calculateGaussianProbabilityFunction(varianceValues[0][2], meanValues[0][2], item.getFeature3())*calculateGaussianProbabilityFunction(varianceValues[0][3], meanValues[0][3], item.getFeature4());
			double posterior2=calculateGaussianProbabilityFunction(varianceValues[1][0], meanValues[1][0], item.getFeature1())*calculateGaussianProbabilityFunction(varianceValues[1][1], meanValues[1][1], item.getFeature2())*calculateGaussianProbabilityFunction(varianceValues[1][2], meanValues[1][2], item.getFeature3())*calculateGaussianProbabilityFunction(varianceValues[1][3], meanValues[1][3], item.getFeature4());
			double posterior3=calculateGaussianProbabilityFunction(varianceValues[2][0], meanValues[2][0], item.getFeature1())*calculateGaussianProbabilityFunction(varianceValues[2][1], meanValues[2][1], item.getFeature2())*calculateGaussianProbabilityFunction(varianceValues[2][2], meanValues[2][2], item.getFeature3())*calculateGaussianProbabilityFunction(varianceValues[2][3], meanValues[2][3], item.getFeature4());
			double posterior4=calculateGaussianProbabilityFunction(varianceValues[3][0], meanValues[3][0], item.getFeature1())*calculateGaussianProbabilityFunction(varianceValues[3][1], meanValues[3][1], item.getFeature2())*calculateGaussianProbabilityFunction(varianceValues[3][2], meanValues[3][2], item.getFeature3())*calculateGaussianProbabilityFunction(varianceValues[3][3], meanValues[3][3], item.getFeature4());	
			double evidence = posterior1+posterior2+posterior3+posterior4;
			
			ErrorFunction erf = new ErrorFunction();
			 double [] probabilities = new double[4];
			 probabilities[0]=erf.erf((probability*posterior1)/evidence);
			 probabilities[1]=erf.erf((probability*posterior2)/evidence);
			 probabilities[2]=erf.erf((probability*posterior3)/evidence);
			 probabilities[3]=erf.erf((probability*posterior4)/evidence);
			
			 int maxIndex = 0;
			 for (int i = 1; i < probabilities.length; i++){
			    double newnumber = probabilities[i];
			    if ((newnumber > probabilities[maxIndex])){
			    maxIndex = i;
			   }
			 }
			 switch (maxIndex){
			 case 0:
				 finalResults.add(new Result(item.getImageId(), 0));
				 break;
			 case 1:
				 finalResults.add(new Result(item.getImageId(), 63));
				 break;
			 case 2:
				 finalResults.add(new Result(item.getImageId(), 127));
				 break;
			 case 3:
				 finalResults.add(new Result(item.getImageId(), 195));
				 break;
			 }
		}
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
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private double [][] calculateMeanValueForClasses(ArrayList<Extractable> list){
        double[][] Mean = new double[4][4];
        for(int i=0;i<list.size();i++){
        	switch (list.get(i).getImageId()){
        	case 0:
        		Mean[0][0]+=list.get(i).getFeature1();
        		Mean[0][1]+=list.get(i).getFeature2();
        		Mean[0][2]+=list.get(i).getFeature3();
        		Mean[0][3]+=list.get(i).getFeature4();
        		break;
        	case 63:
        		Mean[1][0]+=list.get(i).getFeature1();
        		Mean[1][1]+=list.get(i).getFeature2();
        		Mean[1][2]+=list.get(i).getFeature3();
        		Mean[1][3]+=list.get(i).getFeature4();
        		break;
        	case 127:
        		Mean[2][0]+=list.get(i).getFeature1();
        		Mean[2][1]+=list.get(i).getFeature2();
        		Mean[2][2]+=list.get(i).getFeature3();
        		Mean[2][3]+=list.get(i).getFeature4();
        		break;
        	case 191:
        		Mean[3][0]+=list.get(i).getFeature1();
        		Mean[3][1]+=list.get(i).getFeature2();
        		Mean[3][2]+=list.get(i).getFeature3();
        		Mean[3][3]+=list.get(i).getFeature4();
        		break;
        	}
        }
        for (int i=0;i<4;i++){
        	for(int j =0;j<4;j++){
        		Mean[i][j]= Mean[i][j]/812;
        	}
        }
        return Mean;
	}
   private double[][] calculateVarianceForClasses(double[][] averages, ArrayList<Extractable> list){
	   double[][] Variances = new double[4][4];
	   for(int i=0;i<list.size();i++){
       	switch (list.get(i).getImageId()){
       	case 0:
       		Variances[0][0]+=Math.pow(list.get(i).getFeature1()-averages[0][0],2);
       		Variances[0][1]+=Math.pow(list.get(i).getFeature2()-averages[0][1],2);
       		Variances[0][2]+=Math.pow(list.get(i).getFeature3()-averages[0][2],2);
       		Variances[0][3]+=Math.pow(list.get(i).getFeature4()-averages[0][3],2);
       		break;
       	case 63:
       		Variances[1][0]+=Math.pow(list.get(i).getFeature1()-averages[1][0],2);
       		Variances[1][1]+=Math.pow(list.get(i).getFeature2()-averages[1][1],2);
       		Variances[1][2]+=Math.pow(list.get(i).getFeature3()-averages[1][2],2);
       		Variances[1][3]+=Math.pow(list.get(i).getFeature4()-averages[1][2],2);
       		break;
       	case 127:
       		Variances[2][0]+=Math.pow(list.get(i).getFeature1()-averages[2][0],2);
       		Variances[2][1]+=Math.pow(list.get(i).getFeature2()-averages[2][1],2);
       		Variances[2][2]+=Math.pow(list.get(i).getFeature3()-averages[2][2],2);
       		Variances[2][3]+=Math.pow(list.get(i).getFeature4()-averages[2][3],2);
       		break;
       	case 191:
       		Variances[3][0]+=Math.pow(list.get(i).getFeature1()-averages[3][0],2);
       		Variances[3][1]+=Math.pow(list.get(i).getFeature2()-averages[3][1],2);
       		Variances[3][2]+=Math.pow(list.get(i).getFeature3()-averages[3][2],2);
       		Variances[3][3]+=Math.pow(list.get(i).getFeature4()-averages[3][3],2);
       		break;
       	}
       }
	   for (int i=0;i<4;i++){
       	for(int j =0;j<4;j++){
       		Variances[i][j]= Variances[i][j]/811;
       	}
       }
	   return Variances;
   }
   private double calculateGaussianProbabilityFunction(double ro, double mi, double value){
  return (1/Math.pow((2*Math.PI*ro),0.5))*(Math.pow(Math.E, -Math.pow(value-mi, 2)/(2*ro)));
   }
	
}
