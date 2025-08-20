1. Ratelimit이 걸린 api를 사용할때 rate limit이 걸리지 않도록 안정적으로 사용하는 방법이 있을까?
2. 큐에 요청을 쌓고, 소비자가 제한 속도로 처리하는 건 어떻게 구현할 수 있을까
3. 멀티서버 구조인데, @scheduled 사용 시 fix rate 작으면 큐를 소비하는 서버의 부담이 있지 않을까
4. 내가 구현해야하는 시나리오는 한 번에 수만 수십만명 사람에 메세지를 보내는거야. 근데 메시지 발송은 외부 api를 써야하고 외부 api는 rate limit이 있는 상태야. 그렇다면 내가 저 외부api를
   rate 제한이 걸리지 않게 안정적으로 호출할 수 있을까
5. 이벤트 드리븐 소비를 위해 카프카를 사용한다고 가정해보자. 수만~ 수십만명되는 회원에게 메세지를 보내기 위해 수만~수십만 이벤트가 한번에 발행될텐데, 이거를 컨슘하는 서버에서 카프카에서 메세지를 컨슘할때 부하가
   있지 않을까?
6. 다중 서버인데, 서버에 배포된 프로젝트는 하나이고,
   프록젝트 하나에서 카프카 이벤트 발행과 구독을 다 진행할거야.
   spring boot 3, kafka, redis(rate limit 중앙 관리) + bucket4j + 외부 API 비동기 호출
   이것을 고려해서 코드를 작성해줘.
7. retry, dlt에 대한 컨슘 코드는 없는 것 같은데?
8. yml 파일에는 컨슈머가 하나만 설정된거 같은데, send, retry, dlt 각각 필요한거 아냐?
9. 같은 group id를 쓸지, 다른 group id를 쓸지 어떤 기준으로 정해야 될까?
10. 근데 동일 그룹 id를 쓰면 send,retry,dlt 컨슈머의 오프셋이 구분돼서 관리가 안되는거지?
11. Ackmode 기본값이 batch일텐데 manual로 변경한 이유가 있어?
12. AckMode가 manual일 때 메세지 단위로 acknowledge 메소드를 호출하면 카프카 브로커랑 네트워크 통신이 많아지는게 문제되지 않을까
13. application.yml에 여러개 컨슈머 그룹 등록하는 방법
14. ```
    public void send(String message) {
    // rate limit 인 경우
    if (!rateLimiter.tryConsume("kakao_" + kakaoId, "kakao")) {
    publishSmsEvent(message);
    }

        // 카카오 메세지 전송 (외부 API 연동)
        KakaoMessageEventDto event = parseMessage(message);
        String auth = kakaoId + ":" + kakaoPassword;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        // TODO 카카오 메세지 전송 성공/실패 시 DB 저장 (로깅용도)
        webClient.post()
                .uri(kakaoMessageUrl)
                .header("Authorization", "Basic " + encodedAuth)
                .bodyValue(Map.of("phone", event.getPhone(), "message", String.format(MESSAGE, event.getName())))
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(res -> log.info("카카오 메세지 발송 성공: {}", res.getStatusCode()))
                .doOnError(res -> {
                    // 카카오 메세지 전송 실패 시 (rate limit 걸림 등) sms 이벤트 발행
                    log.error("카카오 메세지 발송 실패: {}", res.getMessage());
                    publishSmsEvent(message);
                });
    } 
    ```
    카프카 이벤트 컴슘 시 호출되는 메서드는 send(String message) 야.
    컨슈머의 max.poll.records는 1로 설정한 상태야.
    send 메서드 구현이 잘 못된 부분이 있을까?
15. 카카오톡 메세지 발송 API는 1분당 100건 제약, SMS 발송 API는 1분당 500건 제약, 카카오톡 메세지 발송 실패 시 SMS 발송 API 요청 이런 요구사항인데, 예를 들어 1000건의 메세지
    발송을 하면 100건 정도는 카카오톡 메세지 발송 API로 전송이 완료되고, 나머지 900건 중 500건 정도가 SMS 발송으로 성공하고, 400 건은 SMS 발송 API 제약조건으로 인해 실패하게 된다. (
    5000건 메세지 발송이라고 하면 100건 정도는 카카오톡 메세지 발송으로 성공, 4900건 중에서 500건은 SMS로 성공, 나머지 4400건이 최종 실패) 카카오톡 메세지 발송이나, SMS 발송 성공
    갯수를 늘릴 수 있는 방법은 없을까?
