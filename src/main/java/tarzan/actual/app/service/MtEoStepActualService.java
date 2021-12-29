package tarzan.actual.app.service;

import java.util.List;

import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.order.api.dto.MtEoRouterDTO7;

/**
 * 执行作业-工艺路线步骤执行实绩应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoStepActualService {
    /**
     * eoStepActualListForUi-前台获取EO步骤实绩信息
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/12/24
     */
    List<MtEoStepActual> eoStepActualListForUi(Long tenantId, MtEoRouterDTO7 dto);
}
