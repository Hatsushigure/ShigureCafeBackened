package cafe.shigure.ShigureCafeBackened.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponse {
    private Long id;
    private String title;
    private String content;
    private boolean pinned;
    private String authorNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
