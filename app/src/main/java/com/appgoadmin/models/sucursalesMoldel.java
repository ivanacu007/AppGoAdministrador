package com.appgoadmin.models;

public class sucursalesMoldel {
    String id, name, ciudad;

    public sucursalesMoldel() {
    }

    public sucursalesMoldel(String id, String name, String ciudad) {
        this.id = id;
        this.name = name;
        this.ciudad = ciudad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
