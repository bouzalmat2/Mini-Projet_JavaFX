
package com.mycompany.javafx_mvc.models;

public class Matiere {
    private int id;
    private String code;
    private String designation;
    private int VH;
    private String codeModule;

    public Matiere(int id, String code, String designation, int VH, String codeModule) {
        this.id = id;
        this.code = code;
        this.designation = designation;
        this.VH = VH;
        this.codeModule = codeModule;
    }
    
    
    
    
    

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDesignation() {
        return designation;
    }

    public int getVH() {
        return VH;
    }

    public String getCodeModule() {
        return codeModule;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setVH(int VH) {
        this.VH = VH;
    }

    public void setCodeModule(String codeModule) {
        this.codeModule = codeModule;
    }

   @Override
    public String toString() {
        return designation;
    }
    
    
    
    
}