16. 채널확장은 불가한 상태이고, 가능한 빨리 보내고 싶어
17. 시간은 걸리더라도 메세지 발송 성공률을 90% 정도로 맞추고 싶다면?
18. Mono.whenDelayError(tasks).doFinally(sig -> ack.acknowledge()).subscribe(); 이 코드에 대해서 좀 더 자세히 설명해줘.
19. private Mono publishRetry(String type, String message, Duration delay) { ... } 근데 이렇게 딜레이를 하게되면 해당 서버가 스레드를 오래
    잡고있으면서 서버의 처리 성능이 떨어지는거아냐?
20. idlebetweenpolls는 몇으로 설정하는게 좋을까
21. 대량 배치 전송 성격이랑 가까워. application.yml에 어떻게 설정하면 될까
22. idle-between-polls 속성은 밀리초 단위인거지?
23. spring.kafka.listener.type의 기본값이 무엇인가?
24. spring.kafka.listener.type이 batch일 때 @KafakListener에서는 레코드를 무조건 리스트로 받아야하나?
25. 스프링 부트에서 컨슈머마다 속성을 따로 설정하는 방법
26. @EnableKafak 어노테이션은 언제 필요한거야?
27. AUTO_OFFSET_RESET_CONFIG 이 속성이 의미하는 것이 무엇인가
28. idle-between-polls 이 값은 containerfactory에 어떻게 설정해?
29. max.poll.records 만큼 메세지가 쌓이지 않았으면 poll하지 않나요? 아니면 그냥 poll 하나?
30. KakaoMessage, KakaoRetry, SMS 3종류 컨슈머 설정 예제 및 최적값 계산
31. 3종류의 컨슈머에 대해서 컨슈머랙이 비슷하게 소진되도록 max.poll.records와 idle.between.polls 값을 설정하고 싶어.
32. 너가 추천한 설정값으로 했을 때 1000건, 10000건, 100000건, 1000000건, 10000000건 처리 시간을 계산해줘.
33. 처리 속도를 유연하게 가져갈순 없을까?
34. 분당 100회 호출 제한이 있는 API와 연동할 때 고가용성을 챙기고 싶다.

    어떻게 구현하면 좋을까?

35. public boolean tryAcquire() {

    long calls = redis.incr("api_calls");

    if (calls == 1) {

    redis.expire("api_calls", 60);

    }

    return calls <= 100;

    } 이 예시에 대해서 설명해줘.

36. resilience4j-ratelimiter 를 사용해서 어떻게 구현할 수 있을까? 
37. public boolean tryAcquire() {

    long calls = redis.incr("api_calls"); // (1)

    if (calls == 1) { // (2)

    redis.expire("api_calls", 60); // (3)

    }

    return calls <= 100; // (4)

    }

    이 방법에서 슬라이딩 윈도우 또는 토큰 버킷 방식으로 어떻게 변경할 수 있을까?

38. 고정 윈도우에 대해서 좀 더 자세히 설명해줄래? 
39. 슬라이딩 윈도우 방식에 대해서 좀 더 자세히 설명해줘. 
40. 너가 예시 든 것 중에 00:01:03 호출 시각에서 00:00:05 기록이 제거된다고 했는데, 00:01:05가 지나야지 00:00:05 기록이 지워지는거 아냐? 
41. 토큰 버킷 방식에 대해서도 자세히 설명해줘.
42. 다중 인스턴스 환경을 고려할 때 보통 bucket4j + redis조합으로 많이 사용하나?
43. bucket4j 말고 redis와 함께 다중 서버 환경에서 ratelimiter를 구현할 수 있는 라이브러리가 있을까? 
44. 개발 시 라이브러리 선택 기준을 어떻게 가져가면 좋을까 
45. 이런 기준에서 ratelimiter 기능 구현 시 bucket4j는 어때? 
46. 호출 제한이 있는 api연동 시 고려사항 
47. 응응 예제를 보여줘. Redis는 lettuce 를 활용해줘 
48. withExpirationStrategy 이거 deprecated 되었는데, 다른 메소드는 없니? 
49. public boolean tryConsume() {

    BucketProxy bucket = proxyManager.builder().build(id, configurationSupplier);

    return bucket.tryConsume(1);

    }

    이렇게 작성했는데 아래와 같은 메세지가 뜨네?

    원인이 뭘까? 해결방법은?

    Unchecked call to 'build(K, Supplier)' as a member of raw type '
    io.github.bucket4j.distributed.proxy.RemoteBucketBuilder'

