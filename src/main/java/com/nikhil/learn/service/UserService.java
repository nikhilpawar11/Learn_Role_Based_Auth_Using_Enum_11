package com.nikhil.learn.service;

import java.util.List;

import com.nikhil.learn.dto.UserDto;
import com.nikhil.learn.exception.PegiableResponse;

public interface UserService {
	
	public UserDto registerUser(UserDto userDto);
	
	public UserDto updateUser(UserDto userDto, String userId);
	
	public void deleteUser(String userId);
	
	public UserDto getSingleUser(String userId);
	
	public List<UserDto> getAllUsers();
	
	public PegiableResponse getUsersWithPegination(int pageNumber, int pageSize, String sortBy, String sortDir);

}
