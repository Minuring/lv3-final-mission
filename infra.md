# 인프라

이 서비스의 인프라 구축을 다음과 같이 했습니다.

|                | 운영 서버     | 개발 서버    | 로컬 |
|----------------|-----------|----------|----|
| SpringBoot App | EC2(prod) | EC2(dev) | -  |
| Database       | RDS       | h2       | h2 |

### 운영 환경 격리

- 운영 서버의 EC2, RDS는 같은 VPC 내에서 다른 서브넷에 속합니다.

- EC2는 public subnet에, RDS는 private subnet에 위치시켰습니다.

- EC2는 northeast-2a, RDS는 northeast-2b 가용영역에 위치시켰습니다.

이를 통해 외부에서는 꼭 필요한 접근(애플리케이션 80포트) 외에는 접근이 불가능하게 설계했습니다.

### 개발과 운영 설정 분리

3가지 환경(운영, 개발, 로컬)에서의 불일치는 없애되, 설정과 관련된 요소들을 독립시키고자 했습니다.

- 모든 환경에서 스프링부트 애플리케이션은 Docker를 이용해 컨테이너화 했습니다.

- 개발 서버, 로컬에서는 DB 간소화를 위해 H2 인메모리 데이터베이스를 사용했습니다.

### 테스트 및 배포 자동화

Github Actions를 이용해 테스트와 배포 과정을 자동화했습니다.

아래와 같은 과정으로 개발 프로세스가 진행됩니다.

1. 개발한 브랜치에서 develop 브랜치로 pull request
    
    github action runner가 docker 이미지를 테스트삼아 빌드합니다.


2. 개발한 브랜치를 develop 브랜치로 merge

    github action runner가 docker 이미지를 빌드하고 [GHCR](https://docs.github.com/ko/packages/working-with-a-github-packages-registry/working-with-the-container-registry)에 푸쉬합니다. 
    
    개발 서버의 EC2가 해당 이미지를 pull받아 실행합니다.


3. develop 브랜치의 내용을 main 브랜치에 merge

    운영 서버의 EC2가 해당 이미지를 pull받아 실행합니다.
