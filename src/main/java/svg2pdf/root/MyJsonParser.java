package svg2pdf.root;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MyJsonParser {

	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		
		try {
			FileReader jsonFileReader = new FileReader("D:\\workdir\\svg2pdf\\model_metrics.json");
			JSONArray jsonArray = (JSONArray) parser.parse(jsonFileReader);
			JSONObject json0 = ((JSONObject)jsonArray.get(0));
			JSONArray logisticRegression = (JSONArray)json0.get("Logistic Regression");
			JSONObject logisticRegression0 = ((JSONObject)logisticRegression.get(0)); 
			Collection<String> values =logisticRegression0.values();
			values.removeIf(ele -> !ele.startsWith("<html>"));
			System.out.println(values.size());
			values.forEach(e -> System.out.println(e));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
