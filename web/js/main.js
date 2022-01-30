function cargarModuloProducto() {
    $.ajax({
        context: document.body,
        url: "gestion/producto/producto.html"
    }).done(function (data) {

        //document.getElementById("contenedorPrincipal").innerHTML = data;

        $("#contenedorPrincipal").html(data); //este hace lo mismo que el de arriba
    });

}

function cargarModuloCliente() {
    $.ajax({
        context: document.body,
        url: "gestion/cliente/cliente.html"
    }).done(function (data) {

        // document.getElementById("contenedorPrincipal").innerHTML = data;
        $("#contenedorPrincipal").html(data); //este hace lo mismo que el de arriba
    });

}

function cargarModuloSucursal() {

    $.ajax({
        context: document.body,
        url: "gestion/sucursal/sucursal.html"
    }).done(function (data) {

        // document.getElementById("contenedorPrincipal").innerHTML = data;
        $("#contenedorPrincipal").html(data); //este hace lo mismo que el de arriba
    });


}

function cargarModuloEmpleado() {
    $.ajax({
        context: document.body,
        url: "gestion/empleado/empleado.html"
    }).done(function (data) {

        // document.getElementById("contenedorPrincipal").innerHTML = data;
        $("#contenedorPrincipal").html(data); //este hace lo mismo que el de arriba
    });

}

function cargarModuloSala() {
    $.ajax({
        context: document.body,
        url: "gestion/sala/sala.html"
    }).done(function (data) {

        // document.getElementById("contenedorPrincipal").innerHTML = data;
        $("#contenedorPrincipal").html(data); //este hace lo mismo que el de arriba
    });

}

function cargarModuloHorario() {
    $.ajax({
        context: document.body,
        url: "gestion/horario/horario.html"
    }).done(function (data) {

        // document.getElementById("contenedorPrincipal").innerHTML = data;
        $("#contenedorPrincipal").html(data); //este hace lo mismo que el de arriba
    });

}


function cerrarModulo() {
    $('#contenedorPrincipal').html('');
}

function cerrarSesion() {
    Swal.fire({
        title: '¿Estas seguro?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#B789B1',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: 'No'
    }).then((result) => {
        if (result.isConfirmed) {
            const empleado = JSON.parse(sessionStorage.getItem("empleado"));
            $.ajax({
                url: 'api/auth/logout',
                type: 'POST',
                data: {
                    user: JSON.stringify(empleado.usuario)
                }
            }).done(function (res) {

                sessionStorage.clear();
                window.location.href = "index.html";
            })
        }
    })

}


function inicializarPagina() {
    var empleado = JSON.parse(sessionStorage.getItem('empleado'));

    $('#lbEmpleado').html(`  ${empleado.persona.nombre} ${empleado.persona.apellidoPaterno}`);
}

inicializarPagina();
