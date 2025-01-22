package com.example.Controller;

import java.util.HashMap;
import java.util.Map;

import com.example.Model.Document;

public class StarterController {

	public long lasttime; 
	public Map<String, Document> documentsReceived;
	public long initTime;

	public StarterController(){
		this.lasttime = System.currentTimeMillis();
		this.documentsReceived = new HashMap<>();
		this.initTime = System.currentTimeMillis();
	}

	public synchronized void updateLastTime(){
		this.lasttime = System.currentTimeMillis();
	}

	public void clearDocumentsReceived() {
		documentsReceived = new HashMap<>();
	}
	
}
