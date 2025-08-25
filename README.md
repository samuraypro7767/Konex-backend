¡hecho! te dejo el README con una sección nueva de **Docker (Oracle Free)** que incluye exactamente los comandos que pasaste (en formato multilinea para Windows `cmd/PowerShell` y en una sola línea), más notas rápidas para conectar la app:

---

# Konex – Inventario y Ventas de Medicamentos

API REST para gestionar medicamentos, inventario, cotizaciones y ventas.
Incluye validaciones, manejo global de errores, migraciones con Flyway, documentación OpenAPI (Swagger) y verificación de cobertura con JaCoCo.

## Tech Stack

* **Java**: 17 (enforced por maven-enforcer)
* **Spring Boot**: 3.5.5 (Web, Data JPA, Validation)
* **DB**: Oracle (ojdbc11 23.3.0.23.09)
* **Migrations**: Flyway 11.11.2 (+ oracle plugin)
* **OpenAPI/Swagger**: springdoc-openapi 2.6.0
* **Lombok**: 1.18.34
* **Testing**: Spring Boot Test, Mockito Inline; **JaCoCo 0.8.12** con quality gate

## Estructura del proyecto

```
controller/                 -> REST controllers (MedicamentoController, VentaController, HealthController)
service/, service/impl/     -> Casos de uso (MedicamentoServiceImpl, VentaServiceImpl)
repository/                 -> Repositorios Spring Data JPA
model/                      -> Entidades JPA (Medicamento, Laboratorio, Venta, DetalleVenta)
dto/                        -> Requests/Responses (MedicamentoRequest/Response, CotizacionResponse, Venta* DTOs)
mapper/                     -> Entity <-> DTO
exception/                  -> NotFoundException, BusinessException, GlobalExceptionHandler
utils/                      -> Validators, DateRange
```

## Configuración (`application.properties` de ejemplo)

```properties
spring.application.name=Konex

# --- Oracle ---
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/FREEPDB1
spring.datasource.username=KONEX
spring.datasource.password=Konex\#2025
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# --- Hikari ---
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=30000

# --- JPA/Hibernate ---
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.open-in-view=false

# --- Server ---
server.port=8080

# --- Flyway ---
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.default-schema=KONEX
spring.flyway.schemas=KONEX
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-version=1
```

> **Nota Oracle**: en **Oracle Free 23c** el *service name* es `FREEPDB1`. En **Oracle XE** suele ser `XEPDB1`.

### Configuración (bloque alternativo copiable)

```properties
spring.application.name=Konex

# --- Oracle ---
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/FREEPDB1
spring.datasource.username=KONEX
spring.datasource.password=Konex\#2025
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# Hikari
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=30000

# --- JPA/Hibernate ---
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.open-in-view=false

# --- Server ---
server.port=8080

# --- Flyway ---
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.default-schema=KONEX
spring.flyway.schemas=KONEX
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-version=1
```

## Docker (Oracle Free)

### 1) Descargar imagen

```bash
docker pull gvenzl/oracle-free:latest
```

### 2) Levantar contenedor

**Windows (cmd/PowerShell, multilinea con `^`):**

```bash
docker run -d --name oracle-free ^
  -p 1521:1521 -p 5500:5500 ^
  -e ORACLE_PASSWORD="Konex#2025" ^
  -e APP_USER=KONEX ^
  -e APP_USER_PASSWORD="Konex#2025" ^
  gvenzl/oracle-free:latest
```

**Una sola línea (cualquier OS):**

```bash
docker run -d --name oracle-free -p 1521:1521 -p 5500:5500 -e ORACLE_PASSWORD="Konex#2025" -e APP_USER=KONEX -e APP_USER_PASSWORD="Konex#2025" gvenzl/oracle-free:latest
```

**Notas rápidas:**

* Crea el usuario de app **KONEX** con la clave dada.
* Puertos:

  * `1521`: Listener Oracle
  * `5500`: Enterprise Manager Express
* **JDBC URL** a usar en la app (coincide con el `application.properties`):

  ```
  jdbc:oracle:thin:@//localhost:1521/FREEPDB1
  ```
* **Credenciales app**: `username=KONEX`, `password=Konex#2025`.
* En el primer arranque puede tardar unos minutos en inicializar.

> Si quieres persistencia entre reinicios, agrega un volumen:
>
> ```bash
> -v oracle_free_data:/opt/oracle/oradata
> ```

## Migraciones Flyway

Coloca scripts en `src/main/resources/db/migration/`:

* `V1__init.sql`
* `V2__seed_data.sql`

*(En Oracle 12c+ puedes usar `IDENTITY`; si no, cambia a secuencias.)*

## Build, ejecución y cobertura

```bash
mvn clean verify          # tests + JaCoCo + quality gate
mvn spring-boot:run       # dev
mvn clean package && java -jar target/Konex-0.0.1-SNAPSHOT.jar

# Reporte cobertura
open target/site/jacoco/index.html
```

### JaCoCo Quality Gate (resumen)

* **Global**: LINE ≥ 0.60, BRANCH ≥ 0.45
* Excluidos del gate: `model`, `exception`, `config`, `*Application*`, `mapper`, `utils`
* Reporte HTML excluye además `dto/**`

## Endpoints principales

**Health**

* `GET /health` → `"OK"`

**Medicamentos** (`/api/medicamentos`)

* CRUD + listar paginado + cotizar + descontar stock
* Ver payload de ejemplo en el bloque original

**Ventas** (`/api/ventas`)

* `POST /api/ventas` (confirmar venta)
* `GET /api/ventas/all`
* `GET /api/ventas/{id}`
* `GET /api/ventas?desde=YYYY-MM-DD&hasta=YYYY-MM-DD&page=&size=`

## Errores globales

Manejados por `GlobalExceptionHandler` con estructura:

```json
{ "error": "Mensaje", "status": 400, "details": "Solo en 500" }
```

## CORS / Seguridad

`@CrossOrigin("*")` en dev; restringir en prod. Considera `@Version` si hay alta concurrencia.

## Swagger / OpenAPI

* UI: `http://localhost:8080/swagger-ui/index.html`
* JSON: `http://localhost:8080/v3/api-docs`

## Colecciones de API

* `Konex.postman_collection.json`
* `Konex.insomnia.json`
  (Usan `{{baseUrl}}`, por defecto `http://localhost:8080`)

---


