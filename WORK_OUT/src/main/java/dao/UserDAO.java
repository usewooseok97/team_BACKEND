package dao;

import dto.UserDTO;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 회원 정보를 관리하는 DAO 클래스 (메모리 기반)
 * 나중에 MongoDB로 전환 가능하도록 설계
 */
public class UserDAO {
    // 싱글톤 패턴
    private static UserDAO instance = new UserDAO();

    // 메모리 기반 저장소 (ConcurrentHashMap으로 Thread-safe 보장)
    private Map<String, UserDTO> userMap;
    private AtomicInteger idCounter;

    private UserDAO() {
        userMap = new ConcurrentHashMap<>();
        idCounter = new AtomicInteger(1);

        // 테스트용 최고 관리자 계정 생성
        UserDTO admin = new UserDTO();
        admin.setId("1");
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setEmail("admin@workout.com");
        admin.setName("최고관리자");
        admin.setPhone("010-0000-0000");
        admin.setRole("SUPER_ADMIN");
        userMap.put(admin.getUsername(), admin);

        // 테스트용 일반 사용자 생성
        UserDTO user = new UserDTO();
        user.setId("2");
        user.setUsername("user1");
        user.setPassword("user123");
        user.setEmail("user1@workout.com");
        user.setName("홍길동");
        user.setPhone("010-1111-1111");
        user.setRole("USER");
        userMap.put(user.getUsername(), user);

        idCounter.set(3);
    }

    public static UserDAO getInstance() {
        return instance;
    }

    /**
     * 회원 가입
     */
    public boolean register(UserDTO user) {
        // 중복 확인
        if (userMap.containsKey(user.getUsername())) {
            return false;
        }

        // ID 자동 생성
        user.setId(String.valueOf(idCounter.getAndIncrement()));

        // 저장
        userMap.put(user.getUsername(), user);
        return true;
    }

    /**
     * 로그인 (username과 password 확인)
     */
    public UserDTO login(String username, String password) {
        UserDTO user = userMap.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * Username으로 회원 조회
     */
    public UserDTO findByUsername(String username) {
        return userMap.get(username);
    }

    /**
     * ID로 회원 조회
     */
    public UserDTO findById(String id) {
        return userMap.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * 회원 정보 수정
     */
    public boolean update(UserDTO user) {
        if (!userMap.containsKey(user.getUsername())) {
            return false;
        }
        userMap.put(user.getUsername(), user);
        return true;
    }

    /**
     * 회원 삭제
     */
    public boolean delete(String username) {
        if (!userMap.containsKey(username)) {
            return false;
        }
        userMap.remove(username);
        return true;
    }

    /**
     * 모든 회원 조회 (관리자용)
     */
    public List<UserDTO> findAll() {
        return new ArrayList<>(userMap.values());
    }

    /**
     * Username 중복 확인
     */
    public boolean isUsernameExists(String username) {
        return userMap.containsKey(username);
    }

    /**
     * Email 중복 확인
     */
    public boolean isEmailExists(String email) {
        return userMap.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    /**
     * 회원 수 조회
     */
    public int getUserCount() {
        return userMap.size();
    }
}
