/**
 * 
 */
package com.aria.captcha.custom.persistence.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Utente
 *
 */
@Entity
public class CaptchaReq {
	
	public static final int EXPIRATION_MINUTE = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String reqCode;

	@ManyToOne
	@JoinColumn(nullable = false, name = "captcha_challenge_id")
	private CaptchaChallenge captchaChallange;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "captcha_session_req_id")
	private CaptchaSessionReq captchaSessionReq;

	/** Variabile che indica la durata della richiesta*/
	private Date expirationDate;
	
	/** Variabile che indica se il Captcha è stato risolto.*/
	private Boolean resolved = null;
	
	/** Variabile che indica se la richiesta è stata consumata a seguito di risoluzione del Captcha.*/
	private Boolean consumed = false;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the reqCode
	 */
	public String getReqCode() {
		return reqCode;
	}

	/**
	 * @param reqCode the reqCode to set
	 */
	public void setReqCode(String reqCode) {
		this.reqCode = reqCode;
	}

	/**
	 * @return the captcha
	 */
	public CaptchaChallenge getCaptchaChallenge() {
		return captchaChallange;
	}

	/**
	 * @param captcha the captcha to set
	 */
	public void setCaptchaChallenge(CaptchaChallenge captchaChallange) {
		this.captchaChallange = captchaChallange;
	}

	/**
	 * @return the expiration
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expiration the expiration to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	public boolean isConsumed() {
		return consumed;
	}

	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}

	public CaptchaChallenge getCaptchaChallange() {
		return captchaChallange;
	}

	public void setCaptchaChallange(CaptchaChallenge captchaChallange) {
		this.captchaChallange = captchaChallange;
	}

	public CaptchaSessionReq getCaptchaSessionReq() {
		return captchaSessionReq;
	}

	public void setCaptchaSessionReq(CaptchaSessionReq captchaSessionReq) {
		this.captchaSessionReq = captchaSessionReq;
	}

	public Boolean getResolved() {
		return resolved;
	}

	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}

	public Boolean getConsumed() {
		return consumed;
	}

	public void setConsumed(Boolean consumed) {
		this.consumed = consumed;
	}
}
