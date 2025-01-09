package com.example.Model;

public class LineModel {

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


	
}
