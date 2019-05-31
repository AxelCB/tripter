# API

Actualmente es posible acceder a una versión simple de la documentación levantando la aplicación y accediendo a < hostname >/swagger-ui.html

Por simplicidad, para la siguiente explicación detallada de los servicios se asume que se está utilizando la configuración por defecto provista en Docker, y que se utilizarán las herramientas provistas por swagger-ui para probar cada servicio.

Los parámetros necesarios para conectarse con la base de datos postgresql utilizada son:
-  hostname: localhost
-  port: 5432
-  username: postgres
-  password: docker

#### Registración de Nuevo usuario

Para registrar un nuevo usuario, ingresar [a la url de la api de swagger](http://localhost:8080/swagger-ui.html) mencionada anteriormente, ubicar dentro de los métodos de user-controller, el método register.

Al abrirlo, se mostrarán los campos que será necesario completar y en el extremo superior derecho de la sección de parámetros, habrá un botón ("Try it out"), el cual permitirá completar los campos y luego hacer click en un botón azul ("Execute") el que ejecutará el request con los parámetros ingresados.

Luego de haber registrado un usuario, podemos corroborar que el mismo ha sido creado ingresando al [login de la aplicación](http://localhost:8080/login) e ingresando las credenciales (ignorar la respuesta luego del login, ya que esta será eliminada en una próxima versión).

#### Eliminar un usuario de un viaje

Para realizar la prueba de dicho servicio, primero debemos contar con más de un usuario registrado, un viaje creado y el usuario a eliminar agregado como viajero. Para facilitar dicha prueba, se incluye un script [removeUserFortripSetup.sql](../test/removeUserForTripSetup.sql) el cual configura lo necesario.

Luego de ejecutar el script con el cliente de base de datos deseado, es necesario ingresar al [login de la aplicación](http://localhost:8080/login) con las credenciales:
- usuario: traveler1
- contraseña: 123456

Luego de haber accedido, ingresar [a la url de la api de swagger](http://localhost:8080/swagger-ui.html) mencionada anteriormente, ubicar dentro de los métodos de trip-controller, el método removeTraveler.
                         
Al abrirlo, se mostrarán los campos que será necesario completar y en el extremo superior derecho de la sección de parámetros, habrá un botón ("Try it out"), el cual permitirá completar los campos y luego hacer click en un botón azul ("Execute") el que ejecutará el request con los parámetros ingresados. Si se utilizó el script, los parámetros a utilizar son:
- TripId: 5
- TravelerId: 3 ó 4

Para validar que efectivamente se haya eliminado el usuario, podemos utilizar una query como `SELECT * FROM user_account_for_trip WHERE trip_id = 5` antes y después de ejecutar el servicio y ver que se elimina la cuenta de ese usuario para dicho viaje.

La aplicación devolverá los errores correspondientes si el usuario o el viaje no existen (404), si el usuario no forma parte de dicho viaje (404), si se inició sesión con un usuario que no es quien organizó el viaje, como si se intenta eliminar un usuario que ya formó parte de algun gasto dentro de dicho viaje (409). Para esto último, ver más abajo un caso donde tenga gastos cargados.

#### Agregar un gasto

Para realizar la prueba de dicho servicio, primero debemos contar con más de un usuario registrado, un viaje creado y los usuarios agregados como viajeros. Para facilitar dicha prueba, se incluye un script [updateTripAddingExpenseSetup.sql](../test/updateTripAddingExpenseSetup.sql) el cual configura lo necesario.

Luego de ejecutar el script con el cliente de base de datos deseado, es necesario ingresar al [login de la aplicación](http://localhost:8080/login) con las credenciales:
- usuario: traveler1
- contraseña: 123456

Luego de haber accedido, ingresar [a la url de la api de swagger](http://localhost:8080/swagger-ui.html) mencionada anteriormente, ubicar dentro de los métodos de trip-controller, el método addExpense.
                         
Al abrirlo, se mostrarán los campos que será necesario completar y en el extremo superior derecho de la sección de parámetros, habrá un botón ("Try it out"), el cual permitirá completar los campos y luego hacer click en un botón azul ("Execute") el que ejecutará el request con los parámetros ingresados. Si se utilizó el script, los parámetros a utilizar son:
- TripId: 5
- Version: 3 (tener en cuenta que este valor deberá ser modificado en caso de querer agregar más de un gasto)
- ExpenseRequest: `{ "cost": 280, "category": "Food", "payments": [ { "userId": 4, "amount": 280 } ], "strategy": "EQUALLY", "usersIds": [ 4, 2 ]}`
 
Para validar que efectivamente se haya creado el gasto, ver el siguiente método para calcular la deuda de un usuario.

El servicio valida que la versión que se está enviando sea la más actualizada, que todos los ids de usuarios enviados pertenezcan a participantes del viaje, y para los tipos de division de gastos porcentual y distribuido se valida que la suma del monto por usuario corresponda al total gastado (estos casos no se incluyen en la prueba descripta). 

#### Listado de deudas de un usuario

Para realizar la prueba de dicho servicio, primero debemos contar con más de un usuario registrado, un viaje creado, los usuarios agregados como viajeros y al menos un gasto creado. Por simplicidad, se recomienda probarlo luego de probar el servicio anterior, momento donde se contarán con todas las precondiciones necesarias.

Es necesario ingresar al [login de la aplicación](http://localhost:8080/login) con las credenciales:
- usuario: traveler1
- contraseña: 123456

Luego de haber accedido, ingresar [a la url de la api de swagger](http://localhost:8080/swagger-ui.html) mencionada anteriormente, ubicar dentro de los métodos de trip-controller, el método getDebts.
                         
Al abrirlo, se mostrarán los campos que será necesario completar y en el extremo superior derecho de la sección de parámetros, habrá un botón ("Try it out"), el cual permitirá completar los campos y luego hacer click en un botón azul ("Execute") el que ejecutará el request con los parámetros ingresados. Si se siguió los pasos recomendados, los parámetros a utilizar son:
- TripId: 5

El resultado será un arreglo de deudas, cada una con:
- UserId: id del usuario al que se le debe dinero
- UserFullName: Nombre completo (nombre y apellido) del usuario al que se le debe dinero
- Debt: Monto de la deuda hacia dicho usuario
