package cafe.shigure.ShigureCafeBackened.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_audits")
public class UserAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String auditCode;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public UserAudit(User user, String auditCode, int expirationDays) {
        this.user = user;
        this.auditCode = auditCode;
        this.expiryDate = LocalDateTime.now().plusDays(expirationDays);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
