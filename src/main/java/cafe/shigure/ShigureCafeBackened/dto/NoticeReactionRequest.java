package cafe.shigure.ShigureCafeBackened.dto;

import cafe.shigure.ShigureCafeBackened.model.ReactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeReactionRequest {
    @NotNull
    private ReactionType type;
}
