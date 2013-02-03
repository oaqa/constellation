package edu.cmu.lti.oaqa.graph.core.load;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class TextFileWriter {
	long lineCount = 0;
	long bytesSoFar = 0;
	long length;
	boolean printUpdates=true;
	boolean isFirstLine=true;
	BufferedWriter out;

	public TextFileWriter(File f) throws IOException{
		Writer fr = new FileWriter(f);
		this.out = new BufferedWriter(fr);
	}
	
	public TextFileWriter(String location) throws IOException{
		selectFile(location);
	}
	

  public void selectFile(String fileName) throws IOException{
		File f = new File(fileName);
		Writer fr = new FileWriter(f);
		this.out = new BufferedWriter(fr);
		
	}
	
	public void next(String line) throws IOException{
		if(this.isFirstLine==false){
			out.newLine();
		}
		out.write(line);
		this.isFirstLine=false;
	}
	
	public void destroy() throws IOException{
		if (out!=null){
			out.close();
		}
	}
	
	public static void writeWholeFile(File file, String string) throws IOException{
		TextFileWriter writer = new TextFileWriter(file);
		writer.next(string);
		writer.destroy();
	}
	
	public static void writeWholeFile(String fileLocation, String string) throws IOException{
		File file = new File(fileLocation);
		writeWholeFile(file,string);
	}
	
	public static void clearFile(File file) throws IOException{
		file.delete();
		file.createNewFile();
	}

	public static void clearFile(String fileLocation) throws IOException {
		clearFile(new File(fileLocation));
	}
	
}