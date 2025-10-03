-- LOJAS (sem ID - gerado automaticamente)
INSERT INTO tb_Loja (nome, cnpj, segmento, logo, email, status) VALUES
('Loja CenterTech', '10234567000101', 'Tecnologia', 'https://drive.google.com/file/d/1qHS3CjGqHF0C3rTDLuMU_dG8dCzYgXrv/view?usp=drivesdk', 'contato@centertech.com', 'ATIVO'),
('Loja Estilo&Cia', '20345678000102', 'Moda', 'https://drive.google.com/file/d/1qQist6b6FVov1d5LbBVddDY3U6IAD264/view?usp=drivesdk', 'contato@estilocia.com', 'ATIVO'),
('Loja MundoLivros', '30456789000103', 'Livros', 'https://drive.google.com/file/d/1ua9emAVgEZIraJIHVhgpcMO0XL6qn8e5/view?usp=drivesdk', 'contato@mundolivros.com', 'ATIVO');

-- ADMIN E LOJISTAS (sem ID - gerado automaticamente)
INSERT INTO tb_Lojista (nome, cpf, email, senha, status, id_loja) VALUES
('Usuário Administrador', '12345678910', 'admin@doisv.com', '$2a$10$s/w9pVrBTGP9MWhfF1c68eorYnBB4wGK.eZ2iHGZGW01X71qkcKxq', 'ATIVO', 1),
('Carla Souza', '11122233344', 'carla.souza@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 2),
('Pedro Lima', '22233344455', 'pedro.lima@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 3),
('Juliana Martins', '33344455566', 'juliana.martins@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 1),
('Bruno Rocha', '44455566677', 'bruno.rocha@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 2),
('Renata Alves', '55566677788', 'renata.alves@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 3),
('Luciano Mendes', '66677788899', 'luciano.mendes@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 1),
('Patrícia Freitas', '77788899900', 'patricia.freitas@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 2),
('Eduardo Silva', '88899900011', 'eduardo.silva@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 3),
('Fernanda Oliveira', '99900011122', 'fernanda.oliveira@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 1),
('João Vieira', '00011122233', 'joao.vieira@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 2),
('Aline Costa', '11122233300', 'aline.costa@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 3),
('Rafael Teixeira', '22233344400', 'rafael.teixeira@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 1),
('Camila Duarte', '33344455500', 'camila.duarte@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 2),
('Diego Ramos', '44455566600', 'diego.ramos@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 3),
('Marina Lopes', '55566677700', 'marina.lopes@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', 1);

-- PRODUTOS (sem ID - gerado automaticamente)
INSERT INTO tb_Produto (descricao, unidade_medida, preco, status, id_loja, imagem) VALUES
('Mouse sem fio Logitech', 'UN', 79.90, 'ATIVO', 1, '1qHS3CjGqHF0C3rTDLuMU_dG8dCzYgXrv'),
('Notebook Lenovo i5', 'UN', 3299.00, 'ATIVO', 1, '1qQist6b6FVov1d5LbBVddDY3U6IAD264'),
('Cabo USB-C 1m', 'UN', 19.90, 'ATIVO', 1, '1ua9emAVgEZIraJIHVhgpcMO0XL6qn8e5'),
('Camisa Polo Masculina', 'UN', 49.90, 'ATIVO', 2, '1h4EitgpXr3VSVNOGXh3BAOS4MsyUomEu'),
('Tênis Casual Feminino', 'UN', 139.90, 'ATIVO', 2, '1ai3ayKbMx-aNXkklsKgudedY5mYhRzSs'),
('Calça Jeans Skinny', 'UN', 99.90, 'ATIVO', 2, '1-34lGTLrMpYY9X0trByV827gGhEKfRB0'),
('Livro - O Pequeno Príncipe', 'UN', 29.90, 'ATIVO', 3, '1jtxvTQXXh9n8JXX10iybZeNiSJp1bF9m'),
('Livro - 1984', 'UN', 39.90, 'ATIVO', 3, '1ZvK1F2CHQQCxJBlNjBtkEbxFgE3b2hAd'),
('Livro - Dom Casmurro', 'UN', 34.90, 'ATIVO', 3, '16vHTW43tZcYi7lPWkBh7GOT6Ds5OzQJD'),
('Fone Bluetooth JBL', 'UN', 199.90, 'ATIVO', 1, '1-ho3hIqUE8O0r1V3hEFhmC_jn2kSuYbS'),
('Blusa de Tricô Feminina', 'UN', 69.90, 'ATIVO', 2, '1WsFWmIFbyhazUCpnIQ4DVo6Y0c1kPQMc'),
('Relógio Smartwatch', 'UN', 249.00, 'ATIVO', 1, '18Gce-cQQ9q0KHTbFQObhQThKPyTmcLiQ'),
('Saia Jeans', 'UN', 59.90, 'ATIVO', 2, '1F2uodXRjD6tGjNObhcMpdvy0mmnzWUGO'),
('Livro - Código Limpo', 'UN', 89.90, 'ATIVO', 3, '1EhLiqIFxRZrMycDoCNacCBYwQiBEvRJd'),
('Carregador Portátil 10000mAh', 'UN', 89.00, 'ATIVO', 1, '16Ub2GOOmRGpgNdpzZU8M8CkJWeUHTaCe'),
('Livro - A Revolução dos Bichos', 'UN', 33.00, 'ATIVO', 3, '1L51eYYJFEDMGg4M1NgzWXXBZaROxAUQ'),
('Camiseta Básica', 'UN', 29.90, 'ATIVO', 2, '1TYACsKaTmTxGmC7x9FtEGh96knnwmjxs'),
('Monitor 24" Samsung', 'UN', 899.00, 'ATIVO', 1, '15lABDs-liGTrqcznHOX8HTRefN9tB0MO'),
('Vestido Longo', 'UN', 129.00, 'ATIVO', 2, '1xrp5UJkIHxN8-FPzmLmfDD_cAEB9zUxK'),
('Livro - O Hobbit', 'UN', 44.90, 'ATIVO', 3, '1yMEWRxmVlzReE1cjWVPU7sSdgLS3FyfC'),
('Teclado Mecânico Redragon', 'UN', 299.90, 'ATIVO', 1, '1b2Za1CyaWisZMSykFzWTKL3scPvmDCLY'),
('Blazer Feminino', 'UN', 159.00, 'ATIVO', 2, '1Vlt-ExQwlaOwGqvRlpg_M5nELhpw7q7W'),
('Livro - Mindset', 'UN', 54.90, 'ATIVO', 3, '1m24_93PGaHu8_9jwsTatGfa3cNMlQrWq'),
('Webcam Full HD', 'UN', 189.00, 'ATIVO', 1, '16JOslYBNXrd_R8WeNxQ6wFDFrhlNybFH'),
('Jaqueta Jeans', 'UN', 139.90, 'ATIVO', 2, '17stsfzWSzBouUboMHFLCigetayqtPjkd'),
('Livro - O Poder do Hábito', 'UN', 49.90, 'ATIVO', 3, '1wn9qbM7WBMIz45gdwQlN3udkL1nLed3m'),
('Cadeira Gamer', 'UN', 1199.00, 'ATIVO', 1, '1zaVLBofRdv1SYF9LvjBsTuIqyMT4NI-l'),
('Saia Midi', 'UN', 74.90, 'ATIVO', 2, '11xZaSMrCpXlJazwYy8WMxOwwAou9FcjD'),
('Livro - A Arte da Guerra', 'UN', 27.90, 'ATIVO', 3, '1Q8KV-4oUJic5rm0aFkAM93UTARmExpEI'),
('Pen Drive 64GB', 'UN', 49.00, 'ATIVO', 1, '1s5PDPUDd1KfBZBiBqre64Di2jXkGrdnA');

-- CONSUMIDORES (sem ID - gerado automaticamente)
INSERT INTO tb_Consumidor (nome, cpf_cnpj, email, celular, telefone, endereco, status, id_loja) VALUES
('Carlos Henrique', '12345678901', 'carlos.henrique@email.com', '(11) 98765-4321', '(11) 3456-7890', 'Rua das Flores, 123 - São Paulo/SP', 'ATIVO', 1),
('Juliana Mendes', '23456789012', 'juliana.mendes@email.com', '(21) 99876-5432', NULL, 'Av. Brasil, 456 - Rio de Janeiro/RJ', 'ATIVO', 2),
('Fernando Silva', '34567890123', 'fernando.silva@email.com', '(31) 91234-5678', '(31) 3222-1111', 'Rua Minas Gerais, 789 - Belo Horizonte/MG', 'ATIVO', 3),
('Ana Beatriz', '45678901234', 'ana.beatriz@email.com', '(51) 98700-1122', NULL, 'Rua João Goulart, 321 - Porto Alegre/RS', 'ATIVO', 1),
('Rafael Gomes', '56789012345', 'rafael.gomes@email.com', '(41) 99888-7766', '(41) 3030-2020', 'Rua XV de Novembro, 654 - Curitiba/PR', 'ATIVO', 2),
('Camila Rocha', '67890123456', 'camila.rocha@email.com', '(61) 91919-1919', NULL, 'SQS 305 Bloco E - Brasília/DF', 'ATIVO', 3),
('Marcos Vinícius', '78901234567', 'marcos.vinicius@email.com', '(85) 98787-1212', '(85) 3222-3333', 'Av. Beira Mar, 800 - Fortaleza/CE', 'ATIVO', 1),
('Patrícia Carvalho', '89012345678', 'patricia.carvalho@email.com', '(71) 92345-6789', NULL, 'Rua do Porto, 200 - Salvador/BA', 'ATIVO', 2),
('Lucas Almeida', '90123456789', 'lucas.almeida@email.com', '(91) 99999-0000', NULL, 'Travessa Tucuruí, 55 - Belém/PA', 'ATIVO', 3),
('Renata Figueiredo', '01234567890', 'renata.figueiredo@email.com', '(62) 98888-1234', '(62) 4002-8922', 'Av. Goiás, 1000 - Goiânia/GO', 'ATIVO', 1);