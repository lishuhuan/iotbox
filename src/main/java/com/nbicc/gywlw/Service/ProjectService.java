package com.nbicc.gywlw.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nbicc.gywlw.Model.*;
import com.nbicc.gywlw.mapper.*;
import com.nbicc.gywlw.util.HttpClientUtil;
import com.nbicc.gywlw.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * Created by BigMao on 2016/11/21.
 */
@Service
public class ProjectService {

    @Autowired
    private GywlwUserMapper gywlwUserMapper;
    @Autowired
    private GywlwProjectMapper gywlwProjectMapper;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private GywlwProjectUserGroupMapper gywlwProjectUserGroupMapper;
    @Autowired
    private GywlwVariableRegGroupMapper gywlwVariableRegGroupMapper;
    @Autowired
    private GywlwHistoryItemMapper gywlwHistoryItemMapper;
    @Autowired
    private GywlwDeviceMapper gywlwDeviceMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private GywlwMessageMapper gywlwMessageMapper;
    @Autowired
    private GywlwVariableMapper gywlwVariableMapper;
    @Autowired
    private GywlwPlcInfoMapper gywlwPlcInfoMapper;
    @Autowired
    private GywlwRegInfoMapper gywlwRegInfoMapper;
    @Autowired
    private GywlwDataTrendMapper gywlwDataTrendMapper;
    @Autowired
    private GywlwProjectDeviceGroupMapper gywlwProjectDeviceGroupMapper;
    @Autowired
    private GywlwWarningRulesMapper gywlwWarningRulesMapper;


    public List<GywlwProject> projectList(String gywlwUserId, Byte projectStatus) {
        List<GywlwProject> list = gywlwProjectMapper.selectByGywlwUserId(gywlwUserId, projectStatus);
        System.out.println(gywlwUserId);
        if(list.size()==0){
            list = gywlwProjectUserGroupMapper.selectProjectByUserId(gywlwUserId);
        }
        for(GywlwProject project : list){
            project.setWritePermission(Byte.parseByte("0"));
            if(project.getAdminId().equals(gywlwUserId)){
                project.setWritePermission(Byte.parseByte("1"));
            }
            GywlwProjectUserGroup gywlwProjectUserGroup = gywlwProjectUserGroupMapper.selectByProjectIdAndUserId(project.getProjectId(),gywlwUserId);
            if(gywlwProjectUserGroup != null){
                project.setWritePermission(gywlwProjectUserGroup.getWritePermission());
            }
        }
        return list;
    }

    public GywlwProject projectInfo(String projectId){
        return gywlwProjectMapper.selectByGywlwProjectId(projectId);
    }
    public String editProject(String projectId, String projectName, String parentText, String projectDesc,
                              String longitude, String latitude, String province, String city, String purchaseDate,
                              String completionDate, String installDate, String contactName, String contactPhone,
                              String customerType, String firstParty, String projectType) {
        int mark = 0;
        GywlwProject gywlwProject = gywlwProjectMapper.selectByGywlwProjectId(projectId);
        if (gywlwProject == null) {
            mark = 1;
            gywlwProject = new GywlwProject();
            gywlwProject.setProjectId(UUID.randomUUID().toString().replaceAll("-", ""));
            gywlwProject.setAdminId(hostHolder.getGywlwUser().getUserId());
        }
        gywlwProject.setProjectName(projectName);
        gywlwProject.setParentText(parentText);
        gywlwProject.setProjectDesc(projectDesc);
        gywlwProject.setLongitude(longitude);
        gywlwProject.setLatitude(latitude);
        gywlwProject.setProvince(province);
        gywlwProject.setCity(city);
        gywlwProject.setPurchaseDate(purchaseDate);
        gywlwProject.setCompletionDate(completionDate);
        gywlwProject.setInstallDate(installDate);
        gywlwProject.setContactName(contactName);
        gywlwProject.setContactPhone(contactPhone);
        gywlwProject.setCustomerType(customerType);
        gywlwProject.setFirstparty(firstParty);
        gywlwProject.setProjectType(projectType);

        if (mark == 1) {
            gywlwProjectMapper.addProject(gywlwProject);
        } else {
            gywlwProjectMapper.updateByProjectId(gywlwProject);
        }
        return gywlwProject.getProjectId();
    }
    public void editDisplay(String projectId, String display){
        GywlwProject gywlwProject = new GywlwProject();
        gywlwProject.setProjectId(projectId);
        gywlwProject.setDisplay(display);
        gywlwProjectMapper.updateByPrimaryKeySelective(gywlwProject);
    }

