package com.smart.smartcontact.Repository;

import com.smart.smartcontact.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {
    
    @Query("select u from User u where u.email =:email")
    public User getUserByUserEmail(@Param("email") String email);
}
