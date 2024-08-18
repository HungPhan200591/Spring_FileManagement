### antMatchers, requestMatchers
`antMatchers` đã được thay thế bằng `requestMatchers` để làm rõ rằng phương thức này có thể sử dụng nhiều loại matcher khác nhau,
không chỉ là các matcher dựa trên mẫu (ant-style). 
Điều này có nghĩa là `requestMatchers` có thể sử dụng các loại matcher khác nhau như:

- **Ant-style matchers**: Sử dụng các ký tự đại diện như `*` và `**` để khớp với các mẫu URL.
- **Regex matchers**: Sử dụng biểu thức chính quy để khớp với các mẫu URL phức tạp hơn.
- **Exact matchers**: Khớp chính xác với các URL cụ thể.

Việc đổi tên này giúp làm rõ rằng phương thức `requestMatchers` linh hoạt hơn và có thể sử dụng nhiều loại matcher khác nhau 
để xác định các quy tắc bảo mật cho các URL.

Ant-style (another neat tool) là một kiểu mẫu (pattern) được sử dụng để khớp các đường dẫn URL trong Spring Security. Nó được đặt tên theo Apache Ant, một công cụ xây dựng phần mềm, và sử dụng các ký tự đại diện để xác định các mẫu URL. Dưới đây là một số ví dụ về ant-style patterns:

- `*` khớp với bất kỳ chuỗi ký tự nào trong một phần của đường dẫn URL.
    - Ví dụ: `/user/*` khớp với `/user/1`, `/user/abc`, nhưng không khớp với `/user/1/profile`.

- `**` khớp với bất kỳ chuỗi ký tự nào trong nhiều phần của đường dẫn URL.
    - Ví dụ: `/user/**` khớp với `/user/1`, `/user/1/profile`, `/user/abc/xyz`.

- `?` khớp với một ký tự duy nhất.
    - Ví dụ: `/user/?` khớp với `/user/a`, `/user/1`, nhưng không khớp với `/user/ab`.

Ant-style patterns giúp định nghĩa các quy tắc bảo mật một cách linh hoạt và dễ dàng hơn trong việc quản lý các URL trong ứng dụng.

+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
### oauth2Login
`oauth2Login` là một phương thức trong Spring Security dùng để cấu hình đăng nhập OAuth2 cho ứng dụng. Dưới đây là chi tiết về cách hoạt động của `oauth2Login`:

1. **Client Registration**: Đăng ký các thông tin của ứng dụng client với nhà cung cấp OAuth2 (như Google, Facebook, GitHub, v.v.). Thông tin này bao gồm client ID, client secret, và các URL chuyển hướng.

2. **Authorization Request**: Khi người dùng cố gắng đăng nhập, ứng dụng sẽ chuyển hướng người dùng đến trang xác thực của nhà cung cấp OAuth2. Tại đây, người dùng sẽ xác thực và ủy quyền cho ứng dụng truy cập thông tin của họ.

3. **Authorization Response**: Sau khi người dùng ủy quyền, nhà cung cấp OAuth2 sẽ chuyển hướng người dùng trở lại ứng dụng với một mã ủy quyền (authorization code).

4. **Token Request**: Ứng dụng sử dụng mã ủy quyền này để yêu cầu mã thông báo truy cập (access token) từ nhà cung cấp OAuth2.

5. **Token Response**: Nhà cung cấp OAuth2 trả về mã thông báo truy cập, và có thể kèm theo mã thông báo làm mới (refresh token).

6. **User Info Request**: Ứng dụng sử dụng mã thông báo truy cập để yêu cầu thông tin người dùng từ nhà cung cấp OAuth2.

7. **User Info Response**: Nhà cung cấp OAuth2 trả về thông tin người dùng. Ứng dụng sử dụng thông tin này để xác thực và tạo phiên làm việc cho người dùng.

8. **Redirection**: Sau khi xác thực thành công, ứng dụng chuyển hướng người dùng đến trang đích (thường là trang chủ hoặc trang quản lý tài khoản).

Ví dụ cấu hình `oauth2Login`:

```java
.oauth2Login(oauth2Login ->
    oauth2Login
        .clientRegistrationRepository(clientRegistrationRepository())
        .authorizedClientService(authorizedClientService())
        .defaultSuccessUrl("/loginSuccess")
        .failureUrl("/loginFailure")
);
```

