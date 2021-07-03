package com.mediscreen.mediscreendiabetesia.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TriggerService {
	
	public List<String> getTriggers(){
		List<String> triggers = new ArrayList<String>();
		
		triggers.add("H�moglobine A1C");
		triggers.add("Microalbumine");
		triggers.add("Taille");
		triggers.add("Poids");
		triggers.add("Fumeur");
		triggers.add("Anormal");
		triggers.add("Cholest�rol");
		triggers.add("Vertige");
		triggers.add("Rechute");
		triggers.add("R�action");
		triggers.add("Anticorps");
		
		return triggers;
	}

}
