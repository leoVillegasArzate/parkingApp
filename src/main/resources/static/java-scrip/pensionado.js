$(document).ready(function () {
  $("#formulario").hide();

  $("#newPensionado").click(function (e) {
    $("#guardarPensionado input").val(""); // Limpiar campos del Formulario 2
    $("#tablaPensionado").hide();
    $("#formulario").show();
    $("#fechaRegistro").val(getFecha());
    getImportePension();
    $("#otraCantidad").prop("readonly", true);
  });
  $("#cancelar").click(function () {
    cancelar();
  });
$('#importe').change(function (e) {
   var valor=$('#importe').val();
      alert(valor);
   if (valor==='OtraCantidad') {
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
        $("#" + campo).next(".mensaje-error").remove(); // Eliminar mensaje de error anterior (si existe)
        $("#" + campo).after('<span class="mensaje-error" style="color: red;">Los campos no deben estar vacios</span>');
      });
    } else {

            if (!/^\d{10}$/.test(jsonData.telefono)) {
              $("#" + Object.keys(jsonData)[2]).next(".mensaje-error").remove(); // Eliminar mensaje de error anterior (si existe)
              $("#" +Object.keys(jsonData)[2]).after('<span class="mensaje-error" style="color: red;">No es un formato de Telefono 10 digitos valido </span>');
              
            }
            if (!/^[0-9a-zA-Z]{12,13}$/.test(jsonData.rfc)) {
              $("#" +Object.keys(jsonData)[3]).next(".mensaje-error").remove(); // Eliminar mensaje de error anterior (si existe)
              $("#" + Object.keys(jsonData)[3]).after('<span class="mensaje-error" style="color: red;">No cumple con las caracteristicas debe tener como minimo 12 y maximo 13 caracteres </span>');
              
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
          savePensionado(JSON.stringify(jsonData));
        }
      });
    }
  });

  $(".texto").on("keyup", function () {
    var idDelElemento = $(this).attr("id");
    $("#" + idDelElemento)
      .next(".mensaje-error")
      .remove(); // Eliminar mensaje de error anterior (si existe)

    this.value = this.value.toUpperCase();
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

function savePensionado(pensionado) {
  jQuery.ajax({
    url: "/pensionado/save",
    type: "POST",
    dataType: "json",
    data: pensionado,
    complete: function (xhr, textStatus) {
      console.log("La solicitud se ha completado.");
      console.log("Estado de la solicitud:", textStatus);
    },
    success: function (jsonRest) {
      var json = JSON.parse(jsonRest);
      if (json.status == "true") {
        success(json.mensaje);
      } else {
        error(json.mensaje);
      }
    },
    error: function (xhr, textStatus, errorThrown) {
      console.log("Error:", textStatus); // Imprimir el estado del error
      console.log("Código de estado HTTP:", xhr.status); // Imprimir el código de estado HTTP
      console.log("Error arrojado:", errorThrown); // Imprimir detalles adicionales del error
    },
  });
}

