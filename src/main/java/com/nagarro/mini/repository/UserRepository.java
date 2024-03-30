package com.nagarro.mini.repository;

import java.awt.print.Pageable;


import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagarro.mini.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}

