package root.using.json.simple.and.poi;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
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

@Deprecated
public class Poi315 {

	public static void createEdaPpt(JSONArray overviews, JSONObject summary) throws IOException {
		String templatePath = "./empty.pptx";
		File kpmgPptTemplate = new File(templatePath);
        InputStream inputStream = new FileInputStream(kpmgPptTemplate);
        XMLSlideShow xmlSlideShow = new XMLSlideShow();
        
        //getting the slide master object
        XSLFSlideMaster slideMaster = xmlSlideShow.getSlideMasters().get(0);
        
        //select a layout from specified list
        XSLFSlideLayout slidelayout = slideMaster.getLayout(SlideLayout.TITLE_ONLY);
        
        //creating a slide with title and content layout
        XSLFSlide slide = xmlSlideShow.createSlide(slidelayout);
        
        //////////////////////////////////////////////////////////////////
        // adding title -starts
        
        //selection of title place holder
        XSLFTextShape body = slide.getPlaceholder(0); //This is to get handle on various division of ppt
        
        //clear the existing text in the slide
        body.clearText();
        
        //adding new paragraph
        XSLFTextParagraph paragraph = body.addNewTextParagraph();
        
        //creating text run object
        XSLFTextRun run = paragraph.addNewTextRun();
        run.setText("<sample-heading>");
        //adding title - ends
        //////////////////////////////////////////////////////////////////
        // adding Table
        
        XSLFTable table = slide.createTable();
        int height = 50;
        int width = 680;
        int y = 150;
        table.setAnchor(new Rectangle(30, y, width, height));
        XSLFTableRow headerRow = table.addRow();
        headerRow.setHeight(table.getAnchor().getHeight());
        for (int i = 0; i < 1; i++) {
            XSLFTableCell th = headerRow.addCell();
            XSLFTextParagraph p = th.addNewTextParagraph();
            p.setTextAlign(TextAlign.CENTER);
            XSLFTextRun r = p.addNewTextRun();
            r.setText("Data Overview");
            r.setFontSize(20.0);
            r.setFontColor(Color.white);
            th.setFillColor(new Color(79, 129, 189));
            table.setColumnWidth(i, table.getAnchor().getWidth());
        }
        table = slide.createTable();
        y += height;
        table.setAnchor(new Rectangle(30, y, width, height*6));
        for(Object object : overviews) {
        	JSONArray overview = (JSONArray)object;
        	headerRow = table.addRow();
        	for(int c=0; c <overview.size() ;c++) {
        		XSLFTableCell th = headerRow.addCell();
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
        
        /////////////////////////////////////////////
        //  EDA SUMMARY Slide
        //creating a slide with title and content layout
        slide = xmlSlideShow.createSlide(slidelayout);
        
        //selection of title place holder
        body = slide.getPlaceholder(0); //This is to get handle on various division of ppt
        
        //clear the existing text in the slide
        body.clearText();
        
        //adding new paragraph
        paragraph = body.addNewTextParagraph();
        
        //creating text run object
        run = paragraph.addNewTextRun();
        run.setText("<sample-heading>");
        
        
        table = slide.createTable();
        height = 50;
        // width = 600;
        y = 150;
        table.setAnchor(new Rectangle(30, y, width, height));
        headerRow = table.addRow();
        headerRow.setHeight(table.getAnchor().getHeight());
        for (int i = 0; i < 1; i++) {
            XSLFTableCell th = headerRow.addCell();
            XSLFTextParagraph p = th.addNewTextParagraph();
            p.setTextAlign(TextAlign.CENTER);
            XSLFTextRun r = p.addNewTextRun();
            r.setText("Data Summary");
            r.setFontSize(20.0);
            r.setFontColor(Color.white);
            th.setFillColor(new Color(79, 129, 189));
            table.setColumnWidth(i, table.getAnchor().getWidth());
        }
        table = slide.createTable();
        y += height;
        table.setAnchor(new Rectangle(30, y, width, height));
        headerRow = table.addRow();
        List<String> summaryHeaders = Stream
        		.of("", "Mean", "Std Dev", "Zeros","Min", "Median", "Max", "type")
        		.collect(Collectors.toList());
        for(int c=0; c < summaryHeaders.size(); c++) {
        	XSLFTableCell th = headerRow.addCell();
            XSLFTextParagraph p = th.addNewTextParagraph();
            p.setTextAlign(TextAlign.CENTER);
            XSLFTextRun r = p.addNewTextRun();
            r.setText(summaryHeaders.get(c));
            r.setFontSize(19.0);
            r.setFontColor(Color.white);
            th.setFillColor(new Color(79, 129, 189));
            table.setColumnWidth(c, table.getAnchor().getWidth()/summaryHeaders.size());
        }
        
        table = slide.createTable();
        y += height;
        table.setAnchor(new Rectangle(30, y, width, height*summary.size()));
        List<String> summaryKeys = new ArrayList<>(summary.keySet());
        int limit = 10;
        //int r = 0;
        for(String key : summaryKeys){
        //for(r = 0; r < limit; r++) {
        	//String key = summaryKeyS.get(r);
        	JSONArray jKeyObjects = (JSONArray)summary.get(key);
        	List<String> summaryHeaderValues = Stream
            		.of(key)
            		.collect(Collectors.toList());
        	for(Object object : jKeyObjects) {
        		JSONArray jKeyObject = (JSONArray)object;
        		summaryHeaderValues.add(jKeyObject.get(1).toString());
        	}
        	
        	headerRow = table.addRow();
        	for(int c=0; c < summaryHeaderValues.size(); c++) {
            	XSLFTableCell th = headerRow.addCell();
                XSLFTextParagraph p = th.addNewTextParagraph();
                p.setTextAlign(TextAlign.CENTER);
                XSLFTextRun r = p.addNewTextRun();
                r.setText(summaryHeaderValues.get(c));
                r.setFontSize(10.0);
                //r.setFontColor(Color.white);
                //th.setFillColor(new Color(79, 129, 189));
                table.setColumnWidth(c, table.getAnchor().getWidth()/summaryHeaders.size());
            }
        	
        }
        //creating a file object
        File file = new File("./empty-output.pptx");
        FileOutputStream out = new FileOutputStream(file);
        
        //saving the changes to a file
        xmlSlideShow.write(out);
        out.close();
	}

}
