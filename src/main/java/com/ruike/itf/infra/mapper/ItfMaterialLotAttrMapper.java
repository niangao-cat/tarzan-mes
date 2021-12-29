package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfMaterialLotAttr;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料批扩展表Mapper
 *
 * @author kejin.liu01@hand-china.com 2020-10-26 21:27:26
 */
public interface ItfMaterialLotAttrMapper extends BaseMapper<ItfMaterialLotAttr> {

    void deleteByMaterialLotId(@Param("materialIds") List<String> materialIds);
}
