var banderaPromo=false;

$(document).ready(function () {
     $("input[type=radio][name=tipoPromocion]").prop('checked', false);
     getEstacionamiento();

});





$('#promocion').click(function(){
	
	 $('#promo').css('display', 'inline');	
	 $('#promocion').hide();
     $('#promocionOf').show();
     banderaPromo=true;

});

$('#promocionOf').click(function(){
			
	 $('#promo').css('display', 'none');		
     $('#promocionOf').hide();
     $('#promocion').show();
     $("input[type=radio][name=tipoPromocion]").prop('checked', false);
     $('#text').text('');
     $('#horaPromocion').val('');
     $('#precioPromo').val('');
		banderaPromo=false;
});


$('#automatico').click(function(){
			$('#text').text('Se cobra por hora, cuando el precio  llegue al precio de la promocion se aplica en automatico la promocion.');
			$('#text').append('<br>Cuando se pasa de las horas de promocion se vuelve a cobrar por hora hasta que el precio alcance otra promocion');
			
});
$('#promocionP').click(function(){
         
     $('#text').text('Tendra 2 formas de entrada: ');
     $('#text').append('<br>A)Hora:  se cobrara de acuerdo a la tarifa estblecida.');
     $('#text').append('<br>B)Promocion: se cobrara por promocion, es decir si se pasa una promocion se cobrara la siguiente en automatico.');
     $('#text').append('<br> ejemplo si un cliente tiene una promocion de 5 hrs, el cliente tarda 5 hrs con 30 min o  6 hrs, se cobrara 2 promociones.');
		

});
$('#promocionH').click(function(){
         
     $('#text').text('Tendra 2 formas de entrada: ');
     $('#text').append('<br>A)Hora:  se cobrara de acuerdo a la tarifa estblecida.');
     $('#text').append('<br>B)Promocion: se cobrara la promocion, cuando se pase de la promo se cobrara por hora hasta que alcance otra promocion.');
     $('#text').append('<br> ejemplo si un cliente tiene una promocion de 5 hrs, el cliente tarda 5 hrs con 30 min o  6 hrs, se cobrara  1 promocion + 1 hr.');
     $('#text').append('<br>si el cliente  ocupo mas de 8 hsr son 3 hrs mas de la promocion  se aplicara otra promocion ');

});




$('#btnRegistrarConfiguracion').submit(function(event) {
  event.preventDefault();


    var jsonData = $('#btnRegistrarConfiguracion').serializeArray()
    .reduce(function(a, z) {
      a[z.name] = z.value;
      return a;
    }, {});
    
    var promo = { 'tienePromo': banderaPromo }; 
    $.extend(jsonData, promo);
    alert(JSON.stringify(jsonData))
   var mensajeError='';
   var mensajeError1='';
    $('.numero').each(function () {	
	  
	  if (this.value == undefined || this.value == null || String(this.value).trim().length < 1) {
		 mensajeError1+=this.name+", ";
       }else{
	       if (!isDouble( this.value)) {							
				  mensajeError+=this.name+", ";				 				    
				  $('#'+this.name).css('border-bottom', '0.25rem solid red');
      		 }
		}		
    });
    
    
	if (banderaPromo) {
		$('.numeroPromo').each(function() {

			if (this.value == undefined || this.value == null || String(this.value).trim().length < 1) {
				mensajeError1 += this.name + ", ";
			} else {
				if (!isDouble(this.value)) {
					mensajeError += this.name + ", ";
					$('#' + this.name).css('border-bottom', '0.25rem solid red');
				}
			}
		});
		
		if(!jsonData.hasOwnProperty('tipoPromocion')){
			$('#inputError').text('Si ha seleccionado una promocion debe seleccionar un tipo de promocion que desea aplicar');
			 $('#xx').css({
		         color: 'red'
		       });
			return;
		}		
		
	}
                   
	if (mensajeError1 != '') {
		$('#inputError').text('Estos Campos no deben de estar vacios [ ' + mensajeError1 + ' ]');
		return;
	}
	if (mensajeError != '') {
		$('#inputError').text('Estos Campos deben de ser numero [ ' + mensajeError + ' ]');
		return;
	}

	if ((( parseInt(jsonData.fraccion1) + parseInt(jsonData.fraccion2) + parseInt(jsonData.fraccion3) + parseInt(jsonData.fraccion4))-parseInt(jsonData.hora)) != 0) {
		$('#inputError').text('El importe de fracciones no coicide  con el importe de la hora, favor de verificar de nuevo');
		return;
	}
 
 if ( 
 	  (jsonData.estacionamiento == undefined || jsonData.estacionamiento == null || String(jsonData.estacionamiento).trim().length < 1))
	{		
		$('#inputError').text('Debe seleccionar una opcion para Estacionamiento y ');
		return;
	}  
		jQuery.ajax({
			url: '/configuracion/guardar',
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
							
			  if(jsonResponse.status=='true'){
				 success(jsonResponse.mensaje);				
			}else{
				error(jsonResponse.mensaje);
			}
			
			},
			error: function(textStatus, errorThrown) {
				
				error('error '+textStatus+" "+errorThrown);
			},
			complete: function(xhr, textStatus) {
				console.log(xhr+' '+textStatus);
			},
		});
});


function getEstacionamiento(){

	jQuery.ajax({
	  url: '/configuracion/getEstacionamiento',
	  type: 'GET',
			cache: false,
			contentType: false,
			processData: false,
	  complete: function(xhr, textStatus) {
	   console.log(xhr+' '+textStatus);
	  },
	  success: function(jsonResponse) {		
		var json = JSON.parse(jsonResponse);
			if (json.status == 'true') {
				var lista = JSON.parse(json.object);
				console.log("valor :: "+lista.length);
                for(var j=0; j<lista.length; j++){
	
				 console.log(lista[j].id+" "+lista[j].nombre );
				     $('#estacionamiento').append($('<option>',
			     		{
					        value: lista[j].id,
					        text : lista[j].nombre 
			    		}));
					}
			} else {
			    alert("no se recupero  lista de etsacionamiewntos")
			}
	  },
	  error: function(xhr, textStatus, errorThrown) {
	   error('error '+textStatus+" "+errorThrown);
	  }
	});
	
}



















