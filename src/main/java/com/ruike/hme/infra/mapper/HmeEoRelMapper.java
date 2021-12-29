package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeEoRel;
import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * EO返修记录关系表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2021-03-22 10:14:00
 */
public interface HmeEoRelMapper extends BaseMapper<HmeEoRel> {

    /**
     * 旧条码返修标识
     *
     * @param tenantId
     * @param eoId
     * @author sanfeng.zhang@hand-china.com 2021/9/14 19:22
     * @return java.util.List<io.tarzan.common.domain.vo.MtExtendAttrVO>
     */
    List<MtExtendAttrVO> queryOldCodeAttrList(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);
}
