package com.example.asyncexample.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.asyncexample.entity.User;

public interface UserRepository extends JpaRepository<User,Integer> {
}