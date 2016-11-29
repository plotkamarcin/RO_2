package extractor;

import java.io.Serializable;
import java.util.Random;

import javax.swing.JTree;

import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.fft.DoubleFFT_2D;

public class TextureImage extends Image implements Serializable, Extractable {

	private static final long serialVersionUID = -1314368100390599483L;
	TextureImage(int[] image, int size, int label) {
		super(image, size, label);
	}

	@Override
	public double calculateFirstFeature() {

    	double[] tmp = new double[getImageTable().length];
    	for(int i=0;i<getImageTable().length;i++){
    		tmp[i]=getImageTable()[i];
    	}
double[] result = calculateFFT(tmp);
		
		return result[0];
		
	}

	@Override
	public double calculateSecondFeature() {
		double[] tmp = new double[getImageTable().length];
		for (int i = 0; i < getImageTable().length; i++) {
			tmp[i] = getImageTable()[i];
		}
		double[] histogram = calculateHistogram(tmp);
		int maxIndex = 0;
		for (int i = 1; i < histogram.length; i++) {
			double newnumber = histogram[i];
			if ((newnumber > histogram[maxIndex])) {
				maxIndex = i;
			}
		}
		return histogram[maxIndex];
	}

	@Override
	public double calculateThirdFeature() {
		return Math.random() * 50 + 1;
	}

	public double calculateFourthFeature() {
		return Math.random() * 550 + 100;
	}

	@Override
	public double getFeature5() {
		return 0.0;
	}

	private double[] calculateFFT(double[] tab) {
		int size = (int)Math.pow(tab.length, 0.5);
	   	// trzeba dac tablice 2 razy wieksza, zwraca w formie i- czesc rzeczywista, i+1 czesc urojona
		double[] fftData = new double[tab.length* 2];	
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
