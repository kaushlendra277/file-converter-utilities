package root.using.json.simple.and.poi;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTableRow;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Image;
import com.itextpdf.svg.converter.SvgConverter;

public class Poi315Refactoring {
	
	public static void createEdaReport(JSONArray overviews, JSONObject summary) throws Exception{
		String templatePath = "./empty.pptx";
		File kpmgPptTemplate = new File(templatePath);
        InputStream inputStream = new FileInputStream(kpmgPptTemplate);
        XMLSlideShow xmlSlideShow = new XMLSlideShow();
        
        addEdaDataOverview(overviews, xmlSlideShow);
        
        int limit  = 10;
        do {
        	//getting the slide master object
            XSLFSlideMaster slideMaster = xmlSlideShow.getSlideMasters().get(0);
            
            //select a layout from specified list
            XSLFSlideLayout slidelayout = slideMaster.getLayout(SlideLayout.TITLE_ONLY);
            XSLFSlide slide = xmlSlideShow.createSlide(slidelayout);
        	addEdaDataSummary(summary, slide, limit);
        }while(summary.size() > 0);
        
        //creating a file object
        File file = new File("./empty-output-new.pptx");
        FileOutputStream out = new FileOutputStream(file);
        
        //saving the changes to a file
        xmlSlideShow.write(out);
        // closing the resource
        inputStream.close();
        out.close();
	}


	private static void addEdaDataSummary(JSONObject summary, XSLFSlide slide, int limit) {
		
        String title = "EDA Report";
        addTitle(slide, title);
        
        int x = 30;
        int y = 150;
        int height = 50;
        int width = 680;
        Rectangle rectangle = new Rectangle(x, y, width, height);
        addTableHeader(slide,"Data Summary", rectangle);
        
        y += height;
        rectangle = new Rectangle(x, y, width, height);
        List<String> summaryHeaders = Stream
        		.of("", "Mean", "Std Dev", "Zeros","Min", "Median", "Max", "type")
        		.collect(Collectors.toList());
        addSubHeaders(slide, rectangle, summaryHeaders);
        
        y +=height;
        height = height*summary.size();
        rectangle =  new Rectangle(x, y, width, height);
        addEdaDataSummary(summary, slide, rectangle, summaryHeaders, limit);
	}


	private static void addEdaDataSummary(
			JSONObject summary, 
			XSLFSlide slide, 
			Rectangle rectangle,
			List<String> summaryHeaders,
			int limit) {
		XSLFTable table = slide.createTable();
        table.setAnchor(rectangle);
        List<String> summaryKeys = new ArrayList<>(summary.keySet());
        int i = 0;
        for(String key : summaryKeys){
        	JSONArray jKeyObjects = (JSONArray)summary.get(key);
        	List<String> summaryHeaderValues = Stream
            		.of(key)
            		.collect(Collectors.toList());
        	for(Object object : jKeyObjects) {
        		JSONArray jKeyObject = (JSONArray)object;
        		summaryHeaderValues.add(jKeyObject.get(1).toString());
        	}
        	
        	XSLFTableRow headerRow = table.addRow();
        	for(int c=0; c < summaryHeaderValues.size(); c++) {
            	XSLFTableCell th = headerRow.addCell();
                XSLFTextParagraph p = th.addNewTextParagraph();
                p.setTextAlign(TextAlign.CENTER);
                XSLFTextRun r = p.addNewTextRun();
                r.setText(summaryHeaderValues.get(c));
                r.setFontSize(10.0);
                //r.setFontColor(Color.white);
                //th.setFillColor(new Color(79, 129, 189));
                table.setColumnWidth(c, table.getAnchor().getWidth()/(summaryHeaders.size()));
            }
        	// removing
        	summary.remove(key);
        	i++;
        	if(i == limit) break;
        }
	}


	private static void addSubHeaders(XSLFSlide slide, Rectangle rectangle, List<String> subHeaders) {
		XSLFTable table = slide.createTable();
        table.setAnchor(rectangle);
        XSLFTableRow headerRow = table.addRow();
        
        for(int c=0; c < subHeaders.size(); c++) {
        	XSLFTableCell th = headerRow.addCell();
            XSLFTextParagraph p = th.addNewTextParagraph();
            p.setTextAlign(TextAlign.CENTER);
            XSLFTextRun r = p.addNewTextRun();
            r.setText(subHeaders.get(c));
            r.setFontSize(19.0);
            r.setFontColor(Color.white);
            th.setFillColor(new Color(79, 129, 189));
            table.setColumnWidth(c, table.getAnchor().getWidth()/subHeaders.size());
        }
	}


