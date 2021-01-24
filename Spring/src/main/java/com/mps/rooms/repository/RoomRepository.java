package com.mps.rooms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mps.rooms.domain.Room;
import com.mps.rooms.domain.Status;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
	
	List<Room> findByStatusIn(List<Status> statuses);
}
