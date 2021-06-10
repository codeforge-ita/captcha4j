/**
 * 
 */
package com.aria.captcha.custom.web.controller;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Locale;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aria.captcha.custom.persistence.model.CaptchaReq;
import com.aria.captcha.custom.persistence.model.CaptchaSessionReq;
import com.aria.captcha.custom.service.CaptchaCustomService;
import com.aria.captcha.custom.web.dto.CaptchaChallengeDto;
import com.aria.captcha.custom.web.error.ReCaptchaInvalidException;
import com.aria.captcha.custom.web.util.GenericResponse;

/**
 * @author Team ATQM
 *
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {
	
	@Value("${aria.custom.captcha.num-challenge}")
	private int numChallenge;
	
	@Autowired
	private CaptchaCustomService captchaCustomService;
	
	@Autowired
    private MessageSource messageSource;
	
	@GetMapping
	@Transactional
	public CaptchaChallengeDto createCaptchaChallenge(final HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
		CaptchaChallengeDto dto = new CaptchaChallengeDto();
		
		CaptchaSessionReq captchaSessionReq = captchaCustomService.checkCaptchaChallenge(request.getParameter("sessionReqCode"));
		dto.setSessionReqCode(captchaSessionReq.getSessionReqCode());
		dto.setWaitingTime(captchaSessionReq.getWaitingTime());
		
		if (captchaSessionReq.getWaitingTime() != null) {
			return dto;
		}
	
		CaptchaReq req = captchaCustomService.createCaptchaChallenge(captchaSessionReq, getRandomNumberUsingNextInt(1,numChallenge));
				
		dto.setReqCode(req.getReqCode());
		dto.setImage(new String(req.getCaptchaChallenge().getImage().getImage()));
		
		byte[] decodedBytes = Base64.getDecoder().decode(dto.getImage());
		FileUtils.writeByteArrayToFile(new File("image.png"), decodedBytes);
		return dto;
	}
	
	@PostMapping("/validate")
	@Transactional(noRollbackFor = ReCaptchaInvalidException.class)
	public GenericResponse validateCaptchaChallenge(final HttpServletRequest request)  {
		String response = request.getParameter("response");
		String reqCode = request.getParameter("reqCode");
		if (response==null || response.length() == 0) {
			 throw new ReCaptchaInvalidException(messageSource.getMessage("aria.custom.captcha.no-robot", null, Locale.ITALIAN));
		}
		if (captchaCustomService.validateCaptchaChallenge(reqCode, response)) {
			return new GenericResponse("success");
		} else {
			  throw new ReCaptchaInvalidException(messageSource.getMessage("aria.custom.captcha.no-validated", null, Locale.ITALIAN));
		}
		
	}
	
	private int getRandomNumberUsingNextInt(int min, int max) throws NoSuchAlgorithmException {
		final Random rand = SecureRandom.getInstanceStrong();  
	    return rand.nextInt(max - min + 1) + min;
	}
}