    public void stopProject(String gywlwProjectId,String projectStatus) {
        gywlwProjectMapper.stopByProjectId(gywlwProjectId,projectStatus);
    }

    public List<GywlwProjectUserGroup> projectMemberList(String projectId, int offset, int limit) {
        List<GywlwProjectUserGroup> list = gywlwProjectUserGroupMapper.selectByProjectId(projectId, offset, limit);
        return list;

    }

    public GywlwUser searchUser(String userPhone) {
        return gywlwUserMapper.selectByPhone(userPhone);
    }

    public String addProjectMember(String projectId, String userId, Byte writePermission) {
        GywlwProjectUserGroup userInProject = gywlwProjectUserGroupMapper.selectByProjectIdAndUserId(projectId, userId);
        if (userInProject != null) {
            return "该成员已添加！";
        } else {
            GywlwProjectUserGroup gywlwProjectUserGroup = new GywlwProjectUserGroup();
            gywlwProjectUserGroup.setProjectId(projectId);
            gywlwProjectUserGroup.setUserId(userId);
            gywlwProjectUserGroup.setWritePermission(writePermission);
            gywlwProjectUserGroupMapper.insert(gywlwProjectUserGroup);
            return "添加项目成员成功";
        }
    }

    public void deleteProjectMember(String projectId, String userId) {
        gywlwProjectUserGroupMapper.deleteByProjectIdAndUserId(projectId, userId);
    }

    public List<GywlwVariableRegGroup> searchDataInProject(String projectId, String variableName) {
        return gywlwVariableRegGroupMapper.selectByProjectIdAndVariableName(projectId, variableName);
    }
    public List<GywlwVariable> variableList(String projectId){
        return gywlwVariableMapper.selectByProjectId(projectId);
    }

    public List<GywlwHistoryItem> searchHistoryData(String projectId, String variableName) {
        refresh();
        return gywlwHistoryItemMapper.selectByVariableName(variableName, projectId);

    }

    public List<GywlwHistoryItem> warningList(String projectId, String variableName, String startTime, String endTime) {
        refresh();
        return gywlwHistoryItemMapper.selectwarning(projectId, variableName, startTime, endTime);
    }

    public List<GywlwDevice> getDeviceList(String deviceSn) {
        if (hostHolder.getGywlwUser().getUserLevel() == 0) {
            List<GywlwDevice> list = gywlwDeviceMapper.selectByUserIdAndDeviceSnWithAdmin(
                    hostHolder.getGywlwUser().getUserId(), deviceSn);
            list.addAll(gywlwDeviceMapper.selectByUserIdAndDeviceSn(hostHolder.getGywlwUser().getUserId(), deviceSn));
            for(GywlwDevice device:list){
                if(device.getFactoryId() != null && !"".equals(device.getFactoryId())) {
                    device.setFactoryName(gywlwUserMapper.selectByPrimaryKey(device.getFactoryId()).getUserName());
                }
            }
            return list;
        } else {
            List<GywlwDevice> list = gywlwDeviceMapper.selectByUserIdAndDeviceSn(hostHolder.getGywlwUser().getUserId(),
                    deviceSn);
            for(GywlwDevice device:list){
                if(device.getFactoryId() != null && !"".equals(device.getFactoryId())) {
                    device.setFactoryName(gywlwUserMapper.selectByPrimaryKey(device.getFactoryId()).getUserName());
                }
            }
            return list;
        }
    }


    public void setExpire(String deviceId, Date expiredDate, Byte expiredRight) {
        GywlwDevice gywlwDevice = new GywlwDevice();
        gywlwDevice.setDeviceId(deviceId);
        gywlwDevice.setExpired(expiredDate);
        gywlwDevice.setExpiredRight(expiredRight);
        gywlwDeviceMapper.updateByPrimaryKeySelective(gywlwDevice);
        messageService.sendMessage(hostHolder.getGywlwUser().getUserId(),
                gywlwDeviceMapper.selectByDeviceId(deviceId).getFactoryId(),"用户向您分配设备权限",Byte.parseByte("0"),
                "");
    }

