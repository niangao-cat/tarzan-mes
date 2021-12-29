package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.SapMaterialVoucherDTO;
import com.ruike.itf.domain.vo.ItfMaterialVoucherVO;
import com.sap.conn.jco.JCoException;

import java.text.ParseException;
import java.util.List;

public interface ItfSapMaterialVoucherService {

    /**
     * 查询SAP物料凭证并且和MES对比差异
     * @param tenantId
     * @param dto
     */
    List<ItfMaterialVoucherVO> materialVoucherList(Long tenantId, SapMaterialVoucherDTO dto) throws JCoException, ParseException;
}
