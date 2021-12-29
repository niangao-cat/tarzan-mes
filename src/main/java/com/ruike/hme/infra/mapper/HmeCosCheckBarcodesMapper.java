package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosCheckBarcodesDTO;
import com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName HmeCosCheckBarcodesMapper
 * @Description cos目检条码表
 * @Author li.zhang 2021/01/19 12:30
 */
public interface HmeCosCheckBarcodesMapper {

    List<HmeCosCheckBarcodesVO> selectCheckBarcodes(@Param("tenantId") String tenantId, @Param("dto") HmeCosCheckBarcodesDTO dto);

    List<HmeCosCheckBarcodesVO> selectCheckBarcodes2(@Param("tenantId") String tenantId, @Param("dto") HmeCosCheckBarcodesDTO dto);
}
