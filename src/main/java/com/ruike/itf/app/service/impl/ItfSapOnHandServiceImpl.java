package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfOnHandDTO;
import com.ruike.itf.domain.vo.MesOnHandVO;
import com.ruike.itf.domain.vo.SapOnHandVO;
import com.ruike.itf.app.service.ItfSapOnHandService;
import com.ruike.itf.infra.mapper.ItfMesOnHandMapper;
import com.ruike.itf.infra.util.SAPClientUtils;
import com.sap.conn.jco.*;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItfSapOnHandServiceImpl implements ItfSapOnHandService {

    private final SAPClientUtils sapClientUtils;

    private final ItfMesOnHandMapper itfMesOnHandMapper;

    public ItfSapOnHandServiceImpl(SAPClientUtils sapClientUtils, ItfMesOnHandMapper itfMesOnHandMapper) {
        this.sapClientUtils = sapClientUtils;
        this.itfMesOnHandMapper = itfMesOnHandMapper;
    }

    @Override
    public List<MesOnHandVO> onHandReport(Long tenantId, ItfOnHandDTO para) throws JCoException {
        List<SapOnHandVO> sapOnHands = countSapOnHand(para);
        List<MesOnHandVO> mesOnHandVOS = countMesOnHand(para);
        // 将SAP数据转成MAP
        Map<String, SapOnHandVO> sapOnHandVOMap = sapOnHands.stream().collect(Collectors.toMap(
                dto -> {
                    String materialCode = dto.getMATNR().replaceAll("^(0+)", "");
                    String locatorCode = dto.getLGORT();
                    return materialCode + "-" + locatorCode;
                }, dto -> dto));

        // 返回数据
        List<MesOnHandVO> resultList = new ArrayList<>();
        for (MesOnHandVO mesOnHandVO : mesOnHandVOS) {
            String mapKey = mesOnHandVO.getMaterialCode() + "-" + mesOnHandVO.getLocatorCode();
            SapOnHandVO sapOnHandVO = sapOnHandVOMap.get(mapKey);
            if (Objects.isNull(sapOnHandVO)) {
                mesOnHandVO.setSapCountQty("0");
                resultList.add(mesOnHandVO);
                continue;
            }
            mesOnHandVO.setSapCountQty(sapOnHandVO.getLABST());
            resultList.add(mesOnHandVO);
            sapOnHandVOMap.remove(mapKey);
        }
        // 如果有SAP物料并且在MES不存在则重新加入
        if (sapOnHandVOMap.size() != 0) {
            sapOnHandVOMap.forEach((k, v) -> {
                resultList.add(new MesOnHandVO(v));
            });
        }

        List<MesOnHandVO> newResultList = resultList.stream().filter(
                a -> new BigDecimal(a.getDifference()).signum() != 0).collect(Collectors.toList());
        return newResultList;
    }


    /**
     * create lkj
     * 2020年11月17日13:51:45
     * 根据工厂查询SAP物料现有量
     *
     * @param para
     * @return java.util.List
     * @throws JCoException
     */
    private List<SapOnHandVO> countSapOnHand(ItfOnHandDTO para) throws JCoException {
        // 获取SAP-RFC连接
        JCoDestination destination = sapClientUtils.getNewJcoConnection();
        // 获取接口function
        JCoFunction function = SAPClientUtils.CreateFunction(destination, ZESB_GET_STOCK);
        if (Objects.isNull(function)) {
            throw new CommonException("未获取到SAP FUNCTION:" + ZESB_GET_STOCK);
        }
        // 获取SAP传入参数
        JCoParameterList importParameterList = function.getImportParameterList();
        importParameterList.setValue("GDF_WERKS", para.getSiteCode());
        // Table 传参
        JCoParameterList tablePara = function.getTableParameterList();
        if (Strings.isNotEmpty(para.getMaterialCode())) {
            JCoTable material = tablePara.getTable("GDT_MATNR");
            material.appendRow();
            material.setValue("MATNR", para.getMaterialCode());
        }

        if (Strings.isNotEmpty(para.getLocatorCode())) {
            JCoTable locator = tablePara.getTable("GDT_LGORT");
            locator.appendRow();
            locator.setValue("LGORT", para.getLocatorCode());
        }

        log.info("=============================>" + ZESB_GET_STOCK + " 参数传递：{}", para);
        //执行函数
        function.execute(destination);
        //获取SAP 结构
        JCoParameterList tableParameterList = function.getTableParameterList();
        //获取SAP传出TABLE
        JCoTable gdtOut = tableParameterList.getTable("GDT_OUT");
        if (Objects.isNull(gdtOut)) {
            throw new CommonException("SAP函数 " + ZESB_GET_STOCK + " 的返回值，GDT_OUT为空！");
        }
        if (gdtOut.getNumRows() == 0) {
            throw new CommonException("SAP函数 " + ZESB_GET_STOCK + " GDT_OUT 返回数据长度为零！");
        }
        List<SapOnHandVO> sapOnHands = new ArrayList<>();
        for (int i = 0; i < gdtOut.getNumRows(); i++) {
            gdtOut.setRow(i);//获取行
            SapOnHandVO sapOnHandDTO = new SapOnHandVO(gdtOut);
            sapOnHands.add(sapOnHandDTO);
        }
        Map<String, List<SapOnHandVO>> sapOnHandMap = sapOnHands.stream().collect(
                Collectors.groupingBy(dto -> {
                    String materialCode = dto.getMATNR().replaceAll("^(0+)", "");
                    String locatorCode = dto.getLGORT();
                    return materialCode + "-" + locatorCode;
                }));
        List<SapOnHandVO> countList = new ArrayList<>();
        sapOnHandMap.forEach((k, v) -> {
            SapOnHandVO count = v.remove(0);
            BigDecimal countQty = new BigDecimal(count.getLABST());
            for (SapOnHandVO vo : v) {
                countQty = countQty.add(new BigDecimal(vo.getLABST()));
            }
            count.setLABST(String.valueOf(countQty));
            countList.add(count);
        });
        return countList;
    }

    /**
     * create lkj
     * 2020年11月17日15:47:41
     * 默认根据工厂查询所有的现有量
     *
     * @param dto
     * @return java.util.List
     */
    private List<MesOnHandVO> countMesOnHand(ItfOnHandDTO dto) {
        List<MesOnHandVO> mesOnHands = itfMesOnHandMapper.selectOnHand(dto);

        return mesOnHands;
    }
}
