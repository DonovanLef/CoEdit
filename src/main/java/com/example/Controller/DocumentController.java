package com.example.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.Model.Document;
import com.example.Model.Folder;

public class DocumentController {

	private static Map<String, Document> documents;

	public static List<Document> getDocuments() {
		if (documents == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(documents.values());
	}

	public static Map<String, Document> getDocumentsMap(){
		return documents;
	}

	public static void addDocument(Document doc){
		documents.put(doc.getName(), doc);
	}

	public static void initDocs(){
		Folder folder = new Folder();
		folder.scanDocumentsFolder();
		List<File> files = folder.getFiles();
		for (File file : files) {	
		}
	}
	
}