50. @Configuration

    public class RedisConfig {

    @Bean

    public RedisConnectionFactory redisConnectionFactory() {

    return new LettuceConnectionFactory();

    }

    @Bean

    public StatefulRedisConnection<String, byte[]> redisConnection(LettuceConnectionFactory factory) {

    factory.afterPropertiesSet();

    try (RedisClient client = RedisClient.create("redis://" + factory.getHostName() + ":" + factory.getPort())) {

    return client.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

    }

    }} 이렇게 작성했는데 Description: Parameter 0 of method redisConnection in com.cbk.user_admin_api.config.RedisConfig
    required a bean of type 'org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory' that could not
    be found. Action: Consider defining a bean of type '
    org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory' in your configuration. 이렇게 뜨네?

51. RedisConnectionFactory와 RedisTemplate 이거 사용하는게 더 일반적인거야? 
52. Unchecked cast: 'java.lang.Object' to 'io.lettuce.core.api.StatefulRedisConnection<java.lang.String,byte[]>'해결방안 
53. 근데 bucket4j 에서 redis connection을 연결해주는 작업이 안전한거야? 
54. 근데 redistemplate 빈 등록은 필수야? 
55. Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name '
    proxyManager' defined in class path resource [com/cbk/user_admin_api/config/RateLimiterConfig.class]: Unsatisfied
    dependency expressed through method 'proxyManager' parameter 0: Error creating bean with name '
    statefulRedisConnection' defined in class path resource [com/cbk/user_admin_api/config/RedisConfig.class]: Failed to
    instantiate [io.lettuce.core.api.StatefulRedisConnection]: Factory method 'statefulRedisConnection' threw exception
    with message: class io.lettuce.core.RedisAsyncCommandsImpl cannot be cast to class
    io.lettuce.core.api.StatefulRedisConnection (io.lettuce.core.RedisAsyncCommandsImpl and
    io.lettuce.core.api.StatefulRedisConnection are in unnamed module of loader 'app')해결방법 
56. bucket에 저장된 value는 뭘까? 
57. redis insight gui 툴 통해서 redis에 저장된 값을 확인했는데, 깨져서 보여.
    @Bean(destroyMethod = "close")
    public StatefulRedisConnection<String, byte[]> statefulRedisConnection(
    @Value("${spring.redis.host}") String host,
    @Value("${spring.redis.port}") int port) {
    RedisClient client = RedisClient.create("redis://" + host + ":" + port);
    return client.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
    }
    뭔가 잘 못 설정한 부분이 있을까? 
58. spring boot 3 + redis + bucket4j 설정 방법을 알려줘. 
59. spring:
    redis:
    host: localhost
    port: 6379
    lettuce:
    pool:
    max-active: 8
    max-idle: 8
    min-idle: 0
    max-wait: 1000ms
    이 설정에서 lettuce pool설정이 무엇을 의미하는지 설명해줘. 
60. lettuce.pool설정 기본값이 어떻게 돼? 
61. @GetMapping("/api")
    public String api(@RequestParam String userId) {
    Bucket bucket = rateLimiterService.resolveBucket(userId);
    if (bucket.tryConsume(1)) {
    return "API 호출 성공";
    } else {
    return "Rate limit 초과. 잠시 후 다시 시도해주세요.";
    }
    }
    이제 tryConsume 이 성공했을 때 외부 API를 호출할거야.스프링 부트에서 외부 API를 호출하기 위한 일반적인 방법과 외부 API 호출이기 때문에 비동기 호출을 고려해서 코드를 구현해줘. 
62. @Bean
    public Supplier<BucketConfiguration> bucketConfigurationSupplier() {
    return () -> BucketConfiguration.builder()
    .addLimit(Bandwidth.builder()
    .capacity(100)
    .refillGreedy(100, Duration.ofSeconds(60L))
    .build())
    .build();
    }
    이렇게 선언해두긴 했는데, 호출하는 외부 API마다 limit 조건이 달라.api 마다 빈을 등록하는건 별로인데, 어떻게 해결할 수 있을까? 
