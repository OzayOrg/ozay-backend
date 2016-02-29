package com.ozay.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.backend.domain.User;
import com.ozay.backend.model.Member;
import com.ozay.backend.repository.MemberRepository;
import com.ozay.backend.repository.UserRepository;
import com.ozay.backend.service.MemberService;
import com.ozay.backend.web.rest.form.MemberFormDTO;
import com.ozay.backend.web.rest.form.RoleFormDTO;
import com.ozay.backend.web.rest.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by naofumiezaki on 11/23/15.
 */
@RestController
@RequestMapping("/api/member")
public class MemberResource {

    private final Logger log = LoggerFactory.getLogger(MemberResource.class);

    @Inject
    MemberService memberService;

    @Inject
    MemberRepository memberRepository;

    @Inject
    UserRepository userRepository;

    @RequestMapping(
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> createMember(@RequestBody MemberFormDTO memberFormDTO) {
        log.debug("REST request to create Member : {}", memberFormDTO);
        memberService.createMember(memberFormDTO);

        return new ResponseEntity<>(memberFormDTO.getMember(), HttpStatus.CREATED);
    }

    @RequestMapping(
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updateMember(@RequestBody MemberFormDTO memberFormDTO) {
        log.debug("REST request to update Member : {}", memberFormDTO);
        memberService.updateMember(memberFormDTO);

        return new ResponseEntity<>(memberFormDTO.getMember(), HttpStatus.OK);
    }

    @RequestMapping(
        value = "/invite",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> inviteMember(@RequestBody Member member, HttpServletRequest request) {
        log.debug("REST request to invite Member : {}", member);
        return userRepository.findOneByLogin(member.getEmail())
            .map(user -> {
                member.setUserId(user.getId());
                memberRepository.update(member);
                return new ResponseEntity<>(member, HttpStatus.OK);
            })
            .orElseGet(() -> {
                String baseUrl = UrlUtil.baseUrlGenerator(request);
                memberService.inviteMember(member, baseUrl);
                return new ResponseEntity<>(member, HttpStatus.OK);
            });

    }

    @RequestMapping(
        value = "/{memberId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId) {
        log.debug("REST request to delete : {}", memberId);
        Member member = memberRepository.findOne(memberId);
        memberService.deleteMember(member);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
