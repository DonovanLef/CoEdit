package com.example.Model;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        String documentsPath ="/documents"; // Chemin vers Documents sur la machine de l'utilisateur
        
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
