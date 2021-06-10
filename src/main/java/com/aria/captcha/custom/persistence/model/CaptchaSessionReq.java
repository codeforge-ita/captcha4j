/**
 * 
 */
package com.aria.captcha.custom.persistence.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * @author Utente
 *
 */
@Entity
public class CaptchaSessionReq {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String sessionReqCode;
	
	/**Il numero massimo di Captcha Challange che si possono sbagliare per singola sessione.*/
	private Integer maxReq;
	
	/**Il tempo di attesa prima di poter riprovare.*/
	private Date waitingTime;
	
	/**Il numero di Captcha Challange consumati/errati per SessionReq*/
	private Integer reqConsumed = 0;
	
	/**Marcatore che viene impostato a TRUE nel momento in cui almeno una Captcha Challage viene risolta*/
	private Boolean consumed = Boolean.FALSE;

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

	public String getSessionReqCode() {
		return sessionReqCode;
	}

	public void setSessionReqCode(String sessionReqCode) {
		this.sessionReqCode = sessionReqCode;
	}

	public Integer getMaxReq() {
		return maxReq;
	}

	public void setMaxReq(Integer maxReq) {
		this.maxReq = maxReq;
	}

	public Date getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(Date waitingTime) {
		this.waitingTime = waitingTime;
	}

	public Integer getReqConsumed() {
		return reqConsumed;
	}

	public void setReqConsumed(Integer reqConsumed) {
		this.reqConsumed = reqConsumed;
	}

	/**
	 * @return the consumed
	 */
	public Boolean getConsumed() {
		return consumed;
	}

	/**
	 * @param consumed the consumed to set
	 */
	public void setConsumed(Boolean consumed) {
		this.consumed = consumed;
	}
}
