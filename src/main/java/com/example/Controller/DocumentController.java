package com.example.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Model.Document;
import com.example.Model.Folder;

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
			try {
				byte[] bytes = Files.readAllBytes(Path.of(file.getAbsolutePath()));
				Document doc = Document.restoreByBytes(bytes);
				documents.put(doc.getName(), doc);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void clear() {
		documents = new HashMap<>();
	}
	
}
