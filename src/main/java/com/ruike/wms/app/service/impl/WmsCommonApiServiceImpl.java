package com.ruike.wms.app.service.impl;


import com.ruike.wms.domain.vo.WmsMaterialLotAttrViewVO;
import com.ruike.wms.domain.vo.WmsMaterialLotPntVO;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname CommonApiServiceImpl
 * @Description 公共api方法
 * @Date 2019/11/21 10:56 上午
 * @Created by selino
 * @Author selino
 */
@Service
@Slf4j
public class WmsCommonApiServiceImpl implements WmsCommonApiService {
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private WmsCommonServiceComponent wmsCommonServiceComponent;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    public List<LovValueDTO> queryLovValueList(Long tenantId, String lovCode, String value) {
        if (StringUtils.isEmpty(lovCode)) {
            throw new MtException("值集编码为空,请核查");
        }
        List<LovValueDTO> list = lovAdapter.queryLovValue(lovCode, tenantId);

        if (StringUtils.isNotEmpty(value)) {
            if (CollectionUtils.isNotEmpty(list)) {
                for (LovValueDTO dto : list) {
                    if (value.equalsIgnoreCase(dto.getValue())) {
                        return new ArrayList<LovValueDTO>() {{
                            add(dto);
                        }};
                    }
                }
            }
        }
        return list;
    }

    @Override
    public String getProfileName(Long tenantId, String profileName) {
        if (StringUtils.isEmpty(profileName)) {
            throw new MtException("配置编码为空,请核查");
        }
        String result = wmsCommonServiceComponent.getProfileValue(tenantId, profileName);
        if (StringUtils.isEmpty(result)) {
            return WmsConstant.CONSTANT_N;
        }

        return result;
    }

