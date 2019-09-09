package root.model;

import java.util.List;

import org.json.simple.JSONObject;

public class KpmgPdf {

	private List<List<String>> overview;
	
	private List<JSONObject> summary;

	public List<List<String>> getOverview() {
		return overview;
	}

	public void setOverview(List<List<String>> overview) {
		this.overview = overview;
	}

	public List<JSONObject> getSummary() {
		return summary;
	}

	public void setSummary(List<JSONObject> summary) {
		this.summary = summary;
	}
}
