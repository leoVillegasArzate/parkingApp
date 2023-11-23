package com.parking.utils;

public class ResponseApp  {

	private String status;
	private String mensaje;
	private String object;
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	@Override
	public String toString() {
		return "ReponseApp [status=" + status + ", mensaje=" + mensaje + ", object=" + object + "]";
	}

	
}
