package com.smart.smartcontact.Repository;


import com.smart.smartcontact.entities.Contact;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact,Integer> {
    
    @Query("from Contact as c where c.user.uid =:userid")
    public Page<Contact> findContactsByUser(@Param("userid") int userid,Pageable pageable);
}