    /**
     * 调用斑马ZR328打印机公共方法
     *
     * @param tenantId
     * @param materialLotList
     * @throws IOException
     */
    @Override
    public String commonPrint(Long tenantId, List<WmsMaterialLotPntVO> materialLotList) throws IOException {
        // 1 创建快递点
        Socket socket = null;
        OutputStream os = null;
        //InputStream is = null;
        try {
            socket = new Socket("10.30.50.140", 9100);
            //FileInputStream fis = null;
            if (CollectionUtils.isEmpty(materialLotList)) {
                return null;
            }
            for (WmsMaterialLotPntVO vo : materialLotList) {
                // 2 准备要发送的数据
                StringBuilder sbInput = new StringBuilder();
                sbInput.append("^XA~TA000~JSN^LT64^MNN^MTD^PON^PMN^LH0,0^JMA^PR5,5~SD10^JUS^LRN^CI27^PA0,1,1,0^XZ^XA^MMT^PW480^LL320^LS0");
                sbInput.append("^FO9,11^GFA,1409,17284,58,:Z64:eJzt2kFrG0cYBuBvdrfepVmk0U0EE02iH+A17kGQgtfF0EsuhRx6VGrotT2GUvCsVGwdQunRvyDnQP/AOgl2DqG99rjNoYSe5EOp2yqarqQgcLPfWnodKjaZ8cGy4eHdb2Z2PZ4dY8A2QuFq5JDmzU2ptN2/8NPHVlr5HkoHk47SLglYkgakR8nkHk+Xl6HqEwXI1Qpz5niBBGSg+i+CUAGyScm9wBhAdlS/HfTagFQq2ZReA6tz8+joGiIpacjDPwB5zfwTPRpsQTNeNMA6qddUD25rQG4+pu7hGZKZfUHdgYvMW/3RvpncoMv3La3nt+eN6jzBrLTyCnKJ1eV5lWW1RsVKK6sp60bPbj1gfRuDmQFpHYCZOyQhGVIK1umb1wKpE5SUpxpQUopltk6HoIwGCuxbuSsJzHyuCJtDk0xsDuV1UgTJvG/9DMvM6yRIzpuVVr4HslrrW1xWa1SstPLqUsDS1fn3SCHSpP6IELlGh8NgwwB7b8Ilh2Iks/kT9YJTJFPeF06wi+y90ch1gt+Q/T7fF8PGp9CeceebtjlBMkn6tAbVWTe/Kwe6WpKCHCjT2ept/H3iAuO51jgmD7raZrSXOV8ifRvJhvSgOkXmqNHPyFuSaYupIk8wK628mlxixXixvbtnIi62ZfrWSiutlJisGx1h0qN4fwTJkNJ0of2+gswF94zfkAH1VBeSvnkK9lBI+jom8xbCcrH92zdk6/TP1INk9FylISTlrozBzIFa7L3D28yE62ydDsF7Ba9z3qy0sjJyFavUVcjSHhLZ/KOvl+pbK618B2XJnvEl8tYtdj2Uy+YBLxufELfWzKX0hBky8sOT8kx3uuQurNMfzn5bnPmdmF5Rkdx+yb63z+X4oE1cpu8pKsu8x9ZZf7DBnWyY1Dl+MtlxL+7bu3Fp5uxj8XiW1XnjoG7GjJz878BnRh6fGTrseZNc/nIgNCe309hjVm+z8WT7Nthjz/JM55Bix1O+L");
                sbInput.append("pPLlOwcCvv9ae8xmQ8VO56+M1tSM5lPiZfff1Uyh77uZ62Mkeufs+dNLnkmqE4Aynmz0sr/Wa5ilboKWa1RsdLKlUp1idSakxGV/M2mVhqzmR2qkSg8n5DL7az2jMtsmaEjRoXvlieZKX3GZUZ6y4xUj5FuohNu3dfIvzxWen79Ly7TNYkOWVkfUz9gpC8EeTEn1yOxx8mg51J4zkl6UfuRk/JOnnlceFYgl8KVO9y6j8bCvHpGzHg6ItFHjNwXZ/TBHU7WzTH9wGWSSzTgpFIb548Y6ZuEaOcVN57Kk11GhtQryay1a52Mkd7kIPa33LyVNw+Jy1ybZgq2zm7G15n/GxS3mMyd9Oav7Do+L3LamGdC7XrIyHljZOh5oJw1K618+3IVq9RVyP92yuLtXzM9mRE=:6306");
                sbInput.append("^FPH,1^FT342,291^A@N,20,20,msyhbd.TTF^FH\\\\^CI28^FD").append(vo.getExpansionCoefficients()).append("^FS^CI27");
                //批次
                sbInput.append("^FPH,1^FT342,242^A@N,18,18,msyhbd.TTF^FH\\\\^CI28^FD").append(vo.getLot()).append("^FS^CI27");
                //客户
                sbInput.append("^FPH,1^FT116,293^A@N,16,16,msyhbd.TTF^FH\\\\^CI28^FD").append("客户").append("^FS^CI27");
                sbInput.append("^FPH,1^FT116,244^A@N,13,13,msyhbd.TTF^FH\\\\^CI28^FD").append("数量").append("^FS^CI27");
                sbInput.append("^FPH,1^FT116,193^A@N,10,10,msyhbd.TTF^FH\\\\^CI28^FD").append("客户码").append("^FS^CI27");
                sbInput.append("^FPH,1^FT116,141^A@N,8,8,msyhbd.TTF^FH\\\\^CI28^FD").append(vo.getMaterialLotCode()).append("^FS^CI27");
                sbInput.append("^FPH,1^FT116,95^A@N,28,28,msyhbd.TTF^FH\\\\^CI28^FD").append(vo.getMaterialCode()).append("^FS^CI27");
                //供应商
                sbInput.append("^FPH,1^FT116,45^A@N,28,28,msyhbd.TTF^FH\\\\^CI28^FD").append(vo.getSupplierName()).append("^FS^CI27");
                sbInput.append("^FT371,138^BQN,2,4^FH\\\\^FDMA,QWERTYUIO^FS^PQ1,0,1,Y^XZ");

                Scanner scanner = new Scanner(sbInput.toString());
                // 3 获得快递员
                os = socket.getOutputStream();
                // is = socket.getInputStream();
                String data = scanner.next();
                Boolean flag = true;
                while (flag) {
                    //String data = scanner.next();
                    os.write(data.getBytes());
                    os.flush();
                    if ("over".equals(data)) {
                        break;
                    }
                    flag = false;
                    // 收到回信
      /*          byte[] bs = new byte[10240];
                int num = is.read(bs);
                String reault = new String(bs, 0, num);
                System.out.println("服务器回复的数据是  : " + reault);
                if ("over".equals(reault)) {
                    break;
                }*/
                }
            }

        } catch (UnknownHostException e) {
            log.error("<==== MaterialLotServiceImpl print: {}", e.getMessage());
            throw e;
        } catch (IOException e) {
            log.error("<==== MaterialLotServiceImpl print: {}", e.getMessage());
            throw e;
        } finally {
            socket.close();
            os.close();
            // Util.closed(null, socket, os);
        }
        return "success";
    }

