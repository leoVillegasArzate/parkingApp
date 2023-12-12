drop table if exists auto;
CREATE TABLE auto (
    idauto serial,
    folio integer NOT NULL,
    marca character varying COLLATE pg_catalog."default" NOT NULL,
    color character varying COLLATE pg_catalog."default" NOT NULL,
    placa character varying COLLATE pg_catalog."default" NOT NULL,
    entrada timestamp without time zone NOT NULL,
    salida timestamp without time zone,
    fecha date NOT NULL,
    consumo character varying COLLATE pg_catalog."default",
    consumofracion character varying COLLATE pg_catalog."default",
    cortecaja boolean DEFAULT false,
    modopago character varying,
	status character varying  NOT NULL,
    importepago integer,
	tipoCobro  varchar not null,
	idestacionamiento integer not null,
	 CONSTRAINT auto_id PRIMARY KEY (idauto),
	 FOREIGN KEY(idestacionamiento)
	  REFERENCES estacionamiento(idestacionamiento)
);

drop table if exists estacionamiento;
CREATE TABLE estacionamiento (
	idestacionamiento serial4 NOT NULL,	-- SALTILLO # 19
	direccion varchar NOT NULL ,
	nombre varchar NOT NULL,
	activo BOOLEAN not null,
	CONSTRAINT pk_id PRIMARY KEY (idestacionamiento)
);
drop table if exists empleado;
create table empleado (
	idempleado serial4 not null,
	nombre varchar not null,
	apellidos varchar not null,
	activo boolean not null,
	idusuario int not null,
	pass varchar not null,	
	 CONSTRAINT pk_empleado PRIMARY KEY (idempleado),	
	 FOREIGN KEY(idusuario) 
	 REFERENCES usuario(idusuario)
);
drop table if exists perfil;
create table perfil(
	idperfil serial4 not null,
	activo BOOLEAN not null,  
	rol varchar NOT NULL, --ADMIN / USER
	CONSTRAINT pk PRIMARY KEY (idperfil)	
);

drop table if exists usuario;
create table usuario(
idusuario serial4 not null,
username  varchar not null,--nombre del estaciomamiento  saltillo 
activo BOOLEAN DEFAULT true,
idperfil int not null,
idestacionamiento int not null,
 CONSTRAINT pk_usuario PRIMARY KEY (idusuario),	
 FOREIGN KEY(idestacionamiento) 
 REFERENCES estacionamiento(idestacionamiento),
 FOREIGN KEY(idperfil) 
 REFERENCES perfil(idperfil)
);

drop table if exists cobro;
CREATE TABLE IF NOT EXISTS public.cobro
(
    idcobro SERIAL not null,
    importeautochico double precision NOT NULL,
    importeautogrande double precision NOT NULL,
    importehora double precision NOT NULL,
    fraccion1 double precision NOT NULL,
    fraccion2 double precision NOT NULL,
    freaccion3 double precision NOT NULL,
	fraccion4 double precision NOT NULL,
    idpromocion INTEGER NOT NULL,
	horaspromocion double precision NOT NULL,
	importepromocion double precision NOT NULL,
    idestacionamiento integer NOT NULL,
    activo boolean NOT NULL,
    fechacreacion date NOT NULL,
	importeBoletoperdido double precision NOT NULL,
    CONSTRAINT cobro_pkey PRIMARY KEY (idcobro),
	FOREIGN KEY(idpromocion)
    REFERENCES tipoPromocion(idpromocion),
	FOREIGN KEY(idestacionamiento)
    REFERENCES estacionamiento(idestacionamiento)
);
drop table if exists tipoPromocion;
CREATE TABLE tipoPromocion (
	idpromocion serial4 NOT NULL,	-- SALTILLO # 19
	tienepromo boolean NOT NULL ,
	tipoPromo varchar NOT NULL,
	descripcion varchar NOT NULL,
	horasPromo double precision NOT NULL,
	importeCobro double precision NOT NULL,
	activo BOOLEAN not null,
	CONSTRAINT promocion_pk_id PRIMARY KEY (idpromocion)
);

drop table if exists tipocobroAuto;
CREATE TABLE tipocobroAuto (
	idtipocobroAuto serial4 NOT NULL,	-- SALTILLO # 19
	tipocobroAuto varchar NOT NULL,
	descripcion varchar NOT NULL,
	CONSTRAINT tipocobroAuto_pk_id PRIMARY KEY (idtipocobroAuto)
);

