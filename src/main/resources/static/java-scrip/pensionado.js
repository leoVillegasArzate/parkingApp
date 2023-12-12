var dataTablePension;
var dataTableMesPagos;
var dataTablePagosAgregados;
var pensionadoid = 0;
$(document).ready(function () {

  $('#tablaPensionado').show();
  $('#newPensionado').show();

  $("#sidebarToggle").click(); // Simula un clic en el botón
  dataTablePension = $('#tablePensionado').DataTable({
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
  dataTablePagosAgregados = $('#pagosAgregados').DataTable({
    bFilter: true,
    bSort: false,
    destroy: true,
    lengthMenu: [5], // Establece el número de entradas a mostrar en el menú desplegable
    pageLength: 5, // Establece el número de entradas por página
    "language": {
      "zeroRecords": "Sin registros",
      "info": "P&aacute;gina _PAGE_ de _PAGES_",
      "infoEmpty": "Sin registros",
      "infoFiltered": "(filtrado de un total de _MAX_)",
      "paginate": {
        "previous": "Anterior",
        "next": "Siguiente",
        "last": "&Uacute;ltima p&aacute;gina",
        "first": "Primera p&aacute;gina"
      }
    }
  });

  dataTableMesPagos = $('#tableMesPagos').DataTable({
    bFilter: true,
    bSort: false,
    destroy: true,
    lengthMenu: [5], // Establece el número de entradas a mostrar en el menú desplegable
    pageLength: 5, // Establece el número de entradas por página
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

  lstPensionados();
  $("#formulario").hide();

  dataTablePension.on('click', '.ver', function () {
    var id = $(this).data('id');
    verPensionado(id);
  });
  dataTableMesPagos.on('click', '.agregarPago', function () {
    var id = $(this).data('id');
    agregarPago(id);
  });

  dataTablePagosAgregados.on('click', '.eliminarRow', function () {
    var id = $(this).data('id');
    eliminarPago(id);
  });

  dataTablePension.on('click', '.eliminar', function () {
    var id = $(this).data('id');
    eliminar(id);
  });
  dataTablePension.on('click', '.cobrar', function () {
    var id = $(this).data('id');
    cobrar(id);
  });
  $('#btn-editar').click(function (e) {
    $('#pagos').hide();
    Swal.fire("Si el pensionado desea cambiar de auto, se elimina el auto existente y se registra nuevamente.");

    $('#guardarPensionado :input:not(#bfechaRegistro, #marca,#modelo,#otraCantidadAux,#idPensionadoAux,#idEstacionamientoAux)').prop('disabled', false);
    $("#btn-sumit").show();
    $("#btn-editar").hide();

  });
  $("#newPensionado").click(function (e) {
    pensionadoid = 0;
    $('#pagos').hide();
    $("#btn-sumit").show();
    $("#btn-editar").hide();

    $("#guardarPensionado input").val(""); // Limpiar campos del Formulario 2
    $("#tablaPensionado").hide();
    $("#formulario").show();
    $("#fechaRegistro").val(getFecha());
    getImportePension();
    $("#otraCantidad").prop("readonly", true);
    $('#idEstacionamiento').val($('#esta').text());

  });
  $("#cancelar").click(function () {
    cancelar();
  });
  $("#cancelarPago").click(function () {
    cancelar();
  });
  $('#importe').change(function (e) {
    var valor = $('#importe').val();
    if (valor === 'OtraCantidad') {
      $("#otraCantidad").val('');
      $("#otraCantidad").prop("readonly", false);
    } else {
      $("#otraCantidad").val(valor);
      $("#otraCantidad").prop("readonly", true);
    }
  });

  $("#guardarPensionado input").on("keyup", function (event) {
    var idDelElemento = $(this).attr("id");
    $("#" + idDelElemento)
      .next(".mensaje-error")
      .remove();
  });

  $("#guardarPensionado").on("submit", function (event) {
    event.preventDefault();
    console.log(pensionadoid);
    var jsonData = $("#guardarPensionado")
      .serializeArray()
      .reduce(function (a, z) {
        a[z.name] = z.value;
        return a;
      }, {});

    var camposVacios = Object.keys(jsonData).filter(function (key) {

      return jsonData[key] === "";
    });

    if (camposVacios.length > 0) {
      camposVacios.forEach(function (campo) {
        console.log("campo vacio " + campo);
        $("#" + campo).next(".mensaje-error").remove(); // Eliminar mensaje de error anterior (si existe)
        $("#" + campo).after('<span class="mensaje-error" style="color: red;">Los campos no deben estar vacios</span>');
      });
    } else {

      if (!/^\d{10}$/.test(jsonData.telefono)) {
        $("#" + Object.keys(jsonData)[2]).next(".mensaje-error").remove(); // Eliminar mensaje de error anterior (si existe)
        $("#" + Object.keys(jsonData)[2]).after('<span class="mensaje-error" style="color: red;">No es un formato de Telefono 10 digitos valido </span>');

      } else {
        if (!/^[0-9a-zA-Z]{12,13}$/.test(jsonData.rfc)) {
          $("#" + Object.keys(jsonData)[3]).next(".mensaje-error").remove(); // Eliminar mensaje de error anterior (si existe)
          $("#" + Object.keys(jsonData)[3]).after('<span class="mensaje-error" style="color: red;">No cumple con las caracteristicas debe tener como minimo 12 y maximo 13 caracteres </span>');
        } else {
          if (pensionadoid > 0) {
            jsonData['idPensionado'] = pensionadoid;
          }
          Swal.fire({
            title:
              "Favor de verificar que  los datos ingresados sean los correctos",
            text: JSON.stringify(jsonData),
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Guardar",
          }).then((result) => {
            if (result.isConfirmed) {
              savePensionado(jsonData);
            }
          });
        }
      }
    }
  });

  $(".texto").on("keyup", function () {
    var idDelElemento = $(this).attr("id");
    $("#" + idDelElemento)
      .next(".mensaje-error")
      .remove(); // Eliminar mensaje de error anterior (si existe)

    this.value = this.value.toUpperCase();
  });
  $("#btn-editar").hide();
  $('#pagos').hide();

  $('#guardarCobro').submit(function (e) {
    e.preventDefault();
    var datosFilas = dataTablePagosAgregados.rows().data();
    var lstPagosAgregados = [];
    if ($('#importeCobro').val() <= 0) {
      Swal.fire("El importe  no puede ser 0.00");
    } else {
      if (datosFilas.length < 1) {
        Swal.fire("No se cargo ningun registro de pago ");
      } else {
        datosFilas.each(function (fila, indice) {
          // Crear un elemento jQuery a partir de la cadena HTML del botón
          var boton = $(fila[3]);
          // Acceder al valor del atributo data-id
          var valorDataId = boton.data('id');
          lstPagosAgregados.push(valorDataId);
        });
        let json = {
          idPensionado: $('#idU').text(),
          lstPagos: lstPagosAgregados
        };
        guardarPagoPension(json);
      }
    }
  });


}); /////////////////////////////fin del ready

function cancelar() {
  window.location.reload();
}

function getFecha() {
  // Obtener la fecha actual
  var fechaActual = new Date();
  // Obtener el año, mes y día
  var anio = fechaActual.getFullYear();
  var mes = (fechaActual.getMonth() + 1).toString().padStart(2, "0"); // Se agrega 1 porque los meses van de 0 a 11
  var dia = fechaActual.getDate().toString().padStart(2, "0");
  // Formatear la fecha como "YYYY-MM-DD"
  var fechaFormateada = anio + "-" + mes + "-" + dia;
  return fechaFormateada;
}

function getImportePension() {
  jQuery.ajax({
    url: "/pensionado/getPension",
    type: "GET",
    complete: function (xhr, textStatus) {
      // Esta función se ejecutará siempre después de que la solicitud se complete
      console.log("La solicitud se ha completado.");
      console.log("Estado de la solicitud:", textStatus);
    },
    success: function (jsonRest) {
      var json = JSON.parse(jsonRest);
      if (json.status == "true") {
        var obj = JSON.parse(json.object);
        $("#importe").append(
          $("<option>", {
            value: 'OtraCantidad',
            text: 'Otra Cantidad',
          })
        );
        $("#importe").append(
          $("<option>", {
            value: obj.autoChico,
            text: JSON.stringify(obj.autoChico) + " / Auto Chico",
          })
        );
        $("#importe").append(
          $("<option>", {
            value: obj.autoGrande,
            text: JSON.stringify(obj.autoGrande) + " / Auto Grande",
          })
        );
        $("#importe").val(obj.autoChico);
        $("#otraCantidad").val(obj.autoChico);
        $("#otraCantidad").prop("readonly", true);
      } else {
        error(json.mensaje);
      }
    },
    error: function (xhr, textStatus, errorThrown) {
      console.log("Error:", textStatus); // Imprimir el estado del error
      console.log("Código de estado HTTP:", xhr.status); // Imprimir el código de estado HTTP
      console.log("Error arrojado:", errorThrown); // Imprimir detalles adicionales del error
      errorActualizaPagina(textStatus);
    },
  });
}

function savePensionado(jsonData) {


  jQuery.ajax({
    url: "/pensionado/save",
    type: 'POST',
    data: JSON.stringify(jsonData),
    dataType: 'json',
    cache: false,
    contentType: false,
    processData: false,
    complete: function (xhr, textStatus) {
      console.log("La solicitud se ha completado.");
      console.log("Estado de la solicitud:", textStatus);
    },
    success: function (jsonResponse) {
      if (jsonResponse.status == 'true') {
        success(jsonResponse.mensaje);
      } else {
        error(jsonResponse.mensaje);
      }
    },
    error: function (xhr, textStatus, errorThrown) {
      console.log("Error:", textStatus); // Imprimir el estado del error
      console.log("Código de estado HTTP:", xhr.status); // Imprimir el código de estado HTTP
      console.log("Error arrojado:", errorThrown); // Imprimir detalles adicionales del error
    },
  });
}

function lstPensionados() {
  // var idEstacionamiento= parseInt ($('#esta').text());  //$('#idEstacionamiento').val($('#esta').text());
  dataTablePension.clear().draw();
  $.ajax({
    type: "GET",
    url: "/pensionado/lstPensionados",
    success: function (jsonRest) {
      var json = JSON.parse(jsonRest);
      if (json.status == "true") {
        var lista = JSON.parse(json.object);
        for (var i = 0; i < lista.length; i++) {
          var btn = '';
          var icon = '';
          var title = '';
          if (lista[i].statusPago == 'CU') {
            btn = 'btn-success';
            icon = 'fas fa-check-circle';
            title = 'PAGO CUBIERTO';
          }
          if (lista[i].statusPago == 'PA') {
            btn = 'btn-warning'
            icon = 'far fa-calendar-alt';
            title = 'PRONTO PAGO';
          }
          if (lista[i].statusPago == 'PE') {
            btn = 'btn-danger'
            icon = 'fas fa-exclamation-triangle';
            title = 'PAGO PENDIENTE';

          }
          dataTablePension.row.add([
            lista[i].nombre,
            lista[i].marcaauto,
            lista[i].placas,
            //            lista[i].modelo,
            lista[i].importe,
            lista[i].fecharegistro,
            lista[i].fechapago,
            lista[i].importePago,
            '<button title="' + title + '" class="btn ' + btn + '" style="padding: 4px 4px; font-size: 9px;"> <i class="' + icon + '" style="font-size:9px"></i></button>',

            '<button  " style="padding: 5px 5px; font-size: 9px;"  title="ver info del pensionado / editar "    class="ver btn btn-info " data-id=\'' + JSON.stringify(lista[i]) + '\'><i class="fas fa-eye" style="font-size:11px"></i></button>' + '  ' +
            '<button " style="padding: 5px 5px; font-size: 9px;"  title="generar cobro de pension " class="cobrar btn btn-success " data-id=\'' + JSON.stringify(lista[i]) + '\'><i class="far fa-money-bill-alt" style="font-size:11px"></i></button>' + '  ' +
            '<button " style="padding: 5px 5px; font-size: 9px;"  title="eliminar pensionado, dar de baja"   class="eliminar btn btn-danger " data-id="' + lista[i].idpensionado + '-' + lista[i].idestacionamiento + '-' + lista[i].nombre + '"><i class="far fa-trash-alt" style="font-size:11px"></i></button>'


          ]).draw();
        }
      }
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log("Error:", textStatus); // Imprimir el estado del error
      console.log("Código de estado HTTP:", xhr.status); // Imprimir el código de estado HTTP
      console.log("Error arrojado:", errorThrown); // Imprimir detalles adicionales del error
      errorActualizaPagina(textStatus);
    },
    complete: function (xhr, textStatus) {
      // Esta función se ejecutará siempre después de que la solicitud se complete
      console.log("La solicitud se ha completado.");
      console.log("Estado de la solicitud:", textStatus);
    }
  });
}

function verPensionado(json) {

  console.log(json.telefono);
  $("#tablaPensionado").hide();
  $("#formulario").show();
  $("#otraCantidad").prop("readonly", true);

  $('#nombre').val(json.nombre);
  $('#fechaRegistro').val(json.fecharegistro);
  $('#telefono').val(json.telefono);
  $('#rfc').val(json.rfc);
  $('#correo').val(json.correo);
  $('#calle').val(json.calle);
  $('#colonia').val(json.colonia);
  $('#delegacion').val(json.delegacion);
  $('#marca').val(json.marcaauto);
  $('#placas').val(json.placas);
  $('#color').val(json.color);
  $('#modelo').val(json.modelo);
  $('#otraCantidad').val(json.importe);
  $('#idEstacionamiento').val(json.idestacionamiento);
  pensionadoid = json.idpensionado;
  $("#guardarPensionado :input:not(#btn-sumit, #cancelar,#btn-editar)").prop("disabled", true);
  $("#btn-sumit").hide();
  $("#btn-editar").show();

}

function eliminar(id) {
  console.log(id);
  // Dividir la cadena en base al guion "-"
  let partes = id.split("-");
  // Crear un objeto JSON con las propiedades a y b
  let json = {
    idPensionado: parseInt(partes[0]),
    idEstacionamiento: parseInt(partes[1]),
    nombre: partes[2]
  };

  Swal.fire({
    title: "Que opcion desea realizar ?",
    text: "¿Esta seguro que desea dar de baja a este Usuario " + partes[2] + " ?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: " Si deseo eliminar"
  }).then((result) => {
    if (result.isConfirmed) {
      $.ajax({
        type: "GET",
        url: "/pensionado/delete/" + JSON.stringify(json),
        success: function (jsonRest) {
          var json = JSON.parse(jsonRest);
          if (json.status == "true") {
            success(json.mensaje);
          } else {
            error(json.mensaje);
          }
        },
        error: function (jqXHR, textStatus, errorThrown) {
          console.log("Error:", textStatus); // Imprimir el estado del error
          console.log("Código de estado HTTP:", xhr.status); // Imprimir el código de estado HTTP
          console.log("Error arrojado:", errorThrown); // Imprimir detalles adicionales del error
          errorActualizaPagina(textStatus);
        },
        complete: function (xhr, textStatus) {
          // Esta función se ejecutará siempre después de que la solicitud se complete
          console.log("La solicitud se ha completado.");
          console.log("Estado de la solicitud:", textStatus);
        }
      });
    }
  });



}

function cobrar(jsonParam) {
  $.ajax({
    type: "GET",
    url: "/pensionado/getMesesPAgo/" + JSON.stringify(jsonParam),
    success: function (jsonRest) {
      var json = JSON.parse(jsonRest);
      if (json.status == "true") {
        var lista = JSON.parse(json.object);
        $('#datosUsuario').text(JSON.stringify(jsonParam.nombre) + ', Auto ' + JSON.stringify(jsonParam.marcaauto) + ', Placas ' + JSON.stringify(jsonParam.placas) + ', fecha Pago ' + JSON.stringify(jsonParam.fechapago));
        $('#idU').text(jsonParam.idpensionado);
        pagosPensionado(lista);
      } else {
        error(json.mensaje);
      }
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log("Error:", textStatus); // Imprimir el estado del error
      console.log("Código de estado HTTP:", xhr.status); // Imprimir el código de estado HTTP
      console.log("Error arrojado:", errorThrown); // Imprimir detalles adicionales del error
      errorActualizaPagina(textStatus);
    },
    complete: function (xhr, textStatus) {
      // Esta función se ejecutará siempre después de que la solicitud se complete
      console.log("La solicitud se ha completado.");
      console.log("Estado de la solicitud:", textStatus);
    }
  });

}

function pagosPensionado(lista) {

  $('#tablaPensionado').hide();
  $('#newPensionado').hide();
  $('#pagos').show();
 var counPendientes=0; 
  dataTableMesPagos.clear().draw();

  for (let i = 0; i < lista.length; i++) {

    var bloqueoBoton = '';
    if (lista[i].statusPago == true) {
      bloqueoBoton = 'disabled';
    }else{
	  counPendientes++;
    }   
    dataTableMesPagos.row.add([
      lista[i].mes,
      lista[i].mensaje,
      lista[i].importe,
      '<button ' + bloqueoBoton + ' "style="padding: 3px 3px; font-size: 8px; "  title="Agregar Pago "   class="agregarPago btn btn-info'
      + ' " data-id=\'' + JSON.stringify(lista[i]) + '\'><i  style="font-size:11px">Agregar Pago</i></button>'
    ]).draw();
  }
      if(counPendientes<1){
		info("No existen pagos vencidos...");	
      }
}
function agregarPago(id) {

  var mes = id.mes;
  var fila = dataTablePagosAgregados.row(function (idx, data, node) {
    var mesEnData = data[0];
    return mesEnData === mes;
  });

  if (fila.length >= 1) {
    Swal.fire("Ya se tiene un pago agregado con el mismo mes seleccionado");
  } else {
    dataTablePagosAgregados.row.add([
      id.mes,
      id.mensaje,
      id.importe,
      '<button  "style="padding: 3px 3px; font-size: 8px; "  title="Eliminar Pago"   class="eliminarRow btn btn-danger'
      + ' " data-id=\'' + JSON.stringify(id) + '\'><i  style="font-size:11px">eliminar Pago</i></button>'
    ]).draw();
  }

  actualizarCobro();
}
function eliminarPago(id) {
  alert(JSON.stringify(id));
  var mes = id.mes;
  var fila = dataTablePagosAgregados.row(function (idx, data, node) {
    var mesEnData = data[0];
    return mesEnData === mes;
  });
  fila.remove().draw();
  actualizarCobro();
}
function actualizarCobro() {
  var importe = 0.00;
  var datosFilas = dataTablePagosAgregados.rows().data();
  // Recorre cada fila y accede a los datos
  datosFilas.each(function (fila, indice) {
    importe += fila[2];
  });

  console.log('importe :: ' + importe);
  $('#importeCobro').val(importe);
}

function guardarPagoPension(jsonData) {
  jQuery.ajax({
    url: "/pensionado/savePago",
    type: 'POST',
    data: JSON.stringify(jsonData),
    dataType: 'json',
    cache: false,
    contentType: false,
    processData: false,
    complete: function (xhr, textStatus) {
      console.log("La solicitud se ha completado.");
      console.log("Estado de la solicitud:", textStatus);
    },
    success: function (jsonResponse) {
      if (jsonResponse.status == 'true') {
        success(jsonResponse.mensaje);
      } else {
        error(jsonResponse.mensaje);
      }
    },
    error: function (xhr, textStatus, errorThrown) {
      console.log("Error:", textStatus); // Imprimir el estado del error
      console.log("Código de estado HTTP:", xhr.status); // Imprimir el código de estado HTTP
      console.log("Error arrojado:", errorThrown); // Imprimir detalles adicionales del error
    },
  });

}



