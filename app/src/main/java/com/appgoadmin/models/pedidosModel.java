package com.appgoadmin.models;

public class pedidosModel {
    String id, fecha, direccion, username, userphone;
    double totalCompra;
    int totalProductos;
    boolean entregado, cancelado, encamino;
    String repartidorID, repartidorName;

    public pedidosModel() {
    }

    public pedidosModel(String id, String fecha, String direccion, String username, String userphone, double totalCompra, int totalProductos, boolean entregado, boolean cancelado, boolean encamino, String repartidorID, String repartidorName) {
        this.id = id;
        this.fecha = fecha;
        this.direccion = direccion;
        this.username = username;
        this.userphone = userphone;
        this.totalCompra = totalCompra;
        this.totalProductos = totalProductos;
        this.entregado = entregado;
        this.cancelado = cancelado;
        this.encamino = encamino;
        this.repartidorID = repartidorID;
        this.repartidorName = repartidorName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public int getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(int totalProductos) {
        this.totalProductos = totalProductos;
    }

    public boolean isEntregado() {
        return entregado;
    }

    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public boolean isEncamino() {
        return encamino;
    }

    public void setEncamino(boolean encamino) {
        this.encamino = encamino;
    }

    public String getRepartidorID() {
        return repartidorID;
    }

    public void setRepartidorID(String repartidorID) {
        this.repartidorID = repartidorID;
    }

    public String getRepartidorName() {
        return repartidorName;
    }

    public void setRepartidorName(String repartidorName) {
        this.repartidorName = repartidorName;
    }
}
