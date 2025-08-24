-- LOJAS
INSERT IGNORE INTO tb_Loja (id_loja, nome, cnpj, segmento, logo, email, status) VALUES
(1, 'Loja CenterTech', '10234567000101', 'Tecnologia', 'https://drive.google.com/file/d/1qHS3CjGqHF0C3rTDLuMU_dG8dCzYgXrv/view?usp=drivesdk', 'contato@centertech.com', 'ATIVO'),
(2, 'Loja Estilo&Cia', '20345678000102', 'Moda', 'https://drive.google.com/file/d/1qQist6b6FVov1d5LbBVddDY3U6IAD264/view?usp=drivesdk', 'contato@estilocia.com', 'ATIVO'),
(3, 'Loja MundoLivros', '30456789000103', 'Livros', 'https://drive.google.com/file/d/1ua9emAVgEZIraJIHVhgpcMO0XL6qn8e5/view?usp=drivesdk', 'contato@mundolivros.com', 'ATIVO');

-- ADMIN
INSERT IGNORE INTO tb_Lojista (id_lojista, nome, cpf, email, senha, status, id_loja) VALUES
(1, 'Usuário Administrador', '12345678910', 'admin@doisv.com', '$2a$10$s/w9pVrBTGP9MWhfF1c68eorYnBB4wGK.eZ2iHGZGW01X71qkcKxq', 'ATIVO', 1);

