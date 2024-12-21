package org.vsa.server.admin.controller;

import org.vsa.server.admin.dto.AdminBookDto;
import org.vsa.server.admin.service.AdminBookListService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AdminBookListController {

    @Autowired
    AdminBookListService adminBookListService;


    @GetMapping("/admin/books")
    @Operation(description = "관리자 pdf 관리 리스트", summary = "관리자 pdf 수정 리스트 관리")
    public ResponseEntity<List<AdminBookDto>> getAdminPdfList(@RequestParam(defaultValue = "") String search){
//        String socialId = session.getAttribute("email").toString();


        List<AdminBookDto> dto =  adminBookListService.getBookList(search);
        if(dto.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"찾으시는 pdf가 없습니다.");
        }


        return ResponseEntity.ok().body(dto);

    }



}
