package string2pdfIText.root;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Deprecated()
public class String2pdfITextInAction {

	public static void main(String[] args) {
		String pdfFile = "D:\\workdir\\IIAW-23-08-2019\\service2-poc\\string2pdfUsingIText\\projectOverview.pdf";
		try {
			exportProjectOverviewPage(pdfFile);
			System.out.println("Project overview page exported...");
		} catch (FileNotFoundException | DocumentException e) {
			System.err.println(e);
		}
		
	}

	private static void exportProjectOverviewPage(String pdfFile) throws FileNotFoundException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
		document.open();
		
		PdfPTable table = new PdfPTable(1);
		PdfPCell column = new PdfPCell();// to configure column
		Font font = FontFactory.getFont(FontFactory.COURIER_BOLD, 16, BaseColor.BLACK);
		Chunk chunk  = new Chunk("Project Name : ", font);
		column.setPhrase(new Phrase(chunk));
		table.addCell(column);
		
		column.setPhrase(new Phrase("project.getName()aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" ));
		table.addCell(column);
		
		chunk  = new Chunk("Project Description : ", font);
		column.setPhrase(new Phrase(chunk));
		table.addCell(column);
		
		column.setPhrase(new Phrase("project.getDescriptionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn()" ));
		table.addCell(column);
		
		document.add(table);
		document.close();
		
	}
}
