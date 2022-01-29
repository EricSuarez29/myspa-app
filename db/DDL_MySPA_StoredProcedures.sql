/********************************************************
 *      BASE DE DATOS myspa                             *
 *                                                      *
 *      Archivo de Definicion de Datos (DDL)            *
 *      Procedimientos Almacenados - Stored Procedures  *
 *******************************************************/
 
 /*
    Version:        1.0
    Fecha:          10/04/2018 19:15:00
    Autor:          Miguel Angel Gil Rios
    Email:          angel.grios@gmail.com
    Comentarios:    Esta es la primera version de los procedimientos
                    almacenados de la base de datos.
 */
 
 USE myspa;
 
-- Stored Procedure para insertar nuevos Empleados.
DROP PROCEDURE IF EXISTS insertarEmpleado;
DELIMITER $$
CREATE PROCEDURE insertarEmpleado(	/* Datos Personales */
                                    IN	var_nombre          VARCHAR(64),    -- 1
                                    IN	var_apellidoPaterno VARCHAR(64),    -- 2
                                    IN	var_apellidoMaterno VARCHAR(64),    -- 3
                                    IN  var_genero          VARCHAR(2),     -- 4
                                    IN	var_domicilio       VARCHAR(200),   -- 5
                                    IN	var_telefono        VARCHAR(25),    -- 6
                                    IN	var_rfc             VARCHAR(14),    -- 7
                                    
                                    /* Datos de Usuario */
                                    IN	var_nombreUsuario   VARCHAR(48),    -- 8
                                    IN	var_contrasenia     VARCHAR(48),    -- 9
                                    IN	var_rol             VARCHAR(24),    -- 10
                                    
                                    /* Datos de Empleado */
                                    IN	var_puesto          VARCHAR(20),    -- 11
                                    IN	var_foto            LONGTEXT,       -- 12
                                    IN	var_rutaFoto        TEXT,           -- 13
                                    
                                    /* Valores de Retorno */
                                    OUT	var_idPersona       INT,            -- 14
                                    OUT	var_idUsuario       INT,            -- 15
                                    OUT	var_idEmpleado      INT,            -- 16
                                    OUT var_numeroEmpleado  VARCHAR(70)     -- 17
				)                                    
    BEGIN        
        -- Comenzamos insertando los datos de la Persona:
        INSERT INTO persona (nombre, apellidoPaterno, apellidoMaterno, genero,
                             domicilio, telefono, rfc)
                    VALUES( var_nombre, var_apellidoPaterno, var_apellidoMaterno, 
                            var_genero, var_domicilio, var_telefono, var_rfc);
        -- Obtenemos el ID de Persona que se gener�:
        SET var_idPersona = LAST_INSERT_ID();

        -- Insertamos los datos de seguridad del Empleado:
        INSERT INTO usuario ( nombreUsuario, contrasenia, rol) 
                    VALUES( var_nombreUsuario, var_contrasenia, var_rol);
        -- Obtenemos el ID de Usuario que se gener�:
        SET var_idUsuario = LAST_INSERT_ID();

        --  Generamos el numero de empleado.
        --  Comenzamos agregando el primer digito (la letra E):
        SET var_numeroEmpleado = 'E';        
        --  Agregamos los digitos del RFC si los tiene:
        IF  LENGTH(var_rfc) >= 4 THEN
            SET var_numeroEmpleado = CONCAT(var_numeroEmpleado, SUBSTRING(var_rfc, 1, 4));
        END IF;
        --  Finalmente, agregamos el timestamp:
        SET var_numeroEmpleado = CONCAT(var_numeroEmpleado, CAST(UNIX_TIMESTAMP() AS CHAR));

        -- Finalmente, insertamos en la tabla Empleado:
        INSERT INTO empleado ( numeroEmpleado, puesto, estatus, foto, rutaFoto, 
                               idPersona, idUsuario) 
                    VALUES( var_numeroEmpleado, var_puesto, 1, var_foto, 
                            var_rutaFoto, var_idPersona, var_idUsuario);
        -- Obtenemos el ID del Empleado que se generó:
        SET var_idEmpleado = LAST_INSERT_ID();
    END
$$
DELIMITER ;



