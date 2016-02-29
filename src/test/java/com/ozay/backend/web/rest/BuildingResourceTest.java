package com.ozay.backend.web.rest;

import com.ozay.backend.Application;
import com.ozay.backend.domain.Authority;
import com.ozay.backend.domain.User;
import com.ozay.backend.model.Building;
import com.ozay.backend.repository.AuthorityRepository;
import com.ozay.backend.repository.BuildingRepository;
import com.ozay.backend.repository.UserRepository;
import com.ozay.backend.security.AuthoritiesConstants;
import com.ozay.backend.service.MailService;
import com.ozay.backend.service.UserService;
import com.ozay.backend.web.rest.dto.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BuildingResourceTest {

    @Inject
    private BuildingRepository buildingRepository;

    private MockMvc restBuildingMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        BuildingResource buildingResource = new BuildingResource();
        ReflectionTestUtils.setField(buildingResource, "buildingRepository", buildingRepository);

        this.restBuildingMockMvc = MockMvcBuilders.standaloneSetup(buildingResource).build();
    }

    @Test
    @Transactional
    public void testRegisterValid() throws Exception {

        Building building = new Building();
        long id = 111;
        building.setName("Test Apartment");
        building.setEmail("test@gmail.com");
        building.setCity("Test City");
        building.setStreet("Test Street");
        building.setCreatedBy(id);
        building.setPhone("123-121-1211");

    System.out.println("TTTTEEESSST");

        restBuildingMockMvc.perform(
            post("/api/buildings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(building)))
            .andExpect(status().isCreated());


        Building newBuilding = buildingRepository.findOne(building.getId());

    }
}
