-- =============================================
-- Tabla accounts (misma base crud_usuarios de TiDB que usa users)
-- Ejecutar una sola vez en el SQL Editor de TiDB Cloud
-- =============================================

USE crud_usuarios;

CREATE TABLE IF NOT EXISTS accounts (
  id         VARCHAR(36)  NOT NULL PRIMARY KEY,
  user_id    VARCHAR(36)  NOT NULL UNIQUE,
  email      VARCHAR(150) NOT NULL,
  status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);
