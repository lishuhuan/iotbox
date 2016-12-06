package com.nbicc.gywlw.Controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbicc.gywlw.Model.*;
import com.nbicc.gywlw.Service.MessageService;
import com.nbicc.gywlw.Service.ProjectService;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by BigMao on 2016/11/19.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MessageService messageService;

    //项目列表
    @RequestMapping(path = {"/projectlist"}, method = {RequestMethod.POST})
    @ResponseBody
    public String projectList(@RequestParam(value = "project_status", defaultValue = "0")String projectStatus) {
        try {
            String localUserId = hostHolder.getGywlwUser().getUserId();
            List<GywlwProject> allProject = new ArrayList<>();
            allProject = projectService.projectList(localUserId,Byte.parseByte(projectStatus));
            String returnString = MyUtil.getJSONString(0, allProject);
            return returnString;
        }catch (Exception e){
            logger.error("获取项目列表失败" + e.getMessage());
            return MyUtil.getJSONString(1, "获取项目列表失败");
        }
    }

    //新增项目
    @RequestMapping(path = {"/addproject"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addProject(@RequestParam("project_name") String projectName,
                          @RequestParam(value = "parent_text", defaultValue = "") String parentText,
                          @RequestParam(value = "project_desc", defaultValue = "") String projectDesc,
                          @RequestParam(value = "longitude", defaultValue = "") String longitude,
                          @RequestParam(value = "latitude", defaultValue = "") String latitude,
                          @RequestParam(value = "province", defaultValue = "") String province,
                          @RequestParam(value = "city", defaultValue = "") String city,
                          @RequestParam(value = "purchase_date", defaultValue = "") String purchaseDate,
                          @RequestParam(value = "completion_date", defaultValue = "") String completionDate,
                          @RequestParam(value = "install_date", defaultValue = "") String installDate,
                          @RequestParam(value = "contact_name", defaultValue = "") String contactName,
                          @RequestParam(value = "contact_phone", defaultValue = "") String contactPhone,
                          @RequestParam(value = "customer_type", defaultValue = "") String customerType,
                          @RequestParam(value = "first_party", defaultValue = "") String firstParty,
                          @RequestParam(value = "project_type", defaultValue = "") String projectType) {
        try {
            if (hostHolder.getGywlwUser() == null) {
                return MyUtil.getJSONString(1, "请登录！");
            }
            String newProjectId = projectService.editProject("",projectName,parentText,projectDesc,longitude,latitude,
                    province,city,purchaseDate,completionDate,installDate,contactName,contactPhone,customerType,
                    firstParty,projectType);
            return MyUtil.getJSONString(0, newProjectId);
        } catch (Exception e) {
            logger.error("新建项目失败" + e.getMessage());
            return MyUtil.getJSONString(1, "新建项目失败");
        }
    }

    @RequestMapping(path = {"/projectinfo"}, method = {RequestMethod.POST})
    @ResponseBody
    public String projectInfo(@RequestParam(value = "project_id")String projectId) {
        try {
            GywlwProject gywlwProject = projectService.projectInfo(projectId);

            String[] str = gywlwProject.getDisplay().split(",");
            List<String> list = Arrays.asList(str);
            gywlwProject.setDisplay1(list);
            return MyUtil.getJSONString(0,gywlwProject);
        }catch (Exception e){
            logger.error("获取项目列表失败" + e.getMessage());
            return MyUtil.getJSONString(1, "获取项目列表失败");
        }
    }


    //编辑项目
    @RequestMapping(path = {"/editproject"}, method = {RequestMethod.POST})
    @ResponseBody
    public String editProject(@RequestParam("project_id") String projectId,
                              @RequestParam("project_name") String projectName,
                             @RequestParam(value = "parent_text", defaultValue = "") String parentText,
                             @RequestParam(value = "project_desc", defaultValue = "") String projectDesc,
                             @RequestParam(value = "longitude", defaultValue = "") String longitude,
                             @RequestParam(value = "latitude", defaultValue = "") String latitude,
                             @RequestParam(value = "province", defaultValue = "") String province,
                             @RequestParam(value = "city", defaultValue = "") String city,
                             @RequestParam(value = "purchase_date", defaultValue = "") String purchaseDate,
                             @RequestParam(value = "completion_date", defaultValue = "") String completionDate,
                             @RequestParam(value = "install_date", defaultValue = "") String installDate,
                             @RequestParam(value = "contact_name", defaultValue = "") String contactName,
                             @RequestParam(value = "contact_phone", defaultValue = "") String contactPhone,
                             @RequestParam(value = "customer_type", defaultValue = "") String customerType,
                             @RequestParam(value = "first_party", defaultValue = "") String firstParty,
                             @RequestParam(value = "project_type", defaultValue = "") String projectType) {
        try {
            if (hostHolder.getGywlwUser() == null) {
                return MyUtil.getJSONString(1, "请登录！");
            }
            String newProjectId = projectService.editProject(projectId, projectName, parentText, projectDesc, longitude,
                    latitude, province, city, purchaseDate, completionDate, installDate, contactName, contactPhone,
                    customerType, firstParty, projectType);
            return MyUtil.getJSONString(0, newProjectId);
        }catch (Exception e) {
            logger.error("编辑项目失败" + e.getMessage());
            return MyUtil.getJSONString(1, "编辑项目失败");
        }
    }

    @RequestMapping(path = "editdisplay", method = RequestMethod.POST)
    @ResponseBody
    public String editDisplay(@RequestParam("project_id")String projectId,
                              @RequestParam("display")String display){
        try {
            projectService.editDisplay(projectId,display);
            return MyUtil.getJSONString(0,"编辑成功");
        }catch (Exception e){
            logger.error("编辑显示失败" + e.getMessage());
            return MyUtil.getJSONString(1, "编辑显示失败");
        }

    }
    //停用和启用项目
    @RequestMapping(path = {"/stopproject"}, method = {RequestMethod.POST})
    @ResponseBody
    public String stopProject(@RequestParam("project_id") String projectId,
                              @RequestParam("project_status")String projectStatus){
        try {
            projectService.stopProject(projectId, projectStatus);
            return MyUtil.getJSONString(0, "停用/启用成功！");
        }catch (Exception e){
            logger.error("停用启用失败" + e.getMessage());
            return MyUtil.getJSONString(1, "停用启用失败");
        }
    }


    //获取项目成员列表
    @RequestMapping(path = {"/projectmemberlist"}, method = {RequestMethod.POST})
    @ResponseBody
    public String projectMemberList(@RequestParam("project_id") String projectId){
        try {
            List<GywlwProjectUserGroup> allProjectMember = new ArrayList<GywlwProjectUserGroup>();
            allProjectMember = projectService.projectMemberList(projectId,0,5);
            String returnString = MyUtil.getJSONString(0, allProjectMember);
            return returnString;
        }catch (Exception e){
            logger.error("获取项目成员列表失败" + e.getMessage());
            return MyUtil.getJSONString(1, "获取项目成员列表失败");
        }
    }

    //搜索用户，限普通用户
    @RequestMapping(path = {"/searchuser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String searchUser(@RequestParam("user_phone") String userPhone){
        try{
            GywlwUser User = projectService.searchUser(userPhone);
            return MyUtil.getJSONString(0, User);
        }catch (Exception e){
            logger.error("搜索用户失败" + e.getMessage());
            return MyUtil.getJSONString(1, "搜不到该用户！");
        }
    }

    //添加项目成员
    @RequestMapping(path = {"/addprojectmember"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addProjectMember(@RequestParam("user_id") String userId,
                                   @RequestParam("write_permission")String writePermission,
                                   @RequestParam("project_id")String projectId){
        try {
            messageService.sendMessage(hostHolder.getGywlwUser().getUserId(),userId,"邀请你加入项目",
                    Byte.parseByte("3"),projectId+" "+writePermission);
            return MyUtil.getJSONString(0,"发送邀请成功");
        }catch (Exception e){
            logger.error("发送失败" + e.getMessage());
            return MyUtil.getJSONString(1, "发送失败!");
        }
    }

    //删除项目成员
    @RequestMapping(path = {"/deleteprojectmember"}, method = {RequestMethod.POST})
    @ResponseBody
    public String deleteProjectMember(@RequestParam("project_id")String projectId,
                                      @RequestParam("user_id")String userId){
        try {
            projectService.deleteProjectMember(projectId, userId);
            return MyUtil.getJSONString(0, "删除项目成员成功!");
        }catch (Exception e){
            logger.error("删除项目成员失败" + e.getMessage());
            return MyUtil.getJSONString(1, "删除项目成员失败!");
        }
    }


    //修改项目成员权限

    //项目关联变量组列表，并提供变量组搜索
    @RequestMapping(path = {"/datainproject"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dataInProject(@RequestParam("project_id")String projectId,
                                @RequestParam(value = "variable_name",defaultValue = "ALL")String variableName){
        try{
            List<GywlwVariableRegGroup> list = projectService.searchDataInProject(projectId,variableName);
            return MyUtil.getJSONString(0,list);
        }catch (Exception e){
            logger.error("未查到相关数据（变量组）" + e.getMessage());
            return MyUtil.getJSONString(1, "未查到相关数据!");
        }
    }

    //告警规则列表
    @RequestMapping(path = {"/warningruleslist"}, method = {RequestMethod.POST})
    @ResponseBody
    public String getWarningRulesList(@RequestParam("device_id")String deviceId){
        try{
            List<GywlwWarningRules> list = projectService.getWarningList(deviceId);
            return MyUtil.getJSONString(0,list);
        }catch (Exception e){
            logger.error("查找规则列表出错" + e.getMessage());
            return MyUtil.getJSONString(1, "查找规则列表出错!");
        }
    }

    //变量组中的数据详情
    @RequestMapping(path = {"/historydata"}, method = {RequestMethod.POST})
    @ResponseBody
    public String historyData(@RequestParam("project_id")String projectId,
                                @RequestParam(value = "variable_name")String variableName){
        try{
            List<GywlwHistoryItem> list = projectService.searchHistoryData(projectId,variableName);
            return MyUtil.getJSONString(0,list);
        }catch (Exception e){
            logger.error("未查到相关数据详情" + e.getMessage());
            return MyUtil.getJSONString(1, "未查到相关数据详情!");
        }
    }

    //告警列表,并提供变量组和日期查询
    @RequestMapping(path = {"/warninglist"}, method = {RequestMethod.POST})
    @ResponseBody
    public String warningList(@RequestParam("project_id")String projectId,
                              @RequestParam(value = "variable_name", defaultValue = "ALL")String variableName,
                              @RequestParam(value = "start_time",defaultValue = "0")String startTimeString,
                              @RequestParam(value = "end_time", defaultValue = "2480036920")String endTimeString){
        try{
            //将时间戳转化成date格式的字符串
            String startTime = MyUtil.timeTransformToString(startTimeString);
            String endTime = MyUtil.timeTransformToString(endTimeString);
            List<GywlwHistoryItem> list = projectService.warningList(projectId,variableName,startTime,endTime);
            return MyUtil.getJSONString(0,list);
        }catch (Exception e){
            logger.error("未查到告警数据详情" + e.getMessage());
            return MyUtil.getJSONString(1, "未查到告警数据详情!");
        }
    }

    //设备列表，唯一识别码查询
    @RequestMapping(path = {"/devicelist"}, method = {RequestMethod.POST})
    @ResponseBody
    public String deviceList(@RequestParam(value = "device_sn",defaultValue = "ALL")String deviceSn){
        try{
            List<GywlwDevice> list = projectService.getDeviceList(deviceSn);
            return MyUtil.getJSONString(0,list);
        }catch (Exception e){
            logger.error("未查到设备列表" + e.getMessage());
            return MyUtil.getJSONString(1, "未查到设备列表!");
        }
    }



    //对设备厂商权限分配
    @RequestMapping(path = {"/setexpire"}, method = {RequestMethod.POST})
    @ResponseBody
    public String setExpire(@RequestParam(value = "expired_date",defaultValue = "0")String expiredDate,
                            @RequestParam("expired_right")String expiredRight,
                            @RequestParam("device_id")String deviceId){
        try{
            if(!projectService.getDevice(deviceId).getAdminId().equals(hostHolder.getGywlwUser().getUserId())){
                return MyUtil.getJSONString(1,"无权限设置");
            }
            Date date = MyUtil.timeTransformToDate(expiredDate);
            projectService.setExpire(deviceId,date,Byte.parseByte(expiredRight));
            return MyUtil.getJSONString(0,"设置权限成功");
        }catch (Exception e){
            logger.error("设置权限出错" + e.getMessage());
            return MyUtil.getJSONString(1, "设置权限出错!");
        }
    }

    //查看用户资料
    @RequestMapping(path = {"/userinfo"}, method = {RequestMethod.POST})
    @ResponseBody
    public String userInfo(){
        try {
            Map<String, GywlwUser> map = new HashMap<>();
            map.put("user", hostHolder.getGywlwUser());
            return MyUtil.getJSONString(0, map);
        }catch (Exception e){
            return MyUtil.getJSONString(1, "请登录!");
        }
    }

    //修改资料
    @RequestMapping(path = {"/changeinfo"}, method = {RequestMethod.POST})
    @ResponseBody
    public String changeInfo(@RequestParam("user_name")String userName,
                             @RequestParam(value = "company_name",defaultValue = "")String companyName,
                             @RequestParam("sex")String sex,
                             @RequestParam("e_mail")String email,
                             @RequestParam("fixedphone")String fixedphone){
        try{
            int code = projectService.changeInfo(userName,companyName,sex,email,fixedphone);
            GywlwUser gywlwUser = projectService.searchUser(hostHolder.getGywlwUser().getUserPhone());
            hostHolder.setGywlwUser(gywlwUser);
            if(code == 0) {
                return MyUtil.getJSONString(0, "修改资料成功");
            }else{
                return MyUtil.getJSONString(1, "不能修改公司名");
            }
        }catch (Exception e){
            logger.error("修改资料出错" + e.getMessage());
            return MyUtil.getJSONString(1, "修改资料出错!");
        }
    }


    //绑定设备
    @RequestMapping(path = {"/binddevice"}, method = {RequestMethod.POST})
    @ResponseBody
    public String bindDevvice(@RequestParam("device_sn")String deviceSn){
        try{
            String msg = projectService.bindDevice(deviceSn,0);//mark=0为绑定设备
            return MyUtil.getJSONString(0,msg);
        }catch (Exception e){
            logger.error("绑定设备出错" + e.getMessage());
            return MyUtil.getJSONString(1, "绑定设备出错!");
        }

    }

    //解绑设备
    @RequestMapping(path = {"/unbinddevice"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unbindDevvice(@RequestParam("device_sn")String deviceSn){
        try{
            String msg = projectService.bindDevice(deviceSn,1);//mark=1为解绑设备
            return MyUtil.getJSONString(0,msg);
        }catch (Exception e){
            logger.error("解绑设备出错" + e.getMessage());
            return MyUtil.getJSONString(1, "解绑设备出错!");
        }

    }

    //转移管理员权限
    @RequestMapping(path = {"/giveadmin"}, method = {RequestMethod.POST})
    @ResponseBody
    public String giveAdmin(@RequestParam("user_phone")String userPhone){
        try {
            String msg = projectService.giveAdmin(userPhone);
            return MyUtil.getJSONString(0, msg);
        }catch (Exception e){
            logger.error("转移管理员权限出错" + e.getMessage());
            return MyUtil.getJSONString(1, "转移管理员权限出错!");
        }
    }

    //变量组列表
    @RequestMapping(path = {"/variablelist"}, method = {RequestMethod.POST})
    @ResponseBody
    public String variableList(@RequestParam("project_id") String projectId){
        try{
            List<GywlwVariable> list = projectService.variableList(projectId);
            return MyUtil.getJSONString(0,list);
        }catch (Exception e){
            logger.error("获取变量组列表失败" + e.getMessage());
            return MyUtil.getJSONString(1, "获取变量组列表失败!");
        }
    }

    //plc列表
    @RequestMapping(path = {"/plclist"}, method = {RequestMethod.POST})
    @ResponseBody
    public String plcList(@RequestParam("device_id") String deviceId){
        try{
            List<GywlwPlcInfo> list = projectService.getPlcListByDeviceId(deviceId);
            return MyUtil.getJSONString(0,list);
        }catch (Exception e){
            logger.error("获取plc列表失败" + e.getMessage());
            return MyUtil.getJSONString(1, "获取plc列表失败!");
        }
    }

    //数据项列表
    @RequestMapping(path = {"/reglist"}, method = {RequestMethod.POST})
    @ResponseBody
    public String regList(@RequestParam("plc_id") String plcId){
        try{
            List<GywlwRegInfo> list = projectService.getRegListByPlcId(plcId);
            return MyUtil.getJSONString(0,list);
        }catch (Exception e){
            logger.error("获取数据项列表失败" + e.getMessage());
            return MyUtil.getJSONString(1, "获取数据项列表失败!");
        }
    }

    @RequestMapping(path = {"/user/bindvariableandreg"}, method = {RequestMethod.POST})
    @ResponseBody
    public String bindVariableAndReg(@RequestParam("variable_id")String variableId,
                                     @RequestParam("reg_id")String regId,
                                     @RequestParam("device_id")String deviceId,
                                     @RequestParam("project_id")String projectId){
        try{
            projectService.bindRegAndVariable(variableId,regId,deviceId,projectId);
            return MyUtil.getJSONString(0,"绑定成功");
        }catch (Exception e){
            logger.error("绑定失败（variableandreg）" + e.getMessage());
            return MyUtil.getJSONString(1, "绑定失败!");
        }
    }

    @RequestMapping(path = {"/unbindvariableandreg"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unbindVariableAndReg(@RequestParam("id")String id){
        try{
            projectService.unbindRegAndVariable(id);
            return MyUtil.getJSONString(0,"解绑成功");
        }catch (Exception e){
            logger.error("解绑失败（variableandreg）" + e.getMessage());
            return MyUtil.getJSONString(1, "解绑失败!");
        }
    }

    //趋势参数设置
    @RequestMapping(path = {"/trendpicture"}, method = {RequestMethod.POST})
    @ResponseBody
    public String trendpicture(@RequestBody ReceiveModel model){
        try {
            projectService.saveTrendInfo(model);
            return MyUtil.getJSONString(0, "ok");
        }catch (Exception e){
            logger.error("设置趋势出错" + e.getMessage());
            return MyUtil.getJSONString(1, "设置趋势出错!");
        }
    }


    //趋势数据输出
    @RequestMapping(path = {"/getdata"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dataForTrend(@RequestParam("photo_name")String photoName){
        try {
            List<Map> list = projectService.getDataForTrend(photoName);
            return MyUtil.getJSONString(0, list);
        }catch (Exception e){
            logger.error("获取趋势数据失败" + e.getMessage());
            return MyUtil.getJSONString(1, "获取趋势数据失败!");
        }
    }

    //项目列表,带分页
    @RequestMapping(path = {"/projectlistbypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String projectListByPage(@RequestParam(value = "project_status", defaultValue = "0")String projectStatus,
                                    @RequestParam(value = "page_num", required = false, defaultValue = "2")Integer pageNum,
                                    @RequestParam(value = "page_size", required = false, defaultValue = "3")Integer pageSize) {
        try {
            String localUserId = hostHolder.getGywlwUser().getUserId();
            List<GywlwProject> allProject = new ArrayList<>();
            PageHelper.startPage(pageNum,pageSize);
            allProject = projectService.projectList(localUserId,Byte.parseByte(projectStatus));
            PageInfo<GywlwProject> pageInfo = new PageInfo<>(allProject);
            return MyUtil.getJSONString(0, pageInfo);
        }catch (Exception e){
            logger.error("获取项目列表失败" + e.getMessage());
            return MyUtil.getJSONString(1, "获取项目列表失败");
        }
    }

    @RequestMapping(path = {"/refresh"}, method = {RequestMethod.POST})
    @ResponseBody
    public String refreshData(){
        try {
            projectService.refresh();
            return "ok";
        }catch (Exception e){
            logger.error("refresh失败" + e.getMessage());
            return MyUtil.getJSONString(1, "refresh失败");
        }
    }




}
