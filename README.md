# 시나리오 기반 제어 프로토콜 시험환경 구축
프로젝트 기간 | 2023.09 ~ 2023.11
인원 | FE/BE - 2명, 시스템 - 2명

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

## 웹 페이지
1. 로그인 화면 (비로그인 시 사용 불가)
![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/03d5e153-2361-4555-be5e-7ae6347830f6/da72264a-615c-4c27-872a-f70c1681d2ee/Untitled.png)
세션 만료 시 사용자에게 연장 요청
![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/03d5e153-2361-4555-be5e-7ae6347830f6/12b96134-4e98-493d-bc34-752a46d42b09/Untitled.png)

2. 로그인 후 메인페이지
   - 최근 항목 리스트
![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/03d5e153-2361-4555-be5e-7ae6347830f6/68d67fe5-5fbd-40b4-bc7a-71da530fc4ca/Untitled.png)

3. 로그 데이터 검색 (원하는 기간 설정)
![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/03d5e153-2361-4555-be5e-7ae6347830f6/2d0aeddb-d18a-4862-991e-37c45b543170/Untitled.png)

4. 접근 가능 IP 리스트
![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/03d5e153-2361-4555-be5e-7ae6347830f6/a89ed7e1-0940-4e8a-932a-dc447b6e6697/Untitled.png)
- IP Block Off
    - 페이지 접근 불가 및 Block 처리
    ![image](https://github.com/kimdayeon37/SPTB-Scenario-based-control-Protocol-Test-Bed-/assets/93921784/1a399c99-6eb7-4366-9903-6961b2babcb3)

5. SMTP 메일 전송
![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/03d5e153-2361-4555-be5e-7ae6347830f6/0d4ec8fa-79fe-4d8a-8f99-450fa4a846a1/Untitled.png)

7. 프로토콜 파라미터 설정
![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/03d5e153-2361-4555-be5e-7ae6347830f6/f176f33c-ebf9-40cc-b2cf-0637a23ddc06/Untitled.png)
![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/03d5e153-2361-4555-be5e-7ae6347830f6/f80c8182-11a2-46e4-9f48-8f84689545fd/Untitled.png)
![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/03d5e153-2361-4555-be5e-7ae6347830f6/a20c8c43-d95a-4fd7-91bb-23fd980b6270/Untitled.png)
- 설정 후 시스템 통신
  - ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/03d5e153-2361-4555-be5e-7ae6347830f6/fdfe6cdf-fe92-4388-a6f2-a0dd406986b7/Untitled.png)
- 트리구조
  - ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/03d5e153-2361-4555-be5e-7ae6347830f6/fa8d4d95-6dd6-4331-ac68-662a0a0d9d73/Untitled.png)
## 🗂 프로젝트 구조
### FE
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

## BE
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
