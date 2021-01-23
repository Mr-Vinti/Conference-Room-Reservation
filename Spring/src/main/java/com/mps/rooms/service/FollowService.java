package com.mps.rooms.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mps.rooms.domain.Follow;
import com.mps.rooms.domain.FollowPK;
import com.mps.rooms.repository.FollowRepository;
import com.mps.rooms.util.Utils;

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

	public String follow(Integer roomId) {
		String userEmail = Utils.getUserEmail();
		FollowPK id = new FollowPK(roomId, userEmail);
		Optional<Follow> followOpt = followRepository.findById(id);
		if (followOpt.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user already follows this room!");
		}

		Follow follow = new Follow(id);
		followRepository.save(follow);

		return "Success";
	}
	
	public String unfollow(Integer roomId) {
		String userEmail = Utils.getUserEmail();
		FollowPK id = new FollowPK(roomId, userEmail);
		Optional<Follow> followOpt = followRepository.findById(id);
		if (followOpt.isPresent()) {
			followRepository.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user doesn't follow this room!");
		}

		return "Success";
	}
}
