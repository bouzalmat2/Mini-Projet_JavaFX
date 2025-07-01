package com.mycompany.javafx_mvc.models;

public class Eleve {
    private int id;
    private String code,nom, prenom,niveau,codeFiliere;
    
    
    public Eleve(int id, String code, String nom, String prenom, String niveau, String codeFiliere){
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.niveau = niveau;
        this.codeFiliere = codeFiliere;
    
    
    }
       public Eleve( String code, String nom, String prenom, String niveau, String codeFiliere){
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.niveau = niveau;
        this.codeFiliere = codeFiliere;
    
    
    }
   
   
  

    public int getId() {
        return id;
    }

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

    public void setId(int id) {
        this.id = id;
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
    
    @Override
    public String toString() {
        return code + " - " + nom + " " + prenom;
    }
    
    
    
    
    
}
