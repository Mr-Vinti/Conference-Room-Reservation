package com.mps.rooms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mps.rooms.domain.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

}
