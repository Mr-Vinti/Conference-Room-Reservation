package com.mps.rooms.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mps.rooms.domain.Room;
import com.mps.rooms.domain.Status;
import com.mps.rooms.dto.ReservationDto;
import com.mps.rooms.dto.RoomDto;
import com.mps.rooms.repository.RoomRepository;
import com.mps.rooms.util.Utils;

import io.micrometer.core.instrument.util.TimeUtils;
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

	public String pending(Integer roomId) {
		Room room = roomRepository.findById(roomId).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There's no room with id: " + roomId + "!");
		});
		
		if (room.getStatus().getCode().equals(1)) {
			log.warn("Room {} was already in a pending state", roomId);
			if (TimeUnit.MINUTES.convert(new Date().getTime() - room.getPendingDate().getTime(), TimeUnit.MILLISECONDS) < 5) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room " + room.getName() + " was already set in pending less than 5 minutes ago!");
			}
		} else if (room.getStatus().getCode().equals(2)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room " + room.getName() + " is already occupied!");
		}
		
		Status pending = new Status();
		pending.setCode(1);
		room.setPendingDate(new Date());
		room.setPendingBy(Utils.getUserEmail());
		room.setStatus(pending);
		roomRepository.save(room);
		
		return "Success";
	}
}
