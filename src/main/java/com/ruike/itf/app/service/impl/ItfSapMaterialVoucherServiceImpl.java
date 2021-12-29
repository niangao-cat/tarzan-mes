package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.SapMaterialVoucherDTO;
import com.ruike.itf.app.service.ItfSapMaterialVoucherService;
import com.ruike.itf.domain.vo.ItfMaterialVoucherVO;
import com.ruike.itf.infra.mapper.ItfObjectTransactionIfaceMapper;
import com.ruike.itf.infra.util.SAPClientUtils;
import com.sap.conn.jco.*;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItfSapMaterialVoucherServiceImpl implements ItfSapMaterialVoucherService {

    private final String ZMES_GET_MBLNR = "ZMES_GET_MBLNR";

    private final SAPClientUtils sapClientUtils;
    private final ItfObjectTransactionIfaceMapper itfObjectTransactionIfaceMapper;

    public ItfSapMaterialVoucherServiceImpl(SAPClientUtils sapClientUtils, ItfObjectTransactionIfaceMapper itfObjectTransactionIfaceMapper) {
        this.sapClientUtils = sapClientUtils;
        this.itfObjectTransactionIfaceMapper = itfObjectTransactionIfaceMapper;
    }

    @Override
    public List<ItfMaterialVoucherVO> materialVoucherList(Long tenantId, SapMaterialVoucherDTO dto) throws JCoException, ParseException {
        if (Strings.isEmpty(dto.getSiteCode()) || Strings.isEmpty(dto.getSiteId())
                || Strings.isEmpty(dto.getStartDate()) || Strings.isEmpty(dto.getEndDate())) {
            throw new CommonException("工厂，开始时间，结束时间必输！");
        }

        List<ItfMaterialVoucherVO> sapVouchers = sapMaterialVoucherList(dto);
        List<ItfMaterialVoucherVO> mesVouchers = itfObjectTransactionIfaceMapper.selectMaterialVoucher(dto);
        // 对比结果
        List<ItfMaterialVoucherVO> resultData = sapVoucher(mesVouchers, sapVouchers);

        return resultData;

    }

    private List<ItfMaterialVoucherVO> sapVoucher(List<ItfMaterialVoucherVO> mesVouchers, List<ItfMaterialVoucherVO> sapVouchers) {
        log.info("MES凭证数量：{}", mesVouchers.size());
        log.info("SAP凭证数量：{}", sapVouchers.size());
        Set<String> set = mesVouchers.stream().map(vo -> setValue(vo)).collect(Collectors.toSet());

        List<ItfMaterialVoucherVO> list = new ArrayList<>();
        sapVouchers.forEach(vo -> {
            String setValue = setValue(vo);
            if (set.add(setValue)) {
                list.add(vo);
            }
        });
        log.info("SAP->MES对比后凭证数量：{}", list.size());
        return list;
    }

    /**
     * 4.若移动类型为261，只核对MES接口汇总表中attribute1（凭证号），attribute2（年度）与SAP凭证是否一致
     * 5.若移动类型不为261，需核对接口汇总表中attribute1（凭证号），attribute2（年度），attribute3（凭证行号）与SAP凭证是否一致
     *
     * @param vo
     * @return
     */
    private String setValue(ItfMaterialVoucherVO vo) {
        if ("261".equals(vo.getMoveType())) {
            return vo.getVoucherNo() + "-" + vo.getYears();
        } else {
            return vo.getVoucherNo() + "-" + vo.getYears() + "-" + vo.getVoucherLineNo().replaceAll("^(0+)","");
        }
    }


    /**
     * 查询物料凭证
     *
     * @param para
     * @return
     * @throws JCoException
     */
    private List<ItfMaterialVoucherVO> sapMaterialVoucherList(SapMaterialVoucherDTO para) throws JCoException, ParseException {
        // 获取SAP-RFC连接
        JCoDestination destination = sapClientUtils.getNewJcoConnection();
        // 获取接口function
        JCoFunction function = SAPClientUtils.CreateFunction(destination, ZMES_GET_MBLNR);
        if (Objects.isNull(function)) {
            throw new CommonException("未获取到SAP FUNCTION:" + ZMES_GET_MBLNR);
        }
        // 获取SAP传入参数
        // 函数传参
        JCoParameterList importParameterList = function.getImportParameterList();
        importParameterList.setValue("I_BUDAT_F", parseDate(para.getStartDate()));
        importParameterList.setValue("I_BUDAT_T", parseDate(para.getEndDate()));
        importParameterList.setValue("I_WERKS", para.getSiteCode());
        // Table传参
        JCoParameterList tablePara = function.getTableParameterList();
        if (Strings.isNotEmpty(para.getMaterialCode())) {
            JCoTable material = tablePara.getTable("ET_MATNR");
            material.appendRow();
            material.setValue("MATNR", para.getMaterialCode());
        }
        if (Strings.isNotEmpty(para.getLocatorCode())) {
            JCoTable locator = tablePara.getTable("ET_LGORT");
            locator.appendRow();
            locator.setValue("LGORT", para.getLocatorCode());
        }
        if (Strings.isNotEmpty(para.getMoveType())) {
            JCoTable locator = tablePara.getTable("ET_BWART");
            locator.appendRow();
            locator.setValue("BWART", para.getMoveType());
        }

        log.info("=============================>" + ZMES_GET_MBLNR + " 参数传递：{}", para);
        //执行函数
        function.execute(destination);
        //获取SAP 结构
        JCoParameterList tableParameterList = function.getTableParameterList();
        //获取SAP传出TABLE
        JCoTable result = tableParameterList.getTable("ET_MSEG");
        if (Objects.isNull(result)) {
            throw new CommonException("SAP函数 " + ZMES_GET_MBLNR + " 的返回值，ET_MSEG为空！");
        }
        if (result.getNumRows() == 0) {
            throw new CommonException("SAP函数 " + ZMES_GET_MBLNR + " ET_MSEG 返回数据长度为零！");
        }
        log.info("SAP返回数据条数：{}", result.getNumRows());
        List<ItfMaterialVoucherVO> voucherVOS = new ArrayList<>();
        for (int i = 0; i < result.getNumRows(); i++) {
            result.setRow(i);//获取行
            ItfMaterialVoucherVO itfMaterialVoucherVO = new ItfMaterialVoucherVO(result);
            voucherVOS.add(itfMaterialVoucherVO);
        }
        return voucherVOS;
    }

    private String parseDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = format.parse(date);
        String format1 = format.format(parse);
        String replace = format1.replace("-", "");
        return replace;

    }

}
