CREATE DATABASE IF NOT EXISTS chancho_va;
USE chancho_va;

CREATE TABLE IF NOT EXISTS usuarios (
  usuario_id INT AUTO_INCREMENT PRIMARY KEY,
  nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
  contrasena_hash VARCHAR(255) NOT NULL,
  nombre_completo VARCHAR(150),
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS partidas (
  partida_id INT AUTO_INCREMENT PRIMARY KEY,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  estado ENUM('ESPERANDO','EN_JUEGO','FINALIZADA') DEFAULT 'ESPERANDO',
  max_jugadores TINYINT NOT NULL DEFAULT 6,
  prendas_por_perdedor TINYINT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS partida_jugadores (
  pj_id INT AUTO_INCREMENT PRIMARY KEY,
  partida_id INT NOT NULL,
  usuario_id INT NOT NULL,
  indice_turno TINYINT NOT NULL,
  prendas_acumuladas INT NOT NULL DEFAULT 0,
  eliminado BOOLEAN DEFAULT FALSE,
  mano_estado TEXT,
  FOREIGN KEY (partida_id) REFERENCES partidas(partida_id) ON DELETE CASCADE,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS rondas (
  ronda_id INT AUTO_INCREMENT PRIMARY KEY,
  partida_id INT NOT NULL,
  numero_ronda INT NOT NULL,
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (partida_id) REFERENCES partidas(partida_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS movimientos (
  movimiento_id INT AUTO_INCREMENT PRIMARY KEY,
  ronda_id INT NOT NULL,
  pj_id INT NOT NULL,
  carta VARCHAR(50) NOT NULL,
  orden_jugada TINYINT NOT NULL,
  accion ENUM('JUGAR','CHANCHO','APOYAR_MANO','RECIBIR_PRENDA') DEFAULT 'JUGAR',
  creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (ronda_id) REFERENCES rondas(ronda_id) ON DELETE CASCADE,
  FOREIGN KEY (pj_id) REFERENCES partida_jugadores(pj_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS historial_partidas (
  historial_id INT AUTO_INCREMENT PRIMARY KEY,
  partida_id INT NOT NULL,
  ganador_usuario_id INT NULL,
  perdedor_usuario_id INT NULL,
  resumen TEXT,
  finalizada_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (partida_id) REFERENCES partidas(partida_id) ON DELETE CASCADE,
  FOREIGN KEY (ganador_usuario_id) REFERENCES usuarios(usuario_id),
  FOREIGN KEY (perdedor_usuario_id) REFERENCES usuarios(usuario_id)
);
