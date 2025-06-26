package com.mycompany.javafx_mvc.models;

public class ArchiveEtudiant {
     private String code;
    private String nom;
    private String prenom;
    private String niveau;
    private String codeFiliere;
    private String dateSuppression; 

    public String getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNiveau() {
        return niveau;
    }

    public String getCodeFiliere() {
        return codeFiliere;
    }

    public String getDateSuppression() {
        return dateSuppression;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public void setCodeFiliere(String codeFiliere) {
        this.codeFiliere = codeFiliere;
    }

    public void setDateSuppression(String dateSuppression) {
        this.dateSuppression = dateSuppression;
    }
    
    
}
