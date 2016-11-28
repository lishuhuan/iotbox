package com.nbicc.gywlw.Service;

import com.nbicc.gywlw.Model.*;
import com.nbicc.gywlw.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by BigMao on 2016/11/26.
 */
@Service
public class MessageService {
    @Autowired
    private GywlwMessageMapper gywlwMessageMapper;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private GywlwUserMapper gywlwUserMapper;
    @Autowired
    private GywlwDeviceMapper gywlwDeviceMapper;
    @Autowired
    private GywlwProjectMapper gywlwProjectMapper;
    @Autowired
    private GywlwProjectUserGroupMapper gywlwProjectUserGroupMapper;

    public List<GywlwMessage> messageList(){
        return gywlwMessageMapper.selectByUserId(hostHolder.getGywlwUser().getUserId());
    }
    public void sendMessage(String sendId,String receiveId,String content,Byte messageType){
        GywlwMessage gywlwMessage = new GywlwMessage();
        gywlwMessage.setContent(content);
        gywlwMessage.setMessageType(messageType);
        gywlwMessage.setReceiveId(receiveId);
        gywlwMessage.setSendId(sendId);
        gywlwMessage.setOperation(Byte.parseByte("2"));
        gywlwMessageMapper.insertSelective(gywlwMessage);
    }

    public int operate(String messageId,String operate, String delMark){
        if(delMark.equals("1")){
            gywlwMessageMapper.deleteByPrimaryKey(Integer.parseInt(messageId));
            return 0;
        }
        GywlwMessage gywlwMessage = new GywlwMessage();
        gywlwMessage.setMessageId(Integer.parseInt(messageId));
        gywlwMessage.setOperation(Byte.parseByte(operate));
        gywlwMessageMapper.updateByPrimaryKeySelective(gywlwMessage);
        GywlwMessage gywlwMessage1 = gywlwMessageMapper.selectByMessageId(messageId);
        int messageType = gywlwMessage1.getMessageType();
        //转移管理员权限
        if(messageType == 1 && operate.equals("0")){
            changeAdmin(gywlwMessage1);
        }


        return 0;
    }

    public void changeAdmin(GywlwMessage gywlwMessage1){
        String quitId = gywlwMessage1.getSendId();
        String addId = gywlwMessage1.getReceiveId();
        //user table
        GywlwUser gywlwUser = new GywlwUser();
        gywlwUser.setUserId(gywlwMessage1.getSendId());
        gywlwUser.setUserLevel(1);
        gywlwUserMapper.updateByPrimaryKeySelective(gywlwUser);
        gywlwUser.setUserId(gywlwMessage1.getReceiveId());
        gywlwUser.setUserLevel(0);
        gywlwUserMapper.updateByPrimaryKeySelective(gywlwUser);

        //device table
        List<GywlwDevice> list = gywlwDeviceMapper.selectByAdminId(quitId);
        if(list != null) {
            for (GywlwDevice device : list) {
                GywlwDevice gywlwDevice = new GywlwDevice();
                gywlwDevice.setDeviceId(device.getDeviceId());
                gywlwDevice.setAdminId(addId);
                gywlwDeviceMapper.updateByPrimaryKeySelective(gywlwDevice);
            }
        }

        //user_admin_group

        //project table
        List<GywlwProject> list1 = gywlwProjectMapper.selectByGywlwUserId1(quitId);
        if(list != null) {
            for (GywlwProject project : list1) {
                GywlwProject gywlw = new GywlwProject();
                gywlw.setProjectId(project.getProjectId());
                gywlw.setAdminId(addId);
                gywlwProjectMapper.updateByPrimaryKeySelective(gywlw);
            }
        }

        //project_user_group
        List<GywlwProjectUserGroup> list2 = gywlwProjectUserGroupMapper.selectByUserId(quitId);
        if(list != null){
            for(GywlwProjectUserGroup group : list2){
                GywlwProjectUserGroup gywlw = new GywlwProjectUserGroup();
                gywlw.setId(group.getId());
                gywlw.setUserId(addId);
                gywlwProjectUserGroupMapper.updateByPrimaryKeySelective(gywlw);
            }
        }

    }


}
