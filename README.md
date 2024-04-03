# 시나리오 기반 제어 프로토콜 시험환경 구축
- 프로젝트 기간 | 2023.09 ~ 2023.11

- 인원 | FE/BE - 2명, 시스템 - 2명

## ⚡ 기술스택
### FE 
<img src="https://img.shields.io/badge/vue-4FC08D?style=flat-square&logo=vue&logoColor=white"/> <img src="https://img.shields.io/badge/quasar-050A14?style=flat-square&logo=quasar&logoColor=white"/> <img src="https://img.shields.io/badge/typescript-3178C6?style=flat-square&logo=typescript&logoColor=white"/>
### BE
<img src="https://img.shields.io/badge/springboot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/> <img src="https://img.shields.io/badge/kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white"/> <img src="https://img.shields.io/badge/springsecurity-6DB33F?style=flat-square&logo=springsecurity&logoColor=white"/> <img src="https://img.shields.io/badge/swagger-85EA2D?style=flat-square&logo=swagger&logoColor=white"/> <img src="https://img.shields.io/badge/mariadb-003545?style=flat-square&logo=mariadb&logoColor=white"/>
### Dev-Ops
<img src="https://img.shields.io/badge/jenkins-D24939?style=flat-square&logo=jenkins&logoColor=white"/> <img src="https://img.shields.io/badge/docker-2496ED?style=flat-square&logo=docker&logoColor=white"/> <img src="https://img.shields.io/badge/docker-2496ED?style=flat-square&logo=docker&logoColor=white"/> <img src="https://img.shields.io/badge/nginx-009639?style=flat-square&logo=nginx&logoColor=white"/>
### Communication
<img src="https://img.shields.io/badge/gitea-609926?style=flat-square&logo=gitea&logoColor=white"/> <img src="https://img.shields.io/badge/figma-F24E1E?style=flat-square&logo=figma&logoColor=white"/> <img src="https://img.shields.io/badge/slack-4A154B?style=flat-square&logo=slack&logoColor=white"/>

## 시스템 아키텍처
<img width="1037" alt="스크린샷 2024-04-03 오전 12 44 30" src="https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/cdcda520-bc34-4bfa-8ecd-20989564561f">

---

## 웹 페이지
1. 로그인 화면 (비로그인 시 사용 불가)
![Untitled](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/43742dee-3292-4ee3-a9bf-68d0e406b0cf)

- 세션 만료 시 사용자에게 연장 요청
![Untitled](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/7e42b982-d0a9-4d27-9cae-e0942eedc4a7)


2. 로그인 후 메인페이지
   - 최근 항목 리스트
![Untitled](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/7883041b-7f33-4524-9af0-79bd362f6acf)


3. 로그 데이터 검색 (원하는 기간 설정)
![Untitled](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/712798f3-4466-4eed-960e-a26b27e114fa)


4. 접근 가능 IP 리스트
![Untitled](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/3196ad26-1317-414f-b1c0-c057d43dad5a)

- IP Block Off
    - 페이지 접근 불가 및 Block 처리
    ![image](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/1a399c99-6eb7-4366-9903-6961b2babcb3)

5. SMTP 메일 전송
![Untitled](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/714a8b1f-376c-428a-9e90-d270189affcf)


7. 프로토콜 파라미터 설정
![Untitled](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/96c42215-fe92-4919-b9e4-1dfa1eeb5544)
![Untitled](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/077a2285-5528-49da-a6ac-5db542ee041a)
![Untitled](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/2dca75fd-2496-4694-8021-f08375c33a53)

- 설정 후 시스템 통신
  - ![Untitled](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/bc0dca55-6a38-437a-9d8a-6d694fa3010c)
  - 트리구조
  - ![Untitled](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/6bee4ce7-b00d-4642-b1d0-0f605a14b804)

## 🗂 프로젝트 구조
### FE
```markdown
└─📦 src
  ├─📂 axios
  ├─📂 components
  │  ├─📂 MME
  │  ├─📂 MSE
  │  ├─📂 OPCUAClient
  │  ├─📂 OPCUAServer
  │  ├─📂 Setting
  │  ├─📂 Stmp
  │  ├─📂 SystemLog
  │  └─📂 Trans-Log
  ├─📂 views
  │  ├─📂 LoginView
  │  ├─📂 SignipView
  │  ├─📂 BlockedView
  │  ├─📂 LogView
  │  ├─📂 HomeView
  │  ├─📂 SettingView
  │  ├─📂 StmpView
  │  ├─📂 ModbusMasterEthernetView
  │  ├─📂 ModbusSlaveEthernetView
  │  ├─📂 OPCUAClientView
  │  └─📂 OPCUAServerView
  ├─📂 layout
  ├─📂 router
      ├─📜 guards
      ├─📜 index
      └─📜 routes
  ├─📂 store
  │  ├─📂 id
  │  ├─📂 state
  │  ├─📂 user
  │  ├─📂 OPCUAClient
  │  └─📂 OPCUAServer
  ├─📂 utils
  │  ├─📜 api_auth
  │  └─📜 useSse
  ├─📜 types.ts
  └─📜 App.vue
```
---

## BE
```markdown
C:.
├─📂 src
│  └─📂 main
│      └─📂 kotlin
│          └─📂 com
│              └─📂 common
│                  ├─📂 config
│                  │  ├─📂 jackson
│                  │  ├─📂 jooq
│                  │  └─📂 rest
│                  └─📂 security
│              └─📂 module
│                  ├─📂 etc
│                  ├─📂 job
│                  ├─📂 modbus
│                  │  ├─📂 dto
│                  │    ├─📂 ModbusId
│                  │    ├─📂 ModbusLog
│                  │    ├─📂 ModbusMaster
│                  │    ├─📂 ModbusSlave
│                  │    ├─📂 OpcuaClient
│                  │    ├─📂 OpcuaServer
│                  │  └─📂 mapstruct
│                  │    ├─📂 IpWhitelistMapper
│                  │    ├─📂 ServerLogMapper
│                  │    └─📂 UserAccountMapper
│                  │  ├─📂 ModbusController
│                  ├─📂 mqtt
│                  │    ├─📂 MqttProperites
│                  │    ├─📂 MqttService
│                  ├─📂 smtp
│                  │    ├─📂 SmtpController
│                  │    ├─📂 SmtpDto
│                  │    └─📂 SmtpService
│                  ├─📂 user
│                  └─📂 whitelistip
│                  │    ├─📂 IpController
│                  │    ├─📂 IpDto
│                  │    └─📂 IpService
│      └─📂 resources
│          └─📂 db
│  └─📂 test
```

```sh
sudo docker run -d \
--restart=always \
--name=mqtt \
-p 1883:1883 \
-p 9001:9001 \
-v /etc/timezone:/etc/timezone:ro \
-v /etc/localtime:/etc/localtime:ro \
eclipse-mosquitto
```


```sh
vi /mosquitto/config/mosquitto.conf
```


```sh
persistence false
allow_anonymous true
connection_messages true
log_type all
listener 1883
```
