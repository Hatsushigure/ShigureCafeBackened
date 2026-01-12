package cafe.shigure.ShigureCafeBackened.service;

import cafe.shigure.ShigureCafeBackened.dto.NoticeResponse;
import cafe.shigure.ShigureCafeBackened.model.Notice;
import cafe.shigure.ShigureCafeBackened.model.NoticeReaction;
import cafe.shigure.ShigureCafeBackened.model.User;
import cafe.shigure.ShigureCafeBackened.repository.NoticeReactionRepository;
import cafe.shigure.ShigureCafeBackened.repository.NoticeRepository;
import cafe.shigure.ShigureCafeBackened.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;
    @Mock
    private NoticeReactionRepository noticeReactionRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoticeService noticeService;

    private User user;
    private Notice notice;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setNickname("Tester");

        notice = new Notice();
        notice.setId(1L);
        notice.setTitle("Test Title");
        notice.setContent("Test Content");
        notice.setAuthor(user);
        notice.setReactions(new ArrayList<>());
    }

    @Test
    void toggleReaction_shouldAddSecondReactionSuccessfully() {
        String emoji1 = "👍";
        String emoji2 = "❤️";

        when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(noticeRepository.saveAndFlush(any(Notice.class))).thenAnswer(i -> i.getArguments()[0]);

        // Add first reaction
        noticeService.toggleReaction(1L, user, emoji1);
        assertEquals(1, notice.getReactions().size());
        assertEquals(emoji1, notice.getReactions().get(0).getEmoji());

        // Add second reaction
        NoticeResponse response = noticeService.toggleReaction(1L, user, emoji2);

        assertEquals(2, notice.getReactions().size(), "Should have two reactions");
        assertTrue(notice.getReactions().stream().anyMatch(r -> r.getEmoji().equals(emoji1)));
        assertTrue(notice.getReactions().stream().anyMatch(r -> r.getEmoji().equals(emoji2)));
        assertEquals(2, response.getReactions().size());
    }

    @Test
    void toggleReaction_shouldRemoveReaction_whenSameEmojiToggled() {
        String emoji = "👍";
        NoticeReaction reaction = new NoticeReaction(notice, user, emoji);
        notice.getReactions().add(reaction);

        when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));
        when(noticeRepository.saveAndFlush(any(Notice.class))).thenAnswer(i -> i.getArguments()[0]);

        noticeService.toggleReaction(1L, user, emoji);

        assertEquals(0, notice.getReactions().size(), "Reaction should be removed");
    }
}
