package com.abernathyclinic.mediscreennote.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abernathyclinic.mediscreennote.exception.NoSuchNoteException;
import com.abernathyclinic.mediscreennote.model.NoteModel;
import com.abernathyclinic.mediscreennote.service.INoteService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "http://localhost:4200") //Allow HTTP request from angular
public class NoteController {
	
	private static Logger log = LoggerFactory.getLogger(NoteController.class);
	public final static String CRUD_ENDPOINT_NAME = "notes";
	private INoteService noteService;

	@Autowired
	public NoteController(INoteService p_noteService){
		noteService = p_noteService;
	}
	
	//CRUD
	//POST
	@ApiOperation(value = "Add a note")
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Constraint error, author must be 20 chars max")})
	@PostMapping(value = CRUD_ENDPOINT_NAME)
	public ResponseEntity<NoteModel> addNote(@Valid @RequestBody NoteModel note) {
    	log.info("POST Request to {} with body: {}",CRUD_ENDPOINT_NAME, note);

    	NoteModel noteCreated = noteService.create(note);
		if(noteCreated != null) {
			return new ResponseEntity<NoteModel>(noteCreated, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<NoteModel>(HttpStatus.BAD_REQUEST);
		}
	}
	
	//GET ALL
	@ApiOperation(value = "Get all notes of a patient, return empty list if no notes was found")
	
	@GetMapping(value = CRUD_ENDPOINT_NAME + "/patient/{pid}")
	public ResponseEntity<List<NoteModel>> listNote(@PathVariable("pid") int pid) {
		log.info("Get Request to {}", CRUD_ENDPOINT_NAME + "/pid");
		List<NoteModel> listNote = null;
		try {
			listNote = noteService.getByPatientIdOrderedDesc(pid);
		} catch (NoSuchNoteException e) {
			log.error(e.getMessage());
			return new ResponseEntity<List<NoteModel>>(listNote = new ArrayList<NoteModel>(), HttpStatus.OK);
		}
		return new ResponseEntity<List<NoteModel>>(listNote, HttpStatus.OK);
	}

	//READ
	@ApiOperation(value = "Get a note by id")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "No note found with this id") })
	@GetMapping(value = CRUD_ENDPOINT_NAME + "/{id}")
	public ResponseEntity<NoteModel> getNote(@PathVariable("id") String id) {
		log.info("GET request to {} with id: {}", CRUD_ENDPOINT_NAME, id);
	
		try {
			return new ResponseEntity<NoteModel>(noteService.getById(id), HttpStatus.OK);
		} catch (NoSuchNoteException e) {
			log.error(e.getMessage());
			return new ResponseEntity<NoteModel>(HttpStatus.NOT_FOUND);
		}

	}
	
	//UPDATE
	@ApiOperation(value = "Update an existing note based on id or create it if not exists")
	
	@PutMapping(value = CRUD_ENDPOINT_NAME)
	public ResponseEntity<NoteModel> updateNote(@Valid @RequestBody NoteModel note)  {

    	log.info("PUT Request to {} with value: {}",CRUD_ENDPOINT_NAME, note);
 	
    	NoteModel noteUpdated = noteService.put(note);
		if(noteUpdated != null) {
			return new ResponseEntity<NoteModel>(noteUpdated, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<NoteModel>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//DELETE
	@ApiOperation(value = "Delete a note")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "No note found with this id") })
	
	@DeleteMapping(value = CRUD_ENDPOINT_NAME + "/{id}")
	public ResponseEntity<Boolean> deleteNote(@PathVariable("id") String id)  {

    	log.info("DELETE Request to: {}",CRUD_ENDPOINT_NAME + "/"+id);

		try {
			noteService.delete(id);
		} catch (NoSuchNoteException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
}
