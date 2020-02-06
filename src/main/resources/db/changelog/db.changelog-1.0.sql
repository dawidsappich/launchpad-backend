--liquibase formatted sql

--changeset dawid.sappich@cdi-ag.de:init

create table app (
   id bigint not null auto_increment,
    app_description varchar(255),
    app_name varchar(255),
    primary key (id)
);

create table launchpad (
   id bigint not null auto_increment,
    title varchar(255),
    template_id bigint not null,
    primary key (id)
);


create table template (
   id bigint not null auto_increment,
    template_description varchar(255),
    template_name varchar(255),
    primary key (id)
);


create table template_applications (
   templates_id bigint not null,
    applications_id bigint not null,
    primary key (templates_id, applications_id)
);


create table tile (
   id bigint not null auto_increment,
    description varchar(255),
    icon varchar(255),
    title varchar(255) not null,
    application_id bigint,
    launchpad_id bigint,
    primary key (id)
);

create table user (
   id bigint not null auto_increment,
    created timestamp,
    modified timestamp not null,
    password varchar(255) not null,
    username varchar(255) not null,
    launchpad_id bigint,
    primary key (id)
);