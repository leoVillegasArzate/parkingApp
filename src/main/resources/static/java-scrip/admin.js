var dataTableEstacionamientos;
var dataTableEmpleados;
var idEstacionamiento=0;
$(document).ready(function() {
	$("#sidebarToggle").click(); // Simula un clic en el botón

	$("#text").hide();
	$("#error").hide();
	$("#verEmpleados").hide();
	$("#verEstacionamiento").show();
	$('#formEstacionamiento').hide();
	$('#formEmpleado').hide();
	$('#formEmpleadoAdmin').hide();


	dataTableEstacionamientos = $('#tableEstacionamiento').DataTable({
		bFilter: true,
		bSort: false,
		destroy: true,
		"language": {
			"zeroRecords": "Sin registros",
			"info": "P&aacute;gina _PAGE_ de _PAGES_",
			"infoEmpty": "No existen registros en base de datos",
			"infoFiltered": "(filtrado de un total de _MAX_)",
			"paginate": {
				"previous": "Anterior",
				"next": "Siguiente",
				"last": "&Uacute;ltima p&aacute;gina",
				"first": "Primera p&aacute;gina"
			}
		}
	});
	dataTableEmpleados = $('#tableEmpleados').DataTable({
		bFilter: true,
		bSort: false,
		destroy: true,
		"language": {
			"zeroRecords": "Sin registros",
			"info": "P&aacute;gina _PAGE_ de _PAGES_",
			"infoEmpty": "No existen registros en base de datos",
			"infoFiltered": "(filtrado de un total de _MAX_)",
			"paginate": {
				"previous": "Anterior",
				"next": "Siguiente",
				"last": "&Uacute;ltima p&aacute;gina",
				"first": "Primera p&aacute;gina"
			}
		}
	});
	ltsEstacionmiento();
	ltsRoles();
	//////////////////////////////////////////////////
	// Agrega un evento clic para los botones "Ver" y "Eliminar"
	dataTableEstacionamientos.on('click', '.ver', function() {
		var id = $(this).data('id');
		getEstacionamientoId(id);
	});
	dataTableEstacionamientos.on('click', '.eliminar', function() {
		var id = $(this).data('id');
	
		elimiarEstacionamientoId(id);
	});


	dataTableEmpleados.on('click', '.eliminar', function() {
		var id = $(this).data('id');
	     alert(id);
	     eliminarEmpleado(id);
		
	});

	////////////////////ver tipo de tabla a mostrar ////////////////////////
	$('#ver').on('change', function(event) {
		var selected = this.value;
		$('#texto').text('');
		$('#texto').hide();
		var roles = $('#roles option:selected');
		// Elimina la opción seleccionada
		roles.prop('selected', false);

		$('form input').val(''); // Limpiar campos del Formulario 2
		$('form select').val(''); // Limpiar campo <select> del Formulario 2

		if (selected == 'estacionamiento') {

			ltsEstacionmiento();
			$("#verEmpleados").hide();
			$('#formEstacionamiento').hide();
			$('#formEmpleado').hide();
			$('#formEmpleadoAdmin').hide();
			$("#verEstacionamiento").show();

		} else {

			$('#texto').show();
			$('#texto').text('solo se puede eliminar empleados,con esta opcion el usuario queda sin uso y se lo puede agregar a otro empleado , para el  administardor elimine y cree otro ');

			ltsEmpleados();
			$('#formEstacionamiento').hide();
			$('#formEmpleado').hide();
			$('#formEmpleadoAdmin').hide();
			$("#verEstacionamiento").hide();
			$("#verEmpleados").show()

		}
	});
	///////////////////ver el tipo de form de empleado a mostrar//////////////////////////
	$('#roles').on('change', function(event) {
		$('#texto').text('');
		$('#texto').hide();
		$('.mensaje-error').remove();
		var selected = this.value;

		var ver = $('#ver option:selected');
		// Elimina la opción seleccionada
		ver.prop('selected', false);
		if (selected == 'OPERADOR') {

			$("#verEmpleados").hide();
			$("#verEstacionamiento").hide();
			$('#formEstacionamiento').hide();
			$('#formEmpleado').show();
			$('#formEstacionamiento input').val(''); // Limpiar campos del Formulario 2
			$('#formEstacionamiento select').val(''); // Limpiar campo <select> del Formulario 2
			$('#formEmpleadoAdmin input').val(''); // Limpiar campos del Formulario 2
			$('#formEmpleadoAdmin select').val(''); // Limpiar campo <select> del Formulario 2
			$('#formEmpleadoAdmin').hide();
			ltsUsuarios();
		} else
			if (selected == 'ADMINISTRADOR') {

				$("#verEmpleados").hide();
				$("#verEstacionamiento").hide();
				$('#formEstacionamiento').hide();
				$('#formEmpleado').hide();
				$('#formEmpleado input').val(''); // Limpiar campos del Formulario 2
				$('#formEmpleado select').val(''); // Limpiar campo <select> del Formulario 2
				$('#formEmpleadoAdmin').show();
				$('#rolA').val('ADMINISTRADOR');
				$('#estacionamientoA').val('MATRIZ');

			}
	});

	//////////////////////////////habilita el from de estacionamiento///////////////////////////////////////////////////////
	$('#newEstacionamiento').click(function() {
		$("#registraEstacionamiento :input").prop("disabled", null);
		idEstacionamiento=0;
		$('#formEstacionamiento input').val(''); // Limpiar campos del Formulario 2
		$('#formEstacionamiento select').val(''); // Limpiar campo <select> del Formulario 2
		$('#texto').text('');
		$('#texto').hide();
		var ver = $('#ver option:selected');
		// Elimina la opción seleccionada
		ver.prop('selected', false);
		var roles = $('#roles option:selected');
		// Elimina la opción seleccionada
		roles.prop('selected', false);		
		 $("#btn_submit").show();
		 $("#editarEstacionamiento").hide();

		$("#verEmpleados").hide();
		$("#verEstacionamiento").hide();
		$('#formEmpleado').hide();
		$('#formEmpleadoAdmin').hide();
		$('#formEstacionamiento').show();

		$('#formEmpleado input').val(''); // Limpiar campos del Formulario 2
		$('#formEmpleado select').val(''); // Limpiar campo <select> del Formulario 2

	});


	$('#tipoCobro').on('change', function(envet) {
		var selected = this.value;

		$('#texto').show('');
		$('#texto').text('');

		$('#horasPromocion').next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
		$('#importePromocion').next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
		if (selected == 'h') {
			$('#texto').css('color', 'black');
			$('#texto').text('El Cobro solo sera por Hora');
			$("#horasPromocion").val(0);
			$("#horasPromocion").prop("disabled", true);
			$("#importePromocion").val(0);
			$("#importePromocion").prop("disabled", true);
			$('#btn_submit').show();
		} else {
			if (selected == '0') {
				$('#texto').text('Seleccione una opcion valida');
				$('#texto').css('color', 'red');
				$('#btn_submit').hide();
			} else {
				if (selected == 'a') {
					$('#texto').css('color', 'black');
					$('#texto').text('El cobro sera Automatico entre Hora y Promocion ');
				}
				if (selected == 'h*p') {
					$('#texto').css('color', 'black');
					$('#texto').text('El cobro sera independiente entre Hora y Promocion ');
				}
				$("#horasPromocion").prop("disabled", null);
				$("#importePromocion").prop("disabled", null);
				$('#btn_submit').show();
			}
		}
	});

	$('.numero').on('keyup', function() {
		var idDelElemento = $(this).attr("id");
		var input = $('#' + idDelElemento).val();
		$('#textoFraciones').text('');

		if (!isDouble(input)) {
			$('#' + idDelElemento).next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#' + idDelElemento).after('<span class="mensaje-error" style="color: red;">No es numero / Acepta numero con 2 decimales</span>');
			$('#' + idDelElemento).css('border', '1px solid red');
			$('#btn_submit').hide();
		} else {
			$('#btn_submit').show();
			$('#' + idDelElemento).next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#' + idDelElemento).css('border', '');

		}
	});


	$('.hora').on('keyup', function() {
		var idDelElemento = $(this).attr("id");
		var input = $('#' + idDelElemento).val();
		if (!isInt(input)) {
			$('#' + idDelElemento).next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#' + idDelElemento).after('<span class="mensaje-error" style="color: red;">No es Numero Entero</span>');
			$('#' + idDelElemento).css('border', '1px solid red');
			$('#btn_submit').hide();
		} else {
			$('#btn_submit').show();
			$('#' + idDelElemento).next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#' + idDelElemento).css('border', '');

		}
	});
	$('.texto1').on('keyup', function() {
		var idDelElemento = $(this).attr("id");
		$('#' + idDelElemento).next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)

		this.value = this.value.toUpperCase();
	});

	$('.validaUser').on('keyup', function() {
		this.value = this.value.toUpperCase();  //el this  el la etiquetra principAL de ahi se puede acceder a todos lados
		const regex = /^[a-zA-Z]{4,}\.[a-zA-Z]{4,}$/;
		var user = $('#' + this.id).val();

		if (regex.test(user)) {
			$('#' + this.id).next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#' + this.id).css('border', '2px solid #1dc62e');
		} else {
			$('#' + this.id).next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#' + this.id).after('<span class="mensaje-error" style="color: red;">Usuario no valido, debe de tener como minimo 4 letras ejemplo DEMO.DEMO</span>');
			$('#' + this.id).css('border', '1px solid red');
		}

	});


	$('.pass').on('keyup', function() {
		var input = $('#password').val();
		var input1 = $('#confirmarPassword').val();

		//     console.log(input+" / "+input1);

		if (input != input1) {
			$('#confirmarPassword').next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#password').next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#confirmarPassword').after('<span class="mensaje-error" style="color: red;">Su password deben de coincidir </span>');
			$('#confirmarPassword').css('border', '1px solid red');
			$('#password').after('<span class="mensaje-error" style="color: red;">Su password deben de coincidir </span>');
			$('#password').css('border', '1px solid red');
			$('#btn_submitE').hide();
		} else {
			$('#confirmarPassword').next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#password').next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#confirmarPassword').css('border', '2px solid #1dc62e');
			$('#password').css('border', '2px solid #1dc62e');
			$('#btn_submitE').show();
		}
	});

	$('.pass1').on('keyup', function() {
		var input = $('#passwordA').val();
		var input1 = $('#confirmarPasswordA').val();

		if (input != input1) {
			$('#confirmarPasswordA').next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#passwordA').next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#confirmarPasswordA').after('<span class="mensaje-error" style="color: red;">Su password deben de coincidir </span>');
			$('#confirmarPasswordA').css('border', '1px solid red');
			$('#passwordA').after('<span class="mensaje-error" style="color: red;">Su password deben de coincidir </span>');
			$('#passwordA').css('border', '1px solid red');
			$('#btn_submitA').hide();
		} else {
			$('#confirmarPasswordA').next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#passwordA').next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
			$('#confirmarPasswordA').css('border', '2px solid #1dc62e');
			$('#passwordA').css('border', '2px solid #1dc62e');
			$('#btn_submitA').show();
		}
	});

	$("#registraEstacionamiento").submit(function(event) {
		event.preventDefault();
        alert("id estacionamiento :: "+idEstacionamiento);
		var jsonData = $('#registraEstacionamiento').serializeArray()
			.reduce(function(a, z) {
				a[z.name] = z.value;
				return a;
			}, {});
		var camposVacios = Object.keys(jsonData).filter(function(key) {
			return jsonData[key] === "";
		});
		if (camposVacios.length > 0) {
			camposVacios.forEach(function(campo) {
				$('#' + campo).next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
				$('#' + campo).after('<span class="mensaje-error" style="color: red;">Los campos no deben estar vacios</span>');
			});
		} else {
			if (((parseInt(jsonData.fraccion1) + parseInt(jsonData.fraccion2) + parseInt(jsonData.fraccion3) + parseInt(jsonData.fraccion4)) - parseInt(jsonData.hora)) != 0) {
				$('#textoFraciones').text('El importe de fracciones no coicide  con el importe de la hora, favor de verificar de nuevo');
				$('#textoFraciones').css('color', 'red');
				return;
			}
				
			if(idEstacionamiento>0){
				console.log("Sera una actualizacion");
				jsonData['nombre'] =$("#nombre").val();
		        jsonData['direccion'] =$("#direccion").val();
		        jsonData['usuario'] =$("#usuario").val();
		        jsonData['idEstacionamiento'] =idEstacionamiento;
		       alert ( JSON.stringify(jsonData));
		        registrarEstacionamiento(jsonData);
				
			}else{
				console.log("Sera un nuevo registro ");
				registrarEstacionamiento(jsonData);
			}	
		

		}
	});

	$(".registra").submit(function(event) {
		event.preventDefault();
		var formId = this.id;
		var formulario = $('#' + formId);
		// Serializar los datos del formulario en un objeto JSON
		var formData = formulario.serializeArray();
		var jsonData = {};
		$.each(formData, function(index, field) {
			jsonData[field.name] = field.value;
		});
		var idRol = document.querySelector('#' + formId + ' input[name="rol"]').id;
		var esta = document.querySelector('#' + formId + ' input[name="estacionamiento"]').id;
		jsonData['rol'] = $('#' + idRol).val();
		jsonData['estacionamiento'] = $('#' + esta).val();

		var camposVacios = Object.keys(jsonData).filter(function(key) {
			return jsonData[key] === "";
		});
		if (camposVacios.length > 0) {
			camposVacios.forEach(function(campo) {
				$('.' + campo).next('.mensaje-error').remove(); // Eliminar mensaje de error anterior (si existe)
				$('.' + campo).after('<span class="mensaje-error" style="color: red;">Los campos no deben estar vacios</span>');
			});
		} else {
			alert(JSON.stringify(jsonData));
			registraEmpleado(jsonData);

		}
	});

	$("#btn_cancelar").click(function() {

		cancelar();
	});
	$("#btn_cancelarE").click(function() {

		cancelar();
	});
	
	///////////////////////////////////////////////////////
	$('#idUser').on('change', function(event) {

	// Obtener el valor seleccionado del elemento <select>
	var selectedValue = this.value;


	if (selectedValue == '0') {
		$('#idEstacionamiento').val('');
		$('#rol').val('');
	} else {
		// Obtener el elemento <select> por su ID
		var selectElement = $('#idUser');
		// Obtener la opción seleccionada
		var selectedOption = selectElement.find('option:selected');
		// Recuperar el valor del atributo data_user
		var dataUser = selectedOption.data('user');
		$('#idEstacionamiento').val(dataUser.estacionamiento);
		$('#rol').val(dataUser.rol);
	}
});

//////////////////////////////////////////////////////
});////fin ready

