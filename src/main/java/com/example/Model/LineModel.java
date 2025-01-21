package com.example.Model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LineModel implements Serializable{

    private long idLine;

    private String line;

    public LineModel(long idLine, String line) {
        this.idLine = idLine;
        this.line = line;
    }

    public LineModel(long idLine) {
        this.idLine = idLine;
        this.line = "";
    }

    public long getIdLine() {
        return idLine;
    }

    public void setIdLine(long idLine) {
        this.idLine = idLine;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String toString(){
        return "Line{ "+idLine+" "+line+" }";
    }
	public static LineModel restoreByBytes(byte[] content){
        try (ByteArrayInputStream bais = new ByteArrayInputStream(content);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            LineModel doc = (LineModel) ois.readObject();
            return doc;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);
        oos.close();
        return baos.toByteArray();
    }


    
}