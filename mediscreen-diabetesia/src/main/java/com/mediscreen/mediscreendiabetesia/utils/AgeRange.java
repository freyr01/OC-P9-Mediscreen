package com.mediscreen.mediscreendiabetesia.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/**
 * Used to represent an age range
 * 8 juil. 2021
 */
public class AgeRange {
	private int start;
	private int end;	
	
	@Override
	public String toString() {
		return start + ", " + end;
	}
	
}
