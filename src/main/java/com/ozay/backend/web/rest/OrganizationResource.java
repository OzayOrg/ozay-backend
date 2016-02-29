package com.ozay.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.backend.domain.User;
import com.ozay.backend.model.Organization;
import com.ozay.backend.repository.OrganizationRepository;
import com.ozay.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by naofumiezaki on 10/30/15.
 */

@RestController
@RequestMapping("/api/organization")
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    UserService userService;


    /**
     * POST  create organization
     */
    @RequestMapping(
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> createOrganization(@RequestBody Organization organization) {
        log.debug("REST request to save organization : {}", organization);
        User user = userService.getUserWithAuthorities();
        organization.setUserId(user.getId());
        organizationRepository.create(organization);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * PUT  Update organization
     */
    @RequestMapping(
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updateOrganization(@RequestBody Organization organization) {
        log.debug("REST request to update organization : {}", organization);

        organizationRepository.update(organization);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
