const d = document,
        $slcSucursales = d.getElementById("select-sucursales"),
        $slcSalas = d.getElementById("select-salas"),
        $slcHorarios = d.getElementById("select-time"),
        $datePicker = d.getElementById("input-date");

const token = sessionStorage.getItem("token");

let idCliente = 0;

const loadOptionOnSelect = (select, options) => {
    let $option = d.createElement("option");

    options.forEach(el => {
        $option.value = el.id;
        $option.textContent = el.nombre;
        let $clone = d.importNode($option, true);
        select.appendChild($clone);
    });
};

const loadOptionOnSelectHorario = (select, options) => {
    let $option = d.createElement("option");

    options.forEach(el => {
        $option.value = el.id;
        $option.textContent = el.horaInicio + " - " + el.horaFin;
        let $clone = d.importNode($option, true);
        select.appendChild($clone);
    });
};

function TableRow({date, cliente, sala, horario, id}) {
    return `
        <tr>
            <td>${date.year}-${date.month}-${date.day}</td>
            <td>${horario.horaInicio}</td>
            <td>${horario.horaFin}</td>
            <td>${cliente.persona.nombre}</td>
            <td>${sala.nombre}</td>
            <td>
                <button class="btn btn-outline-warning btn-sm">
                    <i class="fas fa-concierge-bell"></i>
                </button>
                <button class="btn btn-outline-danger btn-sm">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `;
}

const loadTableContent = (arr) => {
    d.querySelector("tbody").innerHTML = arr.map(el => TableRow(el)).join("");
};

(async () => {
    const [resSucursales, resReservaciones] = await Promise.all([
        fetch("api/sucursal"),
        fetch("api/reservacion")
    ]);

    const jsonSucursales = await resSucursales.json();
    const jsonReservaciones = await resReservaciones.json();

    loadOptionOnSelect($slcSucursales, jsonSucursales);
    loadTableContent(jsonReservaciones);
})();

const getElementSeleted = (element) => {
    return element.options[element.selectedIndex];
};

const loadSalasBySucursal = async (id) => {
    const res = await fetch(`api/salas?idSucursal=${id}&token=${token}`),
            salas = await res.json();

    $slcSalas.innerHTML = '<option value="0">Selecione una Sala</option>';
    loadOptionOnSelect($slcSalas, salas);
};

const loadHourByDate = async (date, idSala) => {
    const res = await fetch(`api/reservacion/hours?date=${date}&sala=${idSala}`),
            horarios = await res.json();

    $slcHorarios.innerHTML = '<option>Selecione un horario</option>';
    loadOptionOnSelectHorario($slcHorarios, horarios);
};

const fetchClients = async (pattern) => {
    const res = await fetch(`api/cliente?filter=${pattern}`);
    return res.json();
};


const searchWrapper = d.querySelector(".search-input"),
        inputBox = searchWrapper.querySelector("input"),
        suggBox = searchWrapper.querySelector(".autocom-box");


let webLink;

inputBox.onkeyup = async (evt) => {
    let userData = evt.target.value;
    let emptyArray = [];
    if (userData) {

        suggestions = await fetchClients(userData);

        emptyArray = suggestions.slice(0, 4).map(data => {
            return `<li class="btn-person" data-id="${data.idCliente}">${data.persona.nombre + " " + data.persona.apellidoPaterno + " " + data.persona.apellidoMaterno}</li>`;
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

    searchWrapper.classList.remove("active");
}

function showSuggestions(list) {
    if (list)
        suggBox.innerHTML = list.join("");
}

const clearForm = () => {
    $slcSucursales.selectedIndex = 0;
    $slcSalas.innerHTML = `<option value="0">Selecione una Sala</option>`;
    $slcHorarios.innerHTML = `<option>Selecione un horario</option>`;
    inputBox.value = '';
    $datePicker.innerHTML = '';
}

const handleClickOnSave = async () => {
    const reservation = {
        horario: {
            id: parseInt(getElementSeleted($slcHorarios).value)
        },
        cliente: {
            idCliente: parseInt(d.getElementById("idCliente").value)
        },
        sala: {
            id: parseInt(getElementSeleted($slcSalas).value)
        }
    };

    $.ajax({
        url: 'api/reservacion',
        type: 'POST',
        data: {
            reservation: JSON.stringify(reservation),
            date: $datePicker.value
        }
    }).done((res) => {
        Swal.fire({
            title: "Reservación creada correctamente",
            icon: "success",
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1500
        });
    }).catch((err) => {
        Swal.fire({
            title: "Ha ocurrido un error",
            icon: "error",
            text: err.status + " : " + err.statusText
        });
    });
};

d.addEventListener("click", evt => {
    let $element = evt.target;
    if ($element.id === "btn-save") {
        handleClickOnSave();
    }

    if ($element === $slcSucursales) {
        let idSucursal = getElementSeleted($element).value;
        if (idSucursal !== "0")
            loadSalasBySucursal(idSucursal);
    }

    if ($element.matches(".btn-person")) {
        d.getElementById("idCliente").value = $element.dataset.id;
    }

});

d.addEventListener("change", (evt) => {
    let $element = evt.target;
    if ($element === $datePicker || $element === $slcSalas) {
        if ($datePicker.value) {
            let idSala = parseInt(getElementSeleted($slcSalas).value);
            console.log(idSala);
            loadHourByDate($datePicker.value, idSala);
        }
    }
});
