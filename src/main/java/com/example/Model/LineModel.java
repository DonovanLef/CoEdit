package com.example.Model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

public class LineModel implements Serializable{

    private UUID idLine;

    private String line;

    public LineModel(UUID idLine, String line) {
        this.idLine = idLine;
        this.line = line;
    }

    public LineModel(UUID idLine) {
        this.idLine = idLine;
        this.line = "";
    }

	public LineModel() {
        this.idLine = UUID.randomUUID();
        this.line = "";
    }

	public LineModel(String line) {
        this.idLine = UUID.randomUUID();
        this.line = line;
    }

    public UUID getIdLine() {
        return idLine;
    }

    public void setIdLine(UUID idLine) {
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