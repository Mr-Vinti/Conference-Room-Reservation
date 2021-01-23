package com.mps.rooms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.mps.rooms.domain.Room;
import com.mps.rooms.domain.Status;
import com.mps.rooms.dto.ReservationDto;
import com.mps.rooms.dto.RoomDto;
import com.mps.rooms.repository.RoomRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
@Log4j2
public class RoomService {
	private RoomRepository roomRepository;
	private ReservationService reservationService;
	private FollowService followService;

	private RoomDto convertToDto(Room room) {
		ReservationDto currentReservation = reservationService.getCurrentReservation(room.getId());
		List<ReservationDto> lastFiveReservations = reservationService.getLastFive(room.getId());

		String userEmail = ((Jwt) ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.getPrincipal()).getClaimAsString("preferred_username");

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
