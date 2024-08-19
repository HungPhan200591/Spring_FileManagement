### `RestTemplate` và `WebClient` là hai công cụ phổ biến để thực hiện các yêu cầu HTTP trong ứng dụng Spring Boot, nhưng chúng có một số khác biệt quan trọng về cách hoạt động và tính năng. Dưới đây là so sánh giữa `RestTemplate` và `WebClient`:


**WebClient được giới thiệu trong Spring 5 và là một phần của module Spring WebFlux. Để sử dụng WebClient, bạn cần có ít nhất Spring Framework 5.0.0 hoặc phiên bản cao hơn. 
Spring Boot: Nếu bạn đang sử dụng Spring Boot, phiên bản tối thiểu tương ứng với Spring 5.0.0 là Spring Boot 2.0.0.**
### 1. **RestTemplate**

**`RestTemplate`** là thư viện client HTTP đồng bộ (synchronous) trong Spring Framework. Nó đã được sử dụng rộng rãi trong nhiều ứng dụng Spring trước khi `WebClient` ra đời.

- **Khả năng đồng bộ (Synchronous):** `RestTemplate` thực hiện các yêu cầu HTTP một cách đồng bộ, nghĩa là mã sẽ chờ đợi cho đến khi nhận được phản hồi từ máy chủ trước khi tiếp tục thực hiện các lệnh tiếp theo.

- **Cấu hình và Tinh chỉnh:** `RestTemplate` hỗ trợ nhiều cấu hình như các `MessageConverter` để chuyển đổi giữa các định dạng dữ liệu khác nhau, cấu hình giao thức (HTTP/HTTPS), và các tùy chọn kết nối.

- **Cách sử dụng:** Nó có cách sử dụng đơn giản với các phương thức như `getForObject()`, `postForObject()`, `put()`, `delete()`, v.v.

- **Hỗ trợ tích hợp:** Nó hỗ trợ dễ dàng với các tính năng khác của Spring như `Spring Security` và `Spring Data`.

