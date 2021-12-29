package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeQualificationDTO;
import com.ruike.hme.domain.entity.HmeQualification;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资质基础信息表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-06-15 10:32:20
 */
public interface HmeQualificationMapper extends BaseMapper<HmeQualification> {

}
