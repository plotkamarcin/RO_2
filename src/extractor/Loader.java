package extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Loader {
	
	
	private byte[] dataImages;
	private byte[] dataLabels;
	
	public byte[] getDataImages() {
		return dataImages;
	}
	public void setDataImages(byte[] dataImages) {
		this.dataImages = dataImages;
	}
	public byte[] getDataLabels() {
		return dataLabels;
	}
	public void setDataLabels(byte[] dataLabels) {
		this.dataLabels = dataLabels;
	}

	

	public int getDataLength(){
		return dataLabels.length-8; 
	}
	public void showNumber(int pos){
		int [] temp= new int[28*28];
		for (int i=0;i<27*27;i++){
			temp[i]=dataImages[(i+16)+(28*28)*(pos-1)]&0xff;
		}
		
		for (int i=0;i<27*27;i++){
			if(i%28==0){
				System.out.println("");
			}
			else{
				if(temp[i]>85){
					System.out.print("*");
				}
				if(temp[i]>170){
					System.out.print("#");
				}

				else{
					System.out.print(" ");
				}
			}
			
		}
		
		System.out.print("Etykieta to: " + dataLabels[pos+7]);
	}
	public void loadSet(String name) {

		File file1 = new File("E:\\ro\\"+name+"-images.idx3-ubyte");
		int size1 = (int) file1.length();
		dataImages=new byte[size1];
		FileInputStream in1;
		try {
			in1 = new FileInputStream(file1);
			in1.read(dataImages);
			in1.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}	
		File file2 = new File("E:\\ro\\"+name+"-labels.idx1-ubyte");
		int size2 = (int) file2.length();
		dataLabels=new byte[size2];
		FileInputStream in2;
		try {
			in2 = new FileInputStream(file2);
			in2.read(dataLabels);
			in2.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}		
	}
	public int[] getImageWithIndex(int index){
		int [] temp= new int[28*28];
		for (int i=0;i<27*27;i++){
			temp[i]=dataImages[(i+16)+(28*28)*(index-1)]&0xff;
		}
		return temp;
	}
	public int getImageLabelWithIndex(int index){
		return dataLabels[index+7];
	}
}
