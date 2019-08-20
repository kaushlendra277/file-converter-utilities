package svg2pdf.root;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class AddAdditionalTextToExistingPdf {

	public static void main(String[] args) throws DocumentException, IOException {
		Document document = new Document(PageSize.A3);
		OutputStream outputStream = new FileOutputStream(new File("D:\\workdir\\svg2pdf\\extra.pdf"));
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);

		document.open();
		PdfContentByte cb = writer.getDirectContent();
		
		// Load existing PDF
		InputStream templateInputStream = new FileInputStream(new File("D:\\workdir\\svg2pdf\\exportedPdf2.pdf"));
		PdfReader reader = new PdfReader(templateInputStream);
		PdfImportedPage page = writer.getImportedPage(reader, 1); 
		
		// Copy first page of existing PDF into output PDF
		document.newPage();
		cb.addTemplate(page, 0, 0);

		// Add your new data / text here
		// for example...
		document.add(new Paragraph("my timestamp")); 

		document.close();
	}

}
