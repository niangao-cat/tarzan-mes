package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;
import com.ruike.hme.domain.vo.HmeTagFormulaLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeTagFormulaLine;

/**
 * 数据采集项公式行表资源库
 *
 * @author guiming.zhou@hand-china.com 2020-09-23 10:04:56
 */
public interface HmeTagFormulaLineRepository extends BaseRepository<HmeTagFormulaLine> {

    /**
     * 获取行数据
     *
     * @param tenantId
     * @param tagFormulaHeadId
     * @param pageRequest
     * @author guiming.zhou@hand-china.com 2020/9/25 14:00
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeTagFormulaLineVO>
     */
    Page<HmeTagFormulaLineVO> getLineList(Long tenantId, String tagFormulaHeadId, PageRequest pageRequest);

    /**
     * 删除行数据
     *
     * @param tenantId
     * @param tagFormulaHeadId
     * @author guiming.zhou@hand-china.com 2020/9/25 14:00
     * @return void
     */
    void deleteByHeadId(Long tenantId, String tagFormulaHeadId);
}