    /**
     * 条码（物料批）扩展属性行转列方法
     *
     * @param tenantId
     * @param mateiralLotIds
     * @author chuang.yang
     * @date 2020/4/22
     */
    @Override
    public List<WmsMaterialLotAttrViewVO> queryMaterialLotAttrViewData(Long tenantId, List<String> mateiralLotIds) {
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setKeyIdList(mateiralLotIds);
        mtExtendVO1.setTableName("mt_material_lot_attr");
        List<MtExtendAttrVO1> materialLotAttrList =
                mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        if (CollectionUtils.isEmpty(materialLotAttrList)) {
            return new ArrayList<>();
        }
        Map<String, List<MtExtendAttrVO1>> materialLotAttrListMap =
                materialLotAttrList.stream().collect(Collectors.groupingBy(MtExtendAttrVO1::getKeyId));

        List<WmsMaterialLotAttrViewVO> resultList = new ArrayList<>(materialLotAttrList.size());
        for (Map.Entry<String, List<MtExtendAttrVO1>> entry : materialLotAttrListMap.entrySet()) {
            WmsMaterialLotAttrViewVO result = new WmsMaterialLotAttrViewVO();
            result.setTenantId(tenantId);
            result.setMaterialLotId(entry.getKey());
            for (MtExtendAttrVO1 materialLotAttr : entry.getValue()) {
                switch (materialLotAttr.getAttrName()) {
                    case "STATUS":
                        result.setStatus(materialLotAttr.getAttrValue());
                        break;
                    case "GRADE_CODE":
                        result.setGradeCode(materialLotAttr.getAttrValue());
                        break;
                    case "ORIGINAL_ID":
                        result.setOriginalId(materialLotAttr.getAttrValue());
                        break;
                    case "PO_NUM":
                        result.setPoNum(materialLotAttr.getAttrValue());
                        break;
                    case "PO_LINE_NUM":
                        result.setPoLineNum(materialLotAttr.getAttrValue());
                        break;
                    case "WAREHOUSE_ID":
                        result.setWarehouseId(materialLotAttr.getAttrValue());
                        break;
                    case "PRODUCT_DATE":
                        result.setProductDate(materialLotAttr.getAttrValue());
                        break;
                    case "INSTRUCTION_DOC_NUM":
                        result.setInstructionDocNum(materialLotAttr.getAttrValue());
                        break;
                    case "INSTRUCTION_LINE_NUM":
                        result.setInstructionLineNum(materialLotAttr.getAttrValue());
                        break;
                    case "OVERDUE_INSPECTION_DATE":
                        result.setOverdueInspectionDate(materialLotAttr.getAttrValue());
                        break;
                    case "WO_ISSUE_DATE":
                        result.setWoIssueDate(materialLotAttr.getAttrValue());
                        break;
                    case "COLOR_BIN":
                        result.setColorBin(materialLotAttr.getAttrValue());
                        break;
                    case "LIGHT_BIN":
                        result.setLightBin(materialLotAttr.getAttrValue());
                        break;
                    case "VOLTAGE_BIN":
                        result.setVoltageBin(materialLotAttr.getAttrValue());
                        break;
                    case "PO_LINE_LOCATION_NUM":
                        result.setPoLineLocationNum(materialLotAttr.getAttrValue());
                        break;
                    case "SO_NUM":
                        result.setSoNum(materialLotAttr.getAttrValue());
                        break;
                    case "SO_LINE_NUM":
                        result.setSoLineNum(materialLotAttr.getAttrValue());
                        break;
                    case "PRINT_TIME":
                        result.setPrintTime(materialLotAttr.getAttrValue());
                        break;
                    case "PRINT_REASON":
                        result.setPrintReason(materialLotAttr.getAttrValue());
                        break;
                    case "HUMIDITY_LEVEL":
                        result.setHumidityLevel(materialLotAttr.getAttrValue());
                        result.setMsl(materialLotAttr.getAttrValue());
                        break;
                    case "PRINTING":
                        result.setPrinting(materialLotAttr.getAttrValue());
                        break;
                    case "EXPANSION_COEFFICIENTS":
                        result.setExpansionCoefficients(materialLotAttr.getAttrValue());
                        break;
                    case "INSTRUCTION_ID":
                        result.setInstructionId(materialLotAttr.getAttrValue());
                        break;
                    case "STICKER_NUMBER":
                        result.setStickerNumber(materialLotAttr.getAttrValue());
                        break;
                    case "WORK_ORDER_ID":
                        result.setWorkorderId(materialLotAttr.getAttrValue());
                        break;
                    case "REVIEW_RESULT":
                        result.setReviewResult(materialLotAttr.getAttrValue());
                        break;
                    case "APPROVAL_RESULT":
                        result.setApprovalResult(materialLotAttr.getAttrValue());
                        break;
                    case "APPROVAL_OPINION":
                        result.setApprovalOpinion(materialLotAttr.getAttrValue());
                        break;
                    case "APPROVAL_NOTE":
                        result.setApprovalNote(materialLotAttr.getAttrValue());
                        break;
                    case "SOLDER_GLUE_STATUS":
                        result.setSolderGlueStatus(materialLotAttr.getAttrValue());
                        break;
                    case "STATUS_DATE_TIME":
                        result.setStatusDateTime(materialLotAttr.getAttrValue());
                        break;
                    case "RECIPIENTS_PROD_LINE":
                        result.setRecipientsProdLine(materialLotAttr.getAttrValue());
                        break;
                    case "RECIPIENTS_COUNTS":
                        result.setRecipientsCounts(materialLotAttr.getAttrValue());
                        break;
                    case "RETURN_OUTTIME":
                        result.setReturnOuttime(materialLotAttr.getAttrValue());
                        break;
                    case "RETURN_EMPTY_BOTTLE":
                        result.setReturnEmptyBottle(materialLotAttr.getAttrValue());
                        break;
                    case "EXECUTOR_ID":
                        result.setExecutorId(materialLotAttr.getAttrValue());
                        break;
                    case "COSTCENTER_CODE":
                        result.setCostcenterCode(materialLotAttr.getAttrValue());
                        break;
                    case "ENTERED_BY":
                        result.setEnteredBy(materialLotAttr.getAttrValue());
                        break;
                    case "ENTERING_DATE":
                        result.setEnteringDate(materialLotAttr.getAttrValue());
                        break;
                    case "SCRAP_REASON":
                        result.setScrapReason(materialLotAttr.getAttrValue());
                        break;
                    case "RECEIPT_DATE":
                        result.setReceiptDate(materialLotAttr.getAttrValue());
                        break;
                    case "delivery_batch":
                        result.setDeliveryBatch(materialLotAttr.getAttrValue());
                        break;
                    case "quality_batch":
                        result.setQualityBatch(materialLotAttr.getAttrValue());
                        break;
                    case "instock_by":
                        result.setInstockBy(materialLotAttr.getAttrValue());
                        break;
                    case "QM_FLAG":
                        result.setQmFlag(materialLotAttr.getAttrValue());
                        break;
                    case "ASN_NUM":
                        result.setAsnNum(materialLotAttr.getAttrValue());
                        break;
                    case "ASN_LINE_NUM":
                        result.setAsnLineNum(materialLotAttr.getAttrValue());
                        break;
                    case "QC_COMPLETE_TIME":
                        result.setQcCompleteTime(materialLotAttr.getAttrValue());
                        break;
                    case "QUALITY_DISPOSAL_STRATEGY":
                        result.setQualityDisposalStrategy(materialLotAttr.getAttrValue());
                        break;
                    case "WBS_NUM":
                        result.setWbsNum(materialLotAttr.getAttrValue());
                        break;
                    case "REMARK":
                        result.setRemark(materialLotAttr.getAttrValue());
                        break;
                    case "MATERIAL_VERSION":
                        result.setMaterialVersion(materialLotAttr.getAttrValue());
                        break;
                    case "ENABLE_DATE":
                        result.setEnableDate(materialLotAttr.getAttrValue());
                        break;
                    case "DEADLINE_DATE":
                        result.setDeadlineDate(materialLotAttr.getAttrValue());
                        break;
                    case "LAB_CODE":
                        result.setLabCode(materialLotAttr.getAttrValue());
                        break;
                    case "LAB_REMARK":
                        result.setLabRemark(materialLotAttr.getAttrValue());
                        break;
                    default:
                        break;
                }
            }
            resultList.add(result);
        }

        return resultList;
    }


