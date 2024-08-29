package lazy.demo.auth_service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lazy.demo.auth_service.dto.resp.UserDetailResp;
import lazy.demo.auth_service.enums.UserProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

@SqlResultSetMapping(
        name = "UserDetailRespMapping",
        classes = @ConstructorResult(
                targetClass = UserDetailResp.class,
                columns = {
                        @ColumnResult(name = "user_id", type = Long.class),
                        @ColumnResult(name = "username", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "is_admin", type = Boolean.class),
                        @ColumnResult(name = "date_of_birth", type = LocalDate.class)
                }
        )
)
@NamedNativeQuery(
        name = "User.findUserDetailRespMapping",
        query = """
            SELECT
                u.user_id,
                u.username,
                u.email,
                u.is_admin,
                u.date_of_birth
            FROM
                users u
            WHERE
                u.username = :username OR u.email = :email
            """,
        resultSetMapping = "UserDetailRespMapping"
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String email;

    @JsonIgnore
    private String password;

    private boolean isAdmin;


    private UserProvider provider;
    private String fullName;
    private LocalDate dateOfBirth;
    private String address;
    private String picture;

}
