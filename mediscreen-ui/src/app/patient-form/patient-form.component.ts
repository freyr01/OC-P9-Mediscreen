import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PatientService} from "../services/patient.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {Observable} from "rxjs";

@Component({
  selector: 'app-patient-form',
  templateUrl: './patient-form.component.html',
  styleUrls: ['./patient-form.component.css']
})
export class PatientFormComponent implements OnInit {

  error: any;
  showError: boolean = false;
  patientForm!: FormGroup;
  id!: string;
  isUpdateMode!: boolean;

  constructor(private patientService: PatientService,
              private router: Router,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.id = this.route.snapshot.params['id'];
    this.isUpdateMode = !!this.id;

    this.patientForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.maxLength(20), Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.maxLength(20), Validators.minLength(2)]],
      sex: ['', [Validators.required]],
      dateOfBirth: ['', [Validators.required]],
      address: ['', [Validators.required, Validators.maxLength(100), Validators.minLength(5)]],
      city: '',
      phone: ['', [Validators.required, Validators.maxLength(20), Validators.minLength(2)]],});


    if(this.isUpdateMode) {
      let patient = this.patientService.getById(this.id);
      if(patient != undefined) {
        this.patientForm.patchValue(patient);
      }
    }

  }

  onSubmit() {
    let method!: Observable<string>;
    if( ! this.isUpdateMode) {
      method = this.patientService.createPatient(this.patientForm?.value);
    } else {
      method = this.patientService.editPatient(this.id, this.patientForm?.value);
    }
    method.subscribe((patientCreated) => {
      this.router.navigate(['/patient']);
    }, (error) =>
      {
        if(error instanceof HttpErrorResponse) {
          if(error.status === 400) {
            this.error = 'Patient with this full name already exist';
            this.showError = true;
          }

        }
      });
  }
}
