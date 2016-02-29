package com.ozay.backend.service;

import com.ozay.backend.domain.User;
import com.ozay.backend.model.*;
import com.ozay.backend.repository.*;
import com.ozay.backend.web.rest.dto.OrganizationUserRoleDTO;
import com.ozay.backend.web.rest.form.RoleFormDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by naofumiezaki on 11/18/15.
 */
@Service
@Transactional
public class RoleService {

    @Inject
    RoleRepository roleRepository;

    @Inject
    RolePermissionRepository rolePermissionRepository;

    @Inject
    RoleMemberRepository roleMemberRepository;

    @Inject
    MemberRepository memberRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    TempUserRepository tempUserRepository;

    @Inject
    OrganizationUserRepository organizationUserRepository;


    public void delete(Role role){
        rolePermissionRepository.deleteAllByRoleId(role.getId());
        roleMemberRepository.deleteAllByRoleId(role.getId());
        roleRepository.delete(role);
    }

    public void create(RoleFormDTO roleFormDTO){
        roleRepository.create(roleFormDTO.getRole());
//        processRolePermission(roleFormDTO.getRole());
//        processRoleAssignedMember(roleFormDTO);
        this.rest(roleFormDTO);
    }

    public void update(RoleFormDTO roleFormDTO){
        roleRepository.update(roleFormDTO.getRole());
        this.rest(roleFormDTO);

    }
    private void rest(RoleFormDTO roleFormDTO){
        processRolePermission(roleFormDTO.getRole());
        processRoleAssignedMember(roleFormDTO);
    }

    private void processRolePermission(Role role){
        rolePermissionRepository.deleteAllByRoleId(role.getId());
        for(RolePermission rolePermission : role.getRolePermissions()){
            rolePermission.setRoleId(role.getId());
            rolePermissionRepository.create(rolePermission);
        }
    }
    private void processRoleAssignedMember(RoleFormDTO roleFormDTO){
        Long buildingId = roleFormDTO.getRole().getBuildingId();

        roleMemberRepository.deleteAllByRoleId(roleFormDTO.getRole().getId());

        for(OrganizationUserRoleDTO organizationUserRoleDTO : roleFormDTO.getOrganizationUserRoleDTOs()){
            OrganizationUser organizationUser = organizationUserRepository.findOne(organizationUserRoleDTO.getId());

            // Only assigned user goes process
            if(organizationUserRoleDTO.isAssigned() == true){
                Member member = memberRepository.findOneByOrganizationUserId(organizationUser.getId(), buildingId);
                // Organization User is already activated
                if(organizationUser.isActivated() == true){
                    if (member == null) {
                        User user = userRepository.findOne(organizationUser.getUserId());
                        if(user != null){
                            member = new Member();
                            member.setBuildingId(buildingId);
                            member.setOrganizationUserId(organizationUserRoleDTO.getId());
                            member.setUserId(user.getId());
                            member.setEmail(user.getEmail());
                            member.setFirstName(user.getFirstName());
                            member.setLastName(user.getLastName());
                            memberRepository.create(member);
                        }
                    }
                } else { // Organization User is not activated
                    if (member == null) {
                        TempUser tempUser = tempUserRepository.findOne(organizationUser.getTempUserId());
                        if(tempUser != null){
                            member = new Member();
                            member.setBuildingId(buildingId);
                            member.setOrganizationUserId(organizationUserRoleDTO.getId());
                            member.setEmail(tempUser.getEmail());
                            member.setFirstName(tempUser.getFirstName());
                            member.setLastName(tempUser.getLastName());
                            memberRepository.create(member);
                        }
                    }
                }

                if(member != null){
                    if(member.isDeleted() == true){
                        member.setDeleted(false);
                        memberRepository.update(member);
                    }
                    roleMemberRepository.create(roleFormDTO.getRole().getId(), member.getId());
                }

            } else { // assigned = false
                Member member = memberRepository.findOneByOrganizationUserId(organizationUser.getId(), buildingId);
                if(member != null){
                    if(roleMemberRepository.hasAnyRolesInBuilding(buildingId, member.getId()) == false){
                        member.setDeleted(true);
                        memberRepository.update(member);
                        roleMemberRepository.create(roleFormDTO.getRole().getId(), member.getId());
                    }
                }
            }
        }
    }
}
