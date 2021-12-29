package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeNcDowngradeDTO2;
import com.ruike.hme.api.dto.HmeNcDowngradeDTO3;
import com.ruike.hme.domain.entity.HmeNcDowngrade;
import com.ruike.hme.domain.vo.HmeNcDowngradeVO;
import com.ruike.hme.domain.vo.HmeNcDowngradeVO2;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品降级关系维护Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-05-18 11:35:47
 */
public interface HmeNcDowngradeMapper extends BaseMapper<HmeNcDowngrade> {

    /**
     * 分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/18 03:10:12
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDowngradeVO>
     */
    List<HmeNcDowngradeVO> pageQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeNcDowngradeDTO2 dto);

    /**
     * 历史数据分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/18 03:40:01
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDowngradeVO2>
     */
    List<HmeNcDowngradeVO2> hisPageQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeNcDowngradeDTO3 dto);
}
