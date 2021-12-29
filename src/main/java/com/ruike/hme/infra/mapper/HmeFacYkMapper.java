package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeFacYk;
import com.ruike.hme.domain.vo.HmeFacYkHisVO;
import com.ruike.hme.domain.vo.HmeFacYkVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * FAC-Y宽判定标准表Mapper
 *
 * @author TAO.XU02@hand-china.com 2021-02-01 10:45:03
 */
public interface HmeFacYkMapper extends BaseMapper<HmeFacYk> {
    /**
     * FAC-Y宽判定标准表查询
     *
     * @param tenantId
     * @param hmeFacYk
     * @return
     */
    List<HmeFacYkVO> selectHmeFacYk(@Param("hmeFacYk")HmeFacYk hmeFacYk, @Param("tenantId")Long tenantId);

    /**
     * 查询FAC-Y宽判定标准
     *
     * @param tenantId 租户ID
     * @param hmeFacYk FAC-Y宽判定标准
     * @return com.ruike.hme.domain.entity.HmeFacYk
     * @author penglin.sui@hand-china.com 2021/2/4 14:16
     */
    HmeFacYk selectMaxMinValue(@Param("tenantId") Long tenantId, @Param("dto") HmeFacYk hmeFacYk);

    /**
     * FAC-Y宽判定标准历史查询
     *
     * @param tenantId
     * @param facYkId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFacYkHisVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/7 10:48
     */
    List<HmeFacYkHisVO> hisListQuery(@Param("tenantId") Long tenantId, @Param("facYkId") String facYkId);

    /**
     * 批量更新FAC-Y宽判定标准
     *
     * @param tenantId
     * @param userId
     * @param updateList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/4/7 14:44
     */
    void myBatchUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("updateList") List<HmeFacYk> updateList);
}
