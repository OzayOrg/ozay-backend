package com.ozay.backend.service;

import com.ozay.backend.model.Building;
import com.ozay.backend.model.InvitedMember;
import com.ozay.backend.model.Member;
import com.ozay.backend.repository.BuildingRepository;
import com.ozay.backend.repository.InvitedMemberRepository;
import com.ozay.backend.repository.MemberRepository;
import com.ozay.backend.repository.RoleMemberRepository;
import com.ozay.backend.security.SecurityUtils;
import com.ozay.backend.service.util.RandomUtil;
import com.ozay.backend.web.rest.form.MemberFormDTO;
import com.ozay.backend.web.rest.form.MemberRoleFormDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by naofumiezaki on 11/29/15.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberService {
    @Inject
    MemberRepository memberRepository;

    @Inject
    RoleMemberRepository roleMemberRepository;

    @Inject
    BuildingRepository buildingRepository;

    @Inject
    MailService mailService;

    @Inject
    InvitedMemberRepository invitedMemberRepository;

    public void createMember(MemberFormDTO memberFormDTO){
        memberRepository.create(memberFormDTO.getMember());
        this.updateRoleMember(memberFormDTO);
    }

    public void updateMember(MemberFormDTO memberFormDTO){
        memberRepository.update(memberFormDTO.getMember());
        this.updateRoleMember(memberFormDTO);
    }

    public void deleteMember(Member member){
        roleMemberRepository.deleteAllByMemberId(member.getId());
        memberRepository.softDelete(member);
    }

    private void updateRoleMember(MemberFormDTO memberFormDTO){
        roleMemberRepository.deleteAllByMemberId(memberFormDTO.getMember().getId());
        for(MemberRoleFormDTO memberRoleFormDTO : memberFormDTO.getRoles()){
            if(memberRoleFormDTO.isAssign() == true){
                roleMemberRepository.create(memberRoleFormDTO.getId(), memberFormDTO.getMember().getId());
            }
        }
    }
    public void inviteMember(Member member, String baseUrl){
        Building building = buildingRepository.findOne(member.getBuildingId());
        InvitedMember invitedMember = new InvitedMember();
        invitedMember.setActivationKey(RandomUtil.generateActivationKey());
        invitedMember.setMemberId(member.getId());
        invitedMember.setLangKey("en");
        invitedMember.setCreatedBy(SecurityUtils.getCurrentLogin());
        invitedMemberRepository.create(invitedMember);
        mailService.inviteMember(member, building, invitedMember, baseUrl);
    }
}
