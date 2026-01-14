package cafe.shigure.ShigureCafeBackened.controller;

import cafe.shigure.ShigureCafeBackened.dto.SystemUpdatesResponse;
import cafe.shigure.ShigureCafeBackened.service.NoticeService;
import cafe.shigure.ShigureCafeBackened.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system")
@RequiredArgsConstructor
public class SystemController {

    private final NoticeService noticeService;
    private final UserService userService;

    @GetMapping("/updates")
    public ResponseEntity<SystemUpdatesResponse> getUpdates() {
        return ResponseEntity.ok(new SystemUpdatesResponse(
                noticeService.getGlobalNoticeListTimestamp(),
                userService.getGlobalUserListTimestamp(),
                userService.getGlobalAuditListTimestamp()
        ));
    }
}
