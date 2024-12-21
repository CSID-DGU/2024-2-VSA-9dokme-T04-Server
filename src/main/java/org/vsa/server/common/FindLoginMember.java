package org.vsa.server.common;

import org.vsa.server.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class FindLoginMember {

    private static MemberRepository memberRepository;

    @Autowired
    public FindLoginMember(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication != null) && (authentication.getPrincipal() instanceof UserDetails)) {
            String userName = authentication.getName();
            if (memberRepository.existsBySocialId(userName)) {
                String memberEmail = memberRepository.findBySocialId(userName).getSocialId();
                return memberEmail;
            }

        }
        return null;
    }

}
