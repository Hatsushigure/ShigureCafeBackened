package cafe.shigure.ShigureCafeBackened.dto;

import cafe.shigure.ShigureCafeBackened.model.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeReactionDTO implements Serializable {
    private ReactionType type;
    private Long count;
    private boolean reacted;
}
