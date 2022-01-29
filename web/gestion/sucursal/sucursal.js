var sucursales;


function inicializarModuloSucursal() {
    $('#divDetalleSucursal').hide();
    refrescarTablaSucursal();
}

function guardarSucursal() {
    var sucursal = new Object();
    sucursal.id = parseInt($('#txtCodigo').val()) || 0;
    sucursal.nombre = $('#txtNombre').val();
    sucursal.latitud = parseFloat($('#txtLatitud').val());
    sucursal.longitud = parseFloat($('#txtLatitud').val());
    sucursal.estatus = 1;
    sucursal.domicilio = "ninguno";

    $.ajax({
        type: 'POST',
        url: 'api/sucursal',
        data: sucursal
    }).done((data) => {
        if (data.error) {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Ocurrio un Error!',
                text: data.error,
                showConfirmButton: false,
                timer: 1500
            });
        } else {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Realizado!',
                text: "Los datos de la sucursal se han guardado correctamente.",
                showConfirmButton: false,
                timer: 1500
            });
        }
        
        limpiarFormularioSucursal();
        refrescarTablaSucursal();
    });
    
}

function eliminarSucursal() {
    var id = parseInt($('#txtCodigo').val());
    $.ajax({
        type: 'DELETE',
        url: 'api/sucursal/' + id
    }).done(function (data) {
        if (!data.error) {
            limpiarFormularioSucursal();
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Realizado!',
                text: data.message,
                showConfirmButton: false,
                timer: 1500
            });
            refrescarTablaSucursal();
        } else {
            Swal.fire({
                position: 'center',
                icon: 'warning',
                title: 'Sucursal No Encontrada',
                showConfirmButton: false,
                timer: 1500
            });
        }
    })

}

function limpiarFormularioSucursal() {
    $('#txtCodigo').val('');
    $('#txtNombre').val('');
    $('#txtLatitud').val('');
    $('#txtLongitud').val('');
}

function refrescarTablaSucursal() {
    // Aqui guardaremos el contenido de la tabla
    var contenido = '';

    $.ajax({
        type: 'GET',
        url: 'api/sucursal'
    }).done((data) => {
        if (data.error) {
            return Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Realizado!',
                text: data.error,
                showConfirmButton: false,
                timer: 1500
            });
        } else {
            // Recorremos el arreglo
            sucursales = data;
            for (var i = 0; i < sucursales.length; i++) {
                contenido += '<tr onclick="mostrarDetalleSucursal(' + sucursales[i].id + ');">' +
                        '<td>' + sucursales[i].id + '</td>' +
                        '<td>' + sucursales[i].nombre + '</td>' +
                        '<td>' + sucursales[i].latitud + '</td>' +
                        '<td>' + sucursales[i].longitud + '</td>' +
                        '<td>' + sucursales[i].estatus + '</td>' +
                        '</tr>';
            }

            // Insertamos el contenido de la tabla
            $('#tbodySucursales').html(contenido);
        }
    });


}

function buscarPosicionSucursalPorId(idSucursal) {
    for (var i = 0; i < sucursales.length; i++) {
        if (sucursales[i].id === idSucursal)
            return i;
    }
    return -1;
}

function mostrarDetalleSucursal(idSucursal) {
    if(idSucursal){
        var pos = buscarPosicionSucursalPorId(idSucursal);
        $('#txtCodigo').val(sucursales[pos].id);
        $('#txtNombre').val(sucursales[pos].nombre);
        $('#txtLatitud').val(sucursales[pos].latitud);
        $('#txtLongitud').val(sucursales[pos].longitud);    
    }else {
        limpiarFormularioSucursal();
    }
    setFormularioDetalleSucursalVisible(true);
}

function setFormularioDetalleSucursalVisible(valor) {
    if (valor) {
        $('#divTablaSucursal').removeClass("col-12");
        $('#divTablaSucursal').addClass("col-8");
        $('#divDetalleSucursal').show();
    } else {
        $('#divDetalleSucursal').hide();
        $('#divTablaSucursal').removeClass("col-8");
        $('#divTablaSucursal').addClass("col-12");
    }
}



