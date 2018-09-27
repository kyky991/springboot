package com.zing.springboot.blog.vo;

import com.zing.springboot.blog.pojo.Catalog;

import java.io.Serializable;


public class CatalogVO implements Serializable {

    private static final long serialVersionUID = -7362839340463876831L;

    private String username;
    private Catalog catalog;

    public CatalogVO() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

}
