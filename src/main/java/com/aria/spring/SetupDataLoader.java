package com.aria.spring;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.aria.captcha.custom.persistence.dao.CaptchaChallengeRepository;
import com.aria.captcha.custom.persistence.dao.CaptchaImageRepository;
import com.aria.captcha.custom.persistence.model.CaptchaChallenge;
import com.aria.captcha.custom.persistence.model.CaptchaImage;
import com.aria.captcha.custom.persistence.model.CaptchaResultEnum;

@Component
public class SetupDataLoader {//implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    
    @Autowired
    private CaptchaImageRepository captchaImageRepository;
    
    @Autowired
    private CaptchaChallengeRepository captchaChallengeRepository;

    // API

//    @Override
    @Transactional
    @EventListener
    public void onApplicationEvent(final ContextRefreshedEvent event) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        if (alreadySetup) {
            return;
        }
        
        createCaptchaChallenge();

        alreadySetup = true;
    }

    private void createCaptchaChallenge() throws IOException {
    	//LOAD IMAGES AND CREATE CHALLENGE
    	loadImages();
	}
    
    private void loadImages() throws IOException {
		String[] degrees = {"UP", "UP_RIGHT", "RIGHT","DOWN_RIGHT", "DOWN", "DOWN_LEFT", "LEFT", "UP_LEFT"};
		
		ClassLoader classLoader = getClass().getClassLoader();
        final File imgFolder = new File(classLoader.getResource("img/").getFile());
        File[] imgFiles = imgFolder.listFiles();
        
        int index = 1;
        int rotation = 0;
        for (File fileImage : imgFiles) {
        	String imagePrefix = FilenameUtils.removeExtension(fileImage.getName());
        	String imageExtension = FilenameUtils.getExtension(fileImage.getName());
        	BufferedImage image = ImageIO.read(fileImage);
        	for (String degree : degrees) {
        		
        		byte[] imageRotated = rotateImage(image, imageExtension, rotation);
    			CaptchaImage captchaImage = new CaptchaImage();
    			captchaImage.setName(imagePrefix+"_"+degree+"."+imageExtension);        
    			captchaImage.setImage(Base64.getEncoder().encode(imageRotated));
    			captchaImageRepository.save(captchaImage);
    			
    			//CREATE CHALLANGE RESULTS
    			CaptchaChallenge challange = new CaptchaChallenge();
    			challange.setType(index);
    			challange.setCaptchaImage(captchaImage);
    			challange.setResult(CaptchaResultEnum.valueOf(org.apache.commons.lang3.StringUtils.deleteWhitespace(degree)).getCode());
    			captchaChallengeRepository.save(challange);
    			
    			rotation = rotation + 45;
        	}
        	
        	index++;
		}
    }

	private byte[] rotateImage(BufferedImage image, String extension,  int rotation) throws IOException {
		final double rads = Math.toRadians(rotation);
		final double sin = Math.abs(Math.sin(rads));
		final double cos = Math.abs(Math.cos(rads));
		final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
		final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
		final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		final AffineTransform at = new AffineTransform();
		at.translate(w / 2, h / 2);
		at.rotate(rads,0, 0);
		at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
		final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		rotateOp.filter(image,rotatedImage);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(rotatedImage, extension, baos);
		return baos.toByteArray();
	}
}