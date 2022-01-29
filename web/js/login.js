function validarUsuario(){
    var usuario = document.getElementById('txtUsuario').value;
    var contrasennia = document.getElementById('txtContrasennia').value;

    $.ajax({
        url: 'api/auth/login',
        type: 'POST',
        data: {
            username: usuario,
            password: contrasennia
        }
    }).done(function(data){
        if(data.error){
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Usuario o Contraseña incorrectos',
                showConfirmButton: false,
                timer: 2100
            });
        } else {
            
            sessionStorage.setItem('empleado', JSON.stringify(data));
            sessionStorage.setItem('token', data.usuario.token);
            
            window.location.replace('main.html');
        }
    });

    
}

document.addEventListener('DOMContentLoaded', evt=>{
    const token = sessionStorage.getItem('token');
    if(token){
        $.ajax({
            url: 'api/auth',
            type: 'POST',
            data: {
                token
            }
        }).done(function(res){
            window.location.replace('main.html');
        });
    }
});