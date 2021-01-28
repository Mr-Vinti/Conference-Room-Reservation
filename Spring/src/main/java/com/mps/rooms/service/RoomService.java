package com.mps.rooms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
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
	private StatusService statusService;

	private RoomDto convertToDto(Room room) {
		ReservationDto currentReservation = null;
		String pendingBy = null;
		Date pendingDate = null;
		if (room.getStatus().getCode() == 2) {
			currentReservation = reservationService.getCurrentReservation(room.getId());
		} else if (room.getStatus().getCode() == 1) {
			pendingBy = room.getPendingBy();
			pendingDate = room.getPendingDate();
		}
		List<ReservationDto> lastFiveReservations = reservationService.getLastFive(room.getId());

		String userEmail = Utils.getUserEmail();

		boolean userFollowsRoom = followService.getFollow(room.getId(), userEmail);

		return RoomDto.builder().id(room.getId()).name(room.getName()).status(Status.toDto(room.getStatus()))
				.description(room.getDescription()).currentReservation(currentReservation)
				.pastFiveReservations(lastFiveReservations).pendingBy(pendingBy).pendingDate(pendingDate)
				.following(userFollowsRoom).build();
	}

	public RoomDto getRoom(Integer roomId) {
		return convertToDto(roomRepository.findById(roomId).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There's no room with id: " + roomId + "!");
		}));
	}

	public List<RoomDto> getRooms() {
		return roomRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public RoomDto pending(Integer roomId) {
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

		Status pending = statusService.getStatusById(1);
		room.setPendingDate(new Date());
		room.setPendingBy(Utils.getUserEmail());
		room.setStatus(pending);
		roomRepository.save(room);

		return convertToDto(room);
	}

	public RoomDto occupy(Integer roomId, ReservationDto reservationDto) {
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

		if (reservationDto.getStartTime().after(reservationDto.getEndTime())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The end time can't be before the start time!");
		}

		Status occupied = statusService.getStatusById(2);
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

		return convertToDto(room);
	}

	public RoomDto free(Integer roomId) {
		Room room = roomRepository.findById(roomId).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There's no room with id: " + roomId + "!");
		});

		String userEmail = Utils.getUserEmail();

		if (room.getStatus().getCode().equals(0)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The room " + room.getName() + " was already free!");
		}
		if (room.getStatus().getCode().equals(1)) {
			log.warn("Room {} was already in a pending state", roomId);
			if (!room.getPendingBy().equals(userEmail)) {
				if (room.getPendingDate() != null && TimeUnit.MINUTES
						.convert(new Date().getTime() - room.getPendingDate().getTime(), TimeUnit.MILLISECONDS) < 5) {
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

		List<String> followers = followService.getFollows(roomId);
		// TODO: Send email to followers

		Status free = statusService.getStatusById(0);
		room.setStatus(free);
		roomRepository.save(room);

		return convertToDto(room);
	}

	@Scheduled(cron = "0 * * * * *")
	private void scheduledCheck() {
		log.info("Checking if any room can be set on free");
		List<Status> statusesToCheck = new ArrayList<Status>();

		Status pending = new Status();
		pending.setCode(1);

		Status complete = new Status();
		complete.setCode(2);

		statusesToCheck.add(pending);
		statusesToCheck.add(complete);

		List<Room> roomsToCheck = roomRepository.findByStatusIn(statusesToCheck);
		for (Room room : roomsToCheck) {
			if (room.getStatus().getCode().equals(1)) {
				if (room.getPendingDate() != null && TimeUnit.MINUTES
						.convert(new Date().getTime() - room.getPendingDate().getTime(), TimeUnit.MILLISECONDS) < 5) {
					continue;
				}
				room.setPendingBy(null);
				room.setPendingDate(null);
			} else if (room.getStatus().getCode().equals(2)) {
				Reservation currentReservation = reservationService.getCurrentReservationEntity(room.getId());
				if (currentReservation != null && new Date().before(currentReservation.getActualEndDate())) {
					continue;
				}
			}

			List<String> followers = followService.getFollows(room.getId());
			// TODO: Send email to followers

			Status free = new Status();
			free.setCode(0);
			room.setStatus(free);
			roomRepository.save(room);
		}
	}

	public RoomDto upsert(Integer roomId, String name, String description) {
		Room room;
		if (roomId != null) {
			room = this.roomRepository.findById(roomId).orElseThrow(() -> {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There's no room with id: " + roomId + "!");
			});
		} else {
			room = new Room();
			room.setStatus(statusService.getStatusById(0));
		}

		room.setName(name);
		room.setDescription(description);
		roomRepository.save(room);
		return convertToDto(roomRepository.save(room));
	}

}
