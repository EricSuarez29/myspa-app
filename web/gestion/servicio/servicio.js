var $modal = new bootstrap.Modal(document.getElementById("exampleModal"), {
    keyboard: false
});

var $tableService = document.querySelector(".table-service tbody");

var $modalTratamiento = document.querySelector(".modal-tratamiento");

var $tableTratamiento = document.querySelector(".table-tratamiento tbody");

function fetchServicios() {
    $.ajax('api/servicio')
    .done((data) =>{
        $tableService.innerHTML = data.map(TableServiceRow).join("");
    })
        .catch(console.error);
}

fetchServicios();

function TableServiceRow({fecha, empleado, reservacion, total}){
    return `
    <tr>
        <td>${fecha.year}-${fecha.month}-${fecha.day}</td>
        <td>${empleado.persona.nombre}</td>
        <td>${reservacion.cliente.persona.nombre}</td>
        <td>${reservacion.sala.nombre}</td>
        <td>${reservacion.horario.horaInicio} - ${reservacion.horario.horaFin}</td>
        <td>${total}</td>
    </tr>
    `;
}

var initialService = {
    reservacion: {
        id: undefined
    },
    empleado: {
        id: undefined
    },
    total: 0,
    servicioTratamientos: []
};

function setService(service){
    initialService = service;
    loadTableTratamiento();
    loadProductosOnModal();
    document.getElementById("gran-total").innerHTML = `$${initialService.total} MX`;
    console.log(initialService);
}

function calcularGranTotal(servicioTratamientos){
    if(!servicioTratamientos) return;
    return servicioTratamientos.map( el => {
        let { tratamiento, productos } = el;
        let totalProductos = 0;
        if(productos) totalProductos =  productos.map(el => el.precioUso).reduce((current, total)=> current + total, 0);
        return tratamiento.costo + totalProductos;
    }).reduce((current, total) => current + total, 0);
    
}

(async () => {
    let id = sessionStorage.getItem("reservation");
    if(!id) return;
    id = parseInt(id);
    sessionStorage.removeItem("reservation");
    
    const res = await fetch(`api/reservacion/${id}`),
        json = await res.json();
    
    document.querySelector("#lblNombre").innerHTML = json.cliente.persona.nombre;
    document.querySelector("#lblSala").innerHTML = json.sala.nombre;
    document.querySelector("#lblFecha").innerHTML = `${json.date.year}-${json.date.month}-${json.date.day}`;
    document.querySelector("#lblHorario").innerHTML = `${json.horario.horaInicio} - ${json.horario.horaFin}`;
    
    $modal.show();

    setService({
        ...initialService,
        reservacion: { id }
    });
})();

// busqueda empleado

var fetchEmpleados = async (pattern) => {
    const res = await fetch(`api/empleados?filter=${pattern}`);
    return await res.json();
};


var searchWrapper = document.querySelector(".search-input"),
        inputBox = searchWrapper.querySelector("input"),
        suggBox = searchWrapper.querySelector(".autocom-box");


inputBox.onkeyup = async (evt) => {
    let userData = evt.target.value;
    let emptyArray = [];
    if (userData) {

        let suggestions = await fetchEmpleados(userData);

        emptyArray = suggestions.slice(0, 4).map(data => {
            return `<li class="btn-person" data-id="${data.id}">${data.persona.nombre + " " + data.persona.apellidoPaterno + " " + data.persona.apellidoMaterno}</li>`;
        });

        searchWrapper.classList.add("active");
        showSuggestions(emptyArray);

        let allList = suggBox.querySelectorAll("li");
        for (let i = 0; i < allList.length; i++) {
            allList[i].setAttribute("onclick", "select(this)");
        }
    } else {
        searchWrapper.classList.remove("active");
    }

};

function select(element) {
    let selectData = element.textContent;
    inputBox.value = selectData;
    let id = parseInt(element.dataset.id);
    searchWrapper.classList.remove("active");
    setService({
        ...initialService,
        empleado: { id }
    });
}

function showSuggestions(list) {
    if (list)
        suggBox.innerHTML = list.join("");
}

// busqueda tratamiento

var fetchTratamientos = async (pattern) => {
    const res = await fetch(`api/tratamientos?filter=${pattern}`);
    return await res.json();
};


var searchWrapperTratamiento = document.querySelector(".search-input-2"),
        inputBoxTratamiento = searchWrapperTratamiento.querySelector("input"),
        suggBoxTratamiento = searchWrapperTratamiento.querySelector(".autocom-box");

inputBoxTratamiento.onkeyup = async (evt) => {
    let userData = evt.target.value;
    let emptyArray = [];
    if (userData) {

        suggestions = await fetchTratamientos(userData);

        emptyArray = suggestions.slice(0, 4).map(data => {
            return `<li class="btn-tratamiento" data-id="${data.id}" data-value="${data.costo}">${data.nombre}</li>`;
        });

        searchWrapperTratamiento.classList.add("active");
        showSuggestionsTratamiento(emptyArray);

        let allList = suggBoxTratamiento.querySelectorAll("li");
        for (let i = 0; i < allList.length; i++) {
            allList[i].setAttribute("onclick", "selectTratamiento(this)");
        }
    } else {
        searchWrapperTratamiento.classList.remove("active");
    }

};

