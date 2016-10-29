package extractor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Output {


	public <T> void saveToFile(String name, ArrayList<T> list){
	      try {
	          FileOutputStream fileOut =
	          new FileOutputStream(name);
	          ObjectOutputStream out = new ObjectOutputStream(fileOut);
	          out.writeObject(list);
	          out.close();
	          fileOut.close();
	          System.out.println("Serialized data is saved in "+name);
	       }catch(IOException i) {
	          i.printStackTrace();
	       }
	}
}
