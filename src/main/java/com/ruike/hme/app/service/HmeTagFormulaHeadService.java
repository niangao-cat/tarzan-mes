package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;
import com.ruike.hme.domain.vo.HmeTagFormulaVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 数据采集项公式头表应用服务
 *
 * @author guiming.zhou@hand-china.com 2020-09-21 19:50:40
 */
public interface HmeTagFormulaHeadService {

    /**
     * 查询公式头
     *
     * @param tenantId
     * @param vo
     * @param pageRequest
     * @author guiming.zhou@hand-china.com 2020/9/25 13:53
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeTagFormulaHeadVO>
     */
   Page<HmeTagFormulaHeadVO> getTagHeadList(Long tenantId, HmeTagFormulaVO vo, PageRequest pageRequest);

   /**
    * "删除数据采集项公式头表-自定义
    *
    * @param tenantId
    * @param tagFormulaHeadId
    * @author guiming.zhou@hand-china.com 2020/9/25 13:54
    * @return void
    */
    void deleteHmeTagFormulaHead(Long tenantId, String tagFormulaHeadId);
}
