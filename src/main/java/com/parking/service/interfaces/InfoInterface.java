package com.parking.service.interfaces;

import com.parking.models.CobroModel;
import com.parking.utils.ResponseApp;

public interface InfoInterface {
	
	public ResponseApp getInfo(String fecha,int idUsuario,int idEstacionamiento);

	public CobroModel getTarifa(int idEstacionamiento) throws Exception;

}
