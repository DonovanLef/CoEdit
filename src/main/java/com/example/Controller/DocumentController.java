package com.example.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Model.Document;

public class DocumentController {

	private static Map<String, Document> documents;
	
	static{
		documents = new HashMap<>();
	}

	public static List<Document> getDocuments() {
		if (documents == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(documents.values());
	}

	public static void addDocument(Document doc){
		documents.put(doc.getName(), doc);
	}
	
}
