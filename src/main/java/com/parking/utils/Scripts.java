package com.parking.utils;

public class Scripts {
 public static final String GUARDA_AUTO ="INSERT INTO auto(folio,marca, color, placa,  entrada,fecha,cortecaja ,status, idestacionamiento,tipocobro) "
	 		+ "VALUES (?, ?, ?, ?, TO_TIMESTAMP(?, 'YYYY-MM-dd HH24:MI:SS'),now(),?, ?, ?,?);";
		 
		 
 public static final String SEARCH_FOLIO="SELECT folio,marca, color, placa, to_char (entrada, 'YYYY-MM-dd HH24:MI:SS') as entrada   ,status, idestacionamiento,tipocobro,modopago"
 											+ "  FROM auto  where folio=?  and idestacionamiento=?;";
 
 //public static final String GET_ESTACIONAMIENTO="select * from estacionamiento e where e.activo=1 and e.id<>2;";
 public static final String SAVE_COBRO="INSERT INTO public.cobro(importe_auto_chico, importe_auto_grande, importe_hora, fraccion1, fraccion2, freaccion3,fraccion4, importe_promocion, horas_promo, tipo_promocion,id_estacionamiento, activo, \"tienePromo\",fechacreacion) "
							 		+ "	VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW() );";
 
 public static final String EXISTE_COBRO="SELECT * FROM cobro WHERE id_estacionamiento=? and activo='t'";
 
 public static final String GET_AUTHENTICATION="select es.idestacionamiento,u.idusuario ,e.nombre||e.apellidos as nombreEmpleado ,es.direccion as estacionamiento,u.asignado,\r\n"
										 		+ "u.username as username,pe.rol\r\n"
										 		+ "from empleado e \r\n"
										 		+ "join usuario u    using (idusuario) \r\n"
										 		+ "join perfil pe using(idperfil)\r\n"
										 		+ "join estacionamiento es  using (idestacionamiento) \r\n"
										 		+ " where u.activo is true and e.activo is true and es.activo is true and u.asignado is true and u.username=? ;";
										 
 public static final String GET_INFO="select * from getInfo(?,?)";
 
 public static final String GET_COBRO="select co.importeautochico ,co.importeautogrande,co.importehora,co.fraccion1,co.fraccion2,co.freaccion3,co.fraccion4,co.horaspromo,co.importepromo,\r\n"
								 		+ "tp.tipopromo ,importeboletoperdido "
								 		+ "from cobro co\r\n"
								 		+ "join tipopromocion tp using (idpromocion)\r\n"
								 		+ "join estacionamiento es using(idestacionamiento)\r\n"
								 		+ "where es.idestacionamiento=? and es.activo is true and co.activo is true";
 
 public static final String GUARDA_PAGO_AUTO ="update auto set salida=TO_TIMESTAMP(?, 'YYYY-MM-dd HH24:MI:SS') , consumo=? ,consumofracion=?,status=?,importepago=? ,modopago=?"
 									+ "where folio= ? and idestacionamiento=?";
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public static final String LIST_ESTACIONAMIENTOS="select es.idestacionamiento, es.nombre,es.direccion,co.importehora,co.importeAutoChico,co.importeAutogrande,tp.tipopromo\r\n"
											 		+ "from estacionamiento es\r\n"
											 		+ "join cobro co  using (idestacionamiento)\r\n"
											 		+ "join tipopromocion tp using(idpromocion)\r\n"
											 		+ "where nombre!='MATRIZ' and es.activo is true and co.activo is true order by direccion asc";		
 
public static final String LIST_EMPLEADOS="select  e.apellidos||' '|| e.nombre  as nombreEmpleado ,es.direccion as estacionamiento,asignado as ocupado ,u.username as username \r\n "
										+ ",pe.rol as perfil, case when e.activo is true then 'ACTIVO'  end as estatus , u.idusuario "
										+ " from empleado e \r\n"
										+ " join usuario u using(idusuario)\r\n"
										+ " join perfil pe using(idperfil)\r\n"
										+ " join estacionamiento es using (idestacionamiento)\r\n"
										+ " where u.activo is true and e.activo is true and es.activo is true  and u.asignado is true order by e.apellidos asc";

public static final String SAVE_ESTACIONAMIENTO="select * from saveEstacionamiento ( ?)";
public static final String SAVE_EMPLEADO_ADMIN="select * from saveEmpleadoAdmin ( ?)";

public static final String GET_ROLES="select * from perfil where activo is true ";

public static final String GET_USUARIOS="select u.idusuario as idusuario, u.username as usuario ,e.direccion as estacionamiento ,p.rol as rol\r\n"
										+ "from usuario u\r\n"
										+ "join perfil p using(idperfil)\r\n"
										+ "join estacionamiento e using(idestacionamiento)\r\n"
										+ "where u.asignado is false and u.activo is true and e.activo is true and p.rol<>'ADMINISTARDOR'";	

public static final String SAVE_EMPLEADO="INSERT INTO empleado(nombre, apellidos, activo, idusuario, pass)	VALUES ( ?, ?, true, ?, ?);";
public static final String GET_ESTACIONAMIENTO_ID="select es.idestacionamiento,es.nombre,es.direccion,co.importehora,co.importeboletoperdido,co.fraccion1,\r\n"
											+ "		co.fraccion2,co.freaccion3 as fraccion3,co.fraccion4,co.horaspromo,co.importepromo,co.importeautochico,co.importeautogrande,\r\n"
											+ "		u.username,tp.tipopromo\r\n"
											+ "from estacionamiento es \r\n"
											+ "inner join cobro co using(idestacionamiento)\r\n"
											+ "inner join usuario u using(idestacionamiento)\r\n"
											+ "inner join tipopromocion tp using(idpromocion)where es.idestacionamiento=? and es.activo is true;";

public static final String UPDATE_ESTACIONAMIENTO="select * from update_cobro ( ?)";
public static final String ELIMINAR_ESTACIONAMIENTO="select * from eliminar_estacionamiento ( ?)";
public static final String ELIMINAR_EMPLEADO="select * from eliminar_empleado ( ?)";
public static final String GET_LISTA_AUTOS="select folio,marca,color,placa,to_char (entrada, 'YYYY-MM-dd HH24:MI:SS') as entrada ,idestacionamiento from auto where status='PENDIENTE' and idestacionamiento=?";
public static final String  GET_PRECIO_PENSION="select idcobro,importeautochico,importeautogrande from cobro where activo is true and idestacionamiento=?"; 	
public static final String SAVE_PENSIONADO="INSERT INTO pensionado(nombre, fecharegistro, telefono, correo, calle, colonia, delegacion, marcaauto, placas, color, modelo, importe, idestacionamiento)\r\n"
										+ "	VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
}

