package org.vsa.server.inquiring.service;

import org.vsa.server.inquiring.dto.request.InquireRequestDto;
import org.vsa.server.inquiring.dto.response.InquireDto;
import org.vsa.server.inquiring.entity.Inquire;
import org.vsa.server.inquiring.repository.InquireRepository;
import org.vsa.server.member.entity.Member;
import org.vsa.server.member.repository.MemberRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Data
@NoArgsConstructor
@Service
public class InquireService {
    @Autowired
    private InquireRepository inquireRepository;
    @Autowired
    private MemberRepository memberRepository;


    public Inquire createInquire(InquireRequestDto inquireRequest, Long memberId) {
        Inquire inquire = new Inquire();

        Member member = memberRepository.findByMemberId(memberId);

        inquire.setTitle(inquireRequest.getTitle());
        inquire.setContent(inquireRequest.getContent());
        inquire.setUserId(member.getMemberId());
        return inquireRepository.save(inquire);
    }

    public Page<InquireDto> getInquireList(int pageNo)
    {
        Pageable pageable = PageRequest.of(pageNo,10);
        Page<Inquire> inquireList = inquireRepository.findAll(pageable);

        Page<InquireDto> InquireDtoPage = inquireList.map(inquire -> new InquireDto(
                inquire.getInquireId(),
                inquire.getUserId(),
                inquire.getTitle(),
                inquire.getContent()));

        return InquireDtoPage;
    }

    public void deleteInquire(Long inquireId){
        if (inquireRepository.existsById(inquireId)) {
            inquireRepository.deleteById(inquireId);
        } else {
            throw new RuntimeException("Inquire with ID " + inquireId + " not found");
        }
    }
}