Trong đó:
- `clientRegistrationRepository()` và `authorizedClientService()` là các bean cung cấp thông tin đăng ký client và dịch vụ quản lý client đã ủy quyền.
- `defaultSuccessUrl("/loginSuccess")` và `failureUrl("/loginFailure")` là các URL để chuyển hướng sau khi đăng nhập thành công hoặc thất bại.

Spring secutỉy PasswordEncoder có những loại nào và nên sử dụng cái nào?
Trong Spring Security, `PasswordEncoder` có một số loại phổ biến như sau:

1. **BCryptPasswordEncoder**:
  - Đây là loại `PasswordEncoder` phổ biến nhất. Nó sử dụng thuật toán BCrypt, một trong những thuật toán mã hóa mạnh mẽ và an toàn nhất hiện nay. BCrypt thêm một lớp "salt" (muối) vào mật khẩu trước khi mã hóa, làm tăng tính bảo mật.
  - **Nên sử dụng**: Đây là lựa chọn tốt nhất cho hầu hết các trường hợp vì mức độ bảo mật cao.

2. **Pbkdf2PasswordEncoder**:
  - Sử dụng thuật toán PBKDF2 với khả năng cấu hình số lần lặp lại và độ dài của "salt". Đây cũng là một thuật toán mã hóa mạnh và an toàn.
  - **Sử dụng khi**: Bạn cần cấu hình chi tiết hơn về thuật toán mã hóa, như số lần lặp lại để tăng tính bảo mật.

3. **SCryptPasswordEncoder**:
  - Sử dụng thuật toán SCrypt, được thiết kế để chống lại các cuộc tấn công brute-force nhờ việc tiêu thụ nhiều bộ nhớ và thời gian xử lý.
  - **Sử dụng khi**: Bạn muốn chống lại các cuộc tấn công từ các phần cứng chuyên dụng như GPU.

4. **NoOpPasswordEncoder**:
  - Không mã hóa mật khẩu, lưu mật khẩu dưới dạng plain text (rất không an toàn).
  - **Không nên sử dụng**: Chỉ sử dụng trong môi trường phát triển hoặc khi không cần bảo mật.

5. **StandardPasswordEncoder**:
  - Sử dụng thuật toán SHA-256 cùng với "salt". Tuy nhiên, nó đã lỗi thời và không được khuyến khích sử dụng trong các dự án mới.
  - **Không nên sử dụng**: Vì tính bảo mật kém hơn các giải pháp khác như BCrypt hoặc SCrypt.

### Lời khuyên:
- **BCryptPasswordEncoder** là lựa chọn tốt nhất cho hầu hết các trường hợp vì tính bảo mật cao, dễ sử dụng và đã được chứng minh trong thực tế.


so sánh SCryptPasswordEncoder và BCryptPasswordEncoder, tại sao SCryptPasswordEncoder  chống lại các cuộc tấn công từ các phần cứng chuyên dụng như GPU. BCryptPasswordEncoder có chống đc không?
### So sánh SCryptPasswordEncoder và BCryptPasswordEncoder

1. **BCryptPasswordEncoder**:
  - **Thuật toán**: BCrypt
  - **Tính năng**: Sử dụng thuật toán bcrypt để mã hóa mật khẩu với một giá trị "salt" ngẫu nhiên và khả năng cấu hình số vòng (work factor) để tăng độ phức tạp.
  - **Bảo mật**: BCrypt đã được kiểm chứng qua nhiều năm sử dụng và được coi là rất an toàn cho việc mã hóa mật khẩu.
  - **Hiệu suất**: BCrypt tối ưu cho việc chạy trên CPU, và mặc dù nó có thể bị tấn công bởi phần cứng chuyên dụng như GPU, nhưng số vòng (work factor) có thể được tăng lên để làm chậm các cuộc tấn công này. Tuy nhiên, nó không được thiết kế đặc biệt để chống lại GPU.

