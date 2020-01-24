--liquibase formatted sql

--changeset dawid.sappich@cdi-ag.de:2020-01-23T11:24:29

alter table App
add column version varchar;
