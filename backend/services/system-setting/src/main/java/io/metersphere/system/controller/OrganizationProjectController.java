package io.metersphere.system.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.metersphere.project.domain.Project;
import io.metersphere.sdk.constants.PermissionConstants;
import io.metersphere.sdk.dto.AddProjectRequest;
import io.metersphere.sdk.dto.ProjectDTO;
import io.metersphere.sdk.dto.ProjectExtendDTO;
import io.metersphere.sdk.dto.UpdateProjectRequest;
import io.metersphere.sdk.log.annotation.Log;
import io.metersphere.sdk.log.constants.OperationLogType;
import io.metersphere.sdk.util.PageUtils;
import io.metersphere.sdk.util.Pager;
import io.metersphere.sdk.util.SessionUtils;
import io.metersphere.system.domain.User;
import io.metersphere.system.dto.UserExtend;
import io.metersphere.system.request.OrganizationProjectRequest;
import io.metersphere.system.request.ProjectAddMemberBatchRequest;
import io.metersphere.system.request.ProjectAddMemberRequest;
import io.metersphere.system.request.ProjectMemberRequest;
import io.metersphere.system.service.OrganizationProjectLogService;
import io.metersphere.system.service.OrganizationProjectService;
import io.metersphere.system.service.UserService;
import io.metersphere.validation.groups.Created;
import io.metersphere.validation.groups.Updated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "系统设置-组织级项目")
@RequestMapping("/organization/project")
public class OrganizationProjectController {
    @Resource
    private OrganizationProjectService organizationProjectService;
    @Resource
    private UserService userService;

    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.ORGANIZATION_PROJECT_READ_ADD)
    @Log(type = OperationLogType.ADD, expression = "#msClass.addLog(#project)", msClass = OrganizationProjectLogService.class)
    @Operation(summary = "添加项目")
    public ProjectExtendDTO addProject(@RequestBody @Validated({Created.class}) AddProjectRequest project) {
        return organizationProjectService.add(project, SessionUtils.getUserId());
    }


    @GetMapping("/get/{id}")
    @Operation(summary = "根据ID获取项目信息")
    @Parameter(name = "id", description = "项目id", schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED))
    @RequiresPermissions(PermissionConstants.ORGANIZATION_PROJECT_READ)
    public Project getProject(@PathVariable @NotBlank String id) {
        return organizationProjectService.get(id);
    }

    @PostMapping("/page")
    @RequiresPermissions(PermissionConstants.ORGANIZATION_PROJECT_READ)
    @Operation(summary = "获取项目列表")
    public Pager<List<ProjectDTO>> getProjectList(@Validated @RequestBody OrganizationProjectRequest request) {
        Page<Object> page = PageHelper.startPage(request.getCurrent(), request.getPageSize(),
                StringUtils.isNotBlank(request.getSortString()) ? request.getSortString() : "create_time desc");
        return PageUtils.setPageInfo(page, organizationProjectService.getProjectList(request));
    }

    @PostMapping("/update")
    @Log(type = OperationLogType.UPDATE, expression = "#msClass.updateLog(#project)", msClass = OrganizationProjectLogService.class)
    @Operation(summary = "更新项目信息")
    @RequiresPermissions(PermissionConstants.ORGANIZATION_PROJECT_READ_UPDATE)
    public ProjectExtendDTO updateProject(@RequestBody @Validated({Updated.class}) UpdateProjectRequest project) {
        return organizationProjectService.update(project, SessionUtils.getUserId());
    }

    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.ORGANIZATION_PROJECT_READ_DELETE)
    @Operation(summary = "删除项目")
    @Parameter(name = "id", description = "项目", schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED))
    @Log(type = OperationLogType.DELETE, expression = "#msClass.deleteLog(#id)", msClass = OrganizationProjectLogService.class)
    public int deleteProject(@PathVariable String id) {
        return organizationProjectService.delete(id, SessionUtils.getUserId());
    }

    @GetMapping("/revoke/{id}")
    @RequiresPermissions(PermissionConstants.ORGANIZATION_PROJECT_READ_RECOVER)
    @Operation(summary = "恢复项目")
    @Log(type = OperationLogType.UPDATE, expression = "#msClass.recoverLog(#id)", msClass = OrganizationProjectLogService.class)
    @Parameter(name = "id", description = "项目", schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED))
    public int revokeProject(@PathVariable String id) {
        return organizationProjectService.revoke(id);
    }

    @GetMapping("/enable/{id}")
    @Operation(summary = "启用项目")
    @Parameter(name = "id", description = "项目ID", schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED))
    @Log(type = OperationLogType.UPDATE, expression = "#msClass.updateLog(#id)", msClass = OrganizationProjectLogService.class)
    @RequiresPermissions(PermissionConstants.ORGANIZATION_PROJECT_READ_UPDATE)
    public void enable(@PathVariable String id) {
        organizationProjectService.enable(id);
    }

    @GetMapping("/disable/{id}")
    @Operation(summary = "禁用项目")
    @Parameter(name = "id", description = "项目ID", schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED))
    @RequiresPermissions(PermissionConstants.ORGANIZATION_PROJECT_READ_UPDATE)
    @Log(type = OperationLogType.UPDATE, expression = "#msClass.updateLog(#id)", msClass = OrganizationProjectLogService.class)
    public void disable(@PathVariable String id) {
        organizationProjectService.disable(id);
    }

    @PostMapping("/member-list")
    @RequiresPermissions(PermissionConstants.ORGANIZATION_PROJECT_READ)
    @Operation(summary = "获取项目下成员列表")
    public Pager<List<UserExtend>> getProjectMember(@Validated @RequestBody ProjectMemberRequest request) {
        Page<Object> page = PageHelper.startPage(request.getCurrent(), request.getPageSize(),
                StringUtils.isNotBlank(request.getSortString()) ? request.getSortString() : "create_time desc");
        return PageUtils.setPageInfo(page, organizationProjectService.getProjectMember(request));
    }

    @PostMapping("/add-members")
    @RequiresPermissions(PermissionConstants.ORGANIZATION_PROJECT_MEMBER_ADD)
    @Operation(summary = "添加项目成员")
    public void addProjectMember(@Validated @RequestBody ProjectAddMemberRequest request) {
        ProjectAddMemberBatchRequest batchRequest = new ProjectAddMemberBatchRequest();
        batchRequest.setProjectIds(List.of(request.getProjectId()));
        batchRequest.setUserIds(request.getUserIds());
        organizationProjectService.addProjectMember(batchRequest, SessionUtils.getUserId());
    }

    @GetMapping("/remove-member/{projectId}/{userId}")
    @Operation(summary = "移除项目成员")
    @Parameter(name = "userId", description = "用户id", schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED))
    @Parameter(name = "projectId", description = "项目id", schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED))
    @RequiresPermissions(PermissionConstants.ORGANIZATION_PROJECT_MEMBER_DELETE)
    @Log(type = OperationLogType.DELETE, expression = "#msClass.deleteLog(#projectId)", msClass = OrganizationProjectLogService.class)
    public int removeProjectMember(@PathVariable String projectId, @PathVariable String userId) {
        return organizationProjectService.removeProjectMember(projectId, userId, SessionUtils.getUserId());
    }

    @GetMapping("/user-list")
    @Operation(summary = "获取用户列表")
    @RequiresPermissions(PermissionConstants.ORGANIZATION_PROJECT_READ)
    public List<User> getUserList() {
        return userService.getUserList();
    }

}
