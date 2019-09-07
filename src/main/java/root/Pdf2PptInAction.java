package root;

import root.poi.Poi315HelperWithoutCreatinImagesInHardDrive;

public class Pdf2PptInAction {

	public static void main(String[] args) {
		
		String sourcePdfPath = "./reports.pdf"; // Pdf files are read from this folder
		// WAY1 - To convert ppt-> images -> save them to HD -> read images fro HD ->add them to PPT [NOT RECOMMENDED]
		/*
		String destinationDir = "./pdfImages/"; // converted images from pdf document
		List<String> pngImages = Poi315Helper.toPngImages(sourceDir , destinationDir);
		
		try {
			Poi315Helper.ToPpt(destinationDir, pngImages);
			System.out.println("PPT exported successfully");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = new File(destinationDir);
		
		// deleting temp folder of images
		FileUtils.deleteQuietly(file);
		*/
		
		// WAY2 - To convert ppt-> images as BufferedImage-> convert BufferedImage to  Byte array-> add them to PPT [RECOMMENDED]
		// Creates a ned ppt and add slides to it
		Poi315HelperWithoutCreatinImagesInHardDrive.exportNewPptfromPdf(sourcePdfPath);
		
		// Adding slides to KPMG provided template
		String pptExten = ".pptx";
		String pptTemplatePath = "./KPMG-Template"+pptExten;
		// https://poi.apache.org/components/slideshow/xslf-cookbook.html
		// for examples refer https://www.tutorialspoint.com/apache_poi_ppt/apache_poi_ppt_management_slides.htm
		Poi315HelperWithoutCreatinImagesInHardDrive.addPdfToExistingPptTemplate(pptTemplatePath, sourcePdfPath, pptExten);
	}
}
