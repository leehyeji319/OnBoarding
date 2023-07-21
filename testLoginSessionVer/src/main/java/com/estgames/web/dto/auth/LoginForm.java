package com.estgames.web.dto.auth;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginForm {

	@NotBlank //validation
	private String loginId;

	@NotBlank
	private String password;

}
