package com.mps.rooms.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mps.rooms.dto.ReservationDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(schema = "dbo", name = "RESERVATION")
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "ROOM_ID", referencedColumnName = "ID")
	private Room room;
	
	@Column(name = "NAM")
	private String name;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "BEGIN_DT")
	private Date beginDate;
	
	@Column(name = "END_DT")
	private Date endDate;
	
	@Column(name = "ACTL_END_DT")
	private Date actualEndDate;
	
	@Column(name = "REASN")
	private String reason;
	
	public static ReservationDto toDto(Reservation entity) {
		return ReservationDto.builder()
				.name(entity.name)
				.email(entity.email)
				.startTime(entity.beginDate)
				.endTime(entity.actualEndDate)
				.reason(entity.reason)
				.build();
	}
}
