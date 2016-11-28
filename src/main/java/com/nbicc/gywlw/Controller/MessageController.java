package com.nbicc.gywlw.Controller;

import com.nbicc.gywlw.Model.GywlwMessage;
import com.nbicc.gywlw.Service.MessageService;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by BigMao on 2016/11/26.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @RequestMapping(path = {"/message/list"}, method = {RequestMethod.GET})
    @ResponseBody
    public String messageList(){
        try {
            List<GywlwMessage> list = messageService.messageList();
            return MyUtil.getJSONString(0, list);
        }catch (Exception e){
            logger.error("获取消息列表出错" + e.getMessage());
            return MyUtil.getJSONString(1, "获取消息列表出错!");
        }
    }

    @RequestMapping(path = {"/message/operate"}, method = {RequestMethod.POST})
    @ResponseBody
    public String messageOperate(@RequestParam("message_id")String messageId,
                                 @RequestParam("operate")String operate,
                                 @RequestParam(value = "del_mark",defaultValue = "0")String delMark){
        try{
            messageService.operate(messageId,operate,delMark);
            return MyUtil.getJSONString(0,"消息处理成功");
        }catch (Exception e){
            logger.error("消息处理出错" + e.getMessage());
            return MyUtil.getJSONString(1, "消息处理出错!");
        }

    }


}