2. **SCryptPasswordEncoder**:
  - **Thuật toán**: SCrypt
  - **Tính năng**: SCrypt sử dụng nhiều tài nguyên không chỉ về thời gian mà còn về bộ nhớ, khiến nó tốn kém hơn khi thực hiện trên các thiết bị phần cứng chuyên dụng như GPU.
  - **Bảo mật**: SCrypt được thiết kế để chống lại các cuộc tấn công brute-force sử dụng phần cứng chuyên dụng, đặc biệt là GPU và ASIC (Application-Specific Integrated Circuit). SCrypt yêu cầu không chỉ nhiều vòng tính toán mà còn sử dụng một lượng lớn bộ nhớ, khiến việc tối ưu hóa trên GPU khó khăn hơn.
  - **Hiệu suất**: SCrypt có thể yêu cầu tài nguyên hệ thống cao hơn so với BCrypt, đặc biệt là bộ nhớ. Điều này làm cho SCrypt mạnh hơn trong các tình huống cần chống lại tấn công từ các hệ thống phần cứng mạnh.

### Tại sao SCrypt chống lại được các cuộc tấn công từ phần cứng chuyên dụng như GPU?

- **Bộ nhớ cao (Memory-hard)**: SCrypt yêu cầu sử dụng một lượng lớn bộ nhớ trong quá trình mã hóa mật khẩu. GPU thường có nhiều lõi xử lý nhưng lượng bộ nhớ trên mỗi lõi thường hạn chế, do đó việc chạy SCrypt trên GPU không hiệu quả như trên CPU.
- **CPU-GPU asymmetry**: Thuật toán SCrypt được thiết kế để tận dụng điểm yếu của GPU trong xử lý các tác vụ yêu cầu nhiều bộ nhớ. Điều này làm cho SCrypt ít bị ảnh hưởng bởi các cuộc tấn công brute-force sử dụng GPU, vì các phần cứng này không thể tối ưu hóa việc tính toán với SCrypt như với các thuật toán khác.

### BCrypt có chống được các cuộc tấn công từ GPU không?

- **Khả năng chống lại**: BCrypt có thể chống lại các cuộc tấn công từ GPU nhờ vào việc tăng số vòng (work factor), nhưng không hiệu quả bằng SCrypt. Điều này là do BCrypt chủ yếu sử dụng CPU-bound, tức là dựa vào việc tăng thời gian tính toán hơn là tiêu thụ bộ nhớ. GPU có thể xử lý nhiều tác vụ song song, do đó một GPU mạnh vẫn có thể tấn công một mật khẩu mã hóa bằng BCrypt nhanh hơn so với SCrypt.

### Kết luận

- **Nếu ưu tiên bảo mật chống lại các cuộc tấn công sử dụng phần cứng mạnh**: SCryptPasswordEncoder là lựa chọn tốt hơn vì nó được thiết kế để chống lại các cuộc tấn công brute-force trên GPU và ASIC.
- **Nếu cần sự kết hợp giữa bảo mật và hiệu suất**: BCryptPasswordEncoder vẫn là một lựa chọn rất tốt, đặc biệt trong các ứng dụng mà tấn công từ GPU không phải là mối đe dọa lớn.

### So sánh BCrypt và Pbkdf2PasswordEncoder

#### 1. **Thuật toán sử dụng**
- **BCrypt**:
  - Sử dụng thuật toán **Blowfish** đã được điều chỉnh để làm chậm quá trình mã hóa, chống lại các cuộc tấn công brute-force.
  - BCrypt bao gồm việc tạo ra một "salt" ngẫu nhiên và kết hợp với mật khẩu để mã hóa, sau đó thực hiện nhiều vòng (work factor) để tăng độ phức tạp của việc tính toán.

- **PBKDF2 (Pbkdf2PasswordEncoder)**:
  - Sử dụng thuật toán **HMAC** (Hash-based Message Authentication Code) kết hợp với các hàm băm phổ biến như SHA-1, SHA-256.
  - PBKDF2 áp dụng nhiều vòng (iteration) để làm chậm quá trình mã hóa, tương tự như BCrypt, và cũng sử dụng một "salt" ngẫu nhiên.

#### 2. **Mức độ bảo mật**
- **BCrypt**:
  - **Mạnh về bảo mật**: Được thiết kế đặc biệt cho việc mã hóa mật khẩu với tính năng tạo "salt" và điều chỉnh work factor (tăng độ phức tạp theo thời gian).
  - **Chống tấn công brute-force**: Tốt, nhưng không tiêu thụ nhiều bộ nhớ như SCrypt, do đó vẫn có thể bị tấn công bởi phần cứng mạnh như GPU hoặc ASIC.

