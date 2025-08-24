
# Konex – Inventario y Ventas de Medicamentos

API REST para gestionar medicamentos, inventario, cotizaciones y ventas.  
Incluye validaciones, manejo global de errores, migraciones con Flyway, documentación OpenAPI (Swagger) y verificación de cobertura con JaCoCo.

---

## Tech Stack

- **Java**: 17 (enforced por maven-enforcer)
- **Spring Boot**: 3.5.5
  - Web, Data JPA, Validation
- **DB**: Oracle (ojdbc11 23.3.0.23.09)
- **Migrations**: Flyway 11.11.2 (+ oracle plugin)
- **OpenAPI/Swagger**: springdoc-openapi 2.6.0
- **Lombok**: 1.18.34
- **Testing**: Spring Boot Test, Mockito Inline; **JaCoCo 0.8.12** con quality gate

---

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

---

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

> **Nota Oracle**: si usas Oracle XE por defecto, el service name suele ser `XEPDB1` en lugar de `FREEPDB1`.

---

## Migraciones Flyway

Ubica los scripts en `src/main/resources/db/migration/`:

- `V1__init.sql`
- `V2__seed_data.sql`

> **Oracle**: las entidades usan `GenerationType.IDENTITY`. Asegura versión 12c+ o cambia a secuencias.

---

## Build, ejecución y cobertura

```bash
# Compilar + tests + cobertura + quality gate
mvn clean verify

# Ejecutar en dev
mvn spring-boot:run

# Empaquetar y ejecutar JAR
mvn clean package
java -jar target/Konex-0.0.1-SNAPSHOT.jar

# Reporte HTML de cobertura
open target/site/jacoco/index.html
```

### JaCoCo Quality Gate (resumen del POM)

- Gate global mínimo:
  - LINE COVEREDRATIO ≥ **0.60**
  - BRANCH COVEREDRATIO ≥ **0.45**
- Excluidos del gate: `model`, `exception`, `config`, `*Application*`, `mapper`, `utils`
- Reporte HTML excluye además `dto/**`

---

## Endpoints principales

### Health
- **GET** `/health` → `"OK"`

### Medicamentos (base: `/api/medicamentos`)
- **POST** `/api/medicamentos` — crear
- **PUT** `/api/medicamentos/{id}` — actualizar
- **DELETE** `/api/medicamentos/{id}` — eliminar lógico (`activo = 0`)
- **GET** `/api/medicamentos/{id}` — obtener por id (solo activos)
- **PATCH** `/api/medicamentos/{id}/inactivar` — inactivar (alias de eliminar lógico)
- **GET** `/api/medicamentos` — listar paginado + filtro `nombre`
  - Query: `?nombre=&page=&size=`
- **GET** `/api/medicamentos/{id}/cotizar?cantidad=#` — cotización (no altera stock)
- **PATCH** `/api/medicamentos/{id}/descontar?cantidad=#` — descuenta stock

**Payload ejemplo (POST/PUT `/api/medicamentos`):**
```json
{
  "nombre": "Ibuprofeno 400mg",
  "laboratorioId": 1,
  "fechaFabricacion": "2025-01-10",
  "fechaVencimiento": "2027-01-10",
  "cantidadStock": 500,
  "valorUnitario": 1200.50
}
```

### Ventas (base: `/api/ventas`)
- **POST** `/api/ventas` — confirmar venta  
  **Body:**
  ```json
  { "medicamentoId": 1, "cantidad": 3 }
  ```
- **GET** `/api/ventas/all` — listar todas sin paginar
- **GET** `/api/ventas/{id}` — detalle por id
- **GET** `/api/ventas?desde=YYYY-MM-DD&hasta=YYYY-MM-DD&page=&size=` — listar por rango (paginado)
  - El servicio expande a `[desde 00:00:00, hasta 23:59:59.999999999]`.

---

## Manejo global de errores

`GlobalExceptionHandler` mapea:
- `NotFoundException` → **404**
- `BusinessException` → **400**
- `MethodArgumentNotValidException` → **400** (primer error de campo)
- Cualquier otra excepción → **500** con `details`

**Estructura de error:**
```json
{ "error": "Mensaje", "status": 400, "details": "Solo en 500" }
```

---

## CORS / Seguridad

- `@CrossOrigin("*")` en controladores (útil en desarrollo).  
  Restringir en producción a dominios confiables.
- Considera control de versiones (`@Version`) o bloqueos si hay alta concurrencia en stock.

---

## Swagger / OpenAPI

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

---

## Colecciones de API

En este repo encontrarás:
- **Konex.postman_collection.json** — importable en Postman
- **Konex.insomnia.json** — export de workspace para Insomnia

Cada colección usa la variable `{{baseUrl}}` (por defecto `http://localhost:8080`).

---

© Konex. Ajusta licencia/autores según corresponda.
