# 토비의 스프링 예제 - SpringBoot
토비의 스프링 예제를 스프링부트로 구축한겁니다.

토비의 스프링과 조금 다른점이 있을 수 있습니다.

현재는 1장만 구축되어있습니다.

추후에 본인이 2장 3장까지 늘려가시면 됩니다.

---

## ⚙️ Skills

- java 11
- springboot 2.5.5
- H2 DB
- JDBC

---

시스템 사용법

토비의 스프링에서는 로컬 DB를 사용합니다.

하지만 로컬DB 의 설치가 귀찮은 관계로

H2 DB를 사용하겠습니다.

H2 DB는 인모메리 DB라 서버를 내렸다 키시면 초기화가 됩니다.

JPA처럼 자동생성이 없기에 직접 쿼리문을 작성해주셔야 합니다 <br> <br>

**쿼리문 생성위치**

src/main/resource/schema.sql 

해당 파일에 쿼리문을 작성하시면 서버가 실행되면서 자동으로 쿼리문을 실행합니다

ex) 테이블생성 더미데이터 입력

이렇게 한후 테스트코드를 돌리거나 서버를 구동시켜보시면 됩니다.
<br><br><br>

**JDBC 사용법**

토비의 스프링에선 Connection 에 직접 jdbc://mysql 이런식으로 넣어주지면

스프링부트의 DI를이용햐여 

```java
package com.example.springtobi.chapter1;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@NoArgsConstructor
@Component
public class UserDao {

    @Autowired
    private DataSource dataSource;

    // Insert
    public void add(User user) throws ClassNotFoundException {
        String sql = "insert into users (id, name, password) values(?,?,?)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1,user.getId());
            ps.setString(2,user.getName());
            ps.setString(3,user.getPassword());

            int result = ps.executeUpdate();

            if(result != 1) {
                throw new SQLException();
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public User get(String id) throws ClassNotFoundException {

        User user = new User();

        String sql = "select * from users where id = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));

            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return user;
    }

}

```

DataSource 를 넣어주시면됩니다~ 

현재 @Autowired 를 사용하고 있지만 스프링부트에서 권장하는 방식이 아니기에

본인의 입맛대로 생성자 주입으로 변경하셔도 좋습니다.
