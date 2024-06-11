package io.metersphere.plan.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.metersphere.plan.constants.TestPlanResourceConfig;
import io.metersphere.plan.dto.request.*;
import io.metersphere.plan.dto.response.TestPlanApiCasePageResponse;
import io.metersphere.plan.dto.response.TestPlanAssociationResponse;
import io.metersphere.plan.dto.response.TestPlanOperationResponse;
import io.metersphere.plan.service.TestPlanApiCaseLogService;
import io.metersphere.plan.service.TestPlanApiCaseService;
import io.metersphere.plan.service.TestPlanManagementService;
import io.metersphere.plan.service.TestPlanService;
import io.metersphere.sdk.constants.HttpMethodConstants;
import io.metersphere.sdk.constants.PermissionConstants;
import io.metersphere.sdk.dto.api.task.TaskRequestDTO;
import io.metersphere.system.dto.LogInsertModule;
import io.metersphere.system.dto.sdk.BaseTreeNode;
import io.metersphere.system.log.annotation.Log;
import io.metersphere.system.log.constants.OperationLogType;
import io.metersphere.system.security.CheckOwner;
import io.metersphere.system.utils.PageUtils;
import io.metersphere.system.utils.Pager;
import io.metersphere.system.utils.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Tag(name = "测试计划接口用例")
@RestController
@RequestMapping("/test-plan/api/case")
public class TestPlanApiCaseController {

    @Resource
    private TestPlanApiCaseService testPlanApiCaseService;
    @Resource
    private TestPlanManagementService testPlanManagementService;
    @Resource
    private TestPlanService testPlanService;

    @PostMapping(value = "/sort")
    @Operation(summary = "测试计划功能用例-功能用例拖拽排序")
    @RequiresPermissions(PermissionConstants.TEST_PLAN_READ_UPDATE)
    @CheckOwner(resourceId = "#request.getTestPlanId()", resourceType = "test_plan")
    public TestPlanOperationResponse sortNode(@Validated @RequestBody ResourceSortRequest request) {
        testPlanManagementService.checkModuleIsOpen(request.getTestCollectionId(), TestPlanResourceConfig.CHECK_TYPE_TEST_PLAN, Collections.singletonList(TestPlanResourceConfig.CONFIG_TEST_PLAN_FUNCTIONAL_CASE));
        return testPlanApiCaseService.sortNode(request, new LogInsertModule(SessionUtils.getUserId(), "/test-plan/api/case/sort", HttpMethodConstants.POST.name()));
    }

    @PostMapping("/page")
    @Operation(summary = "测试计划-已关联接口用例列表分页查询")
    @RequiresPermissions(PermissionConstants.TEST_PLAN_READ)
    @CheckOwner(resourceId = "#request.getTestPlanId()", resourceType = "test_plan")
    public Pager<List<TestPlanApiCasePageResponse>> page(@Validated @RequestBody TestPlanApiCaseRequest request) {
        Page<Object> page = PageHelper.startPage(request.getCurrent(), request.getPageSize(),
                StringUtils.isNotBlank(request.getSortString("id")) ? request.getSortString("id") : "create_time desc");
        return PageUtils.setPageInfo(page, testPlanApiCaseService.hasRelateApiCaseList(request, false));
    }


    @PostMapping("/module/count")
    @Operation(summary = "测试计划-已关联接口用例模块数量")
    @RequiresPermissions(PermissionConstants.TEST_PLAN_READ)
    @CheckOwner(resourceId = "#request.getTestPlanId()", resourceType = "test_plan")
    public Map<String, Long> moduleCount(@Validated @RequestBody TestPlanApiCaseModuleRequest request) {
        return testPlanApiCaseService.moduleCount(request);
    }

    @PostMapping("/tree")
    @Operation(summary = "测试计划-已关联接口用例列表模块树")
    @RequiresPermissions(PermissionConstants.TEST_PLAN_READ)
    @CheckOwner(resourceId = "#request.getTestPlanId()", resourceType = "test_plan")
    public List<BaseTreeNode> getTree(@Validated @RequestBody TestPlanApiCaseTreeRequest request) {
        return testPlanApiCaseService.getTree(request);
    }

    @PostMapping("/disassociate")
    @Operation(summary = "测试计划-计划详情-接口用例列表-取消关联用例")
    @RequiresPermissions(PermissionConstants.TEST_PLAN_READ_ASSOCIATION)
    @CheckOwner(resourceId = "#request.getTestPlanId()", resourceType = "test_plan")
    public TestPlanAssociationResponse disassociate(@Validated @RequestBody TestPlanDisassociationRequest request) {
        TestPlanApiCaseBatchRequest batchRequest = new TestPlanApiCaseBatchRequest();
        batchRequest.setTestPlanId(request.getTestPlanId());
        batchRequest.setSelectIds(List.of(request.getId()));
        TestPlanAssociationResponse response = testPlanApiCaseService.disassociate(batchRequest, new LogInsertModule(SessionUtils.getUserId(), "/test-plan/api/case/disassociate", HttpMethodConstants.POST.name()));
        testPlanService.refreshTestPlanStatus(request.getTestPlanId());
        return response;
    }


    @PostMapping("/batch/disassociate")
    @Operation(summary = "测试计划-计划详情-列表-批量取消关联用例")
    @RequiresPermissions(PermissionConstants.TEST_PLAN_READ_ASSOCIATION)
    @CheckOwner(resourceId = "#request.getTestPlanId()", resourceType = "test_plan")
    public TestPlanAssociationResponse batchDisassociate(@Validated @RequestBody TestPlanApiCaseBatchRequest request) {
        TestPlanAssociationResponse response = testPlanApiCaseService.disassociate(request, new LogInsertModule(SessionUtils.getUserId(), "/test-plan/api/case/batch/disassociate", HttpMethodConstants.POST.name()));
        testPlanService.refreshTestPlanStatus(request.getTestPlanId());
        return response;
    }


    @PostMapping("/batch/update/executor")
    @Operation(summary = "测试计划-计划详情-接口用例列表-批量更新执行人")
    @RequiresPermissions(PermissionConstants.TEST_PLAN_READ_UPDATE)
    @CheckOwner(resourceId = "#request.getTestPlanId()", resourceType = "test_plan")
    @Log(type = OperationLogType.UPDATE, expression = "#msClass.batchUpdateExecutor(#request)", msClass = TestPlanApiCaseLogService.class)
    public void batchUpdateExecutor(@Validated @RequestBody TestPlanApiCaseUpdateRequest request) {
        testPlanApiCaseService.batchUpdateExecutor(request);
    }

    @GetMapping("/run/{id}")
    @Operation(summary = "用例执行")
    @RequiresPermissions(PermissionConstants.TEST_PLAN_READ_EXECUTE)
//    @CheckOwner(resourceId = "#id", resourceType = "test_plan_api_case") todo
    public TaskRequestDTO run(@PathVariable String id,
                              @Schema(description = "报告ID，传了可以实时获取结果，不传则不支持实时获取")
                              @RequestParam(required = false) String reportId) {
        return testPlanApiCaseService.run(id, reportId, SessionUtils.getUserId());
    }


    //TODO 批量移动 （计划集内）
}
