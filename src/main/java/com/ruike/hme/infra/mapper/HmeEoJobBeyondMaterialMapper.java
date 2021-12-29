package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.domain.entity.HmeEoJobBeyondMaterial;
import com.ruike.hme.domain.vo.HmeEoJobBeyondMaterialVO;
import io.choerodon.mybatis.common.BaseMapper;

import org.apache.ibatis.annotations.Param;

/**
 * 工序作业平台-计划外物料Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-07-15 15:27:06
 */
public interface HmeEoJobBeyondMaterialMapper extends BaseMapper<HmeEoJobBeyondMaterial> {

    List<HmeEoJobBeyondMaterial> selectByWkc(@Param("tenantId") Long tenantId,
                                             @Param("dto") HmeEoJobBeyondMaterialVO dto);
}
