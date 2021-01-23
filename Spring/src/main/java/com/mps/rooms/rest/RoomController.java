package com.mps.rooms.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mps.rooms.dto.RoomDto;
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
}
