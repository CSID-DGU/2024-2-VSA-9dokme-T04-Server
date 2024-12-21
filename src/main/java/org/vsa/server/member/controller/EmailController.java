package org.vsa.server.member.controller;

import org.vsa.server.member.dto.request.MailDto;
import org.vsa.server.member.service.EmailService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
public class EmailController {
    @Autowired
    private EmailService emailService;
    @PostMapping("/mail")
    public String execMail(@RequestBody @Valid MailDto dto) {

        emailService.sendSimpleMessage(dto);
        return "전송 완료";
    }
}