	private static void addEdaDataOverview(JSONArray overviews, XMLSlideShow xmlSlideShow) {
		//getting the slide master object
        XSLFSlideMaster slideMaster = xmlSlideShow.getSlideMasters().get(0);
        
        //select a layout from specified list
        XSLFSlideLayout slidelayout = slideMaster.getLayout(SlideLayout.TITLE_ONLY);
        
        //creating a slide with title and content layout
        XSLFSlide slide = xmlSlideShow.createSlide(slidelayout);
        
        String title = "EDA Report";
        addTitle(slide, title);
        
        int x = 30;
        int y = 150;
        int height = 50;
        int width = 680;
        Rectangle rectangle = new Rectangle(x, y, width, height);
        addTableHeader(slide,"Data Overview", rectangle);
        
        y += height;
        height *= 6;
        rectangle = new Rectangle(x, y, width, height);
        addEdaDataOverview(slide, overviews, rectangle);
	}


	private static void addEdaDataOverview(XSLFSlide slide, JSONArray overviews, Rectangle rectangle) {
		XSLFTable table = slide.createTable();
        table.setAnchor(rectangle);
        for(Object object : overviews) {
        	JSONArray overview = (JSONArray)object;
        	XSLFTableRow row = table.addRow();
        	for(int c=0; c <overview.size() ;c++) {
        		XSLFTableCell th = row.addCell();
        		 XSLFTextParagraph p = th.addNewTextParagraph();
        		 //p.setTextAlign(TextAlign.CENTER);
        		 XSLFTextRun r = p.addNewTextRun();
        		 r.setText(overview.get(c).toString());
        		 r.setFontSize(20.0);
        		 // r.setFontColor(Color.white);
        		 // th.setFillColor(new Color(79, 129, 189));
        		 table.setColumnWidth(c, table.getAnchor().getWidth()/2);
        	}	
        }
	}


	private static void addTableHeader(XSLFSlide slide, String header, Rectangle rectangle) {
		XSLFTable table = slide.createTable();
        table.setAnchor(rectangle);
        XSLFTableRow headerRow = table.addRow();
        headerRow.setHeight(table.getAnchor().getHeight());
        for (int i = 0; i < 1; i++) {
            XSLFTableCell th = headerRow.addCell();
            XSLFTextParagraph p = th.addNewTextParagraph();
            p.setTextAlign(TextAlign.CENTER);
            XSLFTextRun r = p.addNewTextRun();
            r.setText(header);
            r.setFontSize(20.0);
            r.setFontColor(Color.white);
            th.setFillColor(new Color(79, 129, 189));
            table.setColumnWidth(i, table.getAnchor().getWidth());
        }
	}

	
	private static void addTitle(XSLFSlide slide, String title) {
		//selection of title place holder 
        XSLFTextShape body = slide.getPlaceholder(0); 
        
        //clear the existing text in the slide
        body.clearText();
        
        //adding new paragraph
        XSLFTextParagraph paragraph = body.addNewTextParagraph();
        
        //creating text run object
        XSLFTextRun run = paragraph.addNewTextRun();
        run.setText(title);
	}


	public static void createEcReport(JSONArray jArray) throws Exception {
		XMLSlideShow xmlSlideShow = new XMLSlideShow();
		for (Object object : jArray) {
			JSONObject jsonObject = (JSONObject)object;
			Set<String> jsonObjectKeys = jsonObject.keySet();
			for (String jsonObjectKey : jsonObjectKeys) {
				JSONArray jsonObjectKeyObjects = (JSONArray) jsonObject.get(jsonObjectKey);
				JSONObject jsonObjectKeyObject = (JSONObject) jsonObjectKeyObjects.get(0);
				if (jsonObjectKeyObject.containsKey("contrast_exp")) {
					addContrastOfExplainationText(xmlSlideShow, jsonObjectKeyObject);
				}
				if (jsonObjectKeyObject.containsKey("graph")) {
					addContrastOfExplainationGraph(xmlSlideShow, jsonObjectKeyObject);
				}
			}
		}
		
		//creating a file object
        try(FileOutputStream out = new FileOutputStream(new File("./ec-output-new.pptx"))){
        	//saving the changes to a file
            xmlSlideShow.write(out);
        }
	}


