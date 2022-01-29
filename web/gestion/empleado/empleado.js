
var empleados;
var codigoImagen;

function guardarEmpleado() {

    var empleado = new Object(),
            persona = new Object(),
            usuario = new Object();

    empleado.id = parseInt($('#txtCod').val()) || 0;
    persona.nombre = $('#txtNombre').val();
    persona.apellidoPaterno = $('#txtApeP').val();
    persona.apellidoMaterno = $('#txtApeM').val();
    persona.rfc = $('#txtRFC').val();
    persona.domicilio = $('#txtDomicilio').val();
    persona.telefono = $('#txtTel').val();
    empleado.puesto = $('#txtP').val();


    empleado.rutaFoto = $('#txtRutaFoto').val();
    empleado.foto = codigoImagen || "";
    usuario.nombreUsuario = $('#txtNU').val();
    usuario.contrasegna = $('#txtContrasennia').val();
    usuario.rol = "Empleado";
    persona.genero = $('#cmbGenero').val();
    empleado.estatus = 1;
    empleado.persona = persona;
    empleado.usuario = usuario;

    $.ajax({
        type: 'POST',
        url: 'api/empleados',
        contentType: 'application/json',
        data: JSON.stringify(empleado)
    }).done(function(data) {
        if (data.error) {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Error',
                text: data.error,
                showConfirmButton: false,
                timer: 1500
            });
        } else {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Realizado!',
                text: "Los datos del empleado se han guardado correctamente.",
                showConfirmButton: false,
                timer: 1500
            });
            limpiarFormulario();
            setFormularioDetalleVisible(false);
            refrescarTablaEmpleados();
        }
    });





}



function eliminarEmpleado() {

    //recuperamos el id del producto a eliminar

    var id = parseInt($('#txtCod').val());
    
    $.ajax({
        type: 'DELETE',
        url: `api/empleados/${id}`
    }).done(function(data){
        if(data.error){
            Swal.fire({
                position: 'center',
                icon: 'warning',
                title: 'Empleado No Encontrado',
                text: data.error,
                showConfirmButton: false,
                timer: 1500
            });
        }else{
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Realizado!',
                text: data.message,
                showConfirmButton: false,
                timer: 1500
            });
            limpiarFormulario();
            refrescarTablaEmpleados();

        }
    });

}

function limpiarFormulario() {

    $('#txtCod').val('');
    $('#txtNombre').val('');
    $('#txtApeP').val('');
    $('#txtApeM').val('');
    $('#txtRFC').val('');
    $('#txtDomicilio').val('');
    $('#txtTel').val('');
    $('#divFoto').hide();
    $('#txtP').val('');
    $('#txtRutaFoto').val('');
    $('#txtNU').val('');
    $('#txtContrasennia').val('');
    $('#cmbGenero').val('');


}

function refrescarTablaEmpleados() {
    // aqui guardaremos el ccontenido de la tabla
    var contenido = '';

    $.ajax({
        type: 'GET',
        url: 'api/empleados'
    }).done(function (data) {
        if (data.error) {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Error',
                text: data.error,
                showConfirmButton: false,
                timer: 1500
            });
        } else {
            empleados = data;
            for (var i = 0; i < empleados.length; i++) {

                contenido += '<tr onclick="mostrarDetalleEmpleado(' + empleados[i].id + ');">' +
                        '<td>' + empleados[i].id + '</td>' +
                        '<td>' + empleados[i].persona.nombre + '</td>' +
                        '<td>' + empleados[i].persona.apellidoPaterno + '</td>' +
                        '<td>' + empleados[i].persona.apellidoMaterno + '</td>' +
                        '<td>' + empleados[i].persona.rfc + '</td>' +
                        '<td>' + empleados[i].persona.domicilio + '</td>' +
                        '<td>' + empleados[i].persona.telefono + '</td>' +
                        '<td>' + empleados[i].puesto + '</td>' +
                        '<td>' + empleados[i].usuario.nombreUsuario + '</td>' +
                        '<td>' + empleados[i].persona.genero + '</td>' +
                        '<td>' + empleados[i].estatus + '</td>' +
                        '</tr>';
            }
            $('#tbodyEmpleados').html(contenido);
        }
    });

}

function mostrarDetalleEmpleado(idEmpleado) {
    // buscamos la posicion del empleado en base a su id
    var pos = buscarPosicionPorId(idEmpleado);

    // llenamos el formulario con los datos del empleado


    var persona = empleados[pos].persona;
    var usuario = empleados[pos].usuario;

    $('#txtCod').val(empleados[pos].id);
    $('#txtNombre').val(persona.nombre);
    $('#txtApeP').val(persona.apellidoPaterno);
    $('#txtApeM').val(persona.apellidoMaterno);
    $('#txtRFC').val(persona.rfc);
    $('#txtDomicilio').val(persona.domicilio);
    $('#txtTel').val(persona.telefono);
    $('#txtP').val(empleados[pos].puesto);
    $('#txtNU').val(usuario.nombreUsuario);
    $('#txtContrasennia').val(usuario.contrasegna);
    $('#cmbGenero').val(persona.genero);
    $('#divFoto').show();
    $('#img_empleado').prop('src', 'data:image/png;base64,' + empleados[pos].foto);

    setFormularioDetalleVisible(true);
}


//busca la posicion de un objeto producto dentro de un arreglo
//con base en el id del producto
//si no se encuentra el producto con ese id la funcion devolvera -1
function buscarPosicionPorId(id) {

    // recorremos el arreglo porsicion por posicion
    for (var i = 0; i < empleados.length; i++) {
        //evaluamos si el id del producto en la posicion actual 
        //es igual al id del producto que nos pasan como paramatro

        if (empleados[i].id === id)
            return i; // si los id's coinciden regresamos la posicion
    }
    //si llegamos a este punto signifca que no encotraos u producto con el id buscado en este caso
    //devolvemos -1
    return -1;

}

function inicializarModuloEmpleado() {

    $('#divDetalleEmpleado').hide();
    refrescarTablaEmpleados();

}
// esta funcion muestra y oculta el formulario detalle
// dependiendo del valor si es true el formulario se muestra
// si es false el formulario e oculta
function setFormularioDetalleVisible(valor) {

    if (valor) {
        $('#divTablaEmpleado').removeClass("col-12");
        $('#divTablaEmpleado').addClass("col-8");
        $('#divDetalleEmpleado').show();

    } else {
        $('#divDetalleEmpleado').hide();
        $('#divTablaEmpleado').removeClass("col-8");
        $('#divTablaEmpleado').addClass("col-12");
    }
}

function cargarFotografia(){
    var inputFile = document.getElementById('imgEmpleado');
    
    if(inputFile.files && inputFile.files[0]){
        var reader = new FileReader();
        
        reader.onload = function(e){
            var fotoB64 = e.target.result;
            $('#img_empleado').attr("src", fotoB64);
            codigoImagen = fotoB64.substring(22, fotoB64.length);
            
        }
        
        reader.readAsDataURL(inputFile.files[0]);
    }
}
