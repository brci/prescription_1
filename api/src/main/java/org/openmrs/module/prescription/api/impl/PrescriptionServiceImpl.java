package org.openmrs.module.prescription.api.impl;

import java.util.List;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.UserService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.prescription.Prescription;
import org.openmrs.module.prescription.api.PrescriptionService;
import org.openmrs.module.prescription.api.dao.PrescriptionDao;
import org.springframework.transaction.annotation.Transactional;

import org.openmrs.module.prescription.Drug;

import java.io.FileOutputStream;
import java.io.File;
//import com.itextpdf.text.*;

import org.openmrs.module.prescription.api.print.PdfTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import java.util.HashMap;

public class PrescriptionServiceImpl extends BaseOpenmrsService implements PrescriptionService {
	
	PrescriptionDao dao;
	
	UserService userService;
	
	//String druglistFilename = "druglist_20161107.csv";
	
	CSVReader csvReader = new CSVReader();
	
	private List<Drug> druglist = null;
	
	private HashMap<Integer, String> druglistMap = null;
	
	private Properties properties = null;
	
	public void setDao(PrescriptionDao dao) {
		this.dao = dao;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public Prescription getItemByUuid(String uuid) throws APIException {
		return this.dao.getItemByUuid(uuid);
	}
	
	public Prescription getItemById(Integer pid) throws APIException {
		return this.dao.getItemById(pid);
	}
	
	public Prescription saveItem(Prescription item) throws APIException {
		return this.dao.saveItem(item);
	}
	
	public boolean deleteItem(Prescription item) throws APIException {
		return this.dao.deleteItem(item);
	}
	
	public boolean deleteItemFile(Prescription item) throws APIException {
		String fileName = item.getPrescriptionFile();
		
		try {
			if (!dao.getPrescriptionFileInUse(fileName)) {
				
				if (properties == null) {
					PropertiesReader propertiesReader = new PropertiesReader();
					properties = propertiesReader.readProperties();
				}
				File file = new File(properties.getProperty("prescription_file_path") + fileName + ".pdf");
				
				file.delete();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Transactional(readOnly = true)
	public List<Prescription> getAllPrescriptions(Patient patient) throws APIException, IllegalArgumentException {
		if (patient == null) {
			throw new IllegalArgumentException("An existing (NOT NULL) patient is required to get allergies");
		}
		
		return this.dao.getAllPrescriptions(patient);
	}
	
	@Transactional(readOnly = true)
	public HashMap<Integer, Prescription> getAllPrescriptionsMap(Patient patient) throws APIException,
	        IllegalArgumentException {
		
		List<Prescription> pList = getAllPrescriptions(patient);
		
		if (pList.size() == 0)
			return null;
		
		HashMap<Integer, Prescription> prescriptionlistMap = new HashMap<Integer, Prescription>();
		
		for (Prescription prescription : pList)
			prescriptionlistMap.put(prescription.getId(), prescription);
		
		return prescriptionlistMap;
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllPrescriptionsGrouped(Patient patient) throws APIException, IllegalArgumentException {
		if (patient == null) {
			throw new IllegalArgumentException("An existing (NOT NULL) patient is required to get allergies");
		}
		
		return this.dao.getAllPrescriptionsGrouped(patient);
	}
	
	public boolean printPrescriptions(List<Prescription> prescriptions, String filename, String patientName) {
		
		if (properties == null) {
			PropertiesReader propertiesReader = new PropertiesReader();
			
			properties = propertiesReader.readProperties();
		}
		
		System.out.println("Print service: " + prescriptions.size());
		
		PdfTemplate pt = new PdfTemplate();
		pt.writeOutPdf(prescriptions, properties.getProperty("prescription_file_path") + filename + ".pdf",
		    properties.getProperty("prescription_address_1"), properties.getProperty("prescription_address_2"),
		    properties.getProperty("prescription_address_3"), patientName);
		
		return true;
	}
	
	public List<Drug> getAllDrugs() {
		if (druglist == null) {
			
			if (properties == null) {
				PropertiesReader propertiesReader = new PropertiesReader();
				
				properties = propertiesReader.readProperties();
				System.out.println(properties.getProperty("druglist_file"));
			}
			druglist = csvReader.read(properties.getProperty("druglist_file"),
			    Integer.parseInt(properties.getProperty("druglist_file_col_id")),
			    Integer.parseInt(properties.getProperty("druglist_file_col_txt")));
			
			druglistMap = new HashMap<Integer, String>();
			
			for (Drug drug : druglist)
				druglistMap.put(drug.getId(), drug.getDrugConcatenated());
			
		}
		return druglist;
	}
	
	//probably not needed for approx 1000 entries
	/*
	public List<Drug> getAllDrugsExceptOne(Integer id) {
		//if(druglist==null)
		druglist = drugRetriever.getDrugListExceptOne(id);
		return druglist;
	}
	
	*/
	public String getDrugDescriptionById(Integer id) {
		return (String) druglistMap.get(id);
	}
	
	public String getPrescriptionsFolder() {
		
		if (properties == null) {
			PropertiesReader propertiesReader = new PropertiesReader();
			properties = propertiesReader.readProperties();
		}
		
		return (properties.getProperty("prescription_file_path"));
	}
	
}
