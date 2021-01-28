package com.mps.rooms.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mps.rooms.beans.StringResponse;
import com.mps.rooms.dto.ReservationDto;
import com.mps.rooms.dto.RoomDto;
import com.mps.rooms.service.FollowService;
import com.mps.rooms.service.RoomService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Api(tags = "Rooms Controller")
@RequestMapping("/api/rooms")
@AllArgsConstructor
@Log4j2
public class RoomController {
	private RoomService roomService;
	private FollowService followService;

	@ApiOperation("Get All Rooms For User Method")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successful"),
			@ApiResponse(code = 400, message = "Malformed request"),
			@ApiResponse(code = 500, message = "Internal error") })
	@GetMapping
	public ResponseEntity<List<RoomDto>> getAllRooms(Principal principal) {
		log.info("User {} retrieved all rooms.",
				((Jwt) ((JwtAuthenticationToken) principal).getPrincipal()).getClaimAsString("name"));

		return ResponseEntity.ok(roomService.getRooms());
	}
	
	@ApiOperation("Get Room For User Method")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successful"),
			@ApiResponse(code = 400, message = "Malformed request"),
			@ApiResponse(code = 500, message = "Internal error") })
	@GetMapping("/{room-id}")
	public ResponseEntity<RoomDto> getRoom(@PathVariable(name = "room-id") Integer roomId, Principal principal) {
		log.info("User {} retrieved room with id {}.",
				((Jwt) ((JwtAuthenticationToken) principal).getPrincipal()).getClaimAsString("name"), roomId);

		return ResponseEntity.ok(roomService.getRoom(roomId));
	}
	
	@ApiOperation("Upsert Room Method")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successful"),
			@ApiResponse(code = 400, message = "Malformed request"),
			@ApiResponse(code = 500, message = "Internal error") })
	@PutMapping
	public ResponseEntity<RoomDto> upsertRoom(@RequestParam(value = "id", required = false) Integer roomId,
			@RequestParam(value = "name", required = false) String name, 
			@RequestParam(value = "description", required = false) String description, 
			Principal principal) {
		if (roomId != null) {
			log.info("User {} updated room with id {}.",
					((Jwt) ((JwtAuthenticationToken) principal).getPrincipal()).getClaimAsString("name"), roomId);
		} else {
			log.info("User {} created a new room.",
					((Jwt) ((JwtAuthenticationToken) principal).getPrincipal()).getClaimAsString("name"));
		}

		return ResponseEntity.ok(roomService.upsert(roomId, name, description));
	}
	
	@ApiOperation("Register user to follow room Method")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successful"),
			@ApiResponse(code = 400, message = "Malformed request"),
			@ApiResponse(code = 500, message = "Internal error") })
	@PostMapping("/{room-id}/follow")
	public ResponseEntity<StringResponse> follow(@PathVariable(name = "room-id") Integer roomId, Principal principal) {
		log.info("User {} followed room with id {}.",
				((Jwt) ((JwtAuthenticationToken) principal).getPrincipal()).getClaimAsString("name"), roomId);

		return ResponseEntity.ok(new StringResponse(followService.follow(roomId)));
	}
	
	@ApiOperation("User unfollows room Method")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successful"),
			@ApiResponse(code = 400, message = "Malformed request"),
			@ApiResponse(code = 500, message = "Internal error") })
	@PostMapping("/{room-id}/unfollow")
	public ResponseEntity<StringResponse> unfollow(@PathVariable(name = "room-id") Integer roomId, Principal principal) {
		log.info("User {} followed room with id {}.",
				((Jwt) ((JwtAuthenticationToken) principal).getPrincipal()).getClaimAsString("name"), roomId);

		return ResponseEntity.ok(new StringResponse(followService.unfollow(roomId)));
	}
	
	@ApiOperation("Set room on pending when user starts reservation Method")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successful"),
			@ApiResponse(code = 400, message = "Malformed request"),
			@ApiResponse(code = 500, message = "Internal error") })
	@PostMapping("/{room-id}/pending")
	public ResponseEntity<RoomDto> pending(@PathVariable(name = "room-id") Integer roomId, Principal principal) {
		log.info("User {} opened reservation dialog for room with id {}.",
				((Jwt) ((JwtAuthenticationToken) principal).getPrincipal()).getClaimAsString("name"), roomId);

		return ResponseEntity.ok(roomService.pending(roomId));
	}
	
	@ApiOperation("Set room on occupied Method")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successful"),
			@ApiResponse(code = 400, message = "Malformed request"),
			@ApiResponse(code = 500, message = "Internal error") })
	@PostMapping("/{room-id}/occupy")
	public ResponseEntity<RoomDto> occupy(@PathVariable(name = "room-id") Integer roomId, @RequestBody ReservationDto reservation, Principal principal) {
		log.info("User {} sent call to occupy room with id {}.",
				((Jwt) ((JwtAuthenticationToken) principal).getPrincipal()).getClaimAsString("name"), roomId);

		return ResponseEntity.ok(roomService.occupy(roomId, reservation));
	}
	
	@ApiOperation("Set room on free Method")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successful"),
			@ApiResponse(code = 400, message = "Malformed request"),
			@ApiResponse(code = 500, message = "Internal error") })
	@PostMapping("/{room-id}/free")
	public ResponseEntity<RoomDto> free(@PathVariable(name = "room-id") Integer roomId, Principal principal) {
		log.info("User {} sent call to free room with id {}.",
				((Jwt) ((JwtAuthenticationToken) principal).getPrincipal()).getClaimAsString("name"), roomId);

		return ResponseEntity.ok(roomService.free(roomId));
	}
}
