CREATE TABLE usuario (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE produto (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sku VARCHAR(255) UNIQUE NOT NULL,
    nome VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE deposito (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(255) UNIQUE NOT NULL,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255) NOT NULL
);

CREATE TABLE estoque (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    deposito_id BIGINT NOT NULL,
    quantidade INT NOT NULL,

    CONSTRAINT fk_estoque_produto
        FOREIGN KEY (produto_id) REFERENCES produto(id),

    CONSTRAINT fk_estoque_deposito
        FOREIGN KEY (deposito_id) REFERENCES deposito(id)
);

CREATE TABLE movimento_estoque (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    produto_id BIGINT NOT NULL,
    deposito_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    data_hora_movimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observacao TEXT,
    usuario_id BIGINT NOT NULL,

    CONSTRAINT fk_mov_produto
        FOREIGN KEY (produto_id) REFERENCES produto(id),

    CONSTRAINT fk_mov_deposito
        FOREIGN KEY (deposito_id) REFERENCES deposito(id),

    CONSTRAINT fk_mov_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);