function ltsRoles() {

	jQuery.ajax({
		url: '/admin/getRoles',
		type: 'GET',
		complete: function(xhr, status) {
			console.log(xhr + ' ' + status);
		},
		success: function(jsonRest) {
			var json = JSON.parse(jsonRest);
			if (json.status == 'true') {
				var lista = JSON.parse(json.object);
				$('#roles').append($('<option>',
					{
						value: 0,
						text: 'Selecione una Opcion '
					}));
				for (var i = 0; i < lista.length; i++) {
					console.log(" rol --->" + lista[i].rol)
					$('#roles').append($('<option>',
						{
							value: lista[i].rol,
							text: lista[i].rol
						}));
				}
			} else {
				error(json.mensaje);
			}
		},
		error: function(jqXHR, exception) {
			console.log("Se genero el siguiente error", exception);
			mostrarError('Error while request [ListaNotasCredito] : ' + getErrorMessage(jqXHR, exception));
		}
	});
}
function ltsUsuarios() {

	$('#idUser').empty();
	jQuery.ajax({
		url: '/admin/getUsuarios',
		type: 'GET',
		complete: function(xhr, status) {
			console.log(xhr + ' ' + status);
		},
		success: function(jsonRest) {
			var json = JSON.parse(jsonRest);
			var lista1 = JSON.parse(json.object);
			if (json.status == 'true') {
				$('#idUser').append($('<option>',
					{
						value: 0,
						text: 'Selecione una Opcion '
					}));
				for (var i = 0; i < lista1.length; i++) {
					$('#idUser').append($('<option>',
						{
							value: lista1[i].id,
							text: lista1[i].usuario,
							'data-user': JSON.stringify({
								estacionamiento: lista1[i].estacionamiento,
								usuario: lista1[i].usuario,
								rol: lista1[i].rol
							})
						}));
				}
			} else {
				if (lista1.length < 1) {
					errorActualizaPagina(json.mensaje);
				}
			}
		},
		error: function(jqXHR, exception) {
			console.log("Se genero el siguiente error", exception);
			mostrarError('Error while request [ListaNotasCredito] : ' + getErrorMessage(jqXHR, exception));
		}
	});
}