63. BucketConfigFactory 클래스는 어느 패키지에 위치하는게 좋을까 \
64. bucket4j 8.10.0 기준으로 제공하는 API쪽의 rate limit 설정하는 방법 알려줘. 
65. 단일서버로 생각해줘. 
66. /kakaotalk-messages 요청에 대해서만 제한 하고 싶어. 
67. /sms?phone= 이 요청에 대해서는 어떻게 필터링 하지? 
68. spring-boot-starter-web 의존성 유무 차이가 뭐야? 
69. spring-boot-starter-validation 이 의존성의 유무 차이도 알려줘. 
70. spring.io에서 spring boot 프로젝트를 만들었는데, 설정파일이 없네?
    
    어느위치에 생성하면 되지?

71. application.properties 표기에 맞게 바꿔줘 
72. docker를 통해 mysql를 띄었을 때 datasource url 주소는 어떻게 설정하지? 
73. useSSL=false → SSL 연결 비활성화

    serverTimezone=UTC → MySQL 타임존 문제 방지 두 속성에 대해서 좀 더 자세히 설명해줄래?

74. application.properties에 작성할 spring.jpa 속성들이 무엇이 있고, 어떤 속성인지 알려줘. 
75. api 동작 확인을 위해서 swagger 설정을 하고 싶어

    어떻게 하면 될까?

76. DDD + 클린 아키텍처 레이어에서 스프링 시큐리티 관련 클래스는 어디에 둘까? 
77. 스프링 시큐리티 사용 시 confg설정은 어떻게 할까? 
78. AuthenticationManager 빈 등록이 필수야? 
79. restcontroller에 swagger 어노테이션 선언하는 방법 
80. restcontroller의 request dto의 필드들이 스웨거 문서에 나타나지 않는 이유? 
81. getter 나 setter 말고는 다른 방법은 없어 
82. 스프링 시큐리티에서 같은 uri인데 post, get에 따라 구분해서 요청 처리를 제한할 수 있어? 
83. RestController끼리 구분하는 기준을 어떻게 가져가면 좋을까? 
84. 회원가입 API(접근제한 없음)

    로그인 API(접근제한 없음)

    일반 사용자 - 회원 상세 조회 API

    시스템 관리자 - 회원 목록 조회, 수정 ,삭제, API, 회원 대상 메세지 발송 API

    이렇게 있는데 어떤 기준으로 컨트롤러를 나누면 좋을까?

85. AuthController: 인증 관련 기능만 담당 → 접근 제한 없음

    UserController: 일반 사용자 기능 → 자신의 정보 조회, 수정 등

    AdminController: 관리자 기능 → 전체 회원 관리, 메시지 발송 등 이렇게 컨트롤러를 나눈다면 DDD 관점에서 패키지 구조는 어떻게 구성해야 될까?

86. @AllArgsConstructor(staticName = "of")

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)

    @Schema(name = "회원 정보")

    public class UserPaginationResponse {

    @Schema(description = "계정")

    private String userId;

    @Schema(description = "성명")

    private String name;

    @Schema(description = "주민등록번호")

    private String ssn;

    @Schema(description = "핸드폰 번호")

    private String phoneNumber;

    @Schema(description = "주소")

    private String address;

    } 해당 클래스는 컨트롤러의 응답모델인데, swagger에서 @Schema에 작성한 내용이 보이지 않고 있어. 다른 설정을 더 해줘야 하나?

87. 아 근데 응답모델이 List 형태야

    혹시 리스트일 때 또 다른 설정이 필요한건가?

88. swagger에 jwt 토큰 넣을 수 있도록 설정하는 방법 
89. 근데 말한대로 설정하면 모든 API에 대해서 authorization 입력이 활성화 되는거야?

    나는 jwt 토큰이 필요한 API에 대해서먼 활성화 하고 싶어.

90. DDD + 클린아키텍처에서 KafakConfig, KafkaProducer, KafkaConsumer 클래스는 어느 레이어에 위치하는 게좋을까? 
91. 회원상세 조회 API 응답에서 회원의 암호는 어떻게 응답해줘야 될까? 
92. 웹서비스 개발시 데이터를 DB에 영속화하는 방법 많을텐데, ORM을 사용하는 방법 또는 jdbctemplate을 이용하는 방법, 또 그밖의 방법이 있다면 각 방법들의 장/단점 비교 및 선택기준을 표로
    설명해줘. 
