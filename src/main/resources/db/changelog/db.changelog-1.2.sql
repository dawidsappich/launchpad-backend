--liquibase formatted sql

--changeset dawid.sappich@cdi-ag.de:2020-01-27 11:38:42

alter table USER
add unique (username);