	private static void addContrastOfExplainationGraph(XMLSlideShow xmlSlideShow, JSONObject jsonObjectKeyObject)
			throws FileNotFoundException, IOException, InvalidPasswordException {
		JSONArray JGraphArray = (JSONArray) jsonObjectKeyObject.get("graph");
		for (Object jGraphObj : JGraphArray) {
			JSONObject jGraph = (JSONObject) jGraphObj;
			JSONArray jHelpTexts = ((JSONArray) jGraph.get("graph_helptext"));
			List<String> helpTexts = new ArrayList<>();
			for (Object jHelpText: jHelpTexts) {
				helpTexts.add(jHelpText.toString());
			}
			String graph = (String) jGraph.get("graph_data");
			byte[] byteArray = parseSvgImageByteArray(graph);
			String tempImageFile = "./graph.pdf";
			// Image image = addingSvgImageAsPdf(tempImageFile, byteArray);
			// adding svg image as pdf 
			PdfWriter writer = new PdfWriter(new File(tempImageFile));
		    PdfDocument pdf = new PdfDocument(writer);
		    Image image = null;
		    int height = 0;
			int width = 0;
		    try(
		    		com.itextpdf.layout.Document doc = new com.itextpdf.layout.Document(pdf, PageSize.A4);
		    		InputStream targetStream = new ByteArrayInputStream(byteArray);){
		        image = SvgConverter.convertToImage(targetStream, pdf);
		        width = (int)image.getImageWidth() + 1; 
		        height = (int)image.getImageHeight() + 1;
		        doc.add(image);
		    }
		    
			addPdfPageToPpt(tempImageFile, image, xmlSlideShow, width, height+10);
		    Files.deleteIfExists(Paths.get(tempImageFile));
		}
	}


	private static void addPdfPageToPpt(
			String pdfFiePath, 
			Image image, 
			XMLSlideShow xmlSlideShow,
			int pptContainerWidth,
			int pptContainerHeight)
			throws IOException, InvalidPasswordException {
		
		File imagePdf = new File(pdfFiePath);
		try(PDDocument pdDocument = PDDocument.load(imagePdf);){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// reading each page of pdf as image
			PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
			BufferedImage image1 = pdfRenderer.renderImage(0).getSubimage(0, 0, pptContainerWidth, pptContainerHeight);

			// converting BufferedImage into a byte array
			ImageIO.write(image1, "png", baos);
			baos.flush();
			byte[] pictureByteArray = baos.toByteArray();
			
			//getting the slide master object
			XSLFSlideMaster slideMaster = xmlSlideShow.getSlideMasters().get(0);
			
			//select a layout from specified list
			XSLFSlideLayout slidelayout = slideMaster.getLayout(SlideLayout.TITLE_ONLY);
			
			//creating a slide with title and content layout
			XSLFSlide slide = xmlSlideShow.createSlide(slidelayout);
			
			String title = "EC Graph";
			addTitle(slide, title);
			
			XSLFPictureData pictureData = xmlSlideShow.addPicture(pictureByteArray, PictureType.PNG);
			// creating a slide with given picture on it
			XSLFPictureShape picture = slide.createPicture(pictureData);
			int pageHeight = (int)xmlSlideShow.getPageSize().getHeight();
			int pageWidth = (int)xmlSlideShow.getPageSize().getWidth();
			picture.setAnchor(new Rectangle((pageWidth-pptContainerWidth)/2, (pageHeight-pptContainerHeight)/2, pptContainerWidth, pptContainerHeight));
		}
	}


	private static Image addingSvgImageAsPdf(String pdfFilePath, byte[] byteArray) throws IOException {
		Image image = null;
		PdfWriter writer = new PdfWriter(new File(pdfFilePath));
        PdfDocument pdf = new PdfDocument(writer);
        try(
        		com.itextpdf.layout.Document doc = new com.itextpdf.layout.Document(pdf, PageSize.A4);
        		InputStream targetStream = new ByteArrayInputStream(byteArray);){
            image = SvgConverter.convertToImage(targetStream, pdf);
            doc.add(image);
        }
        image.getImageHeight();
        return image;
	}


