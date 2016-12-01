package extractor;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Image implements Serializable,Extractable {
	
	private static final long serialVersionUID = 4623746839237756244L;
	
private int imageId;

private transient int[] imageTable;
protected ArrayList<Double> FeatureVector;

public int[] getImageTable() {
	return imageTable;
}

public void setImageTable(int[] imageTable) {
	this.imageTable = imageTable;
}
@Override
public int getImageId() {
	return imageId;
}

public void setImageId(int imageId) {
	this.imageId = imageId;
}

Image(int[] image, int size, int label){
	imageTable= new int[size*size];
	imageTable=image;
	imageId= label;
}

public double calculateFirstFeature(){
	//srodek ciezkosci cyfry reprezentowany jako wektor i wyznaczamy jego d³ugoœæ
	
int area=0;
for(int i=0;i<imageTable.length;i++){
	if(imageTable[i]>0){
		area++;
	}
}
double xPrime=0.0;
double yPrime=0.0;

for(int i=0;i<imageTable.length;i++){
	if(imageTable[i]>0){
		xPrime+=i;
	}
}
for(int i=0;i<27;i++){
	for(int j=0;j<27;j++){
			if(imageTable[j*27+i]>0){
		yPrime+=i;
	}
	}

}
xPrime=xPrime/area;
yPrime=yPrime/area;
return Math.pow((xPrime*xPrime+yPrime*yPrime), 0.5);

}
public double calculateSecondFeature(){
	
	//operatory sobela, wyznaczamy cyfre jako k¹t wektora gradientu do osi OX
	int[] sobelH = {-1,0,1,-2,0,2,-1,0,1};
	int[] sobelV = {-1,-2,-1,0,0,0,1,2,1};
	
	double [] tempH = new double[28*28];
	double [] tempV = new double[28*28];
	for(int j=1;j<27;j++){
	for(int i=1;i<27;i++){
		tempH[28*j+i]=(imageTable[28*j+i]*sobelH[4]+imageTable[(28*j+i)-1]*sobelH[3]+imageTable[(28*j+i)+1]*sobelH[5]
				+imageTable[(28*j+i)-29]*sobelH[0]+imageTable[(28*j+i)-28]*sobelH[1]+imageTable[(28*j+i)-27]*sobelH[2]
				+imageTable[(28*j+i)+27]*sobelH[6]+imageTable[(28*j+i)+28]*sobelH[7]+imageTable[(28*j+i)+29]*sobelH[8])/9;
	}
	}
	for(int j=1;j<27;j++){
	for(int i=1;i<27;i++){
		tempV[28*j+i]=(imageTable[28*j+i]*sobelV[4]+imageTable[(28*j+i)-1]*sobelV[3]+imageTable[(28*j+i)+1]*sobelV[5]
				+imageTable[(28*j+i)-29]*sobelV[0]+imageTable[(28*j+i)-28]*sobelV[1]+imageTable[(28*j+i)-27]*sobelV[2]
				+imageTable[(28*j+i)+27]*sobelV[6]+imageTable[(28*j+i)+28]*sobelV[7]+imageTable[(28*j+i)+29]*sobelV[8])/9;
	}
	}

	double[] vectors = new double[28*28];
	double avgLength=0.0;
	for (int i=0;i<vectors.length;i++){
		vectors[i]=Math.pow((tempH[i]*tempH[i]+tempV[i]+tempV[i]), 0.5);
		avgLength+=vectors[i];
	}
	
	double[]angles=new double[28*28];
	double avgAngle=0.0;
	double count=0.0;
	for (int i=0;i<angles.length;i++){
		angles[i]=Math.atan2(tempH[i],tempV[i]);
		avgAngle+=angles[i];
		if(angles[i]>0){
			count++;
		}
	}
	return avgAngle/count;
}
public double calculateThirdFeature(){
	//moment centralny M11
	double[]tmp= new double[28*28] ;
	for(int i=0;i<imageTable.length;i++){
		tmp[i]= normalize(imageTable[i]);
	}
	double m00 = calculateMomentum(0, 0, tmp);
	double m01 = calculateMomentum(0, 1, tmp);
	double m10 = calculateMomentum(1, 0, tmp);
	double m11 = calculateMomentum(1, 1, tmp);
	return m11-(m10/m00)*m01;
}

public double calculateFourthFeature(){
	//moment geometryczny M30
	double[]tmp= new double[28*28] ;
	for(int i=0;i<imageTable.length;i++){
		tmp[i]= normalize(imageTable[i]);
	}
	double m00 = calculateMomentum(0, 0, tmp);
	double m10 = calculateMomentum(1, 0, tmp);
	double m20 = calculateMomentum(2, 0, tmp);
	double m30 = calculateMomentum(3, 0, tmp);
	return m30-3*m20*(m10/m00)+2*m10*Math.pow((m10/m00), 2.0);
}

public double calculateFifthFeature(){
	//kraglosc obiektu - roundness
	double area=0.0;
	for(int i=0;i<imageTable.length;i++){
		if(imageTable[i]>0){
			area++;
		}
	}
	
	double perimeter =0.0;
	for(int i=1;i<10;i++){
		for(int j=1;j<27;j++){
			if(imageTable[28*i+j]+1!=0 || 
					imageTable[28*i+j]-1!=0 || 
					imageTable[28*i+j]+28!=0||
					imageTable[28*i+j]-28!=0){
				perimeter++;
			}
		}	
	}
	double roundness = 4*Math.PI*(area/Math.pow(perimeter, 2.0));
	return roundness;
	
}

protected double calculateMomentum(int p, int q, double[] tab){
	double sum=0.0;
	for(int i=0;i<28;i++){
		for(int j=0;j<28;j++){
			sum+=Math.pow(i, p)*Math.pow(j, q)*tab[j*28+i];
		}
	}
	return sum;
}
protected double normalize(double value){
	return (value-0)/255.0;
}
protected double normalize(double value, double min, double max){
	return (value - min)/(max-min);
}

@Override
public ArrayList<Double> getFeatures() {
	return FeatureVector;
}
public void calculateFeatures(){
	FeatureVector = new ArrayList<>();
	FeatureVector.add(calculateFirstFeature());
	FeatureVector.add(calculateSecondFeature());
	FeatureVector.add(calculateThirdFeature());
	FeatureVector.add(calculateFourthFeature());
}
}
