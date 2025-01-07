package com.nikhil.learn.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nikhil.learn.dto.UserDto;
import com.nikhil.learn.entity.Role;
import com.nikhil.learn.entity.User;
import com.nikhil.learn.exception.PegiableResponse;
import com.nikhil.learn.exception.ResourceNotFoundException;
import com.nikhil.learn.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public UserDto registerUser(UserDto userDto) {
		
		User user = mapper.map(userDto, User.class);
		// we set random id to user
		String id = UUID.randomUUID().toString();
		user.setId(id);
		// we set encoded password to user
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		// at the time of saving user we set user role is by default ROLE_USER
		user.setRole(Role.ROLE_USER);
		
		User saveUser = userRepo.save(user);
		
		return mapper.map(saveUser, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		
		User userById = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usernot found with given id "+userId));
		
		userById.setName(userDto.getName());
		userById.setEmail(userDto.getEmail());
		userById.setPassword(encoder.encode(userDto.getPassword()));
		userById.setRole(userDto.getRole());
		
		User updateUser = userRepo.save(userById);
		
		return mapper.map(updateUser, UserDto.class);
	}

	@Override
	public void deleteUser(String userId) {
		
		User userById = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id "+userId));
		
		userRepo.delete(userById);
		
	}

	@Override
	public UserDto getSingleUser(String userId) {
		
		User userById = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id "+userId));
		
		return mapper.map(userById, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUsers() {
		
		List<User> allUser = userRepo.findAll();
		
		List<UserDto> userList = allUser.stream().map(ex -> mapper.map(ex, UserDto.class)).collect(Collectors.toList());
		
		return userList;
	}

	@Override
	public PegiableResponse getUsersWithPegination(int pageNumber, int pageSize, String sortBy, String sortDir) {
		
		Sort sort = (sortBy.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<User> page = userRepo.findAll(pageable);
		
		List<User> user = page.getContent();
		
		List<UserDto> userDto = user.stream().map(ex -> mapper.map(ex, UserDto.class)).collect(Collectors.toList());
		
		long totalElements = page.getTotalElements();
		int totalPages = page.getTotalPages();
		boolean first = page.isFirst();
		boolean last = page.isLast();
		
		PegiableResponse pegiableResponse = PegiableResponse.builder().content(userDto).totalElements(totalElements)
				.totalPages(totalPages).pageNumber(pageNumber)
				.pageSize(pageSize).isFirst(first).isLast(last)
				.build();
		
		return pegiableResponse;
	}

}