	private static byte[] parseSvgImageByteArray(String graph) {
		org.jsoup.nodes.Document htmlDoc = Jsoup.parse(graph);
		Elements svgImage = htmlDoc.getElementsByTag("img");
		// expecting only one svg image per value
		String src = svgImage.get(0).attr("src");
		String[] parts = src.split(",");
		String srcBase64EncodeString = parts[1];
		byte[] byteArray = Base64.getDecoder().decode(srcBase64EncodeString.getBytes());
		return byteArray;
	}
	
	private static void addContrastOfExplainationText(XMLSlideShow xmlSlideShow, JSONObject jContrastExp) {
		//getting the slide master object
		XSLFSlideMaster slideMaster = xmlSlideShow.getSlideMasters().get(0);
		
		//select a layout from specified list
		XSLFSlideLayout slidelayout = slideMaster.getLayout(SlideLayout.TITLE_ONLY);
		
		//creating a slide with title and content layout
		XSLFSlide slide = xmlSlideShow.createSlide(slidelayout);
		
		String title = "Contrast of Explaination";
		addTitle(slide, title);
		
		int x = 30;
		int y = 150;
		int height = 25;
		int width = 680;
		Rectangle rectangle = new Rectangle(x, y, width, height);
		addTableHeader(slide,"Explaination", rectangle);
		
		y += height;
		rectangle = new Rectangle(x, y, width, height);
		addContrastExpText(slide,jContrastExp.get("contrast_exp").toString(), rectangle);
		
		y += height*3;
		rectangle = new Rectangle(x, y, width, height);
		addTableHeader(slide,"Row Selected", rectangle);
		
		JSONObject jRowSelected = (JSONObject)jContrastExp.get("contrast_row");
		List<String> jRowSelectedKeys = new ArrayList<>(jRowSelected.keySet());
		y += height;
		rectangle = new Rectangle(x, y, width, height);
		addEcRowSelected(slide, rectangle, jRowSelected);
	}


	private static void addEcRowSelected(
			XSLFSlide slide, 
			Rectangle rectangle, 
			JSONObject jRowSelected) {
		List<String> jRowSelectedKeys = new ArrayList<>(jRowSelected.keySet());
		XSLFTable table = slide.createTable();
		table.setAnchor(rectangle);
        for (int r = 0; r < jRowSelectedKeys.size(); r++) {
        	String key = jRowSelectedKeys.get(r);
        	
        	XSLFTableRow headerRow = table.addRow();
        	headerRow.setHeight(table.getAnchor().getHeight());
            XSLFTableCell th = headerRow.addCell();
            XSLFTextParagraph p = th.addNewTextParagraph();
            // p.setTextAlign(TextAlign.CENTER);
            XSLFTextRun textRun = p.addNewTextRun();
            textRun.setText(key);
            textRun.setFontSize(20.0);
            // r.setFontColor(Color.white);
            // th.setFillColor(new Color(79, 129, 189));
            table.setColumnWidth(0, table.getAnchor().getWidth()/2);
            
            th = headerRow.addCell();
            p = th.addNewTextParagraph();
            // p.setTextAlign(TextAlign.CENTER);
            textRun = p.addNewTextRun();
            textRun.setText(jRowSelected.get(key).toString());
            textRun.setFontSize(20.0);
            // r.setFontColor(Color.white);
            // th.setFillColor(new Color(79, 129, 189));
            table.setColumnWidth(0, table.getAnchor().getWidth()/2);
        }
	}
	
	private static void addContrastExpText(XSLFSlide slide, String contarstExp, Rectangle rectangle) {
		XSLFTable table = slide.createTable();
        table.setAnchor(rectangle);
        XSLFTableRow headerRow = table.addRow();
        headerRow.setHeight(table.getAnchor().getHeight());
        for (int i = 0; i < 1; i++) {
            XSLFTableCell th = headerRow.addCell();
            XSLFTextParagraph p = th.addNewTextParagraph();
            p.setTextAlign(TextAlign.CENTER);
            XSLFTextRun r = p.addNewTextRun();
            r.setText(contarstExp);
            r.setFontSize(20.0);
            // r.setFontColor(Color.white);
            // th.setFillColor(new Color(79, 129, 189));
            table.setColumnWidth(i, table.getAnchor().getWidth());
        }
	}
}
