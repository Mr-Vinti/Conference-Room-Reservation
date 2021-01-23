package com.mps.rooms.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class FollowPK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1649146667499939889L;

	@Column(name = "ROOM_ID")
	private Integer roomId;
	
	@Column(name = "EMAIL")
	private String email;
}
