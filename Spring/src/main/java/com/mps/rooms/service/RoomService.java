package com.mps.rooms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mps.rooms.domain.Room;
import com.mps.rooms.domain.Status;
import com.mps.rooms.dto.ReservationDto;
import com.mps.rooms.dto.RoomDto;
import com.mps.rooms.repository.RoomRepository;
import com.mps.rooms.util.Utils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoomService {
	private RoomRepository roomRepository;
	private ReservationService reservationService;
	private FollowService followService;

	private RoomDto convertToDto(Room room) {
		ReservationDto currentReservation = reservationService.getCurrentReservation(room.getId());
		List<ReservationDto> lastFiveReservations = reservationService.getLastFive(room.getId());

		String userEmail = Utils.getUserEmail();

		boolean userFollowsRoom = followService.getFollow(room.getId(), userEmail);

		return RoomDto.builder()
				.name(room.getName())
				.status(Status.toDto(room.getStatus()))
				.description(room.getDescription())
				.currentReservation(currentReservation)
				.pastFiveReservations(lastFiveReservations)
				.following(userFollowsRoom)
				.build();
	}

	public List<RoomDto> getRooms() {
		return roomRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}
}
