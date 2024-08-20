### Dưới đây là tổng kết lại các công nghệ và các service sau khi đã phân phối lại:

### **1. Tổng quan các service**
#### **A. Nhóm Core Services**
1. **User Service**:
  - **Chức năng**: Quản lý thông tin người dùng, xác thực, và phân quyền.
  - **Công nghệ**: Spring Boot, JWT.
  - **Database**: PostgreSQL (lưu thông tin người dùng).

2. **Image/Video Service**:
  - **Chức năng**: Kết hợp xử lý tải lên ảnh và video, xử lý video (resize, watermark), và lưu trữ vào hệ thống.
  - **Công nghệ**: Node.js/Express hoặc Spring Boot, Python (FFmpeg), AWS MediaConvert.
  - **Storage**: Amazon S3 hoặc Google Cloud Storage (với Multipart Upload cho video lớn).

#### **B. Nhóm Social Services**
1. **Social Service**:
  - **Chức năng**: Kết hợp các tính năng like, follow, chia sẻ ảnh/video và chat thời gian thực giữa người dùng.
  - **Công nghệ**: Spring Boot hoặc Node.js, WebSocket cho chat, Redis Pub/Sub.
  - **Database**: PostgreSQL cho thông tin like, follow, và chia sẻ; MongoDB cho tin nhắn chat.

#### **C. Nhóm Utility Services**
1. **Notification and Recommendation Service**:
  - **Chức năng**: Gửi thông báo đẩy cho người dùng khi có tương tác mới (like, follow, tin nhắn) và đề xuất nội dung hoặc người dùng để follow.
  - **Công nghệ**: Firebase Cloud Messaging (FCM) hoặc Amazon SNS cho thông báo đẩy, Machine Learning với TensorFlow hoặc AWS SageMaker cho đề xuất.
  - **Database**: PostgreSQL cho thông báo chưa đọc, Redis để cache, PostgreSQL hoặc MongoDB cho dữ liệu huấn luyện mô hình đề xuất.

#### **D. Nhóm Support Services**
1. **API Gateway**:
  - **Chức năng**: Định tuyến các yêu cầu từ client đến các microservices.
  - **Công nghệ**: Nginx hoặc AWS API Gateway.

2. **Load Balancer**:
  - **Chức năng**: Cân bằng tải giữa các phiên bản microservices.
  - **Công nghệ**: AWS Elastic Load Balancer, Nginx hoặc HAProxy.

3. **CDN Service**:
  - **Chức năng**: Phân phối nội dung ảnh và video với tốc độ cao và giảm tải trực tiếp cho hệ thống backend.
  - **Công nghệ**: AWS CloudFront hoặc Akamai.

### **2. Tổng quan công nghệ**

- **Frontend**:
  - Web: React
  - Mobile: Swift (iOS), Kotlin (Android)

- **Backend**:
  - Spring Boot (Java) hoặc Node.js (JavaScript)

- **Database**:
  - PostgreSQL cho thông tin người dùng, social interaction (like, follow, chia sẻ) và thông báo.
  - MongoDB cho tin nhắn chat và dữ liệu không có cấu trúc.

- **Storage**:
  - Amazon S3 hoặc Google Cloud Storage cho ảnh và video (với hỗ trợ Multipart Upload cho file lớn).

- **Caching**:
  - Redis để cache dữ liệu liên quan đến social interaction và thông báo chưa đọc.

- **Message Queue**:
  - Apache Kafka hoặc RabbitMQ để quản lý các sự kiện không đồng bộ (like, follow, tin nhắn).

- **CDN**:
  - AWS CloudFront hoặc Akamai để phân phối nội dung nhanh chóng và hiệu quả.

- **Machine Learning**:
  - TensorFlow hoặc AWS SageMaker cho hệ thống đề xuất nội dung và người dùng.

### **Lợi ích**
- **Đơn giản hóa quản lý và phát triển**: Kết hợp các service liên quan giúp quản lý dễ dàng hơn.
- **Dễ dàng mở rộng**: Các service quan trọng được tối ưu hóa để dễ dàng mở rộng khi số lượng người dùng tăng.
- **Tối ưu hóa hiệu suất**: Sử dụng caching, CDN và message queue giúp tăng hiệu suất và giảm tải cho hệ thống backend.