------------------------------------------------------------------------------
create table pensionado(
	idpensionado  serial not null,
	nombre varchar,
	fecharegistro date not null,
	telefono integer not null,
	correo varchar not null,
	calle varchar not null,
	colonia varchar not null,
	delegacion varchar not null,
	marcaauto varchar not null,
	placas varchar ,
	color varchar not null,
	modelo int not null,
	importe numeric(10,2) not null,
	idestacionamiento integer not null,
	activo boolean  default 'true' not null,
	rfc varchar not null,
	
	CONSTRAINT pensionado_key primary key (idpensionado),
	foreign key (idestacionamiento)references estacionamiento (idestacionamiento)
	

);
-----------------------------------------------------------------------------------

create table pagoPension (
	idpagopension serial not null,
	idpensionado integer not null,
	fechapago date not null,
	mescorrespondiente date not null,
	importepago numeric(10,2) not null,
	adeudo numeric(10,2) not null,
	CONSTRAINT pago_pension primary key (idpagopension),
	foreign key (idpensionado)references pensionado (idpensionado)
)



-----------------------------------------------------------------------------

------------------------------------
------------------------------------

	CREATE OR REPLACE FUNCTION public.getinfo(	in_fecha character varying,in_idestacionamiento integer)
    RETURNS TABLE(rs_folio integer, rs_entrada integer, rs_pendientes integer, rs_salidas integer) 
AS $save$
declare 
 sp_folio integer;
 sp_entrada integer;
 sp_pendientes integer;
 sp_salidas integer;
BEGIN
      IF (in_fecha is null)then raise exception'Es necesario definir una fecha'; end if;
	  
     SELECT COUNT(*)+1 as folio into sp_folio FROM auto   WHERE idestacionamiento = in_idestacionamiento;
	 select count(*) as entrada into sp_entrada from auto  WHERE idestacionamiento = in_idestacionamiento and to_char (entrada,'YYYY-MM-dd')=in_fecha;
	 select count(*) as pendientes into sp_pendientes from auto  WHERE idestacionamiento = in_idestacionamiento and status='PENDIENTE' and cortecaja is false; 
	 select count(*) as salientes into sp_salidas from auto WHERE idestacionamiento = in_idestacionamiento and status='PAGADO' and cortecaja is false and to_char (salida,'YYYY-MM-dd')=in_fecha;
	 
    
    RETURN QUERY SELECT sp_folio, sp_entrada, sp_pendientes, sp_salidas;
	
	EXCEPTION WHEN others THEN
	RAISE EXCEPTION '%|getinfoParking|%',SQLERRM,SQLSTATE;
END;
$save$ LANGUAGE plpgsql;


1	1	"OPERADOR"
2	1	"ADMINISTRADOR"
CREATE OR REPLACE FUNCTION saveEstacionamiento (json_data varchar)
 RETURNS TABLE(rs_estatus boolean) 
AS $save$
declare
		sp_json json;
	    sp_nombre text;
        sp_direccion text;
		sp_hora integer;sp_tipoCobro  text;
		sp_f1 numeric(10,2);sp_f2 numeric(10,2);sp_f3 numeric(10,2);sp_f4 numeric(10,2);
		sp_horaPromo integer;sp_importePromo numeric(10,2); sp_boletoperdido numeric(10,2);
		sp_pensrionChico numeric(10,2);sp_pensionGrande numeric(10,2);
		sp_usuario text; 
		sp_idEstacionamiento integer; sp_id integer; sp_aux text;
		sp_idcobro integer; sp_iduser integer;
