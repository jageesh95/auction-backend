package com.auction.backend.service.serviceImpl;

import com.auction.backend.dto.CreateMemberRequest;
import com.auction.backend.dto.MembersDto;
import com.auction.backend.entity.Members;
import com.auction.backend.entity.User;
import com.auction.backend.enums.Role;
import com.auction.backend.exception.ResourceNotFoundException;
import com.auction.backend.repository.MembersRepository;
import com.auction.backend.repository.UserRepository;
import com.auction.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Service
public class MemberServiceImpl implements MemberService {

    private final MembersRepository repo;
    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public MemberServiceImpl(MembersRepository repo, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public MembersDto createMember(CreateMemberRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.MEMBER);
        user.setActive(true);

        userRepository.save(user);

        Members member = new Members();
        member.setTeamName(request.getTeamName());
        member.setName(request.getName());
        member.setUser(user);

        Members saved=repo.save(member);

        return entityToDto(saved);
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
        Members member = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        member.setName(dto.getName());
        member.setTeamName(dto.getTeamName());
        return convertToDto(repo.save(member));
    }

    @Override
    public void deleteMember(Long id) {
        repo.deleteAllById(Collections.singleton(id));
    }

    private MembersDto convertToDto(Members m){
        MembersDto dto=new MembersDto();
        dto.setTeamName(m.getTeamName());
        dto.setName(m.getName());
        dto.setId(m.getId());
        dto.setRole(m.getUser().getRole());
        dto.setEmail(m.getUser().getEmail());
        dto.setPhoneNumber(m.getUser().getMobile());

        return dto;

    }

    private MembersDto entityToDto(Members members){
        MembersDto dto=new MembersDto();
        dto.setId(members.getId());
        dto.setRole(members.getUser().getRole());
        dto.setEmail(members.getUser().getEmail());
        dto.setPhoneNumber(members.getUser().getMobile());
        dto.setName(members.getName());
        dto.setTeamName(members.getTeamName());

        return dto;

    }
}
