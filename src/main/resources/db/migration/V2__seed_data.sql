INSERT INTO usuario (nome, email, senha)
VALUES ('Teste Usuario', 'teste@email.com', '$2a$10$iLSBCmppgugO7.z69MheieEaZ1XKxF66DnkQTysl4yOa0DAqucEKC');

INSERT INTO produto (sku, nome, ativo) VALUES
('SKU001', 'Camiseta Azul', true),
('SKU002', 'Calça Preta', true),
('SKU003', 'Tênis Branco', true);

INSERT INTO deposito (codigo, nome, endereco) VALUES
('DEP-01', 'Depósito Central', 'Rua A, 123'),
('DEP-02', 'Depósito Secundário', 'Rua B, 456');

INSERT INTO estoque (produto_id, deposito_id, quantidade) VALUES
(1, 1, 50),
(2, 1, 30),
(3, 1, 20);

INSERT INTO estoque (produto_id, deposito_id, quantidade) VALUES
(1, 2, 10),
(2, 2, 5),
(3, 2, 15);

INSERT INTO movimento_estoque (tipo, produto_id, deposito_id, quantidade, observacao, usuario_id)
VALUES
('ENTRADA', 1, 1, 50, 'Estoque inicial automático', 1),
('ENTRADA', 2, 1, 30, 'Estoque inicial automático', 1),
('ENTRADA', 3, 1, 20, 'Estoque inicial automático', 1);

INSERT INTO movimento_estoque (tipo, produto_id, deposito_id, quantidade, observacao, usuario_id)
VALUES
('ENTRADA', 1, 2, 10, 'Estoque inicial automático', 1),
('ENTRADA', 2, 2, 5, 'Estoque inicial automático', 1),
('ENTRADA', 3, 2, 15, 'Estoque inicial automático', 1);