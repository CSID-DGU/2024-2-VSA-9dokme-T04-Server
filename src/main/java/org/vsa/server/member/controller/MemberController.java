package org.vsa.server.member.controller;

import org.vsa.server.common.FindLoginMember;
import org.vsa.server.common.dto.BaseResponse;
import org.vsa.server.common.dto.ErrorResponse;
import org.vsa.server.common.dto.SuccessResponse;
import org.vsa.server.member.JwtUtil;
import org.vsa.server.member.dto.response.MainPageDto;
import org.vsa.server.member.dto.response.MemberDto;
import org.vsa.server.member.dto.response.PostWrittenDto;
import org.vsa.server.member.entity.Member;
import org.vsa.server.member.repository.MemberRepository;
import org.vsa.server.member.service.KakaoService;
import org.vsa.server.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class MemberController {


    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/oauth")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인 GET")
    public BaseResponse kakaoLogin(@RequestParam String code) {
        String accessToken = kakaoService.getKakaoAccessToken(code);
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        if (accessToken == null) {
            return ErrorResponse.of("로그인 실패", HttpStatus.UNAUTHORIZED);
        }

        if (!memberRepository.existsBySocialId(userInfo.get("email").toString())) {
            kakaoService.registerMember(String.valueOf(userInfo.get("email")), String.valueOf(userInfo.get("nickname")));
        }

        Member member = memberRepository.findBySocialId((String) userInfo.get("email"));
        Long memberId = member.getMemberId();
        String jwtToken = jwtUtil.generateToken(member.getSocialId());

        userInfo.put("memberId", memberId); // memberId 추가
        userInfo.put("token", jwtToken); // JWT 토큰 추가

        return SuccessResponse.success(String.valueOf(userInfo),member.getUserRole());
    }

    @PostMapping("/logout")
    @Operation(summary = "카카오 로그아웃", description = "카카오 로그아웃")
    public SuccessResponse<?> kakaoLogout(HttpSession session) {
        String accessToken = (String)session.getAttribute("accessToken");

        if(accessToken != null && !"".equals(accessToken)){
            try {
                kakaoService.kakaoDisconnect(accessToken);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            session.removeAttribute("accessToken");
            session.removeAttribute("email");
        }else{
            System.out.println("accessToken is null");
        }

        return SuccessResponse.success("로그아웃 성공");
    }

    @GetMapping("/mainpage")
    @Operation(summary = "메인 페이지", description = "메인페이지, 페이지 네이션 적용")
    public BaseResponse mainPage(
            @RequestParam(required = false, defaultValue = "", value = "category") String category,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
    ) {
        String memberEmail = FindLoginMember.getCurrentUserId();
        log.info("email={}", memberEmail);
        //String token = tokenHeader.replace("Bearer ", "");

//        if (!jwtUtil.validateToken(token)) {
//            return ErrorResponse.of("토큰이 유효하지 않습니다", HttpStatus.UNAUTHORIZED);
//        }

        //String socialId = jwtUtil.getEmailFromToken(token);
        MainPageDto mainPageDto = memberService.getMainPage(category, pageNo, memberEmail);

        return SuccessResponse.success("메인 페이지", mainPageDto);
    }


//    @GetMapping("/mainpage")
//    @Operation(summary = "메인 페이지", description = "메인페이지, 페이지 네이션 적용")
//    public SuccessResponse<MainPageDto> mainPage(
//                                                 @RequestParam(required = false, defaultValue = "", value = "category")  String category,
//                                                 @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
//                                                 Long memberId
//    ){
//
//
//        String socialId = (String) memberRepository.findByMemberId(memberId).getSocialId();
////        String accessToken = (String) session.getAttribute("accessToken");
//        MainPageDto mainPageDto = memberService.getMainPage(category,pageNo,socialId.toString());
//
//        return SuccessResponse.success("메인 페이지",mainPageDto);
//    }

    @GetMapping("/admin/members/{pageNo}")
    public Page<MemberDto> getMemberList(@RequestParam(defaultValue = "0") int pageNo){
        return memberService.getMemberList(pageNo);
    }

    @DeleteMapping("/admin/members")
    @Operation(summary = "멤버 삭제")
    public ResponseEntity<Void> deleteInquire() {
        String memberEmail = FindLoginMember.getCurrentUserId();
        Long memberId = memberRepository.findBySocialId(memberEmail).getMemberId();
        try {
            memberService.deleteMember(memberId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            log.error("Error deleting member with ID " + memberId, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/myHistory")
    @Operation(summary = "나의 작성글")
    public ResponseEntity<Page<PostWrittenDto>> getPostWritten( @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
    ){
        String memberEmail = FindLoginMember.getCurrentUserId();
        Long memberId = memberRepository.findBySocialId(memberEmail).getMemberId();
        Page<PostWrittenDto> listdto = memberService.getPostWrittenList(memberId,pageNo);

        if(listdto.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(listdto);
    }
}
