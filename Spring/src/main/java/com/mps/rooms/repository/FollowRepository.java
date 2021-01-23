package com.mps.rooms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mps.rooms.domain.Follow;
import com.mps.rooms.domain.FollowPK;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowPK> {

}