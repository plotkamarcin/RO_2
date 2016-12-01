package extractor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JTree;

import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.fft.DoubleFFT_2D;

public class TextureImage extends Image implements Serializable, Extractable {

	private static final long serialVersionUID = -1314368100390599483L;
	private transient double[][] Image2D;
	private transient GLCM matrix;

	TextureImage(int[] image, int size, int label) {
		super(image, size, label);
		int TabSize = (int) Math.pow(getImageTable().length, 0.5);
		Image2D = new double[TabSize][TabSize];

		for (int i = 0; i < TabSize; i++) {
			for (int j = 0; j < TabSize; j++) {
				Image2D[i][j] = getImageTable()[TabSize * i + j];
			}
		}
		matrix = GLCM.createGLCM(Image2D, 2, 2, 10, false, true, 0.0, 255.0);
	}

	@Override
	public double calculateFirstFeature() {
		double[] tmp = new double[getImageTable().length];
		for (int i = 0; i < getImageTable().length; i++) {
			tmp[i] = getImageTable()[i];
		}
		double[] histogram = calculateHistogram(tmp);
        
	double avg =0.0;
	for (int i=0;i<histogram.length;i++){
		avg+=i*histogram[i];
	}
		return (avg/getImageTable().length)/256;

	}

	@Override
	public double calculateSecondFeature() {
		return matrix.computeContrast();
	}

	@Override
	public double calculateThirdFeature() {
		return matrix.computeCorrelation();
	}

	@Override
	public double calculateFourthFeature() {
		return matrix.computeEnergy();
	}

	@Override
	public double calculateFifthFeature() {
		return matrix.computeHomogeneity();
	}

	public double calculateFFT1() {
		double[] tmp = new double[getImageTable().length];
		for (int i = 0; i < getImageTable().length; i++) {
			tmp[i] = getImageTable()[i];
		}
		double[] result = calculateFFT(tmp);
		double avgSpectrum=0.0;
		for(int i=0;i<result.length-1;i+=2){
			avgSpectrum+=Math.pow(Math.pow(result[i], 2)+Math.pow(result[i+1], 2), 0.5);
		}
		return avgSpectrum/(result.length/2);
	}
	private double calculateFFT2() {
		double[] tmp = new double[getImageTable().length];
		for (int i = 0; i < getImageTable().length; i++) {
			tmp[i] = getImageTable()[i];
		}
		double[] result = calculateFFT(tmp);
		double avgPhase=0.0;
		for(int i=0;i<result.length-1;i+=2){
			avgPhase+=Math.atan2(result[i+1], result[i]);
		}
		return avgPhase/(result.length/2);
	}

	private double calculateFFT3() {
		double[] tmp = new double[getImageTable().length];
		for (int i = 0; i < getImageTable().length; i++) {
			tmp[i] = getImageTable()[i];
		}
		double[] result = calculateFFT(tmp);
		double QuartileAvg=0.0;
        int Quartile=((int)Math.sqrt(getImageTable().length))/4;
        for(int i=0;i<Quartile*2;i+=2){
        		QuartileAvg+=Math.pow(Math.pow(result[i], 2)+Math.pow(result[i+1], 2), 0.5);
        }
        QuartileAvg/=Quartile;
        return QuartileAvg;
	}

	private double calculateFFT4() {
		double[] tmp = new double[getImageTable().length];
		for (int i = 0; i < getImageTable().length; i++) {
			tmp[i] = getImageTable()[i];
		}
		double[] result = calculateFFT(tmp);
		double QuartileAvg=0.0;
        int Quartile=((int)Math.sqrt(getImageTable().length))/4;
        for(int i=Quartile*2;i<Quartile*4;i+=2){
        		QuartileAvg+=Math.pow(Math.pow(result[i], 2)+Math.pow(result[i+1], 2), 0.5);
        }
        QuartileAvg/=Quartile;
        return QuartileAvg;
	}

	private double calculateFFT5() {
		double[] tmp = new double[getImageTable().length];
		for (int i = 0; i < getImageTable().length; i++) {
			tmp[i] = getImageTable()[i];
		}
		double[] result = calculateFFT(tmp);
		double QuartileAvg=0.0;
        int Quartile=((int)Math.sqrt(getImageTable().length))/4;
        for(int i=Quartile*3;i<Quartile*6;i+=2){
        		QuartileAvg+=Math.pow(Math.pow(result[i], 2)+Math.pow(result[i+1], 2), 0.5);
        }
        QuartileAvg/=Quartile;
        return QuartileAvg;
	}
	private double calculateFFT6() {
		double[] tmp = new double[getImageTable().length];
		for (int i = 0; i < getImageTable().length; i++) {
			tmp[i] = getImageTable()[i];
		}
		double[] result = calculateFFT(tmp);
		double QuartileAvg=0.0;
        int Quartile=((int)Math.sqrt(getImageTable().length))/4;
        for(int i=Quartile*4;i<Quartile*8;i+=2){
        		QuartileAvg+=Math.pow(Math.pow(result[i], 2)+Math.pow(result[i+1], 2), 0.5);
        }
        QuartileAvg/=Quartile;
        return QuartileAvg;
	}
	@Override
	public void calculateFeatures() {
		FeatureVector = new ArrayList<>();
		FeatureVector.add(calculateFirstFeature());
		FeatureVector.add(calculateSecondFeature());
		FeatureVector.add(calculateThirdFeature());
		FeatureVector.add(calculateFourthFeature());
		FeatureVector.add(calculateFifthFeature());
		//FeatureVector.add(calculateFFT1());
		//FeatureVector.add(calculateFFT2());
		//FeatureVector.add(calculateFFT3());
		//FeatureVector.add(calculateFFT4());
		//FeatureVector.add(calculateFFT5());
		//FeatureVector.add(calculateFFT6());
		
	}



	private double[] calculateFFT(double[] tab) {
		int size = (int) Math.pow(tab.length, 0.5);
		// trzeba dac tablice 2 razy wieksza, zwraca w formie i- czesc
		// rzeczywista, i+1 czesc urojona
		double[] fftData = new double[tab.length * 2];
		for (int i = 0; i < tab.length; i++) {
			// copying audio data to the fft data buffer, imaginary part is 0
			fftData[2 * i] = getImageTable()[i];
			fftData[2 * i + 1] = 0;
		}
		DoubleFFT_2D fft2 = new DoubleFFT_2D(size, size);
		fft2.complexForward(fftData);
		return fftData;
	}

	private double[] calculateHistogram(double[] tab) {
		double[] bins = new double[256];
		for (int i = 0; i < tab.length; i++) {
			bins[(int) tab[i]]++;
		}
		return bins;
	}
}
