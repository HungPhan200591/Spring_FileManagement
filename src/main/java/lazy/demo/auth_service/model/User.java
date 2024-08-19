package lazy.demo.auth_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "users_uq_username", columnNames = "username"),
        @UniqueConstraint(name = "users_uq_email", columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String email;

    private String password;
    private boolean isAdmin;

    private String fullName;
    private LocalDate dateOfBirth;
    private String address;
    private String picture;

}
