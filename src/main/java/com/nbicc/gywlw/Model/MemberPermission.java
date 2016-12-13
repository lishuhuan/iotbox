package com.nbicc.gywlw.Model;

/**
 * Created by BigMao on 2016/12/13.
 */
public class MemberPermission {
    private Integer id;

    private Byte writePermission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getWritePermission() {
        return writePermission;
    }

    public void setWritePermission(Byte writePermission) {
        this.writePermission = writePermission;
    }
}
