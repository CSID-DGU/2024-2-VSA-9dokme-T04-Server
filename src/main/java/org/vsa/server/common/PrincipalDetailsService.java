package org.vsa.server.common;

import org.vsa.server.member.entity.Member;
import org.vsa.server.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public PrincipalDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findBySocialId(username);

        if(member == null){
            return new PrincipalDetails(member);
        }
        else{
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

    }
}
