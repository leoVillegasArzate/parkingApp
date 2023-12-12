package com.parking.utils;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class Constantes {
  public static final DateTimeFormatter FORMATO_FECHA_HORA= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  public static final DateTimeFormatter FECHA= DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public static final SimpleDateFormat FECHA_HORA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Define el formato que desees
  
  public static final SimpleDateFormat FORMATO_FECHA=  new SimpleDateFormat("yyyy-MM-dd");
  public static final String PENDIENTE ="PENDIENTE";
  public static final String COBRADO ="COBRADO";
  
  public static final String ERROR ="false";
  public static final String SUCCESS ="true";
  
  
}
