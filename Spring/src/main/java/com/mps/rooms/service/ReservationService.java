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

	public ReservationDto getCurrentReservation(Integer id) {
		Optional<Reservation> currentReservation = reservationRepository.findCurrentReservation(id, new Date());
		return currentReservation.isPresent() ? Reservation.toDto(currentReservation.get()) : null;
	}

	public List<ReservationDto> getLastFive(Integer id) {
		List<ReservationDto> lastFive = reservationRepository.findTop5ByRoomIdOrderByBeginDateDesc(id).stream()
				.map(Reservation::toDto).collect(Collectors.toList());
		return lastFive;
	}

}
