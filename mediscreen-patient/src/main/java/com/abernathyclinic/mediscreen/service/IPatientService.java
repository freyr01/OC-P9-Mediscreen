package com.abernathyclinic.mediscreen.service;

import java.util.List;

import com.abernathyclinic.mediscreen.exception.AlreadyExistsPatientException;
import com.abernathyclinic.mediscreen.exception.NoSuchPatientException;
import com.abernathyclinic.mediscreen.model.Patient;

public interface IPatientService {
	
	public Patient create(Patient patient) throws AlreadyExistsPatientException;
	public Patient read(Integer id) throws NoSuchPatientException;
	public Patient update(Patient patient) throws AlreadyExistsPatientException;
	public void delete(String id) throws NoSuchPatientException;
	public List<Patient> getAllPatient();
	public Patient getByLastName(String lastname) throws NoSuchPatientException ;

}
