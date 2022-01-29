var productos = [];

function inicializarModuloProducto(){
    $('#divDetalleProducto').hide();
    refrescarTablaProducto();
}

/*
    Esta función muestra y oculta el formulario del detalle, dependiendo
    del valor.
    Si valor es true, el formulario se muestra.
    Si el valor es false, el formulario de detalle se oculta.
*/
function setFormularioDetalleVisible(valor){
    if(valor){        
        $('#divTablaProductos').removeClass("col-12");
        $('#divTablaProductos').addClass("col-8");
        $('#divDetalleProducto').show();
    }
    
    else{
        $('#divDetalleProducto').hide();
        $('#divTablaProductos').removeClass("col-8");
        $('#divTablaProductos').addClass("col-12");
    }
}

function guardarProducto() {
    var pos = -1;

    //Generamos nuevo objeto
    var producto = new Object();
    
    //Agregamos sus propiedades y valores:
    if($('#txtCodigo').val().length < 1)
        producto.id = 0;
    else
        producto.id = parseInt($('#txtCodigo').val());
    
    producto.nombre = $('#txtNombre').val();
    producto.marca = $('#txtMarca').val();
    producto.precioUso = parseFloat($('#txtPrecio').val());
    producto.estatus = 1;
    
    $.ajax( {
                type: "POST",
                url: "api/producto/save",
                data: {
                        idProducto : producto.id,
                        nombre : producto.nombre,
                        marca : producto.marca,
                        precioUso : producto.precioUso
                      }
    })
    .done(function (data) {
        //Revisamos si hubo algún error:
        if (data.error != null)
            Swal.fire('Error', data.error, 'warning');
        else {
            refrescarTablaProducto();
            producto = data;
            $('#txtCodigo').val(producto.id);
            limpiarFormularioProducto();
            Swal.fire("Movimiento realizado", "Datos de productos guardados correctamente.", "success");
            setFormularioDetalleVisible(false);
        }
    });
}

function eliminarProducto() {
    //Obtenemos el ID del producto a eliminar
    var idProducto = 0;
    
    if ($('#txtCodigo').val().length > 0) {
        idProducto = parseInt($('#txtCodigo').val());
        $.ajax( {
                    type: "POST",
                    url: "api/producto/delete",
                    async: true,
                    data: {id: idProducto}
        }).done(function(data){
            if(data.error != null)
                Swal.fire('Error', data.error, 'warning');
            else {
                Swal.fire('Movimiento realizado', data.result, 'success');
                limpiarFormularioProducto();
                refrescarTablaProducto();
                setFormularioDetalleVisible(false);
            }
        });
    }
    else {
        Swal.fire('', 'No se ha seleccionado algún registro de producto para eliminar.', 'warning');
    }
}

function limpiarFormularioProducto() {
    $('#txtCodigo').val('');
    $('#txtNombre').val('');
    $('#txtMarca').val('');
    $('#txtPrecio').val('');
}

function refrescarTablaProducto() {
    //Aqui guardamos el contenido de la tabla
    var contenido = '';

    //Hacemos la petición al Servicio REST que nos consulta los productos:
    $.ajax( {
                type: "GET",
                url: "api/producto/getAll"
            })
    .done(function (data) {
        //Revisamos si hubo algún error:
        if(data.error != null)
            Swal.fire("Error", data.error, "warning");
        else {
            //Recorremos el arreglo de productos para ir generando del contenido de una tabla
            for(var i=0; i < data.length; i++){
                productos = data;
                contenido += '<tr onclick="mostrarDetallesProducto('+ productos[i].id + ');">' +
                                '<td>' + productos[i].id  + '</td>' +
                                '<td>' + productos[i].nombre  + '</td>' +
                                '<td>' + productos[i].marca  + '</td>' +
                                '<td>' + productos[i].precioUso  + '</td>' +
                                '<td>' + productos[i].estatus  + '</td>' +
                    '</tr>';
            } 

            //Insertamos el contenido de la tabla
            $('#tbodyProductos').html(contenido);
        }
    });
}

function mostrarDetallesProducto(idProducto) {
    //Buscamos la posición de un producto en base en su ID
    var pos = buscarPosicionProductoPorId(idProducto);
    

    //Llenamos los datos del producto
    $('#txtCodigo').val(productos[pos].id);
    $('#txtNombre').val(productos[pos].nombre);
    $('#txtMarca').val(productos[pos].marca);
    $('#txtPrecio').val(productos[pos].precioUso);

    setFormularioDetalleVisible(true);
}

/*
    Busca la posición de un objeto producto dentro
    de un arreglo con base en el id del producto

    Si no se encuentra un producto con ese id, la 
    función devolverá -1
*/

function buscarPosicionProductoPorId(id){
    for (var i = 0; i < productos.length; i++){
        if(productos[i].id === id)
            return i;
    }
    return -1;
}