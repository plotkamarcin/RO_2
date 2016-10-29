package extractor;

import java.io.Serializable;

import javax.swing.JTree;

import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.fft.DoubleFFT_2D;

public class TextureImage extends Image implements Serializable,Extractable{

	private static final long serialVersionUID = -1314368100390599483L;
	
	TextureImage(int[] image, int size, int label){
		super(image,size,label);
	}
    @Override
	public double calculateFirstFeature(){
 
    	double[] tmp = new double[64*64];
    	for(int i=0;i<getImageTable().length;i++){
    		tmp[i]=getImageTable()[i];
    	}
    	double[] result = calculateFFT(tmp);
    	return 0.0;
	}
    @Override
	public double calculateSecondFeature(){
    	double[] tmp = new double[64*64];
    	for(int i=0;i<getImageTable().length;i++){
    		tmp[i]=getImageTable()[i];
    	}
    	double [] histogram = calculateHistogram(tmp);
     return 0.0;
	}
    @Override
	public double calculateThirdFeature(){
     return 0.0;
	}
    public double calculateFourthFeature(){
     return 0.0;
    }
	@Override
	public double getFeature5() {
		return 0.0;
	}
private double[] calculateFFT(double[]tab){    
   	// trzeba dac tablice 2 razy wieksza, zwraca w formie i- czesc rzeczywista, i+1 czesc urojona
	double[] fftData = new double[64*64* 2];	
    for (int i = 0; i < tab.length; i++) {
        // copying audio data to the fft data buffer, imaginary part is 0
        fftData[2 * i] = getImageTable()[i];
        fftData[2 * i + 1] = 0;
    }
	DoubleFFT_2D fft2 = new DoubleFFT_2D(64, 64);
	fft2.complexForward(fftData);
return fftData;
}
private double[] calculateHistogram(double[] tab){
	double[]bins = new double[256];
	for(int i=0;i<tab.length;i++){
		bins[(int) tab[i]]++;
	}
	return bins;
}
}
