package com.parking.models;

public class CobroModel {

private double autoChico;
private double autoGrande;
private double hora;
private double fraccion1;
private double fraccion2;
private double fraccion3;
private double fraccion4;
private double horasPromo;
private double importePromo;
private boolean promocion;
private String tipo;
private double cobroBoletoPerdido;



@Override
public String toString() {
	return "CobroModel [autoChico=" + autoChico + ", autoGrande=" + autoGrande + ", hora=" + hora + ", fraccion1="
			+ fraccion1 + ", fraccion2=" + fraccion2 + ", fraccion3=" + fraccion3 + ", fraccion4=" + fraccion4
			+ ", horasPromo=" + horasPromo + ", importePromo=" + importePromo + ", promocion=" + promocion + ", tipo="
			+ tipo + ", cobroBoletoPerdido=" + cobroBoletoPerdido + "]";
}



public double getAutoChico() {
	return autoChico;
}



public void setAutoChico(double autoChico) {
	this.autoChico = autoChico;
}



public double getAutoGrande() {
	return autoGrande;
}



public void setAutoGrande(double autoGrande) {
	this.autoGrande = autoGrande;
}



public double getHora() {
	return hora;
}



public void setHora(double hora) {
	this.hora = hora;
}



public double getFraccion1() {
	return fraccion1;
}



public void setFraccion1(double fraccion1) {
	this.fraccion1 = fraccion1;
}



public double getFraccion2() {
	return fraccion2;
}



public void setFraccion2(double fraccion2) {
	this.fraccion2 = fraccion2;
}



public double getFraccion3() {
	return fraccion3;
}



public void setFraccion3(double fraccion3) {
	this.fraccion3 = fraccion3;
}



public double getFraccion4() {
	return fraccion4;
}



public void setFraccion4(double fraccion4) {
	this.fraccion4 = fraccion4;
}



public double getHorasPromo() {
	return horasPromo;
}



public void setHorasPromo(double horasPromo) {
	this.horasPromo = horasPromo;
}



public double getImportePromo() {
	return importePromo;
}



public void setImportePromo(double importePromo) {
	this.importePromo = importePromo;
}



public boolean isPromocion() {
	return promocion;
}



public void setPromocion(boolean promocion) {
	this.promocion = promocion;
}



public String getTipo() {
	return tipo;
}



public void setTipo(String tipo) {
	this.tipo = tipo;
}



public double getCobroBoletoPerdido() {
	return cobroBoletoPerdido;
}



public void setCobroBoletoPerdido(double cobroBoletoPerdido) {
	this.cobroBoletoPerdido = cobroBoletoPerdido;
}



public CobroModel(double autoChico, double autoGrande, double hora, double fraccion1, double fraccion2,
		double fraccion3, double fraccion4, double horasPromo, double importePromo, boolean promocion, String tipo,
		double cobroBoletoPerdido) {
	super();
	this.autoChico = autoChico;
	this.autoGrande = autoGrande;
	this.hora = hora;
	this.fraccion1 = fraccion1;
	this.fraccion2 = fraccion2;
	this.fraccion3 = fraccion3;
	this.fraccion4 = fraccion4;
	this.horasPromo = horasPromo;
	this.importePromo = importePromo;
	this.promocion = promocion;
	this.tipo = tipo;
	this.cobroBoletoPerdido = cobroBoletoPerdido;
}



public CobroModel() {
	super();
	// TODO Auto-generated constructor stub
}









}