BEGIN
        sp_json := json_data::json;
        sp_nombre := sp_json->>'nombre';
        sp_direccion := sp_json->>'direccion';
		sp_hora := sp_json->>'hora';
        sp_tipoCobro := sp_json->>'tipoCobro';
		sp_f1 := sp_json->>'fraccion1';
        sp_f2 := sp_json->>'fraccion2';
		sp_f3 := sp_json->>'fraccion3';
        sp_f4 := sp_json->>'fraccion4';
		sp_horaPromo := sp_json->>'horasPromocion';
        sp_importePromo := sp_json->>'importePromocion';
		sp_pensrionChico := sp_json->>'pensionAutoChico';
        sp_pensionGrande := sp_json->>'pensionAutoGrande';
		sp_usuario := sp_json->>'usuario';
		sp_boletoperdido := sp_json->>'boletoPerdido';
		
		sp_idEstacionamiento:=0;
		raise notice '%',sp_json;
		raise notice 'vamos a guardar estacionamiento';
		
		if exists (select * from estacionamiento where direccion= REPLACE(sp_direccion, ' ', '') and nombre=sp_nombre and activo is true)
			then   RAISE EXCEPTION  'Ya existe un registro con los mismos datos [%]',sp_nombre; end if;
			
		INSERT INTO public.estacionamiento(	 direccion, nombre, activo)
	    VALUES (REPLACE(sp_direccion, ' ', ''), sp_nombre, true) returning idestacionamiento into sp_idEstacionamiento;

		if(sp_idEstacionamiento is null or sp_idEstacionamiento<1) then  RAISE EXCEPTION  'NO SE LOGRO GUARDAR EL ESTACIONAMIENTO'; end if;
		
		raise notice 'El id del estacionamiento [%]',sp_idEstacionamiento;
		

		sp_id:=0;
		if(sp_tipoCobro='a')then select idpromocion into sp_id  from tipopromocion where tipopromo='AUTOMATICO'; end if;
		if(sp_tipoCobro='h')then select idpromocion into sp_id  from tipopromocion where  tipopromo='HORA'; end if;
		if(sp_tipoCobro='h*p')then select idpromocion into sp_id  from tipopromocion where tipopromo='HORA*PROMOCION';  end if;

  	    raise notice 'Tipo Promocion id[%] ',sp_id;
		
		INSERT INTO public.cobro(importeautochico, importeautogrande, importehora, fraccion1, fraccion2, freaccion3,
		 fraccion4, importepromo, idpromocion, idestacionamiento, activo, fechacreacion, horaspromo, 
		 importeboletoperdido)
		VALUES (sp_pensrionChico, sp_pensrionChico, sp_hora, sp_f1, sp_f2, sp_f3, sp_f4, sp_importePromo, sp_id,
		 sp_idEstacionamiento, true, now(), sp_horaPromo, sp_boletoperdido) returning idcobro into sp_idcobro;

		if(sp_idcobro is null or sp_idcobro<1) then  RAISE EXCEPTION  'NO SE LOGRO GUARDAR EL ESTSISTEMA DE COBRO'; end if;
		
		 raise notice 'EL id de cobro [%]',sp_idcobro;
		 
		sp_id:=0;
		select idperfil into sp_id  from perfil where rol ='OPERADOR' and activo is true;
		if(sp_id is null or sp_id<1) then  RAISE EXCEPTION  'NO SE ENCONTRO EL PERFIL PARA RELACIONAR AL USUARIO QUE DESEA INGRESAR'; end if;
	      
		  raise notice 'PErfil [%]',sp_id;
	    if exists (select * from usuario where username=sp_usuario and activo is true)
			then   RAISE EXCEPTION  'Ya existe un usuario con los mismos datos [%]',sp_usuario; end if;
			
		INSERT INTO public.usuario(	username, activo, idperfil, idestacionamiento, asignado)
	    VALUES ( sp_usuario, true, sp_id, sp_idEstacionamiento, false) returning idusuario into sp_iduser;

		if(sp_id is null or sp_id<1) then  RAISE EXCEPTION  'NO SE LOGRO REGISTRAR EL USARIO '; end if;
		raise notice 'EL id de usuario [%]',sp_iduser;
		RETURN query select true;
        EXCEPTION WHEN others THEN
	    RAISE EXCEPTION '%|saveParking|%',SQLERRM,SQLSTATE;
     
END;
$save$ LANGUAGE plpgsql;
------------------------------------------------------
1	"HORA"	"COBRO POR HORA"
2	"PROMOCION"	"COBRO POR PROMOCION"
3	"AUTOMATICO"	"COBRO DE HORA Y PROMOCION AUTOMATICA"

---------------------------------------------------------------

1	"HORA"	"el cobro solo sera por hora"	true
2	"AUTOMATICO"	"se expide solo un boleto pero aplica la promocion y cobro por hora  automaticamente"	true
3	"H*P"	"se cobra hora y promocion por separado"	true

--------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION saveEmpleadoAdmin (json_data varchar)
 RETURNS TABLE(rs_estatus boolean) 
AS $save$
declare
		sp_json json;
	    sp_nombre text;
        sp_apellidos text;
		sp_usuario text;
		sp_pass text;
		sp_rol text;
		sp_estacionamiento text;
		sp_id_perfil integer ;
		sp_id_estacionamiento integer ;
		sp_id_usuario integer ;
