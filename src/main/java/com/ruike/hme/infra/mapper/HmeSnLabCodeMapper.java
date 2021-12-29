package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeSnLabCode;
import com.ruike.hme.domain.vo.HmeSnLabCodeVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SN工艺实验代码表Mapper
 *
 * @author penglin.sui@hand-china.com 2021-03-19 16:01:36
 */
public interface HmeSnLabCodeMapper extends BaseMapper<HmeSnLabCode> {
    /**
     * SN工艺实验代码表 查询
     *
     * @param tenantId
     * @param hmeSnLabCode
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-22 14:57
     * @return List<HmeSnLabCodeVO>
     */
    List<HmeSnLabCodeVO> listByMaterialLotId(@Param("tenantId") Long tenantId, @Param("hmeSnLabCode") HmeSnLabCode hmeSnLabCode);
}
