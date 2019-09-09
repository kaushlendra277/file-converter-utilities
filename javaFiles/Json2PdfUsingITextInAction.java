package root;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.xml.simpleparser.NewLineHandler;

public class Json2PdfUsingITextInAction {

	public static void main(String[] args) throws IOException, ParseException, DocumentException {
		// Step 1 - prepare json object from json file
		JSONArray jsonArray = fromJsonFile("D:\\workdir\\poc\\json2pdf\\json2pdf\\address.json");
		JSONArray overview = null;
		JSONObject summary = null;
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject)object;
			overview = (JSONArray)jsonObject.get("overview");
			summary = (JSONObject)jsonObject.get("summary");
			
		}	
		
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("D:\\workdir\\poc\\json2pdf\\json2pdf\\address.pdf"));
		document.open();
		 
		Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
		String indent = " ";
		Chunk chunk = new Chunk("Data Overview....................", font);
		document.add( new Paragraph(chunk) );
		for (Object object : overview) {
			JSONArray overviewValues = (JSONArray)object;
			//System.out.println(overviewValues.get(0)+","+overviewValues.get(1) );
			chunk = new Chunk(overviewValues.get(0)+indent+indent+indent+indent+overviewValues.get(1));
			document.add( new Paragraph(chunk) );
		}
		
		chunk = new Chunk("Data Summary....................", font);
		document.add( new Paragraph(chunk) );
		chunk = new Chunk(Chunk.NEWLINE);
		document.add(chunk);
		PdfPTable table = new PdfPTable(7); // 6 + 1 blank
		addTableHeader(table);
		Set<String> summaryColunm0 = summary.keySet();
		for (String key : summaryColunm0) {
			JSONObject row = (JSONObject)summary.get(key);
			addRows(table, key , row);
		}
		
		document.add(table);
		
		document.close();
		System.out.println("PDF generated .............");

	}
	
	private static JSONArray fromJsonFile(String file) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		FileReader jsonFileReader = new FileReader(file);
		JSONArray jsonArray = (JSONArray) parser.parse(jsonFileReader);
		return jsonArray;
	}
	
	private static void addTableHeader(PdfPTable table) {
	    Stream.of("","MEAN", "STD DEV", "ZEROS", "MIN", "MEDIAN", "MAX")
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header.setBorderWidth(2);
	        header.setPhrase(new Phrase(columnTitle));
	        table.addCell(header);
	    });
	}
	private static void addRows(PdfPTable table, String key, JSONObject value ) {
		table.addCell(key);
		table.addCell(value.get("mean").toString());
	    table.addCell(value.get("std").toString());
	    table.addCell(value.get("zeros").toString());
	    table.addCell(value.get("min").toString());
	    table.addCell(value.get("median").toString());
	    table.addCell(value.get("max").toString());
	}
}
