var user = 0;
var estacionamineto = 0;
var hora = 0,
	fraccion1 = 0,
	fraccion2 = 0,
	fraccion3 = 0,
	fraccion4 = 0,
	horasPromo = 0,
	importePromo = 0;
var tienePromo = "";
var tipoPromo = "";
var dataTableAutos;
$(document).ready(function() {

	$('#verAutosPerdidos').hide();

	$("#sidebarToggle").click(); // Simula un clic en el botón
	cargarHora();
	getInfo();
	$(document).keydown(function(tecla) {

		if (tecla.keyCode == 113) {
			entrada();
		}
		if (tecla.keyCode == 115) {
			salida();
		}

		if (tecla.keyCode == 119) {
			busqueda();
		}
	});

	user = $('#idUser').text();
	estacionamineto = $('#idEstacionamiento').text();
	hora = $('#hora').text();
	fraccion1 = $('#fraccion1').text();
	fraccion2 = $('#fraccion2').text();
	fraccion3 = $('#fraccion3').text();
	fraccion4 = $('#fraccion4').text();
	horasPromo = $('#horasPromo').text();
	importePromo = $('#importePromo').text();
	tienePromo = $('#tienePromo').text();
	tipoPromo = $('#tipo').text();

	$('#entrada').click(function() {
		entrada();
	});
	$('#salida').click(function() {
		salida();
	});
	$('#busqueda').click(function() {
		busqueda();
	});

	$("#cancelar_cobro").click(function() {

		cancelar();
	});

	$('#importe').keyup(function(event) {

		var importe = $('#importe').val();
		var importePago = $('#importePago').val().replace('$', "").replace(/\s+/g, '');
		var resultado = 0.00;



		if (importe != "") {

			if (isDouble(importe) && isDouble(importePago)) {
				resultado = importe - importePago;

				if (resultado >= 0) {
					$('#mensaje').text('');
					$('#cambioForm').show();
					$('#cambio').css('color', 'black');
					$('#cambio').val(formatoMoneda(resultado));
					$('#btn_submit').show();

				} else {
					$('#btn_submit').hide();
					$('#cambioForm').hide();
					$('#mensaje').css('color', 'red');
					$('#mensaje').text('Hay un faltante de ' + formatoMoneda((resultado * -1)));
				}

			} else {
				$('#btn_submit').hide();
				$('#cambioForm').hide();
				$('#mensaje').css('color', 'red');
				$('#mensaje').text('No es un importe correcto, ejemplo $1.20');
			}
		} else {
			$('#mensaje').text('');
		}
	});

	$("#btnRegistrarSalida").submit(function(e) {

		e.preventDefault();
		var jsonData = $("#btnRegistrarSalida").serializeArray()
			.reduce(function(a, z) {
				a[z.name] = z.value;
				return a;
			}, {});

      alert(JSON.stringify(jsonData));
		if (jsonData != null) {
			jQuery.ajax({
				url: 'auto/guardarSalida',
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
					} else {
						error(jsonResponse.mensaje);
					}
				},
				error: function(textStatus, errorThrown) {
					Swal.fire('error ' + textStatus + " " + errorThrown);
				},
				complete: function(xhr, textStatus) {
					console.log(xhr + ' ' + textStatus);
				},
			});

		} else {
			error("No se recibieron datos para guardar")
		}
		$('#importe').val('')
		$('#cambio').val('$0.00');
		$('#mensaje').text('');
		////////////////////////////////
		$('#contenido').show();
		$('#cobrarBoletaje').css('display', 'none');
	});


	dataTableAutos = $('#tablaAutos').DataTable({
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

	dataTableAutos.on('click', '.cobrar', function() {
		var id = $(this).data('id');
		generarCobroBoletoPerdido(id);
	});


	$("#btnRegistrarEntrada").submit(function(e) {

		e.preventDefault();

		var folio = $('#folio').text();
		var jsonData = $("#btnRegistrarEntrada").serializeArray()
			.reduce(function(a, z) {
				a[z.name] = z.value;
				return a;
			}, {});


		jsonData['idEstacionamiento'] = parseInt(estacionamineto);
		jsonData['idUsuario'] = parseInt(user);
		jsonData['folio'] = parseInt(folio);

		alert('valores de entrda ' + JSON.stringify(jsonData))

		jQuery.ajax({
			url: 'auto/guardar',
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
				} else {
					error(jsonResponse.mensaje);
				}

			},
			error: function(textStatus, errorThrown) {

				Swal.fire('error ' + textStatus + " " + errorThrown);
			},
			complete: function(xhr, textStatus) {
				getInfo();
				$('#contenido').show();
				$('#entradaAuto').css('display', 'none');
				console.log(xhr + ' ' + textStatus);
			},
		});
	});


}); //****************************fin ready*****************************************************************



