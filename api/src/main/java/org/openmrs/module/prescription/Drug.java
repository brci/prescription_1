package org.openmrs.module.prescription;

public class Drug {
	
	private int id;
	
	private String uuid;
	
	private String productName;
	
	private String dosageForm;
	
	private String drugConcatenated;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getDosageForm() {
		return dosageForm;
	}
	
	public void setDosageForm(String dosageForm) {
		this.dosageForm = dosageForm;
	}
	
	public String getDrugConcatenated() {
		return drugConcatenated;
	}
	
	// idea: to concatenate a string by e.g. drug name and dosage .. now
	// done via config: the druglist..csv has that concatenation complete
	public void setDrugConcatenated(String drugConcatenated) {
		this.drugConcatenated = drugConcatenated;
	}
}
