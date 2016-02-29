package com.ozay.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.backend.domain.Authority;
import com.ozay.backend.domain.User;
import com.ozay.backend.model.Building;
import com.ozay.backend.repository.BuildingRepository;
import com.ozay.backend.repository.UserRepository;
import com.ozay.backend.security.SecurityUtils;
import com.ozay.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 10/30/15.
 */

@RestController
@RequestMapping("/api/building")
public class BuildingResource {

    private final Logger log = LoggerFactory.getLogger(BuildingResource.class);

    @Inject
    BuildingRepository buildingRepository;

    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;


    @RequestMapping(
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<Building>> getAll(){

        List<Building> buildingList = new ArrayList<Building>();

        if(SecurityUtils.isUserInRole("ROLE_ADMIN")){
            buildingList = buildingRepository.findAll();
        } else {
            User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
            if(user != null){
                buildingList = buildingRepository.findAllUserCanAccess(user);
            }
        }

        return new ResponseEntity<List<Building>>(buildingList, HttpStatus.OK);
    }

    /**
     * POST  /building -> Create a new building.
     */
    @RequestMapping(
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> createBuilding(@RequestBody Building building) {
        log.debug("REST request to save Building : {}", building);
        User user = userService.getUserWithAuthorities();
        building.setCreatedBy(user.getId());
        buildingRepository.create(building);
        return new ResponseEntity<>(building, HttpStatus.CREATED);
    }

    /**
     * POST  /building -> Update a building.
     */
    @RequestMapping(
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updateBuilding(@RequestBody Building building) {
        log.debug("REST request to update Building : {}", building);
        User user = userService.getUserWithAuthorities();
        building.setLastModifiedBy(user.getId());
        buildingRepository.update(building);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
