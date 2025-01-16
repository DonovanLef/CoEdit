package com.example.Model;

import java.util.UUID;

public class LineModel {

    private UUID idLine;
    private String line;

	public LineModel(UUID idLine, String line) {
        this.idLine = idLine;
        this.line = line;
    }

    public LineModel(String line) {
        this.idLine = UUID.randomUUID(); // Génère un GUID unique
        this.line = line;
    }

    public LineModel() {
        this.idLine = UUID.randomUUID(); // Génère un GUID unique
        this.line = "";
    }

    public UUID getIdLine() {
        return idLine;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}