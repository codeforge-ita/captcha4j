package com.aria.captcha.custom.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aria.captcha.custom.persistence.model.CaptchaSessionReq;

public interface CaptchaSessionReqRepository extends JpaRepository<CaptchaSessionReq, Long> {

	CaptchaSessionReq findBySessionReqCode(String sessionReqCode);
	
	CaptchaSessionReq findBySessionReqCodeAndConsumedFalse(String sessionReqCode);

}