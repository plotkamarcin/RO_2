package extractor;

import java.util.ArrayList;
import java.util.Random;

public class ImageProcessor extends FeatureProcessor {



	ArrayList<TextureImage> firstImage;
	ArrayList<TextureImage> secondImage;
	ArrayList<TextureImage> thirdImage;
	
	public ArrayList<TextureImage> getFirstImage() {
		return firstImage;
	}
	public ArrayList<TextureImage> getSecondImage() {
		return secondImage;
	}
	public ArrayList<TextureImage> getThirdImage() {
		return thirdImage;
	}
	
	int [] image1 = new int[512*512];
	int [] image2 = new int[512*512];
	int [] image3 = new int[512*512];
	
	public ImageProcessor(ImageLoader loader) {
		super(loader);
		separateImages();
	}
    private void separateImages(){
		for (int j = 0; j < 512 * 512; j++) {
			image1[j] = this.getStarImages().get(3).getImageTable()[j];
		}
		for (int j = 0; j < 512 * 512; j++) {
			image2[j] = this.getStarImages().get(4).getImageTable()[j];
		}
		for (int j = 0; j < 512 * 512; j++) {
			image3[j] = this.getStarImages().get(5).getImageTable()[j];
		}

    	
    }
	public void processTexturedImage(int FrameSize) {
		firstImage=new ArrayList<>();
		int factor = FrameSize*2+1;
		for(int i=0;i<512*512;i++){
			int [] buffer = new int [factor*factor];
			
            for(int j=0;j<factor;j++){
            	for(int k=0;k<factor;k++){
            		int x=i+(((k-1)*512))-(k-1);
            		
            		if(i+(((k-1)*512))-(k-1)<0 ||i+(((k-1)*512))-(k-1)>=(512*512) ){
            			buffer[j*factor+k]=0;
            		}
            		else{
            		buffer[j*factor+k]=image1[i+(((k-1)*512))-(k-1)];
            		}
            	}
            }
//			buffer[0]=image1[i-512-1];
//			buffer[1]=image1[i-512];
//			buffer[2]=image1[i-512+1];
//			buffer[3]=image1[i-1];
//			buffer[4]=image1[i];
//			buffer[5]=image1[i+1];
//			buffer[6]=image1[i+512-1];
//		    buffer[7]=image1[i+512];
//		    buffer[8]=image1[i+512+1];
			//buffer[(factor*factor)/2]=image1[i];
			firstImage.add(new TextureImage(buffer, factor, this.getStarImages().get(0).getImageTable()[i]));
		}
		calculateFeatures();
	}
	public void calculateFeatures(){

		 Random rand = new Random();
		for(TextureImage t : firstImage){
			t.setFeature1(t.calculateFirstFeature());
			t.setFeature2(t.calculateSecondFeature());
			t.setFeature3(t.calculateThirdFeature());
			t.setFeature4(t.calculateFourthFeature());
		}
	}
}
