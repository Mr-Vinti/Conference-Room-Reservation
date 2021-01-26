package com.mps.rooms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mps.rooms.domain.Status;
import com.mps.rooms.dto.StatusDto;
import com.mps.rooms.repository.StatusRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StatusService {
	private StatusRepository statusRepository;
	
	public Status getStatusById(Integer statusId) {
		return statusRepository.findById(statusId).orElseThrow(() -> {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status id " + statusId + " can't be found!");
		});
	}
	
	public List<StatusDto> getStatuses() {
		return statusRepository.findAll().stream().map(Status::toDto).collect(Collectors.toList());
	}
}
