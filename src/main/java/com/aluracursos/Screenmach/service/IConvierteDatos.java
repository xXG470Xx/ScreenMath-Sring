package com.aluracursos.Screenmach.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);
}
