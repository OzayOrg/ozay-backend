package com.ozay.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.backend.model.Member;
import com.ozay.backend.model.Role;
import com.ozay.backend.model.RolePermission;
import com.ozay.backend.repository.RoleMemberRepository;
import com.ozay.backend.repository.RolePermissionRepository;
import com.ozay.backend.repository.RoleRepository;
import com.ozay.backend.service.RoleService;
import com.ozay.backend.web.rest.dto.OrganizationRoleMemberDTO;
import com.ozay.backend.web.rest.errors.ErrorDTO;
import com.ozay.backend.web.rest.form.RoleFormDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by naofumiezaki on 11/18/15.
 */
@RestController
@RequestMapping("/api/role")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    @Inject
    RoleService roleService;

    @Inject
    RoleRepository roleRepository;

    @Inject
    RoleMemberRepository roleMemberRepository;


    @RequestMapping(
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> createRole(@RequestBody RoleFormDTO roleFormDTO) {
        log.debug("REST request to create Role : {}", roleFormDTO);
        roleService.create(roleFormDTO);
        return new ResponseEntity<>(roleFormDTO.getRole(), HttpStatus.CREATED);
    }

    @RequestMapping(
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updateRole(@RequestBody RoleFormDTO roleFormDTO) {
        log.debug("REST request to update Role : {}", roleFormDTO);
        roleService.update(roleFormDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(
        value = "/{roleId}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> deleteRoles(@PathVariable Long roleId) {
        log.debug("REST request to delete Roles : {}", roleId);

        Role role = roleRepository.findOne(roleId);

        if(role == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        roleService.delete(role);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
