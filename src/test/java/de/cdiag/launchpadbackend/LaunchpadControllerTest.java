package de.cdiag.launchpadbackend;

import de.cdiag.launchpadbackend.model.Launchpad;
import de.cdiag.launchpadbackend.resources.LaunchpadController;
import de.cdiag.launchpadbackend.service.AppExecutorService;
import de.cdiag.launchpadbackend.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// instantiates only the web layer in the app context
@WebMvcTest(LaunchpadController.class)
@ExtendWith(SpringExtension.class)
public class LaunchpadControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

    @MockBean
    private AppExecutorService appExecutorService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    // ############ TESTING OF WEB LAYER ONLY #############

    @Test
    public void whenLoadAllTilesForUser_thenReturnWithStatusOk() throws Exception {
        when(userService.loadLaunchpad("user"))
                .thenReturn(new Launchpad());

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/launchpad/tile/all"))
                .andDo(print())
                .andExpect(status().isOk())
                // check complete response object
                .andExpect(jsonPath("$.status").value("OK"))
                .andReturn();

        Assert.assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void givenTemplate_whenAddTemplate_shouldReturnTile() {

    }

    @Test
    public void givenAppId_whenStartAppRequested_shouldReturnStatusOK() {

    }

    @Test
    public void givenAppId_whenStartAppRequestedAndAlreadyRunning_shouldReturnStatusMessageAlreadyRunning() {

    }

    @Test
    public void givenApplicationJson_whenUpdateApplNameAndDescription_shouldReturnUpdatedApp() {

    }

    @Test
    public void whenRequestingAllTemplates_shouldReturnTwoTemplates() {

    }
}