package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;
import com.ruike.hme.domain.vo.HmeTagFormulaLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 数据采集项公式行表应用服务
 *
 * @author guiming.zhou@hand-china.com 2020-09-23 10:04:56
 */
public interface HmeTagFormulaLineService {

    /**
     * 获取行数据
     *
     * @param tenantId
     * @param tagFormulaHeadId
     * @param pageRequest
     * @author guiming.zhou@hand-china.com 2020/9/25 14:01
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeTagFormulaLineVO>
     */
    Page<HmeTagFormulaLineVO> getTagLineList(Long tenantId, String tagFormulaHeadId, PageRequest pageRequest);

    /**
     * 删除行信息
     *
     * @param tenantId
     * @param tagFormulaHeadId
     * @author guiming.zhou@hand-china.com 2020/9/25 14:01
     * @return void
     */
    void deleteHmeTagFormulaLine(Long tenantId, String tagFormulaHeadId);

}
