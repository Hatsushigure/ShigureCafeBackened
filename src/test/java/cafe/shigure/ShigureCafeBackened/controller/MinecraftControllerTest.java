package cafe.shigure.ShigureCafeBackened.controller;

import cafe.shigure.ShigureCafeBackened.model.Role;
import cafe.shigure.ShigureCafeBackened.model.User;
import cafe.shigure.ShigureCafeBackened.model.UserStatus;
import cafe.shigure.ShigureCafeBackened.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MinecraftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private cafe.shigure.ShigureCafeBackened.repository.UserAuditRepository userAuditRepository;

    @Autowired
    private cafe.shigure.ShigureCafeBackened.repository.NoticeRepository noticeRepository;

    @Autowired
    private cafe.shigure.ShigureCafeBackened.repository.ChatMessageRepository chatMessageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        chatMessageRepository.deleteAll();
        noticeRepository.deleteAll();
        userAuditRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetWhitelist() throws Exception {
        // ... (existing test logic)
    }

    @Test
    public void testGetChatMessages() throws Exception {
        // Create and save a user to satisfy @AuthenticationPrincipal User currentUser and #currentUser.id in @RateLimit
        User user = new User();
        user.setUsername("testuser");
        user.setNickname("TestUser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);

        // Save some messages
        cafe.shigure.ShigureCafeBackened.model.ChatMessage msg1 = new cafe.shigure.ShigureCafeBackened.model.ChatMessage();
        msg1.setName("Player1");
        msg1.setMessage("Msg 1");
        msg1.setTimestamp(System.currentTimeMillis() - 1000);
        chatMessageRepository.save(msg1);

        cafe.shigure.ShigureCafeBackened.model.ChatMessage msg2 = new cafe.shigure.ShigureCafeBackened.model.ChatMessage();
        msg2.setName("Player2");
        msg2.setMessage("Msg 2");
        msg2.setTimestamp(System.currentTimeMillis());
        chatMessageRepository.save(msg2);

        mockMvc.perform(get("/api/v1/minecraft/chat")
                        .with(user(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Player2"))
                .andExpect(jsonPath("$.content[1].name").value("Player1"));
    }
}
