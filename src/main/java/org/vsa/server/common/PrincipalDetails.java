package org.vsa.server.common;

import org.vsa.server.member.entity.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PrincipalDetails implements UserDetails {

    private Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority(){
            @Override
            public String getAuthority() {
                if(member!=null){
                    return member.getUserRole();
                }
                return "없는 사용자입니다.";
            }
        });


        return collect;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        if(member!=null){
            return member.getSocialId();
        }

        return null;
    }
}