-- LOJISTAS (senha = 'teste')
INSERT IGNORE INTO tb_Lojista (id_lojista, nome, cpf, email, senha, status, id_loja) VALUES
(2, 'Carla Souza', '11122233344', 'carla.souza@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 2),
(3, 'Pedro Lima', '22233344455', 'pedro.lima@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 3),
(4, 'Juliana Martins', '33344455566', 'juliana.martins@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 1),
(5, 'Bruno Rocha', '44455566677', 'bruno.rocha@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 2),
(6, 'Renata Alves', '55566677788', 'renata.alves@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 3),
(7, 'Luciano Mendes', '66677788899', 'luciano.mendes@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 1),
(8, 'Patrícia Freitas', '77788899900', 'patricia.freitas@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 2),
(9, 'Eduardo Silva', '88899900011', 'eduardo.silva@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 3),
(10, 'Fernanda Oliveira', '99900011122', 'fernanda.oliveira@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 1),
(11, 'João Vieira', '00011122233', 'joao.vieira@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 2),
(12, 'Aline Costa', '11122233300', 'aline.costa@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 3),
(13, 'Rafael Teixeira', '22233344400', 'rafael.teixeira@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 1),
(14, 'Camila Duarte', '33344455500', 'camila.duarte@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 2),
(15, 'Diego Ramos', '44455566600', 'diego.ramos@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 3),
(16, 'Marina Lopes', '55566677700', 'marina.lopes@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 1);

-- PRODUTOS
INSERT IGNORE INTO tb_Produto (id_produto, descricao, unidade_medida, preco, status, id_loja, imagem) VALUES
(1, 'Mouse sem fio Logitech', 'UN', 79.90, 'ATIVO', 1, '1h4EitgpXr3VSVNOGXh3BAOS4MsyUomEu'),
(2, 'Notebook Lenovo i5', 'UN', 3299.00, 'ATIVO', 1, '1ai3ayKbMx-aNXkklsKgudedY5mYhRzSs'),
(3, 'Cabo USB-C 1m', 'UN', 19.90, 'ATIVO', 1, '1-34lGTLrMpYY9X0trByV827gGhEKfRB0'),
(4, 'Camisa Polo Masculina', 'UN', 49.90, 'ATIVO', 2, '1jtxvTQXXh9n8JXX10iybZeNiSJp1bF9m'),
(5, 'Tênis Casual Feminino', 'UN', 139.90, 'ATIVO', 2, '1ZvK1F2CHQQCxJBlNjBtkEbxFgE3b2hAd'),
(6, 'Calça Jeans Skinny', 'UN', 99.90, 'ATIVO', 2, '16vHTW43tZcYi7lPWkBh7GOT6Ds5OzQJD'),
(7, 'Livro - O Pequeno Príncipe', 'UN', 29.90, 'ATIVO', 3, '1-ho3hIqUE8O0r1V3hEFhmC_jn2kSuYbS'),
(8, 'Livro - 1984', 'UN', 39.90, 'ATIVO', 3, '1WsFWmIFbyhazUCpnIQ4DVo6Y0c1kPQMc'),
(9, 'Livro - Dom Casmurro', 'UN', 34.90, 'ATIVO', 3, '18Gce-cQQ9q0KHTbFQObhQThKPyTmcLiQ'),
(10, 'Fone Bluetooth JBL', 'UN', 199.90, 'ATIVO', 1, '1F2uodXRjD6tGjNObhcMpdvy0mmnzWUGO'),
(11, 'Blusa de Tricô Feminina', 'UN', 69.90, 'ATIVO', 2, '1EhLiqIFxRZrMycDoCNacCBYwQiBEvRJd'),
(12, 'Relógio Smartwatch', 'UN', 249.00, 'ATIVO', 1, '16Ub2GOOmRGpgNdpzZU8M8CkJWeUHTaCe'),
(13, 'Saia Jeans', 'UN', 59.90, 'ATIVO', 2, '1L51eYYJFEDMGg4M1NgzWXXBZaROxAUQ'),
(14, 'Livro - Código Limpo', 'UN', 89.90, 'ATIVO', 3, '1TYACsKaTmTxGmC7x9FtEGh96knnwmjxs'),
(15, 'Carregador Portátil 10000mAh', 'UN', 89.00, 'ATIVO', 1, '15lABDs-liGTrqcznHOX8HTRefN9tB0MO'),
(16, 'Livro - A Revolução dos Bichos', 'UN', 33.00, 'ATIVO', 3, '1xrp5UJkIHxN8-FPzmLmfDD_cAEB9zUxK'),
(17, 'Camiseta Básica', 'UN', 29.90, 'ATIVO', 2, '1yMEWRxmVlzReE1cjWVPU7sSdgLS3FyfC'),
(18, 'Monitor 24" Samsung', 'UN', 899.00, 'ATIVO', 1, '1b2Za1CyaWisZMSykFzWTKL3scPvmDCLY'),
(19, 'Vestido Longo', 'UN', 129.00, 'ATIVO', 2, '1Vlt-ExQwlaOwGqvRlpg_M5nELhpw7q7W'),
(20, 'Livro - O Hobbit', 'UN', 44.90, 'ATIVO', 3, '1m24_93PGaHu8_9jwsTatGfa3cNMlQrWq'),
(21, 'Teclado Mecânico Redragon', 'UN', 299.90, 'ATIVO', 1, '16JOslYBNXrd_R8WeNxQ6wFDFrhlNybFH'),
(22, 'Blazer Feminino', 'UN', 159.00, 'ATIVO', 2, '17stsfzWSzBouUboMHFLCigetayqtPjkd'),
(23, 'Livro - Mindset', 'UN', 54.90, 'ATIVO', 3, '1wn9qbM7WBMIz45gdwQlN3udkL1nLed3m'),
(24, 'Webcam Full HD', 'UN', 189.00, 'ATIVO', 1, '1zaVLBofRdv1SYF9LvjBsTuIqyMT4NI-l'),
(25, 'Jaqueta Jeans', 'UN', 139.90, 'ATIVO', 2, '11xZaSMrCpXlJazwYy8WMxOwwAou9FcjD'),
(26, 'Livro - O Poder do Hábito', 'UN', 49.90, 'ATIVO', 3, '1Q8KV-4oUJic5rm0aFkAM93UTARmExpEI'),
(27, 'Cadeira Gamer', 'UN', 1199.00, 'ATIVO', 1, '1s5PDPUDd1KfBZBiBqre64Di2jXkGrdnA'),
(28, 'Saia Midi', 'UN', 74.90, 'ATIVO', 2, '1nvtIeHEkU8N4HOzPE7dRq1r4ZEk1zvbs'),
(29, 'Livro - A Arte da Guerra', 'UN', 27.90, 'ATIVO', 3, '1FY2sYRwJd4XwPneDuTR504KW0Z_a_yhT'),
(30, 'Pen Drive 64GB', 'UN', 49.00, 'ATIVO', 1, '1mdL_BkPU19n06j0KeF53UjNXD1GJLh7n');

-- CONSUMIDORES
INSERT IGNORE INTO tb_Consumidor (id_consumidor, nome, cpf_cnpj, email, celular, telefone, endereco, status, id_loja) VALUES
(1, 'Carlos Henrique', '12345678901', 'carlos.henrique@email.com', '(11) 98765-4321', '(11) 3456-7890', 'Rua das Flores, 123 - São Paulo/SP', 'ATIVO', 1),
(2, 'Juliana Mendes', '23456789012', 'juliana.mendes@email.com', '(21) 99876-5432', NULL, 'Av. Brasil, 456 - Rio de Janeiro/RJ', 'ATIVO', 2),
(3, 'Fernando Silva', '34567890123', 'fernando.silva@email.com', '(31) 91234-5678', '(31) 3222-1111', 'Rua Minas Gerais, 789 - Belo Horizonte/MG', 'ATIVO', 3),
(4, 'Ana Beatriz', '45678901234', 'ana.beatriz@email.com', '(51) 98700-1122', NULL, 'Rua João Goulart, 321 - Porto Alegre/RS', 'ATIVO', 1),
(5, 'Rafael Gomes', '56789012345', 'rafael.gomes@email.com', '(41) 99888-7766', '(41) 3030-2020', 'Rua XV de Novembro, 654 - Curitiba/PR', 'ATIVO', 2),
(6, 'Camila Rocha', '67890123456', 'camila.rocha@email.com', '(61) 91919-1919', NULL, 'SQS 305 Bloco E - Brasília/DF', 'ATIVO', 3),
(7, 'Marcos Vinícius', '78901234567', 'marcos.vinicius@email.com', '(85) 98787-1212', '(85) 3222-3333', 'Av. Beira Mar, 800 - Fortaleza/CE', 'ATIVO', 1),
(8, 'Patrícia Carvalho', '89012345678', 'patricia.carvalho@email.com', '(71) 92345-6789', NULL, 'Rua do Porto, 200 - Salvador/BA', 'ATIVO', 2),
(9, 'Lucas Almeida', '90123456789', 'lucas.almeida@email.com', '(91) 99999-0000', NULL, 'Travessa Tucuruí, 55 - Belém/PA', 'ATIVO', 3),
(10, 'Renata Figueiredo', '01234567890', 'renata.figueiredo@email.com', '(62) 98888-1234', '(62) 4002-8922', 'Av. Goiás, 1000 - Goiânia/GO', 'ATIVO', 1);
