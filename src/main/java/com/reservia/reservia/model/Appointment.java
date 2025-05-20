package com.reservia.reservia.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id",
            nullable = false,
            unique = true)
    private int appointmentId;

    @Column(name = "date",
            nullable = false)
    private LocalDate date;

    @Column(name = "time",
            nullable = false)
    private String time;

    @Column(name = "reason",
            nullable = false)
    private String reason;

    @Column(name = "doctor_id",
            nullable = false)
    private int doctorId;
    @Column(name = "patient_id",
            nullable = false)
    private int patientId;

    public Appointment() {
    }

    public Appointment( LocalDate date, String time, String reason, int doctorId, int patientId) {
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
}
