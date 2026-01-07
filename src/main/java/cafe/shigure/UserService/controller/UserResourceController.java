package cafe.shigure.UserService.controller;

import cafe.shigure.UserService.dto.UserResponse;
import cafe.shigure.UserService.model.Role;
import cafe.shigure.UserService.model.User;
import cafe.shigure.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserResourceController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(@RequestParam(required = false) String username) {
        if (username != null && !username.isEmpty()) {
             try {
                 User user = userService.getUserByUsername(username);
                 return ResponseEntity.ok(Collections.singletonList(mapToUserResponse(user)));
             } catch (Exception e) {
                 return ResponseEntity.ok(Collections.emptyList());
             }
        }
        
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(this::mapToUserResponse)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(mapToUserResponse(user));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        userService.deleteUser(user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{username}/password")
    public ResponseEntity<Void> changePassword(@PathVariable String username, @RequestBody ChangePasswordRequest request, @AuthenticationPrincipal User currentUser) {
        boolean isSelf = currentUser.getUsername().equals(username);
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isSelf && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User targetUser = userService.getUserByUsername(username);
        if (isAdmin && !isSelf) {
            userService.resetPassword(targetUser.getId(), request.newPassword());
        } else {
            userService.changePassword(targetUser.getId(), request.oldPassword(), request.newPassword());
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}/email")
    public ResponseEntity<Void> updateEmail(@PathVariable String username, @RequestBody cafe.shigure.UserService.dto.UpdateEmailRequest request, @AuthenticationPrincipal User currentUser) {
        if (!currentUser.getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = userService.getUserByUsername(username);
        userService.updateEmail(user.getId(), request.getNewEmail(), request.getVerificationCode());
        return ResponseEntity.ok().build();
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(user.getUsername(), user.getEmail(), user.getRole());
    }

    public record ChangePasswordRequest(String oldPassword, String newPassword) {}
}
