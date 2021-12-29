package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeTagFormulaLine;
import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;
import com.ruike.hme.domain.vo.HmeTagFormulaLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据采集项公式行表Mapper
 *
 * @author guiming.zhou@hand-china.com 2020-09-23 10:04:56
 */
public interface HmeTagFormulaLineMapper extends BaseMapper<HmeTagFormulaLine> {

    /**
     * 获取行信息
     *
     * @param tenantId
     * @param tagFormulaHeadId
     * @author guiming.zhou@hand-china.com 2020/9/25 14:03
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagFormulaLineVO>
     */
    List<HmeTagFormulaLineVO> getTagLineList(@Param("tenantId") Long tenantId, @Param("tagFormulaHeadId") String tagFormulaHeadId);

    /**
     * 删除行信息
     *
     * @param tenantId
     * @param tagFormulaHeadId
     * @author guiming.zhou@hand-china.com 2020/9/25 14:03
     * @return void
     */
    void  deleteByHeadId(@Param("tenantId") Long tenantId, @Param("tagFormulaHeadId") String tagFormulaHeadId);
}
