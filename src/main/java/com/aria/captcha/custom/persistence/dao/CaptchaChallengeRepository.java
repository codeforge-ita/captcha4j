package com.aria.captcha.custom.persistence.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aria.captcha.custom.persistence.model.CaptchaChallenge;

public interface CaptchaChallengeRepository extends JpaRepository<CaptchaChallenge, Long> {
	
	@Query("SELECT c FROM CaptchaChallenge c WHERE c.type = ?1 order by function('RAND')")
	List<CaptchaChallenge> findRandomCaptchaChallenge(Integer type);

}