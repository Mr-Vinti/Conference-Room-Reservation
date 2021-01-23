package com.mps.rooms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mps.rooms.domain.Status;
import com.mps.rooms.dto.StatusDto;
import com.mps.rooms.repository.StatusRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StatusService {
	private StatusRepository statusRepository;
	
	public List<StatusDto> getStatuses() {
		return statusRepository.findAll().stream().map(Status::toDto).collect(Collectors.toList());
	}
}
