package cafe.shigure.ShigureCafeBackened.controller;

import cafe.shigure.ShigureCafeBackened.dto.MinecraftWhitelistResponse;
import cafe.shigure.ShigureCafeBackened.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/minecraft")
@RequiredArgsConstructor
public class MinecraftController {

    private final UserService userService;

    @GetMapping("/whitelist")
    public ResponseEntity<List<MinecraftWhitelistResponse>> getWhitelist() {
        return ResponseEntity.ok(userService.getMinecraftWhitelist());
    }
}
