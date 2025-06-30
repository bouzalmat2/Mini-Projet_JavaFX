
package com.mycompany.javafx_mvc.models;


public class Note {
    private int id;
    private String codeEleve;
    private String codeMatiere;
    private double note;

    public Note(int id, String codeEleve, String codeMatiere, double note) {
        this.id = id;
        this.codeEleve = codeEleve;
        this.codeMatiere = codeMatiere;
        this.note = note;
    }
     public Note( String codeEleve, String codeMatiere, double note) {
        this.codeEleve = codeEleve;
        this.codeMatiere = codeMatiere;
        this.note = note;
    }
    
    
    
    
    
    

    public int getId() {
        return id;
    }

    public String getCodeEleve() {
        return codeEleve;
    }

    public String getCodeMatiere() {
        return codeMatiere;
    }

    public double getNote() {
        return note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCodeEleve(String codeEleve) {
        this.codeEleve = codeEleve;
    }

    public void setCodeMatiere(String codeMatiere) {
        this.codeMatiere = codeMatiere;
    }

    public void setNote(double note) {
        this.note = note;
    }
    
    

}
