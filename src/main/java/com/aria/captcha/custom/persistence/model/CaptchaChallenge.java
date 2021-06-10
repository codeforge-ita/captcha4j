package com.aria.captcha.custom.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CaptchaChallenge {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * Identifica tutte le captcha challenges dello stesso tipo
	 */
	@Column(nullable = false, columnDefinition = "integer default 0")
	private Integer type = 0;

	@ManyToOne
	@JoinColumn(nullable = false, name = "captcha_image_id")
	private CaptchaImage image;
	
//	@Lob
//	private byte[] result;
	
	private String result;

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(CaptchaImage image) {
		this.image = image;
	}

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
	 * @return the image
	 */
	public CaptchaImage getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setCaptchaImage(CaptchaImage image) {
		this.image = image;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

}
