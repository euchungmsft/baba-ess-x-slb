package com.eg.baba.cms.alert;

import java.io.Serializable;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class CMSAlertMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String expression;
	String curValue;
	String unit;
	String levelDescription;
	String metricProject;
	String userId;
	Map<String, String> dimensions;
	String evaluationCount;
	String period;
	String metricName;
	String alertName;
	
	String signature;
	String namespace;
	String timestamp;
	String alertState;

	public CMSAlertMessage() {
		super();
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getCurValue() {
		return curValue;
	}

	public void setCurValue(String curValue) {
		this.curValue = curValue;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getLevelDescription() {
		return levelDescription;
	}

	public void setLevelDescription(String levelDescription) {
		this.levelDescription = levelDescription;
	}

	public String getMetricProject() {
		return metricProject;
	}

	public void setMetricProject(String metricProject) {
		this.metricProject = metricProject;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, String> getDimensions() {
		return dimensions;
	}

	public void setDimensions(Map<String, String> dimensions) {
		this.dimensions = dimensions;
	}

	public String getEvaluationCount() {
		return evaluationCount;
	}

	public void setEvaluationCount(String evaluationCount) {
		this.evaluationCount = evaluationCount;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public String getAlertName() {
		return alertName;
	}

	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getAlertState() {
		return alertState;
	}

	public void setAlertState(String alertState) {
		this.alertState = alertState;
	}

	public static CMSAlertMessage fromString(String str) {
		// TODO Auto-generated method stub

		Gson gson = new Gson();
		
		String alertName = "";
		
		Hashtable msg = new Hashtable();   
		StringTokenizer st = new StringTokenizer(str, "&");
		for(; st.hasMoreTokens();) {
			String tok = st.nextToken();
			//System.out.println(tok);
			
			String[] toks = tok.split("=");
			//System.out.println(toks[0]+" = "+URLDecoder.decode(toks[1]));
			
			String valu = URLDecoder.decode(toks[1]);
			if(toks[0].equals("dimensions")) {
				try {
					Map map = gson.fromJson(valu, Map.class);
					msg.put(toks[0], map);
				}
				catch(JsonSyntaxException ex) {
					//	ignore the exception and skip
				}
				continue;
			}
			if(toks[0].equals("time")) {
				msg.put("timestamp", valu);
				continue;
			}
			
			msg.put(toks[0], valu);
		}
		
		gson = new GsonBuilder().setPrettyPrinting().create();
		
		StringWriter sw = new StringWriter(); 
		gson.toJson(msg, sw);
		//System.out.println(sw);
		
		CMSAlertMessage ret = gson.fromJson(sw.toString(), CMSAlertMessage.class);
		//gson.toJson(ret, System.out);
		
		return ret;
	}

	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		Gson gson = new GsonBuilder().create();
		gson.toJson(this, sw);
		
		return sw.toString();
	}

}
