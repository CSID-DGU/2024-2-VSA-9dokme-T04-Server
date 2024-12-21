package org.vsa.server.inquiring.controller;

import org.vsa.server.common.FindLoginMember;
import org.vsa.server.inquiring.dto.request.InquireRequestDto;
import org.vsa.server.inquiring.dto.response.InquireDto;
import org.vsa.server.inquiring.entity.Inquire;
import org.vsa.server.inquiring.service.InquireService;
import org.vsa.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class InquireController {
    @Autowired
    private InquireService inquireService;

    @Autowired
    private MemberRepository memberRepository;


    @PostMapping("/inquire")
    public ResponseEntity<Inquire> createInquire(@RequestBody InquireRequestDto inquireRequest) {

        String memberEmail = FindLoginMember.getCurrentUserId();
        Long memberId = memberRepository.findBySocialId(memberEmail).getMemberId();

        Inquire createdInquire = inquireService.createInquire(inquireRequest, memberId);
        return new ResponseEntity<>(createdInquire, HttpStatus.CREATED);
    }

    @GetMapping("/admin/inquiries/{pageNo}")
    public Page<InquireDto> getInquireList(@PathVariable int pageNo){
        return inquireService.getInquireList(pageNo);
    }

    @DeleteMapping("/admin/inquiries/delete/{inquireId}")
    public ResponseEntity<Void> deleteInquire(@PathVariable Long inquireId) {
        try {
            inquireService.deleteInquire(inquireId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            log.error("Error deleting inquire with ID " + inquireId, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
