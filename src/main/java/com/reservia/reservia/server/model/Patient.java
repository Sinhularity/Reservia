package com.reservia.reservia.server.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "patient")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id",
            nullable = false,
            unique = true
    )
    private int patientId;

    @Column(name = "first_name",
            nullable = false
    )
    private String firstName;

    @Column(name = "middle_name",
            nullable = false
    )
    private String middleName;

    @Column(name = "last_name",
            nullable = false
    )
    private  String lastName;

    @Column(name = "curp",
            nullable = false
    )
    private  String curp;

    @Column(name = "phone",
            nullable = false
    )
    private  String phone;

    @Column(name = "email",
            nullable = false
    )
    private  String email;

    public Patient() {
    }

    public Patient( String firstName, String middleName, String lastName, String curp, String phone, String email) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.curp = curp;
        this.phone = phone;
        this.email = email;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
