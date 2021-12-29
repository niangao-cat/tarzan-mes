package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosScrap;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报废记录表Mapper
 *
 * @author yuchao.wang@hand-china.com 2020-10-26 20:09:44
 */
public interface HmeCosScrapMapper extends BaseMapper<HmeCosScrap> {

    /**
     * 根据作业类型为'NC_RECORD'和条码Id查询未出站数据
     *
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/9 21:37:01
     * @return java.util.List<java.lang.String>
     */
    List<String> noSiteOutQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);
}