Với kiến trúc này, bạn sẽ có một hệ thống linh hoạt, có khả năng chịu tải lớn, và dễ dàng mở rộng khi cần thiết.








### Thiết kế hệ thống kho lưu trữ ảnh lớn, nhiều người truy cập và sử dụng

#### 1. Kiến trúc tổng quan
- **Client**: Giao diện người dùng (web, mobile) để tải lên và truy cập ảnh.
- **API Gateway**: Quản lý các yêu cầu từ client và chuyển tiếp đến các dịch vụ backend.
- **Microservices**: Các dịch vụ nhỏ, độc lập để xử lý các chức năng khác nhau như tải lên ảnh, xử lý ảnh, lưu trữ metadata, xác thực người dùng.
- **Storage**: Hệ thống lưu trữ ảnh (S3, Google Cloud Storage).
- **Database**: Lưu trữ metadata của ảnh (PostgreSQL, MongoDB).
- **CDN**: Phân phối nội dung để tăng tốc độ truy cập ảnh (CloudFront, Akamai).

#### 2. Các thành phần chi tiết
- **Client**:
    - Giao diện người dùng để tải lên và xem ảnh.
    - Sử dụng React, Angular hoặc Vue.js cho web.
    - Sử dụng Swift (iOS) và Kotlin (Android) cho mobile.

- **API Gateway**:
    - Sử dụng Nginx hoặc AWS API Gateway để quản lý các yêu cầu.
    - Cung cấp các endpoint RESTful hoặc GraphQL.

- **Microservices**:
    - **User Service**: Quản lý người dùng và xác thực.
    - **Image Upload Service**: Xử lý tải lên ảnh, lưu trữ ảnh vào hệ thống lưu trữ.
    - **Image Processing Service**: Xử lý ảnh (resize, watermark).
    - **Metadata Service**: Lưu trữ và truy vấn metadata của ảnh.

- **Storage**:
    - Sử dụng Amazon S3 hoặc Google Cloud Storage để lưu trữ ảnh.
    - Sử dụng cơ chế lưu trữ phân tán để đảm bảo tính sẵn sàng và khả năng mở rộng.

- **Database**:
    - Sử dụng PostgreSQL hoặc MongoDB để lưu trữ metadata của ảnh.
    - Sử dụng Redis hoặc Memcached để cache metadata và tăng tốc độ truy vấn.

- **CDN**:
    - Sử dụng CloudFront hoặc Akamai để phân phối ảnh đến người dùng cuối.
    - Giảm tải cho hệ thống lưu trữ và tăng tốc độ truy cập.

#### 3. Sơ đồ kiến trúc
```plaintext
+--------+        +-------------+        +----------------+        +-----------------+
| Client | <----> | API Gateway | <----> | Microservices  | <----> | Storage (S3)    |
+--------+        +-------------+        +----------------+        +-----------------+
                      |                       |                        |
                      v                       v                        v
                +-------------+        +----------------+        +-----------------+
                | Auth Service|        | Database (SQL) |        | CDN (CloudFront)|
                +-------------+        +----------------+        +-----------------+
```

#### 4. Công nghệ sử dụng
- **Frontend**: React, Angular, Vue.js, Swift, Kotlin.
- **Backend**: Spring Boot, Node.js, Express.
- **Database**: PostgreSQL, MongoDB, Redis.
- **Storage**: Amazon S3, Google Cloud Storage.
- **CDN**: CloudFront, Akamai.
- **API Gateway**: Nginx, AWS API Gateway.
- **Containerization**: Docker, Kubernetes.

#### 5. Các bước triển khai
1. **Thiết kế và phát triển giao diện người dùng**.
2. **Xây dựng API Gateway và các microservices**.
3. **Cấu hình hệ thống lưu trữ và CDN**.
4. **Triển khai và cấu hình cơ sở dữ liệu**.
5. **Tích hợp và kiểm thử hệ thống**.
6. **Triển khai hệ thống lên môi trường sản xuất**.




#### **Hệ thống quản lý ảnh**
- **Frontend**:
  - **Công nghệ**: React (Web), Swift (iOS), Kotlin (Android).
  - **Chức năng**: Giao diện người dùng để tải lên, xem và quản lý ảnh.

