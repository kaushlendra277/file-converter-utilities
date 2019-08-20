package svg2pdf.root;

import java.io.FileOutputStream;
import java.io.IOException;

public class CreatingAFile {
	public static void main(String[] args) throws IOException {
		String fileData = "Pankaj Kumar";
		FileOutputStream fos = new FileOutputStream("D:\\workdir\\html2pdf\\exportedPdf2.svg");
		fos.write(fileData.getBytes());
		fos.flush();
		fos.close();
	}
}
