# PARTE TEORICA

### Lifecycle

#### Explica el ciclo de vida de una Activity.

##### ¿Por qué vinculamos las tareas de red a los componentes UI de la aplicación?
Porque los datos no pueden mostrarse en caso de que los componentes UI no existan, por lo tanto, las peticiones de red deben cancelarse si los elementos que las muestran van a ser destruidos.

##### ¿Qué pasaría si intentamos actualizar la recyclerview con nuevos streams después de que el usuario haya cerrado la aplicación?
Se produciría una excepción ya que la recyclerview no existiría. 

##### Describe brevemente los principales estados del ciclo de vida de una Activity.
El primer estado se produce con el lanzamiento de la actividad. Al producirse este estado, se lanzan las llamadas a los métodos `onCreate()`, `onStart()` y `onResume()`, que hacen que la actividad pase a estar activa (*running*). En este punto, la actividad es visible para el usuario.
Cuando se pasa a otra actividad, se produce la llamada al método `onPause()`. Después de esto se ejecutarían los métodos `onStop()' y `onDestroy()` y la actividad pasaría a ser destruida.

---

### Paginación 

#### Explica el uso de paginación en la API de Twitch.

##### ¿Qué ventajas ofrece la paginación a la aplicación?
Escribe aquí tu respuesta

##### ¿Qué problemas puede tener la aplicación si no se utiliza paginación?
Escribe aquí tu respuesta

##### Lista algunos ejemplos de aplicaciones que usan paginación.
Escribe aquí tu respuesta
