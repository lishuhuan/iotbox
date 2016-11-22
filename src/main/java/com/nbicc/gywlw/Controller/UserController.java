package com.nbicc.gywlw.Controller;

import com.nbicc.gywlw.Model.GywlwProject;
import com.nbicc.gywlw.Model.GywlwProjectUserGroup;
import com.nbicc.gywlw.Model.GywlwUser;
import com.nbicc.gywlw.Model.HostHolder;
import com.nbicc.gywlw.Service.ProjectService;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BigMao on 2016/11/19.
 */
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private ProjectService projectService;

    //项目列表
    @RequestMapping(path = {"/user/projectlist"}, method = {RequestMethod.GET})
    @ResponseBody
    public String projectList(@RequestParam(value = "project_status", defaultValue = "0")String projectStatus) {
        try {
            String localUserId = hostHolder.getGywlwUser().getUserId();
            List<GywlwProject> allProject = new ArrayList<>();
            allProject = projectService.projectList(localUserId,0,5,Byte.parseByte(projectStatus));
            String returnString = MyUtil.getJSONString(0, allProject);
            return returnString;
        }catch (Exception e){
            logger.error("获取项目列表失败" + e.getMessage());
            return MyUtil.getJSONString(1, "获取项目列表失败");
        }
    }

    //新增项目
    @RequestMapping(path = {"/user/addproject"}, method = {RequestMethod.POST})
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

    //编辑项目
    @RequestMapping(path = {"/user/editproject"}, method = {RequestMethod.POST})
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

    //停用项目
    @RequestMapping(path = {"/user/stopproject"}, method = {RequestMethod.POST})
    @ResponseBody
    public String stopProject(@RequestParam("project_id") String projectId){
        projectService.stopProject(projectId);
        return MyUtil.getJSONString(0,"停用成功！");
    }


    //获取项目成员列表
    @RequestMapping(path = {"/user/projectmemberlist"}, method = {RequestMethod.GET})
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
    @RequestMapping(path = {"/user/searchuser"}, method = {RequestMethod.GET})
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
    @RequestMapping(path = {"/user/addprojectmember"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addProjectMember(@RequestParam("user_id") String userId,
                                   @RequestParam("write_permission")String writePermission,
                                   @RequestParam("project_id")String projectId){
        try {

            String msg = projectService.addProjectMember(projectId, userId, Byte.parseByte(writePermission));
            return MyUtil.getJSONString(1,msg);
        }catch (Exception e){
            logger.error("添加项目成员失败" + e.getMessage());
            return MyUtil.getJSONString(1, "添加项目成员失败");
        }
    }

    //删除项目成员
  //  @RequestMapping(path = {"/user/addprojectmember"}, method = {RequestMethod.POST})
  //  @ResponseBody


    //修改项目成员权限

    //项目关联变量组列表，并提供变量组搜索

    //变量组中的数据详情

    //告警列表,并提供变量组和日期查询

    //设备列表，唯一识别码查询

    //对设备厂商权限分配

    //绑定设备


}
