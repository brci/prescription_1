package org.openmrs.module.prescription.api.print;

import java.io.FileOutputStream;
import java.util.Date;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
//import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.PageSize;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openmrs.module.prescription.Prescription;
import java.util.List;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.Chunk;

public class PdfTemplate {
	
	public static final Chunk NEWLINE = new Chunk("\n");
	
	private static Font fontSmallBold = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
	
	private static Font fontSmallNormal = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
	
	private static Font fontSmallerNormal = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
	
	public void writeOutPdf(List<Prescription> prescriptions, String filename, String add1, String add2, String add3,
	        String patientName) {
		try {
			Document document = new Document();
			document.setPageSize(PageSize.A6);
			PdfWriter.getInstance(document, new FileOutputStream(filename));
			document.open();
			addMetaData(document);
			addContent(document, prescriptions, add1, add2, add3, patientName);
			document.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("wrote a prescription");
	}
	
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
		for (int i = 0; i < prescriptions.size(); i++) {
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
