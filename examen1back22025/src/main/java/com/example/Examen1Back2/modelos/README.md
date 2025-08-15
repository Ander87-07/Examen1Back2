Informe Correcciones Realizadas al Proyecto Evaluativo

Elaborado por:
Anderson Zapata Vanegas

Institución:
CESDE

Técnico laboral en auxiliar en desarrollo de software
Modulo:
Backend: 2

Instructor:
Juan José Gallego

15 de agosto de 2025
Medellín – Colombia

Introducción
Este informe resume las correcciones y mejoras realizadas en las clases principales del proyecto evaluativo, enfocándose en el correcto uso de enumeraciones, relaciones bidireccionales entre entidades, anotaciones JPA y buenas prácticas de programación en Java. Estas modificaciones aseguran un mejor manejo de datos, persistencia eficiente y mayor consistencia en la aplicación.

Descripción del proyecto
Proyecto evaluativo Java con Spring Boot (paquete com.example.Examen1Back2) que modela entidades como Usuario, Docente y Curso. El proyecto usa JPA/Hibernate para persistencia y estaba configurado para conectarse a una base de datos MySQL llamada develop_db.
El objetivo de esta documentación es dejar constancia de las correcciones hechas, explicar por qué ocurrían los errores y proveer una guía reproducible para conectar la aplicación a MySQL.

1. Correcciones en la clase Usuario.java
   a) Modificación del tipo del atributo tipoUsuario
   Originalmente, el atributo tipoUsuario estaba declarado como un String, lo cual no era ideal para representar un conjunto cerrado de posibles valores. Se corrigió para usar un enum llamado TipoUsuario, con la siguiente definición:

@Enumerated(EnumType.STRING)
private TipoUsuario tipoUsuario;

Esto permite que el tipo de usuario solo pueda ser uno de los valores definidos en el enum, garantizando integridad y facilitando la validación.

b) Actualización de constructor, getters y setters
El constructor y los métodos getter y setter fueron ajustados para trabajar con TipoUsuario en lugar de String. Ejemplo:

public Usuario(Integer id, String nombre, String correoElectronico, String contrasena, String telefono, TipoUsuario tipoUsuario) {
this.tipoUsuario = tipoUsuario;
}

public TipoUsuario getTipoUsuario() { return tipoUsuario; }
public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }

Esto mantiene coherencia con la nueva definición del atributo.

c) Sincronización de la relación bidireccional con Docente
Para evitar inconsistencias entre las entidades Usuario y Docente, se mejoró el método setDocente en Usuario para sincronizar ambas partes de la relación:

public void setDocente(Docente docente) {
this.docente = docente;
if (docente != null && docente.getUsuario() != this) {
docente.setUsuario(this);
}
}

Así se garantiza que, al asignar un docente a un usuario, el docente también tenga asignado ese usuario, manteniendo la relación coherente en memoria.

d) Mantenimiento de anotaciones JPA y JSON
Se mantuvieron y corrigieron las anotaciones necesarias para la persistencia con JPA y la serialización con JSON, asegurando que el mapeo objeto-relacional y el manejo de datos en formatos como JSON funcionen correctamente.

2. Creación de la clase TipoUsuario (enum)
   Para implementar la mejora en el tipo de usuario, se creó un nuevo archivo TipoUsuario.java en el paquete correspondiente, con la siguiente estructura:

public enum TipoUsuario {
ADMIN,
DOCENTE,
ESTUDIANTE
}

Esto define claramente los posibles tipos de usuario, favoreciendo la robustez y legibilidad del código.

3. Correcciones en la clase Docente
   a) Corrección de anotaciones JPA
- Se corrigió la anotación mal escrita @Entit por @Entity para que la clase Docente sea reconocida correctamente como una entidad JPA.
- Se agregó la anotación @Id en el campo id para identificar la clave primaria, lo cual es indispensable para JPA.

b) Métodos getter y setter para usuario
Se implementaron los métodos getter y setter para el atributo usuario que faltaban, incluyendo lógica para mantener la relación bidireccional sincronizada:

public void setUsuario(Usuario usuario) {
this.usuario = usuario;
if (usuario != null && usuario.getDocente() != this) {
usuario.setDocente(this);
}
}

Esto evita inconsistencias entre objetos relacionados.

4. Relaciones JPA
   Se confirmó y ajustó la configuración correcta de las relaciones entre entidades usando anotaciones JPA y JSON para manejo bidireccional y evitar problemas en la serialización.
   a) En la clase Usuario:
   @OneToOne(mappedBy = "usuario")
   @JsonBackReference(value = "docente-usuario")
   private Docente docente;

b) En la clase Docente:
@OneToOne
@JoinColumn(name = "fk_usuario", referencedColumnName = "id_usuario")
@JsonManagedReference(value = "docente-usuario")
private Usuario usuario;

c) Relación Docente - Curso
Además, en Docente se mantiene la relación uno a muchos con Curso, correctamente anotada para manejo JPA y JSON.

5. Resumen de correcciones en la clase Curso.java
- @I corregido por @Id.
- @Ge(strategy = IDENTITY) corregido por @GeneratedValue(strategy = GenerationType.IDENTITY).
- Se encapsularon correctamente los atributos agregando el modificador private.
- Se corrigieron errores como puntos y comas sobrantes o faltantes.
- Se ajustó la relación @ManyToOne con Docente, con la anotación @JoinColumn correcta y las referencias JSON para serialización adecuada.
- Se añadieron los métodos getters y setters para todos los atributos, promoviendo buen encapsulamiento y acceso controlado.

Conclusión
Las correcciones realizadas fortalecen el proyecto desde la perspectiva del modelado de datos, persistencia y arquitectura del código. El uso correcto de enums, las relaciones JPA bien definidas y sincronizadas, junto con el respeto a buenas prácticas de programación, contribuyen a un sistema más robusto, mantenible y menos propenso a errores tanto en tiempo de ejecución como en la integración con la base de datos y la serialización JSON.

Guía de Conexión de Spring Boot a MySQL

1. Requisitos previos
- Java JDK instalado (en este caso JDK 21).
- MySQL Server funcionando en el puerto 3306.
- Base de datos llamada develop_db creada en MySQL.
- Spring Boot configurado en el proyecto.
- Dependencias necesarias en pom.xml.

2. Crear la base de datos en MySQL
   En la consola MySQL o phpMyAdmin ejecutar:
   CREATE DATABASE develop_db;

3. Agregar dependencias en pom.xml
   <dependencies>
   <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
   </dependency>
   <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-data-jpa</artifactId>
   </dependency>
   <dependency>
   <groupId>mysql</groupId>
   <artifactId>mysql-connector-java</artifactId>
   <scope>runtime</scope>
   </dependency>
   </dependencies>

4. Configuración en application.properties
   spring.application.name=Examen1Back2
   spring.datasource.url=jdbc:mysql://localhost:3306/develop_db?useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

5. Crear una entidad de prueba
   @Entity
   public class Usuario {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private String nombre;
   private String correo;
   }

6. Crear repositorio
   public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {}

7. Probar la conexión
   En una clase CommandLineRunner insertar un registro para verificar que la conexión funciona.

8. Resultado esperado
- En la consola: "Usuario guardado correctamente en MySQL"
- En la base de datos develop_db, tabla usuario con el registro insertado.
