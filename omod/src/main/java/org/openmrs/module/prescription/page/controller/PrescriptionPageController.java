package org.openmrs.module.prescription.page.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Patient;
import org.openmrs.module.prescription.Drug;
import org.openmrs.module.prescription.Prescription;
import org.openmrs.module.prescription.api.PrescriptionService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
//import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
//import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

//import java.util.ArrayList;

public class PrescriptionPageController {

	public void controller(@RequestParam("patientId") Patient patient,
			@RequestParam(value = "prescriptionId", required = false) Integer prescriptionId,
			@RequestParam(value = "returnUrl", required = false) String returnUrl, PageModel model,
			@SpringBean("prescription.PrescriptionService") PrescriptionService service, UiUtils ui) {

		// model.addAttribute("prescriptions",
		// service.getAllPrescriptions(patient));

		if (StringUtils.isBlank(returnUrl)) {
			returnUrl = ui.pageLink("coreapps", "clinicianfacing/patient",
					Collections.singletonMap("patientId", (Object) patient.getId()));
		}

		List<Drug> drugList;

		if (prescriptionId != null) {
			Prescription prescription = service.getItemById(prescriptionId);
			model.addAttribute("prescription", prescription);
		}
		drugList = service.getAllDrugs();

		model.addAttribute("drugstoprescribe", drugList);

		model.addAttribute("patient", patient);
		model.addAttribute("returnUrl", returnUrl);
		model.addAttribute("prescriptionId", prescriptionId);
	}

	public String post(@MethodParam("getPrescription") @BindParams Prescription prescription,
			@RequestParam("patientId") Patient patient, @RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "prescriptionId", required = false) Integer prescriptionId,
			@RequestParam(value = "returnUrl", required = false) String returnUrl, PageModel model,
			@SpringBean("prescription.PrescriptionService") PrescriptionService service, HttpSession session,
			UiUtils ui) throws Exception {

		if (StringUtils.isNotBlank(action))
			System.out.println("Log: post action PrescriptionPageController - " + action);

		try {

			if (prescription != null) {

				if (StringUtils.isNotBlank(prescription.getDose())) {

					service.saveItem(prescription);
					model.addAttribute("prescription", prescription);
				}
			}

			// InfoErrorMessageUtil.flashInfoMessage(session, successMsgCode);

			return "redirect:prescription/prescriptions.page?patientId=" + patient.getPatientId() + "&returnUrl="
					+ ui.urlEncode(returnUrl);
		} catch (Exception e) {
			session.setAttribute("error_message", "failed to add prescription");
			e.printStackTrace();
		}

		model.addAttribute("returnUrl", returnUrl);
		model.addAttribute("patient", patient);
		if (prescription != null)
			model.addAttribute("prescription", prescription);

		return null;
	}

	public Prescription getPrescription(@RequestParam("patientId") Patient patient,
			@RequestParam(value = "drugId", required = false) Integer drugId,
			@RequestParam(value = "dose", required = false) String dose,
			@RequestParam(value = "advice", required = false) String advice,
			@RequestParam(value = "prescriptionId", required = false) Integer prescriptionId,
			@SpringBean("prescription.PrescriptionService") PrescriptionService service, HttpServletRequest request) {

		if (drugId == 0)
			return null;
		else
			System.out.println("drugID " + drugId);

		if (StringUtils.isBlank(dose))
			return null;

		Prescription prescription = null;

		if (prescriptionId != null) {
			prescription = service.getItemById(prescriptionId);

			System.out.println("Log PrescriptionPageController: prescriptionId - " + prescriptionId);
		} else {
			prescription = new Prescription();
			prescription.setPatientId(patient.getPatientId());
		}

		prescription.setDrugId(drugId);
		prescription.setDescription(service.getDrugDescriptionById(drugId));
		prescription.setDose(dose);

		if (StringUtils.isBlank(advice))
			prescription.setAdvice(null);
		else
			prescription.setAdvice(advice);

		return prescription;
	}

}
