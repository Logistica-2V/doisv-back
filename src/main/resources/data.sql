-- LOJAS
INSERT IGNORE INTO tb_Loja (id_loja, nome, cnpj, segmento, logo, email, status, id_publico) VALUES
(1, 'Loja CenterTech', 'RKO5GZPX000178', 'Tecnologia', '1qHS3CjGqHF0C3rTDLuMU_dG8dCzYgXrv', 'contato@centertech.com', 'ATIVO', UUID()),
(2, 'Loja Estilo&Cia', '35420723000124', 'Moda', '1qQist6b6FVov1d5LbBVddDY3U6IAD264', 'contato@estilocia.com', 'ATIVO', UUID()),
(3, 'Loja MundoLivros', 'NWFO17KR000186', 'Livros', '1ua9emAVgEZIraJIHVhgpcMO0XL6qn8e5', 'contato@mundolivros.com', 'ATIVO', UUID());

-- ADMIN (admin = true)
INSERT IGNORE INTO tb_Lojista (id_lojista, nome, cpf, email, senha, status, admin, id_loja) VALUES
(1, 'Usuário Administrador', '03032792061', 'admin@doisv.com', '$2a$10$s/w9pVrBTGP9MWhfF1c68eorYnBB4wGK.eZ2iHGZGW01X71qkcKxq', 'ATIVO', true, 1);

-- LOJISTAS (todos com admin = false)
INSERT IGNORE INTO tb_Lojista (id_lojista, nome, cpf, email, senha, status, admin, id_loja) VALUES
(2, 'Carla Souza', '76196945017', 'carla.souza@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', false, 2),
(3, 'Pedro Lima', '35749998010', 'pedro.lima@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', true, 3),
(4, 'Juliana Martins', '08233781010', 'juliana.martins@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', false, 1),
(5, 'Bruno Rocha', '50543505006', 'marcelo_willian@icloud.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', true, 2),
(6, 'Renata Alves', '51340979098', 'renata.alves@email.com', '$2a$10$GVrZrk.vF0EA9dcblX.Q8.PBtsQEMrWUzrGq6YBn5IrRJ2h5eC0zu', 'ATIVO', false, 3);

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
(1, 'Carlos Henrique', '82771935058', 'carlos.henrique@email.com', '11987654321', '1134567890', 'Rua das Flores, 123 - São Paulo/SP', 'ATIVO', 1),
(2, 'Juliana Mendes', '73466938090', 'juliana.mendes@email.com', '21998765432', NULL, 'Av. Brasil, 456 - Rio de Janeiro/RJ', 'ATIVO', 2),
(3, 'Fernando Silva', '71831158019', 'fernando.silva@email.com', '31912345678', '3132221111', 'Rua Minas Gerais, 789 - Belo Horizonte/MG', 'ATIVO', 3),
(4, 'Ana Beatriz', '02546517040', 'ana.beatriz@email.com', '51987001122', NULL, 'Rua João Goulart, 321 - Porto Alegre/RS', 'ATIVO', 1),
(5, 'Rafael Gomes', '21101893044', 'rafael.gomes@email.com', '41998887766', '4130302020', 'Rua XV de Novembro, 654 - Curitiba/PR', 'ATIVO', 2),
(6, 'Camila Rocha', '28912505084', 'camila.rocha@email.com', '61919191919', NULL, 'SQS 305 Bloco E - Brasília/DF', 'ATIVO', 3),
(7, 'Marcelo William', '85515937066', 'marcelo.sousa@uscsonline.com.br', '85987871212', '8532223333', 'Av. Beira Mar, 800 - Fortaleza/CE', 'ATIVO', 1),
(8, 'Patrícia Carvalho', '08448589009', 'patricia.carvalho@email.com', '71923456789', NULL, 'Rua do Porto, 200 - Salvador/BA', 'ATIVO', 2),
(9, 'Lucas Almeida', '26360658062', 'lucas.almeida@email.com', '91999990000', NULL, 'Travessa Tucuruí, 55 - Belém/PA', 'ATIVO', 3),
(10, 'Renata Figueiredo', '00415751055', 'renata.figueiredo@email.com', '62988881234', '6240028922', 'Av. Goiás, 1000 - Goiânia/GO', 'ATIVO', 1);

