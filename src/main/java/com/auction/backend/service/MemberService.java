package com.auction.backend.service;

import com.auction.backend.dto.MembersDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MemberService {

    MembersDto createMember(MembersDto dto);
    MembersDto getMember(Long id);
    List<MembersDto> getAllMembers();

    MembersDto updateMember(Long id,MembersDto dto);

    void deleteMember(Long id);



}
