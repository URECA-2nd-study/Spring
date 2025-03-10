package com.spring.fixture;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
public class UserFixture {

    private final Faker faker = new Faker();

    //유저 생성자
    public static User createUser(String email, String password, String name, Role role){
        return User.of(email, password, name, role);
    }

    public static User createUser(String email, String password, String name, Role role, BigDecimal point){
        return User.of(email, password, name, role,point);
    }

    //랜덤 유저 만들기
    public List<User> createRandomUsers(int count){
        List<User> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String email = faker.internet().emailAddress();
            String password = faker.internet().password();
            String name = faker.name().fullName();
            Role role = (i % 2 == 0) ? Role.MEMBER : Role.ADMIN; // 랜덤 역할 (반반)

            User user = User.of(email, password, name, role);
//            users.add(userRepository.save(user)); // DB에 저장
        }

        return users;
    }
}
