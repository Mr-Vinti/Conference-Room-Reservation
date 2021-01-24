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
@Table(schema = "dbo", name = "ROOM")
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "NAM")
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "STA_CD", referencedColumnName = "CD")
	private Status status;
	
	@Column(name = "[DESC]")
	private String description;
	
	@Column(name = "PENDNG_DT")
	private Date pendingDate;
	
	@Column(name = "PENDNG_BY")
	private String pendingBy;
}
