package com.mps.rooms.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mps.rooms.domain.Reservation;
import com.mps.rooms.dto.ReservationDto;
import com.mps.rooms.repository.ReservationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReservationService {
	private ReservationRepository reservationRepository;
	
	public Reservation getCurrentReservationEntity(Integer roomId) {
		Optional<Reservation> currentReservation = reservationRepository.findCurrentReservation(roomId, new Date());
		return currentReservation.isPresent() ? currentReservation.get() : null;
	}

	public ReservationDto getCurrentReservation(Integer roomId) {
		Optional<Reservation> currentReservation = reservationRepository.findCurrentReservation(roomId, new Date());
		return currentReservation.isPresent() ? Reservation.toDto(currentReservation.get()) : null;
	}

	public List<ReservationDto> getLastFive(Integer roomId) {
		List<ReservationDto> lastFive = reservationRepository.findTop5ByRoomIdOrderByBeginDateDesc(roomId).stream()
				.map(Reservation::toDto).collect(Collectors.toList());
		return lastFive;
	}
	
	public void saveReservation(Reservation reservation) {
		reservationRepository.save(reservation);
	}

}
