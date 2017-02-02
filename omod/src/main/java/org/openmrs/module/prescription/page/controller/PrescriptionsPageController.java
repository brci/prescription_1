package org.openmrs.module.prescription.page.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Patient;
import org.openmrs.module.prescription.Prescription;
import org.openmrs.module.prescription.api.PrescriptionService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class PrescriptionsPageController {
	
	public void controller(@RequestParam("patientId") Patient patient,
	        @RequestParam(value = "returnUrl", required = false) String returnUrl,
	        @RequestParam(value = "action", required = false) String action,
	        @RequestParam(value = "prescriptionId", required = false) Integer prescriptionId, PageModel model,
	        @SpringBean("prescription.PrescriptionService") PrescriptionService service, UiUtils ui) {
		
		if (StringUtils.isBlank(returnUrl)) {
			returnUrl = ui.pageLink("coreapps", "clinicianfacing/patient",
			    Collections.singletonMap("patientId", (Object) patient.getId()));
		}
		
		if (StringUtils.isNotBlank(action)) {
			try {
				System.out.println("Log: controller action " + action);
				if ("remove".equals(action)) {
					Prescription prescription = service.getItemById(prescriptionId);
					service.deleteItem(prescription);
				}
			}
			catch (Exception e) {
				// session.setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				// "allergyui.message.fail");
				e.printStackTrace();
			}
		}
		
		model.addAttribute("prescriptions", service.getAllPrescriptions(patient));
		
		model.addAttribute("patient", patient);
		model.addAttribute("returnUrl", returnUrl);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // file:
		                                                           // name_yyyy-MM-dd_HHmm
		Calendar todayDate = Calendar.getInstance();
		String today = sdf.format(todayDate.getTime());
		model.addAttribute("today", today);
		// changes of prescription only the same day permitted !
		
	}
	
	public String post(@RequestParam("patientId") Patient patient,
	        @RequestParam(value = "action", required = false) String action,
	        @RequestParam(value = "prescriptionId", required = false) Integer prescriptionId,
	        @RequestParam(value = "returnUrl", required = false) String returnUrl,
	        @RequestParam(value = "printprescription", required = false) Integer[] pIds, PageModel model, UiUtils ui,
	        HttpSession session, @SpringBean("prescription.PrescriptionService") PrescriptionService service) {
		
		if (StringUtils.isNotBlank(action)) {
			try {
				System.out.println("Log: post action " + action);
				
				if ("print".equals(action)) {
					
					if (pIds != null) {
						
						if (service.getAllPrescriptions(patient) == null)
							;
						else {
							HashMap<Integer, Prescription> allPrescriptions = (HashMap<Integer, Prescription>) service
							        .getAllPrescriptionsMap(patient);
							
							List<Prescription> printPrescriptions = new ArrayList<Prescription>();
							
							DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmm");
							Calendar cal = Calendar.getInstance();
							String prescriptionFileName = dateFormat.format(cal.getTime()); // 2014/08/06
							                                                                // 16:00:22											
							for (int i = 0; i < pIds.length; i++) {
								System.out.println("" + pIds[i]);
								
								Prescription current = (Prescription) allPrescriptions.get(pIds[i]);
								printPrescriptions.add(current);
								
								current.setPrescriptionFile(prescriptionFileName);
								current.setPrescriptionDateCreated(cal.getTime());
								service.saveItem(current);
							}
						}
					}
				}
				
				System.out.println("Log: post after action");
				model.addAttribute("prescriptions", service.getAllPrescriptions(patient));
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // file:
				                                                           // name_yyyy-MM-dd_HHmm
				Calendar todayDate = Calendar.getInstance();
				String today = sdf.format(todayDate.getTime());
				model.addAttribute("today", today);
				
				return "redirect:prescription/prescriptions.page?patientId=" + patient.getPatientId() + "&returnUrl="
				        + ui.urlEncode(returnUrl);
				
			}
			catch (Exception e) {
				// session.setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				// "allergyui.message.fail");
				e.printStackTrace();
			}
		}
		
		model.addAttribute("returnUrl", returnUrl);
		
		return null;
	}
}