--VENDAS
INSERT IGNORE INTO tb_Venda
(id, data_criacao, data_entrega, desconto, forma_pagamento, prazo_devolucao, prazo_troca, preco_total, senha, serial_venda, status, status_pedido, id_consumidor, id_loja)
VALUES
(1, '2025-12-10 10:15:00', '2025-12-15 14:00:00', 0.00, 'CREDITO', 7, 30, 3378.90, '$2a$10$yrORnoO.6CpHF3zk0rpsIOIv9oiEWj16NDx.00FwRRAhjKcUpS3VC', '8f2e3d4a5c', 'ATIVO', 'ENTREGUE', 1, 1),
(2, '2026-04-28 14:30:00', NULL, 10.00, 'PIX', 7, 30, 289.80, NULL, NULL, 'ATIVO', 'EM_ANDAMENTO', 2, 2),
(3, '2026-05-01 09:00:00', NULL, 5.00, 'BOLETO', 7, 30, 64.80, NULL, NULL, 'ATIVO', 'EM_ANDAMENTO', 3, 3),
(4, '2026-05-02 18:45:00', NULL, 0.00, 'PIX', 7, 30, 19.90, NULL, NULL, 'ATIVO', 'EM_ANDAMENTO', 4, 1),
(5, '2025-12-20 11:20:00', '2025-12-24 09:30:00', 0.00, 'DEBITO', 7, 30, 139.90, '$2a$10$a1sX37tLUnP5XLJgEo8Nsu5O7oFmKaa1l0ChDbEtSEbodVC6DPS7m', 'b1c2d3e4f5', 'ATIVO', 'ENTREGUE', 5, 2),
(6, '2026-01-05 16:10:00', NULL, 0.00, 'CREDITO', 7, 30, 34.90, NULL, NULL, 'INATIVO', 'CANCELADA', 6, 3),
(7, '2026-01-12 13:00:00', '2026-01-18 16:20:00', 50.00, 'CREDITO', 7, 30, 1199.00, '$2a$10$MYXUFMXTkS/eJdxOphkKEudSTUCiNBbXmey2OpUy90BVIYrzmJHy2', '7a8b9c0d1e', 'ATIVO', 'ENTREGUE', 7, 1),
(8, '2026-04-25 08:30:00', NULL, 0.00, 'PIX', 7, 30, 159.00, NULL, NULL, 'ATIVO', 'EM_ANDAMENTO', 8, 2),
(9, '2026-02-10 09:45:00', '2026-02-14 11:00:00', 10.00, 'DEBITO', 7, 30, 82.80, '$2a$10$2MIi1J7AsF.OFN/u939a9eDSgl95980Wenk0RbLlG.evdjvX56RrO', '4f5e6d7c8b', 'ATIVO', 'ENTREGUE', 9, 3),
(10, '2026-05-03 15:55:00', NULL, 0.00, 'CREDITO', 7, 30, 249.00, NULL, NULL, 'ATIVO', 'EM_ANDAMENTO', 10, 1),
(11, '2025-12-25 10:00:00', '2025-12-30 15:00:00', 0.00, 'PIX', 7, 30, 1198.90, '$2a$10$VENDATESTE1234567890qwertyuiopasdfghjklz', '1a2b3c4d5e', 'ATIVO', 'ENTREGUE', 1, 1),
(12, '2026-01-20 14:00:00', '2026-01-25 10:30:00', 0.00, 'CREDITO', 7, 30, 129.00, '$2a$10$VENDATESTE0987654321poiuytrewqasdfghjklz', '2b3c4d5e6f', 'ATIVO', 'ENTREGUE', 2, 2),
(13, '2026-02-05 09:15:00', '2026-02-09 14:45:00', 0.00, 'PIX', 7, 30, 89.90, '$2a$10$VENDATESTEzxcvbnm123456lkjhgfdsaqwertyui', '3c4d5e6f7g', 'ATIVO', 'ENTREGUE', 3, 3),
(14, '2026-02-20 11:30:00', '2026-02-24 09:00:00', 0.00, 'DEBITO', 7, 30, 189.00, '$2a$10$VENDATESTEMNBVCXZ098765lkjhgfdsaqwertyui', '4d5e6f7g8h', 'ATIVO', 'ENTREGUE', 4, 1),
(15, '2026-03-01 16:45:00', '2026-03-06 13:20:00', 0.00, 'CREDITO', 7, 30, 169.80, '$2a$10$VENDATESTEasdfghjkl1234567890qwertyuiopz', '5e6f7g8h9i', 'ATIVO', 'ENTREGUE', 5, 2),
(16, '2025-12-18 08:20:00', '2025-12-22 17:10:00', 0.00, 'PIX', 7, 30, 82.80, '$2a$10$VENDATESTElkjhgfdsa0987654321qwertyuiopz', '6f7g8h9i0j', 'ATIVO', 'ENTREGUE', 6, 3),
(17, '2026-03-10 10:05:00', '2026-03-14 11:30:00', 0.00, 'CREDITO', 7, 30, 199.90, '$2a$10$VENDATESTE1234567890qwertyuiopasdfghjklz', '7g8h9i0j1k', 'ATIVO', 'ENTREGUE', 7, 1),
(18, '2026-03-25 14:50:00', '2026-03-29 09:15:00', 0.00, 'BOLETO', 7, 30, 74.90, NULL, NULL, 'ATIVO', 'ENTREGUE', 8, 2),
(19, '2026-04-10 12:10:00', '2026-04-15 15:00:00', 0.00, 'PIX', 7, 30, 49.90, NULL, NULL, 'ATIVO', 'ENTREGUE', 9, 3),
(20, '2026-04-20 17:30:00', '2026-04-24 10:45:00', 0.00, 'DEBITO', 7, 30, 89.00, NULL, NULL, 'ATIVO', 'ENTREGUE', 10, 1),
(21, '2026-04-05 10:20:00', '2026-04-10 14:00:00', 0.00, 'PIX', 7, 30, 99.80, '$2a$10$VENDATESTE21qweasdzxc1234567890poiuytre', '8h9i0j1k2l', 'ATIVO', 'ENTREGUE', 2, 1),
(22, '2026-05-01 11:45:00', NULL, 0.00, 'CREDITO', 7, 30, 99.90, NULL, NULL, 'ATIVO', 'EM_ANDAMENTO', 4, 2),
(23, '2025-12-15 09:10:00', '2025-12-20 16:30:00', 5.00, 'BOLETO', 7, 30, 34.90, '$2a$10$VENDATESTE23lkjhgfdsa0987654321mnbvcxz', '9i0j1k2l3m', 'ATIVO', 'ENTREGUE', 6, 3),
(24, '2026-02-18 14:30:00', '2026-02-22 10:15:00', 100.00, 'PIX', 7, 30, 3199.00, '$2a$10$VENDATESTE24poiuytrewqasdfghjklzxcvbnm', '0j1k2l3m4n', 'ATIVO', 'ENTREGUE', 8, 1),
(25, '2026-03-22 16:00:00', '2026-03-26 13:40:00', 0.00, 'CREDITO', 7, 30, 129.80, '$2a$10$VENDATESTE25mnbvcxzlkjhgfdsapoiuytrewq', '1k2l3m4n5o', 'ATIVO', 'ENTREGUE', 10, 2),
(26, '2026-04-30 08:50:00', NULL, 0.00, 'DEBITO', 7, 30, 89.90, NULL, NULL, 'ATIVO', 'EM_ANDAMENTO', 1, 3),
(27, '2026-01-10 13:20:00', '2026-01-15 11:00:00', 0.00, 'PIX', 7, 30, 249.00, '$2a$10$VENDATESTE27zxcvbnmasdfghjklqwertyuiop', '2l3m4n5o6p', 'ATIVO', 'ENTREGUE', 3, 1),
(28, '2026-04-12 15:10:00', '2026-04-16 17:25:00', 0.00, 'CREDITO', 7, 30, 49.90, '$2a$10$VENDATESTE28qwertyuiopasdfghjklzxcvbnm', '3m4n5o6p7q', 'ATIVO', 'ENTREGUE', 5, 2),
(29, '2026-05-02 09:30:00', NULL, 0.00, 'PIX', 7, 30, 33.00, NULL, NULL, 'INATIVO', 'CANCELADA', 7, 3),
(30, '2026-03-05 11:05:00', '2026-03-09 14:50:00', 0.00, 'DEBITO', 7, 30, 138.00, '$2a$10$VENDATESTE30asdfghjklpoiuytrewqzxcvbnm', '4n5o6p7q8r', 'ATIVO', 'ENTREGUE', 9, 1);