-- Stored Procedure para actualizar datos de empleados existentes.
DROP PROCEDURE IF EXISTS actualizarEmpleado;
DELIMITER $$
CREATE PROCEDURE actualizarEmpleado(	/* Datos Personales */
                                    IN  var_nombre          VARCHAR(64),    -- 1
                                    IN  var_apellidoPaterno VARCHAR(64),    -- 2
                                    IN  var_apellidoMaterno VARCHAR(64),    -- 3
                                    IN  var_genero          VARCHAR(2),     -- 4
                                    IN  var_domicilio       VARCHAR(200),   -- 5
                                    IN  var_telefono        VARCHAR(25),    -- 6
                                    IN	var_rfc             VARCHAR(14),    -- 7
                                    
                                    /* Datos de Usuario */
                                    IN	var_nombreUsuario   VARCHAR(48),    -- 8
                                    IN	var_contrasenia     VARCHAR(48),    -- 9
                                    IN	var_rol             VARCHAR(24),    -- 10
                                    
                                    /* Datos de Empleado */
                                    IN  var_puesto          VARCHAR(20),    -- 11
                                    IN  var_foto            LONGTEXT,       -- 12
                                    IN  var_rutaFoto        TEXT,           -- 13
                                    --  El número de empleado no se considera
                                    --  porque no es actualizable.
                                    
                                    /* ID's de las tablas relacionadas con el Empleado */
                                    IN	var_idPersona       INT,            -- 14
                                    IN	var_idUsuario       INT,            -- 15
                                    IN	var_idEmpleado      INT             -- 16
                                )                                    
    BEGIN
        -- Comenzamos actualizando los datos personales del Empleado:
        UPDATE persona  SET     nombre = var_nombre, 
                                apellidoPaterno = var_apellidoPaterno,
                                apellidoMaterno = var_apellidoMaterno,
                                genero = var_genero,
                                domicilio = var_domicilio,
                                telefono = var_telefono, 
                                rfc = var_rfc
                        WHERE   idPersona = var_idPersona;

        -- Actualizamos los datos de Seguridad:
        UPDATE usuario  SET     nombreUsuario = var_nombreUsuario,
                                contrasenia = var_contrasenia,
                                rol = var_rol
                        WHERE   idUsuario = var_idUsuario;

        -- Actualizamos sus datos de Empleado:
        UPDATE empleado SET     puesto = var_puesto, 
                                -- estatus = var_estatus, 
                                foto = var_foto, 
                                rutaFoto = var_rutaFoto 
                        WHERE   idEmpleado = var_idEmpleado;        
    END
$$
DELIMITER ;



-- Stored Procedure para insertar nuevos Clientes.
DROP PROCEDURE IF EXISTS insertarCliente;
DELIMITER $$
CREATE PROCEDURE insertarCliente(   /* Datos Personales */
                                    IN  var_nombre          VARCHAR(64),
                                    IN  var_apellidoPaterno VARCHAR(64),
                                    IN  var_apellidoMaterno VARCHAR(64),
                                    IN  var_genero          VARCHAR(2),
                                    IN  var_domicilio       VARCHAR(200),
                                    IN	var_telefono        VARCHAR(25),
                                    IN	var_rfc             VARCHAR(14),
                                    
                                    /* Datos de Usuario */
                                    IN  var_nombreUsuario   VARCHAR(48),
                                    IN  var_contrasenia     VARCHAR(48),
                                    IN  var_rol             VARCHAR(24),
                                    
                                    /* Datos de Cliente */
                                    IN  var_correo          VARCHAR(200),
                                    IN  var_numeroUnico     VARCHAR(11),
                                    
                                    /* Valores de Retorno */
                                    OUT var_idPersona       INT,
                                    OUT var_idUsuario       INT,
                                    OUT var_idCliente       INT
                                )
    BEGIN
        -- Comenzamos insertando en la tabla Persona:
        INSERT INTO persona ( nombre, apellidoPaterno, apellidoMaterno, genero,
                              domicilio, telefono, rfc) 
                    VALUES( var_nombre, var_apellidoPaterno, var_apellidoMaterno, 
                            var_genero, var_domicilio, var_telefono, var_rfc);
        -- Recuperamos el ID de la Persona que se generÃ³:
        SET var_idPersona = LAST_INSERT_ID();


        -- Insertamos en la tabla de Usuario con los datos de seguridad:
        INSERT INTO usuario ( nombreUsuario, contrasenia, rol) 
                    VALUES( var_nombreUsuario, var_contrasenia, var_rol);
        -- Recuperamos el ID del Usuario que se generÃ³ de forma automÃ¡tica:
        SET var_idUsuario = LAST_INSERT_ID();


        -- Finalmente, insertamos en la tabla cliente:
        INSERT INTO cliente ( correo, numeroUnico, estatus,
                              idPersona, idUsuario) 
                    VALUES( var_correo, var_numeroUnico, 1, var_idPersona, var_idUsuario);
        -- Recuperamos el ID del CLiente que se generÃ³ de forma automÃ¡tica:
        SET var_idCliente = LAST_INSERT_ID();
    END
