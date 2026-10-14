# accounts-service

Microservicio **accounts** del parcial 

1. **Paso 6** – Recibe `POST /api/accounts` (lo llama `users` de forma *sync*) y guarda la cuenta en la tabla `accounts` de TiDB.
2. **Paso 7** – Deja en el tópico Kafka `notificaciones` el mensaje de **"cuenta creada"** para que `notifiers` envíe el correo (*async*).

Misma arquitectura hexagonal del repo del profe: `domain` → `application` → `infrastructure`.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/accounts` | Crea la cuenta. Cuerpo: `{ "userId": "...", "email": "ana@gmail.com", "nombre": "Ana Pérez" }` → **201** |
| GET | `/api/accounts` | Lista las cuentas (para mostrar en el video) |
| GET | `/swagger-ui.html` | Swagger |

Respuestas de error: **400** cuerpo inválido, **409** el usuario ya tiene cuenta, **500** error de BD.

## Base de datos

Ejecutar una vez en el SQL Editor de TiDB (misma base `crud_usuarios`): [`src/main/resources/schema.sql`](src/main/resources/schema.sql).

## Variables de entorno (Render)

| Variable | Ejemplo |
|---|---|
| `DB_HOST` | `gateway01.us-east-1.prod.aws.tidbcloud.com` |
| `DB_PORT` | `4000` |
| `DB_NAME` | `crud_usuarios` |
| `DB_USERNAME` | `xxxxxxxx.root` |
| `DB_PASSWORD` | *(TiDB)* |
| `DB_SSL_MODE` | `REQUIRED` |
| `KAFKA_BOOTSTRAP_SERVERS` | `kafka-xxxx.aivencloud.com:12345` *(puerto SASL)* |
| `KAFKA_USERNAME` | `avnadmin` |
| `KAFKA_PASSWORD` | *(Aiven)* |
| `KAFKA_CA_CERT` | contenido completo de `ca.pem` (desde `-----BEGIN CERTIFICATE-----` hasta `-----END CERTIFICATE-----`) |

## Ejecutar en local

```bash
./mvnw test
./mvnw spring-boot:run   # puerto 8081
```

## Despliegue en Render

New → Web Service → conectar este repositorio → **Runtime: Docker** (usa el `Dockerfile`) → agregar las variables de arriba → Deploy.
