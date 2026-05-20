DROP TABLE IF EXISTS consultations CASCADE;
DROP TABLE IF EXISTS appointments CASCADE;
DROP TABLE IF EXISTS medical_records CASCADE;
DROP TABLE IF EXISTS doctors CASCADE;
DROP TABLE IF EXISTS patients CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50)
);

CREATE TABLE doctors (
    id SERIAL PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(20),
    office_hours TEXT,

    CONSTRAINT fk_doctor_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE patients (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    cnp VARCHAR(20) UNIQUE,
    gender VARCHAR(10),
    birth_date DATE,
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT
);

CREATE TABLE medical_records (
    id SERIAL PRIMARY KEY,
    patient_id INT UNIQUE NOT NULL,

    CONSTRAINT fk_record_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients(id)
        ON DELETE CASCADE
);

CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    date TIMESTAMP NOT NULL,
    status VARCHAR(30),

    CONSTRAINT fk_appointment_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctors(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients(id)
        ON DELETE CASCADE
);

CREATE TABLE consultations (
    id SERIAL PRIMARY KEY,
    medical_record_id INT NOT NULL,
    doctor_id INT NOT NULL,
    date TIMESTAMP NOT NULL,

    symptoms TEXT,
    diagnosis TEXT,
    treatment TEXT,
    observations TEXT,

    CONSTRAINT fk_consultation_record
        FOREIGN KEY (medical_record_id)
        REFERENCES medical_records(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_consultation_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctors(id)
        ON DELETE CASCADE
);