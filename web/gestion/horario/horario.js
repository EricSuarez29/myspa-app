
var horario;
function inicializarModuloHorario() {
    $('#divDetalleHorario').hide();
    refrescarTablaHorario();
}

function guardarHorario() {
    //var pos = -1;
    var horario = new Object();
    horario.IdHorario = parseInt($('#txtCodigo').val());
    horario.horaInicio = $('#txtHoraInicio').val();
    horario.horaFin = $('#txtHoraFin').val();
    pos = buscarPosicionHorarioPorId(horario.id);

    $.ajax({
        type: 'POST',
        url: 'api/horario/save',
        data: {
            horario: JSON.stringify(horario)
        }

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
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Realizado!',
                text: "Los datos del empleado se han guardado correctamente.",
                showConfirmButton: false,
                timer: 1500
            });
            limpiarFormularioHorario();
            setFormularioDetalleHorarioVisible(false);
            refrescarTablaHorario();
        }
    });

}

function eliminarHorario() {
    var idHorario = parseInt($('#txtCodigo').val());
    // var pos = buscarPosicionHorarioPorId(id);
    $.ajax({
        type: 'POST',
        url: `api/horario/delete`,
        data: {
            id: idHorario
        }
    }).done(function (data) {
        if (data.error) {
            Swal.fire({
                position: 'center',
                icon: 'warning',
                title: 'Horario No Encontrado',
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
            limpiarFormularioHorario();
            refrescarTablaHorario();
            setFormularioDetalleHorarioVisible(false);
        }
    });

}

function limpiarFormularioHorario() {
    $('#txtCodigo').val('');
    $('#txtHoraInicio').val('');
    $('#txtHoraFin').val('');
}

function refrescarTablaHorario() {
    // Aqui guardaremos el contenido de la tabla
    var contenido = '';
    $.ajax({
        type: 'GET',
        url: 'api/horario/getAll'
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

            horarios = data;
            // Recorremos el arreglo
            for (var i = 0; i < horarios.length; i++) {
                contenido += '<tr onclick="mostrarDetalleHorario(' + horarios[i].IdHorario + ');">' +
                        '<td>' + horarios[i].IdHorario + '</td>' +
                        '<td>' + horarios[i].horaInicio + '</td>' +
                        '<td>' + horarios[i].horaFin + '</td>' +
                        '</tr>';
            }

            // Insertamos el contenido de la tabla
            $('#tbodyHorarios').html(contenido);
        }
    });
}

function buscarPosicionHorarioPorId(idHorario) {
    for (var i = 0; i < horarios.length; i++) {
        if (horarios[i].IdHorario === idHorario)
            return i;
    }
    return -1;
}

function mostrarDetalleHorario(idHorario) {
    var pos = buscarPosicionHorarioPorId(idHorario);
    $('#txtCodigo').val(horarios[pos].IdHorario);
    $('#txtHoraInicio').val(horarios[pos].horaInicio);
    $('#txtHoraFin').val(horarios[pos].horaFin);
    setFormularioDetalleHorarioVisible(true);
}

function setFormularioDetalleHorarioVisible(valor) {
    if (valor) {
        $('#divTablaHorario').removeClass("col-12");
        $('#divTablaHorario').addClass("col-8");
        $('#divDetalleHorario').show();
    } else {
        $('#divDetalleHorario').hide();
        $('#divTablaHorario').removeClass("col-8");
        $('#divTablaHorario').addClass("col-12");
    }
}



