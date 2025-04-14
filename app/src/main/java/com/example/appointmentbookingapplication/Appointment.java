package com.example.appointmentbookingapplication;


public class Appointment {
    private final String patientName;
    private final String doctorName;
    private final String appointmentDate;
    private final String appointmentTime;

    public Appointment(String patientName, String doctorName, String appointmentDate, String appointmentTime) {
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }
}
