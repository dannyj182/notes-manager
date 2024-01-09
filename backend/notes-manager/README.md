# Notes Manager

This is the backend project that supports the web application that allows you to manage notes and tags, designed for general use.

You have the possibility to create notes, edit them, delete them, archive them, list them, assign tags, you can also create tags and delete them.

For added security a login was implemented where only authenticated and authorised users can access their notes and tags.

## Link to the Deployed Project

You can access (soon) the deployed version of this project at [Web]().

This link will take you to the live version of the application so you can test and explore it.

## Project Configuration

### Cloning the Repository

First, clone this repository on your local machine using Git:

```bash
git clone https://github.com/ensolvers-github-challenges/JimenezCasares-24baa6
```

### application.properties File

In the application.properties file we have the following configuration:

```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC
spring.datasource.username=admin
spring.datasource.password=admin
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

server.error.include-exception=true
server.error.include-stacktrace=always
```

Be sure to replace **`username`** and **`password`** with your MySQL credentials.

### MySQL

This project uses MySQL as its database, perform an installation on your local computer using port **`3306`** and the name of the database to create is **`test`**.

### Installing Dependencies

When you open the project in your IDE of choice, it will automatically install the necessary dependencies.

### Running the Server

The server will run on `http://localhost:port`, where `port` is the port that the application finds available, by default it takes 8080.

### Usage

This backend provides an API that can be used by the frontend application. Make sure your frontend application is configured to make requests to this API, have CORS enabled.

### Frontend repository

The frontend code for this project can be found in this [repository](https://github.com/ensolvers-github-challenges/JimenezCasares-24baa6/tree/main/frontend/notes-manager-frontend).

---

# Gestor de Notas

Este es el proyecto Backend que soporta a la aplicación web que permite administrar notas y etiquetas, diseñada para uso general.

Tienes la posibilidad de crear notas, editarlas, borrarlas, archivarlas, listarlas, asignarle etiquetas, también puedes crear etiquetas y eliminarlas.

Para mayor seguridad se implementó un login donde solo los usuarios autenticados y autorizados pueden acceder a sus notas y etiquetas.

## Enlace al Proyecto Desplegado

Puedes acceder (proximamente) a la versión desplegada de este proyecto en [Web]().

Este enlace te llevará a la versión en vivo de la aplicación para que puedas probarla y explorarla.

## Configuración del Proyecto

### Clonar el Repositorio

Primero, clona este repositorio en tu máquina local utilizando Git:

```bash
git clone https://github.com/ensolvers-github-challenges/JimenezCasares-24baa6
```

### Archivo application.properties

En el archivo application.properties tenemos la siguiente configuración:

```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC
spring.datasource.username=admin
spring.datasource.password=admin
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

server.error.include-exception=true
server.error.include-stacktrace=always
```

Asegúrate de reemplazar **`username`** y **`password`** con tus credenciales en MySQL.

### MySQL

Este proyecto utiliza MySQL como su base de datos, realiza una instalación en tu computadora local usando el puerto **`3306`** y el nombre de la base de datos que debes crear es **`test`**

### Instalación de Dependencias

Al abrir el proyecto en tu IDE de preferencia, instalará automáticamente las dependencias necesarias.

### Ejecutar el Servidor

El servidor se ejecutará en `http://localhost:port`, donde `port` es el puerto que la aplicación encuentre disponible, por defecto toma el 8080.

### Uso

Este backend proporciona una API que puede ser utilizada por la aplicación frontend. Asegúrate de que tu aplicación frontend esté configurada para hacer solicitudes a esta API, tiene los CORS habilitados.

### Repositorio Frontend

El código del frontend correspondiente a este proyecto se encuentra en este [repositorio](https://github.com/ensolvers-github-challenges/JimenezCasares-24baa6/tree/main/frontend/notes-manager-frontend).