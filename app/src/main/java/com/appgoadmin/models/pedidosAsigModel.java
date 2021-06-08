package com.appgoadmin.models;

public class pedidosAsigModel {
    String pedidoID, repID, repName;
    int curso, pendientes;

    public pedidosAsigModel() {
    }

    public pedidosAsigModel(String pedidoID, String repID, String repName, int curso, int pendientes) {
        this.pedidoID = pedidoID;
        this.repID = repID;
        this.repName = repName;
        this.curso = curso;
        this.pendientes = pendientes;
    }

    public String getPedidoID() {
        return pedidoID;
    }

    public void setPedidoID(String pedidoID) {
        this.pedidoID = pedidoID;
    }

    public String getRepID() {
        return repID;
    }

    public void setRepID(String repID) {
        this.repID = repID;
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    public int getPendientes() {
        return pendientes;
    }

    public void setPendientes(int pendientes) {
        this.pendientes = pendientes;
    }
}

