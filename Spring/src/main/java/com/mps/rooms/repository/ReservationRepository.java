package com.mps.rooms.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mps.rooms.domain.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	@Query("Select r from Reservation r where r.room.id = ?1 and ?2 between r.beginDate and r.actualEndDate")
	public Optional<Reservation> findCurrentReservation(Integer roomId, Date date);
	
	public List<Reservation> findTop5ByRoomIdOrderByBeginDateDesc(Integer roomId);
}
