package com.learn.springSecurity2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.springSecurity2.models.ApplicationUser;

@Repository
public interface UserRepo extends JpaRepository<ApplicationUser, Integer>{

	Optional<ApplicationUser> findByUsername(String username);
}
