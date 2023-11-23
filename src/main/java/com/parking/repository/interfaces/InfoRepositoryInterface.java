package com.parking.repository.interfaces;

import com.parking.models.CobroModel;
import com.parking.utils.ResponseApp;

public interface InfoRepositoryInterface {
	
	public ResponseApp getInfo(String fecha,int idUsuario,int idEstacionamiento) throws Exception;

	public CobroModel getTarifa(int idEstacionamiento) throws Exception;

}
