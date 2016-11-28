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
    private GywlwUserAdminGroupMapper gywlwUserAdminGroupMapper;
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
            if(hostHolder.getGywlwUser().getUserType() == 0){
                changeAdmin(gywlwMessage1);
            }else {
                changeFactoryAdmin(gywlwMessage1);
            }

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
        gywlwDeviceMapper.updateByaddIdAndQuitId(addId,quitId,"0");

        //user_admin_group
        gywlwUserAdminGroupMapper.updateByaddIdAndQuitId(addId,quitId,"0");

        //project table
        List<GywlwProject> list1 = gywlwProjectMapper.selectByGywlwUserId1(quitId);
        if(list1 != null) {
            for (GywlwProject project : list1) {
                GywlwProject gywlw = new GywlwProject();
                gywlw.setProjectId(project.getProjectId());
                gywlw.setAdminId(addId);
                gywlwProjectMapper.updateByPrimaryKeySelective(gywlw);
            }
        }

        //project_user_group
        gywlwProjectUserGroupMapper.deleteByaddId(addId);

    }
    public void changeFactoryAdmin(GywlwMessage gywlwMessage1){
        String quitId = gywlwMessage1.getSendId();
        String addId = gywlwMessage1.getReceiveId();
        //user table
        GywlwUser gywlwUser = new GywlwUser();
        gywlwUser.setUserId(gywlwMessage1.getSendId());
        gywlwUser.setDuserLevel(1);
        gywlwUserMapper.updateByPrimaryKeySelective(gywlwUser);
        gywlwUser.setUserId(gywlwMessage1.getReceiveId());
        gywlwUser.setDuserLevel(0);
        gywlwUserMapper.updateByPrimaryKeySelective(gywlwUser);

        //device table
        gywlwDeviceMapper.updateByaddIdAndQuitId(addId,quitId,"1");
        //user_admin_group
        gywlwUserAdminGroupMapper.updateByaddIdAndQuitId(addId,quitId,"1");

    }

}
