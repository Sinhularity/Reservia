DROP DATABASE IF EXISTS reservia;
CREATE DATABASE IF NOT EXISTS reservia;
USE reservia;

DROP TABLE IF EXISTS doctor;
CREATE TABLE doctor (
                  doctor_id INT AUTO_INCREMENT PRIMARY KEY,
                  first_name VARCHAR(100) NOT NULL,
                  last_name VARCHAR(100) NOT NULL,
                  middle_name VARCHAR(100),
                  license_number VARCHAR(50) NOT NULL,
                  specialty VARCHAR(100) NOT NULL,
                  email VARCHAR(100) NOT NULL
              );

DROP TABLE IF EXISTS patient;
CREATE TABLE patient (
                  patient_id INT AUTO_INCREMENT PRIMARY KEY,
                  first_name VARCHAR(100) NOT NULL,
                  last_name VARCHAR(100) NOT NULL,
                  middle_name VARCHAR(100) NOT NULL,
                  curp VARCHAR(18) NOT NULL,
                  phone VARCHAR(20) NOT NULL,
                  email VARCHAR(100) NOT NULL
              );

DROP TABLE IF EXISTS appointment;
CREATE TABLE appointment (
                  appointment_id INT AUTO_INCREMENT PRIMARY KEY,
                  date DATE NOT NULL,
                  time VARCHAR(5) NOT NULL,
                  reason TEXT,
                  doctor_id INT,
                  patient_id INT
              );

ALTER TABLE appointment ADD FOREIGN KEY fk_doctor (doctor_id) REFERENCES doctor(doctor_id);
ALTER TABLE appointment ADD FOREIGN KEY fk_patient (patient_id) REFERENCES patient(patient_id);

INSERT INTO doctor (first_name, last_name, middle_name, license_number, specialty, email)
VALUES ('Juan', 'Pérez', 'Martínez', 'MED123456', 'Cardiología', 'juan.perez@hospital.com');

INSERT INTO doctor (first_name, last_name, middle_name, license_number, specialty, email)
VALUES ('Laura', 'Gómez', 'Ramírez', 'MED654321', 'Pediatría', 'laura.gomez@hospital.com');

INSERT INTO doctor (first_name, last_name, middle_name, license_number, specialty, email)
VALUES ('Carlos', 'Fernández', 'Lopez', 'MED789012', 'Dermatología', 'carlos.fernandez@hospital.com');