93. user도메인에 매핑되는 테이블명을 user라고 할까 users라고 할까? 
94. DDD 기반의 개발을 하려고 할 때 프로젝트의 레이어는 어떻게 구성하는게 좋을까? 
95. 레이어별 폴더 구조까지 보여줘. 
96. DDD + 클린 아키텍처를 고려한 레이어는 어떻게 될까 
97. Domain layer가 외부기술에 의존하지 않도록 의존 역전을 하는 것이 어떠한 이점이 있는걸까 
98. DDD + 클린 아키텍처 레이어에서 예를 들어 member와 전혀 다른 도메인의 조합으로 기능을 제공해야된다면 레이어 구성을 어떻게 가져가는게 좋을까? 
99. 근데 내가 알기론 DDD에서 도메인 서비스는 하나의 도메인 모델로 비즈니스 기능을 제공하기 어렵고, 다른 도메인 모델함께 비즈니스 기능을 제공하기 위해 쓰이는 걸로 알고 있는데 맞아? 맞다면 members
    domain 레이어의 도메인 서비스는 다른 도메인이 필요할텐데 레이어간 경계가 모호해지지 않을까? 
100. 보통 infratstructure 의 repository 패키지 안에 jpa entity를 담은 별도의 패키지를 또 만드나? 
101. 사용자 도메인 모델의 이름은 Member로 할거야 사용자는 계정, 암호, 성명, 주민등록번호, 핸드폰번호, 주소 이렇게 속성을 갖는데, JPAEntity에서 자동 증가하는 대체키(pk)를 따로 둘거라서
     이것을 고려해서 변수명 작성해줘. 
102. memberId 계정의 길이 제한은 보통 얼마나 하나? 
103. 패스워드의 최소/최대 길이는 어떻게 될까? 
104. 원문 패스워드의 최소~최대길이 추천해줘. 
105. base64 인코딩 시 원문 패스워드 최대길이 64자여도 괜찮아? 
106. memberId 기준 동등성 테스트는 어떻게 작성하면 될까? 
107. @DisplayName도 작성해줘. 
108. 회원가입 서비스 변수명 추천해줘. 
109. 회원가입 API URI를 restful하게 추천해줘. 
110. controller api 메소드가 public 필요가 있을까? 
111. API 응답이 따로 없는 경우 void가 나을까? 아니면 ResponseENtity 가 나을까 
112. 관리자만 호출할 수 있는 회원 조회(목록), 수정, 삭제 API URI를 restful하게 추천해줘. 
113. 페이지네이션이 가능한 회원 조회 API 컨트롤러 작성해줘. 
114. ADMIN hasrole말고 id: admin, pw : 1212 라는 고정된 정보가 있어서 이정보를 기반으로 basic auth로 인증되어 API가 호출되도록 해야돼 
115. httpBasic(); 6.1 부터 deprecated 되었대. 그럼 어떻게 해야되지? 
116. DDD + 클린아키텍처의 레이어 구조에서 application 패키지 안에 command, query, service 패키지를 두는걸로 너가 얘기해줬잖아. query 패키지의 있는 dto는 서비스 응답모델을
     controller로 서빙하는 dto인거야? 
117. public List readUsers(UserPaginationCommand command) { } application 서비스의 메소드인데, ??? 부분의 변수명을 뭘로 지으면 좋을까? 페이지네이션
     조회한 User 목록이 담기긴 하는데, UserQuery라고 하면 별론가?
118. com.example.project └── members ├── presentation │ ├── controller │ ├── request │ └── response │ ├── application │
     ├── service │ ├── command │ └── query │ ├── domain │ ├── model # Entity, Value Object │ ├── service # Domain
     Service │ └── repository # Repository Interface │ └── infrastructure ├── repository # JPA/MyBatis 구현체 │ ├──
     messaging # Kafka, RabbitMQ │ └── external # 외부 API Client 너가 말한 이 구조에서 application 안의 query는 서비스 응답모델 클래스를 가지는
     패키지인거야? 아니면 조회 요청에 대한 request 파라미터가 있는 클래스를 가지는 패키지인거야?
