package com.example.Model;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ListView;

public class Folder {

    private List<File> files;

    public Folder() {
        files = new ArrayList<>();
    }

    // Méthode pour ajouter un fichier au dossier
    public void addFile(File file) {
        files.add(file);
    }

    // Méthode pour récupérer tous les fichiers du dossier
    public List<File> getFiles() {
        return files;
    }

    // Méthode pour récupérer un fichier spécifique
    public File getFile(String name) {
        for (File file : files) {
            if (file.getName().equals(name)) {
                return file;
            }
        }
        return null;
    }

    // Méthode pour lister les fichiers sous forme de chaînes de caractères (pour l'affichage)
    public List<String> getFileNames() {
        List<String> fileNames = new ArrayList<>();
        for (File file : files) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }
    public void scanDocumentsFolder() {
        // Remplacez ceci par le chemin de votre dossier Documents
        String documentsPath =System.getProperty("user.dir") +"/documents"; // Chemin vers Documents sur la machine de l'utilisateur
        System.out.println(documentsPath); // Chemin racine du projet
        File folder = new File(documentsPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        addFile(file); // Ajouter chaque fichier à la liste
                    }
                }
            }
        }
    }
    public void renameFile(String oldName, String newName, ListView<String> fileListView) {
        try {
            String projectPath = System.getProperty("user.dir");
            String documentsPath = projectPath + File.separator + "documents";
            File oldFile = new File(documentsPath, oldName);
            File newFile = new File(documentsPath, newName);
    
            if (oldFile.exists() && oldFile.renameTo(newFile)) {
                System.out.println("Fichier renommé de " + oldName + " à " + newName);
            } else {
                System.out.println("Impossible de renommer le fichier.");
                // Rétablir l'ancien nom dans la liste
                int index = fileListView.getItems().indexOf(newName);
                fileListView.getItems().set(index, oldName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String createFile(String fileName) {
        try {
            String projectPath = System.getProperty("user.dir");
            String documentsPath = projectPath + File.separator + "documents";
            File newFile = new File(documentsPath, fileName);
    
            if (newFile.createNewFile()) {
                System.out.println("Fichier créé : " + newFile.getName());
                addFile(newFile); // Ajouter au modèle
                return "";
            } else {
                return "Attention: Le fichier existe déjà.";
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la création du fichier.");
            return "Erreur: Une erreur s'est produite lors de la création du fichier.";
        }
    }
}
