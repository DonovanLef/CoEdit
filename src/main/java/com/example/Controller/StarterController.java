package com.example.Controller;

import java.util.Map;

import com.example.Model.Document;

public class StarterController {

	public long lasttime; 
	public Map<String, Document> documentsReceived;

	public StarterController(){
		this.lasttime = System.currentTimeMillis();
	}

	public synchronized void updateLastTime(){
		this.lasttime = System.currentTimeMillis();
	}
	
}
