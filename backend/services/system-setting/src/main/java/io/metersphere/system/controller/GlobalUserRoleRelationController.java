package io.metersphere.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.metersphere.sdk.constants.PermissionConstants;
import io.metersphere.sdk.dto.UserRoleRelationUserDTO;
import io.metersphere.sdk.dto.request.GlobalUserRoleRelationUpdateRequest;
import io.metersphere.sdk.log.annotation.Log;
import io.metersphere.sdk.log.constants.OperationLogType;
import io.metersphere.sdk.util.PageUtils;
import io.metersphere.sdk.util.Pager;
import io.metersphere.sdk.util.SessionUtils;
import io.metersphere.system.dto.request.GlobalUserRoleRelationQueryRequest;
import io.metersphere.system.dto.request.user.UserAndRoleBatchRequest;
import io.metersphere.system.service.GlobalUserRoleRelationLogService;
import io.metersphere.system.service.GlobalUserRoleRelationService;
import io.metersphere.validation.groups.Created;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : jianxing
 * @date : 2023-6-12
 */
@RestController
@Tag(name = "全局用户组与用户的关联关系")
@RequestMapping("/user/role/relation/global")
public class GlobalUserRoleRelationController {

    @Resource
    private GlobalUserRoleRelationService globalUserRoleRelationService;

    @PostMapping("/list")
    @Operation(summary = "获取全局用户组对应的用户列表")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_ROLE_READ)
    public Pager<List<UserRoleRelationUserDTO>> list(@Validated @RequestBody GlobalUserRoleRelationQueryRequest request) {
        Page<Object> page = PageHelper.startPage(request.getCurrent(), request.getPageSize(), true);
        return PageUtils.setPageInfo(page, globalUserRoleRelationService.list(request));
    }

    @PostMapping("/add")
    @Operation(summary = "创建全局用户组和用户的关联关系")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_ROLE_UPDATE)
    @Log(type = OperationLogType.ADD, expression = "#msClass.addLog(#request)", msClass = GlobalUserRoleRelationLogService.class)
    public void add(@Validated({Created.class}) @RequestBody GlobalUserRoleRelationUpdateRequest request) {
        request.setCreateUser(SessionUtils.getUserId());
        globalUserRoleRelationService.add(request);
    }

    //用户管理页面，批量添加用户到多个用户组。 权限所属是用户管理的编辑页面权限
    @PostMapping("/add/batch/user-role")
    @Operation(summary = "批量添加用户到多个用户组中")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_READ_UPDATE)
    @Log(type = OperationLogType.ADD, expression = "#msClass.batchAddLog(#request)", msClass = GlobalUserRoleRelationLogService.class)
    public void batchAdd(@Validated({Created.class}) @RequestBody UserAndRoleBatchRequest request) {
        globalUserRoleRelationService.batchAdd(request, SessionUtils.getUserId());
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "删除全局用户组和用户的关联关系")
    @RequiresPermissions(PermissionConstants.SYSTEM_USER_ROLE_UPDATE)
    @Log(type = OperationLogType.DELETE, expression = "#msClass.deleteLog(#id)", msClass = GlobalUserRoleRelationLogService.class)
    public void delete(@PathVariable String id) {
        globalUserRoleRelationService.delete(id);
    }
}