119. 흠,, 그러면 presentation 에서는 request와 response 패키지를 나눴잖아 근데 query 패키지 안에서는 요청,응답 모델을 같이 두는게 일관성이 떨어지지 않아?
120. application 의 응답모델 변수명과 controller 응답모델 변수명을 어떻게 구분하면 좋을까?
121. domain 패키지에서 repository 인터페이스에 메소드를 정의할 때 메소드 파라미터로 사용되는 객체의 클래스는 어느 패키지에 두어야 하지?
122. basic auth 일때 클라이언트는 어떻게 API를 호출해야되는거지?
123. 그러면 spring security의 httpbasic() 이 알아서 base64로 인코딩된 내용을 디코딩해서 인증확인을 하는거야?
124. User 도메인 모델에 아래와 같이 두 함수가 추가되었는데, public void updatePassword(String password) { if (StringUtils.hasText(
     password)) { this.password = password; } } public void updateAddress(String address) { if (StringUtils.hasText(
     address)) { this.address = address; } } 테스트할만한 단위 테스트를 작성해줘. (@DisplayName 필수)
125. 응 ParameterizedTest로 통합해줘.
126. Optional을 이용해서 userJpaRepository.findByUserId(userId) 결과가 있으면 User 모델로 변환해서 Optional로 응답하고, 결과가 없으면
     Optional.empty()로 응답하는 코드를 작성해줘.
127. user 도메인 엔티티와 user jpa entity가 분리된 상태에서 user의 정보를 업데이트 하는 애플리케이션 로직은 어떻게 작성할 수 있지?
128. 아 여기서 문제는 userid는 pk가 아니야 즉 User 도메인 모델에서는 pk를 갖고 있지 않아
129. 한 트랜잭션 내에서 pk로 jpa 엔티티를 여러번 조회하면 1차 캐시로 인해 쿼리가 한번만 수행되는거 맞니? 그리고 한 트랜잭션 내에서 pk가 아닌 값으로 jpa 엔티티를 여러번 조회하면 조회횟수만큼 쿼리가
     수행되는데, 1차 캐시된 값이 있으면 해당 값으로 조회되는 맞아?
130. 회원 가입한 사용자는 로그인할 수 있어야돼. 로그인 API를 구현해주고, 로그인한 사용자는 자신의 정보 상세조회를 할 수있어. 상세조회 API도 구현해줘.
131. 근데 이건 내가 아니라더도 다른 사람의 계정을 알면 조회할 수 있는 거잖아. 로그인한 사람이 본인 정보만 조회가능해야돼
132. basic auth로 구현해줄 수 있어?
133. inmemory 가 아닌 dB값을 활용하도록 변경해줘.
134. basic auth 기반일 때 base64로 인코딩한 값을 authorization 헤더에 전달할 때 key가 어떻게 되는거지?
135. DB기반 UserDetailService 구현이 일반적인가요?
136. DbUserDetailsService 클래스는 DDD+ 클린아키텍처 구조에서 어떤 레이어에 위치해야될까?
137. 그러면 SecurityConfig 클래스는 어디에 위치해야될까?
138. basic auth기반 로그인 API를 구현해줘.
139. basic auth 기반에서는 로그인 API가 필요없는거야?
140. 아 그럼 jwt 기반 로그인 API를 구현해줘.
141. 관리자는 basic auth, 일반 사용자는 jwt로 인증하도록 할 수 있어?
142. jwt 사용하기 위해서 어떤 의존성이 필요하지?
143. .signWith(SignatureAlgorithm.HS256, secretKey) 이렇게 선언하니깐 'io.jsonwebtoken.SignatureAlgorithm' is deprecated 이렇게 나와
144. 키 길이가 최소 256비트가 되려면 문자열 길이가 어느정되야하지?
145. jjwt 0.12.6 버전에 맞게 jwt 파싱 로직을 구현해줘.
146. 근데 authservice에서 jwtTokenProvider를 주입해서 쓰면 클린 아키텍처 관점에서는 의존 방향이 잘 못 된거 아냐?
147. public boolean matchedPassword(String password) { return this.password.equals(password); } user에 해당 메소드가 추가되었어.
     단위테스트 작성해줘. (@DisplayName 필수)
148. User.create(@NotNull String userId, ...) 정적 팩토리 메서드를 사용해서 테스트를 수정해줘.
149. UserController에서 본인 조회 API의 파라미터가 @AuthenticationPrincipal UserDetails가 아니라 @AuthenticationPrincipal String userId
     여도 상관없어?
