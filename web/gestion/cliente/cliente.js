var clientes;

function guardarCliente() {
    var cliente = new Object();
    var persona = new Object();
    var usuario = new Object();

    cliente.idCliente = parseInt($('#txtCod').val()) || 0;
    cliente.numeroUnico = $('#txtNumeroUnico').val() || "0";
    persona.nombre = $('#txtNombre').val();
    persona.apellidoPaterno = $('#txtApeP').val();
    persona.apellidoMaterno = $('#txtApeM').val();
    persona.rfc = $('#txtRFC').val();
    persona.domicilio = $('#txtDomicilio').val();
    persona.telefono = $('#txtTel').val();
    cliente.correo = $('#txtEmail').val();
    persona.genero = $('#cmbGenero').val();
    usuario.nombreUsuario = $('#txtNU').val();
    usuario.contrasegna = $('#txtContrasennia').val();
    usuario.rol = "Cliente";
    cliente.estatus = 1;

    cliente.usuario = usuario;
    cliente.persona = persona;

    $.ajax({
        url: 'api/cliente',
        type: 'POST',
        data: JSON.stringify(cliente)
    }).done(function (data) {
        if (data.error) {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Error!',
                text: data.error,
                showConfirmButton: false,
                timer: 1500
            });
        } else {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Realizado!',
                text: data.message,
                showConfirmButton: false,
                timer: 1500
            });
        }
        refrescarTablaClientes();
    });

}

function eliminarCliente() {

    var id = $('#txtCod').val();

    $.ajax({
        url: `api/cliente/${id}`,
        type: 'DELETE'
    }).done(function (data) {
        if (data.error) {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Error!',
                text: data.error,
                showConfirmButton: false,
                timer: 1500
            });

        } else {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Realizado!',
                text: data.message,
                showConfirmButton: false,
                timer: 1500
            });
        }

        refrescarTablaClientes();
        limpiarFormularioCliente();
    });

}

function limpiarFormularioCliente() {
    $('#txtCod').val('');
    $('#txtNombre').val('');
    $('#txtApeP').val('');
    $('#txtApeM').val('');
    $('#txtRFC').val('');
    $('#txtDomicilio').val('');
    $('#txtTel').val('');
    $('#txtEmail').val('');
    $('#txtNU').val('');
    $('#txtContrasennia').val('');
    $('#cmbGenero').val('');
    $('#txtNumeroUnico').val('');
}

function refrescarTablaClientes() {
    var contenido = "";
    $.ajax({
        url: `api/cliente`,
        type: 'GET'
    }).done(function (data) {
        clientes = data;
        for (var i = 0; i < clientes.length; i++) {
            var persona = clientes[i].persona;
            var usuario = clientes[i].usuario;
            
//            <th>#</th>
//                            <th>Nombre</th>
//                            <th>Numero Unico</th>
//                            <th>Apellido Paterno</th>
//                            <th>Apellido Materno</th>
//                            <th>G&eacute;nero</th>
//                            <th>domicilio</th>
//                            <th>Telefono</th>
//                            <th>Email</th>
//                            <th>Usuario</th>
            
            contenido += '<tr onclick="mostrarDetalleCliente(' + clientes[i].idCliente + ');">' +
                    '<td>' + clientes[i].idCliente + '</td>' +
                    '<td>' + persona.nombre + '</td>' +
                    '<td>' + clientes[i].numeroUnico + '</td>' +
                    '<td>' + persona.apellidoPaterno +'</td>' +
                    '<td>' + persona.apellidoMaterno +'</td>' +
                    '<td>' + persona.genero + '</td>' +
                    '<td>' + persona.domicilio + '</td>' +
                    '<td>' + persona.telefono + '</td>' +
                    '<td>' + clientes[i].correo + '</td>' +
                    '<td>' + usuario.nombreUsuario + '</td>' +
                    '</tr>';
        }
        // Insertamos el contenido de la tabla
        $('#tbodyClientes').html(contenido);
    });
}

function buscarPosicionClientePorId(idCliente) {
    for (var i = 0; i < clientes.length; i++) {
        if (clientes[i].idCliente === idCliente)
            return i;
    }
    return -1;
}

function mostrarDetalleCliente(idCliente) {
    var pos = buscarPosicionClientePorId(idCliente);
    var persona = clientes[pos].persona;
    var usuario = clientes[pos].usuario;
    $('#txtCod').val(clientes[pos].idCliente);
    $('#txtNombre').val(persona.nombre);
    $('#txtApeP').val(persona.apellidoPaterno);
    $('#txtApeM').val(persona.apellidoMaterno);
    $('#txtRFC').val(persona.rfc);
    $('#txtDomicilio').val(persona.domicilio);
    $('#txtTel').val(persona.telefono);
    $('#txtEmail').val(clientes[pos].correo);
    $('#txtNU').val(usuario.nombreUsuario);
    $('#txtContrasennia').val(usuario.contrasegna);
    $('#cmbGenero').val(persona.genero);
    $('#txtNumeroUnico').val(clientes[pos].numeroUnico);
    setFormularioDetalleClienteVisible(true);
}

function inicializarModuloCliente() {
    $('#divDetalleCliente').hide();
    refrescarTablaClientes();
}

function setFormularioDetalleClienteVisible(valor) {
    if (valor) {
        $('#divTablaClientes').removeClass("col-12");
        $('#divTablaClientes').addClass("col-8");
        $('#divDetalleCliente').show();
    } else {
        $('#divDetalleCliente').hide();
        $('#divTablaClientes').removeClass("col-8");
        $('#divTablaClientes').addClass("col-12");
    }
}

refrescarTablaClientes();