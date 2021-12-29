package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeMaterialVersion;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料版本表Mapper
 *
 * @author kejin.liu01@hand-china.com 2020-09-28 15:14:48
 */
public interface HmeMaterialVersionMapper extends BaseMapper<HmeMaterialVersion> {

    List<HmeMaterialVersion> selectMaterialVersion(@Param("tenantId") Long tenantId, @Param("materialIds") String materialIds);
}
