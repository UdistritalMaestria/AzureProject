# language: es
Característica: Listar y eliminar libros
  Para mantener el catálogo actualizado necesito ver y eliminar libros.

  Antecedentes:
    Dado que tengo un catálogo vacío
    Y existen los siguientes libros:
      | titulo                | autor                  | isbn            |
      | Cien años de soledad  | Gabriel García Márquez | 978-0307474728  |
      | Don Quijote           | Miguel de Cervantes    | 978-8424936464  |
      | 1984                  | George Orwell          | 978-0451524935  |

  Escenario: Listar todos los libros
    Cuando consulto el catálogo
    Entonces veo 3 libro(s)

  Escenario: Eliminar un libro específico
    Cuando elimino el libro con título "Don Quijote"
    Entonces el total de libros es 2
    Y el libro "Don Quijote" no existe en el catálogo

  Escenario: Catálogo vacío muestra cero libros
    Dado que tengo un catálogo vacío
    Cuando consulto el catálogo
    Entonces veo 0 libro(s)