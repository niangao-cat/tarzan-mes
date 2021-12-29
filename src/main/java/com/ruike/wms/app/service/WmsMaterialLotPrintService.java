package com.ruike.wms.app.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.ruike.wms.api.dto.WmsMaterialLotQryResultDTO;
import com.ruike.wms.domain.vo.WmsMaterialLotPrintVO;
import com.itextpdf.text.DocumentException;
import com.ruike.wms.domain.vo.WmsMaterialLotPrintVO;
import freemarker.template.TemplateException;

public interface WmsMaterialLotPrintService {
    /**
     * 查询条码信息
     *
     * @Description 条码打印方法声明
     * @param tenantId
     * @param materialLotIds
     * @date 2020/07/29 20:11
     * @auther penglin.sui@hand-china.com
     */
    public List<WmsMaterialLotPrintVO> queryPrintMaterialLot(Long tenantId, List<String> materialLotIds);

    /**
     * 条码打印
     *
     * @author penglin.sui@hand-china.com 2020-07-29 21:17
     */
    public String singlePrint(Long tenantId, WmsMaterialLotPrintVO wmsMaterialLotPrintVo) throws Exception;

    public void multiplePrint(Long tenantId, List<String> materialLotIds, HttpServletResponse response) throws Exception;

    void print(Long tenantId, String type, List<WmsMaterialLotQryResultDTO> wmsMaterialLotQryResultDTOList, HttpServletResponse response);
}
