package com.example.Model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.Controller.DocumentController;

public class Folder {

    private List<File> files;
    public static final String PATH = System.getProperty("user.dir") +"/documents/";

    public Folder() {
        files = new ArrayList<>();
    }

    // Méthode pour ajouter un fichier au dossier
    public void addFile(File file) {
        files.add(file);
    }

    // Methode pour supprimer un fichier au dossier
    public void removeFile(File file) {
        files.remove(file);
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
        files.clear();
        DocumentController.clear();
        // Remplacez ceci par le chemin de votre dossier Documents
        File folder = new File(PATH);
        if (folder.exists() && folder.isDirectory()) {
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        addFile(file); // Ajouter chaque fichier à la liste
                        DocumentController.addDocument(Document.restoreByFile(PATH + file.getName()));
                    }
                }
            }
        }
    }

    public void renameFile(String oldName, String newName) {
        try {
            File oldFile = new File(PATH, oldName);
            File newFile = new File(PATH, newName);
    
            if (oldFile.exists() && oldFile.renameTo(newFile)) {
                // System.out.println("Fichier renommé de " + oldName + " à " + newName);
                files.remove(oldFile);
                files.add(newFile);
            } else {
                // Rétablir l'ancien nom de fichier dans le dossier
                files.set(files.indexOf(newFile), oldFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createFile(String fileName) {
        try {
            File newFile = new File(PATH, fileName);
    
            System.out.println(PATH);
            System.out.println(fileName);
            if (newFile.createNewFile()) {
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


    public void deleteFile(String name) {
        File file = getFile(name);
        if (file != null) {
            if (file.delete()) {
                System.out.println("Fichier supprimé : " + name);
                files.remove(file);
            } else {
                System.out.println("Impossible de supprimer le fichier.");
            }
            files.remove(file);
        }
    }
}
