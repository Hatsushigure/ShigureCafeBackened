package cafe.shigure.ShigureCafeBackened.controller;

import cafe.shigure.ShigureCafeBackened.repository.NoticeRepository;
import cafe.shigure.ShigureCafeBackened.repository.UserAuditRepository;
import cafe.shigure.ShigureCafeBackened.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cache")
@RequiredArgsConstructor
public class CacheController {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final UserAuditRepository userAuditRepository;

    @GetMapping("/timestamps")
    public Map<String, LocalDateTime> getTimestamps() {
        Map<String, LocalDateTime> timestamps = new HashMap<>();
        LocalDateTime defaultTime = LocalDateTime.of(1970, 1, 1, 0, 0);
        timestamps.put("notices", noticeRepository.findMaxUpdatedAt().orElse(defaultTime));
        timestamps.put("users", userRepository.findMaxUpdatedAt().orElse(defaultTime));
        timestamps.put("audits", userAuditRepository.findMaxUpdatedAt().orElse(defaultTime));
        return timestamps;
    }
}
