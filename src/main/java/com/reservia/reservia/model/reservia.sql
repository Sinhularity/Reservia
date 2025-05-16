CREATE DATABASE IF NOT EXISTS reservia;
USE reservia;

DROP TABLE IF EXISTS Doctor;
CREATE TABLE Doctor (
                  doctor_id INT AUTO_INCREMENT PRIMARY KEY,
                  first_name VARCHAR(100) NOT NULL,
                  last_name VARCHAR(100) NOT NULL,
                  middle_name VARCHAR(100) NOT NULL,
                  license_number VARCHAR(50) NOT NULL,
                  specialty VARCHAR(100) NOT NULL,
                  email VARCHAR(100) NOT NULL
              );

DROP TABLE IF EXISTS Patient;
CREATE TABLE Patient (
                  patient_id INT AUTO_INCREMENT PRIMARY KEY,
                  first_name VARCHAR(100) NOT NULL,
                  last_name VARCHAR(100) NOT NULL,
                  middle_name VARCHAR(100) NOT NULL,
                  curp VARCHAR(18) NOT NULL,
                  phone VARCHAR(20) NOT NULL,
                  email VARCHAR(100) NOT NULL
              );

DROP TABLE IF EXISTS Appointment;
CREATE TABLE Appointment (
                  appointment_id INT AUTO_INCREMENT PRIMARY KEY,
                  date DATE NOT NULL,
                  time TIME NOT NULL,
                  reason TEXT,
                  doctor_id INT,
                  patient_id INT
              );

ALTER TABLE Appointment ADD FOREIGN KEY fk_doctor (doctor_id) REFERENCES Doctor(doctor_id);
ALTER TABLE Appointment ADD FOREIGN KEY fk_patient (patient_id) REFERENCES Patient(patient_id);