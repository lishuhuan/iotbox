package com.nbicc.gywlw.Model;

import java.util.List;

/**
 * Created by BigMao on 2016/12/30.
 */
public class TreeListModel {
    private String type;
    private String name;
    private String id;
    private List<TreeListModel> children;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TreeListModel> getChildren() {
        return children;
    }

    public void setChildren(List<TreeListModel> children) {
        this.children = children;
    }
}
