package com.ozay.backend.service;

import com.ozay.backend.domain.User;
import com.ozay.backend.model.Building;
import com.ozay.backend.model.InvitedMember;
import com.ozay.backend.model.Member;
import com.ozay.backend.repository.*;
import com.ozay.backend.security.SecurityUtils;
import com.ozay.backend.service.util.RandomUtil;
import com.ozay.backend.web.rest.form.MemberFormDTO;
import com.ozay.backend.web.rest.form.MemberRegisterFormDTO;
import com.ozay.backend.web.rest.form.MemberRoleFormDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by naofumiezaki on 11/29/15.
 */
@Service
@Transactional
public class InvitedMemberService {
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

    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;

    public void createMember(InvitedMember invitedMember, MemberRegisterFormDTO memberRegisterFormDTO){

        Member member = memberRepository.findOne(invitedMember.getMemberId());

        User user = userService.createUserInformation(
            memberRegisterFormDTO.getLogin(),
            memberRegisterFormDTO.getPassword(),
            member.getFirstName(),
            member.getLastName(),
            member.getEmail(),
            invitedMember.getLangKey()
            );

        user.setActivated(true);
        user.setActivationKey(invitedMember.getActivationKey());
        userRepository.save(user);

        member.setUserId(user.getId());
        memberRepository.update(member);

        invitedMember.setActivated(true);
        invitedMemberRepository.update(invitedMember);

    }
}
