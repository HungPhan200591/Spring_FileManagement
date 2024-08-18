###  Spring AOP với Spring Security nhằm kiểm tra quyền truy cập trước khi thực hiện các method
- Để sử dụng Spring AOP, ta cần tạo một class với annotation @Aspect và các method với annotation @Before, @After, @Around, @AfterReturning, @AfterThrowing
- Để sử dụng Spring AOP với Spring Security, ta cần tạo một class extends từ org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler và override method createSecurityExpressionRoot
- Trong method createSecurityExpressionRoot, ta cần tạo một class extends từ org.springframework.security.access.expression.method.MethodSecurityExpressionRoot và override method hasPermission
- Trong method hasPermission, ta cần kiểm tra quyền truy cập của user
- Trong class extends từ DefaultMethodSecurityExpressionHandler, ta cần set methodSecurityExpressionHandler cho SecurityExpressionRoot

### Có thể sử dụng PermissionEvaluator và @PreAuthorize để kiểm tra quyền truy cập
### Pseudocode
1. **Tạo `CustomPermissionEvaluator`**: Tạo class để kiểm tra quyền truy cập.
2. **Cập nhật `MethodSecurityConfig`**: Cấu hình để sử dụng `CustomPermissionEvaluator`.
3. **Cập nhật `SecurityConfig`**: Cấu hình Spring Security để sử dụng `MethodSecurityConfig`.
4. **Sử dụng `@PreAuthorize`**: Áp dụng annotation `@PreAuthorize` vào các method cần kiểm tra quyền truy cập.

### Code

#### Tạo `CustomPermissionEvaluator`
```java
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final PermissionRepository permissionRepository;

    // Constructor để inject PermissionRepository
    public CustomPermissionEvaluator(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Override
    // Kiểm tra quyền truy cập dựa trên đối tượng và quyền cụ thể
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if (auth == null || permission == null) {
            return false; // Nếu auth hoặc permission là null, trả về false
        }

        // Lấy tên module và quyền từ các tham số
        String module = (targetDomainObject != null) ? targetDomainObject.toString().toUpperCase() : "";
        String perm = permission.toString().toUpperCase();

        // Kiểm tra quyền truy cập
        return hasPrivilege(auth, module, perm);
    }

    @Override
    // Kiểm tra quyền truy cập dựa trên ID đối tượng, loại đối tượng và quyền cụ thể
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if (auth == null || targetType == null || permission == null) {
            return false; // Nếu bất kỳ tham số nào là null, trả về false
        }

        // Kiểm tra quyền truy cập
        return hasPrivilege(auth, targetType.toUpperCase(), permission.toString().toUpperCase());
    }

    // Phương thức kiểm tra quyền truy cập
    private boolean hasPrivilege(Authentication auth, String module, String permission) {
        // Duyệt qua các quyền của user
        for (SimpleGrantedAuthority grantedAuthority : auth.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            // Nếu quyền của user khớp với quyền yêu cầu, trả về true
            if (authority.equals(module + "_" + permission)) {
                return true;
            }
        }

        // Kiểm tra quyền từ database
        String username = auth.getName();
        return permissionRepository.existsByUsernameAndModuleAndPermission(username, module, permission);
    }
}
```

#### Cập nhật `MethodSecurityConfig`
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected DefaultMethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        // Sử dụng CustomPermissionEvaluator
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return expressionHandler;
    }
}
```

#### Cập nhật `SecurityConfig`

```java
import lazy.demo.auth_service.config.security.jwt.JwtAuthenticationEntryPoint;
import lazy.demo.auth_service.config.security.jwt.JwtAuthenticationFilter;
import lazy.demo.auth_service.config.security.impl.UserInfoUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final UserInfoUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**", "/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(ss -> ss.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(_ -> {
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

#### Sử dụng `@PreAuthorize` trong `BookController`
```java
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {

    @GetMapping("/view")
    @PreAuthorize("hasPermission(null, 'book:view')")
    public String viewBook() {
        return "Viewing book";
    }

    @GetMapping("/edit")
    @PreAuthorize("hasPermission(null, 'book:edit')")
    public String editBook() {
        return "Editing book";
    }
}
```

### Cơ chế kiểm tra permission với cách code như trên

1. **Annotation `@PreAuthorize`**:
    - Khi một method được gọi và có annotation `@PreAuthorize`, Spring Security sẽ kiểm tra quyền truy cập trước khi thực hiện method đó.
    - Ví dụ: `@PreAuthorize("hasPermission(null, 'book:view')")` trong `BookController`.

2. **MethodSecurityConfig**:
    - `MethodSecurityConfig` cấu hình để sử dụng `CustomPermissionEvaluator` thông qua `DefaultMethodSecurityExpressionHandler`.
    - Khi `@PreAuthorize` được gọi, `DefaultMethodSecurityExpressionHandler` sẽ sử dụng `CustomPermissionEvaluator` để kiểm tra quyền.

3. **CustomPermissionEvaluator**:
    - `CustomPermissionEvaluator` implements `PermissionEvaluator` và override hai method `hasPermission`.
    - Khi `@PreAuthorize` gọi `hasPermission`, nó sẽ truyền các tham số `Authentication`, `targetDomainObject`, và `permission`.

4. **Kiểm tra quyền trong `CustomPermissionEvaluator`**:
    - Method `hasPermission` kiểm tra các tham số, nếu bất kỳ tham số nào là null, trả về false.
    - Lấy tên module và quyền từ các tham số.
    - Gọi method `hasPrivilege` để kiểm tra quyền.

5. **Method `hasPrivilege`**:
    - Duyệt qua các quyền của user từ `Authentication`.
    - Nếu quyền của user khớp với quyền yêu cầu, trả về true.
    - Nếu không, kiểm tra quyền từ database thông qua `PermissionRepository`.
