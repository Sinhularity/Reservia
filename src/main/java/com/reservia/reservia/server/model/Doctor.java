package com.reservia.reservia.server.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "doctor")
public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id",
            nullable = false,
            unique = true)
    private int doctorId;

    @Column(name = "first_name",
            nullable = false)
    private String firstName;
    @Column(name = "last_name",
            nullable = false)
    private String lastName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "license_number",
            nullable = false,
            unique = true)
    private String licenseNumber;
    @Column(name = "specialty",
            nullable = false)
    private String specialty;
    @Column(name = "email",
            nullable = false,
            unique = true)
    private String email;

    public Doctor() {
    }

    public Doctor( String firstName, String lastName, String middleName
            , String licenseNumber, String specialty, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.licenseNumber = licenseNumber;
        this.specialty = specialty;
        this.email = email;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
