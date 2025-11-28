# Security Policy (Java Version 기반)

## Supported Java Versions

우리 프로젝트는 아래의 Java 버전에서 보안 업데이트 및 유지 관리를 지원합니다.

| Java Version | Supported          |
| ------------ | ------------------ |
| 17 LTS       | ✅ Yes (기본 지원) |
| 11 LTS       | ⚠️ 제한적 지원     |
| 8            | ❌ Not Supported   |

> 프로젝트는 현재 **JDK 17 LTS** 환경을 기준으로 개발 및 테스트되고 있습니다.

---

## Reporting a Vulnerability

보안 취약점을 발견하신 경우 아래 절차에 따라 보고해주세요.

### 📩 보고 방법
- 이메일: **anam0409@dongyang.ac.kr**
- 제목: `[Security Issue] 취약점 보고`
- 포함할 내용:
  - 취약점 설명 및 발생 조건
  - 재현(Reproduce) 단계
  - 사용 환경 (OS, 브라우저, JDK 버전, 서버 정보 등)
  - 로그 또는 스크린샷(가능한 경우)

### ⏱️ 처리 절차
- 3영업일 이내 검토 및 회신
- 심각한 취약점은 즉시 패치 또는 Hotfix 배포
- 패치 완료 후 릴리즈 노트에 공지

### ⚠️ 주의사항
- 보안 취약점을 **공개 Issue로 게시하지 말아주세요.**
- 패치 이전에 취약점이 공개되면 악용될 위험이 있습니다.

---

## Security Updates

- 지원되는 Java 버전에서만 보안 업데이트와 호환성 패치가 제공됩니다.
- Java 8 및 비 LTS 버전은 테스트 대상이 아니며, 보안 이슈도 처리되지 않습니다.
