package root.poi;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class Poi315Helper {

	public static List<String> toPngImages(String sourceDir, String destinationDir) {
		List<String> pngImages = new ArrayList<String>();
		try {
			File sourceFile = new File(sourceDir);
			File destinationFile = new File(destinationDir);
			if (!destinationFile.exists()) {
				destinationFile.mkdir();
				System.out.println("Folder Created -> " + destinationFile.getAbsolutePath());
			}
			if (sourceFile.exists()) {
				System.out.println("Images copied to Folder: " + destinationFile.getName());
				PDDocument document = PDDocument.load(new File(sourceDir));
				PDPageTree pages =document.getPages();
				int numOfPages = pages.getCount();
				System.out.println("Total files to be converted -> " + numOfPages);

				String fileName = sourceFile.getName().replace(".pdf", "");
				int pageNumber = 1;
				//for (PDPage page : list) {
				for(int i = 0; i<numOfPages; i++) {
					PDDocument pdDocument = new PDDocument();
					pdDocument.addPage(pages.get(i));
					PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
					BufferedImage image = pdfRenderer.renderImage(0);
					String imageName = fileName + "_" + pageNumber + ".png";
					File outputfile = new File(destinationDir + imageName);
					System.out.println("Image Created -> " + outputfile.getName());
					ImageIO.write(image, "png", outputfile);
					pngImages.add(imageName);
					pageNumber++;
					pdDocument.close();
				}
				document.close();
				System.out.println("Converted Images are saved at -> " + destinationFile.getAbsolutePath());
			} else {
				System.err.println(sourceFile.getName() + " File not exists");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pngImages;
	}

	public static void ToPpt(String destinationDir, List<String> pngImages) throws FileNotFoundException, IOException {
		// creating a presentation
		XMLSlideShow ppt = new XMLSlideShow();

		for (String pngImage : pngImages) {

			// creating a slide in it
			XSLFSlide slide = ppt.createSlide();
			

			// reading an image
			File image = new File(destinationDir + pngImage);

			// converting it into a byte array
			FileInputStream imageIS =  new FileInputStream(image);
			byte[] pictureByteArray = IOUtils.toByteArray(imageIS);
			
			

			// adding the image to the presentation
			XSLFPictureData pictureData = ppt.addPicture(pictureByteArray, PictureType.PNG);

			// creating a slide with given picture on it
			XSLFPictureShape picture = slide.createPicture(pictureData);
			 // Set picture position and size
		    picture.setAnchor(new Rectangle(0, 0, 600, 500));
		    imageIS.close();
		}
		// creating a file object
		File file = new File("./reports.pptx");
		FileOutputStream out = new FileOutputStream(file);

		// saving the changes to a file
		ppt.write(out);
		System.out.println("image added successfully");
		out.close();
		ppt.close();
	}
}
