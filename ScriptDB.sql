CREATE DATABASE Eleve;
USE Eleve;

CREATE TABLE Usuario (
	id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100),
    email VARCHAR(256),
    senha VARCHAR(100)
);

SELECT * FROM Usuario;