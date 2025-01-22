package com.example.Model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

public class LineModel implements Serializable {

    private static int order = 0;

    private UUID idLine;

    private String line;

    private int nbOrder;

    private String createdBy;
    private String modifiedBy;
    private String docName;

    public LineModel(UUID idLine, String line, int nbOrder, String createdBy, String docName) {
        this.idLine = idLine;
        this.line = line;
        this.nbOrder = nbOrder;
        this.createdBy = createdBy;
        this.modifiedBy = "";
        this.docName = docName;
    }

    public LineModel(UUID idLine, int nbOrder, String createdBy) {
        this.idLine = idLine;
        this.line = "";
        this.nbOrder = nbOrder;
        this.createdBy = createdBy;
        this.modifiedBy = "";
    }

    public LineModel(String createdBy) {
        this.idLine = UUID.randomUUID();
        this.line = "";
        this.nbOrder = order++;
        this.createdBy = createdBy;
        this.modifiedBy = "";
    }

    public LineModel(String line, int nbOrder, String createdBy) {
        this.idLine = UUID.randomUUID();
        this.line = line;
        this.nbOrder = nbOrder;
        this.createdBy = createdBy;
        this.modifiedBy = "";
    }

    public LineModel(String line, String createdBy) {
        this.idLine = UUID.randomUUID();
        this.line = line;
        this.nbOrder = order++;
        this.modifiedBy = "";
        this.createdBy = createdBy;
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

    public void setLine(String line, String modifiedBy) {
        this.line = line;
        this.modifiedBy = modifiedBy;
    }
    
    public int getNbOrder() {
        return nbOrder;
    }

    public void setNbOrder(int nbOrder) {
        this.nbOrder = nbOrder;
    }

    public String toString() {
        return "Line{ " + idLine + " " + line + " }";
    }


    public static int getOrder() {
        return order;
    }

    public static void setOrder(int order) {
        LineModel.order = order;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCreatedBy() {
        return createdBy;
    }
    public String getDocName () {
        return docName;
    }
    public void setDocName(String docName) {
        this.docName = docName;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public static LineModel restoreByBytes(byte[] content) {
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