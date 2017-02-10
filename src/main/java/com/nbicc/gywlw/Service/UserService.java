package com.nbicc.gywlw.Service;

import com.nbicc.gywlw.Dao.LoginTicketDAO;
import com.nbicc.gywlw.Model.GywlwUser;
import com.nbicc.gywlw.Model.HostHolder;
import com.nbicc.gywlw.Model.LoginTicket;
import com.nbicc.gywlw.mapper.GywlwUserMapper;
import com.nbicc.gywlw.util.MyUtil;
import com.nbicc.gywlw.util.RedisAPI;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;


/**
 * Created by BigMao on 2016/11/17.
 */
@Service

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private GywlwUserMapper gywlwUserMapper;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private HostHolder hostHolder;

    //注册
    @Transactional
    public Map<String, Object> register(String username, String password, String phone, String companyName, String sms) {
        Map<String, Object> map = new HashMap<String, Object>();

        //验证码比对
        RedisAPI redisAPI = new RedisAPI();
        if(!sms.equals(redisAPI.get(phone))){
            map.put("data","验证码不正确");
            return map;
        }

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
        //注册时默认有两个身份：厂商受限用户1，普通受限用户1；
        gywlwUser.setDuserLevel(1);
        gywlwUser.setUserLevel(1);
        //MD5加密
        gywlwUser.setUserPsd(MyUtil.MD5(password));
        gywlwUserMapper.insert(gywlwUser);

        // 注册完以普通用户登录
        String ticket = addLoginTicket(gywlwUser.getUserId(),0);
        map.put("ticket", ticket);
        return map;
    }

    //登录
    @Transactional
    public Map<String, Object> login(String phone, String password,Integer userType) {
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
//  在拦截器中已经设置过了
//        map.put("userId", gywlwUser.getUserId());
//        map.put("userPhone", gywlwUser.getUserPhone());
//        map.put("userLevel", gywlwUser.getUserLevel());
//        map.put("duserLevel", gywlwUser.getDuserLevel());

        String ticket = addLoginTicket(gywlwUser.getUserId(),userType);
        map.put("ticket", ticket);
        return map;
    }

    //添加ticket
    private String addLoginTicket(String userId,Integer userType){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        ticket.setUserType(userType);
        Date date = new Date();
        date.setTime(date.getTime() + 1000L*3600*24*30);//int会溢出
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    @Transactional
    public void logout(String ticket) {
        loginTicketDAO.delete(ticket);
    }

    //短信验证码发送
    public String sendSms(String phone) throws ApiException {
        Random random = new Random();
        String str = String.valueOf(random.nextInt(9999)%(9999-1000+1) + 1000);
        JedisPool pool = RedisAPI.getPool();
        Jedis jedis = pool.getResource();
        jedis.set(phone,str);
        jedis.expire(phone,60);
        logger.info("jedis insert ok");

        String url = "http://gw.api.taobao.com/router/rest";
        String appkey = "23560535";
        String secret = "a4e858331188253d9bf8788d11ac214e";
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName("gywlw测试");
        String json = "{\"number\":\""+ str + "\"}";
        System.out.println(json);
        req.setSmsParamString(json);
        req.setRecNum(phone);
        req.setSmsTemplateCode("SMS_33675301");
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
        return str;
    }

    //修改密码 By Sms or Oldpsd
    @Transactional
    public int changePsd(String phone, String code, String newPassword,int mark){
        //mark==0 通过短信修改密码
        if(mark == 0) {
            RedisAPI redisAPI = new RedisAPI();
            if (redisAPI.get(phone) == null || !code.equals(redisAPI.get(phone))) {
                return -1;
            }
        }
        //mark==1 通过old密码修改密码
        if(mark == 1) {
            if(!MyUtil.MD5(code).equals(gywlwUserMapper.selectByPhoneWithPsd(hostHolder.getGywlwUser().getUserPhone()).getUserPsd())){
                return -1;
            }
        }
        GywlwUser gywlwUser = new GywlwUser();
        gywlwUser.setUserId(gywlwUserMapper.selectByPhone(phone).getUserId());
        gywlwUser.setUserPsd(MyUtil.MD5(newPassword));
        gywlwUserMapper.updateByPrimaryKeySelective(gywlwUser);
        loginTicketDAO.deleteByUserId(gywlwUser.getUserId());//修改密码以后删除该用户所有cookie
        return 0;
    }


}
