-- Crear la base de datos
CREATE DATABASE ControlActividadesBD;
GO

-- Usar la base de datos
USE ControlActividadesBD;
GO

-- Tabla: Role
CREATE TABLE Role (
    idRole INT IDENTITY(1,1) NOT NULL,
    name VARCHAR(50) NOT NULL,
    status INT NOT NULL,
    description VARCHAR(100) NULL,
    PRIMARY KEY CLUSTERED (idRole ASC)
);
GO

-- Tabla: Users
CREATE TABLE Users (
    idUser INT IDENTITY(1,1) NOT NULL,
    name VARCHAR(100) NOT NULL,
    passwordHash VARCHAR(64) NOT NULL,
    email VARCHAR(200) NOT NULL,
    status TINYINT NOT NULL,
    idRole INT NOT NULL,
    PRIMARY KEY CLUSTERED (idUser ASC),
    UNIQUE (email)
);
GO

-- Tabla: UserHistory
CREATE TABLE UserHistory (
    idHistory INT IDENTITY(1,1) NOT NULL,
    idUser INT NOT NULL,
    action VARCHAR(255) NOT NULL,
    timestamp DATETIME NULL,
    status INT NOT NULL,
    details VARCHAR(MAX) NULL,
    PRIMARY KEY CLUSTERED (idHistory ASC)
);
GO
