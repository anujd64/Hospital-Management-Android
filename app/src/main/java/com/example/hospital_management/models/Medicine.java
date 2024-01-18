package com.example.hospital_management.models;

public class Medicine {
    String name;
    int dosage;

    public Medicine(){}
    public Medicine(String name, int dosage){
        this.setDosage(dosage);
        this.setName(name);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }
}
