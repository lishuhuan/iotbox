package com.nbicc.gywlw.Service;

import com.nbicc.gywlw.Dao.LoginTicketDAO;
import com.nbicc.gywlw.Model.GywlwUser;
import com.nbicc.gywlw.Model.LoginTicket;
import com.nbicc.gywlw.mapper.GywlwUserMapper;
import com.nbicc.gywlw.util.MyUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by BigMao on 2016/11/17.
 */
@Service
public class UserService {

    @Autowired
    private GywlwUserMapper gywlwUserMapper;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    //注册
    public Map<String, Object> register(String username, String password, String phone, String companyName) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("data", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("data", "密码不能为空");
            return map;
        }

        GywlwUser gywlwUser = gywlwUserMapper.selectByPhone(phone);

        if (gywlwUser != null) {
            map.put("data", "用户名已经被注册");
            return map;
        }


        gywlwUser = new GywlwUser();
        gywlwUser.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
        gywlwUser.setUserName(username);
        gywlwUser.setCompanyName(companyName);
        gywlwUser.setUserPhone(phone);
        gywlwUser.setDelMark(false);
        gywlwUser.setDuserLevel(1);
        gywlwUser.setUserLevel(1);

        gywlwUser.setUserPsd(MyUtil.MD5(password));
        gywlwUserMapper.insert(gywlwUser);


        // 注册完以普通用户登录
        String ticket = addLoginTicket(gywlwUser.getUserId(),Byte.parseByte("0"));
        map.put("ticket", ticket);
        return map;
    }

    //登录
    public Map<String, Object> login(String phone, String password,Byte userType) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(phone)) {
            map.put("data", "手机号不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("data", "密码不能为空");
            return map;
        }

        GywlwUser gywlwUser = gywlwUserMapper.selectByPhoneWithPsd(phone);

        if (gywlwUser == null) {
            map.put("data", "手机号不存在");
            return map;
        }

        if (!MyUtil.MD5(password).equals(gywlwUser.getUserPsd())) {
            map.put("data", "密码不正确");
            return map;
        }

        map.put("userId", gywlwUser.getUserId());
        map.put("userPhone", gywlwUser.getUserPhone());
        map.put("userLevel", gywlwUser.getUserLevel());
        map.put("duserLevel", gywlwUser.getDuserLevel());

        String ticket = addLoginTicket(gywlwUser.getUserId(),userType);
        map.put("ticket", ticket);
        return map;
    }

    //添加ticket
    private String addLoginTicket(String userId,Byte userType){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        ticket.setUserType(userType);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24*30);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }

}
