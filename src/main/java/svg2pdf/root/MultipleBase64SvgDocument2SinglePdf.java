package svg2pdf.root;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

public class MultipleBase64SvgDocument2SinglePdf {

	public static void main(String[] args) throws IOException, TranscoderException, DocumentException {
		Collection<String> values = new ArrayList<String>();

		// Step 1 - Reading json from file
		try {
			JSONParser parser = new JSONParser();
			FileReader jsonFileReader = new FileReader("D:\\workdir\\svg2pdf\\model_metrics.json");
			JSONArray jsonArray = (JSONArray) parser.parse(jsonFileReader);
			for (Object object : jsonArray) {
				JSONObject jsonObject = (JSONObject) object;
				if (jsonObject.containsKey("Logistic Regression")) {
					JSONArray lrJsonArray = (JSONArray)jsonObject.get("Logistic Regression");
					for(Object innerObject : lrJsonArray) {
						values.addAll(((JSONObject)innerObject).values());
					}
					
				} else if (jsonObject.containsKey("Support Vector Machines")) {
					JSONArray lrJsonArray = (JSONArray)jsonObject.get("Support Vector Machines");
					for(Object innerObject : lrJsonArray) {
						values.addAll(((JSONObject)innerObject).values());
					}
				}
			}
			values.removeIf(e -> !e.trim().startsWith("<html>"));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		System.out.println("********JSON read successfully"+values.size());
		//values.forEach(System.out :: println);
		
		// Step 2 - extract src from list of string
		List<byte[]> byteArrays = new ArrayList<>();
		for (String value : values) {
			Document htmlDoc = Jsoup.parse(value);
			Elements svgImage = htmlDoc.getElementsByTag("img");
			
			// expecting only one svg image per value
			String src =  svgImage.get(0).attr("src");
			String[] parts = src.split(",");
			String srcBase64EncodeString = parts[1];
			byte[] byteArray = Base64.getDecoder().decode(srcBase64EncodeString.getBytes());
			byteArrays.add(byteArray);
		}
		
		//Step 3 - exporting svg document byte array to pdfs
		// List to track all generated pdfs
		List<String> pdfFileDirectories = new ArrayList<String>();
		for (byte[] bytes : byteArrays) {
			exportSvg2Pdf(bytes,pdfFileDirectories);
			int index = pdfFileDirectories.size()-1;
			String lastGeneratedPdfFile =  pdfFileDirectories.get(index);
			// adding additional text
			pdfFileDirectories.set(index, addAdditionalTextToPdf(lastGeneratedPdfFile));
		}
		
		// Step 4 - merging all pdfs
		PDFMergerUtility pdfMerger = new PDFMergerUtility();
	    
	    //Setting the destination file
	    pdfMerger.setDestinationFileName("D:\\workdir\\svg2pdf\\merged.pdf");
	    List<PDDocument> pdDocuments = new ArrayList<PDDocument>();
	    List<File> files = new ArrayList<File>();
	    for (String pdfFile : pdfFileDirectories) {
	    	File file = new File(pdfFile);
	    	pdDocuments.add(PDDocument.load(file));
	    	pdfMerger.addSource(file);
	    	files.add(file);
		}
	    pdfMerger.mergeDocuments();
	    
	    System.out.println("Documents merged");
	    
	    //Step 5 - closing resources and cleaning up temp pdfs
	    
	    //Closing the documents
	    for (PDDocument pdDocument : pdDocuments) {
	    	pdDocument.close();
		}
	    
	    for (File file : files) {
	    	file.delete();
		}
	}
	
	private static String addAdditionalTextToPdf(String path) throws DocumentException, IOException {
		String newPath = path.replace("oldd", "");
		com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A3);
		OutputStream outputStream = new FileOutputStream(new File(newPath));
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		
		// Load existing PDF
		InputStream templateInputStream = new FileInputStream(new File(path));
		PdfReader reader = new PdfReader(templateInputStream);
		PdfImportedPage page = writer.getImportedPage(reader, 1);
		
		// Copy first page of existing PDF into output PDF
		document.newPage();
		cb.addTemplate(page, 0, 0);

		// Add your new data / text here
		// for example...
		document.add(new Paragraph("ADDITIONAL_TEXT_COMES_HERE")); 

		document.close();
		File file = new File(path);
		if(file.exists())
			file.delete();
		return newPath;
	}

	private static void exportSvg2Pdf(byte[] byteArray, List<String> fileDirectories) throws IOException, TranscoderException {

		OutputStream pdfOutputStream = null;
		try {
			// Step -1: We read the input SVG document into Transcoder Input
			String pdfFileDir = "D:\\workdir\\svg2pdf\\exportedPdf"+fileDirectories.size()+"oldd.pdf";
			fileDirectories.add(pdfFileDir);
			TranscoderInput transcoderInput = new TranscoderInput(new ByteArrayInputStream(byteArray));
			TranscoderInput transcoderInput2 = new TranscoderInput(new ByteArrayInputStream(byteArray));
			
			// Step-2: Define OutputStream to PDF file and attach to TranscoderOutput
			File targetDirectory = new File(pdfFileDir);

			pdfOutputStream = new FileOutputStream(targetDirectory, true);
			TranscoderOutput transcoderOutput = new TranscoderOutput(pdfOutputStream);

			// Step-3: Create a PDF Transcoder and define hints
			Transcoder transcoder = new PDFTranscoder();

			// Step-4: Write output to PDF format
			transcoder.transcode(transcoderInput, transcoderOutput);
			pdfOutputStream.write(byteArray);
			transcoder.transcode(transcoderInput2, transcoderOutput);
			
		} finally {
			// Step 5- close / flush Output Stream
			pdfOutputStream.flush();
			pdfOutputStream.close();
		}
	}

}
