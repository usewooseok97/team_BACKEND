package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import dto.UserDTO;
import mongoutil.MongoConn;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 회원 정보를 관리하는 DAO 클래스 (MongoDB 기반)
 */
public class UserDAO {
    // 싱글톤 패턴
    private static UserDAO instance = new UserDAO();

    private MongoCollection<Document> userCollection;

    private UserDAO() {
        MongoDatabase database = MongoConn.getDatabase();
        userCollection = database.getCollection("users");

        // 초기 관리자 계정 생성 (존재하지 않을 경우에만)
        if (userCollection.countDocuments(Filters.eq("username", "admin")) == 0) {
            UserDTO admin = new UserDTO();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setName("관리자");
            admin.setRole("SUPER_ADMIN");
            register(admin);
        }
    }

    public static UserDAO getInstance() {
        return instance;
    }

    /**
     * UserDTO를 MongoDB Document로 변환
     */
    private Document userToDocument(UserDTO user) {
        Document doc = new Document();
        if (user.getId() != null) {
            doc.append("_id", new ObjectId(user.getId()));
        }
        doc.append("username", user.getUsername())
           .append("password", user.getPassword())
           .append("name", user.getName())
           .append("role", user.getRole() != null ? user.getRole() : "USER")
           .append("regDate", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        return doc;
    }

    /**
     * MongoDB Document를 UserDTO로 변환
     */
    private UserDTO documentToUser(Document doc) {
        if (doc == null) return null;

        UserDTO user = new UserDTO();
        user.setId(doc.getObjectId("_id").toString());
        user.setUsername(doc.getString("username"));
        user.setPassword(doc.getString("password"));
        user.setName(doc.getString("name"));
        user.setRole(doc.getString("role"));

        if (doc.containsKey("regDate")) {
            Date regDate = doc.getDate("regDate");
            user.setRegDate(LocalDateTime.ofInstant(regDate.toInstant(), ZoneId.systemDefault()));
        }

        return user;
    }

    /**
     * 회원 가입
     */
    public boolean register(UserDTO user) {
        try {
            System.out.println("=== 회원가입 시작 ===");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Name: " + user.getName());

            // 중복 확인
            if (isUsernameExists(user.getUsername())) {
                System.out.println("오류: 이미 존재하는 아이디");
                return false;
            }

            // MongoDB에 저장
            Document doc = userToDocument(user);
            System.out.println("저장할 Document: " + doc.toJson());
            userCollection.insertOne(doc);
            System.out.println("회원가입 성공!");
            return true;
        } catch (Exception e) {
            System.out.println("회원가입 오류 발생:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 로그인 (username과 password 확인)
     */
    public UserDTO login(String username, String password) {
        try {
            Document doc = userCollection.find(
                Filters.and(
                    Filters.eq("username", username),
                    Filters.eq("password", password)
                )
            ).first();

            return documentToUser(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Username으로 회원 조회
     */
    public UserDTO findByUsername(String username) {
        try {
            Document doc = userCollection.find(Filters.eq("username", username)).first();
            return documentToUser(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ID로 회원 조회
     */
    public UserDTO findById(String id) {
        try {
            Document doc = userCollection.find(Filters.eq("_id", new ObjectId(id))).first();
            return documentToUser(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 회원 정보 수정
     */
    public boolean update(UserDTO user) {
        try {
            System.out.println("=== 회원 정보 수정 ===");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Name: " + user.getName());
            System.out.println("Role: " + user.getRole());

            userCollection.updateOne(
                Filters.eq("_id", new ObjectId(user.getId())),
                Updates.combine(
                    Updates.set("password", user.getPassword()),
                    Updates.set("name", user.getName()),
                    Updates.set("role", user.getRole())
                )
            );
            System.out.println("회원 정보 수정 성공!");
            return true;
        } catch (Exception e) {
            System.out.println("회원 정보 수정 오류:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 회원 삭제
     */
    public boolean delete(String username) {
        try {
            userCollection.deleteOne(Filters.eq("username", username));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 모든 회원 조회 (관리자용)
     */
    public List<UserDTO> findAll() {
        List<UserDTO> users = new ArrayList<>();
        try {
            for (Document doc : userCollection.find()) {
                users.add(documentToUser(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Username 중복 확인
     */
    public boolean isUsernameExists(String username) {
        try {
            return userCollection.countDocuments(Filters.eq("username", username)) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 회원 수 조회
     */
    public int getUserCount() {
        try {
            return (int) userCollection.countDocuments();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
//    /**
//     * ID와 이메일이 일치하는 회원이 있는지 확인
//     */
//    public boolean checkUserEmail(String username, String email) {
//        try {
//            Document doc = userCollection.find(
//                Filters.and(
//                    Filters.eq("username", username),
//                    Filters.eq("email", email)  // email 필드가 users 컬렉션에 있어야 함
//                )
//            ).first();
//
//            return doc != null;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

}
