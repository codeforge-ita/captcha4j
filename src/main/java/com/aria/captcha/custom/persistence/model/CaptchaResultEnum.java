/**
 * 
 */
package com.aria.captcha.custom.persistence.model;

/**
 * @author Utente
 *
 */
public enum CaptchaResultEnum {
	
	UP("UP"), UP_RIGHT("UP-RIGHT"), UP_LEFT("UP-LEFT"),
	RIGHT("RIGHT"), LEFT("LEFT"),
	DOWN("DOWN"), DOWN_RIGHT("DOWN-RIGHT"), DOWN_LEFT("DOWN-LEFT");
	
	private String code;
	
	private CaptchaResultEnum(final String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
}
