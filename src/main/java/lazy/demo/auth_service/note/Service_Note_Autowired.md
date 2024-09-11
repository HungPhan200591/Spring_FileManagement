**Giải thích:**

Không nên sử dụng `@Autowired` vì:

1. **Khó khăn trong việc kiểm thử (Testing):**: Sử dụng `@Autowired` gây khó khăn khi viết các bài kiểm thử đơn vị (Unit Test) vì bạn không thể dễ dàng kiểm soát các phụ thuộc trong lớp của mình
2. **Khó bảo trì**: Khi dự án lớn lên, việc sử dụng `@Autowired` có thể dẫn đến các vấn đề về bảo trì và quản lý phụ thuộc.
3. **Không rõ ràng**: `@Autowired` có thể làm cho mã nguồn trở nên không rõ ràng về các phụ thuộc của lớp.
4. Không hỗ trợ Immutable Objects:Không hỗ trợ Immutable Objects:@Autowired trong trường (field) không cho phép tạo các đối tượng bất biến (immutable) vì các trường cần được thay đổi sau khi đối tượng được khởi tạo.
5. Khả năng tiêm phụ thuộc bị lỗi (Dependency Injection Failure)  nếu có nhiều hơn một bean của cùng một loại trong Spring context mà không được chỉ định rõ ràng.
6. @Autowired trong Spring sử dụng Java Reflection để thực hiện tiêm phụ thuộc. Điều này có thể làm chậm hiệu suất của ứng dụng.
   Cách @Autowired sử dụng Java Reflection:
   Khi Spring phát hiện một lớp có chú thích @Autowired, nó sẽ sử dụng cơ chế Java Reflection để dò tìm các trường, constructor, hoặc phương thức setter có chú thích này.
   Sau đó, Spring sẽ xác định loại của các phụ thuộc (beans) cần được tiêm và tìm kiếm các bean tương ứng trong Spring context.
   Spring sẽ tạo ra các thể hiện của các bean này (nếu chúng chưa tồn tại) và sau đó sử dụng Reflection để gán giá trị cho các trường, gọi constructor hoặc phương thức setter tương ứng để hoàn thành quá trình tiêm phụ thuộc.
   Ví dụ về cách sử dụng Java Reflection:
   Giả sử bạn có một lớp với một trường UserRepository được tiêm bằng @Autowired:
```java
@Component
public class UserService {
@Autowired
private UserRepository userRepository;

    public void createUser(User user) {
        userRepository.save(user);
    }
}
```
Trong quá trình khởi tạo của Spring, framework này sẽ sử dụng Java Reflection để:

1. Tìm ra rằng userRepository cần được tiêm phụ thuộc.
2. Tìm kiếm một bean thuộc loại UserRepository trong Spring context.
3. Sử dụng Reflection để gán giá trị của bean UserRepository vào trường userRepository.

Kết luận:
@Autowired thực sự dựa vào Java Reflection để thực hiện tiêm phụ thuộc, điều này cho phép Spring xử lý các phụ thuộc trong thời gian chạy (runtime) mà không cần phải thay đổi mã nguồn trực tiếp. Điều này giúp cho Spring có tính linh hoạt cao, nhưng đồng thời cũng làm tăng độ phức tạp và ảnh hưởng đến hiệu năng một chút do việc sử dụng Reflection.
Nên sử dụng Dependency Injection (DI) thông qua constructor vì:

1. **Dễ kiểm tra**: Dễ dàng kiểm tra các lớp bằng cách khởi tạo chúng với các phụ thuộc cần thiết mà không cần khởi tạo toàn bộ ngữ cảnh Spring.
2. **Rõ ràng**: Constructor injection làm cho các phụ thuộc của lớp rõ ràng hơn.
3. **Bất biến**: Các phụ thuộc được khởi tạo một lần duy nhất khi đối tượng được tạo, giúp đối tượng trở nên bất biến.

**Ví dụ:**

1. **Sử dụng `@Autowired` (không nên sử dụng):**

```java
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
```

2. **Sử dụng Constructor Injection (nên sử dụng):**

```java
@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
```

**Kiểm tra đơn vị (Unit Test) với Constructor Injection:**

```java

import lazy.demo.auth_service.repository.UserRepository;
import lazy.demo.auth_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceTest {

   @Test
   public void testFindByEmail() {
      UserRepository userRepository = Mockito.mock(UserRepository.class);
      UserService userService = new UserService(userRepository);

      User user = new User();
      user.setEmail("test@example.com");

      when(userRepository.findByEmail("test@example.com")).thenReturn(user);

      User result = userService.findByUserNameOrEmail("test@example.com");
      assertEquals("test@example.com", result.getEmail());
   }
}
```

https://quochung.cyou/3-cach-thuc-hien-dependency-injection-di-va-van-de-voi-autowired-trong-spring/