function cargarHora() {

	var actualizarHora = function() {
		var fecha = new Date(),
			hora = fecha.getHours(),
			minutos = fecha.getMinutes(),
			segundos = fecha.getSeconds(),
			diaSemana = fecha.getDay(),
			dia = fecha.getDate(),
			mes = fecha.getMonth(),
			anio = fecha.getFullYear(),
			ampm;

		var $pHoras = $("#horas"),
			$pSegundos = $("#segundos"),
			$pMinutos = $("#minutos"),
			$pAMPM = $("#ampm"),
			$pDiaSemana = $("#diaSemana"),
			$pDia = $("#dia"),
			$pMes = $("#mes"),
			$pAnio = $("#anio");
		var semana = ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'];
		var meses = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];

		$pDiaSemana.text(semana[diaSemana]);
		$pDia.text(dia);
		$pMes.text(meses[mes]);
		$pAnio.text(anio);
		if (hora >= 12) {
			hora = hora - 12;
			ampm = "PM";
		} else {
			ampm = "AM";
		}
		if (hora == 0) {
			hora = 12;
		}
		if (hora < 10) {
			$pHoras.text("0" + hora)
		} else {
			$pHoras.text(hora)
		};
		if (minutos < 10) {
			$pMinutos.text("0" + minutos)
		} else {
			$pMinutos.text(minutos)
		};
		if (segundos < 10) {
			$pSegundos.text("0" + segundos)
		} else {
			$pSegundos.text(segundos)
		};
		$pAMPM.text(ampm);

	};
	actualizarHora();
	var intervalo = setInterval(actualizarHora, 1000);


}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function crearFormulario() {
	var tipoCobro = $('#tipo').text();
	var x = ' <div class="form-group text-black-border">' +
		' <label for="">Marca</label>' +
		'<input type="text"  class="form-control  border-modal"  list="marcas" name="marca" id="marcaAuto" placeholder="Marca/Modelo" onkeyup="javascript:this.value=this.value.toUpperCase();">' +
		'<datalist id="marcas">';
	obtenerMarcas().forEach(function(marca) {
		x += '<option value=' + marca + '>' + marca + '</option>'
	});
	x += '</datalist>' +
		'</div>' +
		'<div class="form-group text-black-border">' +
		'<label for="">Color</label>' +
		' <input type="text"  class="form-control  border-modal" name="color" id="colorAuto" list="colores" placeholder="Color" autocomplete="off" onkeyup="javascript:this.value=this.value.toUpperCase();">' +
		'<datalist id="colores">';


	getColores().forEach(function(color) {

		x += '<option value=' + color + '>' + color + '</option>'
	});

	x += '</datalist>' +
		'</div>' +
		'<div class="form-group text-black-border">' +
		'<label for="">Placa</label>' +
		' <input type="text" class="form-control border-modal placa1" name="placa" id="placaAuto" onkeyup="javascript:this.value=this.value.toUpperCase();"  placeholder="Si no exite placas, poner S/P " autocomplete="off"> ' +
		'</div>';



	if (tipoCobro == 'HORA') {

		x += '<div class="form-group  text-black-border">' +
			'<label for="">Tipo Cobro</label>' +
			' <input type="text" class="form-control border-modal" name="tipoCobro" id="tipoCobro" value="HORA"   readOnly> ' +
			'</div>';
	}
	if (tipoCobro == 'AUTOMATICO') {

		x += '<div class="form-group  text-black-border">' +
			'<label for="">Tipo Cobro</label>' +
			' <input type="text" class="form-control border-modal" name="tipoCobro" id="tipoCobro" value="AUTOMATICO"   readOnly> ' +
			'</div>';
	}
		if (tipoCobro == 'HORA*PROMOCION') {

		x += '<div class="form-group  text-black-border">' +
			'<label for="">Tipo Cobro</label>' +
			 '<select  class="form-control border-modal" name="tipoCobro" id="tipoCobro" >'+
			    '<option selected value="HORA">HORA</option>'+
			    '<option value="PROMOCION">PROMOCION</option>'+
		 ' </select>'  +
			//' <input type="text" class="form-control border-modal" name="tipoCobro" id="tipoCobro" value="AUTOMATICO"   readOnly> ' +
			'</div>';
	}

	return x;
}

