package com.auction.backend.service.serviceImpl;

import com.auction.backend.dto.MembersDto;
import com.auction.backend.entity.Members;
import com.auction.backend.repository.MembersRepository;
import com.auction.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Service
public class MemberServiceImpl implements MemberService {

    private final MembersRepository repo;
    @Autowired
    public MemberServiceImpl(MembersRepository repo) {
        this.repo = repo;
    }


    @Override
    public MembersDto createMember(MembersDto dto) {
        Members members=new Members();
        members.setEmail(dto.getEmail());
        members.setName(dto.getName());
        members.setTeamName(dto.getTeamName());

        Members saved=repo.save(members);
        dto.setId(saved.getId());

        return dto;
    }

    @Override
    public MembersDto getMember(Long id) {

        Members members= repo.getById(id);
        MembersDto dto= convertToDto(members);
        return dto;
    }

    @Override
    public List<MembersDto> getAllMembers() {
        List<Members> list =repo.findAll();
        List<MembersDto> dtoList=new ArrayList<>();
        for(Members members:list){
            dtoList.add(convertToDto(members));
        }
        return dtoList;
    }

    @Override
    public MembersDto updateMember(Long id, MembersDto dto) {

        return null;
    }

    @Override
    public void deleteMember(Long id) {
        repo.deleteAllById(Collections.singleton(id));
    }

    private MembersDto convertToDto(Members m){
        MembersDto dto=new MembersDto();
        dto.setTeamName(m.getTeamName());
        dto.setName(m.getName());
        dto.setEmail(m.getEmail());
        dto.setId(m.getId());

        return dto;

    }
}
