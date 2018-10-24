package com.zing.boot.house.repository;

import com.zing.boot.house.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByName(String username);

    User findUserByPhoneNumber(String telephone);

    @Modifying
    @Query("UPDATE User AS user SET user.name = :name WHERE id = :id")
    void updateUsername(@Param(value = "id") Long id, @Param(value = "name") String name);

    @Modifying
    @Query("UPDATE User AS user SET user.email = :email WHERE id = :id")
    void updateEmail(@Param(value = "id") Long id, @Param(value = "email") String email);

    @Modifying
    @Query("UPDATE User AS user SET user.password = :password WHERE id = :id")
    void updatePassword(@Param(value = "id") Long id, @Param(value = "password") String password);
}