--ITENS VENDA
INSERT IGNORE INTO tb_Item_Venda
(id, detalhes, percentual_variacao, preco_original, preco_vendido, quantidade, status, id_produto, id_venda)
VALUES
-- Venda 1 (Total: 3378.90 | Loja Tech) -- Notebook Lenovo (3299.00) + Mouse (79.90) = 3378.90
(1, 'Notebook para trabalho', 1.00, 3299.00, 3299.00, 1.0, 'ATIVO', 2, 1),
(2, 'Acessório incluso', 1.00, 79.90, 79.90, 1.0, 'ATIVO', 1, 1),
-- Venda 2 (Total: 289.80 | Loja Moda) -- 2 Tênis com leve ajuste de preço (144.90 * 2 = 289.80) | Orig: 139.90
(3, 'Par de tênis promocional', 1.04, 139.90, 144.90, 2.0, 'ATIVO', 5, 2),
-- Venda 3 (Total: 64.80 | Loja Livros) -- Dom Casmurro (34.90) + Pequeno Príncipe (29.90) = 64.80
(4, 'Clássico Brasileiro', 1.00, 34.90, 34.90, 1.0, 'ATIVO', 9, 3),
(5, 'Literatura Infantil', 1.00, 29.90, 29.90, 1.0, 'ATIVO', 7, 3),
-- Venda 4 (Total: 19.90 | Loja Tech) -- Cabo USB (19.90)
(6, 'Reposição de cabo', 1.00, 19.90, 19.90, 1.0, 'ATIVO', 3, 4),
-- Venda 5 (Total: 139.90 | Loja Moda) -- Tênis Feminino (139.90)
(7, 'Presente dia das mães', 1.00, 139.90, 139.90, 1.0, 'ATIVO', 5, 5),
-- Venda 6 (Total: 34.90 | Loja Livros) -- Dom Casmurro (34.90)
(8, 'Leitura escolar', 1.00, 34.90, 34.90, 1.0, 'ATIVO', 9, 6),
-- Venda 7 (Total: 1199.00 | Loja Tech) -- Cadeira Gamer (1199.00)
(9, 'Setup Home Office', 1.00, 1199.00, 1199.00, 1.0, 'ATIVO', 27, 7),
-- Venda 8 (Total: 159.00 | Loja Moda) -- Blazer Feminino (159.00)
(10, 'Roupa social', 1.00, 159.00, 159.00, 1.0, 'ATIVO', 22, 8),
-- Venda 9 (Total: 82.80 | Loja Livros) -- 2 Livros O Hobbit com desconto (Orig: 44.90 -> Vendido: 41.40 * 2 = 82.80)
(11, 'Combo Fantasia', 0.92, 44.90, 41.40, 2.0, 'ATIVO', 20, 9),
-- Venda 10 (Total: 249.00 | Loja Tech) -- Smartwatch (249.00)
(12, 'Relógio Inteligente', 1.00, 249.00, 249.00, 1.0, 'ATIVO', 12, 10),
(13, 'Monitor para escritório', 1.00, 899.00, 899.00, 1.0, 'ATIVO', 18, 11),
(14, 'Teclado para digitação rápida', 1.00, 299.90, 299.90, 1.0, 'ATIVO', 21, 11),
(15, 'Vestido para festa', 1.00, 129.00, 129.00, 1.0, 'ATIVO', 19, 12),
(16, 'Livro técnico', 1.00, 89.90, 89.90, 1.0, 'ATIVO', 14, 13),
(17, 'Reuniões online', 1.00, 189.00, 189.00, 1.0, 'ATIVO', 24, 14),
(18, 'Moda outono', 1.00, 139.90, 139.90, 1.0, 'ATIVO', 25, 15),
(19, 'Basica', 1.00, 29.90, 29.90, 1.0, 'ATIVO', 17, 15),
(20, 'Estratégia', 1.00, 27.90, 27.90, 1.0, 'ATIVO', 29, 16),
(21, 'Desenvolvimento pessoal', 1.00, 54.90, 54.90, 1.0, 'ATIVO', 23, 16),
(22, 'Música no treino', 1.00, 199.90, 199.90, 1.0, 'ATIVO', 10, 17),
(23, 'Uso diário', 1.00, 74.90, 74.90, 1.0, 'ATIVO', 28, 18),
(24, 'Leitura de cabeceira', 1.00, 49.90, 49.90, 1.0, 'ATIVO', 26, 19),
(25, 'Bateria extra viagem', 1.00, 89.00, 89.00, 1.0, 'ATIVO', 15, 20),
(26, 'Mouse ergonômico', 1.00, 79.90, 79.90, 1.0, 'ATIVO', 1, 21),
(27, 'Cabo USB-C reserva', 1.00, 19.90, 19.90, 1.0, 'ATIVO', 3, 21),
(28, 'Jeans para dia a dia', 1.00, 99.90, 99.90, 1.0, 'ATIVO', 6, 22),
(29, 'Edição especial', 1.00, 39.90, 34.90, 1.0, 'ATIVO', 8, 23),
(30, 'Notebook alta performance', 1.00, 3299.00, 3199.00, 1.0, 'ATIVO', 2, 24),
(31, 'Blusa de inverno', 1.00, 69.90, 69.90, 1.0, 'ATIVO', 11, 25),
(32, 'Saia casual', 1.00, 59.90, 59.90, 1.0, 'ATIVO', 13, 25),
(33, 'Estudos de TI', 1.00, 89.90, 89.90, 1.0, 'ATIVO', 14, 26),
(34, 'Relógio para corrida', 1.00, 249.00, 249.00, 1.0, 'ATIVO', 12, 27),
(35, 'Camisa para o trabalho', 1.00, 49.90, 49.90, 1.0, 'ATIVO', 4, 28),
(36, 'Clássico', 1.00, 33.00, 33.00, 1.0, 'ATIVO', 16, 29),
(37, 'Carregador viagem', 1.00, 89.00, 89.00, 1.0, 'ATIVO', 15, 30),
(38, 'Armazenamento', 1.00, 49.00, 49.00, 1.0, 'ATIVO', 30, 30);

