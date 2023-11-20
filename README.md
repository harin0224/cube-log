# cube-log

큐브 사용 기록에 대한 API를 받아와 등급업 관련 항목을 등급과 큐브의 종류에 따라 분류하고, 등급과 큐브 종류별로 사용자가 몇 번의 시도만에 등급업에 성공했는지와 확률인지를 계산해줍니다.

## 기술

> - Java
> - Spring Boot
> - Kotlin
> - Github Actions
> - Docker

## 기능

- 사용자의 큐브 종류와 등급별 사용 기록을 제공합니다.
- 사용자의 등급업 확률을 제공합니다.

## 실행

```
1. git clone https://github.com/harin0224/cube-log.git
2. cd cube-log
3. ./gradlew clean build
4. docker build -t cube-log .
5. docker run -p 8080:8080 cube-log
```