- **API Gateway**:
  - **Công nghệ**: AWS API Gateway hoặc Nginx.
  - **Chức năng**: Quản lý các yêu cầu từ client và định tuyến chúng đến các microservices. Sử dụng REST cho các yêu cầu đơn giản và GraphQL cho các truy vấn phức tạp.

- **Microservices**:
  1. **User Service**:
    - **Công nghệ**: Spring Boot, JWT cho xác thực.
    - **Database**: PostgreSQL hoặc MongoDB (lưu trữ thông tin người dùng).
    - **Chức năng**: Đăng ký, đăng nhập, xác thực người dùng.

  2. **Image Upload Service**:
    - **Công nghệ**: Node.js/Express hoặc Spring Boot.
    - **Storage**: Amazon S3 hoặc Google Cloud Storage.
    - **Chức năng**: Xử lý tải lên ảnh, lưu trữ ảnh vào hệ thống lưu trữ.

  3. **Image Processing Service**:
    - **Công nghệ**: Python với OpenCV hoặc Node.js với Sharp.
    - **Chức năng**: Xử lý ảnh như resize, thêm watermark.

  4. **Metadata Service**:
    - **Công nghệ**: Spring Boot hoặc Node.js.
    - **Database**: MongoDB (vì metadata của ảnh có thể có cấu trúc không cố định và dễ mở rộng).
    - **Chức năng**: Lưu trữ và truy vấn metadata của ảnh.

#### **Hệ thống lưu trữ và phân phối**
- **Storage**:
  - **Công nghệ**: Amazon S3 hoặc Google Cloud Storage.
  - **Chức năng**: Lưu trữ ảnh với tính sẵn sàng cao và khả năng mở rộng.

- **Database**:
  - **Công nghệ**: PostgreSQL hoặc MongoDB cho metadata.
  - **Chức năng**: Lưu trữ metadata về ảnh, chẳng hạn như tên file, người tải lên, và thông tin khác liên quan.

- **CDN**:
  - **Công nghệ**: CloudFront hoặc Akamai.
  - **Chức năng**: Phân phối nội dung để tăng tốc độ truy cập ảnh cho người dùng cuối, giảm tải hệ thống lưu trữ chính.

#### **Real-time Notifications**:
- **Công nghệ**: WebSocket hoặc Server-Sent Events (SSE).
- **Chức năng**: Gửi thông báo thời gian thực cho người dùng khi ảnh đã được xử lý hoặc cập nhật.

### 3. **Database cụ thể cho từng service**
- **User Service**: PostgreSQL hoặc MongoDB (lưu trữ thông tin người dùng).
- **Metadata Service**: MongoDB (lưu trữ metadata của ảnh với cấu trúc động).
- **Image Processing Service**: Không cần database, lưu trữ kết quả xử lý trực tiếp lên S3.
- **Caching**: Redis (cache metadata để tăng tốc truy vấn).

### 4. **Tóm tắt kiến thức cần học**
- **REST API và GraphQL**: Hiểu bản chất của mỗi loại và khi nào nên sử dụng.
- **WebSocket**: Tìm hiểu cách triển khai các kết nối thời gian thực.
- **Spring Boot và Node.js**: Nền tảng cho việc xây dựng backend.
- **Containerization**: Docker và Kubernetes để quản lý triển khai hệ thống.
- **Cloud Services**: AWS S3, Google Cloud Storage, và các dịch vụ CDN như CloudFront.
- **Database**: PostgreSQL, MongoDB, và Redis.




### **Kiến trúc hệ thống khi thêm chức năng upload và xem video trực tuyến**

Với yêu cầu bổ sung chức năng upload và xem video, kiến trúc hệ thống sẽ cần được mở rộng để đáp ứng các yêu cầu về xử lý video lớn và phát trực tuyến với tốc độ cao. Dưới đây là các đề xuất về kiến trúc, công nghệ và chức năng cần bổ sung:

### 1. **Cấu trúc mở rộng**
- **Frontend**:
  - **Chức năng bổ sung**: Giao diện cho việc tải lên video và xem video trực tuyến với hỗ trợ phát trực tuyến (streaming).
  - **Công nghệ bổ sung**: Sử dụng các thư viện phát video như Video.js cho web, AVPlayer cho iOS, và ExoPlayer cho Android.

