package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.ItfModLocatorIfaceDTO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.api.dto.MtModLocatorDTO6;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.vo.*;

import java.util.List;

/**
 * 库位Mapper
 *
 * @author taowen.wang@hand-china.com 2021-07-1 18:20:58
 */
public interface ModLocatorIfaceMapper extends BaseMapper<ItfModLocatorIfaceDTO> {

    String selectByParentLocatorId(String locatorId);

    List<String> selectByLocatorId(@Param("parentLocatorId") String parentLocatorId,@Param(value = "locatorType") String locatorType);
}