$$
DELIMITER ;



-- Stored Procedure para actualizar datos de clientes previamente registrados.
DROP PROCEDURE IF EXISTS actualizarCliente;
DELIMITER $$
CREATE PROCEDURE actualizarCliente(     /* Datos Personales */
                                        IN  var_nombre          VARCHAR(64),
                                        IN  var_apellidoPaterno VARCHAR(64),
                                        IN  var_apellidoMaterno VARCHAR(64),
                                        IN  var_genero          VARCHAR(2),
                                        IN  var_domicilio       VARCHAR(200),
                                        IN  var_telefono        VARCHAR(25),
                                        IN  var_rfc             VARCHAR(14),

                                        /* Datos de Usuario */
                                        IN  var_nombreUsuario   VARCHAR(48),
                                        IN  var_contrasenia     VARCHAR(48),
                                        IN  var_rol             VARCHAR(24),

                                        /* Datos de Cliente */
                                        IN  var_correo          VARCHAR(200),
                                        IN  var_clave           VARCHAR(11),

                                        /* ID's de las tablas afectadas */
                                        IN var_idPersona        INT,
                                        IN var_idUsuario        INT,
                                        IN var_idCliente        INT
                                    )
    BEGIN
        -- Comenzamos actualizando los datos personales:
        UPDATE  persona SET     nombre = var_nombre, 
                                apellidoPaterno = var_apellidoPaterno,
                                apellidoMaterno = var_apellidoMaterno,
                                genero = var_genero,
                                domicilio = var_domicilio,
                                telefono = var_telefono, 
                                rfc = var_rfc
                        WHERE   idPersona = var_idPersona;

        -- Después actualizamos los datos de seguridad:
        UPDATE  usuario SET     nombreUsuario = var_nombreUsuario, 
                                contrasenia = var_contrasenia,
                                rol = var_rol 
                        WHERE   idUsuario = var_idUsuario;

        -- Finalmente, actualizamos los datos del cliente:
        UPDATE  cliente SET     correo = var_correo,
                                numeroUnico = var_clave
                        WHERE   idCliente = var_idCliente;

    END
$$
DELIMITER ;

-- Stored Procedure para insertar una Reservacion:
DROP PROCEDURE IF EXISTS insertarReservacion;
DELIMITER $$
CREATE PROCEDURE insertarReservacion(   /* Datos Personales */
                                        IN var_fechaHoraInicio  VARCHAR(21),
                                        IN var_fechaHoraFin     VARCHAR(21),
                                        IN var_idCliente        INT,
                                        IN var_idSala           INT,
                                        OUT var_idReservacion   INT
                                )
    BEGIN
        -- Comenzamos insertando en la tabla Persona:
        INSERT INTO reservacion (fechaHoraInicio, fechaHoraFin, estatus, idCliente, idSala) 
                    VALUES(STR_TO_DATE(var_fechaHoraInicio, '%d/%m/%Y %H:%i:%s'), STR_TO_DATE(var_fechaHoraFin, '%d/%m/%Y %H:%i:%s'), 1, var_idCliente, var_idSala);
        -- Recuperamos el ID de la Persona que se generÃ³:
        SET var_idReservacion = LAST_INSERT_ID();
    END
$$
DELIMITER ;