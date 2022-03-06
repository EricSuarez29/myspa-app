var salas;
var sucursales;

function inicializarModuloSala() {
	$('#divDetalleSala').hide();
	refrescarTablaSala();
}

function sanitization(text){
    return text.replaceAll(/[()`\\";._-]/ig, "");
}

function normalizar(text){
    text = text.toUpperCase();
    text = text.replaceAll("Á", "A");
    text = text.replaceAll("É", "E");
    text = text.replaceAll("Í", "I");
    text = text.replaceAll("Ó", "O");
    text = text.replaceAll("Ú", "U");
    return text;
}

function formatText(text){
    return sanitization(normalizar(text));
}

function guardarSala() {
	var sala = new Object();

	sala.id = parseInt($('#txtCod').val() || "0");
	sala.nombre = formatText($('#txtNombre').val());
	sala.descripcion = formatText($('#txtDescripcion').val());
        sala.foto = getImageSucursal();
	sala.idSucursal = getIdSucursal();
        $.ajax({
            url: 'api/salas',
            type: 'POST',
            data: {
                sala: JSON.stringify(sala),
                token: sessionStorage.getItem('token')
            }
        }).done(function(res){
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Realizado!',
                text: res.message,
                showConfirmButton: false,
                timer: 1500
            });
            refrescarTablaSala();
            limpiarFormulario();
            setFormularioDetalleVisible(false);
        }).catch(function(err){
            console.log(err);
        });
	
}

function getImageSucursal(){
    return '';
}

function getIdSucursal(){
    return 1;
}

function eliminarSala() {

	var id = $('#txtCod').val() || 0;
	
        $.ajax({
            url: `api/salas/${id}`,
            type: 'DELETE',
            data: {
                token: sessionStorage.getItem('token')
            }
        }).done(function(res){
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Realizado!',
                text: res.message,
                showConfirmButton: false,
                timer: 1500
            });
            refrescarTablaSala();
            limpiarFormulario();
            setFormularioDetalleVisible(false);
        }).catch(function(err){
            console.log(err);
        });

}


function limpiarFormulario() {

	$('#txtCod').val('');
	$('#txtNombre').val('');
	$('#txtDescripcion').val('');
	$('#divFoto').hide();
	$('#txtSucursal').val('');

}

async function mostrarSucursales(){
    
    var res = await fetch('api/sucursal'),
            json = await res.json();
    sucursales = json;
    
    var content = '<option value="0">Selecione una sucursal</option>';
    sucursales.forEach(el => content += `<option value="${el.id}">${el.nombre}</option>`);
    $('#slt-sucursales').html(content);
    
}

function refrescarTablaSala() {
	
    $.ajax({
        url: 'api/salas',
        type: 'GET',
        data: {
            token: sessionStorage.getItem('token')
        }
    }).done(function(res){
        var contenido = '';
        salas = res;
        for (var i = 0; i < salas.length; i++) {
                contenido += '<tr onclick="mostrarDetalleSala(' + salas[i].id + ');">' +
                        '<td>' + salas[i].id + '</td>' +
                        '<td>' + salas[i].nombre + '</td>' +
                        '<td>' + salas[i].descripcion + '</td>' +
                        '<td>' + salas[i].idSucursal + '</td>' +
                        '</tr>';
        }
        $('#tbodySalas').html(contenido);
    }).catch(function(err){
        console.log(err);
    });
    
    
}

function mostrarDetalleSala(id) {
        let idSala = buscarPosicionPorId(parseInt(id));
	(async ()=>{
            await mostrarSucursales();
        
            if(idSala !== -1){
                $('#txtCod').val(salas[idSala].id);
                $('#txtNombre').val(salas[idSala].nombre);
                $('#txtDescripcion').val(salas[idSala].descripcion);
                $('#divFoto').show();
                if(salas[idSala].foto) $('#fotoSala').prop('src', 'data:image/png;base64,' + salas[idSala].foto);
                
                var id = salas[idSala].idSucursal;
                if(id)document.querySelector('#slt-sucursales [value="' + id + '"]').selected = true;
            } else {
                limpiarFormulario();
            }


            setFormularioDetalleVisible(true);
        })();
}

//busca la posicion de un objeto sala dentro de un arreglo
//con base en el id de la sala
//si no se encuentra la sala con ese id la funcion devolvera -1
function buscarPosicionPorId(id) {

	// recorremos el arreglo porsicion por posicion
	for (var i = 0; i < salas.length; i++) {
		//evaluamos si el id de la sala en la posicion actual 
		//es igual al id de la sala que nos pasan como paramatro

		if (salas[i].id === id)
			return i; // si los id's coinciden regresamos la posicion
	}
	//si llegamos a este punto signifca que no encotraos una sala con el id buscado en este caso
	//devolvemos -1
	return -1;

}

// esta funcion muestra y oculta el formulario detalle
// dependiendo del valor si es true el formulario se muestra
// si es false el formulario e oculta
function setFormularioDetalleVisible(valor) {

	if (valor) {
		$('#divTablaSala').removeClass("col-12");
		$('#divTablaSala').addClass("col-8");
		$('#divDetalleSala').show();

	} else {
		$('#divDetalleSala').hide();
		$('#divTablaSala').removeClass("col-8");
		$('#divTablaSala').addClass("col-12");
	}
}

//function cargarFotografia(){
//    var inputFile = document.getElementById('imgSala');
//    
//    if(inputFile.files && inputFile.files[0]){
//        var reader = new FileReader();
//        
//        reader.onload = function(e){
//            var fotoB64 = e.target.result;
//            $('#fotoSala').attr("src", fotoB64);
//            codigoImagen = fotoB64.substring(22, fotoB64.length);
//            
//        }
//        
//        reader.readAsDataURL(inputFile.files[0]);
//    }
//}
