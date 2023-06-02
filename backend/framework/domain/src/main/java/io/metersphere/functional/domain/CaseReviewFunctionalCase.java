package io.metersphere.functional.domain;

import io.metersphere.validation.groups.Created;
import io.metersphere.validation.groups.Updated;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;

@Data
public class CaseReviewFunctionalCase implements Serializable {
    @Schema(title = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{case_review_functional_case.id.not_blank}", groups = {Updated.class})
    @Size(min = 1, max = 50, message = "{case_review_functional_case.id.length_range}", groups = {Created.class, Updated.class})
    private String id;

    @Schema(title = "评审ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{case_review_functional_case.review_id.not_blank}", groups = {Created.class})
    @Size(min = 1, max = 50, message = "{case_review_functional_case.review_id.length_range}", groups = {Created.class, Updated.class})
    private String reviewId;

    @Schema(title = "用例ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{case_review_functional_case.case_id.not_blank}", groups = {Created.class})
    @Size(min = 1, max = 50, message = "{case_review_functional_case.case_id.length_range}", groups = {Created.class, Updated.class})
    private String caseId;

    @Schema(title = "评审状态：进行中/通过/不通过/重新提审", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{case_review_functional_case.status.not_blank}", groups = {Created.class})
    @Size(min = 1, max = 64, message = "{case_review_functional_case.status.length_range}", groups = {Created.class, Updated.class})
    private String status;

    @Schema(title = "创建时间")
    private Long createTime;

    @Schema(title = "更新时间")
    private Long updateTime;

    @Schema(title = "创建人")
    private String createUser;

    @Schema(title = "自定义排序，间隔5000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{case_review_functional_case.pos.not_blank}", groups = {Created.class})
    private Long pos;

    @Schema(title = "关联的用例是否放入回收站", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{case_review_functional_case.deleted.not_blank}", groups = {Created.class})
    private Boolean deleted;

    private static final long serialVersionUID = 1L;
}