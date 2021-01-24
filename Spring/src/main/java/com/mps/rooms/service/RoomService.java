package com.mps.rooms.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mps.rooms.domain.Reservation;
import com.mps.rooms.domain.Room;
import com.mps.rooms.domain.Status;
import com.mps.rooms.dto.ReservationDto;
import com.mps.rooms.dto.RoomDto;
import com.mps.rooms.repository.RoomRepository;
import com.mps.rooms.util.Utils;

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

		return RoomDto.builder().name(room.getName()).status(Status.toDto(room.getStatus()))
				.description(room.getDescription()).currentReservation(currentReservation)
				.pastFiveReservations(lastFiveReservations).following(userFollowsRoom).build();
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
			if (TimeUnit.MINUTES.convert(new Date().getTime() - room.getPendingDate().getTime(),
					TimeUnit.MILLISECONDS) < 5) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Room " + room.getName() + " was already set in pending less than 5 minutes ago!");
			}
		} else if (room.getStatus().getCode().equals(2)) {
			ReservationDto currentReservation = reservationService.getCurrentReservation(roomId);
			if (new Date().before(currentReservation.getEndTime())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Room " + room.getName() + " is already occupied!");
			}
		}

		Status pending = new Status();
		pending.setCode(1);
		room.setPendingDate(new Date());
		room.setPendingBy(Utils.getUserEmail());
		room.setStatus(pending);
		roomRepository.save(room);

		return "Success";
	}

	public String occupy(Integer roomId, ReservationDto reservationDto) {
		Room room = roomRepository.findById(roomId).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There's no room with id: " + roomId + "!");
		});

		String userEmail = Utils.getUserEmail();

		if (room.getStatus().getCode().equals(1)) {
			log.warn("Room {} was already in a pending state", roomId);
			if (!room.getPendingBy().equals(userEmail)) {
				if (TimeUnit.MINUTES.convert(new Date().getTime() - room.getPendingDate().getTime(),
						TimeUnit.MILLISECONDS) < 5) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"You took too long to occupy this room! Another user is in the process of occupying room: "
									+ room.getName());
				}
			}
		} else if (room.getStatus().getCode().equals(2)) {
			ReservationDto currentReservation = reservationService.getCurrentReservation(roomId);
			if (currentReservation != null && new Date().before(currentReservation.getEndTime())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Room " + room.getName() + " is already occupied!");
			}
		}

		Status occupied = new Status();
		occupied.setCode(2);
		room.setPendingDate(null);
		room.setPendingBy(null);
		room.setStatus(occupied);
		roomRepository.save(room);

		Reservation reservation = new Reservation();
		reservation.setName(reservationDto.getName());
		reservation.setEmail(reservationDto.getEmail());
		reservation.setReason(reservationDto.getReason());
		reservation.setRoom(room);
		reservation.setBeginDate(reservationDto.getStartTime());
		reservation.setEndDate(reservationDto.getEndTime());
		reservation.setActualEndDate(reservationDto.getEndTime());
		reservationService.saveReservation(reservation);

		return "Success";
	}

	public String free(Integer roomId, boolean userAction) {
		Room room = roomRepository.findById(roomId).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There's no room with id: " + roomId + "!");
		});

		if (userAction) {
			String userEmail = Utils.getUserEmail();

			if (room.getStatus().getCode().equals(0)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"The room " + room.getName() + " was already free!");
			}
			if (room.getStatus().getCode().equals(1)) {
				log.warn("Room {} was already in a pending state", roomId);
				if (!room.getPendingBy().equals(userEmail)) {
					if (TimeUnit.MINUTES.convert(new Date().getTime() - room.getPendingDate().getTime(),
							TimeUnit.MILLISECONDS) < 5) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
								"The room: " + room.getName() + " is already in a pending status!");
					}
				}
				room.setPendingBy(null);
				room.setPendingDate(null);
			} else if (room.getStatus().getCode().equals(2)) {
				Reservation currentReservation = reservationService.getCurrentReservationEntity(roomId);
				if (currentReservation != null && !userEmail.equals(currentReservation.getEmail())) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"Room " + room.getName() + " is occupied by someone else!");
				} else if (currentReservation != null) {
					currentReservation.setActualEndDate(new Date());
					reservationService.saveReservation(currentReservation);
				}
			}
		} else {
			if (room.getStatus().getCode().equals(1)) {
				if (TimeUnit.MINUTES.convert(new Date().getTime() - room.getPendingDate().getTime(),
						TimeUnit.MILLISECONDS) < 5) {
					return "";
				}
				room.setPendingBy(null);
				room.setPendingDate(null);
			} else if (room.getStatus().getCode().equals(2)) {
				Reservation currentReservation = reservationService.getCurrentReservationEntity(roomId);
				if (currentReservation != null && new Date().before(currentReservation.getActualEndDate())) {
					return "";
				}
			}
		}

		List<String> followers = followService.getFollows(roomId);
		// TODO: Send email to followers

		Status free = new Status();
		free.setCode(0);
		room.setStatus(free);
		roomRepository.save(room);

		return "Success";
	}
}