function selectTratamiento(element) {
    let nombre = element.textContent,
            costo = parseFloat(element.dataset.value),
            id = parseInt(element.dataset.id);
    
    const tratamiento = { nombre, costo, id };
    
    inputBoxTratamiento.value = '';
    let servicioTratamientos =  [...initialService.servicioTratamientos, {tratamiento, productos: []}]
    setService({
        ...initialService,
        servicioTratamientos,
        total: calcularGranTotal(servicioTratamientos)
    });
    searchWrapperTratamiento.classList.remove("active");
}

function showSuggestionsTratamiento(list) {
    if (list)
        suggBoxTratamiento.innerHTML = list.join("");
}

var idCurrentTratamiento;

function loadTratamientoOnModal(nombre, id){
    $modalTratamiento.querySelector(".modal-title").innerHTML = nombre;
    idCurrentTratamiento = id;
    loadProductosOnModal();
}

function loadTableTratamiento(){
    $tableTratamiento.innerHTML = initialService
            .servicioTratamientos
            .map(el => TableRowTratamiento(el.tratamiento)).join("");
}

function TableRowTratamiento({nombre, costo, id}){
    return `
    <tr>
        <td>${nombre}</td>
        <td>${costo}</td>
        <td>
            <button onclick="loadTratamientoOnModal('${nombre}', ${id})" class="btn btn-outline-success btn-sm" data-bs-target="#exampleModalToggle2" data-bs-toggle="modal">
                <i class="fas fa-align-justify"></i>
            </button>
            <button onclick="removeTratamiento(${id})" class="btn btn-outline-danger btn-sm">
                <i class="fas fa-trash"></i>
            </button>
        </td>
    </tr>
    `;
}

function removeTratamiento(id){
    let servicioTratamientos = initialService.servicioTratamientos.filter(el => el.tratamiento.id !== id);
    setService({
                ...initialService,
                servicioTratamientos,
                total: calcularGranTotal(servicioTratamientos)
    });
}

// busqueda tratamiento

var fetchProductos = async (pattern) => {
    const res = await fetch(`api/producto/getAll?filter=${pattern}`);
    return await res.json();
};

var searchWrapperProductos = document.querySelector(".search-input-3"),
        inputBoxProductos = searchWrapperProductos.querySelector("input"),
        suggBoxProductos = searchWrapperProductos.querySelector(".autocom-box");

inputBoxProductos.onkeyup = async (evt) => {
    let userData = evt.target.value;
    let emptyArray = [];
    if (userData) {

        let suggestions = await fetchProductos(userData);

        emptyArray = suggestions.slice(0, 4).map(data => {
            return `<li class="btn-producto" data-id="${data.id}" data-value="${data.precioUso}">${data.nombre}</li>`;
        });

        searchWrapperProductos.classList.add("active");
        showSuggestionsProducto(emptyArray);

        let allList = suggBoxProductos.querySelectorAll("li");
        for (let i = 0; i < allList.length; i++) {
            allList[i].setAttribute("onclick", "selectProducto(this)");
        }
    } else {
        searchWrapperProductos.classList.remove("active");
    }

};

var tableProductos = $modalTratamiento.querySelector('tbody');

function loadProductosOnModal(){
    if(!idCurrentTratamiento) return;
    
    const servicioTratamiento = initialService.servicioTratamientos.find(el => {
        return el.tratamiento.id === idCurrentTratamiento;
    });
    
    if(servicioTratamiento) tableProductos.innerHTML = servicioTratamiento.productos.map(TableProductoRow).join("");
}

function TableProductoRow({nombre, precioUso, id}){
    return `
        <tr>
            <td>${nombre}</td>
            <td>${precioUso}</td>
            <td>
                <button onclick="removeProducto(${id})" class="btn btn-outline-danger btn-sm">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `;
}

function removeProducto(id){
    let servicioTratamiento = initialService.servicioTratamientos.find(el => el.tratamiento.id === idCurrentTratamiento);
    servicioTratamiento.productos = servicioTratamiento.productos.filter(el => el.id !== id);
    let newServicioTratamientos = initialService.servicioTratamientos.filter(el => el.tratamiento.id !== idCurrentTratamiento);
    const servicioTratamientos = [...newServicioTratamientos, servicioTratamiento];
    setService({
                ...initialService,
                servicioTratamientos,
                total: calcularGranTotal(servicioTratamientos)
    });
}

function selectProducto(element) {
    let nombre = element.textContent,
            precioUso = parseFloat(element.dataset.value),
            id = parseInt(element.dataset.id);
    
    const producto = { nombre, precioUso, id };
    
    inputBoxProductos.value = '';
    let servicioTratamiento = initialService.servicioTratamientos.find(el => el.tratamiento.id === idCurrentTratamiento);
    let newServicioTratamientos = initialService.servicioTratamientos.filter(el => el.tratamiento.id !== idCurrentTratamiento);
    const servicioTratamientos = [...newServicioTratamientos, {...servicioTratamiento, productos: [...servicioTratamiento.productos, producto]}];
    setService({
        ...initialService,
        servicioTratamientos,
        total: calcularGranTotal(servicioTratamientos)
    });
    searchWrapperProductos.classList.remove("active");
}

function showSuggestionsProducto(list) {
    if (list)
        suggBoxProductos.innerHTML = list.join("");
}

function handleSaveService(){
    $.ajax({
        url: 'api/servicio',
        type: 'POST',
        data: {
            servicio: JSON.stringify(initialService)
        }
    }).done(function(data){
        fetchServicios();
        Swal.fire({
            title: 'Servicio creado correctamente',
            icon: 'success'
        });
    }).catch(function(err){
        Swal.fire({
            title: 'Servicio creado correctamente',
            text: err,
            icon: 'success'
        });
    });
}
