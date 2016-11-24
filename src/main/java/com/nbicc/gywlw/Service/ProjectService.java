package com.nbicc.gywlw.Service;

import com.nbicc.gywlw.Model.*;
import com.nbicc.gywlw.mapper.GywlwProjectMapper;
import com.nbicc.gywlw.mapper.GywlwProjectUserGroupMapper;
import com.nbicc.gywlw.mapper.GywlwUserMapper;
import com.nbicc.gywlw.mapper.GywlwVariableRegGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public List<GywlwProject> projectList(String gywlwUserId, int offset, int limit, Byte projectStatus){
        return gywlwProjectMapper.selectByGywlwUserId(gywlwUserId, offset, limit, projectStatus);
    }

    public String editProject(String projectId, String projectName, String parentText, String projectDesc,
                              String longitude, String latitude, String province, String city, String purchaseDate,
                              String completionDate,String installDate, String contactName, String contactPhone,
                              String customerType, String firstParty, String projectType)
    {
        int mark = 0;
        GywlwProject gywlwProject = gywlwProjectMapper.selectByGywlwProjectId(projectId);
        if(gywlwProject == null) {
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

        if(mark == 1){
            gywlwProjectMapper.addProject(gywlwProject);
        } else {
            gywlwProjectMapper.updateByProjectId(gywlwProject);
        }
        return gywlwProject.getProjectId();
    }

    public void stopProject(String gywlwProjectId){
        gywlwProjectMapper.stopByProjectId(gywlwProjectId);
    }

    public List<GywlwProjectUserGroup> projectMemberList(String projectId, int offset, int limit){
        List<GywlwProjectUserGroup> list =  gywlwProjectUserGroupMapper.selectByProjectId(projectId,offset,limit);
        //这里因为两次查询反应速度有点慢，考虑要不要加冗余字段提高速度
        for(GywlwProjectUserGroup group : list){
            group.setUserName(gywlwUserMapper.selectByPrimaryKey(group.getUserId()).getUserName());
        }
        return list;

    }

    public GywlwUser searchUser(String userPhone){
        return gywlwUserMapper.selectByPhone(userPhone);
    }

    public String addProjectMember(String projectId, String userId, Byte writePermission){
        GywlwProjectUserGroup userInProject = gywlwProjectUserGroupMapper.selectByProjectIdAndUserId(projectId,userId);
        if(userInProject != null){
            return "该成员已添加！";
        }else {
            GywlwProjectUserGroup gywlwProjectUserGroup = new GywlwProjectUserGroup();
            gywlwProjectUserGroup.setProjectId(projectId);
            gywlwProjectUserGroup.setUserId(userId);
            gywlwProjectUserGroup.setWritePermission(writePermission);
            gywlwProjectUserGroupMapper.insert(gywlwProjectUserGroup);
            return "添加项目成员成功";
        }
    }

    public void deleteProjectMember(String projectId, String userId){
        gywlwProjectUserGroupMapper.deleteByProjectIdAndUserId(projectId,userId);
    }

    public List<GywlwVariableRegGroup> searchDataInProject(String projectId, String variableName){
        return gywlwVariableRegGroupMapper.selectByProjectIdAndVariableName(projectId,variableName);
    }
}
