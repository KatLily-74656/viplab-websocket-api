package de.uni_stuttgart.tik.viplab.websocket;

import org.json.JSONException;
import org.json.JSONObject;

public class TestJSONMessageProvider {
	public static JSONObject getComputationTemplate() {
		JSONObject computationTemplate = new JSONObject();
		return computationTemplate;
	}

	public static JSONObject getComputationTask() {
		JSONObject computationTask = new JSONObject();
		return computationTask;
	}

	/**
	 * 
	 * @param computationTemplate
	 *            the Base64 encoded computation template
	 * @param computationTask
	 *            the computation task
	 * @return
	 */
	public static JSONObject getCreateComputationMessage(String computationTemplate, JSONObject computationTask) {
		try {
			JSONObject createComputationMessage = new JSONObject();
			createComputationMessage.put("type", "create-computation");
			JSONObject composition = new JSONObject();
			composition.put("template", computationTemplate);
			composition.put("task", computationTask);
			createComputationMessage.put("content", composition);
			return createComputationMessage;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param jwt
	 *            the jwt string
	 * @return
	 */
	public static JSONObject getAuthenticationMessage(String jwt) {
		try {
			JSONObject authenticateMessage = new JSONObject();
			authenticateMessage.put("type", "authenticate");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("jwt", jwt);
			authenticateMessage.put("content", jsonObject);
			return authenticateMessage;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
