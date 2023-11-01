package io.metersphere.project.mapper;

import io.metersphere.project.dto.NodeSortQueryParam;
import io.metersphere.system.dto.sdk.BaseModule;
import io.metersphere.system.dto.sdk.BaseTreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtFileModuleMapper {
    List<BaseTreeNode> selectBaseByProjectId(@Param("projectId") String projectId, @Param("moduleType") String moduleType);

    List<BaseTreeNode> selectIdAndParentIdByProjectId(String projectId);

    List<String> selectChildrenIdsByParentIds(@Param("ids") List<String> deleteIds);

    List<String> selectChildrenIdsSortByPos(String parentId);

    void deleteByIds(@Param("ids") List<String> deleteId);

    Long getMaxPosByParentId(String parentId);

    List<String> selectIdsByProjectId(String projectId);

    String selectNameById(String moduleId);

    BaseModule selectBaseModuleById(String dragNodeId);

    BaseModule selectModuleByParentIdAndPosOperator(NodeSortQueryParam nodeSortQueryParam);
}
