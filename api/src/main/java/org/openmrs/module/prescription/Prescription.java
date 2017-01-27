package org.openmrs.module.prescription;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.openmrs.BaseOpenmrsObject;

import java.util.Date;

@Entity(name = "prescription")
@Table(name = "prescription")
public class Prescription extends BaseOpenmrsObject {
	
	@Id
	@GeneratedValue
	@Column(name = "prescription_id")
	private Integer id;
	
	@Basic
	@Column(name = "description", length = 255)
	private String description;
	
	@Basic
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Basic
	@Column(name = "drug_id")
	private Integer drugId;
	
	@Basic
	@Column(name = "dose", length = 255)
	private String dose;
	
	@Basic
	@Column(name = "advice", length = 255)
	private String advice;
	
	@Basic
	@Column(name = "prescription_file", length = 255)
	private String prescriptionFile;
	
	@Column(name = "prescription_date_created")
	private Date prescriptionDateCreated;
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUuid() {
		return super.getUuid();
	}
	
	public void setUuid(String uuid) {
		super.setUuid(uuid);
	}
	
	public Integer getPatientId() {
		return this.patientId;
	}
	
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	public Integer getDrugId() {
		return this.drugId;
	}
	
	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDose() {
		return this.dose;
	}
	
	public void setDose(String dose) {
		this.dose = dose;
	}
	
	public String getAdvice() {
		return this.advice;
	}
	
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	
	public String getPrescriptionFile() {
		return this.prescriptionFile;
	}
	
	public void setPrescriptionFile(String prescriptionFile) {
		this.prescriptionFile = prescriptionFile;
	}
	
	public Date getPrescriptionDateCreated() {
		return prescriptionDateCreated;
	}
	
	public void setPrescriptionDateCreated(Date prescriptionDateCreated) {
		this.prescriptionDateCreated = prescriptionDateCreated;
	}
	
}
