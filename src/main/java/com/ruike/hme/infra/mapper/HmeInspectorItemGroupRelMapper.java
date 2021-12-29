package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeInspectorItemGroupRelDTO;
import com.ruike.hme.domain.entity.HmeInspectorItemGroupRel;
import com.ruike.hme.domain.vo.HmeInspectorItemGroupRelVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检验员与物料组关系表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-03-29 13:44:29
 */
public interface HmeInspectorItemGroupRelMapper extends BaseMapper<HmeInspectorItemGroupRel> {

    /**
     * 分页查询检验员与物料组关系
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/29 02:18:28
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInspectorItemGroupRelVO>
     */
    List<HmeInspectorItemGroupRelVO> relPageQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeInspectorItemGroupRelDTO dto);

    /**
     * 根据登录名查询用户ID
     *
     * @param loginName
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/30 04:03:15
     * @return java.lang.Long
     */
    Long getUserIdByLoginName(@Param("loginName") String loginName);
}
