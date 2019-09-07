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
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class Poi39Helper {

	/*
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
				PDDocument document = PDDocument.load(sourceDir);
				List<PDPage> list = document.getDocumentCatalog().getAllPages();
				System.out.println("Total files to be converted -> " + list.size());

				String fileName = sourceFile.getName().replace(".pdf", "");
				int pageNumber = 1;
				for (PDPage page : list) {
					BufferedImage image = page.convertToImage();
					String imageName = fileName + "_" + pageNumber + ".png";
					File outputfile = new File(destinationDir + imageName);
					System.out.println("Image Created -> " + outputfile.getName());
					ImageIO.write(image, "png", outputfile);
					pngImages.add(imageName);
					pageNumber++;
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
			byte[] pictureByteArray = IOUtils.toByteArray(new FileInputStream(image));
			
			

			// adding the image to the presentation
			// XSLFPictureData.PICTURE_TYPE_PNG = 6 in poi 3.9
			int idx = ppt.addPicture(pictureByteArray, XSLFPictureData.PICTURE_TYPE_PNG);

			// creating a slide with given picture on it
			XSLFPictureShape picture = slide.createPicture(idx);
			 // Set picture position and size
		    picture.setAnchor(new Rectangle(0, 0, 600, 900));

		}
		// creating a file object
		File file = new File("./reports.pptx");
		FileOutputStream out = new FileOutputStream(file);

		// saving the changes to a file
		ppt.write(out);
		System.out.println("image added successfully");
		out.close();
	}
	*/
}