150. 관리자는 basic auth, 일반 사용자는 jwt로 인증 처리를 하기 때문에 securityFilterChain 빈을 구분해서 설정했는데, securityMatcher() 사용이 꼭 필수인가?
     requestMatchers만 사용해도 되지 않아?
151. 그런데 .requestMatchers("/admin/**").hasRole("ADMIN") 이렇게 설정했을 때 어떻게 admin API만 .httpBasic(withDefaults()) basic auth
     인증처라가 되는거지?
152. securityChainFilter를 분리한다면 securityMatcher() 사용은 필수인거지?
153. 정리해서 물어볼게: admin/** 요청은 basic auth 사용, swagger 관련 요청 및 /signup, /login 요청은 permit all, 그외 모든 요청은 jwt 인증 사용. 이렇게 한다고
     했을 때 securityChainFilter를 어떻게 구성하면 될까?
154. Customizer.withDefaults() 는 무슨 역할이야?
155. 아 그런데 publicSecurity 에서 /signup 과 /login은 POST 메소드에 대해서 인증 절차를 진행해야돼.
156. 아 내가 잘 못 말했어. /signup", "/login" 는 permit all인데, POST 메소드에만 적용되어야 해
157. jwt 인증 필터 구현 시 보통 어떤 필터를 상속받아서 구현하나?
158. jwtTokenProvider를 빈으로 설정할 필요가 있을까?
159. resolveToken(HttpServletRequest request) 구현 시 header가 Bearer로 시작한다는 보장이 있나?
160. basic auth로 한 번 인증 후 만료 시간은 언제인가?
161. swagger에서 한번 basic auth 인증 후 이후에는 다시 물어보지 않는데, 잘 못 설정된 부분인건가?
162. SecurityConfig 코드 공유, curl 요청 시 403 에러 발생 이유 문의
163. JwtTokenProvider 코드 공유, parseSignedClaims(token) 수행 시 UnsupportedJwtException 발생 원인 문의
164. JwtAuthenticationFilter 코드 공유, @AuthenticationPrincipal UserDetails가 null로 들어오는 이유 문의
165. userId를 통해 UserDetails 객체를 만들려면 어떻게 해야되지?
166. UsernamePasswordAuthenticationToken 의 두번째 인자는 보통 암호를 의미? 주입을 하니? null로 설정하니?
167. userId 기반으로 UserDetails 객체를 만들 때 굳이 dB를 사용할 필요가 없으면 어떻게 구성하면 좋을까?
168. InMemoryUserDetailsService 클래스는 infrastructure.security 패키지에 위치하는게 좋을까?
169. JwtAuthenticationFilter 에 UserDetailsService를 빈으로 주입받을 때 인터페이스로 선언하는게 좋을까, 아니면 구현체로 선언하는게 좋을까
170. SecurityConfig에서 addFilterBefore로 JwtAuthenticationFilter 객체를 넘겨줄 때, 생성자에 tokenprovider와 userdetailsservice를 넣어주기
     위해 userdetailsservice 빈을 주입할 때 인터페이스로 주입할까, 구현체로 주입할까
171. userdetailsservice 구현체가 여러 개 있을 때, 런타임 시점에 어떤 구현체를 주입할지 어떻게 결정하는가?
172. Address VO 코드 공유, 동등성 테스트 및 추가 단위테스트 작성 요청 (@DisplayName 필수)
173. SecurityConfig 코드 공유, /admin API 호출 시 403 원인 문의
174. /admin 요청에 대해 basic auth 사용, Authorization header 넣어도 403 에러 발생 원인 문의
175. security filter 동작 방식을 확인하기 위해 디버깅할 클래스 브레이크포인트 문의
176. 디버깅 결과 No AuthenticationProvider found for UsernamePasswordAuthenticationToken 오류 발생 원인 문의
177. securityFilterChain을 하나만 빈으로 등록했다면 명시적으로 .userDetailsService()를 호출 안 해도 되나?
178. LettuceBasedProxyManager 빈 생성 코드 공유, 코드 의미 설명 요청
179. resilience4j:

ratelimiter:

configs:

default:

limitForPeriod: 3

limitRefreshPeriod: 10s

timeoutDuration: 1s

이렇게 설정하면 10초당 3개 요청을 한다는건가

180. resilience4j-ratelimiter는 호출 당하는 쪽에서 제약을 걸어둘 때 사용하는건가?
181. 음 그러니깐 Resilience4j RateLimiter는 API를 제공하는 쪽에서 설정하는거지?

그래서 해당 API를 사용하는 클라이언트가 무분별하게 호출할 때 자신의 서비스를 안전하게 지키기 위한 용도?

182. Resilience4j RateLimiter 를 사용해서 API를 호출하는 클라이언트 쪽에서는 호출 속도를 제어할 수 있어?
183. 클라이언트 측에서 Resilience4j RateLimiter 를 사용해서 외부 API의 요청 속도를 조절할 때, 만약 외부 API 요청 제약이 초과된 경우에 어떻게 예외처리를 할 수 있을까?
184. Resilience4j RateLimiter 는 고정 윈도우 방식으로 알고 있는데, 슬라이딩 윈도우 방식으로도 사용가능한가?
185. resilience4j-ratelimiter의 timeoutDuration 속성은 내부적으로 큐를 사용하는건가?
186. resilience4j-ratelimiter 에서 큐를 사용하는 설정이 있나?
187. Rate limiting is an imperative technique to prepare your API for scale and establish high availability and
     reliability of your service. But also, this technique comes with a whole bunch of different options of how to
     handle a detected limits surplus, or what type of requests you want to limit. You can simply decline this over
     limit request, or build a queue to execute them later or combine these two approaches in some way.

여기서 큐 내용이 나오는데, 이건 어떤 의미인거야?

188. resilience4j-ratelimiter 고정 윈도우 방식인가요? 토큰 버킷 방식인가요?
189. resilience4j-ratelimiter 의 호출 정보 같은건 인메모리에서 관리될텐데, 멀티 인스턴스 환경에서는 어떻게 구현할 수 있지?
190. bucket4j는 요청 제한이 있는 API를 호출하는 클라이언트 쪽에 설정하는 건가?

아니면 요청 제한을 하는 API 제공 측에서 설정하는건가?

191. kafkaTemplate.send(topic, key, message)에서 key는 뭐야?
192. kakaoMessageSendTopic와 smsSendTopic 구독하는 컨슈머 그룹을 각각 따로 설정하고 싶어.
193. @KafkaListener(topics = "kakaoMessageSendTopic", groupId = "consumer-group-kakao")

topics랑 groupId를 application.yml에서 가져오게 설정할 수 있어?

194. @KafkaListener(

topics = "${kafka.topics.kakao-message}",

groupId = "${kafka.consumer-groups.kakao-message}")

public void consumeKakao(String message) {

MessageSendService messageSendService = getMessageSendService(MessageHandlerKey.KAKAO_MESSAGE);

messageSendService.send(message);

}

public void consumeKakao(String message) 이렇게 사용하려면 max.poll.records을 1로 설정해야되는건가?

195. consumeKakao(List<ConsumerRecord<String, SendMessageEvent>> records, Acknowledgment ack)

메소드 시그니처를 이렇게 바꿔도 가능한건가?

196. public void consumeKakao(String message) 이때는 오프셋 커밋인 언제될까?
197. public void consumeKakao(String message) 이 메소드 내부에서 예외 발생시 batch 모드를 통해 가져온 모든 메시지가 롤백되나?
198. Caused by: org.springframework.util.PlaceholderResolutionException: Could not resolve placeholder '
     kafka.topics.kakao-message-send' in value "${kafka.topics.kakao-message-send}"

원인과 해결방안

199. application.yml에

kafka:

topics:

kakao-message: kakao-message-send

sms: sms-send

consumer-groups:

kakao-message: kakao-message-consumer-group

sms: sms-consumer-group

이렇게 설정해두었어

200. Caused by: org.apache.kafka.common.errors.InvalidReplicationFactorException: Replication factor: 3 larger than
     available brokers: 1.

원인과 해결방안

201. max.poll.records = 500 , ackmode = batch로 설정된 상태에서

poll를 통해 메세지를 가져와 처리하던 중 카프카 쪽 오류가 발생한다면

커밋이 안돼서 다시 500개의 메세지를 가져와서 처리하는 건가?

202. 중복 처리를 피하려면 어떻게 하면 좋을까
203. @Component

@RequiredArgsConstructor

public class KafkaConsumer {

private final List messageSendServices;

...

}

이렇게 최종적으로 작성했는데, 너가 봤을 때 문제가 될만한 부분이나 개선할 부분이 있는것 같아?

204. 응 운영 레벨의 Kafka Listener + 서비스 코드로 정리해줘.