- **API Gateway**:
  - **Chức năng bổ sung**: Định tuyến các yêu cầu liên quan đến việc tải lên và phát video trực tuyến.
  - **Công nghệ bổ sung**: Có thể cấu hình các endpoint chuyên dụng cho việc tải lên video lớn, và sử dụng GraphQL cho các truy vấn metadata phức tạp của video.

- **Microservices bổ sung**:
  1. **Video Upload Service**:
    - **Chức năng**: Xử lý tải lên video lớn, lưu trữ video vào hệ thống lưu trữ và xử lý chunk upload cho các file lớn.
    - **Công nghệ**: Node.js/Express hoặc Spring Boot với hỗ trợ multipart upload, và S3 Transfer Acceleration để tăng tốc độ tải lên.

  2. **Video Processing Service**:
    - **Chức năng**: Xử lý video sau khi tải lên (chuyển đổi định dạng, cắt đoạn video, thêm watermark).
    - **Công nghệ**: Sử dụng FFmpeg để xử lý video. Để tăng tốc độ xử lý, có thể sử dụng hệ thống phân tán hoặc tích hợp với các dịch vụ như AWS Elastic Transcoder hoặc AWS Elemental MediaConvert.

  3. **Streaming Service**:
    - **Chức năng**: Phát trực tuyến video với tốc độ cao, hỗ trợ nhiều định dạng như HLS (HTTP Live Streaming) hoặc MPEG-DASH.
    - **Công nghệ**: Sử dụng các dịch vụ như AWS MediaLive hoặc tự xây dựng với Nginx RTMP Module để phát trực tuyến.

  4. **Metadata Service cho Video**:
    - **Chức năng**: Lưu trữ metadata của video, bao gồm thông tin về thời lượng, định dạng, và chất lượng video.
    - **Công nghệ**: MongoDB để lưu trữ metadata của video do sự linh hoạt trong cấu trúc dữ liệu.

### 2. **Hệ thống lưu trữ và phân phối mở rộng**
- **Storage**:
  - **Chức năng bổ sung**: Lưu trữ video lớn với khả năng truy cập nhanh. Sử dụng cơ chế lưu trữ đối tượng phân tán như Amazon S3 hoặc Google Cloud Storage. Để tối ưu hóa cho video lớn, sử dụng S3 Transfer Acceleration và Multipart Upload.

- **CDN bổ sung**:
  - **Chức năng bổ sung**: Phân phối video trực tuyến với độ trễ thấp, hỗ trợ nhiều độ phân giải khác nhau (adaptive bitrate streaming).
  - **Công nghệ**: Sử dụng CloudFront hoặc Akamai để tối ưu hóa việc phân phối video tới người dùng cuối.

### 3. **Bổ sung về Database**
- **Metadata Service cho Video**:
  - **Database**: MongoDB để lưu trữ metadata của video như tên file, thời lượng, định dạng, độ phân giải, và các thông tin khác liên quan.

- **Caching**:
  - **Chức năng bổ sung**: Cache metadata của video để tăng tốc độ truy vấn. Redis vẫn là lựa chọn tốt cho việc cache này.

### 4. **Tính năng đặc biệt**
- **Video Streaming với CDN**:
  - **Công nghệ**: Sử dụng HLS hoặc MPEG-DASH để phát trực tuyến video, hỗ trợ nhiều độ phân giải khác nhau (adaptive streaming). CDN sẽ chịu trách nhiệm cache và phân phối video đến người dùng cuối.

- **Upload video lớn**:
  - **Công nghệ**: Sử dụng S3 Multipart Upload hoặc Google Cloud Resumable Uploads để hỗ trợ việc tải lên các file video lớn mà không bị gián đoạn.

### 5. **Tóm tắt công nghệ cần học thêm**
- **FFmpeg**: Công cụ mạnh mẽ để xử lý video, chuyển đổi định dạng và thêm hiệu ứng.
- **AWS Media Services**: Các dịch vụ như Elastic Transcoder và MediaLive để xử lý và phát trực tuyến video.
- **Streaming Protocols**: HLS (HTTP Live Streaming) và MPEG-DASH để phát trực tuyến video với adaptive bitrate.
- **Advanced CDN**: Tìm hiểu sâu hơn về các công cụ như AWS CloudFront, Akamai cho việc phân phối video với độ trễ thấp.
- **Multipart Upload**: Học cách triển khai việc upload file lớn với Multipart Upload trên các nền tảng cloud như Amazon S3 hoặc Google Cloud Storage.