    public GywlwDevice getDevice(String deviceId) {
        return gywlwDeviceMapper.selectByDeviceId(deviceId);
    }

    public int changeInfo(String userName, String companyName, String sex, String email, String fixedphone) {
        if(hostHolder.getGywlwUser().getUserType().equals("1") && hostHolder.getGywlwUser().getDuserLevel() == 0 &&
                !companyName.equals("")){
            return 1;
        }
        GywlwUser gywlwUser = new GywlwUser();
        gywlwUser.setUserId(hostHolder.getGywlwUser().getUserId());
        gywlwUser.setUserName(userName);
        gywlwUser.setCompanyName(companyName);
        gywlwUser.setUserEmail(email);
        gywlwUser.setUserFixedphone(fixedphone);
        gywlwUser.setUserSex(Boolean.parseBoolean(sex));
        gywlwUserMapper.updateByPrimaryKeySelective(gywlwUser);
        return 0;

    }

    public String bindDevice(String deviceSn, int mark) {
        String msg = null;
        GywlwDevice gywlwDevice = gywlwDeviceMapper.selectByDeviceSn(deviceSn);
        if (gywlwDevice == null) {
            return "该唯一标识码不存在！";
        }

        if (hostHolder.getGywlwUser().getUserType() == 1) {
            //成为普通用户管理员
            GywlwDevice gywlwDevice1 = new GywlwDevice();
            GywlwUser gywlwUser = new GywlwUser();
            gywlwUser.setUserId(hostHolder.getGywlwUser().getUserId());
            gywlwDevice1.setDeviceSn(deviceSn);
            if (mark == 0) {
                if (gywlwDevice.getAdminId() != null && !gywlwDevice.getAdminId().equals("")) {
                    return "该唯一标识码已被用户管理员绑定";
                }
                gywlwUser.setUserLevel(0);
                gywlwDevice1.setAdminId(hostHolder.getGywlwUser().getUserId());
                gywlwUserMapper.updateByPrimaryKeySelective(gywlwUser);
                gywlwDeviceMapper.updateByDeviceSnSelective(gywlwDevice1);
                msg = "绑定成功";
            }
            if (mark == 1) {
                if (gywlwDevice.getAdminId().equals("")) {
                    return "该唯一标识码未被绑定";
                }
                gywlwDevice1.setAdminId("");
                gywlwDeviceMapper.updateByDeviceSnSelective(gywlwDevice1);
                List<GywlwDevice> list = gywlwDeviceMapper.selectByAdminId(hostHolder.getGywlwUser().getUserId());
                if (list.size()==0) {
                    //如果该用户管理员下无设备，则将其设为受限用户；
                    gywlwUser.setUserLevel(1);
                    gywlwUserMapper.updateByPrimaryKeySelective(gywlwUser);
                }
                msg =  "解绑成功";
            }
            return msg;

        } else {
            //成为厂商用户
            GywlwDevice gywlwDevice1 = new GywlwDevice();
            GywlwUser gywlwUser = new GywlwUser();
            gywlwUser.setUserId(hostHolder.getGywlwUser().getUserId());
            gywlwDevice1.setDeviceSn(deviceSn);
            if (mark == 0) {
                if (gywlwDevice.getFactoryId() != null && !"".equals(gywlwDevice.getFactoryId())) {
                    return "该唯一标识码已被用户管理员绑定";
                }
                gywlwUser.setDuserLevel(0);
                gywlwDevice1.setFactoryId(hostHolder.getGywlwUser().getUserId());
                gywlwUserMapper.updateByPrimaryKeySelective(gywlwUser);
                gywlwDeviceMapper.updateByDeviceSnSelective(gywlwDevice1);
                msg =  "绑定成功";
            }
            if (mark == 1) {
                if (gywlwDevice.getFactoryId().equals("")) {
                    return "该唯一标识码未被绑定";
                }
                gywlwDevice1.setFactoryId("");
                gywlwDeviceMapper.updateByDeviceSnSelective(gywlwDevice1);
                List<GywlwDevice> list = gywlwDeviceMapper.selectByFactoryId(hostHolder.getGywlwUser().getUserId());
                if (list.size()==0) {
                    //如果该用户管理员下无设备，则将其设为受限用户；
                    gywlwUser.setDuserLevel(1);
                    gywlwUserMapper.updateByPrimaryKeySelective(gywlwUser);
                }
                msg =  "解绑成功";
            }
            return msg;
            }
        }

