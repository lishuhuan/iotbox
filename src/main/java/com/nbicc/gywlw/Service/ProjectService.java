package com.nbicc.gywlw.Service;

import com.nbicc.gywlw.Model.*;
import com.nbicc.gywlw.mapper.*;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * Created by BigMao on 2016/11/21.
 */
@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

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
    @Autowired
    private RefreshService refreshService;
    @Autowired
    private GywlwDeviceGpioMapper gywlwDeviceGpioMapper;
    @Autowired
    private GywlwHistoryDataForGPIOMapper gywlwHistoryDataForGPIOMapper;


    public List<GywlwProject> projectList(String gywlwUserId, Byte projectStatus) {
        List<GywlwProject> list = gywlwProjectMapper.selectByGywlwUserId(gywlwUserId, projectStatus);
        System.out.println(gywlwUserId);
        if(list.size()==0){
            list = gywlwProjectUserGroupMapper.selectProjectByUserId(gywlwUserId, projectStatus);
        }
        //设置权限
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
        GywlwProject project =  gywlwProjectMapper.selectByGywlwProjectId(projectId);
        //设置权限
        project.setWritePermission(Byte.parseByte("0"));
        if(project.getAdminId().equals(hostHolder.getGywlwUser().getUserId())){
            project.setWritePermission(Byte.parseByte("1"));
            return project;
        }else {
            GywlwProjectUserGroup gywlwProjectUserGroup = gywlwProjectUserGroupMapper.selectByProjectIdAndUserId(
                    project.getProjectId(), hostHolder.getGywlwUser().getUserId());
            if (gywlwProjectUserGroup != null) {
                project.setWritePermission(gywlwProjectUserGroup.getWritePermission());
                return project;
            }
        }
        return null;
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

    //删除所有与项目相关的
    public void deleteProject(String gywlwProjectId) {
        gywlwProjectDeviceGroupMapper.deleteByProjectId(gywlwProjectId);
        gywlwProjectUserGroupMapper.deleteByProjectIdAndUserId(gywlwProjectId,"ALL");
        gywlwVariableMapper.deleteByProjectId(gywlwProjectId);
        gywlwVariableRegGroupMapper.deleteByProjectId(gywlwProjectId);
        gywlwProjectMapper.deleteByProjectId(gywlwProjectId);//因为foreign key约束，所以project表最后删
    }

    public List<GywlwProjectUserGroup> projectMemberList(String projectId) {
        List<GywlwProjectUserGroup> list = gywlwProjectUserGroupMapper.selectByProjectId(projectId);
        String projectName = gywlwProjectMapper.selectByGywlwProjectId(projectId).getProjectName();
        for(GywlwProjectUserGroup gywlwProjectUserGroup:list){
            gywlwProjectUserGroup.setProjectName(projectName);
        }
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

    public void editMemberPermission(List<MemberPermission> list){
        GywlwProjectUserGroup gywlwProjectUserGroup = new GywlwProjectUserGroup();
        for(MemberPermission memberPermission : list){
            gywlwProjectUserGroup.setId(memberPermission.getId());
            gywlwProjectUserGroup.setWritePermission(memberPermission.getWrite_permission());
            gywlwProjectUserGroupMapper.updateByPrimaryKeySelective(gywlwProjectUserGroup);
        }
    }

    public List<GywlwVariableRegGroup> searchDataInProject(String projectId) {
        return gywlwVariableRegGroupMapper.selectByProjectId(projectId);
    }
    public List<GywlwVariable> variableList(String projectId,String variableName){
        return gywlwVariableMapper.selectByProjectId(projectId,variableName);
    }
    public List<GywlwVariable> variableListWithoutTime(String projectId){
        return gywlwVariableMapper.selectByProjectIdWithoutTime(projectId);
    }

    public int addVariable(String projectId, String variableName){
        if(gywlwVariableMapper.selectByProjectIdAndVariableName(projectId,variableName) != null){
            return -1;
        };
        GywlwVariable gywlwVariable = new GywlwVariable();
        gywlwVariable.setProjectId(projectId);
        gywlwVariable.setVariableName(variableName);
        gywlwVariable.setVariableId(UUID.randomUUID().toString().replaceAll("-",""));
        gywlwVariableMapper.insert(gywlwVariable);
        return 0;
    }

    public void deleteVariable(String variableId){
        gywlwVariableMapper.deleteByPrimaryKey(variableId);
    }

    public List<GywlwHistoryItem> searchHistoryData(String projectId, String variableName) {
        refreshService.refresh();
        List<GywlwHistoryItem> list = gywlwHistoryItemMapper.selectByVariableName(variableName, projectId);
        List<GywlwHistoryDataForGPIO> list1 = gywlwHistoryDataForGPIOMapper.getLatestDataByVariableName(projectId,variableName);
        if(list1.size() > 0){
            //todo
        }
        return list;

    }

    public List<GywlwHistoryItem> warningList(String projectId, String variableName, String startTime, String endTime) {
        refreshService.refresh();
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
        if(hostHolder.getGywlwUser().getUserType()==1 && hostHolder.getGywlwUser().getDuserLevel() == 0 &&
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

    public String giveAdmin(String userPhone,String password){
        if(!MyUtil.MD5(password).equals(gywlwUserMapper.selectByPhoneWithPsd(hostHolder.getGywlwUser().getUserPhone()).getUserPsd())){
            return "密码错误";
        }

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


    public void bindRegAndVariable(String variableId,String regId){
        String deviceId = gywlwRegInfoMapper.selectByPrimaryKey(regId).getDeviceId();
        String projectId = gywlwVariableMapper.selectByPrimaryKey(variableId).getProjectId();
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //当变量组绑定数据项时，将盒子与项目绑定
    public void operateDeviceToProject(String deviceId, String projectId){
        GywlwProjectDeviceGroup gywlwProjectDeviceGroup1 = gywlwProjectDeviceGroupMapper.
                selectByDeviceIdAndProjectId(deviceId,projectId);
        if(gywlwProjectDeviceGroup1 == null) {
            GywlwProjectDeviceGroup gywlwProjectDeviceGroup = new GywlwProjectDeviceGroup();
            gywlwProjectDeviceGroup.setProjectId(projectId);
            gywlwProjectDeviceGroup.setDeviceId(deviceId);
            gywlwProjectDeviceGroupMapper.insertSelective(gywlwProjectDeviceGroup);
        }
    }

    public List<GywlwWarningRules> getWarningList(String deviceId){
        return gywlwWarningRulesMapper.selectByDeviceId(deviceId);
    }

    //该用户下，树形结构列表（device/plc/reg)
    public List<TreeListModel> getTreeListOfUser(){
        GywlwUser gywlwUser = hostHolder.getGywlwUser();
        List<TreeListModel> treeListModels = new ArrayList<>();
        List<GywlwDevice> devices = gywlwDeviceMapper.selectByAdminId(gywlwUser.getUserId());
        if(devices.size() > 0){
            for(GywlwDevice device : devices){
                TreeListModel treeListModel = new TreeListModel();
                treeListModel.setType("device");
                treeListModel.setName(device.getDeviceName());
                treeListModel.setId(device.getDeviceId());
                List<GywlwPlcInfo> gywlwPlcInfos = gywlwPlcInfoMapper.selectByDeviceId(device.getDeviceId());
                List<GywlwDeviceGpio> gywlwDeviceGpios = gywlwDeviceGpioMapper.selectByDeviceId(device.getDeviceId());
                List<TreeListModel> treeListForPlc = new ArrayList<>();
                if(gywlwPlcInfos.size() > 0){
                    for(GywlwPlcInfo plcInfo : gywlwPlcInfos){
                        TreeListModel treeListModel1ForPlc = new TreeListModel();
                        treeListModel1ForPlc.setType("plc");
                        treeListModel1ForPlc.setName(plcInfo.getPlcName());
                        treeListModel1ForPlc.setId(plcInfo.getId());
                        List<GywlwRegInfo> regInfos = gywlwRegInfoMapper.selectByPlcId(plcInfo.getId());
                        List<TreeListModel> treeListForReg = new ArrayList<>();
                        if(regInfos.size() > 0){
                            for(GywlwRegInfo regInfo : regInfos){
                                TreeListModel modelForReg = new TreeListModel();
                                modelForReg.setType("reg");
                                modelForReg.setName(regInfo.getRegName());
                                modelForReg.setId(regInfo.getRegId());
                                treeListForReg.add(modelForReg);
                            }
                        }
                        treeListModel1ForPlc.setChildren(treeListForReg);
                        treeListForPlc.add(treeListModel1ForPlc);
                    }
                }
                if(gywlwDeviceGpios.size() > 0 ){
                    TreeListModel modelForGpio = new TreeListModel();
                    modelForGpio.setType("plc");
                    modelForGpio.setName("gpio");
                    modelForGpio.setId("0");
                    List<TreeListModel> treeListForGpio = new ArrayList<>();
                    for(GywlwDeviceGpio gywlwDeviceGpio : gywlwDeviceGpios){
                        TreeListModel modelForGpio1 = new TreeListModel();
                        modelForGpio1.setId(gywlwDeviceGpio.getId());
                        modelForGpio1.setName(gywlwDeviceGpio.getFieldName());
                        modelForGpio1.setType("gpio");
                        treeListForGpio.add(modelForGpio1);
                    }
                    modelForGpio.setChildren(treeListForGpio);
                    treeListForPlc.add(modelForGpio);
                }
                treeListModel.setChildren(treeListForPlc);
                treeListModels.add(treeListModel);
            }
        }
        return treeListModels;
    }

    //项目下，已经绑定的数据项列表
    public List<TreeListModel> getTreeListOfProject(String projectId) {
        List<TreeListModel> treeListModels = new ArrayList<>();
        List<GywlwVariable> variables = gywlwVariableMapper.selectByProjectIdWithoutTime(projectId);
        if(variables.size() > 0){
            for(GywlwVariable variable : variables){
                TreeListModel treeListModel = new TreeListModel();
                treeListModel.setType("variable");
                treeListModel.setName(variable.getVariableName());
                treeListModel.setId(variable.getVariableId());
                List<TreeListModel> listForReg = new ArrayList<>();
                List<GywlwVariableRegGroup> gywlwRegInfoList = gywlwVariableRegGroupMapper.selectByProjectIdAndVariable(projectId,
                        variable.getVariableName());
                if(gywlwRegInfoList.size() > 0){
                    for(GywlwVariableRegGroup variableRegGroup : gywlwRegInfoList){
                        TreeListModel modelForReg = new TreeListModel();
                        modelForReg.setType("reg");
                        modelForReg.setName(variableRegGroup.getRegName());
                        modelForReg.setId(variableRegGroup.getId().toString());//放这个关系的id，以便删除时用
                        listForReg.add(modelForReg);
                    }
                }
                treeListModel.setChildren(listForReg);
                treeListModels.add(treeListModel);
            }
        }
        return treeListModels;
    }

    //查看plc中数据项的历史数据
    public List<GywlwHistoryItem> getHistoryDataForReg(String regId,String startTime,String endTime) throws ParseException {
        refreshService.refresh();
        GywlwRegInfo gywlwRegInfo = gywlwRegInfoMapper.selectByPrimaryKey(regId);
        if(gywlwRegInfo == null){
            return null;
        }
        if(hostHolder.getGywlwUser().getUserId().
                equals(gywlwDeviceMapper.selectByDeviceId(gywlwRegInfo.getDeviceId()).getAdminId())){
            List<GywlwHistoryItem> list = gywlwHistoryItemMapper.getDataForReg(regId,
                    MyUtil.timeTransformToDateNo1000(startTime),MyUtil.timeTransformToDateNo1000(endTime));
            return list;
        }
        return null;
    }

    public List<GywlwHistoryDataForGPIO> getHistoryDataForGpio(String regId, String startTime, String endTime) {
//        refreshService.refresh();  //在查询数据项的时候已经刷新过一次了
        GywlwDeviceGpio gywlwDeviceGpio = gywlwDeviceGpioMapper.selectByPrimaryKey(regId);
        if(gywlwDeviceGpio == null){
            return null;
        }
        String[] str = gywlwDeviceGpio.getFieldAddress().split("_");
        return gywlwHistoryDataForGPIOMapper.getHistoryData(gywlwDeviceGpio.getDeviceId(),str[1]);
    }
}




