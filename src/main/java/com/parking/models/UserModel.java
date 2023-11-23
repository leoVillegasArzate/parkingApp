package com.parking.models;

public class UserModel {

	private int idEstacionamiento;
	private int idEmpleado;
	private String nombreEmpleado;
	private String estacionamiento;
	private String userName;
	private String fecha;
	private boolean inUse;
	private String rol;
	public UserModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserModel(int idEstacionamiento, int idEmpleado, String nombreEmpleado, String estacionamiento,
			String userName, boolean inUse, String rol) {
		super();
		this.idEstacionamiento = idEstacionamiento;
		this.idEmpleado = idEmpleado;
		this.nombreEmpleado = nombreEmpleado;
		this.estacionamiento = estacionamiento;
		this.userName = userName;
		this.inUse = inUse;
		this.rol = rol;
	}
	public int getIdEstacionamiento() {
		return idEstacionamiento;
	}
	public void setIdEstacionamiento(int idEstacionamiento) {
		this.idEstacionamiento = idEstacionamiento;
	}
	public int getIdEmpleado() {
		return idEmpleado;
	}
	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	public String getNombreEmpleado() {
		return nombreEmpleado;
	}
	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}
	public String getEstacionamiento() {
		return estacionamiento;
	}
	public void setEstacionamiento(String estacionamiento) {
		this.estacionamiento = estacionamiento;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public boolean isInUse() {
		return inUse;
	}
	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	@Override
	public String toString() {
		return "UserModel [idEstacionamiento=" + idEstacionamiento + ", idEmpleado=" + idEmpleado + ", nombreEmpleado="
				+ nombreEmpleado + ", estacionamiento=" + estacionamiento + ", userName=" + userName + ", fecha="
				+ fecha + ", inUse=" + inUse + ", rol=" + rol + "]";
	}
	
	
	
}
