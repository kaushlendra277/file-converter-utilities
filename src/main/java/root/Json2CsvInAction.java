package root;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Json2CsvInAction {

	public static void main(String[] args) throws IOException, ParseException {
		String file = "D:\\workdir\\IIAW-23-08-2019\\service2-poc\\json2csv\\mitigate_bias.json";
		JSONArray jsonArray = JsonUtils.fromJsonFile(file);
		exportCsv(jsonArray);
		System.out.println("CSV file exported.......");
	}

	private static void exportCsv(JSONArray jsonArray) throws FileNotFoundException {
		String base64EncodedCsv = JsonUtils.getBase64EncodedStringForCsvContent(jsonArray, "transformed_dataset");
		// Decode base 64
		byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedCsv);
		String decodedCsvString = new String(decodedBytes);
		// System.out.println("decodedBytes " + new String(decodedBytes));
		try (PrintWriter writer = new PrintWriter(new File("D:\\workdir\\IIAW-23-08-2019\\service2-poc\\json2csv\\test.csv"))) {
			writer.write(decodedCsvString);
		}
	}
}

class JsonUtils {

	public static JSONArray fromJsonFile(String file) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		FileReader jsonFileReader = new FileReader(file);
		JSONArray jsonArray = (JSONArray) parser.parse(jsonFileReader);
		return jsonArray;
	}

	public static String getBase64EncodedStringForCsvContent(JSONArray jsonArray, String fieldKey) {
		String fieldKeyString = null;
		outerloop: for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			Set<String> jsonObjectKeys = jsonObject.keySet();
			for (String key : jsonObjectKeys) {
				JSONObject keyJsonObject = (JSONObject) jsonObject.get(key);
				if (keyJsonObject.containsKey(fieldKey)) {
					fieldKeyString = keyJsonObject.get(fieldKey).toString();
					break outerloop;
				} else {
					System.out.println("key " + fieldKey + " not found");
				}
				Map<String, Object> map = new HashMap<>();
				map.put(key, keyJsonObject);
			}
		}
		return fieldKeyString;
	}
}