Với hệ thống này, bạn sẽ có một nền tảng mạnh mẽ cho việc lưu trữ và phân phối cả ảnh và video với tốc độ cao, phục vụ cho nhiều người dùng truy cập đồng thời.



### Để xây dựng một hệ thống có khả năng chịu tải lên đến 1 triệu người dùng đồng thời, cần có kiến trúc phân tán, mở rộng, và hiệu quả. Dưới đây là các yếu tố cần được xem xét:

### 1. **Kiến trúc Microservices**
- **Phân chia hệ thống thành các microservices**:
  - Chia hệ thống thành các dịch vụ độc lập như quản lý người dùng, upload ảnh/video, xử lý ảnh/video, quản lý metadata, và streaming.
  - Mỗi microservice có thể được triển khai và mở rộng độc lập, giúp tăng khả năng chịu tải cho hệ thống tổng thể.

### 2. **Load Balancing (Cân bằng tải)**
- **Sử dụng Load Balancer**:
  - Triển khai các công cụ cân bằng tải như AWS Elastic Load Balancer (ELB), Nginx hoặc HAProxy để phân phối lưu lượng truy cập giữa các máy chủ backend.
  - Load balancer giúp chia tải giữa các phiên bản của microservices để không có máy chủ nào bị quá tải.

### 3. **Auto-Scaling (Tự động mở rộng)**
- **Tự động mở rộng hạ tầng**:
  - Sử dụng các dịch vụ như AWS Auto Scaling hoặc Kubernetes Horizontal Pod Autoscaling để tự động mở rộng số lượng máy chủ khi tải tăng cao.
  - Tự động thêm hoặc giảm số lượng phiên bản của các dịch vụ dựa trên lưu lượng thực tế.

### 4. **Caching (Bộ nhớ đệm)**
- **Sử dụng bộ nhớ đệm (cache)**:
  - Sử dụng Redis hoặc Memcached để cache các kết quả truy vấn thường xuyên hoặc các metadata của ảnh/video.
  - CDN cũng đóng vai trò quan trọng trong việc cache nội dung tĩnh (ảnh, video) để giảm tải cho hệ thống backend.

### 5. **Database Scalability (Mở rộng cơ sở dữ liệu)**
- **Database Sharding**:
  - Phân tán cơ sở dữ liệu thành nhiều phần nhỏ hơn (shards), mỗi phần chịu trách nhiệm cho một tập dữ liệu riêng biệt. Điều này giúp cơ sở dữ liệu có thể mở rộng khi số lượng người dùng và dữ liệu tăng cao.

- **Replication**:
  - Sử dụng replication để sao chép dữ liệu trên nhiều máy chủ, giúp tăng khả năng truy cập dữ liệu nhanh chóng và đảm bảo tính sẵn sàng của hệ thống.

- **Read-Write Splitting**:
  - Tách các truy vấn đọc và ghi, sử dụng các node chỉ đọc để xử lý các truy vấn đọc lớn và giảm tải cho node chính.

### 6. **Content Delivery Network (CDN)**
- **Sử dụng CDN để phân phối nội dung**:
  - Sử dụng các dịch vụ như AWS CloudFront hoặc Akamai để cache và phân phối ảnh/video tới người dùng cuối, giúp giảm tải trực tiếp lên hệ thống backend.
  - CDN giúp giảm độ trễ và tăng tốc độ tải dữ liệu khi người dùng truy cập từ nhiều vị trí địa lý khác nhau.

### 7. **Message Queuing (Hệ thống hàng đợi tin nhắn)**
- **Sử dụng hệ thống hàng đợi (queue)**:
  - Sử dụng các hệ thống như Apache Kafka, RabbitMQ hoặc AWS SQS để xử lý các tác vụ không đồng bộ, giảm tải cho hệ thống khi phải xử lý các tác vụ nặng như xử lý ảnh/video.
  - Hàng đợi giúp đảm bảo rằng các tác vụ xử lý nặng có thể được thực hiện mà không gây ảnh hưởng đến hiệu suất hệ thống tổng thể.

