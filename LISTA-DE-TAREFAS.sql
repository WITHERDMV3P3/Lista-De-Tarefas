create database Tarefas;
use Tarefas;

create table Tarefas(
id int primary key auto_increment,
Nome_da_tarefa varchar(255) unique,
Custo decimal(10,2),
data_limite varchar(20),
ordem_apresentacao int
);