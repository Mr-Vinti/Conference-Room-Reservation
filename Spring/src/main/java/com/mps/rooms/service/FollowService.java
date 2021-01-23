package com.mps.rooms.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mps.rooms.domain.Follow;
import com.mps.rooms.domain.FollowPK;
import com.mps.rooms.repository.FollowRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FollowService {
	private FollowRepository followRepository;
	
	public boolean getFollow(Integer roomId, String email) {
		FollowPK id = new FollowPK(roomId, email);
		
		Optional<Follow> follow = followRepository.findById(id);
		return follow.isPresent() ? true : false;
	}
}
