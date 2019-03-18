## Requerimientos funcionales de Tripter

### Introducción
Se desea realizar una aplicación que permita administrar gastos compartidos entre varias personas durante un viaje.

### Administración de viajes
La misma debe permitir a un usuario crear un viaje del cual será organizador, y dentro del mismo agregar a los viajeros que desee. Un viaje debe tener un nombre o título, una fecha de inicio, y opcionalmente una fecha de fin.

### Registración de usuarios
Todo usuario de la aplicación (tanto organizador como viajero) deberá haberse registrado previamente. <Complete with what data is necesary>

### Carga de gastos
Un usuario debe poder agregar gastos para los viajes de los que forme parte. Para cada gasto deberá indicarse el monto total, la categoría a la que pertenece, la forma en que se dividirá el mismo, y los viajeros involucrados, junto con cuánto pagó cada uno. La aplicación debe permitir dividir el gasto de las tres siguientes formas: 
* De forma equitativa: a cada viajero le corresponde pagar la misma cantidad del gasto.
* De forma porcentual: para cada viajero se indica un porcentaje que le corresponde pagar del gasto. La suma de porcentajes debe ser 100%.
* Distribuido: para cada viajero se indica el monto exacto que le corresponde pagar. La suma de los montos a pagar por cada viajero no debe igualar la suma total del gasto en cuestión.

### Consulta de deudas
Dentro de un viaje, un viajero debe poder consultar cuánto debe a otros viajeros y cuánto le deben, teniendo en cuenta el total de gastos del viaje.

### Pago de deudas y préstamos de dinero
Por otro lado, un viajero debe poder reflejar en la aplicación cuando salde alguna de sus deudas (de forma parcial o completa), y si realiza algún préstamo de dinero a otro viajero (lo cual generará una deuda de la cual será acreedor), indicando el monto y el viajero que recibe el dinero.