package com.parking.repository.interfaces;

import com.google.gson.JsonObject;
import com.parking.models.UserModel;

public interface UserAuthRepositoryInterface {
	
	public UserModel getAuthentication (String username) throws Exception;


	
}
