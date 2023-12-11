package com.learn.springSecurity2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.springSecurity2.models.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>{

	Optional<Role> findByAuthority(String authority);
}
