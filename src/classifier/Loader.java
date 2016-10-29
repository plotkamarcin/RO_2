package classifier;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import extractor.Extractable;
import extractor.Image;

public class Loader {
	
	
	Loader(){
		
	}
	
	public ArrayList<Extractable> loadData(String name){
	
		ArrayList<Extractable>images=null;
		 try {
	         FileInputStream fileIn = new FileInputStream(name);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         images = (ArrayList<Extractable>) in.readObject();
	         in.close();
	         fileIn.close();
	         System.out.println("Image Data Loaded from "+name);
	      }catch(IOException i) {
	         i.printStackTrace();
	         
	      }catch(ClassNotFoundException c) {
	         System.out.println("Image Data not found");
	         c.printStackTrace();
	         
	      }
		 return images;
	}

}
