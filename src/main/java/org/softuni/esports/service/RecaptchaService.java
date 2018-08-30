package org.softuni.esports.service;

public interface RecaptchaService {
    String verifyRecaptcha(String userIpAddress, String gRecaptchaResponse);
}