function elimiarEstacionamientoId (id){
	
	var ver = $('#ver option:selected');
	// Elimina la opción seleccionada
	ver.prop('selected', false);
	var roles = $('#roles option:selected');
	// Elimina la opción seleccionada
	roles.prop('selected', false);


	Swal.fire({
	  title: "Que opcion desea realizar",
	  text: "Se eliminara  a los Empleados y Usuarios que esten ligados a este estacionamiento que desea eliminar",
	  icon: "warning",
	  showCancelButton: true,
	  confirmButtonColor: "#3085d6",
	  cancelButtonColor: "#d33",
	  confirmButtonText: "Deseo eliminar"
	}).then((result) => {
	  if (result.isConfirmed) {

	jQuery.ajax({
		url: '/admin/eliminarEstacionamiento/' + id,
		type: 'GET',
		complete: function(xhr, status) {
			console.log(xhr + ' ' + status);
		},
		success: function(jsonRest) {
			var json = JSON.parse(jsonRest);

			if (json.status == 'true') {
	 		   success(json.mensaje);
			} else {
			errorActualizaPagina(json.mensaje);
			}
		},
		error: function(jqXHR, exception) {
			console.log("Se genero el siguiente error", exception);
			mostrarError('Error while request [ListaNotasCredito] : ' + getErrorMessage(jqXHR, exception));
		}
	});
	  }
	});
	
}


