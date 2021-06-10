package com.aria.captcha.custom.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aria.captcha.custom.persistence.model.CaptchaReq;

public interface CaptchaReqRepository extends JpaRepository<CaptchaReq, Long> {
	
	public CaptchaReq findByReqCodeAndResolvedIsNullAndConsumedFalse(String reqCode);

	public CaptchaReq findByReqCodeAndResolvedTrueAndConsumedFalse(String reqCode);

}