package com.mps.rooms.dto;

import java.util.Date;

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
public class ReservationDto {
	private String name;
	private String email;
	private Date date;
	private Date startTime;
	private Date endTime;
	private String reason;
}