- **PBKDF2**:
  - **Bảo mật cao**: Sử dụng nhiều vòng lặp (iterations) và "salt" để bảo vệ mật khẩu. PBKDF2 được khuyến nghị bởi NIST (National Institute of Standards and Technology) cho việc bảo vệ mật khẩu.
  - **Tính linh hoạt**: PBKDF2 cho phép cấu hình số lượng vòng lặp và loại hàm băm được sử dụng, giúp tăng độ bảo mật tùy theo nhu cầu.

#### 3. **Hiệu suất**
- **BCrypt**:
  - **Hiệu suất tương đối tốt**: Mặc dù có thể điều chỉnh số vòng lặp để làm chậm quá trình mã hóa, BCrypt vẫn tiêu thụ ít bộ nhớ và chủ yếu phụ thuộc vào CPU.
  - **Thời gian mã hóa**: Thường tương đối ổn định với cấu hình work factor thích hợp.

- **PBKDF2**:
  - **Hiệu suất linh hoạt**: Có thể cấu hình số lượng vòng lặp để đạt được độ trễ mong muốn. Tuy nhiên, khi số vòng lặp tăng, thời gian mã hóa cũng sẽ tăng tương ứng.
  - **Sử dụng trong nhiều hệ thống**: PBKDF2 là một trong những thuật toán phổ biến và được hỗ trợ rộng rãi trong nhiều ngôn ngữ lập trình và thư viện bảo mật.

#### 4. **Tính tương thích**
- **BCrypt**:
  - **Hỗ trợ mạnh trong Java/Spring**: BCryptPasswordEncoder là một phần của Spring Security và được hỗ trợ tốt trong hệ sinh thái Java.
  - **Giới hạn ngôn ngữ**: Mặc dù phổ biến, nhưng không phải tất cả các ngôn ngữ lập trình đều hỗ trợ BCrypt tốt như Java.

- **PBKDF2**:
  - **Tính tương thích cao**: PBKDF2 là một tiêu chuẩn mở, được hỗ trợ bởi hầu hết các ngôn ngữ lập trình và thư viện mã hóa.
  - **Ứng dụng rộng rãi**: Ngoài việc sử dụng trong Spring Security, PBKDF2 còn được sử dụng trong nhiều hệ thống bảo mật khác.

#### 5. **Sử dụng thực tế**
- **BCrypt**:
  - **Dễ sử dụng**: Trong các ứng dụng Spring Security, việc sử dụng BCrypt là rất đơn giản và bảo mật cao cho hầu hết các ứng dụng web.
  - **Thích hợp cho hầu hết các trường hợp**: Đặc biệt là những nơi bảo mật cao nhưng không đối mặt trực tiếp với các mối đe dọa từ phần cứng chuyên dụng.

- **PBKDF2**:
  - **Linh hoạt và mạnh mẽ**: Phù hợp cho các ứng dụng yêu cầu cấu hình chi tiết về bảo mật, đặc biệt là trong các hệ thống cần tuân thủ các tiêu chuẩn bảo mật như PCI-DSS.
  - **Lựa chọn khi cần tương thích đa nền tảng**: Thích hợp khi hệ thống cần hỗ trợ nhiều ngôn ngữ lập trình hoặc khi có yêu cầu cụ thể từ các tiêu chuẩn bảo mật.

### Kết luận
- **BCrypt** là một lựa chọn mạnh mẽ và đơn giản cho việc mã hóa mật khẩu trong các ứng dụng thông thường. Nó cung cấp sự bảo mật cao và dễ dàng tích hợp trong các ứng dụng Spring Security.
- **PBKDF2** mang lại sự linh hoạt và khả năng tương thích cao hơn, phù hợp cho các hệ thống cần bảo mật mạnh mẽ và có yêu cầu tuân thủ các tiêu chuẩn bảo mật nghiêm ngặt. Nếu bạn cần tùy chỉnh sâu hơn về bảo mật hoặc tương thích với nhiều hệ thống khác nhau, PBKDF2 có thể là lựa chọn tốt hơn.