    public String giveAdmin(String userPhone){
        //限制只能转移权限给一个人，只一次
        GywlwMessage msg = new GywlwMessage();
        msg.setMessageType(Byte.parseByte("1"));
        msg.setSendId(hostHolder.getGywlwUser().getUserId());
        GywlwMessage message = gywlwMessageMapper.selectByUserIdAndMessageType(msg);
        if(message != null){
            return "您只能转移一次权限";
        }


        GywlwUser receive = gywlwUserMapper.selectByPhone(userPhone);
        if(receive == null){
            return "该手机用户不存在";
        }
        String receiveId = receive.getUserId();
        String content = hostHolder.getGywlwUser().getUserName() + "向你转移系统管理员权限";
        Byte messageType = 1;
        messageService.sendMessage(hostHolder.getGywlwUser().getUserId(),receiveId,content,messageType,"");
        return "转移权限消息发送成功";
    }

    public List<GywlwPlcInfo> getPlcListByDeviceId(String deviceId){
        return gywlwPlcInfoMapper.selectByDeviceId(deviceId);
    }

    public List<GywlwRegInfo> getRegListByPlcId(String plcId){
        return gywlwRegInfoMapper.selectByPlcId(plcId);
    }

    public void bindRegAndVariable(String variableId,String regId, String deviceId, String projectId){
        GywlwVariableRegGroup gywlwVariableRegGroup = new GywlwVariableRegGroup();
        gywlwVariableRegGroup.setVariableId(variableId);
        gywlwVariableRegGroup.setDeviceId(deviceId);
        gywlwVariableRegGroup.setProjectId(projectId);
        gywlwVariableRegGroup.setRegId(regId);
        gywlwVariableRegGroupMapper.insertSelective(gywlwVariableRegGroup);
        operateDeviceToProject(deviceId,projectId);
    }

    public void unbindRegAndVariable(String id){
        GywlwVariableRegGroup gywlwVariableRegGroup = new GywlwVariableRegGroup();
        gywlwVariableRegGroup.setId(Integer.parseInt(id));
        gywlwVariableRegGroup.setDelMark(Byte.parseByte("1"));
        gywlwVariableRegGroupMapper.updateByPrimaryKeySelective(gywlwVariableRegGroup);
    }

    public List<Map> getDataForTrend(String photoName){
        List<Map> list = new ArrayList<>();
        List<GywlwDataTrend> trends = gywlwDataTrendMapper.selectByPhotoName(photoName);
        if(trends.size() != 0){
            for(GywlwDataTrend gywlwDataTrend : trends){
                String regId = gywlwDataTrend.getRegId();
                Map<String,Object> map1 = new HashMap<>();
                map1.put("info",gywlwDataTrend);
                List<GywlwHistoryItem> historyItems = gywlwHistoryItemMapper.getDataForTrend(
                        regId,gywlwDataTrend.getStartDate(),gywlwDataTrend.getEndDate());
                map1.put("data",historyItems);
                list.add(map1);
            }
        }
        return list;
    }

