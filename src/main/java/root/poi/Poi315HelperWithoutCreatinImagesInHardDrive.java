package root.poi;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;

public class Poi315HelperWithoutCreatinImagesInHardDrive {

	public static void exportNewPptfromPdf(String sourcePdfPath) {
		try {
			File sourceFile = new File(sourcePdfPath);
			if (sourceFile.exists()) {

				try (PDDocument document = PDDocument.load(new File(sourcePdfPath));
						XMLSlideShow ppt = new XMLSlideShow();
						FileOutputStream out = new FileOutputStream(new File("./reports.pptx"));) {
					// all action happens here 
					exportPptfromPdfInternal(document, ppt, false);
					
					// saving the changes to a file
					ppt.write(out);
				}catch(Exception e) {
					e.printStackTrace();
				}
			} else {
				System.err.println(sourceFile.getName() + " File not exists");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ppt expoted from Poi315HelperWithoutCreatinImagesInHardDrive");
	}

	private static void exportPptfromPdfInternal(PDDocument sourcePdf, XMLSlideShow targetPpt, boolean modifyingExisting) {
		PDPageTree pages = sourcePdf.getPages();
		int numOfPages = pages.getCount();
		for (int i = 0; i < numOfPages; i++) {
			try (PDDocument pdDocument = new PDDocument();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
				// reading each page of pdf as image
				pdDocument.addPage(pages.get(i));
				PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
				BufferedImage image = pdfRenderer.renderImage(0);

				// converting BufferedImage into a byte array
				ImageIO.write(image, "png", baos);
				baos.flush();
				byte[] pictureByteArray = baos.toByteArray();
				// creating new slide in the ppt
				XSLFSlide slide = null;
				if(modifyingExisting) {
					//  we get this name by right clicking .potx doc -> Layout
					// for referernce visit https://poi.apache.org/components/slideshow/xslf-cookbook.html
					String layoutName  = "Title Only";
					XSLFSlideLayout titleOnly = targetPpt.getSlideMasters().get(0).getLayout(layoutName);
					/*
					for(XSLFSlideLayout xslfSlideLayout: l) {
						System.out.println(xslfSlideLayout.getName());
						//System.out.println(xslfSlideLayout.getType());
					}
					*/
					// creating new slide in the ppt
					slide = targetPpt.createSlide(titleOnly);
				}else {
					slide = targetPpt.createSlide();
				}
				
				// adding the image to the presentation
				XSLFPictureData pictureData = targetPpt.addPicture(pictureByteArray, PictureType.PNG);
				// creating a slide with given picture on it
				XSLFPictureShape picture = slide.createPicture(pictureData);
				// Set picture position and size
				picture.setAnchor(new Rectangle(70, 30, 500, 450));
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			// targetPpt.removeSlide(1);
			// targetPpt.removeSlide(1);
		}
	}
	
	public static void addPdfToExistingPptTemplate(String pptTemplatePath, String sourcePdfPath, String pptExten) {
		try {
			File sourceFile = new File(sourcePdfPath);
			if (sourceFile.exists()) {

				try (PDDocument document = PDDocument.load(new File(sourcePdfPath));
						XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(pptTemplatePath));
						FileOutputStream out = new FileOutputStream(new File("./reports-kpmg-template"+pptExten));) {
					
					// setting page size to beautify ppt slide
					
					//ppt.setPageSize(new Dimension(800, 1000));
					
					// removing blank page from index 1 
					ppt.removeSlide(1);
					
					// all action happens here 
					exportPptfromPdfInternal(document, ppt, true);
					
					// re ordering end slide to the last
					List<XSLFSlide> slides = ppt.getSlides();
					XSLFSlide slide = slides.get(1);
					ppt.setSlideOrder(slide, slides.size()-1);
					
					// saving the changes to a file
					ppt.write(out);
				}catch(Exception e) {
					e.printStackTrace();
				}
			} else {
				System.err.println(sourceFile.getName() + " File not exists");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ppt template modified from Poi315HelperWithoutCreatinImagesInHardDrive");
	}
}