### 8. **Monitoring và Alerting (Giám sát và cảnh báo)**
- **Giám sát hiệu suất hệ thống**:
  - Sử dụng các công cụ giám sát như Prometheus, Grafana, Datadog hoặc AWS CloudWatch để theo dõi tình trạng hệ thống.
  - Thiết lập các cảnh báo để phát hiện sớm các vấn đề như quá tải CPU, bộ nhớ hoặc đứt gãy dịch vụ.

### 9. **Data Partitioning (Phân vùng dữ liệu)**
- **Phân vùng dữ liệu**:
  - Đối với các hệ thống lưu trữ dữ liệu lớn như video, phân vùng dữ liệu có thể được sử dụng để chia nhỏ khối lượng dữ liệu trên các server hoặc cluster khác nhau. Điều này giúp giảm tải và tăng khả năng mở rộng.

### 10. **Service Mesh**
- **Sử dụng Service Mesh**:
  - Sử dụng công nghệ service mesh như Istio để quản lý việc giao tiếp giữa các microservices, giúp tối ưu hóa lưu lượng mạng, quản lý lỗi, và bảo mật.

### 11. **Stateless Services (Dịch vụ không trạng thái)**
- **Thiết kế dịch vụ không trạng thái**:
  - Đảm bảo các microservices là không trạng thái (stateless) để chúng có thể dễ dàng được nhân bản và mở rộng. Sử dụng Redis hoặc Memcached để lưu trữ session hoặc các thông tin tạm thời.

### 12. **Distributed Logging và Tracing**
- **Quản lý log phân tán và tracing**:
  - Sử dụng các công cụ như ELK Stack (Elasticsearch, Logstash, Kibana) hoặc Jaeger để theo dõi log và tracing của hệ thống nhằm phát hiện các vấn đề hiệu suất hoặc lỗi.

### **Tóm tắt**
- **Kiến trúc phân tán và microservices** giúp hệ thống dễ dàng mở rộng và chịu tải lớn.
- **Caching và CDN** giúp giảm tải hệ thống và tăng tốc độ truy cập dữ liệu.
- **Auto-scaling và Load Balancing** đảm bảo hệ thống có thể mở rộng tự động khi tải tăng cao.
- **Message Queuing và Database Sharding** giúp quản lý các tác vụ nặng và phân phối dữ liệu hợp lý.

Kết hợp tất cả các thành phần này, hệ thống của bạn sẽ có khả năng chịu tải lớn, đáp ứng nhu cầu của 1 triệu người dùng đồng thời.



### Mở rộng hệ thống với các chức năng xã hội: chia sẻ ảnh, like ảnh, follow, và chat

Việc bổ sung các tính năng xã hội như chia sẻ ảnh, like ảnh, follow, và chat yêu cầu phải mở rộng hệ thống hiện có với các microservices và cơ sở dữ liệu hỗ trợ các tính năng này. Dưới đây là các đề xuất chi tiết:

### 1. **Kiến trúc tổng quan**
- **Frontend**:
  - **Chức năng bổ sung**: Giao diện cho người dùng chia sẻ ảnh, like ảnh, theo dõi người dùng khác, và chat.
  - **Công nghệ bổ sung**:
    - React (Web): Sử dụng Redux hoặc MobX để quản lý trạng thái của ứng dụng.
    - Mobile (Swift, Kotlin): Sử dụng các thành phần như RecyclerView và ViewModel để quản lý dữ liệu người dùng và tương tác.

- **API Gateway**:
  - **Chức năng bổ sung**: Định tuyến các yêu cầu liên quan đến tính năng chia sẻ ảnh, like, follow, và chat tới các microservices tương ứng.
  - **Công nghệ bổ sung**: Nginx hoặc AWS API Gateway sẽ cần mở rộng để quản lý lưu lượng truy cập tăng từ các tính năng xã hội.

