package com.nikhil.learn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nikhil.learn.dto.UserDto;
import com.nikhil.learn.exception.ApiMessageResponse;
import com.nikhil.learn.exception.PegiableResponse;
import com.nikhil.learn.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/registerUser")
	public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
		
		UserDto registerUser = userService.registerUser(userDto);
		
		return new ResponseEntity<>(registerUser, HttpStatus.CREATED);
		
	}
	
	@PutMapping("/updateUser/{userId}")
	public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable String userId){
		
		UserDto updateUser = userService.updateUser(userDto, userId);
		
		return new ResponseEntity<>(updateUser, HttpStatus.OK);
		
	}
	
	@DeleteMapping("deleteUser/{userId}")
	public ResponseEntity<ApiMessageResponse> deleteUser(@PathVariable String userId){
		
		userService.deleteUser(userId);
		
		ApiMessageResponse apiMessageResponse = ApiMessageResponse.builder().message("User deleted successfully !!").success(true).status(HttpStatus.OK).build();
		
		return new ResponseEntity<>(apiMessageResponse, HttpStatus.OK);
		
	}
	
	@GetMapping("/userById/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
		
		UserDto userById = userService.getSingleUser(userId);
		
		return new ResponseEntity<>(userById, HttpStatus.OK);
		
	}
	
	@GetMapping("/allUser")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		
		List<UserDto> allUsers = userService.getAllUsers();
		
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
		
	}
	
	@GetMapping("/getUserByPegination")
	public ResponseEntity<PegiableResponse> getUserWithPegination(
			@RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "3", required = false) int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
			){
		
		PegiableResponse usersWithPegination = userService.getUsersWithPegination(pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<>(usersWithPegination, HttpStatus.OK);
		
	}

}
