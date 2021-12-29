package tarzan.actual.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.actual.api.dto.MtEoStepWipDTO;
import tarzan.actual.api.dto.MtEoStepWipDTO2;

/**
 * 执行作业在制品应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:30
 */
public interface MtEoStepWipService {

    /**
     * UI在制品报表查询
     *
     * @Author Xie.yiyang
     * @Date  2020/1/7 16:13
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<tarzan.actual.api.dto.MtEoStepWipDTO2>
     */
    Page<MtEoStepWipDTO2> eoStepWipReportForUi(Long tenantId, MtEoStepWipDTO dto, PageRequest pageRequest);
}