function entrada() {
	$('#contenidodasboard').show();
	$('#div_boton').show();
	$('#verAutosPerdidos').hide();
	var html = crearFormulario();

	Swal.fire({
		closeOnEsc: false,
		closeOnClickOutside: false,
		showCloseButton: true,
		color: '#4E73DF',
		allowOutsideClick: false,
		title: 'Ingresa los datos del Vehiculo',
		html: html,
		confirmButtonText: 'Guardar ',
		confirmButtonColor: '#4E73DF',

		preConfirm: () => {
			var placa = Swal.getPopup().querySelector('#placaAuto').value;
			var color = Swal.getPopup().querySelector('#colorAuto').value;
			var marca = Swal.getPopup().querySelector('#marcaAuto').value;
			var tipoCobro = Swal.getPopup().querySelector('#tipoCobro').value;
			if (!placa || !color || !marca) {
				Swal.showValidationMessage(`Es necesario completar todos los campos...`)
			}
			return {
				placa: placa,
				color: color,
				marca: marca,
				tipoCobro: tipoCobro
			}
		}
	}).then((result) => {
		if (result.isConfirmed) {
			registrarEntrada(result.value.placa, result.value.color, result.value.marca, result.value.tipoCobro);
		}
	});

}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

function cancelar() {
	window.location.reload();
}

function salida() {

	$('#contenidodasboard').show();
	$('#div_boton').show();
	$('#verAutosPerdidos').hide();

	var formulario = '<div class="form-group text-black-border ">' +
		'<label for="folio">N° Folio</label>' +
		' <input type="text" class="form-control  border-modal_succes " id="folio" placeholder="N° Folio" autocomplete="off">' +
		' </div>';

	Swal.fire({
		closeOnEsc: false,
		closeOnClickOutside: false,
		showCloseButton: true,
		allowOutsideClick: false,
		color: '#1cc88a',
		title: 'Ingresa N° de Folio ',
		html: formulario,
		confirmButtonColor: '#1cc88a',
		confirmButtonText: 'Buscar',
		preConfirm: () => {
			var folio = Swal.getPopup().querySelector('#folio').value;

			if (!folio) {
				Swal.showValidationMessage('Es necesario ingresar el N° Folio')
			} else if (isInt(folio)) {
				return {
					folio: folio
				}
			} else {
				Swal.showValidationMessage('El dato recibido no es numero.<br>ingresar  solo numeros enteros por favor...<br> ejemplo 123456456789 ')
			}

		}
	}).then((result) => {
		/* Read more about isConfirmed, isDenied below */
		if (result.isConfirmed) {
			registrarSalida(result.value.folio)
		}
	})
}