**Ví dụ:**

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestTemplateExample {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.example.com/data";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
    }
}
```

### 2. **WebClient**

**`WebClient`** là một công cụ client HTTP không đồng bộ (asynchronous) được giới thiệu trong Spring 5 như một phần của Spring WebFlux. `WebClient` cung cấp một API hiện đại hơn để thực hiện các yêu cầu HTTP và xử lý phản hồi.

- **Khả năng không đồng bộ (Asynchronous):** `WebClient` hỗ trợ cả cách tiếp cận đồng bộ và không đồng bộ, cho phép bạn thực hiện các yêu cầu HTTP mà không cần chặn luồng chính, giúp tăng khả năng mở rộng và hiệu suất của ứng dụng.

- **Tính năng nâng cao:** `WebClient` hỗ trợ các tính năng như reactive streams, tích hợp tốt với các ứng dụng Spring WebFlux và các thư viện reactive khác, hỗ trợ các giao thức như WebSocket, và có khả năng xử lý lỗi mạnh mẽ.

- **Cấu hình và Tinh chỉnh:** `WebClient` hỗ trợ cấu hình chi tiết hơn và dễ dàng tùy chỉnh các phần như retry, fallback, và các cấu hình khác.

- **API Linh hoạt:** `WebClient` cung cấp một API linh hoạt hơn với phương thức `get()`, `post()`, `put()`, `delete()`, và các tính năng nâng cao như xử lý phản hồi dạng flux và mono.

**Ví dụ:**

```java
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientExample {

    public static void main(String[] args) {
        WebClient webClient = WebClient.create("https://api.example.com");

        Mono<String> response = webClient.get()
                                         .uri("/data")
                                         .retrieve()
                                         .bodyToMono(String.class);

        response.subscribe(System.out::println);
    }
}
```

### So Sánh

| Tính Năng        | `RestTemplate`                     | `WebClient`                           |
|------------------|-----------------------------------|--------------------------------------|
| **Phong cách**   | Đồng bộ (synchronous)             | Không đồng bộ (asynchronous)         |
| **API**          | Đơn giản và dễ sử dụng            | Linh hoạt và mạnh mẽ hơn             |
| **Xử lý lỗi**    | Có thể cần cấu hình thêm          | Có tính năng xử lý lỗi mạnh mẽ hơn    |
| **Hiệu suất**    | Không hỗ trợ xử lý không đồng bộ   | Hỗ trợ xử lý không đồng bộ, hiệu suất cao hơn |
| **Tích hợp**     | Hỗ trợ tốt với Spring và các thư viện khác | Tích hợp tốt với Spring WebFlux và các thư viện reactive |
| **Phương thức**  | `getForObject()`, `postForObject()`, v.v. | `get()`, `post()`, `put()`, `delete()`, v.v. |

### Kết Luận

- **`RestTemplate`** là lựa chọn phù hợp nếu bạn đang làm việc với ứng dụng đồng bộ và cần một công cụ đơn giản để thực hiện các yêu cầu HTTP.
- **`WebClient`** là lựa chọn ưu tiên nếu bạn muốn tận dụng các tính năng không đồng bộ và reactive, hoặc nếu bạn đang phát triển ứng dụng Spring WebFlux và cần các tính năng nâng cao.

Với sự phát triển của Spring Framework và Spring WebFlux, **`WebClient`** đang dần trở thành công cụ tiêu chuẩn cho các yêu cầu HTTP trong các ứng dụng Spring Boot hiện đại.

+-----------------------------------------------------------------------------------------------+

`RestTemplate` và `WebClient` phục vụ các nhu cầu khác nhau và vì vậy, cả hai vẫn còn được duy trì trong hệ sinh thái Spring. Dưới đây là lý do tại sao `RestTemplate` vẫn được sử dụng và không bị loại bỏ dù `WebClient` đã được giới thiệu:

### 1. **Tính Tương Thích Ngược**

- **Ứng Dụng Cũ:** `RestTemplate` đã được sử dụng rộng rãi trong nhiều ứng dụng Spring trước khi `WebClient` được giới thiệu. Nhiều ứng dụng vẫn dựa vào `RestTemplate` và việc loại bỏ nó có thể gây ra sự không tương thích và yêu cầu nỗ lực chuyển đổi lớn.

### 2. **Khả Năng và Tính Năng**

- **Đồng Bộ vs. Bất Đồng Bộ:** `RestTemplate` hoạt động theo cách đồng bộ và chặn (blocking), điều này có thể đơn giản hơn trong một số tình huống và dễ dàng hơn để tích hợp với mã hiện tại. `WebClient`, ngược lại, hỗ trợ giao tiếp bất đồng bộ và không chặn (non-blocking), điều này mang lại hiệu suất tốt hơn trong các ứng dụng cần xử lý nhiều yêu cầu đồng thời.

### 3. **Ứng Dụng và Cấu Trúc**

- **Ứng Dụng Đơn Giản:** `RestTemplate` có thể đơn giản hơn cho các ứng dụng nhỏ hoặc khi bạn chỉ cần thực hiện các yêu cầu HTTP một cách đơn giản và đồng bộ.
- **Ứng Dụng Phức Tạp:** `WebClient` được thiết kế cho các ứng dụng yêu cầu xử lý bất đồng bộ, và nó hỗ trợ các tính năng như backpressure và streaming, điều này rất hữu ích cho các ứng dụng phức tạp hơn.

### 4. **Hỗ Trợ và Tính Năng**

- **Hỗ Trợ Đang Được Cung Cấp:** `RestTemplate` vẫn được duy trì và cập nhật với các bản vá lỗi và tính năng bảo mật, mặc dù nó không còn được phát triển thêm các tính năng mới như `WebClient`.
- **Chuyển Đổi Mượt Mà:** Nhiều tổ chức vẫn chưa chuyển đổi hoàn toàn từ `RestTemplate` sang `WebClient` vì lý do chi phí và độ phức tạp.

### 5. **Tài Liệu và Cộng Đồng**

- **Tài Liệu Hiện Tại:** Có rất nhiều tài liệu, hướng dẫn và mã nguồn sử dụng `RestTemplate`, và việc loại bỏ nó có thể làm giảm khả năng hỗ trợ và tài nguyên cho cộng đồng phát triển.

### Kết Luận

`RestTemplate` vẫn còn được duy trì vì nó phục vụ một mục đích và nhu cầu nhất định trong cộng đồng phát triển. Mặc dù `WebClient` cung cấp nhiều tính năng hiện đại hơn và là lựa chọn ưu tiên cho các ứng dụng yêu cầu bất đồng bộ, `RestTemplate` vẫn là một công cụ hữu ích cho nhiều ứng dụng và trường hợp sử dụng. Việc chuyển đổi giữa hai công cụ nên được thực hiện dựa trên yêu cầu cụ thể của ứng dụng và tính khả thi trong quá trình phát triển.

### Tại sao đổi sang MultiValueMap lại được mà để Map bth ko đc khi sử dụng content type application/x-www-form-urlencoded?
Trong Spring WebFlux, khi bạn muốn gửi dữ liệu dưới dạng `application/x-www-form-urlencoded`, dữ liệu này cần phải được định dạng đúng cách trước khi gửi đi.

Dưới đây là lý do tại sao cần sử dụng `MultiValueMap` thay vì `Map` thông thường:

1. **Cấu trúc Dữ liệu của `application/x-www-form-urlencoded`:**
    - Khi gửi dữ liệu dưới dạng `application/x-www-form-urlencoded`, dữ liệu được định dạng dưới dạng các cặp khóa-giá trị (key-value pairs).
    - `MultiValueMap` là một giao diện trong Spring cho phép một khóa có thể chứa nhiều giá trị. Đây là một cấu trúc rất phù hợp để chuyển đổi thành định dạng `application/x-www-form-urlencoded`.

2. **Cơ chế Chuyển đổi của Spring WebFlux:**
    - Spring WebFlux có các cơ chế tự động để chuyển đổi `MultiValueMap` thành định dạng `application/x-www-form-urlencoded`.
    - Khi bạn sử dụng `MultiValueMap`, Spring WebFlux hiểu rằng bạn đang muốn gửi dữ liệu dưới dạng các cặp khóa-giá trị và nó tự động chuyển đổi dữ liệu này thành chuỗi định dạng `application/x-www-form-urlencoded` trước khi gửi đi.
    - Với `Map` thông thường, Spring WebFlux không thể tự động xác định rằng dữ liệu cần được chuyển đổi sang định dạng `application/x-www-form-urlencoded`. Thay vào đó, `Map` được xử lý như một đối tượng JSON hoặc một loại khác, dẫn đến lỗi `UnsupportedMediaTypeException`.

3. **Sự khác biệt trong xử lý của `BodyInserters`:**
    - `BodyInserters.fromFormData()` được thiết kế để làm việc với `MultiValueMap` vì nó biết cách chuyển đổi nó thành dữ liệu `application/x-www-form-urlencoded`.
    - Nếu bạn chỉ đơn thuần sử dụng `Map` với `bodyValue()`, Spring sẽ không biết phải chuyển đổi nó thành dạng form data, dẫn đến lỗi.

**Tóm lại:** `MultiValueMap` là loại dữ liệu đặc biệt mà Spring sử dụng để biểu diễn các cặp khóa-giá trị trong một request `application/x-www-form-urlencoded`. Bằng cách sử dụng `MultiValueMap`, bạn cho phép Spring WebFlux tự động xử lý và chuyển đổi dữ liệu của bạn thành định dạng mong muốn. `Map` thông thường không cung cấp thông tin về cách định dạng dữ liệu, do đó, nó không thể được xử lý đúng cách trong trường hợp này.

+-----------------------------------------------------------------------------------------------+
### Tại sao kết nối vs google cần sử dụng application/x-www-form-urlencoded
Khi kết nối với Google (hoặc nhiều dịch vụ OAuth 2.0 khác) để trao đổi mã xác thực (authorization code) lấy token, giao thức yêu cầu bạn phải gửi dữ liệu trong định dạng `application/x-www-form-urlencoded`. Điều này xuất phát từ cách mà giao thức OAuth 2.0 được thiết kế và lý do lịch sử liên quan đến việc gửi dữ liệu qua HTTP.

Dưới đây là một số lý do cụ thể:

### 1. **Tuân theo Tiêu chuẩn OAuth 2.0:**
- Giao thức OAuth 2.0 quy định rằng khi gửi các tham số (như `code`, `client_id`, `client_secret`, `redirect_uri`, và `grant_type`) tới máy chủ token (token endpoint), dữ liệu phải được gửi dưới dạng `application/x-www-form-urlencoded`.
- Đây là một phần của tiêu chuẩn OAuth 2.0 được nêu rõ trong tài liệu RFC 6749, giúp đảm bảo tính tương thích giữa các ứng dụng khách (client) và máy chủ (server) của các nhà cung cấp khác nhau.

### 2. **Tính tương thích với các dịch vụ Web:**
- `application/x-www-form-urlencoded` là định dạng phổ biến cho các biểu mẫu (form) HTML. Nó được thiết kế để gửi dữ liệu qua các yêu cầu HTTP POST theo cách đơn giản và hiệu quả.
- Bằng cách sử dụng định dạng này, các dịch vụ như Google đảm bảo rằng hệ thống của họ có thể xử lý các yêu cầu từ nhiều loại ứng dụng khác nhau (từ ứng dụng web đến ứng dụng di động) mà không gặp vấn đề về định dạng dữ liệu.

### 3. **Quản lý Kích thước Dữ liệu:**
- `application/x-www-form-urlencoded` chuyển đổi các cặp khóa-giá trị thành một chuỗi ký tự được mã hóa theo URL (URL-encoded string), dễ dàng truyền tải qua mạng mà không lo lắng về các vấn đề liên quan đến ký tự đặc biệt hoặc khoảng trắng.
- Dạng mã hóa này đảm bảo rằng dữ liệu có thể được truyền đi một cách an toàn và nhất quán, ngay cả khi nó bao gồm các ký tự không phải chữ cái hoặc chữ số.

### 4. **Truyền tải qua HTTP POST:**
- Định dạng `application/x-www-form-urlencoded` thường được sử dụng trong các yêu cầu HTTP POST, nơi dữ liệu cần được truyền tải như một phần của thân yêu cầu (request body) chứ không phải trong URL.
- Điều này hữu ích đặc biệt khi cần truyền tải dữ liệu nhạy cảm như `client_secret`, vì nó không bị lộ trong URL như khi sử dụng GET.

### 5. **Lịch sử và Truyền thống:**
- `application/x-www-form-urlencoded` đã trở thành một tiêu chuẩn lâu đời cho việc gửi dữ liệu từ các biểu mẫu (forms) HTML. Vì nhiều dịch vụ web, bao gồm cả các dịch vụ của Google, đã tồn tại từ lâu và được xây dựng trên các tiêu chuẩn web cũ, nên việc sử dụng định dạng này giúp duy trì tính tương thích ngược (backward compatibility).

### **Tóm lại:**
Khi làm việc với các dịch vụ như Google và các dịch vụ OAuth 2.0 khác, bạn cần sử dụng `application/x-www-form-urlencoded` để tuân theo các tiêu chuẩn OAuth 2.0. Định dạng này không chỉ đảm bảo tính tương thích mà còn hỗ trợ truyền tải dữ liệu an toàn và hiệu quả qua mạng.


+-----------------------------------------------------------------------------------------------+
### Ngoài application/x-www-form-urlencoded còn những kiểu gửi dữ liệu nào? Thường sử dụng khi nào và vì sao?
Ngoài `application/x-www-form-urlencoded`, còn có một số kiểu gửi dữ liệu khác trong các yêu cầu HTTP, mỗi loại thường được sử dụng trong các tình huống khác nhau tùy thuộc vào loại dữ liệu cần gửi và yêu cầu của ứng dụng. Dưới đây là một số kiểu phổ biến:

### 1. **`application/json`**
- **Sử dụng khi nào:**
    - Thường được sử dụng khi gửi dữ liệu dạng JSON từ ứng dụng này sang ứng dụng khác, đặc biệt là trong các API RESTful.
    - Phù hợp khi cần gửi dữ liệu có cấu trúc phức tạp như đối tượng JavaScript hoặc mảng dữ liệu.
- **Vì sao:**
    - JSON là định dạng phổ biến và dễ dàng để mã hóa, phân tích và xử lý trong hầu hết các ngôn ngữ lập trình.
    - Giúp duy trì cấu trúc của dữ liệu, bao gồm cả các đối tượng lồng nhau, mảng và các kiểu dữ liệu phức tạp khác.
    - Được sử dụng rộng rãi trong các ứng dụng web hiện đại vì tính dễ đọc và sự hỗ trợ rộng rãi từ các thư viện lập trình.

### 2. **`multipart/form-data`**
- **Sử dụng khi nào:**
    - Thường được sử dụng khi cần tải lên tệp tin (file) cùng với các dữ liệu khác trong một biểu mẫu HTML.
    - Ví dụ: khi người dùng gửi biểu mẫu có chứa tệp ảnh, video hoặc tài liệu qua trang web.
- **Vì sao:**
    - Định dạng này cho phép gửi nhiều phần dữ liệu khác nhau trong một yêu cầu duy nhất, mỗi phần có thể có kiểu dữ liệu riêng.
    - Hỗ trợ tốt cho việc tải lên các tệp lớn hoặc nhiều tệp cùng một lúc.
    - Phân tách từng phần dữ liệu rõ ràng, giúp máy chủ dễ dàng xử lý từng thành phần của yêu cầu.

### 3. **`text/plain`**
- **Sử dụng khi nào:**
    - Thường được sử dụng khi gửi dữ liệu dạng văn bản thuần túy, không có cấu trúc phức tạp.
    - Phù hợp cho các biểu mẫu đơn giản hoặc khi chỉ cần gửi một chuỗi văn bản.
- **Vì sao:**
    - Dữ liệu được gửi dưới dạng văn bản đơn giản, không cần phải mã hóa hay cấu trúc phức tạp.
    - Dễ dàng xử lý và phân tích trong các ứng dụng không yêu cầu cấu trúc dữ liệu phức tạp.

### 4. **`application/xml`**
- **Sử dụng khi nào:**
    - Được sử dụng trong các hệ thống cần trao đổi dữ liệu dưới dạng XML, đặc biệt trong các dịch vụ web SOAP.
    - Thích hợp cho các ứng dụng hoặc hệ thống kế thừa đã được xây dựng dựa trên XML.
- **Vì sao:**
    - XML là định dạng mạnh mẽ, có thể định nghĩa cấu trúc dữ liệu phức tạp và được hỗ trợ rộng rãi trong các hệ thống lớn và cũ.
    - Được thiết kế để có thể mở rộng và tự mô tả, phù hợp với các ứng dụng cần kiểm tra dữ liệu chặt chẽ.

### 5. **`application/octet-stream`**
- **Sử dụng khi nào:**
    - Được sử dụng khi cần gửi dữ liệu nhị phân hoặc dữ liệu chưa được định dạng cụ thể (raw data).
    - Thường dùng khi gửi các tệp nhị phân như chương trình, video, hoặc khi cần truyền tải dữ liệu không rõ kiểu.
- **Vì sao:**
    - Loại nội dung này biểu thị rằng dữ liệu được gửi không thuộc bất kỳ định dạng cụ thể nào, và cần được xử lý như dữ liệu nhị phân.
    - Đảm bảo dữ liệu được truyền đi một cách nguyên vẹn mà không bị biến đổi hoặc mã hóa lại.

### 6. **`application/protobuf`**
- **Sử dụng khi nào:**
    - Được sử dụng khi giao tiếp giữa các dịch vụ với nhau trong một môi trường hiệu năng cao, như trong các hệ thống microservices.
    - Thường được sử dụng bởi các API hoặc hệ thống cần truyền tải dữ liệu nhanh chóng và hiệu quả, ví dụ như các dịch vụ của Google.
- **Vì sao:**
    - Protocol Buffers (Protobuf) là một định dạng nhị phân nhỏ gọn, giúp tiết kiệm băng thông và cải thiện hiệu suất so với JSON hoặc XML.
    - Được thiết kế để dễ dàng mở rộng và tương thích với các phiên bản khác nhau của dữ liệu mà không ảnh hưởng đến hiệu năng.

### 7. **`application/x-ndjson` (Newline Delimited JSON)**
- **Sử dụng khi nào:**
    - Thường được sử dụng trong các API hoặc hệ thống streaming dữ liệu, nơi mỗi dòng dữ liệu là một đối tượng JSON độc lập.
    - Phù hợp với việc xử lý dữ liệu luồng hoặc dữ liệu hàng loạt.
- **Vì sao:**
    - Định dạng này cho phép truyền tải từng đối tượng JSON độc lập, giúp dễ dàng xử lý dữ liệu theo từng phần mà không cần tải toàn bộ dữ liệu vào bộ nhớ.
    - Hữu ích trong các hệ thống xử lý dữ liệu lớn hoặc phân tán, nơi dữ liệu được tiêu thụ từng phần.

### **Tóm lại:**
Mỗi loại `Content-Type` có mục đích riêng và được lựa chọn dựa trên đặc điểm của dữ liệu cần gửi và yêu cầu của hệ thống. Hiểu rõ từng loại sẽ giúp bạn lựa chọn phương pháp truyền tải dữ liệu phù hợp nhất cho ứng dụng của mình.



