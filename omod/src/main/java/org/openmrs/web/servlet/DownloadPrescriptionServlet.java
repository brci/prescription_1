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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.prescription.Prescription;
import org.openmrs.module.prescription.api.PrescriptionService;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
//instead: com.lowagie.text.pdf
//using (2.1.7): com.itextpdf.text.pdf
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Pls see DownloadConceptServlet
 * http://uat02.openmrs.org:8080/openmrs/admin/forms/formEdit.form?formId=6
 * http://uat02.openmrs.org:8080/openmrs/dictionary/index.htm
 * https://github.com/openmrs/openmrs-module
 * -legacyui/blob/c9bb7b8ba008573568f46cb2d09ca987d309683d/omod
 * /src/main/java/org/openmrs/web/servlet/DownloadDictionaryServlet.java
 * https://wiki.openmrs.org/display/docs/Module+Servlets a
 * href="/openmrs/moduleServlet/legacyui/downloadDictionaryServlet">Download
 * file
 */
public class DownloadPrescriptionServlet extends HttpServlet {

	public static final long serialVersionUID = 1231231L;

	private Log log = LogFactory.getLog(this.getClass());

	public static final Chunk NEWLINE = new Chunk("\n");

	private static Font fontSmallBold = new Font(Font.TIMES_ROMAN, 11, Font.BOLD);

	private static Font fontSmallNormal = new Font(Font.TIMES_ROMAN, 11, Font.NORMAL);

	private static Font fontSmallerNormal = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.error("Servlet starts");

		try {
			String fileName = request.getParameter("filename");
			List<Prescription> prescriptions = Context.getService(PrescriptionService.class)
					.getAllPrescriptionsByFile(fileName);

			String patientName = "";

			if (prescriptions.size() > 0) {
				int patientId = ((Prescription) prescriptions.get(0)).getPatientId();
				Patient patient = Context.getService(PatientService.class).getPatient(patientId);

				patientName = patient.getFamilyName() + " " + patient.getGivenName();
			}

			String[] providerAddress = Context.getService(PrescriptionService.class).getAddressToPrint();

			String addr1 = providerAddress[0];
			String addr2 = providerAddress[1];
			String addr3 = providerAddress[2];
			Properties rtp = Context.getRuntimeProperties();

			if (rtp.getProperty("prescription_address_1") != null)
				addr1 = rtp.getProperty("prescription_address_1");

			if (rtp.getProperty("prescription_address_2") != null)
				addr2 = rtp.getProperty("prescription_address_2");

			if (rtp.getProperty("prescription_address_3") != null)
				addr3 = rtp.getProperty("prescription_address_3");

			// int BUFSIZE = 4096;

			Document document = new Document();
			document.setPageSize(PageSize.A6);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, baos);
			document.open();
			addMetaData(document);
			addContent(document, prescriptions, addr1, addr2, addr3, patientName);
			document.close();

			response.setHeader("Cache-Control", "no cache");
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			OutputStream os = response.getOutputStream();
			baos.writeTo(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error("Param: " + request.getParameter("filename"));
			log.error("Error while downloading concepts.", e);
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	// ------------- pdf ----------------

	private void addMetaData(Document document) {
		document.addTitle("Prescription");
		document.addAuthor("OpenMRS");
		document.addCreator("OpenMRS, Admin");
	}

	private void addContent(Document document, List<Prescription> prescriptions, String address1, String address2,
			String address3, String patientName) throws DocumentException {

		Paragraph p;

		p = new Paragraph(13, address1, fontSmallBold);
		p.setAlignment(Element.ALIGN_CENTER);
		document.add(p);

		p = new Paragraph(13, address2, fontSmallNormal);
		p.setAlignment(Element.ALIGN_CENTER);
		document.add(p);

		p = new Paragraph(13, address3, fontSmallNormal);
		p.setAlignment(Element.ALIGN_CENTER);
		document.add(p);

		document.add(Chunk.NEWLINE);

		// patient information

		PdfPTable table;

		table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setTotalWidth(PageSize.A6.getWidth() - Math.round(PageSize.A6.getWidth() * 0.1));
		table.setLockedWidth(true);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		String contentRight = patientName;
		String contentLeft = new SimpleDateFormat("MMM dd, yyyy").format(new Date());

		PdfPCell cell;

		cell = getCell(contentLeft, Element.ALIGN_LEFT, fontSmallerNormal);
		cell.setBorder(Rectangle.TOP);
		cell.setBorder(Rectangle.BOTTOM);
		table.addCell(cell);

		cell = getCell(contentRight, Element.ALIGN_RIGHT, fontSmallerNormal);
		cell.setBorder(Rectangle.TOP);
		cell.setBorder(Rectangle.BOTTOM);
		table.addCell(cell);

		document.add(table);

		document.add(Chunk.NEWLINE);

		// prescriptions

		p = new Paragraph(12, "Prescriptions", fontSmallNormal);
		p.setAlignment(Element.ALIGN_CENTER);
		document.add(p);

		table = new PdfPTable(3);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10);
		table.setSpacingAfter(10);
		table.setWidths(new int[] { 1, 14, 4 });

		table.setTotalWidth(PageSize.A6.getWidth() - Math.round(PageSize.A6.getWidth() * 0.1));
		table.setLockedWidth(true);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		cell = getCell("", Element.ALIGN_CENTER, fontSmallBold);
		cell.setColspan(3);
		cell.setBorder(Rectangle.TOP);
		cell.setBorder(Rectangle.BOTTOM);
		table.addCell(cell);

		int count = 0;

		int noOfP = 0;

		if (prescriptions != null)
			noOfP = prescriptions.size();

		for (int i = 0; i < noOfP; i++) {
			Prescription item = (Prescription) prescriptions.get(i);

			cell = getCell(item.getDescription(), Element.ALIGN_LEFT, fontSmallNormal);
			cell.setColspan(2);
			table.addCell(cell);

			cell = getCell(item.getDose(), Element.ALIGN_LEFT, fontSmallNormal);
			table.addCell(cell);

			if (item.getAdvice() == null || item.getAdvice().isEmpty())
				;
			else {
				cell = getCell("", Element.ALIGN_CENTER, fontSmallBold);
				table.addCell(cell);

				cell = getCell(item.getAdvice(), Element.ALIGN_LEFT, fontSmallNormal);
				cell.setColspan(2);
				cell.setNoWrap(false);
				table.addCell(cell);
			}
			cell = getCell("", Element.ALIGN_CENTER, fontSmallBold);
			cell.setColspan(3);
			cell.setFixedHeight(5f);
			table.addCell(cell);

			count++;
		}
		for (int i = count; i < 12; i++) {
			cell = getCell("", Element.ALIGN_CENTER, fontSmallBold);
			cell.setColspan(3);
			cell.setFixedHeight(10f);
			table.addCell(cell);
		}

		cell = getCell("", Element.ALIGN_CENTER, fontSmallBold);
		cell.setFixedHeight(10f);
		cell.setColspan(3);
		cell.setBorder(Rectangle.BOTTOM);
		table.addCell(cell);

		document.add(table);
	}

	public PdfPCell getCell(String value, int alignment, Font font) {

		PdfPCell cell = new PdfPCell();
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setUseAscender(true);
		cell.setUseDescender(true);

		Paragraph p = new Paragraph(13, value, font);
		p.setAlignment(alignment);

		cell.addElement(p);
		return cell;
	}

}
