package com.estgames.web.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummary {
	private long id;
	private String email;
	private String name;
	private int cash;
}
