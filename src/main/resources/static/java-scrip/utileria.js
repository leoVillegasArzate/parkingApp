function tiempoPeticion(mensaje) {

	Swal.fire({
		icon: "info",
		title: mensaje,
		html: "Realizando peticion...",
		allowEscapeKey: false,
		allowOutsideClick: false,
		closeOnEsc: false,
		allowOutsideClick: false, // evita es esc
		timerProgressBar: true,
		didOpen: () => {
			Swal.showLoading()
		}
	})
}


function success(mensaje){		
Swal.fire({
    icon: 'success',  // incono
    title: 'Operacion Exitosa!', //titulo de html
    html:mensaje,
    confirmButtonColor: '#3085d6',
	confirmButtonText: 'ok', // verifca que se un ok para cerrar
	closeOnClickOutside: false, // cierra cuando se pica a fuera
	closeOnEsc: false,
	allowOutsideClick: false, // evita es esc
}).then(function(result) { // uba operacion que no ha sido
	if (result.isConfirmed) {
		setTimeout(function() {
			window.location.reload();
		}, 200);
	}
});
}

function errorActualizaPagina(mensaje){		
Swal.fire({
    title: 'Operacion Erronea!',
    icon: 'error',
    html:mensaje,
    confirmButtonColor: '#3085d6',
	confirmButtonText: 'ok', // verifca que se un ok para cerrar
	closeOnClickOutside: false, // cierra cuando se pica a fuera
	closeOnEsc: false,
	allowOutsideClick: false, // evita es esc
}).then(function(result) { // uba operacion que no ha sido
	if (result.isConfirmed) {
		setTimeout(function() {
			window.location.reload();
		}, 200);
	}
});
}


function error(mensaje){	
   Swal.fire({
  title: 'Operacion Erronea!',
  icon: 'error',
  confirmButtonColor: '#3085d6',
  closeOnEsc: false,
  closeOnClickOutside: false,
  allowOutsideClick: false,  
  html:mensaje,
  confirmButtonText: 'OK'
})
}

function isDouble(value) {

    var RegExpDouble = /^(\-)?[0-9]{0,}([.,][0-9]{1,})?$/;
    return RegExpDouble.test(value);
}

function isInt(value) {
    var patt = new RegExp('^[^?\.?\s]*[0-9]$');
    var res = patt.test(value);
    return res;
}

function formatoMoneda(x) {
    try{
        return "$"+parseFloat(x).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }catch(err){
        console.log("Se genero un error al dar formato a la cadena ("+x+") como moneda:: "+err);
        return x;
    }
}

// Valida input dentro de un form-group especifico
function validaForm(idForm) {
	var error = '';
	$("div[class='form-group " + idForm + "']").children().each(
	    function() {
	        if (this.type != undefined) {
	            if (this.value == undefined || this.value == null || String(this.value).trim().length < 1) {
	                error += "<br> Es necesario completar " + $(this).attr("placeholder") + "\n";
	            }
	        }
	    });
	return error;
}