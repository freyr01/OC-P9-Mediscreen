package com.mediscreen.mediscreendiabetesia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.mediscreen.mediscreendiabetesia.dto.PatientAssessDto;
import com.mediscreen.mediscreendiabetesia.exception.NoSuchPatientException;
import com.mediscreen.mediscreendiabetesia.model.PatientTestModel;
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
	
	@Autowired
	private DiabetesService diabetesService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	NoteProxy noteProxy;
	
	@Autowired
	PatientProxy patientProxy;
	
	public static List<PatientTestModel> patients;
	
	@BeforeAll
	public static void setUp() {
		
		patients = new ArrayList<PatientTestModel>();
		Note[] noteFergusen =  { 
				new Note("Dr Strange", "Le patient d�clare qu'il � se sent tr�s bien �\r\n"
						+ "Poids �gal ou inf�rieur au poids recommand�"),
				new Note("Dr Strange", "Le patient d�clare qu'il se sent fatigu� pendant la journ�e\r\n"
						+ "Il se plaint �galement de douleurs musculaires\r\n"
						+ "Tests de laboratoire indiquant une microalbumine �lev�e"),
				new Note("Dr Strange", "Le patient d�clare qu'il ne se sent pas si fatigu� que �a\r\n"
						+ "Fumeur, il a arr�t� dans les 12 mois pr�c�dents\r\n"
						+ "Tests de laboratoire indiquant que les anticorps sont �lev�s")
			};
		
		patients.add(new PatientTestModel(
				"Fergusen",
				new Patient("Lucas", "Fergusen", LocalDate.of(1968, 6, 22), "M", "387-866-1399", "2 Warren Street", null),
				noteFergusen, 
				RiskLevel.InDanger));
		
		
		Note[] noteRees =  { 
				new Note("Dr Strange", "Le patient d�clare qu'il ressent beaucoup de stress au travail\r\n"
						+ "Il se plaint �galement que son audition est anormale derni�rement"),
				new Note("Dr Strange", "Le patient d�clare avoir fait une r�action aux m�dicaments au cours des 3 derniers mois\r\n"
						+ "Il remarque �galement que son audition continue d'�tre anormale"),
				new Note("Dr Strange", "Tests de laboratoire indiquant une microalbumine �lev�e\r\n"),
				new Note("Dr Strange", "Le patient d�clare que tout semble aller bien\r\n"
						+ "Le laboratoire rapporte que l'h�moglobine A1C d�passe le niveau recommand�\r\n"
						+ "Le patient d�clare qu�il fume depuis longtemps")
			};
		patients.add(new PatientTestModel(
				"Rees",
				new Patient("Pippa", "Rees", LocalDate.of(1952, 9, 27), "F", "628-423-0993", "745 West Valley Farms Drive", null),
				noteRees, 
				RiskLevel.InDanger));
		
		Note[] noteArnold = {
				new Note("Dr Strange", "Le patient d�clare qu'il fume depuis peu\r\n"),
				new Note("Dr Strange", "Tests de laboratoire indiquant une microalbumine �lev�e\r\n"),
				new Note("Dr Strange", "Le patient d�clare qu'il est fumeur et qu'il a cess� de fumer l'ann�e derni�re\r\n"
						+ "Il se plaint �galement de crises d�apn�e respiratoire anormales\r\n"
						+ "Tests de laboratoire indiquant un taux de cholest�rol LDL �lev�"),
				new Note("Dr Strange", "Tests de laboratoire indiquant un taux de cholest�rol LDL �lev�\r\n")
			};
		
		patients.add(new PatientTestModel(
				"Arnold",
				new Patient("Edward", "Arnold", LocalDate.of(1952, 11, 11), "M", "123-727-2779", "599 East Garden Ave", null),
				noteArnold, 
				RiskLevel.InDanger));
		
		Note[] noteSharp = {
				new Note("Dr Strange", "Le patient d�clare qu'il lui est devenu difficile de monter les escaliers\r\n"
						+ "Il se plaint �galement d��tre essouffl�\r\n"
						+ "Tests de laboratoire indiquant que les anticorps sont �lev�s\r\n"
						+ "R�action aux m�dicaments"),
				new Note("Dr Strange", "Le patient d�clare qu'il a mal au dos lorsqu'il reste assis pendant longtemps\r\n"),
				new Note("Dr Strange", "Le patient d�clare avoir commenc� � fumer depuis peu\r\n"
						+ "H�moglobine A1C sup�rieure au niveau recommand�")
			};
		patients.add(new PatientTestModel(
				"Sharp",
				new Patient("Anthony", "Sharp", LocalDate.of(1946, 11, 26), "M", "451-761-8383", "894 Hall Street", null),
				noteSharp, 
				RiskLevel.Borderline));
		
		
		Note[] noteInce = {
				new Note("Dr Strange", "Le patient d�clare avoir des douleurs au cou occasionnellement\r\n"
						+ "Le patient remarque �galement que certains aliments ont un go�t diff�rent\r\n"
						+ "R�action apparente aux m�dicaments\r\n"
						+ "Poids corporel sup�rieur au poids recommand�"),
				new Note("Dr Strange", "Le patient d�clare avoir eu plusieurs �pisodes de vertige depuis la derni�re visite.\r\n"
						+ "Taille incluse dans la fourchette concern�e"),
				new Note("Dr Strange", "Le patient d�clare qu'il souffre encore de douleurs cervicales occasionnelles\r\n"
						+ "Tests de laboratoire indiquant une microalbumine �lev�e\r\n"
						+ "Fumeur, il a arr�t� dans les 12 mois pr�c�dents"),
				new Note("Dr Strange", "Le patient d�clare avoir eu plusieurs �pisodes de vertige depuis la derni�re visite.\r\n"
						+ "Tests de laboratoire indiquant que les anticorps sont �lev�s\r\n"
						+ "")
			};
		
		patients.add(new PatientTestModel(
				"Ince",
				new Patient("Wendy", "Ince", LocalDate.of(1958, 6, 29), "F", "802-911-9975", "4 Southampton Road", null),
				noteInce, 
				RiskLevel.EarlyOnset));
		
		Note[] noteRoss = 	{
				new Note("Dr Strange", "Le patient d�clare qu'il se sent bien\r\n"
						+ "Poids corporel sup�rieur au poids recommand�"),
				new Note("Dr Strange", "Le patient d�clare qu'il se sent bien\r\n")
			};
		
		patients.add(new PatientTestModel(
				"Ross",
				new Patient("Tracey", "Ross", LocalDate.of(1949, 12, 7), "F", "131-396-5049", "40 Sulphur Springs Dr", null),
				noteRoss, 
				RiskLevel.None));
		
		
		Note[] noteWilson = {
				new Note("Dr Strange", "Le patient d�clare qu'il se r�veille souvent avec une raideur articulaire\r\n"
						+ "Il se plaint �galement de difficult�s pour s�endormir\r\n"
						+ "Poids corporel sup�rieur au poids recommand�\r\n"
						+ "Tests de laboratoire indiquant un taux de cholest�rol LDL �lev�")
			};
		
		patients.add(new PatientTestModel(
				"Wilson",
				new Patient("Claire", "Wilson", LocalDate.of(1949, 12, 7), "F", "131-396-5049", "40 Sulphur Springs Dr", null),
				noteWilson, 
				RiskLevel.Borderline));
		
		Note[] noteBuckland = {
				new Note("Dr Strange", "Les tests de laboratoire indiquent que les anticorps sont �lev�s\r\n"
						+ "H�moglobine A1C sup�rieure au niveau recommand�")	
			};
		
		patients.add(new PatientTestModel(
				"Buckland",
				new Patient("Max", "Buckland", LocalDate.of(1945, 6, 24), "M", "833-534-0864", "193 Vale St", null),
				noteBuckland, 
				RiskLevel.Borderline));
		
		Note[] noteClark = {
				new Note("Dr Strange", "Le patient d�clare avoir de la difficult� � se concentrer sur ses devoirs scolaires\r\n"
						+ "H�moglobine A1C sup�rieure au niveau recommand�"),
				new Note("Dr Strange", "Le patient d�clare qu'il s�impatiente facilement en cas d�attente prolong�e\r\n"
						+ "Il signale �galement que les produits du distributeur automatique ne sont pas bons\r\n"
						+ "Tests de laboratoire signalant des taux anormaux de cellules sanguines"),
				new Note("Dr Strange", "Le patient signale qu'il est facilement irrit� par des broutilles\r\n"
						+ "Il d�clare �galement que l'aspirateur des voisins fait trop de bruit\r\n"
						+ "Tests de laboratoire indiquant que les anticorps sont �lev�s")
			};
		
		patients.add(new PatientTestModel(
				"Clark",
				new Patient("Natalie", "Clark", LocalDate.of(1964, 6, 18), "F", "241-467-9197", "12 Beechwood Road", null),
				noteClark, 
				RiskLevel.Borderline));
		
		Note[] noteBailey = {
				new Note("Dr Strange", "Le patient d�clare qu'il n'a aucun probl�me\r\n"),
				new Note("Dr Strange", "Le patient d�clare qu'il n'a aucun probl�me\r\n"
						+ "Taille incluse dans la fourchette concern�e\r\n"
						+ "H�moglobine A1C sup�rieure au niveau recommand�"),
				new Note("Dr Strange", "Le patient d�clare qu'il n'a aucun probl�me\r\n"
						+ "Poids corporel sup�rieur au poids recommand�\r\n"
						+ "Le patient a signal� plusieurs �pisodes de vertige depuis sa derni�re visite"),
				new Note("Dr Strange", "Le patient d�clare qu'il n'a aucun probl�me\r\n"
						+ "Tests de laboratoire indiquant une microalbumine �lev�e")
			};
		
		patients.add(new PatientTestModel(
				"Bailey",
				new Patient("Piers", "Bailey", LocalDate.of(1959, 6, 22), "M", "747-815-0557", "1202 Bumble Dr", null),
				noteBailey, 
				RiskLevel.InDanger));
		
		
	}
	
	@Test
	@Disabled //Execute this only one time to add demonstration datas
	public void addDemoPatients() 
						throws JsonProcessingException{
		
		for(PatientTestModel pt : patients) {
			int id = patientProxy.addPatient(objectMapper.writeValueAsString(pt.getPatient())).getId();
			Note[] pNotes = pt.getNotes();
			for(int i = 0; i < pNotes.length; i++) {
				pNotes[i].setPatientId(id);
				noteProxy.addNote(objectMapper.writeValueAsString(pNotes[i]));
			}
		}
		
	}
	
	@Test
	public void getDiabetesRiskOfUnknownPatient_shouldThrowException() {
		assertThrows(NoSuchPatientException.class, () -> diabetesService.getPatientAssess(9999));
	}
	
	@Test
	public void getDiabetesRiskTest_shouldReturnCorrectlyFilledPatientAssessDto() throws NoSuchPatientException {
		List<Patient> db_patients = patientProxy.getAllPatients();
		
		for(PatientTestModel pt : patients) {
			for(Patient p : db_patients) {
				if(pt.getLastName().equals(p.getLastName())) {
					PatientAssessDto patientAssessDto = diabetesService.getPatientAssess(p.getId());
					assertEquals(pt.getPatient().getFirstName(), patientAssessDto.getFirstName());
					assertEquals(pt.getPatient().getLastName(), patientAssessDto.getLastName());
					assertEquals(pt.getRiskLevel(), patientAssessDto.getRiskLevel());
					assertTrue(patientAssessDto.getAge() >= 0);
				}
			}
		}
		
	}
}