function ltsEstacionmiento() {
	dataTableEstacionamientos.clear().draw();
	jQuery.ajax({
		url: '/admin/getEstacionamientos',
		type: 'GET',
		complete: function(xhr, status) {
			console.log(xhr + ' ' + status);
		},
		success: function(jsonRest) {
			var json = JSON.parse(jsonRest);
			if (json.status == 'true') {
				var lista = JSON.parse(json.object);
				for (var i = 0; i < lista.length; i++) {
					dataTableEstacionamientos.row.add([
						lista[i].id,
						lista[i].direccion,
						lista[i].hora,
						lista[i].autoChico,
						lista[i].autoGrande,
						lista[i].tipoPgo,
						'<button class="ver btn btn-info" data-id="' + lista[i].id + '"><i class="fas fa-eye" style="font-size:18px"></i></button>' + ' ' +
						'<button class="eliminar btn btn-danger" data-id="' + lista[i].id + '">X</button>'
					]).draw();
				}
			} else {
				error(json.mensaje);
			}
		},
		error: function(jqXHR, exception) {
			console.log("Se genero el siguiente error", exception);
			mostrarError('Error while request [ListaNotasCredito] : ' + getErrorMessage(jqXHR, exception));
		}
	});

}
function ltsEmpleados() {
	dataTableEmpleados.clear().draw();
	jQuery.ajax({
		url: '/admin/getEmpleados',
		type: 'GET',
		complete: function(xhr, status) {
			console.log(xhr + ' ' + status);
		},
		success: function(jsonRest) {
			var json = JSON.parse(jsonRest);
			if (json.status == 'true') {
				var lista = JSON.parse(json.object);
				for (var i = 0; i < lista.length; i++) {
					dataTableEmpleados.row.add([
						lista[i].empleado,
						lista[i].usuario,
						lista[i].perfil,
						lista[i].estacionamiento,			
						lista[i].estatus,
						'<button class="eliminar btn btn-danger" data-id="' + lista[i].idUsuario + '">X</button>'


					]).draw();
				}
			} else {
				error(json.mensaje);
			}
		},
		error: function(jqXHR, exception) {
			console.log("Se genero el siguiente error", exception);
			mostrarError('Error while request [ListaNotasCredito] : ' + getErrorMessage(jqXHR, exception));
		}
	});

}

