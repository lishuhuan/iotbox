package com.nbicc.gywlw.Controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbicc.gywlw.Model.*;
import com.nbicc.gywlw.Service.MessageService;
import com.nbicc.gywlw.Service.ProjectService;
import com.nbicc.gywlw.Service.RefreshService;
import com.nbicc.gywlw.Service.UserService;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

/**
 * Created by BigMao on 2016/11/19.
 * 用户层
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

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshService refreshService;

    //项目列表
    @RequestMapping(path = {"/projectlist"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject projectList(@RequestParam(value = "project_status", defaultValue = "1")String projectStatus) {
        try {
            String localUserId = hostHolder.getGywlwUser().getUserId();
            List<GywlwProject> allProject = projectService.projectList(localUserId,Byte.parseByte(projectStatus));
            return MyUtil.response(0, allProject);
        }catch (Exception e){
            logger.error("获取项目列表失败" + e.getMessage());
            return MyUtil.response(1, "获取项目列表失败");
        }
    }

    //项目列表,带分页
    @RequestMapping(path = {"/projectlistbypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject projectListByPage(@RequestParam(value = "project_status", defaultValue = "0")String projectStatus,
                                        @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
                                        @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize) {
        try {
            String localUserId = hostHolder.getGywlwUser().getUserId();
            PageHelper.startPage(pageNum,pageSize);
            List<GywlwProject> allProject = projectService.projectList(localUserId,Byte.parseByte(projectStatus));
            PageInfo<GywlwProject> pageInfo = new PageInfo<>(allProject);
            return MyUtil.response(0, pageInfo);
        }catch (Exception e){
            logger.error("获取项目列表失败" + e.getMessage());
            return MyUtil.response(1, "获取项目列表失败");
        }
    }

    //新增项目
    @RequestMapping(path = {"/addproject"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject addProject(@RequestParam("project_name") String projectName,
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
                return MyUtil.response(1, "请登录！");
            }
            String newProjectId = projectService.editProject("",projectName,parentText,projectDesc,longitude,latitude,
                    province,city,purchaseDate,completionDate,installDate,contactName,contactPhone,customerType,
                    firstParty,projectType);
            return MyUtil.response(0, newProjectId);
        } catch (Exception e) {
            logger.error("新建项目失败" + e.getMessage());
            return MyUtil.response(1, "新建项目失败");
        }
    }

    //项目信息
    @RequestMapping(path = {"/projectinfo"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject projectInfo(@RequestParam(value = "project_id")String projectId) {
        try {
            GywlwProject gywlwProject = projectService.projectInfo(projectId);
            if(gywlwProject == null){
                return MyUtil.response(0,"未找到项目");
            }
            String[] str = gywlwProject.getDisplay().split(",");
            List<String> list = Arrays.asList(str);
            List<Integer> list1 = new ArrayList<>();
            for(String string : list){
                if(string.equals("0")){
                    list1.add(0);
                }else{
                    list1.add(1);
                }
            }
            gywlwProject.setDisplay1(list1);
            return MyUtil.response(0,gywlwProject);
        }catch (Exception e){
            logger.error("获取项目列表失败" + e.getMessage());
            return MyUtil.response(1, "获取项目列表失败");
        }
    }


    //编辑项目
    @RequestMapping(path = {"/editproject"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject editProject(@RequestParam("project_id") String projectId,
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
                return MyUtil.response(1, "请登录！");
            }
            String newProjectId = projectService.editProject(projectId, projectName, parentText, projectDesc, longitude,
                    latitude, province, city, purchaseDate, completionDate, installDate, contactName, contactPhone,
                    customerType, firstParty, projectType);
            return MyUtil.response(0, newProjectId);
        }catch (Exception e) {
            logger.error("编辑项目失败" + e.getMessage());
            return MyUtil.response(1, "编辑项目失败");
        }
    }

    //修改项目模块可见权限
    @RequestMapping(path = "/editdisplay", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject editDisplay(@RequestParam("project_id")String projectId,
                              @RequestParam("display")String display){
        try {
            projectService.editDisplay(projectId,display);
            return MyUtil.response(0,"编辑成功");
        }catch (Exception e){
            logger.error("编辑显示失败" + e.getMessage());
            return MyUtil.response(1, "编辑显示失败");
        }
    }

    //停用和启用项目
    @RequestMapping(path = {"/stopproject"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject stopProject(@RequestParam("project_id") String projectId,
                              @RequestParam("project_status")String projectStatus){
        try {
            projectService.stopProject(projectId, projectStatus);
            return MyUtil.response(0, "停用/启用成功！");
        }catch (Exception e){
            logger.error("停用启用失败" + e.getMessage());
            return MyUtil.response(1, "停用启用失败");
        }
    }

    //删除项目
    @RequestMapping(path = {"/deleteproject"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject deleteProject(@RequestParam("project_id") String projectId){
        try {
            projectService.deleteProject(projectId);
            return MyUtil.response(0, "删除项目成功！");
        }catch (Exception e){
            logger.error("删除项目失败" + e.getMessage());
            return MyUtil.response(1, "删除项目失败");
        }
    }


    //获取项目成员列表
    @RequestMapping(path = {"/projectmemberlist"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject projectMemberList(@RequestParam("project_id") String projectId){
        try {
            List<GywlwProjectUserGroup> allProjectMember = projectService.projectMemberList(projectId);
            return MyUtil.response(0, allProjectMember);
        }catch (Exception e){
            logger.error("获取项目成员列表失败" + e.getMessage());
            return MyUtil.response(1, "获取项目成员列表失败");
        }
    }

    //获取项目成员列表by page
    @RequestMapping(path = {"/projectmemberlistbypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject projectMemberListByPage(@RequestParam("project_id") String projectId,
                                              @RequestParam(value = "page_num",defaultValue = "1")Integer pageNum,
                                              @RequestParam(value = "page_size",defaultValue = "0")Integer pageSize){
        try {
            PageHelper.startPage(pageNum,pageSize);
            List<GywlwProjectUserGroup>allProjectMember = projectService.projectMemberList(projectId);
            PageInfo<GywlwProjectUserGroup> pageInfo = new PageInfo<>(allProjectMember);
            return MyUtil.response(0, pageInfo);
        }catch (Exception e){
            logger.error("获取项目成员列表失败" + e.getMessage());
            return MyUtil.response(1, "获取项目成员列表失败");
        }
    }

    //搜索用户，限普通用户
    @RequestMapping(path = {"/searchuser"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject searchUser(@RequestParam("user_phone") String userPhone){
        try{
            GywlwUser User = projectService.searchUser(userPhone);
            if(User == null){
                return MyUtil.response(1, "搜不到该用户！");
            }
            return MyUtil.response(0, User);
        }catch (Exception e){
            logger.error("搜索用户失败" + e.getMessage());
            return MyUtil.response(1, "搜索该用户失败！");
        }
    }

    //添加项目成员
    @RequestMapping(path = {"/addprojectmember"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject addProjectMember(@RequestParam("user_id") String userId,
                                   @RequestParam("write_permission")String writePermission,
                                   @RequestParam("project_id")String projectId){
        try {
            messageService.sendMessage(hostHolder.getGywlwUser().getUserId(),userId,"邀请你加入项目",
                    Byte.parseByte("3"),projectId+" "+writePermission);
            return MyUtil.response(0,"发送邀请成功");
        }catch (Exception e){
            logger.error("发送失败" + e.getMessage());
            return MyUtil.response(1, "发送失败!");
        }
    }

    //删除项目成员
    @RequestMapping(path = {"/deleteprojectmember"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject deleteProjectMember(@RequestParam("project_id")String projectId,
                                      @RequestParam("user_id")String userId){
        try {
            projectService.deleteProjectMember(projectId, userId);
            return MyUtil.response(0, "删除项目成员成功!");
        }catch (Exception e){
            logger.error("删除项目成员失败" + e.getMessage());
            return MyUtil.response(1, "删除项目成员失败!");
        }
    }

    //修改项目成员权限
    @RequestMapping(path = {"/editprojectmember"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject editProjectMember(@RequestBody List<MemberPermission> list){
        try {
            projectService.editMemberPermission(list);
            return MyUtil.response(0,"修改权限成功");
        }catch (Exception e){
            logger.error("修改项目成员权限失败" + e.getMessage());
            return MyUtil.response(1, "修改项目成员权限失败!");
        }
    }

    //项目关联变量组列表，并提供变量组搜索
    @RequestMapping(path = {"/datainproject"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject dataInProject(@RequestParam("project_id")String projectId){
        try{
            List<GywlwVariableRegGroup> list = projectService.searchDataInProject(projectId);
            return MyUtil.response(0,list);
        }catch (Exception e){
            logger.error("未查到相关数据（变量组）" + e.getMessage());
            return MyUtil.response(1, "未查到相关数据!");
        }
    }

    //项目关联变量组列表，并提供变量组搜索by page
    @RequestMapping(path = {"/datainprojectbypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject dataInProjectByPage(@RequestParam("project_id")String projectId,
                                          @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
                                          @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize){
        try{
            PageHelper.startPage(pageNum,pageSize);
            List<GywlwVariableRegGroup> list = projectService.searchDataInProject(projectId);
            PageInfo<GywlwVariableRegGroup> pageInfo = new PageInfo<>(list);
            return MyUtil.response(0, pageInfo);
        }catch (Exception e){
            logger.error("未查到相关数据（变量组）" + e.getMessage());
            return MyUtil.response(1, "未查到相关数据!");
        }
    }

    //告警规则列表
    @RequestMapping(path = {"/warningruleslist"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject getWarningRulesList(@RequestParam("device_id")String deviceId){
        try{
            List<GywlwWarningRules> list = projectService.getWarningList(deviceId);
            return MyUtil.response(0,list);
        }catch (Exception e){
            logger.error("查找规则列表出错" + e.getMessage());
            return MyUtil.response(1, "查找规则列表出错!");
        }
    }

    //告警规则列表by page
    @RequestMapping(path = {"/warningruleslistbypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject getWarningRulesListByPage(@RequestParam("device_id")String deviceId,
                                                @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
                                                @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize){
        try{
            PageHelper.startPage(pageNum,pageSize);
            List<GywlwWarningRules> list = projectService.getWarningList(deviceId);
            PageInfo<GywlwWarningRules> pageInfo = new PageInfo<>(list);
            return MyUtil.response(0, pageInfo);
        }catch (Exception e){
            logger.error("查找规则列表出错" + e.getMessage());
            return MyUtil.response(1, "查找规则列表出错!");
        }
    }

    //变量组中的数据详情
    @RequestMapping(path = {"/historydata"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject historyData(@RequestParam("project_id")String projectId,
                                @RequestParam(value = "variable_name",defaultValue = "ALL")String variableName){
        try{
            List<GywlwHistoryItem> list = projectService.searchHistoryData(projectId,variableName);
            return MyUtil.response(0,list);
        }catch (Exception e){
            logger.error("未查到相关数据详情" + e.getMessage());
            return MyUtil.response(1, "未查到相关数据详情!");
        }
    }

    //变量组中的数据详情by page
    @RequestMapping(path = {"/historydatabypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject historyDataByPage(@RequestParam("project_id")String projectId,
                                        @RequestParam(value = "variable_name",defaultValue = "ALL")String variableName,
                                        @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
                                        @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize ){
        try{
            PageHelper.startPage(pageNum,pageSize);
            List<GywlwHistoryItem> list = projectService.searchHistoryData(projectId,variableName);
            PageInfo<GywlwHistoryItem> pageInfo = new PageInfo<>(list);
            return MyUtil.response(0, pageInfo);
        }catch (Exception e){
            logger.error("未查到相关数据详情" + e.getMessage());
            return MyUtil.response(1, "未查到相关数据详情!");
        }
    }

    //告警列表,并提供变量组和日期查询
    @RequestMapping(path = {"/warninglist"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject warningList(@RequestParam("project_id")String projectId,
                              @RequestParam(value = "variable_name", defaultValue = "ALL")String variableName,
                              @RequestParam(value = "start_time",defaultValue = "0")String startTimeString,
                              @RequestParam(value = "end_time", defaultValue = "2480036920")String endTimeString){
        try{
            //将时间戳转化成date格式的字符串
            String startTime = MyUtil.timeTransformToString(startTimeString);
            String endTime = MyUtil.timeTransformToString(endTimeString);
            List<GywlwHistoryItem> list = projectService.warningList(projectId,variableName,startTime,endTime);
            return MyUtil.response(0,list);
        }catch (Exception e){
            logger.error("未查到告警数据详情" + e.getMessage());
            return MyUtil.response(1, "未查到告警数据详情!");
        }
    }

    //告警列表,并提供变量组和日期查询by page
    @RequestMapping(path = {"/warninglistbypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject warningListByPage(@RequestParam("project_id")String projectId,
                                        @RequestParam(value = "variable_name", defaultValue = "ALL")String variableName,
                                        @RequestParam(value = "start_time",defaultValue = "0")String startTimeString,
                                        @RequestParam(value = "end_time", defaultValue = "2480036920")String endTimeString,
                                        @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
                                        @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize ){
        try{
            //将时间戳转化成date格式的字符串
            String startTime = MyUtil.timeTransformToString(startTimeString);
            String endTime = MyUtil.timeTransformToString(endTimeString);
            PageHelper.startPage(pageNum,pageSize);
            List<GywlwHistoryItem> list = projectService.warningList(projectId,variableName,startTime,endTime);
            PageInfo<GywlwHistoryItem> pageInfo = new PageInfo<>(list);
            return MyUtil.response(0, pageInfo);
        }catch (Exception e){
            logger.error("未查到告警数据详情" + e.getMessage());
            return MyUtil.response(1, "未查到告警数据详情!");
        }
    }

    //设备列表，唯一识别码查询
    @RequestMapping(path = {"/devicelist"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject deviceList(@RequestParam(value = "device_sn",defaultValue = "ALL")String deviceSn){
        try{
            List<GywlwDevice> list = projectService.getDeviceList(deviceSn);
            return MyUtil.response(0,list);
        }catch (Exception e){
            logger.error("未查到设备列表" + e.getMessage());
            return MyUtil.response(1, "未查到设备列表!");
        }
    }

    //设备列表，唯一识别码查询 by page
    @RequestMapping(path = {"/devicelistbypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject deviceListByPage(@RequestParam(value = "device_sn",defaultValue = "ALL")String deviceSn,
                                       @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
                                       @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize){
        try{
            PageHelper.startPage(pageNum,pageSize);
            List<GywlwDevice> list = projectService.getDeviceList(deviceSn);
            PageInfo<GywlwDevice> pageInfo = new PageInfo<>(list);
            return MyUtil.response(0, pageInfo);
        }catch (Exception e){
            logger.error("未查到设备列表" + e.getMessage());
            return MyUtil.response(1, "未查到设备列表!");
        }
    }


    //对设备厂商权限分配
    @RequestMapping(path = {"/setexpire"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject setExpire(@RequestParam(value = "expired_date",defaultValue = "0")String expiredDate,
                            @RequestParam("expired_right")String expiredRight,
                            @RequestParam("device_id")String deviceId){
        try{
            if(!projectService.getDevice(deviceId).getAdminId().equals(hostHolder.getGywlwUser().getUserId())){
                return MyUtil.response(1,"无权限设置");
            }
            Date date = MyUtil.timeTransformToDate(expiredDate);
            projectService.setExpire(deviceId,date,Byte.parseByte(expiredRight));
            return MyUtil.response(0,"设置权限成功");
        }catch (Exception e){
            logger.error("设置权限出错" + e.getMessage());
            return MyUtil.response(1, "设置权限出错!");
        }
    }

    //查看用户资料
    @RequestMapping(path = {"/userinfo"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject userInfo(){
        try {
            Map<String, GywlwUser> map = new HashMap<>();
            map.put("user", hostHolder.getGywlwUser());
            return MyUtil.response(0,map);
        }catch (Exception e){
            return MyUtil.response(1, "请登录!");
        }
    }

    //修改资料
    @RequestMapping(path = {"/changeinfo"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject changeInfo(@RequestParam("user_name")String userName,
                             @RequestParam(value = "company_name",defaultValue = "")String companyName,
                             @RequestParam("sex")String sex,
                             @RequestParam("e_mail")String email,
                             @RequestParam("fixedphone")String fixedphone){
        try{
            int code = projectService.changeInfo(userName,companyName,sex,email,fixedphone);
            GywlwUser gywlwUser = projectService.searchUser(hostHolder.getGywlwUser().getUserPhone());
            hostHolder.setGywlwUser(gywlwUser);
            if(code == 0) {
                return MyUtil.response(0, "修改资料成功");
            }else{
                return MyUtil.response(1, "不能修改公司名");
            }
        }catch (Exception e){
            logger.error("修改资料出错" + e.getMessage());
            return MyUtil.response(1, "修改资料出错!");
        }
    }

    //修改密码（通过旧密码）
    @RequestMapping(path = {"/changepsd"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject changePsd(@RequestParam("old_password")String oldPassword,
                                @RequestParam("new_password")String newPassword){
        try{
            int resultCode = userService.changePsd("",oldPassword,newPassword,1);
            if(resultCode == -1){
                return MyUtil.response(1,"密码错误");
            }
            return MyUtil.response(0,"修改密码成功");
        }catch (Exception e){
            logger.error("修改密码异常 " + e.getMessage());
            return MyUtil.response(1, "修改密码异常");
        }
    }

    //绑定设备
    @RequestMapping(path = {"/binddevice"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject bindDevvice(@RequestParam("device_sn")String deviceSn){
        try{
            String msg = projectService.bindDevice(deviceSn,0);//mark=0为绑定设备
            return MyUtil.response(0,msg);
        }catch (Exception e){
            logger.error("绑定设备出错" + e.getMessage());
            return MyUtil.response(1, "绑定设备出错!");
        }

    }

    //解绑设备
    @RequestMapping(path = {"/unbinddevice"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject unbindDevvice(@RequestParam("device_sn")String deviceSn){
        try{
            String msg = projectService.bindDevice(deviceSn,1);//mark=1为解绑设备
            return MyUtil.response(0,msg);
        }catch (Exception e){
            logger.error("解绑设备出错" + e.getMessage());
            return MyUtil.response(1, "解绑设备出错!");
        }

    }

    //转移管理员权限
    @RequestMapping(path = {"/giveadmin"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject giveAdmin(@RequestParam("user_phone")String userPhone,
                                @RequestParam("password")String password){
        try {
            String msg = projectService.giveAdmin(userPhone,password);
            return MyUtil.response(0, msg);
        }catch (Exception e){
            logger.error("转移管理员权限出错" + e.getMessage());
            return MyUtil.response(1, "转移管理员权限出错!");
        }
    }

    //变量组列表,带最近联系时间
    @RequestMapping(path = {"/variablelist"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject variableList(@RequestParam("project_id") String projectId,
                                   @RequestParam(value = "variable_name",defaultValue = "ALL") String variableName){
        try{
            List<GywlwVariable> list = projectService.variableList(projectId,variableName);
            return MyUtil.response(0,list);
        }catch (Exception e){
            logger.error("获取变量组列表失败1" + e.getMessage());
            return MyUtil.response(1, "获取变量组列表失败!");
        }
    }

    //变量组列表,带最近联系时间 by page
    @RequestMapping(path = {"/variablelistbypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject variableListByPage(@RequestParam("project_id") String projectId,
                                         @RequestParam(value = "variable_name",defaultValue = "ALL") String variableName,
                                         @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
                                         @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize){
        try{
            PageHelper.startPage(pageNum,pageSize);
            List<GywlwVariable> list = projectService.variableList(projectId,variableName);
            PageInfo<GywlwVariable> pageInfo = new PageInfo<>(list);
            return MyUtil.response(0, pageInfo);
        }catch (Exception e){
            logger.error("获取变量组列表失败1" + e.getMessage());
            return MyUtil.response(1, "获取变量组列表失败!");
        }
    }

    //变量组列表,不带最近联系时间
    @RequestMapping(path = {"/variablelistwithouttime"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject variableListWT(@RequestParam("project_id") String projectId){
        try{
            List<GywlwVariable> list = projectService.variableListWithoutTime(projectId);
            return MyUtil.response(0,list);
        }catch (Exception e){
            logger.error("获取变量组列表失败" + e.getMessage());
            return MyUtil.response(1, "获取变量组列表失败!");
        }
    }

    //变量组列表,不带最近联系时间 by page
    @RequestMapping(path = {"/variablelistwithouttimebypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject variableListWTByPage(@RequestParam("project_id") String projectId,
                                         @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
                                         @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize){
        try{
            PageHelper.startPage(pageNum,pageSize);
            List<GywlwVariable> list = projectService.variableListWithoutTime(projectId);
            PageInfo<GywlwVariable> pageInfo = new PageInfo<>(list);
            return MyUtil.response(0, pageInfo);
        }catch (Exception e){
            logger.error("获取变量组列表失败" + e.getMessage());
            return MyUtil.response(1, "获取变量组列表失败!");
        }
    }

    //新建变量组
    @RequestMapping(path = {"/addvariable"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject addVariable(@RequestParam("project_id")String projectId,
                                  @RequestParam("variable_name")String variableName){
        try{
            int mark = projectService.addVariable(projectId,variableName);
            if(mark == -1){
                return MyUtil.response(1,"变量组名重复");
            }
            return MyUtil.response(0,"OK");
        }catch (Exception e){
            logger.error("新建变量组失败" + e.getMessage());
            return MyUtil.response(1, "新建变量组失败!");
        }
    }

    //删除变量组
    @RequestMapping(path = {"/deletevariable"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject deleteVariable(@RequestParam("variable_id")String variableId){
        try{
            projectService.deleteVariable(variableId);
            return MyUtil.response(0,"OK");
        }catch (Exception e){
            logger.error("删除变量组失败" + e.getMessage());
            return MyUtil.response(1, "删除变量组失败!");
        }
    }

    //批量删除变量组
    @RequestMapping(path = {"/deletevariablebatch"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject deleteVariableBatch(@RequestBody List<Map> list){
        try{
            if(list.size() > 0) {
                for (Map map : list) {
                    String variableId = (String) map.get("variable_id");
                    projectService.deleteVariable(variableId);
                }
            }
            return MyUtil.response(0,"OK");
        }catch (Exception e){
            logger.error("删除变量组失败" + e.getMessage());
            return MyUtil.response(1, "删除变量组失败!");
        }
    }


    //plc列表
    @RequestMapping(path = {"/plclist"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject plcList(@RequestParam("device_id") String deviceId){
        try{
            List<GywlwPlcInfo> list = projectService.getPlcListByDeviceId(deviceId);
            return MyUtil.response(0,list);
        }catch (Exception e){
            logger.error("获取plc列表失败" + e.getMessage());
            return MyUtil.response(1, "获取plc列表失败!");
        }
    }

    //plc列表 by page
    @RequestMapping(path = {"/plclistbypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject plcListByPage(@RequestParam("device_id") String deviceId,
                              @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
                              @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize){
        try{
            PageHelper.startPage(pageNum,pageSize);
            List<GywlwPlcInfo> list = projectService.getPlcListByDeviceId(deviceId);
            PageInfo<GywlwPlcInfo> pageInfo = new PageInfo<>(list);
            return MyUtil.response(0, pageInfo);
        }catch (Exception e){
            logger.error("获取plc列表失败" + e.getMessage());
            return MyUtil.response(1, "获取plc列表失败!");
        }
    }

    //数据项列表
    @RequestMapping(path = {"/reglist"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject regList(@RequestParam("plc_id") String plcId){
        try{
            List<GywlwRegInfo> list = projectService.getRegListByPlcId(plcId);
            return MyUtil.response(0,list);
        }catch (Exception e){
            logger.error("获取数据项列表失败" + e.getMessage());
            return MyUtil.response(1, "获取数据项列表失败!");
        }
    }

    //数据项列表 by page
    @RequestMapping(path = {"/reglistbypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject regListByPage(@RequestParam("plc_id") String plcId,
                                    @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
                                    @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize){
        try{
            PageHelper.startPage(pageNum,pageSize);
            List<GywlwRegInfo> list = projectService.getRegListByPlcId(plcId);
            PageInfo<GywlwRegInfo> pageInfo = new PageInfo<>(list);
            return MyUtil.response(0, pageInfo);
        }catch (Exception e){
            logger.error("获取数据项列表失败" + e.getMessage());
            return MyUtil.response(1, "获取数据项列表失败!");
        }
    }


    @RequestMapping(path = {"/bindvariableandreg"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject bindVariableAndReg(@RequestParam("variable_id")String variableId,
                                     @RequestParam("reg_id")String regId){
        try{
            projectService.bindRegAndVariable(variableId,regId);
            return MyUtil.response(0,"绑定成功");
        }catch (Exception e){
            logger.error("绑定失败（variableAndReg）" + e.getMessage());
            return MyUtil.response(1, "绑定失败!");
        }
    }

    //批量绑定变量组和数据项
    @RequestMapping(path = {"/bindvariableandregbatch"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject bindVariableAndRegBatch(@RequestBody List<Map> list){
        try{
            if(list.size() > 0) {
                for (Map map : list) {
                    String variableId = (String) map.get("variable_id");
                    String regId = (String) map.get("reg_id");
//                    String deviceId = (String) map.get("device_id");
//                    String projectId = (String) map.get("project_id");
                    projectService.bindRegAndVariable(variableId, regId);

                }
                return MyUtil.response(0, "绑定成功");
            }else{
                return MyUtil.response(1, "无数据!");
            }
        }catch (Exception e){
            logger.error("绑定失败（variableAndReg）" + e.getMessage());
            return MyUtil.response(1, "绑定失败!");
        }
    }

    //解绑数据项和变量组
    @RequestMapping(path = {"/unbindvariableandreg"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject unbindVariableAndReg(@RequestParam("id")String id){
        try{
            projectService.unbindRegAndVariable(id);
            return MyUtil.response(0,"解绑成功");
        }catch (Exception e){
            logger.error("解绑失败（variableandreg）" + e.getMessage());
            return MyUtil.response(1, "解绑失败!");
        }
    }

    //批量解绑数据项和变量组
    @RequestMapping(path = {"/unbindvariableandregbatch"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject unbindVariableAndRegBatch(@RequestBody List<Map> list){
        try{
            if(list.size() > 0){
                for(Map map : list){
                    String id = (String) map.get("id");
                    projectService.unbindRegAndVariable(id);
                }
            }
            return MyUtil.response(0,"解绑成功");
        }catch (Exception e){
            logger.error("解绑失败（variableandreg）" + e.getMessage());
            return MyUtil.response(1, "解绑失败!");
        }
    }

    //每个reg对应的历史数据查看
    @RequestMapping(path = {"/historydataforreg"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject historyDataForReg(@RequestParam("reg_id")String regId,
                                        @RequestParam(value = "start_time",defaultValue = "0")String startTime,
                                        @RequestParam(value = "end_time",defaultValue = "4640396560000")String endTime,
                                        @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
                                        @RequestParam(value = "page_size", defaultValue = "20")Integer pageSize){
        try {
            PageInfo<GywlwHistoryItem> pageInfo = projectService.getHistoryDataForReg(regId,
                    startTime,endTime,pageNum,pageSize);
            if (pageInfo == null || pageInfo.getSize() == 0) {
                PageInfo<GywlwHistoryDataForGPIO> listForGpio = projectService.getHistoryDataForGpio(regId, startTime,
                        endTime, pageNum, pageSize);
                if(listForGpio == null){
                    return MyUtil.response(1,"查不到相关数据");
                }
                return MyUtil.response(0, listForGpio);
            }
            return MyUtil.response(0, pageInfo);
        } catch (ParseException e) {
            logger.error("查询数据项历史数据出错" + e.getMessage());
            return MyUtil.response(1, "查询数据项历史数据出错!");
        }
    }



    //趋势参数设置
    @RequestMapping(path = {"/trendpicture"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject trendpicture(@RequestBody ReceiveModel model){
        try {
            projectService.saveTrendInfo(model);
            return MyUtil.response(0, "ok");
        }catch (Exception e){
            logger.error("设置趋势出错" + e.getMessage());
            return MyUtil.response(1, "设置趋势出错!");
        }
    }


    //趋势数据输出
    @RequestMapping(path = {"/getdata"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject dataForTrend(@RequestParam("photo_name")String photoName){
        try {
            List<Map> list = projectService.getDataForTrend(photoName);
            return MyUtil.response(0, list);
        }catch (Exception e){
            logger.error("获取趋势数据失败" + e.getMessage());
            return MyUtil.response(1, "获取趋势数据失败!");
        }
    }



    //趋势数据输出 by page
    @RequestMapping(path = {"/getdatabypage"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject dataForTrendByPage(@RequestParam("photo_name")String photoName,
                                         @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
                                         @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize){
        try {
            PageHelper.startPage(pageNum,pageSize);
            List<Map> list = projectService.getDataForTrend(photoName);
            PageInfo<Map> pageInfo = new PageInfo<>(list);
            return MyUtil.response(0, pageInfo);
        }catch (Exception e){
            logger.error("获取趋势数据失败" + e.getMessage());
            return MyUtil.response(1, "获取趋势数据失败!");
        }
    }



    //该用户下，树形结构列表（device/plc(gpio)/reg)
    @RequestMapping(path = {"/treelistofuser"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject treeListOfUser(){
        try {
            return MyUtil.response(0,projectService.getTreeListOfUser());
        }catch (Exception e){
            logger.error("获取树形结构列表失败(user)" + e.getMessage());
            return MyUtil.response(1,"获取树形结构列表失败(user)");
        }
    }

    //项目下，已经绑定的数据项列表
    @RequestMapping(path = {"/treelistofproject"}, method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject treeListOfProject(@RequestParam("project_id")String projectId){
        try {
            return MyUtil.response(0,projectService.getTreeListOfProject(projectId));
        }catch (Exception e){
            logger.error("获取树形结构列表失败(project)" + e.getMessage());
            return MyUtil.response(1,"获取树形结构列表失败(project)");
        }
    }






}
