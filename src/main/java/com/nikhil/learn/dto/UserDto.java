package com.nikhil.learn.dto;

import com.nikhil.learn.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDto {
	
	private String id;

	private String name;
	
	private String email;
	
	private String password;
	
	private Role role;

}