function cancelar() {
	window.location.reload();
}


function registrarEstacionamiento(jsonData) {
	jQuery.ajax({
		url: '/admin/saveEstacionamiento',
		type: 'POST',
		data: JSON.stringify(jsonData),
		dataType: 'json',
		cache: false,
		contentType: false,
		processData: false,
		beforeSend: function(xhr) {
			tiempoPeticion('Guardando');
		},
		success: function(jsonResponse) {

			if (jsonResponse.status == 'true') {
				success(jsonResponse.mensaje);
				// window.location.reload();		
			} else {
				error(jsonResponse.mensaje);
				//window.location.reload();
			}

		},
		error: function(textStatus, errorThrown) {

			error('error ' + textStatus + " " + errorThrown);
		},
		complete: function(xhr, textStatus) {
			console.log(xhr + ' ' + textStatus);
		},
	});
}
function registraEmpleado(jsonData) {
	jQuery.ajax({
		url: '/admin/saveEmpleado',
		type: 'POST',
		data: JSON.stringify(jsonData),
		dataType: 'json',
		cache: false,
		contentType: false,
		processData: false,
		beforeSend: function(xhr) {
			tiempoPeticion('Guardando');
		},
		success: function(jsonResponse) {

			if (jsonResponse.status == 'true') {
				success(jsonResponse.mensaje);
				// window.location.reload();		
			} else {
				error(jsonResponse.mensaje);
				//window.location.reload();
			}
		},
		error: function(textStatus, errorThrown) {

			error('error ' + textStatus + " " + errorThrown);
		},
		complete: function(xhr, textStatus) {
			console.log(xhr + ' ' + textStatus);
		},
	});
}
function getEstacionamientoId(id) {
	
	var ver = $('#ver option:selected');
	// Elimina la opción seleccionada
	ver.prop('selected', false);
	var roles = $('#roles option:selected');
	// Elimina la opción seleccionada
	roles.prop('selected', false);

	jQuery.ajax({
		url: '/admin/getestacionamientoid/' + id,
		type: 'GET',
		complete: function(xhr, status) {
			console.log(xhr + ' ' + status);
		},
		success: function(jsonRest) {
			var json = JSON.parse(jsonRest);
			var estacionamiento = JSON.parse(json.object);
			if (json.status == 'true') {
				
				idEstacionamiento=estacionamiento.idestacionamiento;
				$("#verEmpleados").hide();
				$("#verEstacionamiento").hide();
				$('#formEmpleado').hide();
				$('#formEmpleadoAdmin').hide();
				$('#formEstacionamiento').show();
				
				$("#nombre").val(estacionamiento.nombre);
				$("#direccion").val(estacionamiento.direccion);
				$("#hora").val(parseInt(estacionamiento.importehora));
				$("#boletoPerdido").val(estacionamiento.importeboletoperdido);
				$("#fraccion1").val(estacionamiento.fraccion1);
				$("#fraccion2").val(estacionamiento.fraccion2);
				$("#fraccion3").val(estacionamiento.fraccion3);
				$("#fraccion4").val(estacionamiento.fraccion4);
				$("#horasPromocion").val(parseInt(estacionamiento.horaspromo));
				$("#importePromocion").val(estacionamiento.importepromo);
				$("#pensionAutoChico").val(estacionamiento.importeautochico);
				$("#pensionAutoGrande").val(estacionamiento.importeautogrande);
				$("#usuario").val(estacionamiento.username);
				
				 if(estacionamiento.tipopromo=='HORA'){ $("#tipoCobro").val("h");}
				 if(estacionamiento.tipopromo=='HORA*PROMOCION'){ $("#tipoCobro").val("h*p");}
				 if(estacionamiento.tipopromo=='AUTOMATICO'){ $("#tipoCobro").val("a");}
				 $("*.mensaje-error").remove(".mensaje-error");
				
				 $("#registraEstacionamiento :input:not(#btn_submit, #btn_cancelar)").prop("disabled", true);
				 $("#btn_submit").hide();

            
           $("#editarEstacionamiento").remove(); 
           // Insertar el nuevo botón después del botón "Guardar"
           $("#btn_submit").after('<button  type="button" class="btn btn-info btn-block" id="editarEstacionamiento"  onclick="miFuncion()" >Editar</button>');
				 
			} else {
				if (lista1.length < 1) {
					errorActualizaPagina(json.mensaje);
				}
			}
		},
		error: function(jqXHR, exception) {
			console.log("Se genero el siguiente error", exception);
			mostrarError('Error while request [ListaNotasCredito] : ' + getErrorMessage(jqXHR, exception));
		}
	});
}

