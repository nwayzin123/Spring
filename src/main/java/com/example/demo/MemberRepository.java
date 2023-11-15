package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

	public Member findByUsername(String username);

	

}
