/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptDescription;
import org.openmrs.ConceptName;
import org.openmrs.ConceptSet;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;

import java.io.File;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletContext;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.openmrs.module.prescription.api.PrescriptionService;

/**
 * Pls see DownloadConceptServlet
 * http://uat02.openmrs.org:8080/openmrs/admin/forms/formEdit.form?formId=6
 * http://uat02.openmrs.org:8080/openmrs/dictionary/index.htm
 * https://github.com/openmrs/openmrs-module
 * -legacyui/blob/c9bb7b8ba008573568f46cb2d09ca987d309683d/omod
 * /src/main/java/org/openmrs/web/servlet/DownloadDictionaryServlet.java
 * https://wiki.openmrs.org/display/docs/Module+Servlets a
 * href="/openmrs/moduleServlet/legacyui/downloadDictionaryServlet">Download file
 */
public class DownloadPrescriptionServlet extends HttpServlet {
	
	public static final long serialVersionUID = 1231231L;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		//return;
		
		log.error("Servlet starts");
		
		try {
			//Locale locale = Context.getLocale();
			
			//response.getWriter().write(headerLine);
			
			// takethe path from properties-file:
			String filePath = Context.getService(PrescriptionService.class).getPrescriptionsFolder();
			
			String fileName = request.getParameter("filename");
			
			String fileNameFull = filePath + fileName;
			
			File file = new File(fileNameFull);
			
			log.error("file created: " + fileNameFull + " is? " + file.exists());
			
			ServletContext contexto = getServletConfig().getServletContext();//getServletContext();
			contexto.log("Servlet: " + fileNameFull + " is? " + file.exists());
			
			int BUFSIZE = 4096;
			int length = 0;
			
			if (file.exists()) {
				
				ServletOutputStream outStream = response.getOutputStream();
				ServletContext context = getServletConfig().getServletContext();
				String mimetype = "application/octet-stream";//context.getMimeType(filePath);
				
				response.setContentType(mimetype);
				response.setContentLength((int) file.length());
				
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameFull + "\"");
				
				byte[] byteBuffer = new byte[BUFSIZE];
				DataInputStream in = new DataInputStream(new FileInputStream(file));
				
				while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
					outStream.write(byteBuffer, 0, length);
				}
				
				in.close();
				outStream.close();
				
			} else {
				//handle a response to do nothing
				response.getWriter().write("not existing " + fileNameFull);
			}
			
			// response.getWriter().write(line.toString()); // here: nothing to write, but d.l. a file.
			
		}
		catch (Exception e) {
			log.error("Param: " + request.getParameter("filename"));
			log.error("Error while downloading concepts.", e);
			e.printStackTrace();
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