function miFuncion() {
	var ver = $('#ver option:selected');
	// Elimina la opción seleccionada
	ver.prop('selected', false);
	var roles = $('#roles option:selected');
	// Elimina la opción seleccionada
	roles.prop('selected', false);

	alert("Clic en el botón");
	$("#registraEstacionamiento :input:not(#nombre,#direccion,#usuario)").prop("disabled", null);
	$("#editarEstacionamiento").hide();
	// Usando el método .remove(".mensaje-error")
	$("*.mensaje-error").remove(".mensaje-error");
	$("#btn_submit").show();
}

function eliminarEmpleado(id) {

	var ver = $('#ver option:selected');
	// Elimina la opción seleccionada
	ver.prop('selected', false);
	var roles = $('#roles option:selected');
	// Elimina la opción seleccionada
	roles.prop('selected', false);
	
	Swal.fire({
		title: "Que opcion desea realizar",
		text: "Si elimina al empleado el usuario quedara sin uso , puede agregar otro  empleado y agregarle el usuario que estara disponible",
		icon: "warning",
		showCancelButton: true,
		confirmButtonColor: "#3085d6",
		cancelButtonColor: "#d33",
		confirmButtonText: "Deseo eliminar"
	}).then((result) => {
		if (result.isConfirmed) {

			jQuery.ajax({
				url: '/admin/eliminarEmpleado/' + id,
				type: 'GET',
				complete: function(xhr, status) {
					console.log(xhr + ' ' + status);
				},
				success: function(jsonRest) {
					var json = JSON.parse(jsonRest);

					if (json.status == 'true') {
						success(json.mensaje);
					} else {
						errorActualizaPagina(json.mensaje);
					}
				},
				error: function(jqXHR, exception) {
					console.log("Se genero el siguiente error", exception);
					mostrarError('Error while request [ListaNotasCredito] : ' + getErrorMessage(jqXHR, exception));
				}
			});
		}
	});
}



