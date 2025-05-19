package com.reservia.reservia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "patient")
public class Patient {

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
    private String lastName;

    @Column(name = "curp",
            nullable = false
    )
    private String curp;

    @Column(name = "phone",
            nullable = false
    )
    private String phone;

    @Column(name = "email",
            nullable = false
    )
    private String email;

    public Patient() {
    }

    public Patient(int patientId, String firstName, String middleName, String lastName, String curp, String phone, String email) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.curp = curp;
        this.phone = phone;
        this.email = email;
    }
}
