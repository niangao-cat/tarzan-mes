package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfMaterialSiteAttr;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料站点扩展表Mapper
 *
 * @author kejin.liu01@hand-china.com 2020-10-20 22:23:48
 */
public interface ItfMaterialSiteAttrMapper extends BaseMapper<ItfMaterialSiteAttr> {

    void batchInsert(@Param(value = "siteAttr") List<ItfMaterialSiteAttr> siteAttr);
}
