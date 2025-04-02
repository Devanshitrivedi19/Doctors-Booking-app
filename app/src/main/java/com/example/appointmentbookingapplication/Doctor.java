package com.example.appointmentbookingapplication;

public class Doctor {
    private String name;
    private String speciality;
    private float rating;
    private String experience;

    public Doctor(String name, String speciality, float rating, String experience) {
        this.name = name;
        this.speciality = speciality;
        this.rating = rating;
        this.experience = experience;
    }

    // Getters
    public String getName() { return name; }
    public String getSpeciality() { return speciality; }
    public float getRating() { return rating; }
    public String getExperience() { return experience; }
}