create schema pi_sql_adan_eliud;
use pi_sql_adan_eliud;
create table clientes(
codigo bigint(30) not null primary key auto_increment,
nome varchar(200) not null,
endereco varchar(250) not null,
telefone varchar(20) not null,
email varchar(100) null);

create table produto(
codigo bigint(30) not null primary key auto_increment,
nome varchar(200) not null,
descricao varchar(300) not null,
preco decimal(10,2) not null,
stock int not null);

create table entregador(
codigo bigint(30) not null primary key auto_increment,
nome varchar(200) not null,
telefone varchar(20) not null);

create table pedido(
codigo bigint(30) not null primary key auto_increment,
codigo_entregador bigint(30) not null,
codigo_cliente bigint(30) not null,
data_pedido datetime not null,
foreign key (codigo_entregador) references entregador(codigo),
foreign key (codigo_cliente) references clientes(codigo));

create table detalhepedido(
codigo bigint(30) not null primary key auto_increment,
codigo_pedido bigint(30) not null,
codigo_produto bigint(30) not null,
quantidade int not null,
precio_unitario decimal(10,2) not null,
foreign key (codigo_pedido) references pedido(codigo),
foreign key (codigo_produto) references produto(codigo));

SELECT
    p.codigo AS codigo_pedido,
    c.nome AS cliente,
    e.nome AS entregador,
    p.data_pedido,
    pr.nome AS produto,
    d.quantidade,
    d.precio_unitario,
    (d.quantidade * d.precio_unitario) AS total_linha
FROM pedido p
JOIN clientes c ON p.codigo_cliente = c.codigo
JOIN entregador e ON p.codigo_entregador = e.codigo
JOIN detalhepedido d ON p.codigo = d.codigo_pedido
JOIN produto pr ON d.codigo_produto = pr.codigo
WHERE DATE(p.data_pedido) = '2025-06-18' -- cambia esta fecha según lo necesites
ORDER BY p.codigo;
