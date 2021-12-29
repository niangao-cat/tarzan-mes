package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosBarCodeExceptionDTO;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO;
import io.choerodon.core.domain.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS条码加工异常汇总报表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:31
 */
public interface HmeCosBarCodeExceptionMapper {

    /**
     * COS条码加工异常汇总报表查询
     *
     * @param tenantId
     * @param dto
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-26 15:51
     * @return com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO
     */
    List<HmeCosBarCodeExceptionVO> queryList(@Param("tenantId")Long tenantId, @Param("dto")HmeCosBarCodeExceptionDTO dto);

    /**
     * 查询设备
     *
     * @param tenantId
     * @param jobIdList
     * @author penglin.sui@HAND-CHINA.COM 2021-03-29 19:15
     * @return com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO
     */
    List<HmeCosBarCodeExceptionVO> queryEquipmentList(@Param("tenantId")Long tenantId, @Param("jobIdList")List<String> jobIdList);
}
