package test.Controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.Controller.CommunityController;
import org.example.socialnetwork.DTO.CommunityDTO;
import org.example.socialnetwork.DTO.CommunityMemberDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Service.CommunityService;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public class CommunityControllerTest {
    @Mock
    private CommunityService communityService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommunityController communityController;

    private MockMvc mockMvc;

    private MyUserDetails mockUserDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(communityController).build();

        UserDTO user = new UserDTO();
        user.setUserId(1L); // Задаем ID пользователя
        user.setUserName("testUser"); // Задаем имя пользователя
        mockUserDetails = new MyUserDetails(user);
    }

    @Test
    public void testGetAllCommunities() throws Exception {
        CommunityDTO community1 = new CommunityDTO();
        CommunityDTO community2 = new CommunityDTO();
        when(communityService.getAllCommunities()).thenReturn(List.of(community1, community2));
        ResponseEntity<List<CommunityDTO>> response = communityController.getAllCommunities();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(communityService).getAllCommunities();
    }

    @Test
    public void testGetMembers() {
        Long communityId = 1L;
        CommunityMemberDTO memberDTO = new CommunityMemberDTO();
        when(communityService.getMembers(communityId)).thenReturn(List.of(memberDTO));

        ResponseEntity<List<CommunityMemberDTO>> response = communityController.getMembers(communityId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());

        verify(communityService).getMembers(communityId);
    }
}
