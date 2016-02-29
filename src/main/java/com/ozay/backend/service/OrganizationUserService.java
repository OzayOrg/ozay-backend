package com.ozay.backend.service;

import com.ozay.backend.domain.User;
import com.ozay.backend.model.TempUser;
import com.ozay.backend.model.OrganizationUser;
import com.ozay.backend.model.OrganizationUserPermission;
import com.ozay.backend.repository.*;
import com.ozay.backend.web.rest.dto.OrganizationUserDTO;
import com.ozay.backend.web.rest.form.OrganizationUserRegisterDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by naofumiezaki on 11/19/15.
 */
@Service
@Transactional
public class OrganizationUserService {

    @Inject
    TempUserRepository tempUserRepository;

    @Inject
    OrganizationUserRepository organizationUserRepository;

    @Inject
    OrganizationUserPermissionRepository organizationUserPermissionRepository;

    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;

    @Inject
    MemberRepository memberRepository;

    // If A user record exists in jhi_user table = Existing user
    public void processExistingUser(User user, OrganizationUserDTO organizationUserDTO){
        OrganizationUser organizationUser = new OrganizationUser();
        organizationUser.setUserId(user.getId());
        organizationUser.setOrganizationId(organizationUserDTO.getOrganizationId());
        organizationUser.setActivated(true);
        organizationUserRepository.create(organizationUser);
        organizationUserDTO.setId(organizationUser.getId());
        processPermissions(organizationUserDTO);
    }


    public void createNonExistingUser(OrganizationUserDTO organizationUserDTO){
        // Create Invited User Record
        TempUser tempUser = new TempUser();
        tempUser.setFirstName(organizationUserDTO.getFirstName());
        tempUser.setLastName(organizationUserDTO.getLastName());
        tempUser.setEmail(organizationUserDTO.getEmail());
        tempUserRepository.create(tempUser);

        // Create a record for bridge
        organizationUserDTO.setUserId(tempUser.getId());
        OrganizationUser organizationUser = new OrganizationUser();
        organizationUser.setTempUserId(tempUser.getId());
        organizationUser.setOrganizationId(organizationUserDTO.getOrganizationId());
        organizationUser.setActivated(false);

        organizationUserRepository.create(organizationUser);

        // Set Organization User ID
        organizationUserDTO.setId(organizationUser.getId());

        // Add permission records
        processPermissions(organizationUserDTO);

    }

    public void updateNonExistingUser(OrganizationUserDTO organizationUserDTO){
        TempUser tempUser = new TempUser();
        tempUser.setId(organizationUserDTO.getUserId());
        tempUser.setFirstName(organizationUserDTO.getFirstName());
        tempUser.setLastName(organizationUserDTO.getLastName());
        tempUser.setEmail(organizationUserDTO.getEmail());
        tempUserRepository.update(tempUser);
        processPermissions(organizationUserDTO);
    }

    public void processPermissions(OrganizationUserDTO organizationUserDTO){
        organizationUserPermissionRepository.deleteAllByOrganizationUserId(organizationUserDTO.getId());
        for(OrganizationUserPermission organizationUserPermission : organizationUserDTO.getOrganizationUserPermissions()){
            organizationUserPermission.setOrganizationUserId(organizationUserDTO.getId());
            organizationUserPermissionRepository.create(organizationUserPermission);
        }
    }

    public void processOrganizationUserInformation(OrganizationUserRegisterDTO organizationUserRegisterDTO, TempUser tempUser, OrganizationUser organizationUser){
        User user = userService.createUserInformation(
            organizationUserRegisterDTO.getLogin(),
            organizationUserRegisterDTO.getPassword(),
            tempUser.getFirstName(),
            tempUser.getLastName(),
            tempUser.getEmail().toLowerCase(),
            organizationUserRegisterDTO.getLangKey()
        );

        user.setActivated(true);
        user.setActivationKey(tempUser.getActivationKey());
        userRepository.save(user);

        tempUser.setActivated(true);
        tempUserRepository.update(tempUser);

        // Update organization user table
        organizationUser.setUserId(user.getId());
        organizationUser.setActivated(true);
        organizationUserRepository.update(organizationUser);


        // Update Member table by organization user id
        memberRepository.updateUserIdByOrganizationId(user.getId(), organizationUser.getId());
    }
}
