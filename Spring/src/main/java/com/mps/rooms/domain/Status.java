package com.mps.rooms.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mps.rooms.dto.StatusDto;

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
@Table(schema = "dbo", name = "STATUS")
public class Status {
	@Id
	@Column(name = "CD")
	private Integer code;
	
	@Column(name = "[DESC]")
	private String description;
	
	public static StatusDto toDto(Status entity) {
		return StatusDto.builder()
				.code(entity.code)
				.description(entity.getDescription())
				.build();
	}
}
