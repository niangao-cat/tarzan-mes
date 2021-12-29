package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.SitePlantReleation;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ERP工厂与站点映射关系Mapper
 *
 * @author taowen.wang@hand-china.com 2021-07-06 14:14:34
 */
public interface SitePlantReleationMapper extends BaseMapper<SitePlantReleation> {

    String selectByPlantCode(String plantCode);

    List<String> selectByLoctorType();

    List<String> selectByLoctorId(@Param("locatorId") List<String> locatorId);

}
