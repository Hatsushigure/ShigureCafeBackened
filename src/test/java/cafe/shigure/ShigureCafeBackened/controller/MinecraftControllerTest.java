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
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        noticeRepository.deleteAll();
        userAuditRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetWhitelist() throws Exception {
        // Create active user with Minecraft info
        User user = new User();
        user.setUsername("mcuser");
        user.setNickname("MC User");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setEmail("mc@example.com");
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);
        user.setMinecraftUsername("PlayerOne");
        user.setMinecraftUuid("12345678-1234-1234-1234-123456789012");
        userRepository.save(user);

        // Create active user with non-hyphenated Minecraft UUID
        User user2 = new User();
        user2.setUsername("mcuser2");
        user2.setNickname("MC User 2");
        user2.setPassword(passwordEncoder.encode("password123"));
        user2.setEmail("mc2@example.com");
        user2.setRole(Role.USER);
        user2.setStatus(UserStatus.ACTIVE);
        user2.setMinecraftUsername("PlayerTwo");
        user2.setMinecraftUuid("abcdef1234567890abcdef1234567890");
        userRepository.save(user2);

        // Create active user without Minecraft info
        User userNoMc = new User();
        userNoMc.setUsername("nomcuser");
        userNoMc.setNickname("No MC User");
        userNoMc.setPassword(passwordEncoder.encode("password123"));
        userNoMc.setEmail("nomc@example.com");
        userNoMc.setRole(Role.USER);
        userNoMc.setStatus(UserStatus.ACTIVE);
        userRepository.save(userNoMc);

        // Create pending user with Minecraft info (should be ignored)
        User pendingUser = new User();
        pendingUser.setUsername("pendingmc");
        pendingUser.setNickname("Pending MC");
        pendingUser.setPassword(passwordEncoder.encode("password123"));
        pendingUser.setEmail("pendingmc@example.com");
        pendingUser.setRole(Role.USER);
        pendingUser.setStatus(UserStatus.PENDING);
        pendingUser.setMinecraftUsername("PendingPlayer");
        pendingUser.setMinecraftUuid("00000000-0000-0000-0000-000000000000");
        userRepository.save(pendingUser);

        mockMvc.perform(get("/api/v1/minecraft/whitelist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.name == 'PlayerOne')].uuid").value("12345678-1234-1234-1234-123456789012"))
                .andExpect(jsonPath("$[?(@.name == 'PlayerTwo')].uuid").value("abcdef12-3456-7890-abcd-ef1234567890"));
    }
}