function busqueda() {
	$('#contenidodasboard').hide();
	$('#div_boton').hide();
	$('#verAutosPerdidos').show();
	getAutosPerdidos();
}


function registrarEntrada(placas, color, marca, tipoCobro) {

	var folio = $('#folio').text();


	if (!placas || !color || !marca) {
		Swal.fire('Es necesario completar todos los campos...')
	} else {
		const json = {
			placa: placas,
			color: color,
			marca: marca,
			idEstacionamiento: parseInt(estacionamineto),
			tipoCobro: tipoCobro,
			folio: parseInt(folio)
		};

		jQuery.ajax({
			url: 'auto/guardar',
			type: 'POST',
			data: JSON.stringify(json),
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
				} else {
					error(jsonResponse.mensaje);
				}

			},
			error: function(textStatus, errorThrown) {

				Swal.fire('error ' + textStatus + " " + errorThrown);
			},
			complete: function(xhr, textStatus) {
				getInfo();
				console.log(xhr + ' ' + textStatus);
			},
		});

	}

}

function registrarSalida(folio) {

	if (!folio) {
		Swal.fire('Es necesario ingresar el N° Folio');
	} else {
		if (isInt(folio)) {
			const json = {
				idEstacionamiento: parseInt(estacionamineto),
				folio: parseInt(folio)
			};
			var obj = JSON.stringify(json);
			jQuery.ajax({
				type: 'GET',
				cache: false,
				url: 'auto/buscarSalida/' + obj,
				success: function(jsonResponse) {
					var jsonString = JSON.parse(jsonResponse);

					if (jsonString.status == 'true') {
						mostrarDatosSalida(JSON.parse(jsonString.object), false);//false es que el cobro no incluye boleto perdido 
					} else {
						errorActualizaPagina(jsonString.mensaje);
					}

				},
				error: function(textStatus, errorThrown) {

					Swal.fire('error ' + textStatus + " " + errorThrown);
				},
				complete: function(xhr, textStatus) {
					console.log(xhr + ' ' + textStatus);
				},
			});
		} else {
			Swal.fire('El numero de Folio debe de ser un numero entero, ejemplo: 123456');
		}
	}
}

function mostrarDatosSalida(object, boletoPerdido) {
	
	$("#perdidoCobro").remove();
    $('#verAutosPerdidos').hide();
	$('#contenido').hide();
	$('#cobrarBoletaje').css('display', 'block');
	$('#btn_submit').hide();
	/***************************************************** */
	$('#idEstacionamientoS').val(object.idEstacionamiento);
	$('#consumoFraccion').val(object.consumoFraccion);
	$('#folioSalida').val(object.folio);
	$('#marca1').val(object.marca);
	$('#color1').val(object.color);
	$('#placa1').val(object.placa);
	$('#fechaEntrada').val(object.fechaEntrada);
	$('#fechaSalida').val(object.fechaSalida);
	$('#consumo').val(object.consumo);
	$('#statusPago').val(object.statusPago);
	$('#importePago').val(object.Cobro);
	$('#modoPago').val(object.modoPago);




}
function getAutosPerdidos() {

	dataTableAutos.clear().draw();

	estacionamineto = $('#idEstacionamiento').text();
	jQuery.ajax({
		type: 'GET',
		cache: false,
		url: 'auto/getListaAutos/' + parseInt(estacionamineto),
		success: function(jsonResponse) {
			var json = JSON.parse(jsonResponse);
			if (json.status == 'true') {
				var lista = JSON.parse(json.object);
				for (var i = 0; i < lista.length; i++) {
					dataTableAutos.row.add([
						lista[i].marca,
						lista[i].placa,
						lista[i].color,
						lista[i].fechaEntrada,
						'<button class="cobrar btn btn-info" data-id="' + lista[i].folio + '-' + lista[i].idEstacionamiento + '">Cobrar</button>'
					]).draw();
				}
			} else {
				error(json.mensaje);
			}
		},
		error: function(textStatus, errorThrown) {
			Swal.fire('error ' + textStatus + " " + errorThrown);
		},
		complete: function(xhr, textStatus) {
			console.log(xhr + ' ' + textStatus);
		},
	});
}

