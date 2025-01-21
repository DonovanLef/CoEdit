package com.example.Model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Document implements Serializable{

	private String name;
	private List<LineModel> lines;

	public Document(){
		this.lines = new ArrayList<>();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<LineModel> getLines() {
		return lines;
	}
	public void setLines(List<LineModel> lines) {
		this.lines = lines;
	}

	public void addLine(LineModel line){
		this.lines.add(line);
	}

	public String toString(){
		String res = "Document{ "+name+" }\n";
		for (LineModel line : lines) {
			res += line;
		}
		return res;
	}

	public void save(String path){
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path+this.name+".ser"))) {
			oos.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);
        oos.close();
        return baos.toByteArray();
    }

	public static Document restoreByFile(String name){
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(name))) {
			Document doc = (Document) ois.readObject();
			return doc;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Document restoreByBytes(byte[] content){
        try (ByteArrayInputStream bais = new ByteArrayInputStream(content);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            Document doc = (Document) ois.readObject();
            return doc;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
	}
	public static void deleteFile(String name){
		String path = Folder.PATH+name+".ser";
		try {
			if (new java.io.File(path).delete()) {
				System.out.println("File deleted successfully");
			} else {
				System.out.println("Failed to delete the file");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


}