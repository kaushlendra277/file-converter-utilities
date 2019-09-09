package string2pdfIText.root.hmk;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
 
public class ITextTableBuilder {
     // create table
    public static PdfPTable createProjectOverviewTable() throws DocumentException {
 
        // create 6 column table
        PdfPTable table = new PdfPTable(2);
 
        // set the width of the table to 100% of page
        table.setWidthPercentage(100);
 
        // set relative columns width
        table.setWidths(new float[]{1f,3f});
 
        // ----------------Table Header "Title"----------------
        // font
        Font font = new Font(FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
        // create header cell
        PdfPCell cell = new PdfPCell(new Phrase("Project Overview",font));
        // set Column span "1 cell = 6 cells width"
        cell.setColspan(2);
        // set style
        ITextStyleHelper.headerCellStyle(cell);
        // add to table
        table.addCell(cell);
 
        //-----------------Table Cells Label/Value------------------
 
        // 1st Row
        table.addCell(createLabelCell("Project Name"));
        table.addCell(createValueCell("Nemeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"));
        // 2nd Row
        table.addCell(createLabelCell("Project Description"));
        table.addCell(createValueCell("Descriptionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"));
        return table;
    }
 
    // create cells
    private static PdfPCell createLabelCell(String text){
        // font
        Font font = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.DARK_GRAY);
 
        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text,font));
 
        // set style
        ITextStyleHelper.labelCellStyle(cell);
        return cell;
    }
 
    // create cells
    private static PdfPCell createValueCell(String text){
        // font
        Font font = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
 
        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text,font));
 
        // set style
        ITextStyleHelper.valueCellStyle(cell);
        return cell;
    }
}