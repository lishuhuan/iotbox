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
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private ProjectService projectService;

    public List<GywlwMessage> messageList(){
        return gywlwMessageMapper.selectByUserId(hostHolder.getGywlwUser().getUserId());
    }
    public void sendMessage(String sendId,String receiveId,String content,Byte messageType,String text){
        GywlwMessage gywlwMessage = new GywlwMessage();
        String content1 = content;
        if(messageType == 3){
            String[] list = text.split(" ");
            content1 =content + " \"" + gywlwProjectMapper.selectByGywlwProjectId(list[0]).getProjectName() + "\"";
        }
        gywlwMessage.setContent("\"" + gywlwUserMapper.selectByPrimaryKey(sendId).getUserName() + "\"" + content1);
        gywlwMessage.setMessageType(messageType);
        gywlwMessage.setReceiveId(receiveId);
        gywlwMessage.setSendId(sendId);
        gywlwMessage.setOperation(Byte.parseByte("2"));
        gywlwMessage.setText(text);
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
        if(messageType == 1 && "0".equals(operate)){
            if(hostHolder.getGywlwUser().getUserType() == 0){
                changeAdmin(gywlwMessage1);
            }else {
                changeFactoryAdmin(gywlwMessage1);
            }
        }
        //厂商管理员分配用户给受限用户
        if(messageType == 2 && "0".equals(operate)){
            boolean state=manufacturerService.editFactoryLimitUserDistribution(gywlwMessage1.getReceiveId(),
                    gywlwMessage1.getText(), Integer.parseInt("0"));
        }

        //用户系统管理员分配项目给受限用户
        if(messageType == 3 && "0".equals(operate)){
            String projectId = gywlwMessage1.getText().split(" ")[0];
            String writePermission = gywlwMessage1.getText().split(" ")[1];
            String msg = projectService.addProjectMember(projectId, gywlwMessage1.getReceiveId(),
                        Byte.parseByte(writePermission));

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
