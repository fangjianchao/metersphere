package io.metersphere.functional.controller;

import com.alibaba.excel.util.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.metersphere.functional.dto.FunctionalCasePageDTO;
import io.metersphere.functional.request.RelationshipPageRequest;
import io.metersphere.functional.service.FunctionalCaseRelationshipEdgeService;
import io.metersphere.functional.service.FunctionalCaseService;
import io.metersphere.sdk.constants.PermissionConstants;
import io.metersphere.system.security.CheckOwner;
import io.metersphere.system.utils.PageUtils;
import io.metersphere.system.utils.Pager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wx
 */
@Tag(name = "用例管理-功能用例-用例详情-前后置关系")
@RestController
@RequestMapping("/functional/case/relationship")
public class FunctionalCaseRelationshipController {

    @Resource
    private FunctionalCaseService functionalCaseService;
    @Resource
    private FunctionalCaseRelationshipEdgeService functionalCaseRelationshipEdgeService;


    @PostMapping("/relate/page")
    @Operation(summary = "用例管理-功能用例-用例详情-前后置关系-弹窗获取用例列表")
    @RequiresPermissions(PermissionConstants.FUNCTIONAL_CASE_READ)
    @CheckOwner(resourceId = "#request.getProjectId()", resourceType = "project")
    public Pager<List<FunctionalCasePageDTO>> getFunctionalCasePage(@Validated @RequestBody RelationshipPageRequest request) {
        List<String> excludeIds = functionalCaseRelationshipEdgeService.getExcludeIds(request.getId());
        request.setExcludeIds(excludeIds);
        Page<Object> page = PageHelper.startPage(request.getCurrent(), request.getPageSize(),
                StringUtils.isNotBlank(request.getSortString()) ? request.getSortString() : "pos desc");
        return PageUtils.setPageInfo(page, functionalCaseService.getFunctionalCasePage(request, false));
    }


}