BEGIN
        sp_json := json_data::json;
        sp_nombre := sp_json->>'nombre';
        sp_apellidos := sp_json->>'apellidos';
		sp_usuario := sp_json->>'usuario';
        sp_pass := sp_json->>'pass';
		sp_rol := sp_json->>'rol';
        sp_estacionamiento := sp_json->>'estacionamiento';
		
		raise notice '%',sp_json;
		
		 if exists (select * from usuario where username=sp_usuario and activo is true)
			then   RAISE EXCEPTION  'Ya existe un usuario con los mismos datos [%]',sp_usuario; end if;

		select idperfil into sp_id_perfil  from perfil where rol ='ADMINISTRADOR' and activo is true;

		if(sp_id_perfil is null or sp_id_perfil<1) then  RAISE EXCEPTION  'NO SE ENCONTRO EL PERFIL PARA RELACIONAR AL USUARIO QUE DESEA INGRESAR'; end if;
		raise notice 'Perfil [%]',sp_id_perfil;

		if not exists (select * from estacionamiento where nombre=sp_estacionamiento and activo is true) then RAISE EXCEPTION  'NO SE ENCONTRO  ESTACIONAMIENTO MATRIZ'; end if;	

		select idestacionamiento into sp_id_estacionamiento from estacionamiento where nombre=sp_estacionamiento and activo is true;
		raise notice 'estacionamiento id [%]',sp_id_estacionamiento;

		INSERT INTO public.usuario(	username, activo, idperfil, idestacionamiento, asignado)
	    VALUES ( sp_usuario, true, sp_id_perfil, sp_id_estacionamiento, true) returning idusuario into sp_id_usuario;
		
		if(sp_id_usuario is null or sp_id_usuario<1) then  RAISE EXCEPTION  'NO SE LOGRO REGISTRAR EL USARIO '; end if;
		raise notice 'EL id de usuario [%]',sp_id_usuario;

		INSERT INTO public.empleado(nombre, apellidos, activo, idusuario, pass)VALUES (sp_nombre, sp_apellidos, true, sp_id_usuario, sp_pass);

		RETURN query select true;
        EXCEPTION WHEN others THEN
	    RAISE EXCEPTION '%|EmpleadoAdmin|%',SQLERRM,SQLSTATE;
     
END;
$save$ LANGUAGE plpgsql;


--------------------------------------------------------UPDATE COBRO---------------------------------------------------------

DECLARE
   sp_json json;
	    sp_idEstacionamiento integer;
		sp_hora integer;sp_tipoCobro  text;
		sp_f1 numeric(10,2);sp_f2 numeric(10,2);sp_f3 numeric(10,2);sp_f4 numeric(10,2);
		sp_horaPromo integer;sp_importePromo numeric(10,2); sp_boletoperdido numeric(10,2);
		sp_pensrionChico numeric(10,2);sp_pensionGrande numeric(10,2);
        sp_boolean boolean;sp_idTipopromo integer;
BEGIN
        sp_json := json_data::json;
		sp_hora := sp_json->>'hora';
		sp_idEstacionamiento := sp_json->>'idEstacionamiento';
        sp_tipoCobro := sp_json->>'tipoCobro';
		sp_f1 := sp_json->>'fraccion1';
        sp_f2 := sp_json->>'fraccion2';
		sp_f3 := sp_json->>'fraccion3';
        sp_f4 := sp_json->>'fraccion4';
		sp_horaPromo := sp_json->>'horasPromocion';
        sp_importePromo := sp_json->>'importePromocion';
		sp_pensrionChico := sp_json->>'pensionAutoChico';
        sp_pensionGrande := sp_json->>'pensionAutoGrande';
		sp_boletoperdido := sp_json->>'boletoPerdido';
		
		sp_idTipopromo:=0;
		if(sp_tipoCobro='a')then select idpromocion into sp_idTipopromo  from tipopromocion where tipopromo='AUTOMATICO'; end if;
		if(sp_tipoCobro='h')then select idpromocion into sp_idTipopromo  from tipopromocion where  tipopromo='HORA'; end if;
		if(sp_tipoCobro='h*p')then select idpromocion into sp_idTipopromo  from tipopromocion where tipopromo='HORA*PROMOCION';  end if;
		
		if (sp_idTipopromo<1)then raise exception'No se logro recuperar id del tipo de promocion'; end if;

        UPDATE cobro 
        SET  importeautochico=sp_pensrionChico, importeautogrande=sp_pensionGrande, importehora=sp_hora, fraccion1=sp_f1, fraccion2=sp_f2,
        freaccion3=sp_f3, fraccion4=sp_f4, idpromocion=sp_idTipopromo, horaspromo=sp_horaPromo, importepromo=sp_importePromo, 
        importeboletoperdido=sp_boletoperdido
        WHERE idestacionamiento=sp_idEstacionamiento;

        if not found then raise exception'No se logro realizar la actualizacion '; end if;
         -- Retorna el resultado
         RETURN true;
        EXCEPTION WHEN others THEN
	    RAISE EXCEPTION '%|Update_cobro|%',SQLERRM,SQLSTATE;
