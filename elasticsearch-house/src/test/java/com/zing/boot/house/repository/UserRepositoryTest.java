package com.zing.boot.house.repository;


import com.zing.boot.house.HouseApplicationTest;
import com.zing.boot.house.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class UserRepositoryTest extends HouseApplicationTest {

    @Autowired
    private UserRepository userRepository;

    private final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();

    @Test
    public void testFindOne() {

        String password = passwordEncoder.encodePassword("admin", "2");
        System.out.println(password);

        User user = userRepository.findOne(2L);
        Assert.assertEquals("admin", user.getName());
    }
}