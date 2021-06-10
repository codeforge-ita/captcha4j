package com.aria.captcha.custom.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aria.captcha.custom.persistence.dao.CaptchaChallengeRepository;
import com.aria.captcha.custom.persistence.dao.CaptchaReqRepository;
import com.aria.captcha.custom.persistence.dao.CaptchaSessionReqRepository;
import com.aria.captcha.custom.persistence.model.CaptchaChallenge;
import com.aria.captcha.custom.persistence.model.CaptchaReq;
import com.aria.captcha.custom.persistence.model.CaptchaSessionReq;
import com.aria.captcha.custom.web.error.ReCaptchaInvalidException;

@Service("captchaCustomService")
public class CaptchaCustomService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CaptchaCustomService.class);
    
    @Value( "${aria.custom.captcha.max-request}" )
    private int maxReq;
    
    @Value( "${aria.custom.captcha.waiting-time}" )
    private long waitingTime;
    
    @Value( "${aria.custom.captcha.waiting-time.unit}" )
    private String waitingTimeUnit;
    
    @Value( "${aria.custom.captcha.exipration-time}" )
    private long expirationTime;
    
    @Value( "${aria.custom.captcha.exipration-time.unit}" )
    private String expirationTimeUnit;
    
    @Autowired
	private CaptchaChallengeRepository captchaChallegeRepository;
    
    @Autowired
    private CaptchaReqRepository captchaReqRepository;
    
    @Autowired
    private CaptchaSessionReqRepository captchaSessionReqRepository;
    
    public CaptchaSessionReq checkCaptchaChallenge(String sessionReqCode) {
    	CaptchaSessionReq captchaSessionReq = null;
    	/*Associamo la richiesta ad un SessionReq*/
    	if(StringUtils.isBlank(sessionReqCode)) {
    		captchaSessionReq = new CaptchaSessionReq();
    		captchaSessionReq.setSessionReqCode(UUID.randomUUID().toString());
    		/*Questo valore deve essere configurabile*/
    		captchaSessionReq.setMaxReq(maxReq);
    	} else {
    		captchaSessionReq = captchaSessionReqRepository.findBySessionReqCodeAndConsumedFalse(sessionReqCode);
    	}
    	
    	if ((captchaSessionReq.getReqConsumed()) >= captchaSessionReq.getMaxReq()) {
    		//Se abbiamo raggiunto il massimo numero di richieste sbagliate per client controlliamo se è trascorso il tempo di attesa.
    		if(captchaSessionReq.getWaitingTime() == null) {
    			ChronoUnit[] values = ChronoUnit.values();
    			captchaSessionReq.setWaitingTime(Date.from(Instant.now().plus(waitingTime, ChronoUnit.valueOf(waitingTimeUnit))));
    		} else if ((captchaSessionReq.getWaitingTime().toInstant().isBefore(Instant.now()))) {
    			captchaSessionReq.setWaitingTime(null);
    		}
    	}
    	captchaSessionReq.setReqConsumed(captchaSessionReq.getReqConsumed()+1);
    	
    	captchaSessionReqRepository.save(captchaSessionReq);
    	
    	return captchaSessionReq;
    }
    
    public CaptchaReq createCaptchaChallenge(CaptchaSessionReq captchaSessionReq, Integer typeChallenge) {
    	/*Recuperiamo in maniera Casuale una CHALLANGE*/
    	
    	List<CaptchaChallenge> captchaChallenges = captchaChallegeRepository.findRandomCaptchaChallenge(typeChallenge);
    	CaptchaChallenge randomChallange = captchaChallenges.get(0);
    	
    	/*L'associamo ad una richiesta*/
    	CaptchaReq req = new CaptchaReq();
    	req.setCaptchaChallenge(randomChallange);
    	ChronoUnit[] values = ChronoUnit.values();
    	//Si ha tempo 2 minuti per completare la challenge// questo tempo deve essere configurabile
    	req.setExpirationDate(Date.from(Instant.now().plus(expirationTime, ChronoUnit.valueOf(expirationTimeUnit))));
    	req.setReqCode(UUID.randomUUID().toString());
    	req.setCaptchaSessionReq(captchaSessionReq);
    	captchaReqRepository.save(req);
    	
    	captchaSessionReqRepository.save(captchaSessionReq);
    	
    	return req;
    }
    
	public boolean validateCaptchaChallenge(String reqCode, String response) {
		CaptchaReq captchaReq = captchaReqRepository.findByReqCodeAndResolvedIsNullAndConsumedFalse(reqCode);

		if (captchaReq != null) {
			if (captchaReq.getExpirationDate().toInstant().isBefore(Instant.now())) {
				// Il captcha è scaduto.
				throw new ReCaptchaInvalidException("Il Captcha Aria è scaduto, ricaricalo.");
			}

			CaptchaChallenge challange = captchaReq.getCaptchaChallenge();
			if (challange.getResult().equalsIgnoreCase(response)) {
				captchaReq.setResolved(Boolean.TRUE);

				captchaReqRepository.save(captchaReq);
				return Boolean.TRUE;
			}
			
			/**Se il CAPTCHA non e' stato risolto lo marchiamo come non risolto e come consumato.*/
			captchaReq.setResolved(Boolean.FALSE);
			captchaReq.setConsumed(Boolean.TRUE);
			captchaReqRepository.save(captchaReq);
		} 
		return Boolean.FALSE;
	}
	
	/**
	 * Questo medtodo consente di consumare una CaptchaChallenge e di marcarla come completata e potenzialmente eliminabile.
	 * 
	 * @param reqCode
	 * @param response
	 * @return
	 */
	public boolean consumeCaptchaChallenge(String reqCode, String response) {
		CaptchaReq captchaReq = captchaReqRepository.findByReqCodeAndResolvedTrueAndConsumedFalse(reqCode);
		if (captchaReq != null) {
			captchaReq.setConsumed(Boolean.TRUE);
			captchaReqRepository.save(captchaReq);
			
			CaptchaSessionReq captchaSessionReq = captchaReq.getCaptchaSessionReq();
			captchaSessionReq.setConsumed(Boolean.TRUE);
			captchaSessionReqRepository.save(captchaSessionReq);
			
			return Boolean.TRUE;
		} 
		return Boolean.FALSE;
	}
}
