package com.appgoadmin.models;

public class resumeModel {
    String nombre, img;
    double precio;
    int cantidad;

    public resumeModel() {
    }

    public resumeModel(String nombre, String img, double precio, int cantidad) {
        this.nombre = nombre;
        this.img = img;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