    public void saveTrendInfo(ReceiveModel model){
        try {
            for (Variable variable : model.getVariables()) {
                GywlwDataTrend gywlwDataTrend = new GywlwDataTrend();
                gywlwDataTrend.setRegId(variable.getRegId());
                gywlwDataTrend.setDeviceId(variable.getDeviceId());
                gywlwDataTrend.setProjectId(model.getProjectId());
                gywlwDataTrend.setCreateUserId(hostHolder.getGywlwUser().getUserId());
                Date date = MyUtil.timeTransformToDate(model.getStartTime());
                gywlwDataTrend.setStartDate(date);
                Date date1 = MyUtil.timeTransformToDate(model.getEndTime());
                gywlwDataTrend.setEndDate(date1);
                gywlwDataTrend.setLineColor(variable.getColor());
                gywlwDataTrend.setLineWidth(variable.getWidth());
                gywlwDataTrend.setPhotoName(model.getPhotoName());
                gywlwDataTrendMapper.insert(gywlwDataTrend);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void operateDeviceToProject(String deviceId, String projectId){
        GywlwProjectDeviceGroup gywlwProjectDeviceGroup = new GywlwProjectDeviceGroup();
        gywlwProjectDeviceGroup.setProjectId(projectId);
        gywlwProjectDeviceGroup.setDeviceId(deviceId);
        gywlwProjectDeviceGroupMapper.insertSelective(gywlwProjectDeviceGroup);
    }

    public List<GywlwWarningRules> getWarningList(String deviceId){
        return gywlwWarningRulesMapper.selectByDeviceId(deviceId);
    }

    //实时数据刷新
    public void refresh(){
        refreshData();
    }

    public void refreshData(){
        try {
//            List<String> list = new ArrayList<>();
        /*    list.add("D0001");
            list.add("alarm1");
            list.add("alarm2");
            list.add("D0002");*/

            List<GywlwDevice> devices = gywlwDeviceMapper.selectAll();
            GywlwHistoryItem gywlwHistoryItem = new GywlwHistoryItem();
            //对于每个盒子，找出它管理的plc，并按照plcid查找历史数据，告警数据
            if (devices.size() != 0) {
                for (GywlwDevice device : devices) {
                    List<GywlwPlcInfo> plcinfos = gywlwPlcInfoMapper.selectByDeviceId(device.getDeviceId());
                    if (plcinfos.size() != 0) {
                        for (GywlwPlcInfo plcInfo : plcinfos) {
                            Long timeStamp;
                            GywlwHistoryItem his = gywlwHistoryItemMapper.getLastTimeByPlcId(plcInfo.getId());

                            if(his == null) {
                                System.out.println("null");
                                timeStamp = 1477929600L;
                            }else{
                                timeStamp = his.getItemTime().getTime();
                            }
                            System.out.println("timeStamp:   "+timeStamp);
                            List<String> list = new ArrayList<>();
                            List<GywlwRegInfo> reglists = gywlwRegInfoMapper.selectByPlcId(plcInfo.getId());
                            if(reglists.size() == 0){
                                continue;
                            }else{
                                for(GywlwRegInfo reg: reglists){
                                    list.add(reg.getRegName());
                                }
                            }
                            //设置参数
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("deviceId", plcInfo.getSubdeviceId());//"0052000000000003"
                            jsonObject.put("idList", list);
                            jsonObject.put("timestamp", timeStamp);
                            System.out.println("param");
                            System.out.println(jsonObject);

                            String str = HttpClientUtil.post(jsonObject);

                            //handle response
                            JSONObject json = new JSONObject();
                            Map<String, Object> map1 = JSON.parseObject(str);
                            List<Map> list1 = JSON.parseArray(map1.get("result_data").toString(), Map.class);
                            System.out.println("list1.size():"+MyUtil.getJSONString(0,list1));
                            if(list1.size()!=0) {
                                handler(list1, device, plcInfo, list);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handler(List<Map> list1,GywlwDevice device,GywlwPlcInfo plcInfo,List<String> regList) throws ParseException {
        DataModel dataModel = new DataModel();
        Long time = 0L;
        List<DataModel> list = new ArrayList<>();
        int k = 0;
        for(Map map : list1){
            if(k>600){
                break;
            }
            if(!time.equals(map.get("timestamp")) && k != 0){
                dataModel.setTime(String.valueOf(map.get("timestamp")));
                list.add(dataModel);
                dataModel = new DataModel();
            }
            k++;
            if(map.get("alarm1")!=null){
                dataModel.setAlarm1("alarm1");
                dataModel.setAlarm1Value((String) map.get("alarm1"));
            }
            if(map.get("alarm2")!=null){
                dataModel.setAlarm2("alarm2");
                dataModel.setAlarm2Value((String) map.get("alarm2"));
            }
            /*if(map.get("D0001")!=null){
                dataModel.setName("D0001");
                dataModel.setValue(Double.parseDouble((String) map.get("D0001")));
            }
            if(map.get("D0002")!=null){
                dataModel.setName("D0002");
                dataModel.setValue(Double.parseDouble((String) map.get("D0002")));
            }*/
            for(String info : regList){
                if(map.get(info)!=null){
                    dataModel.setName(info);
                    dataModel.setValue(Double.parseDouble((String) map.get(info)));
                }
            }
            time = (Long) map.get("timestamp");
        }
        dataModel.setTime(String.valueOf(time));
        list.add(dataModel);
    //    System.out.println(MyUtil.getJSONString(0,list));
        List<GywlwHistoryItem> historyItems = new ArrayList<>();
        List<GywlwHistoryItem> historyItems1 = new ArrayList<>();

        for(DataModel model :list){
            GywlwHistoryItem gywlwHistoryItem = new GywlwHistoryItem();
            addItem(model,gywlwHistoryItem,device,plcInfo,regList);
            if(model.getAlarm1()!=null){
                gywlwHistoryItem.setSeverity(1);
                String string = model.getAlarm1Value();
                int index = string.lastIndexOf(".");
                gywlwHistoryItem.setRuleId(Integer.valueOf(string.substring(0,index)));
                gywlwHistoryItem.setItemContent(model.getAlarm1());
                historyItems.add(gywlwHistoryItem);
                gywlwHistoryItem = new GywlwHistoryItem();
                //防止被alarm2修改
                addItem(model,gywlwHistoryItem,device,plcInfo,regList);
            }
            if(model.getAlarm2()!=null){
                gywlwHistoryItem.setSeverity(2);
                String string = model.getAlarm2Value();
                int index = string.lastIndexOf(".");
                gywlwHistoryItem.setRuleId(Integer.valueOf(string.substring(0,index)));
                gywlwHistoryItem.setItemContent(model.getAlarm2());
                historyItems.add(gywlwHistoryItem);
            }
            if(model.getAlarm1() == null && model.getAlarm2()==null){
                historyItems1.add(gywlwHistoryItem);
            }
        }
   //     System.out.println(MyUtil.getJSONString(0,historyItems));
        if(historyItems.size() > 0) {
            gywlwHistoryItemMapper.insertBatch(historyItems);
        }
        if(historyItems1.size() > 0) {
            gywlwHistoryItemMapper.insertBatch1(historyItems1);
        }


    }

    public void addItem(DataModel model, GywlwHistoryItem gywlwHistoryItem,GywlwDevice device,GywlwPlcInfo plcInfo,List<String> regList) throws ParseException {
        Date date = MyUtil.timeTransformToDateNo1000(String.valueOf(model.getTime()));
        gywlwHistoryItem.setItemTime(date);
        gywlwHistoryItem.setDeviceId(device.getDeviceId());
        gywlwHistoryItem.setProjectId(gywlwProjectDeviceGroupMapper.selectByDeviceId(device.getDeviceId()).getProjectId());
        gywlwHistoryItem.setPlcId(plcInfo.getId());
        gywlwHistoryItem.setPlcName(plcInfo.getPlcName());
        /*if("D0002".equals(model.getName())) {
            gywlwHistoryItem.setRegId(gywlwRegInfoMapper.selectByRegName("D0002").getRegId());
            gywlwHistoryItem.setItemName("D0002");
            gywlwHistoryItem.setItemValue(model.getValue());
        }else if("D0001".equals(model.getName())){
            gywlwHistoryItem.setRegId(gywlwRegInfoMapper.selectByRegName("D0001").getRegId());
            gywlwHistoryItem.setItemName("D0001");
            gywlwHistoryItem.setItemValue(model.getValue());
        }*/
        for(String info : regList){
            if(info.equals(model.getName())){
                gywlwHistoryItem.setRegId(gywlwRegInfoMapper.selectByRegName(info).getRegId());
                gywlwHistoryItem.setItemName(info);
                gywlwHistoryItem.setItemValue(model.getValue());
            }
        }

    }






}