-- SOLICITACAO
INSERT IGNORE INTO tb_Solicitacao
(id, data_atualizacao, data_solicitacao, motivo, quantidade, status, status_solicitacao, tipo_solicitacao, id_consumidor, id_item_venda, id_venda)
VALUES
(1, '2025-12-25 10:00:00', '2025-12-18 09:30:00', 'Tela apresentando pixels mortos', 1.0, 'ATIVO', 'CONCLUIDA', 'TROCA', 1, 1, 1),
(2, '2025-12-26 14:20:00', '2025-12-20 11:00:00', 'Mouse muito pequeno para minha mão', 1.0, 'ATIVO', 'CONCLUIDA', 'DEVOLUCAO', 1, 2, 1),
(3, '2025-12-30 16:00:00', '2025-12-26 08:45:00', 'Ficou apertado, solicito troca pelo 37', 1.0, 'ATIVO', 'CONCLUIDA', 'TROCA', 5, 7, 5),
(4, '2025-12-26 08:50:00', '2025-12-26 08:40:00', 'Cliquei errado, desconsiderar', 1.0, 'INATIVO', 'CANCELADA', 'TROCA', 5, 7, 5),
(5, '2026-01-28 10:00:00', '2026-01-22 15:00:00', 'A cor não combinou com o quarto', 1.0, 'INATIVO', 'REJEITADA', 'DEVOLUCAO', 7, 9, 7),
(6, '2026-02-05 11:30:00', '2026-01-25 09:00:00', 'Pistão de gás descendo sozinho', 1.0, 'ATIVO', 'CONCLUIDA', 'TROCA', 7, 9, 7),
(7, '2026-02-25 13:00:00', '2026-02-18 10:00:00', 'Chegou com a capa rasgada', 1.0, 'ATIVO', 'CONCLUIDA', 'TROCA', 9, 11, 9),
(8, '2026-03-05 08:00:00', '2026-03-05 20:00:00', 'Folhas amarelando muito rápido (forjado)', 1.0, 'INATIVO', 'REJEITADA', 'TROCA', 9, 11, 9),
(9, '2026-01-10 09:00:00', '2026-01-05 14:00:00', 'Monitor com risco na tela ao tirar da caixa', 1.0, 'ATIVO', 'EM_TRANSITO', 'TROCA', 1, 13, 11),
(10, '2026-02-15 11:00:00', '2026-02-15 10:00:00', 'Não gostei do caimento', 1.0, 'INATIVO', 'REJEITADA', 'DEVOLUCAO', 2, 15, 12),
(11, '2026-02-10 16:30:00', '2026-02-10 16:30:00', 'Comprei a edição errada', 1.0, 'ATIVO', 'PENDENTE', 'DEVOLUCAO', 3, 16, 13),
(12, '2026-03-10 14:00:00', '2026-02-28 09:15:00', 'Webcam veio sem o cabo de conexão', 1.0, 'ATIVO', 'CONCLUIDA', 'TROCA', 4, 17, 14),
(13, '2026-03-20 10:00:00', '2026-03-15 11:00:00', 'Tamanho ficou pequeno, quero o G', 1.0, 'ATIVO', 'CONCLUIDA', 'TROCA', 7, 22, 17),
(14, '2026-04-18 10:00:00', '2026-04-12 09:30:00', 'Mouse parou de funcionar o clique direito', 1.0, 'ATIVO', 'CONCLUIDA', 'TROCA', 2, 26, 21),
(15, '2025-12-28 14:00:00', '2025-12-22 11:15:00', 'Livro veio com a capa dobrada', 1.0, 'ATIVO', 'CONCLUIDA', 'DEVOLUCAO', 6, 29, 23),
(16, '2026-03-20 16:30:00', '2026-03-15 08:40:00', 'Arrependimento da compra do Notebook', 1.0, 'INATIVO', 'REJEITADA', 'DEVOLUCAO', 8, 30, 24),
(17, '2026-03-29 09:00:00', '2026-03-27 15:20:00', 'Blusa ficou muito larga, quero M', 1.0, 'ATIVO', 'EM_TRANSITO', 'TROCA', 10, 31, 25),
(18, '2026-01-25 11:30:00', '2026-01-18 10:00:00', 'Smartwatch não está segurando carga', 1.0, 'ATIVO', 'CONCLUIDA', 'TROCA', 3, 34, 27),
(19, '2026-04-22 13:45:00', '2026-04-17 14:10:00', 'Cor da camisa não é a mesma da foto', 1.0, 'ATIVO', 'CONCLUIDA', 'DEVOLUCAO', 5, 35, 28),
(20, '2026-03-10 09:00:00', '2026-03-10 09:00:00', 'Carregador está esquentando muito', 1.0, 'ATIVO', 'PENDENTE', 'TROCA', 9, 37, 30),
(21, '2026-04-05 10:20:00', '2026-04-02 11:00:00', 'Monitor apresentou listras na tela', 1.0, 'INATIVO', 'REJEITADA', 'TROCA', 1, 13, 11),
(22, '2026-03-05 15:00:00', '2026-02-26 10:30:00', 'Webcam desfocando sozinha', 1.0, 'ATIVO', 'CONCLUIDA', 'TROCA', 4, 17, 14),
(23, '2026-03-12 14:00:00', '2026-03-08 09:15:00', 'Saia veio com um rasgo na costura', 1.0, 'ATIVO', 'EM_TRANSITO', 'DEVOLUCAO', 5, 18, 15);