function getInfo() {

	jQuery.ajax({
		type: 'GET',
		cache: false,
		url: 'info/data/',
		success: function(jsonResponse) {
			var jsonString = JSON.parse(jsonResponse);

			if (jsonString.status == 'true') {
				SetInfoView(JSON.parse(jsonString.object));
			} else {
				error(jsonString.mensaje);
			}

		},
		error: function(textStatus, errorThrown) {

			Swal.fire('error ' + textStatus + " " + errorThrown);
		},
		complete: function(xhr, textStatus) {
			console.log(xhr + ' ' + textStatus);
		},
	});
}
function SetInfoView(json) {

	$('#folio').text(json.folio);
	$('#entrantes').text(json.entrada);
	$('#pendientes').text(json.pendientes);
	$('#salientes').text(json.salidas);
}
function getcobro() {

	jQuery.ajax({
		type: 'GET',
		cache: false,
		url: 'info/cobro',
		success: function(jsonResponse) {
			var jsonString = JSON.parse(jsonResponse);

			if (jsonString.status == 'true') {
				SetInfoView(JSON.parse(jsonString.object));
			} else {
				error(jsonString.mensaje);
			}

		},
		error: function(textStatus, errorThrown) {

			Swal.fire('error ' + textStatus + " " + errorThrown);
		},
		complete: function(xhr, textStatus) {
			console.log(xhr + ' ' + textStatus);
		},
	});
}

function generarCobroBoletoPerdido(id) {

	// Dividir la cadena en base al guion "-"
	let partes = id.split("-");

	// Crear un objeto JSON con las propiedades a y b
	let json = {
		folio: parseInt(partes[0]),
		idEstacionamiento: parseInt(partes[1])
	};

	alert(JSON.stringify(json));
	jQuery.ajax({
		type: 'GET',
		cache: false,
		url: 'auto/buscarSalidaBoletoPerdido/' + JSON.stringify(json),
		success: function(jsonResponse) {
			var jsonString = JSON.parse(jsonResponse);

			if (jsonString.status == 'true') {
				mostrarDatosSalida(JSON.parse(jsonString.object), true);//true es que el cobro se incluye boleto perdido 
			} else {
				error(jsonString.mensaje);
			}

		},
		error: function(textStatus, errorThrown) {

			Swal.fire('error ' + textStatus + " " + errorThrown);
		},
		complete: function(xhr, textStatus) {
			console.log(xhr + ' ' + textStatus);
		},
	});

}

function obtenerMarcas() {
	return [
		"TOYOTA",
		"HONDA",
		"FORD",
		"CHEVROLET",
		"NISSAN",
		"BMW",
		"MERCEDES-BENZ",
		"AUDI",
		"VOLKSWAGEN",
		"JEEP",
		"HYUNDAI",
		"MAZDA",
		"LEXUS",
		"SUBARU",
		"KIA",
		"TESLA",
		"FERRARI",
		"PORSCHE",
		"LAMBORGHINI",
		"ASTON MARTIN",
	];
}
function getColores() {
	return [
		"ROJO",
		"VERDE",
		"AZUL",
		"AMARILLO",
		"NARANJA",
		"MORADO",
		"ROSA",
		"NEGRO",
		"BLANCO",
		"GRIS",
		"MARRÓN",
		"CELESTE",
		"TURQUESA",
		"VIOLETA",
		"PLATA",
		"DORADO",
		"CIAN",
		"MAGENTA",
		"LIMA",
		"GRANATE"
	];
}