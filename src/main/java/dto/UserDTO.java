package dto;

import java.time.LocalDateTime;

/**
 * 회원 정보를 표현하는 DTO 클래스
 */
public class UserDTO {
    private String id;              // 사용자 ID (Primary Key)
    private String username;        // 사용자명 (로그인용)
    private String password;        // 비밀번호
    private String name;            // 실명
    private String role;            // 권한 (USER, ADMIN)
    private LocalDateTime regDate;  // 가입일시

    public UserDTO() {
        this.regDate = LocalDateTime.now();
        this.role = "USER"; // 기본값은 일반 사용자
    }

    public UserDTO(String id, String username, String password, String name, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.regDate = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    /**
     * 관리자 권한 확인 (일반 관리자 + 최고 관리자)
     */
    public boolean isAdmin() {
        return "ADMIN".equals(this.role) || "SUPER_ADMIN".equals(this.role);
    }

    /**
     * 최고 관리자 권한 확인
     */
    public boolean isSuperAdmin() {
        return "SUPER_ADMIN".equals(this.role);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", regDate=" + regDate +
                '}';
    }
}
