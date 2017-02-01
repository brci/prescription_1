/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.prescription.fragment.controller;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.prescription.api.PrescriptionService;
import org.openmrs.ui.framework.annotation.FragmentParam;
//import org.openmrs.api.UserService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class PrescriptionsFragmentController {
	
	public void controller(FragmentModel model, @FragmentParam("patientId") Patient patient,
	        @SpringBean("prescription.PrescriptionService") PrescriptionService service, HttpServletRequest request) {
		
		model.addAttribute("prescriptionsgrouped", service.getAllPrescriptionsGrouped(patient));
		
		model.addAttribute("hasPrescriptionViewPrivilege",
		    Context.getAuthenticatedUser().hasPrivilege("Task: Prescription_view_privilege"));
		model.addAttribute("hasPrescriptionModifyPrivilege",
		    Context.getAuthenticatedUser().hasPrivilege("Task: Prescription_modify_privilege"));
		
		model.addAttribute("currenturl", request.getContextPath());
		
		System.out.println("Privilege view: "
		        + Context.getAuthenticatedUser().hasPrivilege("Task: Prescription_view_privilege"));
		System.out.println("Privilege modify: "
		        + Context.getAuthenticatedUser().hasPrivilege("Task: Prescription_modify_privilege"));
		
	}
	
}
