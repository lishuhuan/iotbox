package com.nbicc.gywlw.Model;

/**
 * Created by BigMao on 2016/12/13.
 */
public class MemberPermission {
    private Integer id;

    private Byte write_permission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getWrite_permission() {
        return write_permission;
    }

    public void setWrite_permission(Byte write_permission) {
        this.write_permission = write_permission;
    }
}
