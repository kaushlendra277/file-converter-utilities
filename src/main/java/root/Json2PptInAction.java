package root;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import root.using.json.simple.and.poi.JsonSimple111;
import root.using.json.simple.and.poi.Poi315Refactoring;

public class Json2PptInAction {

	public static void main(String[] args) {
		String jsonFilePath = "./eda.json";
		try {
			JSONArray jArray = JsonSimple111.fromJsonFile(jsonFilePath);
			JSONObject jObject = (JSONObject)jArray.get(0);
			JSONArray jsonDatas = (JSONArray)jObject.get("json_data");
			JSONObject jsonData = (JSONObject)jsonDatas.get(0);
			JSONArray overviews = (JSONArray)jsonData.get("overview");
			for (Object object : overviews) {
				JSONArray overview = (JSONArray)object;
				System.out.print(overview.get(0).toString() +" - "+overview.get(1));
				System.out.println();
			}
			JSONObject summary = (JSONObject)jsonData.get("summary");
			// Poi315.createEdaPpt(overviews, summary); // use Poi315Refactoring.createEdaReport(overviews, summary);
			Poi315Refactoring.createEdaReport(overviews, summary);
			jsonFilePath = "./ec.json";
			jArray = JsonSimple111.fromJsonFile(jsonFilePath);
			Poi315Refactoring.createEcReport(jArray);
			System.out.println("JSON file read successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
