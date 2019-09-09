package root.Itext7core.hmk;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
public class IText7CoreHelper {

	private IText7CoreHelper() {
	}

	public static Table addHeader(String heading) throws IOException {
		Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
		table.addCell(addHeaderCell(heading));
		return table;
	}

	public static Table addProjectOverview() throws IOException {
		Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
		table.addCell(addLabelCell("Name"));
		table.addCell(addValueCell("projectName"));
		table.addCell(addLabelCell("Description"));
		table.addCell(addValueCell("projectDescription"));
		table.addCell(addLabelCell("Created By"));
		table.addCell(addValueCell("createdBy"));
		return table;
	}
	
	public static Table addDataOverview(JSONArray overview) throws IOException {
        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
 
        for (Object object : overview) {
			JSONArray overviewValues = (JSONArray)object;
			table.addCell(addLabelCell(overviewValues.get(0).toString()));
			table.addCell(addValueCell(overviewValues.get(1).toString()));
		}
        return table;
    }
	
	public static Table addDataSummary(JSONObject summary) throws IOException {
    	Table table = new Table(UnitValue.createPercentArray(7)).useAllAvailableWidth();
        // ----------------Data Summary Table Header "Title"----------------
        // font
    	List<String> dataSummaryHeaders = Stream
    			.of("","MEAN", "STD DEV", "ZEROS", "MIN", "MEDIAN", "MAX")
    			.collect(Collectors.toList());
    	for (String header : dataSummaryHeaders) {
        	table.addCell(addHeaderCell(header));
		}
        
    	// Data Summary table content
        Set<String> summaryKeys = (Set<String>) summary.keySet();
        for (String key : summaryKeys) {
			org.json.simple.JSONArray row = (org.json.simple.JSONArray)summary.get(key);
			Map<String, String> rowMapper = new HashMap<>();
			for(Object object : row) {
				org.json.simple.JSONArray jRowArr = (org.json.simple.JSONArray)object;
				rowMapper.put(jRowArr.get(0).toString(), jRowArr.get(1).toString());
			}
			table.addCell(addLabelCell(key));
			table.addCell(addValueCell(rowMapper.getOrDefault("mean", "")));
		    table.addCell(addValueCell(rowMapper.getOrDefault("std", "")));
		    table.addCell(addValueCell(rowMapper.getOrDefault("zeros", "")));
		    table.addCell(addValueCell(rowMapper.getOrDefault("min", "")));
		    table.addCell(addValueCell(rowMapper.getOrDefault("median", "")));
		    table.addCell(addValueCell(rowMapper.getOrDefault("max", "")));
		}
        
        return table;
    }

	private static Cell addHeaderCell(String heading) throws IOException {
		PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
		Cell headingCell = new Cell()
						.add(new Paragraph(heading)
						.setFont(font)
						.setFontColor(ColorConstants.WHITE));
		headingCell.setBackgroundColor(ColorConstants.BLUE);
		headingCell.setBorder(Border.NO_BORDER);
		headingCell.setTextAlignment(TextAlignment.CENTER);
		return headingCell;
	}
	
	private static Cell addLabelCell(String label) throws IOException {
		PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
		Cell labelCell = new Cell()
								.add(new Paragraph(label)
								.setFont(font)
								.setFontColor(ColorConstants.BLACK));
		labelCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
		labelCell.setBorder(Border.NO_BORDER);
		labelCell.setTextAlignment(TextAlignment.CENTER);
		return labelCell;
	}
	
	private static Cell addValueCell(String label) throws IOException {
		PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		Cell labelCell = new Cell()
								.add(new Paragraph(label)
								.setFont(font)
								.setFontColor(ColorConstants.BLACK));
		labelCell.setBackgroundColor(ColorConstants.WHITE);
		labelCell.setBorder(Border.NO_BORDER);
		labelCell.setTextAlignment(TextAlignment.LEFT);
		return labelCell;
	}

}