    /**
     * 条码（物料批）扩展属性行转列方法
     *
     * @param tenantId
     * @param mateiralLotIds
     * @author chuang.yang
     * @date 2020/4/22
     */
    @Override
    public Map<String, WmsMaterialLotAttrViewVO> queryMaterialLotAttrViewDataMap(Long tenantId,
                                                                                 List<String> mateiralLotIds) {
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setKeyIdList(mateiralLotIds);
        mtExtendVO1.setTableName("mt_material_lot_attr");
        List<MtExtendAttrVO1> materialLotAttrList =
                mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        if (CollectionUtils.isEmpty(materialLotAttrList)) {
            return new HashMap<>();
        }
        Map<String, WmsMaterialLotAttrViewVO> queryMaterialLotAttrViewDataMap = new HashMap<>();

        Map<String, List<MtExtendAttrVO1>> materialLotAttrListMap =
                materialLotAttrList.stream().collect(Collectors.groupingBy(MtExtendAttrVO1::getKeyId));

        for (Map.Entry<String, List<MtExtendAttrVO1>> entry : materialLotAttrListMap.entrySet()) {
            WmsMaterialLotAttrViewVO result = new WmsMaterialLotAttrViewVO();
            result.setTenantId(tenantId);
            result.setMaterialLotId(entry.getKey());
            for (MtExtendAttrVO1 materialLotAttr : entry.getValue()) {
                switch (materialLotAttr.getAttrName()) {
                    case "STATUS":
                        result.setStatus(materialLotAttr.getAttrValue());
                        break;
                    case "GRADE_CODE":
                        result.setGradeCode(materialLotAttr.getAttrValue());
                        break;
                    case "ORIGINAL_ID":
                        result.setOriginalId(materialLotAttr.getAttrValue());
                        break;
                    case "PO_NUM":
                        result.setPoNum(materialLotAttr.getAttrValue());
                        break;
                    case "PO_LINE_NUM":
                        result.setPoLineNum(materialLotAttr.getAttrValue());
                        break;
                    case "WAREHOUSE_ID":
                        result.setWarehouseId(materialLotAttr.getAttrValue());
                        break;
                    case "PRODUCT_DATE":
                        result.setProductDate(materialLotAttr.getAttrValue());
                        break;
                    case "INSTRUCTION_DOC_NUM":
                        result.setInstructionDocNum(materialLotAttr.getAttrValue());
                        break;
                    case "INSTRUCTION_LINE_NUM":
                        result.setInstructionLineNum(materialLotAttr.getAttrValue());
                        break;
                    case "OVERDUE_INSPECTION_DATE":
                        result.setOverdueInspectionDate(materialLotAttr.getAttrValue());
                        break;
                    case "WO_ISSUE_DATE":
                        result.setWoIssueDate(materialLotAttr.getAttrValue());
                        break;
                    case "COLOR_BIN":
                        result.setColorBin(materialLotAttr.getAttrValue());
                        break;
                    case "LIGHT_BIN":
                        result.setLightBin(materialLotAttr.getAttrValue());
                        break;
                    case "VOLTAGE_BIN":
                        result.setVoltageBin(materialLotAttr.getAttrValue());
                        break;
                    case "PO_LINE_LOCATION_NUM":
                        result.setPoLineLocationNum(materialLotAttr.getAttrValue());
                        break;
                    case "SO_NUM":
                        result.setSoNum(materialLotAttr.getAttrValue());
                        break;
                    case "SO_LINE_NUM":
                        result.setSoLineNum(materialLotAttr.getAttrValue());
                        break;
                    case "PRINT_TIME":
                        result.setPrintTime(materialLotAttr.getAttrValue());
                        break;
                    case "PRINT_REASON":
                        result.setPrintReason(materialLotAttr.getAttrValue());
                        break;
                    case "HUMIDITY_LEVEL":
                        result.setHumidityLevel(materialLotAttr.getAttrValue());
                        result.setMsl(materialLotAttr.getAttrValue());
                        break;
                    case "PRINTING":
                        result.setPrinting(materialLotAttr.getAttrValue());
                        break;
                    case "EXPANSION_COEFFICIENTS":
                        result.setExpansionCoefficients(materialLotAttr.getAttrValue());
                        break;
                    case "INSTRUCTION_ID":
                        result.setInstructionId(materialLotAttr.getAttrValue());
                        break;
                    case "STICKER_NUMBER":
                        result.setStickerNumber(materialLotAttr.getAttrValue());
                        break;
                    case "WORK_ORDER_ID":
                        result.setWorkorderId(materialLotAttr.getAttrValue());
                        break;
                    case "REVIEW_RESULT":
                        result.setReviewResult(materialLotAttr.getAttrValue());
                        break;
                    case "APPROVAL_RESULT":
                        result.setApprovalResult(materialLotAttr.getAttrValue());
                        break;
                    case "APPROVAL_OPINION":
                        result.setApprovalOpinion(materialLotAttr.getAttrValue());
                        break;
                    case "APPROVAL_NOTE":
                        result.setApprovalNote(materialLotAttr.getAttrValue());
                        break;
                    case "SOLDER_GLUE_STATUS":
                        result.setSolderGlueStatus(materialLotAttr.getAttrValue());
                        break;
                    case "STATUS_DATE_TIME":
                        result.setStatusDateTime(materialLotAttr.getAttrValue());
                        break;
                    case "RECIPIENTS_PROD_LINE":
                        result.setRecipientsProdLine(materialLotAttr.getAttrValue());
                        break;
                    case "RECIPIENTS_COUNTS":
                        result.setRecipientsCounts(materialLotAttr.getAttrValue());
                        break;
                    case "RETURN_OUTTIME":
                        result.setReturnOuttime(materialLotAttr.getAttrValue());
                        break;
                    case "RETURN_EMPTY_BOTTLE":
                        result.setReturnEmptyBottle(materialLotAttr.getAttrValue());
                        break;
                    case "EXECUTOR_ID":
                        result.setExecutorId(materialLotAttr.getAttrValue());
                        break;
                    case "COSTCENTER_CODE":
                        result.setCostcenterCode(materialLotAttr.getAttrValue());
                        break;
                    case "ENTERED_BY":
                        result.setEnteredBy(materialLotAttr.getAttrValue());
                        break;
                    case "ENTERING_DATE":
                        result.setEnteringDate(materialLotAttr.getAttrValue());
                        break;
                    case "SCRAP_REASON":
                        result.setScrapReason(materialLotAttr.getAttrValue());
                        break;
                    case "RECEIPT_DATE":
                        result.setReceiptDate(materialLotAttr.getAttrValue());
                        break;
                    case "delivery_batch":
                        result.setDeliveryBatch(materialLotAttr.getAttrValue());
                        break;
                    case "quality_batch":
                        result.setQualityBatch(materialLotAttr.getAttrValue());
                        break;
                    case "instock_by":
                        result.setInstockBy(materialLotAttr.getAttrValue());
                        break;
                    case "QM_FLAG":
                        result.setQmFlag(materialLotAttr.getAttrValue());
                        break;
                    case "ASN_NUM":
                        result.setAsnNum(materialLotAttr.getAttrValue());
                        break;
                    case "ASN_LINE_NUM":
                        result.setAsnLineNum(materialLotAttr.getAttrValue());
                        break;
                    case "QC_COMPLETE_TIME":
                        result.setQcCompleteTime(materialLotAttr.getAttrValue());
                        break;
                    case "QUALITY_DISPOSAL_STRATEGY":
                        result.setQualityDisposalStrategy(materialLotAttr.getAttrValue());
                        break;
                    case "WBS_NUM":
                        result.setWbsNum(materialLotAttr.getAttrValue());
                        break;
                    default:
                        break;

                }
            }
            queryMaterialLotAttrViewDataMap.put(entry.getKey(), result);
        }

        return queryMaterialLotAttrViewDataMap;
    }

}
