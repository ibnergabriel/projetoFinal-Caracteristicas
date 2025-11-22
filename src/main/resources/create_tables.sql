USE Proyeto;

CREATE TABLE Aluno(
idAluno INT PRIMARY KEY,
nome VARCHAR(100),
idade INT,
telefone VARCHAR(20),
email VARCHAR(100),
data_matricula DATE
);

CREATE TABLE Professor(
idProfessor INT PRIMARY KEY,
nome VARCHAR(100),
CREF VARCHAR(50),
telefone VARCHAR(20),
email VARCHAR(100),
data_matricula DATE,
usuario VARCHAR(50),
senha VARCHAR(100)
);


CREATE TABLE Treino (
    idTreino INT PRIMARY KEY,
    idAluno INT NOT NULL,
    idProfessor INT NOT NULL,
    descricao VARCHAR(100),
    data_inicio DATE,
    data_fim DATE,
    
    -- (Chaves Estrangeiras)
    CONSTRAINT fk_treino_aluno FOREIGN KEY (idAluno) REFERENCES Aluno(idAluno),
    CONSTRAINT fk_treino_professor FOREIGN KEY (idProfessor) REFERENCES Professor(idProfessor)
);


CREATE TABLE Pagamento (
    idPagamento INT PRIMARY KEY,
    idAluno INT NOT NULL,
    data_pagamento DATE,
    valor INT,
    
    -- (Chave Estrangeira)
    CONSTRAINT fk_pagamento_aluno FOREIGN KEY (idAluno) REFERENCES Aluno(idAluno)
);