END;

----------------------------------------ELIMINAR ESTACIONAMIENTO---------------------------------------------------------------------

DECLARE
     sp_usuarioid integer;
BEGIN
       update cobro set activo = false where idestacionamiento=id;
	   
	   if not found then raise exception 'No se logro eliminar el cobro para [%]',id; end if;
		
	   select idusuario into sp_usuarioid  from usuario where idestacionamiento=id;
	   
	   if (sp_usuarioid is null or sp_usuarioid<1)then raise exception 'No se logro obtener el usuario para [%]',id; end if;
	   
	   raise notice 'id usuario recuperado [%]',sp_usuarioid;
	   
	   if exists (select * from empleado where idusuario = sp_usuarioid)
	      then 
		   update empleado set activo= false where idusuario = sp_usuarioid;
		   
		   if not found then raise exception 'No se logro eliminar el empleado para [%]',sp_usuarioid; end if;
	   end if;	  
	   
	   update usuario set activo= false where idestacionamiento=id;
	   
	   if not found then raise exception 'No se logro eliminar el usuario para [%]',id; end if;
	   
	   update estacionamiento set activo = false where idestacionamiento=id;
	   
	   if not found then raise exception 'No se logro eliminar el estacionamiento para [%]',id; end if;
         -- Retorna el resultado
         RETURN true;
        EXCEPTION WHEN others THEN
	    RAISE EXCEPTION '%|eliminar_estacionamiento|%',SQLERRM,SQLSTATE;
END;



----------------------------------------ELIMINAR EMPLEADO --------------------------------------------------------------------

DECLARE
     sp_usuarioid integer;
BEGIN
       update cobro set activo = false where idestacionamiento=id;
	   
	   if not found then raise exception 'No se logro eliminar el cobro para [%]',id; end if;
		
	   select idusuario into sp_usuarioid  from usuario where idestacionamiento=id;
	   
	   if (sp_usuarioid is null or sp_usuarioid<1)then raise exception 'No se logro obtener el usuario para [%]',id; end if;
	   
	   raise notice 'id usuario recuperado [%]',sp_usuarioid;
	   
	   if exists (select * from empleado where idusuario = sp_usuarioid)
	      then 
		   update empleado set activo= false where idusuario = sp_usuarioid;
		   
		   if not found then raise exception 'No se logro eliminar el empleado para [%]',sp_usuarioid; end if;
	   end if;	  
	   
	   update usuario set activo= false where idestacionamiento=id;
	   
	   if not found then raise exception 'No se logro eliminar el usuario para [%]',id; end if;
	   
	   update estacionamiento set activo = false where idestacionamiento=id;
	   
	   if not found then raise exception 'No se logro eliminar el estacionamiento para [%]',id; end if;
         -- Retorna el resultado
         RETURN true;
        EXCEPTION WHEN others THEN
	    RAISE EXCEPTION '%|eliminar_estacionamiento|%',SQLERRM,SQLSTATE;
END;

-----------------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION eliminar_empleado (id integer)
 RETURNS TABLE(rs_estatus boolean) 
AS $save$
DECLARE
     
BEGIN
       update empleado set activo = false where idusuario =id;
	   
	   if not found then raise exception 'No se logro eliminar el empleado para [%]',id; end if;
	   
	    if((select p.rol from usuario u inner join perfil p using(idperfil) where u.idusuario=id)='ADMINISTRADOR')
		  then 
		     update usuario set asignado= false ,activo=false where idusuario=id;
			  if not found then raise exception 'No se logro eliminar usuario el usuario para [%]',id; end if;
		  else
		   	update usuario set asignado= false where idusuario=id;
			 if not found then raise exception 'No se logro liberar usuario el usuario para [%]',id; end if;
		  end if;
	  
       RETURN query select true;
        EXCEPTION WHEN others THEN
	    RAISE EXCEPTION '%|eliminar_empleado|%',SQLERRM,SQLSTATE;
END;
$save$ LANGUAGE plpgsql;












