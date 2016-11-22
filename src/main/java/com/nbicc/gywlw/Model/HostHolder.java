package com.nbicc.gywlw.Model;

import org.springframework.stereotype.Component;

/**
 * Created by BigMao on 2016/11/18.
 */
@Component
public class HostHolder {
    private static ThreadLocal<GywlwUser> gywlwUsers = new ThreadLocal<>();

    public GywlwUser getGywlwUser(){return gywlwUsers.get();}

    public void setGywlwUser(GywlwUser gywlwUser){gywlwUsers.set(gywlwUser);}

    public void clear(){gywlwUsers.remove();}
}
