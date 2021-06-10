package com.aria.captcha.custom.web.dto;

import java.util.Date;

public class CaptchaChallengeDto {
 
	private String image;
	
	private String sessionReqCode;
	
	private Date waitingTime;
	
	private String reqCode;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getReqCode() {
		return reqCode;
	}

	public void setReqCode(String reqCode) {
		this.reqCode = reqCode;
	}

	public void setSessionReqCode(String sessionReqCode) {
		this.sessionReqCode = sessionReqCode;		
	}

	public String getSessionReqCode() {
		return sessionReqCode;
	}

	public Date getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(Date waitingTime) {
		this.waitingTime = waitingTime;
	}

}
