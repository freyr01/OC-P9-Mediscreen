package com.abernathyclinic.mediscreen.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "patient")
@Entity
public class Patient {
	
	
	
	
	public Patient(@NotBlank @Length(max = 20, message = "max 20 chars") String firstName,
			@NotBlank @Length(max = 20, message = "max 20 chars") String lastName, @Past @NotNull LocalDate dateOfBirth,
			@NotBlank @Pattern(regexp = "M|F") String sex,
			@NotBlank @Length(max = 20, message = "max 20 chars") String phone,
			@NotBlank @Length(max = 100, message = "max 100 chars") String address,
			@Length(max = 20, message = "max 20 chars") String city) {
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.sex = sex;
		this.phone = phone;
		this.address = address;
		this.city = city;
	}

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank
	@Length(max = 20, message = "max 20 chars")
	@Column(name="first_name")
	private String firstName;
	
	@NotBlank
	@Length(max = 20, message = "max 20 chars")
	@Column(name="last_name")
	private String lastName;
	
	@Past
	@NotNull
	@Column(name="date_of_birth")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate dateOfBirth;
	
	@NotBlank
	@Pattern(regexp = "M|F")
	private String sex;
	
	@NotBlank
	@Length(max = 20, message = "max 20 chars")
	private String phone;
	
	@NotBlank
	@Length(max = 100, message = "max 100 chars")
	private String address;
	
	@Length(max = 20, message = "max 20 chars")
	private String city;
	
	//Special setters
	public void setFamily(String family) {
		lastName = family;
	}
	
	public void setGiven(String given) {
		firstName = given;
	}
	
	public void setDob(String dob) {
		LocalDate.parse(dob);
		dateOfBirth = LocalDate.parse(dob);
	}
	
	
}
