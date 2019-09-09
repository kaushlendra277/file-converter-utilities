package string2pdfIText.root.hmk;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

// http://hmkcode.com/java-itext-pdf-table-structure-style/
public class String2PdfInActionByHMK {
	public static void main( String[] args ) throws FileNotFoundException, DocumentException
    {
		String file = "D:\\workdir\\IIAW-23-08-2019\\service2-poc\\string2pdfUsingIText\\projectOverviewHMK.pdf";
		createProjetOverviewPdf(file);
        System.out.println( "PDF Created!" );
    }

	private static void createProjetOverviewPdf(String file) throws DocumentException, FileNotFoundException {
		// step 1
        Document document = new Document();
        document.setPageSize(PageSize.A4);
 
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(file));
 
        // step 3
        document.open();
 
        // step 4 create PDF contents
        document.add(ITextTableBuilder.createProjectOverviewTable());
 
        //step 5
        document.close();
	}  

}