-- HISTORICO SOLICITACAO
INSERT IGNORE INTO tb_Historico_Solicitacao
(id, data_atualizacao, observacao, status_anterior, status_atual, id_solicitacao)
VALUES
(1, '2025-12-20 14:00:00', 'Solicitação de troca aprovada! Aguardando envio.', 'PENDENTE', 'APROVADA', 1),
(2, '2025-12-22 09:30:00', 'Produto postado pelo cliente. Código de rastreio recebido.', 'APROVADA', 'EM_TRANSITO', 1),
(3, '2025-12-25 10:00:00', 'Produto recebido e verificado. Troca autorizada e finalizada.', 'EM_TRANSITO', 'CONCLUIDA', 1),
(4, '2025-12-22 15:00:00', 'Solicitação de devolução aprovada!', 'PENDENTE', 'APROVADA', 2),
(5, '2025-12-26 14:20:00', 'Produto recebido em perfeito estado. Estorno solicitado.', 'APROVADA', 'CONCLUIDA', 2),
(6, '2025-12-26 10:00:00', 'Solicitação de troca aprovada!', 'PENDENTE', 'APROVADA', 3),
(7, '2025-12-30 16:00:00', 'Troca de numeração efetuada com sucesso.', 'APROVADA', 'CONCLUIDA', 3),
(8, '2025-12-26 08:50:00', 'Solicitação cancelada pelo cliente', 'PENDENTE', 'CANCELADA', 4),
(9, '2026-01-28 10:00:00', 'Solicitação de devolução reprovada! Fora do prazo.', 'PENDENTE', 'REJEITADA', 5),
(10, '2026-01-30 10:00:00', 'Solicitação de troca aprovada! Agendando coleta.', 'PENDENTE', 'APROVADA', 6),
(11, '2026-02-05 11:30:00', 'Troca realizada. Pistão substituído.', 'APROVADA', 'CONCLUIDA', 6),
(12, '2026-02-20 14:00:00', 'Solicitação de troca aprovada!', 'PENDENTE', 'APROVADA', 7),
(13, '2026-02-25 13:00:00', 'Novo exemplar enviado ao cliente.', 'APROVADA', 'CONCLUIDA', 7),
(14, '2026-03-05 08:00:00', 'Produto danificado por mal uso do consumidor.', 'PENDENTE', 'REJEITADA', 8),
(15, '2026-01-08 10:00:00', 'Fotos do monitor validadas. Logística reversa gerada.', 'PENDENTE', 'APROVADA', 9),
(16, '2026-01-10 09:00:00', 'Produto em trânsito para o centro de distribuição.', 'APROVADA', 'EM_TRANSITO', 9),
(17, '2026-02-15 11:00:00', 'Rejeitado: Devolução solicitada 21 dias após a entrega.', 'PENDENTE', 'REJEITADA', 10),
(18, '2026-03-02 09:00:00', 'Webcam devolvida, enviando novo produto na caixa lacrada.', 'PENDENTE', 'APROVADA', 12),
(19, '2026-03-10 14:00:00', 'Cliente confirmou o recebimento da nova webcam.', 'APROVADA', 'CONCLUIDA', 12),
(20, '2026-03-16 10:00:00', 'Aprovado, aguardando envio do cliente.', 'PENDENTE', 'APROVADA', 13),
(21, '2026-03-20 10:00:00', 'Nova unidade G despachada e entregue.', 'APROVADA', 'CONCLUIDA', 13),
(22, '2026-04-13 10:00:00', 'Defeito no mouse relatado, aguardando envio do cliente.', 'PENDENTE', 'APROVADA', 14),
(23, '2026-04-18 10:00:00', 'Mouse recebido com defeito confirmado. Novo enviado e entregue.', 'APROVADA', 'CONCLUIDA', 14),
(24, '2025-12-23 09:00:00', 'Fotos do livro validadas. Devolução aprovada.', 'PENDENTE', 'APROVADA', 15),
(25, '2025-12-28 14:00:00', 'Livro retornado ao estoque. Estorno concluído.', 'APROVADA', 'CONCLUIDA', 15),
(26, '2026-03-20 16:30:00', 'Devolução por arrependimento rejeitada: Passaram-se 21 dias da entrega.', 'PENDENTE', 'REJEITADA', 16),
(27, '2026-03-28 10:00:00', 'Troca de tamanho aprovada. Aguardando postagem.', 'PENDENTE', 'APROVADA', 17),
(28, '2026-03-29 09:00:00', 'Cliente postou o produto nos correios.', 'APROVADA', 'EM_TRANSITO', 17),
(29, '2026-01-20 14:00:00', 'Solicitação de garantia aprovada pelo fabricante.', 'PENDENTE', 'APROVADA', 18),
(30, '2026-01-25 11:30:00', 'Novo smartwatch entregue ao consumidor.', 'APROVADA', 'CONCLUIDA', 18),
(31, '2026-04-18 09:00:00', 'Aprovada devolução por divergência de cor.', 'PENDENTE', 'APROVADA', 19),
(32, '2026-04-22 13:45:00', 'Camisa recebida, valor estornado na fatura.', 'APROVADA', 'CONCLUIDA', 19),
(33, '2026-04-05 10:20:00', 'Negado: Dano físico na tela caracterizado como mau uso após 3 meses.', 'PENDENTE', 'REJEITADA', 21),
(34, '2026-02-28 11:00:00', 'Problema crônico da webcam identificado, troca autorizada.', 'PENDENTE', 'APROVADA', 22),
(35, '2026-03-05 15:00:00', 'Equipamento substituído com sucesso.', 'APROVADA', 'CONCLUIDA', 22),
(36, '2026-03-10 10:00:00', 'Defeito de fábrica na costura validado.', 'PENDENTE', 'APROVADA', 23),
(37, '2026-03-12 14:00:00', 'Objeto postado, retornando ao centro de distribuição.', 'APROVADA', 'EM_TRANSITO', 23);

