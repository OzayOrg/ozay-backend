package com.ozay.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.backend.domain.User;
import com.ozay.backend.model.TempUser;
import com.ozay.backend.model.OrganizationUser;
import com.ozay.backend.repository.*;
import com.ozay.backend.service.MailService;
import com.ozay.backend.service.OrganizationUserService;
import com.ozay.backend.service.UserService;
import com.ozay.backend.web.rest.dto.OrganizationUserDTO;
import com.ozay.backend.web.rest.errors.ErrorDTO;
import com.ozay.backend.web.rest.form.OrganizationUserRegisterDTO;
import com.ozay.backend.web.rest.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by naofumiezaki on 10/30/15.
 */

@RestController
@RequestMapping("/api/organization-user")
public class OrganizationUserResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationUserResource.class);

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private OrganizationUserRepository organizationUserRepository;

    @Inject
    private OrganizationUserService organizationUserService;

    @Inject
    TempUserRepository tempUserRepository;

    @Inject
    private MailService mailService;

    /**
     * POST  create organization user
     */
    @RequestMapping(
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> createOrganizationUser(@RequestBody OrganizationUserDTO organizationUserDTO, HttpServletRequest request) {
        return userRepository.findOneByEmail(organizationUserDTO.getEmail())
            .map(user -> {
                organizationUserService.processExistingUser(user, organizationUserDTO);
                return new ResponseEntity<>(organizationUserDTO, HttpStatus.CREATED);
            })
            .orElseGet(() -> {
                List<TempUser> tempUsers = tempUserRepository.findAllByOrganizationIdAndEmail(organizationUserDTO.getOrganizationId(), organizationUserDTO.getEmail());
                if(tempUsers.size() == 0){
                    organizationUserService.createNonExistingUser(organizationUserDTO);
                } else {
                    new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(organizationUserDTO, HttpStatus.CREATED);
            });
    }

    /**
     * PUT  Update organization user
     */
    @RequestMapping(
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updateOrganizationUser(@Valid @RequestBody OrganizationUserDTO organizationUserDTO) {
        log.debug("REST request to update organization user : {}", organizationUserDTO);

        return Optional.ofNullable(organizationUserRepository.findOne(organizationUserDTO.getId()))
            .map(organizationUser -> {
                if(organizationUser.isActivated()){
                    organizationUserService.processPermissions(organizationUserDTO);
                } else {
                    List<TempUser> tempUsers = tempUserRepository.findAllByOrganizationIdAndEmail(organizationUserDTO.getOrganizationId(), organizationUserDTO.getEmail());
                    if(tempUsers.size() == 0){
                        organizationUserService.updateNonExistingUser(organizationUserDTO);
                    } else {
                        for(TempUser tempUser : tempUsers){
                            if(tempUser.getId() != organizationUserDTO.getUserId()){
                                return new ResponseEntity<>(new ErrorDTO("email already in use"), HttpStatus.BAD_REQUEST);
                            }
                        }
                        organizationUserService.updateNonExistingUser(organizationUserDTO);
                    }
                }
                return new ResponseEntity<>(organizationUserDTO, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
    /**
     * POST  create Invitation Email
     */
    @RequestMapping(
        value = "/invite",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> sendInvitationEmail(@Valid @RequestBody OrganizationUserDTO organizationUserDTO, HttpServletRequest request) {
        String activateKey = "";
        if(organizationUserDTO.isActivated() == false){
            TempUser tempUser = tempUserRepository.findOne(organizationUserDTO.getUserId());
            activateKey = tempUser.getActivationKey();
            String baseUrl = UrlUtil.baseUrlGenerator(request);
            mailService.sendOrganizationUserInvitationMail(organizationUserDTO, baseUrl, activateKey);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // User already exists..
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * POST  register Invitation Email
     */
    @RequestMapping(
        value = "/register",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> organizationUserRegister(@Valid @RequestBody OrganizationUserRegisterDTO organizationUserRegisterDTO, @RequestParam(value = "key") String key, HttpServletRequest request) {
        return userRepository.findOneByLogin(organizationUserRegisterDTO.getLogin())
            .map(user -> {
                return new ResponseEntity<>(new ErrorDTO("login already in use"), HttpStatus.BAD_REQUEST);
            })
            .orElseGet(() -> {
                TempUser tempUser = tempUserRepository.findOneByActivationKey(key);
                if(tempUser == null){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                } else if(tempUser.isActivated() == true){
                    return new ResponseEntity<>(new ErrorDTO("You are already activated"), HttpStatus.BAD_REQUEST);
                }

                OrganizationUser organizationUser = organizationUserRepository.findOneByTempUserId(tempUser.getId());
                if(organizationUser == null){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                organizationUserService.processOrganizationUserInformation(organizationUserRegisterDTO, tempUser, organizationUser);
                return new ResponseEntity<>(HttpStatus.CREATED);
            });

    }
}
