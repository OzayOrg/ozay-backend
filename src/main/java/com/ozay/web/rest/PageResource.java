package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.User;
import com.ozay.model.Building;
import com.ozay.model.Member;
import com.ozay.repository.*;
import com.ozay.web.rest.dto.BuildingRoleWrapperDTO;
import com.ozay.web.rest.dto.OrganizationUserDTO;
import com.ozay.web.rest.dto.OrganizationUserRoleDTO;
import com.ozay.web.rest.dto.UserDTO;
import com.ozay.web.rest.dto.page.MemberDetailPage;
import com.ozay.web.rest.dto.page.OrganizationPage;
import com.ozay.web.rest.dto.page.OrganizationUserPage;
import com.ozay.web.rest.dto.page.RolePage;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.ozay.web.rest.dto.page.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api/page")
public class PageResource {

    private final Logger log = LoggerFactory.getLogger(PageResource.class);

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    BuildingRepository buildingRepository;

    @Inject
    RoleRepository roleRepository;

    @Inject
    OrganizationUserRepository organizationUserRepository;

    @Inject
    RoleMemberRepository roleMemberRepository;

    @Inject
    MemberRepository memberRepository;

    @Inject
    PermissionRepository permissionRepository;

    @Inject
    NotificationRecordRepository notificationRecordRepository;


    /**
     * GET  /Organization -> get organizations.
     */
    @RequestMapping(value = "/organization/{organizationId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrganizationPage>getOrganizationPage(@PathVariable long organizationId) {
        OrganizationPage organizationPage = new OrganizationPage();
        organizationPage.setOrganization(organizationRepository.findOne(organizationId));
        organizationPage.setBuildingRoleWrapperDTOs(new ArrayList<BuildingRoleWrapperDTO>());

        List<Building> buildings = buildingRepository.getBuildingsByOrganization(organizationId);

        for(Building building : buildings){
            BuildingRoleWrapperDTO buildingRoleWrapperDTO = new BuildingRoleWrapperDTO();
            buildingRoleWrapperDTO.setBuilding(building);
            buildingRoleWrapperDTO.setRoleList(roleRepository.findAllByBuilding(building.getId()));
            organizationPage.getBuildingRoleWrapperDTOs().add(buildingRoleWrapperDTO);
        }
        List<UserDTO> userDTOs = new ArrayList<UserDTO>();
        for(User user : organizationUserRepository.findOrganizationUsers(organizationId)){
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTOs.add(userDTO);
        }

        organizationPage.setUserDTOs(userDTOs);
        return new ResponseEntity<OrganizationPage>(organizationPage, HttpStatus.OK);
    }


    @RequestMapping(
        value = "/notification-track",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)

    public ResponseEntity<PageNotificationTrackDTO> notificationTrack(@RequestParam(value = "building") Long buildingId, @RequestParam(value = "page", required = false) Long page){

        if(buildingId == null|| buildingId == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        long offset = 0;

        if(page != null && page > 1){
            offset = page - 1;
        }

        log.debug("REST page notification history with page {}", page);

        PageNotificationTrackDTO pageNotificationTrackDTO = new PageNotificationTrackDTO();
        pageNotificationTrackDTO.setNotificationRecords(notificationRecordRepository.findAllTrackedByBuildingId(buildingId));


        return new ResponseEntity<>(pageNotificationTrackDTO, HttpStatus.OK);
    }




    /**
     * GET  /{buildingId}/{id} -> get the "Building" by bu
     */
    @RequestMapping(value = "/member/{memberId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getMemberDetailEdit(@RequestParam(value = "building") Long buildingId, @PathVariable Long memberId) {
        log.debug("REST request to get Member ID : {}", memberId);

        Member member = memberRepository.findOne(memberId);
        if(member == null){
            return new ResponseEntity<>("Member doee not exist", HttpStatus.BAD_REQUEST);
        }
        MemberDetailPage memberDetailPage = new MemberDetailPage();
        memberDetailPage.setMember(member);
        memberDetailPage.setRoles(roleRepository.findAllByBuilding(buildingId));
        return new ResponseEntity<MemberDetailPage>(memberDetailPage, HttpStatus.OK);
    }


    /**
     * GET  management/organization/2/buildings/38/roles/edit/21
     */
    @RequestMapping(value = "/role-edit/{roleId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RolePage> roleEdit(@RequestParam(value = "building") Long buildingId, @RequestParam(value = "organization") Long organizationId, @PathVariable Long roleId) {
        RolePage rolePage = this.findOrganizationUserRoles(organizationId, buildingId, roleId );
        rolePage.setRole(roleRepository.findOne(roleId));

        return new ResponseEntity<RolePage>(rolePage, HttpStatus.OK);
    }

    /**
     * GET  management/organization/2/buildings/38/roles/edit/21
     */
    @RequestMapping(value = "/role-new",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RolePage> roleNew(@RequestParam(value = "building") Long buildingId, @RequestParam(value = "organization") Long organizationId){
        RolePage rolePage = this.findOrganizationUserRoles(organizationId, buildingId, null);

        return new ResponseEntity<RolePage>(rolePage, HttpStatus.OK);

    }

    /**
     * GET
     */
    @RequestMapping(value = "/organization-user-new",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrganizationUserPage> pageOrganizationUserNew(){
        OrganizationUserPage organizationUserPage = new OrganizationUserPage();
        organizationUserPage.setPermissions(permissionRepository.getOrganizationPermissions());
        return new ResponseEntity<OrganizationUserPage>(organizationUserPage, HttpStatus.OK);
    }

    /**
     * GET
     */
    @RequestMapping(value = "/organization-user-edit",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrganizationUserPage> pageOrganizationUserEdit(@RequestParam(value = "organization") Long organizationId, Long userId){
        OrganizationUserDTO organizationUserDTO = organizationUserRepository.findOrganizationUser(userId, organizationId);
        OrganizationUserPage organizationUserPage = new OrganizationUserPage();
        organizationUserPage.setOrganizationUserDTO(organizationUserDTO);
        organizationUserPage.setPermissions(permissionRepository.getOrganizationPermissions());
        return new ResponseEntity<OrganizationUserPage>(organizationUserPage, HttpStatus.OK);
    }


    // Role Edit/New page function

    private RolePage findOrganizationUserRoles(Long organizationId, Long buildingId, Long roleId){
        RolePage rolePage = new RolePage();

        rolePage.setPermissions(permissionRepository.getRolePermissions());
        rolePage.setRoles(roleRepository.findAllByBuilding(buildingId));

        List<OrganizationUserRoleDTO> organizationUserRoleDTOs = new ArrayList<OrganizationUserRoleDTO>();

        for(User user : organizationUserRepository.findOrganizationUsers(organizationId)){
            OrganizationUserRoleDTO organizationUserRoleDTO = new OrganizationUserRoleDTO();
            organizationUserRoleDTO.setUserId(user.getId());
            organizationUserRoleDTO.setFirstName(user.getFirstName());
            organizationUserRoleDTO.setLastName(user.getLastName());
            if(roleId != null){
                Member member = memberRepository.findOneByUserIdAndBuildingId(user.getId(), buildingId);
                if(member != null){
                    organizationUserRoleDTO.setAssigned(roleMemberRepository.hasRole(roleId, member.getId()));
                }
            }
            organizationUserRoleDTOs.add(organizationUserRoleDTO);
        }

        rolePage.setOrganizationUserRoleDTOs(organizationUserRoleDTOs);

        return rolePage;
    }

}