-- FEEDBACKS
INSERT IGNORE INTO tb_feedback
(id_feedback, comentario, data_feedback, nota, status, tipo_feedback, id_consumidor, id_loja, id_solicitacao)
VALUES
(1, 'Processo de troca foi muito rápido e transparente. O novo notebook está perfeito.', '2025-12-26', 5, 'ATIVO', 'SOLICITACAO', 1, 1, 1),
(2, 'Sempre compro na CenterTech e nunca decepcionam, mesmo quando dá problema.', '2025-12-26', 5, 'ATIVO', 'LOJA', 1, 1, 1),
(3, 'A devolução funcionou, mas o estorno demorou um pouco para cair no cartão.', '2025-12-28', 3, 'ATIVO', 'SOLICITACAO', 1, 1, 2),
(4, 'A Estilo&Cia tem as melhores roupas. O atendimento da Carla foi excelente.', '2025-12-31', 5, 'ATIVO', 'LOJA', 5, 2, 3),
(5, 'Tive que insistir muito no chat para conseguirem aprovar a troca.', '2026-02-06', 2, 'ATIVO', 'LOJA', 7, 1, 6),
(6, 'Pelo menos a cadeira nova veio sem defeito no pistão. Resolveram o problema.', '2026-02-06', 4, 'ATIVO', 'SOLICITACAO', 7, 1, 6),
(7, 'Enviam livros sem proteção adequada. É a segunda vez que chega amassado.', '2026-02-26', 1, 'ATIVO', 'LOJA', 9, 3, 7),
(8, 'Sistema de troca confuso, pede muitas fotos e demora para validar.', '2026-02-26', 2, 'ATIVO', 'SOLICITACAO', 9, 3, 7),
(9, 'Suporte atencioso, estou no aguardo do novo monitor.', '2026-01-12', 4, 'ATIVO', 'SOLICITACAO', 1, 1, 9),
(10, 'Absurdo não aceitarem minha devolução, foram poucos dias de atraso!', '2026-02-16', 1, 'ATIVO', 'SOLICITACAO', 2, 2, 10),
(11, 'Troca sem burocracia, fone novo recebido.', '2026-03-21', 5, 'ATIVO', 'SOLICITACAO', 7, 1, 13),
(12, 'O mouse novo chegou muito rápido. Atendimento excelente da loja de tecnologia.', '2026-04-20', 5, 'ATIVO', 'SOLICITACAO', 2, 1, 14),
(13, 'Decepcionado que o livro veio dobrado, mas pelo menos devolveram o dinheiro sem enrolação.', '2025-12-30', 4, 'ATIVO', 'SOLICITACAO', 6, 3, 15),
(14, 'Achei um absurdo não aceitarem minha devolução do notebook, a lei deveria me proteger.', '2026-03-25', 1, 'ATIVO', 'SOLICITACAO', 8, 1, 16),
(15, 'Meu smartwatch parou do nada, mas a garantia funcionou bem.', '2026-01-27', 4, 'ATIVO', 'SOLICITACAO', 3, 1, 18),
(16, 'Devolvi a camisa porque a cor era bem mais apagada que no site.', '2026-04-24', 3, 'ATIVO', 'SOLICITACAO', 5, 2, 19),
(17, 'As roupas da Estilo&Cia são ótimas, mas precisam melhorar as fotos do site.', '2026-04-24', 4, 'ATIVO', 'LOJA', 5, 2, 19),
(18, 'A CenterTech negou a troca do meu monitor dizendo que fui eu quem quebrei. Lamentável.', '2026-04-06', 1, 'ATIVO', 'SOLICITACAO', 1, 1, 21),
(19, 'Tirando o susto da webcam vir com defeito de fábrica, a troca foi suave.', '2026-03-07', 5, 'ATIVO', 'SOLICITACAO', 4, 1, 22),
(20, 'Sempre encontro os lançamentos de livros aqui. Recomendo!', '2026-05-02', 5, 'ATIVO', 'LOJA', 1, 3, NULL),
(21, 'Atendimento da Juliana na CenterTech foi perfeito quando precisei tirar dúvidas antes da compra.', '2026-02-25', 5, 'ATIVO', 'LOJA', 8, 1, NULL);

-- LICENCAS
INSERT IGNORE INTO tb_licenca (id_licenca, validade, status, id_loja) VALUES
('31323635-3133-3064-2d31-3464622d3131', '2026-12-31', 'ATIVO', 1),
('31323635-3163-3634-2d31-3464622d3131', '2026-10-15', 'ATIVO', 2),
('31323635-3165-3330-2d31-3464622d3131', '2026-08-20', 'ATIVO', 3);