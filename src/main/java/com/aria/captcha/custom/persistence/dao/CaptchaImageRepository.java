package com.aria.captcha.custom.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aria.captcha.custom.persistence.model.CaptchaImage;

public interface CaptchaImageRepository extends JpaRepository<CaptchaImage, Long> {

}