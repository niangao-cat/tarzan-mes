package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfOnHandDTO;
import com.ruike.itf.domain.vo.MesOnHandVO;
import com.sap.conn.jco.JCoException;

import java.util.List;

public interface ItfSapOnHandService {

    String ZESB_GET_STOCK = "ZESB_GET_STOCK";

    List<MesOnHandVO> onHandReport(Long tenantId, ItfOnHandDTO dto) throws JCoException;

}
