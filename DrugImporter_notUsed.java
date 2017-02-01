package org.openmrs.module.prescription.api.impl;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
//import org.openmrs.Concept;
//import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
//import org.openmrs.module.emrapi.concept.EmrConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;
*/

import org.openmrs.module.prescription.Drug;

public class DrugImporter_notUsed {
	
	//private Map<String, String> dosageFormShortcuts = new HashMap<String, String>();
	
	/*
	public DrugImporter() {
	    dosageFormShortcuts.put("Tablet", "SNOMED CT:385055001");
	    dosageFormShortcuts.put("Capsule", "SNOMED CT:428641000");
	}


	private CellProcessor[] getCellProcessors() {
	    return new CellProcessor[] {
	            new Optional(new Trim()),               // uuid
	             new Trim(),                 // Name
	             new Optional(new Trim())    // Dosage Form
	    };
	}
	*/
	
	/**
	 * For unit tests -- normally this is autowired
	 * 
	 * @param conceptService
	 */
	
	//public List<Drug> importSpreadsheet(Reader csvFileReader) throws IOException {
	
	public List<Drug> importSpreadsheet() {
		
		//List<DrugImporterRow> drugList = readSpreadsheet(csvFileReader);
		
		List<Drug> drugs = new ArrayList<Drug>();
		
		List<DrugImporterRow> drugList = createDruglist();
		
		for (DrugImporterRow row : drugList) {
			
			Drug drug = null;
			
			/*
			// first, see if there is an existing drug with this uuid
			if (row.getUuid() != null) {
			    drug = conceptService.getDrugByUuid(row.getUuid());
			}

			// if not, see if there is an existing drug with this name
			if (drug == null) {
			    drug = conceptService.getDrug(row.getProductName());
			}
			*/
			
			// if not, we are creating a new drug
			if (drug == null) {
				drug = new Drug();
			}
			
			// now create/update
			
			// set uuid
			if (row.getUuid() != null) {
				drug.setUuid(row.getUuid());
			}
			
			// set name
			//drug.setName(row.getProductName());
			drug.setProductName(row.getProductName());
			
			// set dosage form
			//if (StringUtils.isNotEmpty(row.getDosageForm())) {
			if (row.getDosageForm() != null)
				drug.setDosageForm(row.getDosageForm());
			
			// note that we currently don't store the inventory code anywhere!
			
			//conceptService.saveDrug(drug);
			
			drugs.add(drug);
		}
		return drugs;
	}
	
	/*
	private List<DrugImporterRow> readSpreadsheet(Reader csvFileReader) throws IOException  {

	    List<DrugImporterRow> drugList = new ArrayList<DrugImporterRow>();

	    CsvBeanReader csv = null;
	    try {
	        csv = new CsvBeanReader(csvFileReader, CsvPreference.EXCEL_PREFERENCE);
	        csv.getHeader(true);
	        CellProcessor[] cellProcessors = getCellProcessors();

	        while (true) {
	            DrugImporterRow row = csv.read(DrugImporterRow.class, DrugImporterRow.FIELD_COLUMNS, cellProcessors);
	            if (row == null) {
	                break;
	            }

	            drugList.add(row);
	        }

	    } finally {
	        if (csv != null) {
	            csv.close();
	        }
	    }

	    return drugList;
	}
	*/
	
	public List<DrugImporterRow> createDruglist() {
		
		List<DrugImporterRow> drugList = new ArrayList<DrugImporterRow>();
		
		DrugImporterRow amitriptylineHydrochloride = new DrugImporterRow();
		amitriptylineHydrochloride.setProductName("Amitriptyline hydrochloride");
		amitriptylineHydrochloride.setUuid("63e31e90-329d-11e3-aa6e-0800200c9a66");
		amitriptylineHydrochloride.setDosageForm("25 mg, coated tablet");
		drugList.add(amitriptylineHydrochloride);
		
		DrugImporterRow amlodipineBesylate = new DrugImporterRow();
		amlodipineBesylate.setProductName("Amlodipine besylate");
		amlodipineBesylate.setUuid("6ca5db30-329d-11e3-aa6e-0800200c9a67");
		amlodipineBesylate.setDosageForm("5 mg, tablet");
		drugList.add(amlodipineBesylate);
		
		DrugImporterRow amoxicillin1 = new DrugImporterRow();
		amoxicillin1.setProductName("Amoxicillin");
		amoxicillin1.setUuid("6ca5db30-329d-11e3-aa6e-0800200c9a68");
		amoxicillin1.setDosageForm("100 mL bottle");
		drugList.add(amoxicillin1);
		
		return drugList;
	}
	
}
