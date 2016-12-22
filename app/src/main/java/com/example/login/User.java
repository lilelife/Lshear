package com.example.login;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ”√ªß¿‡
 * @author dell
 *
 */
public class User {
	private String mId;
	private String mPwd;
	private static final String JSON_ID = "user_id";
	private static final String JSON_PWD = "user_pwd";

	public User(String id, String pwd) {
		this.mId = id;
		this.mPwd = pwd;
	}

	public User(JSONObject json) throws Exception {
		if (json.has(JSON_ID)) {
			String id = json.getString(JSON_ID);
			String pwd = json.getString(JSON_PWD);
			
		}
	}

	public JSONObject toJSON() throws Exception {
		
		JSONObject json = new JSONObject();
		try {
			json.put(JSON_ID, mId);
			json.put(JSON_PWD, mPwd);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public String getId() {
		return mId;
	}

	public String getPwd() {
		return mPwd;
	}
	
	
	
}
