package root.Itext7core.hmk;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;


public class Json2PdfUsingIText7CoreInActionHMK{
    public static String DEST = "./target/pdfs/projectOverview.pdf";
 
    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document document = new Document(pdfDoc);
        addProjectOverviewContent(document);
        document.close();
        System.out.println("Project Overview exported");
        // eda report
        DEST = DEST.replace("/projectOverview.pdf", "/eda.pdf");
        pdfDoc = new PdfDocument(new PdfWriter(DEST));
        document = new Document(pdfDoc);
        
    	// Step 0 - prepare json object from json file
 		JSONArray jsonArray;
 		try {
 			jsonArray = fromJsonFile("D:\\workdir\\samples\\eda.json");
 			JSONArray overview = null;
 			JSONObject summary = null;
 			for (Object object : jsonArray) {
 				JSONObject jsonObject = (JSONObject)object;
 				overview = (JSONArray)jsonObject.get("overview");
 				summary = (JSONObject)jsonObject.get("summary");
 			}
 			addDataOverview(overview, document);
 			addDataSummary(summary, document);
 		}
 		catch(Exception ex) {
 			ex.printStackTrace();
 		}
 		finally {
			
		}
 		document.close();
 		System.out.println("Data Overview and summary exported");
    }
 
    private static void addProjectOverviewContent(Document document) throws IOException {
    	document.add(IText7CoreHelper.addHeader("PROJECT OVERVIEW"));
        document.add(IText7CoreHelper.addProjectOverview());
	}
    
    
    // eda report
    private static JSONArray fromJsonFile(String file) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		FileReader jsonFileReader = new FileReader(file);
		JSONArray jsonArray = (JSONArray) parser.parse(jsonFileReader);
		jsonFileReader.close();
		return jsonArray;
	}
    
		
    private static void addDataOverview(JSONArray overview, Document document) throws IOException {
    	// add header for Data Overview
    	document.add(IText7CoreHelper.addHeader("Data Overview"));
		// add data overview content
		document.add(IText7CoreHelper.addDataOverview(overview));
	}
    
    private static void addDataSummary(JSONObject summary, Document document) throws IOException  {
		// add header for Data Overview
		document.add(IText7CoreHelper.addHeader("Data Summary"));
		
		// add data summary content
		document.add(IText7CoreHelper.addDataSummary(summary));
	}
}
