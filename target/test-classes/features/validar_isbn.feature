# language: es
Característica: Validación de ISBN
  Para asegurar la calidad de los datos necesito validar el formato del ISBN.

  Escenario: Rechazar ISBN vacío
    Dado que tengo un catálogo vacío
    Cuando intento agregar un libro con título "Libro Test", autor "Autor Test" e ISBN ""
    Entonces se muestra un error "El ISBN no puede estar vacío"

  Escenario: Rechazar ISBN con formato inválido
    Dado que tengo un catálogo vacío
    Cuando intento agregar un libro con título "Libro Test", autor "Autor Test" e ISBN "123"
    Entonces se muestra un error "El ISBN debe tener al menos 10 caracteres"

  Escenario: Aceptar ISBN válido de 10 dígitos
    Dado que tengo un catálogo vacío
    Cuando agrego un libro con título "Libro Test", autor "Autor Test" e ISBN "1234567890"
    Entonces el libro existe en el catálogo

  Escenario: Aceptar ISBN válido de 13 dígitos
    Dado que tengo un catálogo vacío
    Cuando agrego un libro con título "Libro Test", autor "Autor Test" e ISBN "978-1234567890"
    Entonces el libro existe en el catálogo