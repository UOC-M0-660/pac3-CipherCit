# PARTE TEORICA

### Lifecycle

#### Explica el ciclo de vida de una Activity.

##### ¿Por qué vinculamos las tareas de red a los componentes UI de la aplicación?
Porque los datos no pueden mostrarse en caso de que los componentes UI no existan, por lo tanto, las peticiones de red deben cancelarse si los elementos que las muestran van a ser destruidos.

##### ¿Qué pasaría si intentamos actualizar la recyclerview con nuevos streams después de que el usuario haya cerrado la aplicación?
Se produciría una excepción ya que la recyclerview no existiría. 

##### Describe brevemente los principales estados del ciclo de vida de una Activity.
El primer estado se produce con el lanzamiento de la actividad. Al producirse este estado, se lanzan las llamadas a los métodos `onCreate()`, `onStart()` y `onResume()`, que hacen que la actividad pase a estar activa (*running*). En este punto, la actividad es visible para el usuario.
Cuando se pasa a otra actividad, se produce la llamada al método `onPause()`. Después de esto se ejecutarían los métodos `onStop()` y `onDestroy()` y la actividad pasaría a ser destruida.

---

### Paginación 

#### Explica el uso de paginación en la API de Twitch.

##### ¿Qué ventajas ofrece la paginación a la aplicación?
La paginación reduce el tiempo de respuesta en la aplicación. Al utilizarla, se cargan solo algunos elementos y esto permite que la respuesta sea más rápida. Esto hace, además, que no se carguen grandes listas que el usuario dificilmente va a visualizar (una lista con cientos de elementos, por ejemplo).

##### ¿Qué problemas puede tener la aplicación si no se utiliza paginación?
Puede que tarde demasiado en responder, haciendo que el usuario abandone la aplicación. 

##### Lista algunos ejemplos de aplicaciones que usan paginación.
- [Twitter](https://developer.twitter.com/en/docs/twitter-ads-api/pagination)
- [Slack](https://api.slack.com/docs/pagination)
- [Github](https://docs.github.com/en/free-pro-team@latest/rest/guides/traversing-with-pagination)
