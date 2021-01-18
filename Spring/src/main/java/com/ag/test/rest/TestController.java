package com.ag.test.rest;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ag.test.beans.StringResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "Test Controller")
public class TestController {

	@ApiOperation("Test Get Method")
	@ApiResponses({ @ApiResponse(code = 200, message = "Successful"),
			@ApiResponse(code = 400, message = "Malformed request"),
			@ApiResponse(code = 500, message = "Internal error") })
	@GetMapping("/api/getAll")
	public ResponseEntity<StringResponse> testGet(Principal principal) {
		StringResponse stringResponse = new StringResponse("Success");
		return ResponseEntity.ok(stringResponse);
	}
}
