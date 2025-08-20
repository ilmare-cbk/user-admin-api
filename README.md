# user-admin-api

서버 실행 전, 터미널에서 `docker-compose.yml`을 실행해주세요.

- user-admin-api 프로젝트 내 docker-compose.yml 파일이 있습니다.
- docker-compose.yml 실행 전 host의 포트가 이미 사용 중이라면 사용가능한 다른 포트번호로 변경해주세요.
- docker-compose.yml 파일이 있는 디렉토리에서 아래 명령어를 실행합니다.
    - docker compose up -d
- 종료 시에는 아래 명령어를 실행합니다.
    - docker-compose down

<details>
  <summary>docker-compose.yml 내용 보기</summary>

```yaml
services:
  zookeeper:
    image: wurstmeister/zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"

  kafka:
    image: wurstmeister/kafka
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092,PLAINTEXT_INTERNAL://kafka:9092
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092,PLAINTEXT_INTERNAL://0.0.0.0:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_INTERNAL:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT_INTERNAL
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    depends_on:
      - zookeeper

  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    container_name: kafka-ui
    ports:
      - "8090:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: local
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:9093
      KAFKA_CLUSTERS_0_ZOOKEEPER: zookeeper:2181
    depends_on:
      - kafka
      - zookeeper

  redis:
    image: redis:latest
    container_name: redis
    ports:
      - "6379:6379"

  mysql:
    image: mysql:latest
    container_name: mysql
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: 1234
      MYSQL_DATABASE: useradmin
```

</details>

API 스팩 확인 및 테스트는 아래 링크에서 확인 가능합니다.

- http://localhost:8080/swagger-ui/index.html