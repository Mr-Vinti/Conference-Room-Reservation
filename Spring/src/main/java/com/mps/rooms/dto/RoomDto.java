package com.mps.rooms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDto {
	private String name;
	private StatusDto status;
	private String description;
	private ReservationDto currentReservation;
	private List<ReservationDto> pastFiveReservations;
	private Boolean following;
}
