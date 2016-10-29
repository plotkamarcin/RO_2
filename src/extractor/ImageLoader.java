package extractor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageLoader extends Loader {

	private ArrayList<BufferedImage> images;
	private ArrayList<File> fileList;

	public ArrayList<BufferedImage> getImages() {
		return images;
	}

	public void setImages(ArrayList<BufferedImage> images) {
		this.images = images;
	}
	
	public ImageLoader() {
		images = new ArrayList<BufferedImage>();
		fileList = new ArrayList<File>();
	}

	@Override
	public void loadSet(String name) {
 
		listFiles(name);	
		for(File f : fileList){
			BufferedImage img = null;
			String path = null;
			try {				
				img = ImageIO.read(f);
				images.add(img);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Image " + path + " not found!");
			}
		}	
	}
private void listFiles(String path){
		    File directory = new File(path);

	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isFile()) {
	            fileList.add(file);
	        } else if (file.isDirectory()) {
	            listFiles(file.getAbsolutePath());
	        }
	    }
	}

}

