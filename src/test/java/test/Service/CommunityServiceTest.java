package test.Service;
import org.example.socialnetwork.DTO.CommunityDTO;
import org.example.socialnetwork.DTO.CommunityMemberDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.Community;
import org.example.socialnetwork.Model.CommunityMember;
import org.example.socialnetwork.Repository.CommunityMemberRepository;
import org.example.socialnetwork.Repository.CommunityRepository;
import org.example.socialnetwork.Service.CommunityService;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommunityServiceTest {
    @InjectMocks
    private CommunityService communityService;

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private CommunityMemberRepository communityMemberRepository;

    private UserDTO admin;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        admin = new UserDTO();
        admin.setUserName("Admin");
        admin.setUserId(1L);
    }

    @Test
    public void testCreateCommunity() {
        CommunityDTO community = new CommunityDTO();
        community.setCommunityName("Тестовое сообщество");
        community.setDescription("Описание тестового сообщества");
        when(communityRepository.save(any(Community.class))).thenReturn(convertToEntity(community));
        CommunityDTO createdCommunity = communityService.createCommunity("Тестовое сообщество", "Описание тестового сообщества", admin);
        assertNotNull(createdCommunity);
        assertEquals("Тестовое сообщество", createdCommunity.getCommunityName());
        verify(communityRepository, times(1)).save(any(Community.class));
    }

    @Test
    public void testGetAllCommunities() {
        when(communityRepository.findAll()).thenReturn(Collections.singletonList(new Community()));
        List<CommunityDTO> communities = communityService.getAllCommunities();
        assertNotNull(communities);
        assertEquals(1, communities.size());
        verify(communityRepository, times(1)).findAll();
    }

    @Test
    public void testJoinCommunity() {
        CommunityDTO community = new CommunityDTO();
        community.setId(1L);
        community.setCommunityName("Тестовое сообщество");
        when(communityRepository.findById(1L)).thenReturn(Optional.of(convertToEntity(community)));
        when(communityMemberRepository.save(any(CommunityMember.class))).thenReturn(new CommunityMember());
        CommunityMemberDTO member = communityService.joinCommunity(1L, admin);
        assertNotNull(member);
        verify(communityRepository, times(1)).findById(1L);
        verify(communityMemberRepository, times(1)).save(any(CommunityMember.class));
    }

    @Test
    public void testGetMembers() {
        CommunityDTO community = new CommunityDTO();
        community.setId(1L);
        when(communityRepository.findById(1L)).thenReturn(Optional.of(convertToEntity(community)));
        CommunityMember member = new CommunityMember();
        when(communityMemberRepository.findByCommunity(any())).thenReturn(Collections.singletonList(member));
        List<CommunityMemberDTO> members = communityService.getMembers(1L);
        assertNotNull(members);
        assertEquals(1, members.size());
        verify(communityRepository, times(1)).findById(1L);
    }

    @Test
    public void testJoinCommunityNotFound() {
        when(communityRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            communityService.joinCommunity(1L, admin);
        });
        assertEquals("Сообщество не найдено", exception.getMessage());
        verify(communityRepository, times(1)).findById(1L);
    }

    private Community convertToEntity(CommunityDTO communityDTO) {
        if (communityDTO == null) {
            return null;
        }
        Community community = new Community();
        community.setId(communityDTO.getId());
        community.setCommunityName(communityDTO.getCommunityName());
        community.setDescription(communityDTO.getDescription());
        community.setCreated_at(communityDTO.getCreated_at());
        community.setAdmin(communityDTO.getAdmin());
        return community;
    }
}
