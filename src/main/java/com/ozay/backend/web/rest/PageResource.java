package com.ozay.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.backend.domain.User;
import com.ozay.backend.model.*;
import com.ozay.backend.repository.*;
import com.ozay.backend.security.SecurityUtils;
import com.ozay.backend.service.UserService;
import com.ozay.backend.web.rest.dto.OrganizationRoleMemberDTO;
import com.ozay.backend.web.rest.dto.OrganizationUserDTO;
import com.ozay.backend.web.rest.dto.OrganizationUserRoleDTO;
import com.ozay.backend.web.rest.dto.pages.*;
import com.ozay.backend.web.rest.form.MemberFormDTO;
import com.ozay.backend.web.rest.form.MemberRoleFormDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by naofumiezaki on 11/1/15.
 */

@RestController
@RequestMapping("/api/page")
public class PageResource {

    private final Logger log = LoggerFactory.getLogger(PageResource.class);

    @Inject
    private OrganizationRepository organizationRepository;

    @Inject
    private UserService userService;

    @Inject
    private BuildingRepository buildingRepository;

    @Inject
    private PermissionRepository permissionRepository;

    @Inject
    private RolePermissionRepository rolePermissionRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private OrganizationUserRepository organizationUserRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TempUserRepository tempUserRepository;

    @Inject
    private OrganizationUserPermissionRepository organizationUserPermissionRepository;

    @Inject
    RoleMemberRepository roleMemberRepository;

    @Inject
    NotificationRepository notificationRepository;

    @Inject
    MemberRepository memberRepository;

    @Inject
    NotificationRecordRepository notificationRecordRepository;

    @Inject
    AccountRepository accountRepository;

