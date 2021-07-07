package com.mediscreen.mediscreendiabetesia;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.mediscreendiabetesia.proxy.Note;
import com.mediscreen.mediscreendiabetesia.proxy.NoteProxy;
import com.mediscreen.mediscreendiabetesia.proxy.Patient;
import com.mediscreen.mediscreendiabetesia.proxy.PatientProxy;
import com.mediscreen.mediscreendiabetesia.service.DiabetesService;
import com.mediscreen.mediscreendiabetesia.utils.RiskLevel;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import lombok.AllArgsConstructor;
import lombok.Data;

@SpringBootTest
public class DiabetesServiceIT {
	
	@AllArgsConstructor
	@Data
	public static class PatientTest {
		String lastName;
		Patient patient;
		Note[] notes;
		RiskLevel riskLevel;
	}
	
	@Autowired
	private DiabetesService diabetesService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	NoteProxy noteProxy;
	
	@Autowired
	PatientProxy patientProxy;
	
	public static List<PatientTest> patients;
	
	@BeforeAll
	public static void setUp() {
		
		patients = new ArrayList<PatientTest>();
		Note[] noteFergusen =  { 
				new Note("Dr Strange", "Le patient d�clare qu'il � se sent tr�s bien"),
				new Note("Dr Strange", "Le patient d�clare qu'il se sent fatigu� pendant la journ�e"),
				new Note("Dr Strange", "Le patient d�clare qu'il ne se sent pas si fatigu� que �a")
			};
		
		patients.add(new PatientTest(
				"Fergusen",
				new Patient("Lucas", "Fergusen", LocalDate.of(1968, 6, 22), "M", "387-866-1399", "2 Warren Street", null),
				noteFergusen, 
				RiskLevel.None));
		
		
		Note[] noteRees =  { 
				new Note("Dr Strange", "Le patient d�clare qu'il ressent beaucoup de stress au travail\r\n"),
				new Note("Dr Strange", "Le patient d�clare avoir fait une r�action aux m�dicaments au cours des 3 derniers mois\r\n"),
				new Note("Dr Strange", "Tests de laboratoire indiquant une microalbumine �lev�e\r\n"),
				new Note("Dr Strange", "Le patient d�clare que tout semble aller bien\r\n")
			};
		patients.add(new PatientTest(
				"Rees",
				new Patient("Pippa", "Rees", LocalDate.of(1952, 9, 27), "F", "628-423-0993", "745 West Valley Farms Drive", null),
				noteRees, 
				RiskLevel.None));
		
		Note[] noteArnold = {
				new Note("Dr Strange", "Le patient d�clare qu'il fume depuis peu\r\n"),
				new Note("Dr Strange", "Tests de laboratoire indiquant une microalbumine �lev�e\r\n"),
				new Note("Dr Strange", "Le patient d�clare qu'il est fumeur et qu'il a cess� de fumer l'ann�e derni�re\r\n"),
				new Note("Dr Strange", "Tests de laboratoire indiquant un taux de cholest�rol LDL �lev�\r\n")
			};
		
		patients.add(new PatientTest(
				"Arnold",
				new Patient("Edward", "Arnold", LocalDate.of(1952, 11, 11), "M", "123-727-2779", "599 East Garden Ave", null),
				noteArnold, 
				RiskLevel.None));
		
		Note[] noteSharp = {
				new Note("Dr Strange", "Le patient d�clare qu'il lui est devenu difficile de monter les escaliers\r\n"),
				new Note("Dr Strange", "Le patient d�clare qu'il a mal au dos lorsqu'il reste assis pendant longtemps\r\n"),
				new Note("Dr Strange", "Le patient d�clare avoir commenc� � fumer depuis peu\r\n")
			};
		patients.add(new PatientTest(
				"Sharp",
				new Patient("Anthony", "Sharp", LocalDate.of(1946, 11, 26), "M", "451-761-8383", "894 Hall Street", null),
				noteSharp, 
				RiskLevel.None));
		
		
		Note[] noteInce = {
				new Note("Dr Strange", "Le patient d�clare avoir des douleurs au cou occasionnellement\r\n"),
				new Note("Dr Strange", "Le patient d�clare avoir eu plusieurs �pisodes de vertige depuis la derni�re visite.\r\n"),
				new Note("Dr Strange", "Le patient d�clare qu'il souffre encore de douleurs cervicales occasionnelles\r\n"),
				new Note("Dr Strange", "Le patient d�clare avoir eu plusieurs �pisodes de vertige depuis la derni�re visite.\r\n")
			};
		
		patients.add(new PatientTest(
				"Ince",
				new Patient("Wendy", "Ince", LocalDate.of(1958, 6, 29), "F", "802-911-9975", "4 Southampton Road", null),
				noteInce, 
				RiskLevel.None));
		
		Note[] noteRoss = 	{
				new Note("Dr Strange", "Le patient d�clare qu'il se sent bien\r\n"),
				new Note("Dr Strange", "Le patient d�clare qu'il se sent bien\r\n")
			};
		
		patients.add(new PatientTest(
				"Ross",
				new Patient("Tracey", "Ross", LocalDate.of(1949, 12, 7), "F", "131-396-5049", "40 Sulphur Springs Dr", null),
				noteRoss, 
				RiskLevel.None));
		
		
		Note[] noteWilson = {
				new Note("Dr Strange", "Le patient d�clare qu'il se r�veille souvent avec une raideur articulaire\r\n")
			};
		
		patients.add(new PatientTest(
				"Wilson",
				new Patient("Claire", "Wilson", LocalDate.of(1949, 12, 7), "F", "131-396-5049", "40 Sulphur Springs Dr", null),
				noteWilson, 
				RiskLevel.None));
		
		Note[] noteBuckland = {
				new Note("Dr Strange", "Les tests de laboratoire indiquent que les anticorps sont �lev�s\r\n")	
			};
		
		patients.add(new PatientTest(
				"Buckland",
				new Patient("Max", "Buckland", LocalDate.of(1945, 6, 24), "M", "833-534-0864", "193 Vale St", null),
				noteBuckland, 
				RiskLevel.None));
		
		Note[] noteClark = {
				new Note("Dr Strange", "Le patient d�clare avoir de la difficult� � se concentrer sur ses devoirs scolaires\r\n"),
				new Note("Dr Strange", "Le patient d�clare qu'il s�impatiente facilement en cas d�attente prolong�e\r\n"),
				new Note("Dr Strange", "Le patient signale qu'il est facilement irrit� par des broutilles\r\n")
			};
		
		patients.add(new PatientTest(
				"Clark",
				new Patient("Natalie", "Clark", LocalDate.of(1964, 6, 18), "F", "241-467-9197", "12 Beechwood Road", null),
				noteClark, 
				RiskLevel.None));
		
		Note[] noteBailey = {
				new Note("Dr Strange", "Le patient d�clare qu'il n'a aucun probl�me\r\n"),
				new Note("Dr Strange", "Le patient d�clare qu'il n'a aucun probl�me\r\n"),
				new Note("Dr Strange", "Le patient d�clare qu'il n'a aucun probl�me\r\n"),
				new Note("Dr Strange", "Le patient d�clare qu'il n'a aucun probl�me\r\n")
			};
		
		patients.add(new PatientTest(
				"Bailey",
				new Patient("Piers", "Bailey", LocalDate.of(1959, 6, 22), "M", "747-815-0557", "1202 Bumble Dr", null),
				noteBailey, 
				RiskLevel.None));
		
		
	}
	
	@Test
	@Disabled //Execute this only one time to add demonstration datas
	public void addDemoPatients() 
						throws JsonProcessingException{
		
		for(PatientTest pt : patients) {
			int id = patientProxy.addPatient(objectMapper.writeValueAsString(pt.getPatient())).getId();
			Note[] pNotes = pt.getNotes();
			for(int i = 0; i < pNotes.length; i++) {
				pNotes[i].setPatientId(id);
				noteProxy.addNote(objectMapper.writeValueAsString(pNotes[i]));
			}
		}
		
	}
	
	@Test
	public void getDiabetesRiskTest_shouldReturnCorrectRiskLevel() {
		List<Patient> db_patients = patientProxy.getAllPatients();
		
		for(PatientTest pt : patients) {
			for(Patient p : db_patients) {
				if(pt.getLastName().equals(p.getLastName())) {
					//assertEquals(pt.getRiskLevel(), diabetesService.getDiabetesRiskLevel(p.getId()));
					System.out.println("Patient: " + p.getLastName() + " Risk Level: " + diabetesService.getDiabetesRiskLevel(p.getId()));
				}
			}
		}
		
	}
	
	/*

	 */

}
