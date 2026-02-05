package com.auction.backend.controller;

import com.auction.backend.dto.MembersDto;
import com.auction.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins ="*")
@RestController
@RequestMapping("/api/members")
public class MembersController {

    private MemberService memberService;

    @Autowired
    public MembersController(MemberService memberService) {
        this.memberService = memberService;
    }
    @PostMapping
    public MembersDto create(@RequestBody MembersDto membersDto){
       return memberService.createMember(membersDto);
    }

    @GetMapping
    public List<MembersDto> getAll(){
      return  memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public MembersDto getById(Long id){
        return memberService.getMember(id);
    }

    @PutMapping("/{id}")
    public MembersDto update(Long id,MembersDto dto){
        return memberService.updateMember(id,dto);
    }

    @DeleteMapping("/{id}")
    public void delete(Long id){
         memberService.deleteMember(id);
    }


}
