package com.nbicc.gywlw.Service;

import com.nbicc.gywlw.Model.GywlwMessage;
import com.nbicc.gywlw.Model.HostHolder;
import com.nbicc.gywlw.mapper.GywlwMessageMapper;
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



}