- **Microservices bổ sung**:
  1. **Social Interaction Service**:
    - **Chức năng**: Quản lý việc chia sẻ ảnh, like ảnh, và theo dõi người dùng khác.
    - **Database**: PostgreSQL để lưu trữ dữ liệu liên quan đến like, follow và chia sẻ ảnh (các bảng như `likes`, `follows`, `shares`).
    - **Công nghệ**: Spring Boot hoặc Node.js/Express.

  2. **Chat Service**:
    - **Chức năng**: Cung cấp tính năng chat thời gian thực giữa các người dùng.
    - **Database**: MongoDB để lưu trữ các tin nhắn chat, vì dữ liệu tin nhắn thường không có cấu trúc cố định và cần khả năng mở rộng.
    - **Công nghệ**: Sử dụng WebSocket hoặc gRPC để xử lý các kết nối thời gian thực.

  3. **Notification Service**:
    - **Chức năng**: Gửi thông báo đến người dùng khi có tương tác (like, follow, tin nhắn mới).
    - **Công nghệ**: Sử dụng Firebase Cloud Messaging (FCM) hoặc Amazon SNS để gửi thông báo đẩy.

  4. **Recommendation Service**:
    - **Chức năng**: Đề xuất người dùng khác để follow hoặc đề xuất ảnh dựa trên sở thích.
    - **Công nghệ**: Sử dụng machine learning (ML) với TensorFlow hoặc AWS SageMaker để tạo ra các mô hình đề xuất.

### 2. **Hệ thống lưu trữ và phân phối mở rộng**
- **Storage**:
  - Vẫn sử dụng Amazon S3 hoặc Google Cloud Storage để lưu trữ ảnh và video mà người dùng chia sẻ.

- **CDN bổ sung**:
  - CDN cần mở rộng để phục vụ cả việc chia sẻ ảnh và video từ người dùng với độ trễ thấp.

### 3. **Database cho các tính năng xã hội**
- **Social Interaction Database**:
  - PostgreSQL để lưu trữ các quan hệ xã hội như `likes`, `follows`, và `shares`.

- **Chat Database**:
  - MongoDB để lưu trữ tin nhắn chat. Có thể sử dụng Redis để cache các tin nhắn gần nhất để tối ưu hóa tốc độ truy xuất.

- **Notification Database**:
  - PostgreSQL để lưu trữ các thông báo chưa được đọc, cùng với Redis để cache các thông báo chưa đọc của người dùng.

### 4. **Tính năng đặc biệt**
- **Chat thời gian thực**:
  - **Công nghệ**: Sử dụng WebSocket để đảm bảo chat diễn ra theo thời gian thực, cùng với Redis Pub/Sub để quản lý việc giao tiếp giữa các instance của chat service.

- **Tương tác xã hội**:
  - **Chia sẻ ảnh**: Người dùng có thể chia sẻ ảnh công khai hoặc chỉ với bạn bè của mình.
  - **Like ảnh**: Lưu trữ số lượng like và cho phép người dùng like/unlike ảnh.
  - **Follow người dùng**: Lưu trữ các mối quan hệ follow giữa người dùng với nhau.

- **Notification Push**:
  - **Công nghệ**: Sử dụng Firebase hoặc SNS để gửi thông báo đẩy đến người dùng khi có tương tác mới.

### 5. **Scaling cho tính năng xã hội**
- **Caching**:
  - Redis hoặc Memcached sẽ được sử dụng để cache dữ liệu về số lượng like, follow và tin nhắn chat gần đây để giảm tải lên cơ sở dữ liệu chính.

- **Event-Driven Architecture**:
  - Sử dụng kiến trúc sự kiện (event-driven) với Apache Kafka hoặc RabbitMQ để quản lý các sự kiện như like, follow, và tin nhắn mới, đảm bảo tính mở rộng cao.

### 6. **Học thêm**
- **WebSocket và Redis Pub/Sub**: Cách sử dụng WebSocket để triển khai chat thời gian thực và tích hợp với Redis Pub/Sub để quản lý kết nối đa máy chủ.
- **Event-Driven Architecture**: Học cách sử dụng Kafka hoặc RabbitMQ để xử lý các sự kiện tương tác xã hội với số lượng lớn người dùng.
- **Machine Learning cho Recommendation**: Áp dụng các mô hình machine learning để tạo ra hệ thống đề xuất người dùng hoặc nội dung phù hợp.

Các tính năng xã hội này sẽ làm tăng tính tương tác của hệ thống và đòi hỏi hạ tầng phải mạnh mẽ và có khả năng mở rộng tốt.