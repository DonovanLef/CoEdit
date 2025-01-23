package com.example.Model;

import java.util.HashMap;
import java.util.Map;

public class Starter {

	public long lasttime; 
	public Map<String, Document> documentsReceived;
	public long initTime;

	public Starter(){
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
