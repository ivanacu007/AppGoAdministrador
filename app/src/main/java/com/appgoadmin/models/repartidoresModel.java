package com.appgoadmin.models;

public class repartidoresModel {
    String id, nombre, sucursal, sucursalID;
    int pendientes, entregados, encurso, cancelados, todos;
    public repartidoresModel() {
    }

    public repartidoresModel(String id, String nombre, String sucursal, String sucursalID, int pendientes, int entregados, int encurso, int cancelados, int todos) {
        this.id = id;
        this.nombre = nombre;
        this.sucursal = sucursal;
        this.sucursalID = sucursalID;
        this.pendientes = pendientes;
        this.entregados = entregados;
        this.encurso = encurso;
        this.cancelados = cancelados;
        this.todos = todos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getSucursalID() {
        return sucursalID;
    }

    public void setSucursalID(String sucursalID) {
        this.sucursalID = sucursalID;
    }

    public int getPendientes() {
        return pendientes;
    }

    public void setPendientes(int pendientes) {
        this.pendientes = pendientes;
    }

    public int getEntregados() {
        return entregados;
    }

    public void setEntregados(int entregados) {
        this.entregados = entregados;
    }

    public int getEncurso() {
        return encurso;
    }

    public void setEncurso(int encurso) {
        this.encurso = encurso;
    }

    public int getCancelados() {
        return cancelados;
    }

    public void setCancelados(int cancelados) {
        this.cancelados = cancelados;
    }

    public int getTodos() {
        return todos;
    }

    public void setTodos(int todos) {
        this.todos = todos;
    }
}
