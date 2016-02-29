package com.ozay.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.backend.domain.User;
import com.ozay.backend.model.InvitedMember;
import com.ozay.backend.model.Member;
import com.ozay.backend.repository.InvitedMemberRepository;
import com.ozay.backend.repository.MemberRepository;
import com.ozay.backend.repository.UserRepository;
import com.ozay.backend.service.InvitedMemberService;
import com.ozay.backend.web.rest.dto.UserDTO;
import com.ozay.backend.web.rest.errors.ErrorDTO;
import com.ozay.backend.web.rest.form.MemberRegisterFormDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * Created by naofumiezaki on 11/18/15.
 */
@RestController
@RequestMapping("/api/invited-member")
public class InvitedMemberResource {

    private final Logger log = LoggerFactory.getLogger(InvitedMemberResource.class);

    @Inject
    InvitedMemberRepository invitedMemberRepository;

    @Inject
    MemberRepository memberRepository;

    @Inject
    InvitedMemberService invitedMemberService;

    @Inject
    UserRepository userRepository;

    @RequestMapping(
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getInvitedMember(@RequestParam(value = "key") String key) {
        log.debug("REST request to find invited member : {}", key);

        InvitedMember invitedMember = invitedMemberRepository.findOneByActivationKey(key);
        if(invitedMember == null){
            return new ResponseEntity<>(new ErrorDTO("Sorry, the record could not be found"), HttpStatus.BAD_REQUEST);
        }
        Member member = memberRepository.findOne(invitedMember.getMemberId());
        if(member.getUserId() != null && member.getUserId() > 0){
            return new ResponseEntity<>(new ErrorDTO("You are already activated"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> register(@Valid @RequestBody MemberRegisterFormDTO memberRegisterFormDTO, @RequestParam(value = "key") String key) {

        log.debug("REST request to register Invited member : {}", memberRegisterFormDTO);

        return userRepository.findOneByLogin(memberRegisterFormDTO.getLogin())
            .map(user -> new ResponseEntity<>(new ErrorDTO("login already in use"), HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                    InvitedMember invitedMember = invitedMemberRepository.findOneByActivationKey(key);

                    if(invitedMember == null){
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    invitedMemberService.createMember(invitedMember, memberRegisterFormDTO);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                });
    }
}
