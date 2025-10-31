# language: es
Característica: Agregar libros a la biblioteca
  Para mantener actualizado el catálogo necesito poder agregar libros.

  Escenario: Agregar libro con datos válidos
    Dado que tengo un catálogo vacío
    Cuando agrego un libro con título "Cien años de soledad", autor "Gabriel García Márquez" e ISBN "978-0307474728"
    Entonces el libro existe en el catálogo
    Y el libro tiene un ID asignado
    Y el total de libros es 1

  Escenario: Agregar múltiples libros
    Dado que tengo un catálogo vacío
    Cuando agrego un libro con título "Don Quijote", autor "Miguel de Cervantes" e ISBN "978-8424936464"
    Y agrego un libro con título "1984", autor "George Orwell" e ISBN "978-0451524935"
    Entonces el total de libros es 2

  Escenario: Impedir agregar libro con título vacío
    Dado que tengo un catálogo vacío
    Cuando intento agregar un libro con título "", autor "Autor Test" e ISBN "123456789"
    Entonces se muestra un error "El título no puede estar vacío"
    Y el total de libros es 0