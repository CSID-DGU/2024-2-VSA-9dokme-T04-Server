package org.vsa.server.member.service;

import org.vsa.server.member.dto.request.MailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${google.email}")
    private String FROM_ADDRESS;
    @Autowired
    private JavaMailSender emailSender;
    public void sendSimpleMessage(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_ADDRESS);
        message.setTo(mailDto.getEmailAddr());
        message.setSubject(mailDto.getEmailTitle());
        message.setText(mailDto.getEmailContent());
        emailSender.send(message);
    }
}