    @RequestMapping(
        value = "/management",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<PageManagementDTO> getManagementContents(){
        PageManagementDTO pageManagementDTO = new PageManagementDTO();
        pageManagementDTO.setOrganizations(organizationRepository.findAllUserCanAccess(userService.getUserWithAuthorities()));
        log.debug("REST request to update organization : {}", pageManagementDTO);
        return new ResponseEntity<>(pageManagementDTO, HttpStatus.OK);
    }

    @RequestMapping(
        value = "/organization/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<Organization> getOrganizationEditContents(@PathVariable Long id){
        Organization organization = organizationRepository.findOne(id);
        log.debug("REST request to get organization information : {}", organization);
        return new ResponseEntity<>(organization, HttpStatus.OK);
    }

    @RequestMapping(
        value = "/organization-detail/{organizationId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<PageOrganizationDetailDTO> getOrganizationDetailContents(@PathVariable Long organizationId){
        boolean result1 =  permissionRepository.validateOrganizationInterceptor(organizationId, "ORGANIZATION-USER_GET");
        boolean result2 =  permissionRepository.validateOrganizationInterceptor(organizationId, "BUILDING_GET");
        boolean hasAllAccess = false;
        if(result1 == false && result2 == false){
            boolean isOrganizationSubscriber = accountRepository.isOrganizationSubscriber(organizationId);
            if(isOrganizationSubscriber == true){
                hasAllAccess = true;
            }
        }
        Organization organization = organizationRepository.findOne(organizationId);
        log.debug("REST request to get organization detail : {}", organization);
        PageOrganizationDetailDTO pageOrganizationDetailDTO = new PageOrganizationDetailDTO();
        if(result1 == true || hasAllAccess == true){
            List<OrganizationUserDTO> organizationUserDTOs = this.getAllOrganizationUsers(organizationId);
            pageOrganizationDetailDTO.setOrganizationUserDTOs(organizationUserDTOs);
        }
        if(result2 == true || hasAllAccess == true){
            pageOrganizationDetailDTO.setBuildings(buildingRepository.findAllOrganizationBuildings(organizationId));
        }

        return new ResponseEntity<>(pageOrganizationDetailDTO, HttpStatus.OK);
    }

    private List<OrganizationUserDTO> getAllOrganizationUsers(Long organizationId){
        List<OrganizationUserDTO> organizationUserDTOs = organizationRepository.findAllOrganizationActivatedUser(organizationId);
        organizationUserDTOs.addAll(organizationRepository.findAllOrganizationInactivatedUser(organizationId));
        return organizationUserDTOs;
    }

    @RequestMapping(
        value = "/building-edit/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<PageBuildingEditDTO> getBuildingEditContents(@PathVariable Long id){
        Building building = buildingRepository.findOne(id);
        log.debug("REST request to get building edit : {}", building);
        PageBuildingEditDTO pageBuildingEditDTO = new PageBuildingEditDTO();
        pageBuildingEditDTO.setBuilding(building);
        return new ResponseEntity<>(pageBuildingEditDTO, HttpStatus.OK);
    }

    @RequestMapping(
        value = "/role",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<PageRoleDTO> getRoles(@RequestParam(value = "building") Long buildingId){
        log.debug("REST request to get role list");

        PageRoleDTO pageRoleDTO = new PageRoleDTO();
        pageRoleDTO.setRoles(roleRepository.findAllByBuildingId(buildingId));
        return new ResponseEntity<>(pageRoleDTO, HttpStatus.OK);
    }

    @RequestMapping(
        value = "/role-new",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<PageRoleEditDTO> roleNew(@RequestParam(value = "building") Long buildingId){
        log.debug("REST request to get role new  ");

        PageRoleEditDTO pageRoleEditDTO = new PageRoleEditDTO();
        pageRoleEditDTO.setPermissions(permissionRepository.findRolePermissions());
        pageRoleEditDTO.setRoles(roleRepository.findAllByBuildingId(buildingId));
        Building building = buildingRepository.findOne(buildingId);

        List<OrganizationUserRoleDTO> organizationUserRoleDTOs = createOrganizationUserRoleDTOs(building.getOrganizationId());
        pageRoleEditDTO.setOrganizationUserRoleDTOs(organizationUserRoleDTOs);

        return new ResponseEntity<>(pageRoleEditDTO, HttpStatus.OK);
    }

    private List<OrganizationUserRoleDTO> createOrganizationUserRoleDTOs(Long organizationId){
        List<OrganizationUserDTO> organizationUserDTOs = getAllOrganizationUsers(organizationId);
        List<OrganizationUserRoleDTO> organizationUserRoleDTOs = new ArrayList<OrganizationUserRoleDTO>();

        for(OrganizationUserDTO organizationUserDTO : organizationUserDTOs){
            OrganizationUserRoleDTO organizationUserRoleDTO = new OrganizationUserRoleDTO();
            organizationUserRoleDTO.setId(organizationUserDTO.getId());
            organizationUserRoleDTO.setFirstName(organizationUserDTO.getFirstName());
            organizationUserRoleDTO.setLastName(organizationUserDTO.getLastName());
            organizationUserRoleDTOs.add(organizationUserRoleDTO);
        }
        return organizationUserRoleDTOs;
    }


    @RequestMapping(
        value = "/role-edit/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<PageRoleEditDTO> roleEdit(@PathVariable Long id, @RequestParam(value = "building") Long buildingId){
        log.debug("REST request to get role id : {} ", id);

        PageRoleEditDTO pageRoleEditDTO = new PageRoleEditDTO();
        pageRoleEditDTO.setRole(roleRepository.findOne(id));
        pageRoleEditDTO.setPermissions(permissionRepository.findRolePermissions());
        pageRoleEditDTO.setRoles(roleRepository.findAllExceptId(buildingId, id));
        Building building = buildingRepository.findOne(buildingId);
        List<OrganizationUserRoleDTO> organizationUserRoleDTOs = createOrganizationUserRoleDTOs(building.getOrganizationId());
        List<OrganizationRoleMemberDTO> organizationRoleMemberDTOs = roleMemberRepository.findOrganizationRoleMembers(id);
        for(OrganizationRoleMemberDTO organizationRoleMemberDTO : organizationRoleMemberDTOs){
            for(OrganizationUserRoleDTO organizationUserRoleDTO : organizationUserRoleDTOs){
                if(organizationRoleMemberDTO.getOrganizationUserId() == organizationUserRoleDTO.getId()){
                    organizationUserRoleDTO.setAssigned(true);
                    break;
                }
            }
        }
        pageRoleEditDTO.setOrganizationUserRoleDTOs(organizationUserRoleDTOs);

        return new ResponseEntity<>(pageRoleEditDTO, HttpStatus.OK);
    }

    @RequestMapping(
        value = "/organization-user-new",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<PageOrganizationUserDTO> organizationUserNew(){
        log.debug("REST request to create a organization user  ");

        PageOrganizationUserDTO pageOrganizationUserDTO = new PageOrganizationUserDTO();
        pageOrganizationUserDTO.setPermissions(permissionRepository.findOrganizationPermissions());
        return new ResponseEntity<>(pageOrganizationUserDTO, HttpStatus.OK);
    }

    @RequestMapping(
        value = "/organization-user-edit/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<PageOrganizationUserDTO> organizationUserEdit(@PathVariable Long id){
        log.debug("REST request to get organization user id  {} ", id);
        OrganizationUser organizationUser = organizationUserRepository.findOne(id);
        if(organizationUser == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        OrganizationUserDTO organizationUserDTO = new OrganizationUserDTO();
        organizationUserDTO.setId(organizationUser.getId());
        organizationUserDTO.setOrganizationUserPermissions( new HashSet<OrganizationUserPermission>(organizationUserPermissionRepository.findAll(organizationUser.getId())));
        organizationUserDTO.setOrganizationId(organizationUser.getOrganizationId());
        organizationUserDTO.setActivated(organizationUser.isActivated());
        if(organizationUser.isActivated() == true){
            User user = userRepository.findOne(organizationUser.getUserId());
            if(user == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            organizationUserDTO.setUserId(user.getId());
            organizationUserDTO.setFirstName(user.getFirstName());
            organizationUserDTO.setLastName(user.getLastName());
            organizationUserDTO.setEmail(user.getEmail());
        } else {
            TempUser tempUser = tempUserRepository.findOne(organizationUser.getTempUserId());
            if(tempUser == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            organizationUserDTO.setUserId(tempUser.getId());
            organizationUserDTO.setFirstName(tempUser.getFirstName());
            organizationUserDTO.setLastName(tempUser.getLastName());
            organizationUserDTO.setEmail(tempUser.getEmail());
        }

        PageOrganizationUserDTO pageOrganizationUserDTO = new PageOrganizationUserDTO();
        pageOrganizationUserDTO.setOrganizationUserDTO(organizationUserDTO);
        pageOrganizationUserDTO.setPermissions(permissionRepository.findOrganizationPermissions());
        return new ResponseEntity<>(pageOrganizationUserDTO, HttpStatus.OK);
    }

    @RequestMapping(
        value = "/notification",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<PageNotificationDTO> notificationCreate(@RequestParam(value = "building") Long buildingId){
        if(buildingId == null|| buildingId == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.debug("REST page notification create");

        PageNotificationDTO pageOrganizationUserDTO = new PageNotificationDTO();
        pageOrganizationUserDTO.setNotifications(notificationRepository.searchNotificationWithLimit(buildingId, (long) 10));

        pageOrganizationUserDTO.setRoles(roleRepository.findAllByBuildingId(buildingId));
        pageOrganizationUserDTO.setMembers(memberRepository.findAllByBuildingId(buildingId));

        List<Member> members = memberRepository.findAllByBuildingId(buildingId);
        Collections.sort(members);
        pageOrganizationUserDTO.setMembers(members);

        return new ResponseEntity<>(pageOrganizationUserDTO, HttpStatus.OK);
    }

    @RequestMapping(
        value = "/notification-record",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<PageNotificationRecordDTO> notificationArchive(@RequestParam(value = "building") Long buildingId, @RequestParam(value = "page", required = false) Long page){

        if(buildingId == null|| buildingId == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        long offset = 0;

        if(page != null && page > 1){
            offset = page - 1;
        }

        log.debug("REST page notification history with page {}", page);

        PageNotificationRecordDTO pageOrganizationUserDTO = new PageNotificationRecordDTO();
        pageOrganizationUserDTO.setTotalNumOfPages(notificationRepository.countAllByBuildingId(buildingId));
        pageOrganizationUserDTO.setNotificationRecords(notificationRepository.findAllByBuildingId(buildingId, offset));

        return new ResponseEntity<>(pageOrganizationUserDTO, HttpStatus.OK);
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
        pageNotificationTrackDTO.setNotificationRecords(notificationRecordRepository.findAllTrackedByBuildingId(buildingId, offset));


        return new ResponseEntity<>(pageNotificationTrackDTO, HttpStatus.OK);
    }

    @RequestMapping(

        value = "/notification-record-detail/{notificationId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<PageNotificationRecordDetailDTO> notificationArchiveDetail(@RequestParam(value = "building") Long buildingId, @PathVariable Long notificationId){
        if(buildingId == null|| buildingId == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.debug("REST page notification Details");

       PageNotificationRecordDetailDTO pageOrganizationUserDTO = new PageNotificationRecordDetailDTO();

        pageOrganizationUserDTO.setNotification(notificationRepository.findOne(notificationId));
        pageOrganizationUserDTO.setNotificationRecords(notificationRecordRepository.findAllByNotificationId(notificationId));

        return new ResponseEntity<>(pageOrganizationUserDTO, HttpStatus.OK);
    }

    @RequestMapping(
        value = "/member",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<?> getMembers(@RequestParam(value = "building") Long buildingId){
        if(buildingId == null|| buildingId == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.debug("REST page get member building id {}", buildingId);

        List<Member> members = memberRepository.findAllByBuildingId(buildingId);

        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @RequestMapping(
        value = "/member-edit/{memberId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<?> getMemberNew(@RequestParam(value = "building") Long buildingId, @PathVariable Long memberId){
        if(buildingId == null|| buildingId == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.debug("REST page get member building id {}", buildingId);

        Member member = memberRepository.findOne(memberId);

        MemberFormDTO memberFormDTO = new MemberFormDTO();
        memberFormDTO.setMember(member);

        List<MemberRoleFormDTO> list = new ArrayList<MemberRoleFormDTO>();

        List<RoleMember> roleMembers = roleMemberRepository.findRoleMembersByMemberId(memberId);

        for(Role role: roleRepository.findAllNonOrganizationRolesByBuildingId(buildingId)){
            MemberRoleFormDTO memberRoleFormDTO = new MemberRoleFormDTO();
            memberRoleFormDTO.setId(role.getId());
            memberRoleFormDTO.setName(role.getName());
            memberRoleFormDTO.setSortOrder(role.getSortOrder());
            memberRoleFormDTO.setBelongTo(role.getBelongTo());
            for(RoleMember roleMember : roleMembers){
                if(roleMember.getRoleId() == role.getId()){
                    memberRoleFormDTO.setAssign(true);
                    break;
                }
            }

            list.add(memberRoleFormDTO);
        }

        memberFormDTO.setRoles(list);

        return new ResponseEntity<>(memberFormDTO, HttpStatus.OK);
    }

    @RequestMapping(
        value = "/member-new",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<?> getMemberNew(@RequestParam(value = "building") Long buildingId){
        if(buildingId == null|| buildingId == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.debug("REST page get member building id {}", buildingId);

        MemberFormDTO memberFormDTO = new MemberFormDTO();

        List<MemberRoleFormDTO> list = new ArrayList<MemberRoleFormDTO>();
        for(Role role: roleRepository.findAllNonOrganizationRolesByBuildingId(buildingId)){
            MemberRoleFormDTO memberRoleFormDTO = new MemberRoleFormDTO();
            memberRoleFormDTO.setId(role.getId());
            memberRoleFormDTO.setName(role.getName());
            memberRoleFormDTO.setSortOrder(role.getSortOrder());
            memberRoleFormDTO.setBelongTo(role.getBelongTo());
            list.add(memberRoleFormDTO);
        }

        memberFormDTO.setRoles(list);

        return new ResponseEntity<>(memberFormDTO, HttpStatus.OK);
    }
    @RequestMapping(
        value = "/search",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<?> getSearchResult(@RequestParam(value = "keyword") String keywords, @RequestParam(value = "building") Long buildingId){
        PageSearchDTO pageSearchDTO = new PageSearchDTO();
        String[] items = keywords.split(" ");
        boolean canAccess = SecurityUtils.isUserInRole("ROLE_ADMIN");
        if(canAccess == false){
            canAccess = accountRepository.isSubscriber(buildingId);
        }

        if(canAccess == true){
            pageSearchDTO.setNotifications(notificationRepository.searchNotifications(buildingId, items));
            pageSearchDTO.setMembers(memberRepository.searchMembers(buildingId, items));
        } else {
            if(permissionRepository.validateMemberInterceptor(buildingId, "NOTIFICATION_GET") == true){
                pageSearchDTO.setNotifications(notificationRepository.searchNotifications(buildingId, items));
            }
            if(permissionRepository.validateMemberInterceptor(buildingId, "MEMBER_GET") == true){
                pageSearchDTO.setMembers(memberRepository.searchMembers(buildingId, items));
            }
        }

        return new ResponseEntity<>(pageSearchDTO, HttpStatus.OK);
    }
}
