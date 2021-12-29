package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.app.service.ItfBomComponentIfaceService;
import com.ruike.itf.domain.entity.ItfBomComponentIface;
import com.ruike.itf.domain.entity.ItfRoutingOperationIface;
import com.ruike.itf.infra.mapper.ItfBomComponentIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.itf.utils.Utils;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tarzan.iface.domain.repository.MtBomComponentIfaceRepository;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BOM接口表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@Service
@Slf4j
public class ItfBomComponentIfaceServiceImpl implements ItfBomComponentIfaceService {

    @Autowired
    private ItfBomComponentIfaceMapper itfBomComponentIfaceMapper;

    @Autowired
    private MtBomComponentIfaceRepository mtBomComponentIfaceRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    @Override
    public List<ItfBomComponentIface> invoke(Map dto) {
        Long batchId = Long.valueOf(this.customDbRepository.getNextKey("mt_bom_component_iface_cid_s"));
        List<ItfSapIfaceDTO> headData = JSONArray.parseArray(dto.get("HEAD").toString(), ItfSapIfaceDTO.class);
        List<ItfSapIfaceDTO> itemData = JSONArray.parseArray(dto.get("ITEM").toString(), ItfSapIfaceDTO.class);
        List<ItfBomComponentIface> bomData = isDataNull(getBomData(headData, itemData,batchId));
        // 插入记录表
        //V20210726 modify by penglin.sui for tianyang.xie 分批次插入临时表
        List<List<ItfBomComponentIface>> splitSqlList = InterfaceUtils.splitSqlList(bomData, 3000);
        for (List<ItfBomComponentIface> domains : splitSqlList) {
            itfBomComponentIfaceMapper.batchInsertBomIface("itf_bom_component_iface", domains);
        }
        // 筛选消息为空的数据，消息为空的数据是正确数据
        List<ItfBomComponentIface> insertData = bomData.stream().filter(a -> Strings.isEmpty(a.getMessage())).collect(Collectors.toList());
        if (insertData.size() > 0) {
            //V20210726 modify by penglin.sui for tianyang.xie 分批次插入临时表
            List<List<ItfBomComponentIface>> insertSplitSqlList = InterfaceUtils.splitSqlList(insertData, 3000);
            for (List<ItfBomComponentIface> domains : insertSplitSqlList) {
                itfBomComponentIfaceMapper.batchInsertBom("mt_bom_component_iface", domains);
            }
        }
        // 筛选消息不为空的数据，当消息不为空的时候，说明是错误数据
        List<ItfBomComponentIface> errorData = bomData.stream().filter(a -> !Strings.isEmpty(a.getMessage())).collect(Collectors.toList());
        // 调用API
        // V20211117 modify by penglin.sui for hui.ma 取消实时调用，改为调度任务异步调用
//        mtBomComponentIfaceRepository.myBomInterfaceImport(tenantId, batchId);
        return errorData;
    }

    /**
     * 判断必输字段
     *
     * @param bomData
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/18 17:15
     */
    public List<ItfBomComponentIface> isDataNull(List<ItfBomComponentIface> bomData) {
        // 判断必输字段
        for (int i = 0; i < bomData.size(); i++) {
            StringBuffer errorMsg = new StringBuffer();
            if (Strings.isEmpty(bomData.get(i).getPlantCode())) {
                errorMsg.append("工厂不允许为空！");
            }
            if (Strings.isEmpty(bomData.get(i).getBomCode())) {
                errorMsg.append("BOM编码不允许为空！");
            }
            // 20211008 modify by sanfeng.zhang for wenxin.zhang 物料类型时不校验描述非空
            if (!"MATERIAL".equals(bomData.get(i).getBomObjectType()) && Strings.isEmpty(bomData.get(i).getBomDescription())) {
                errorMsg.append("BOM描述不允许为空！");
            }
            if (Objects.isNull(bomData.get(i).getComponentStartDate())) {
                errorMsg.append("BOM组件开始时间不允许为空！");
            }
            if (Strings.isEmpty(bomData.get(i).getBomObjectCode())) {
                errorMsg.append("BOM对象编码不允许为空！");
            }
            if (Objects.isNull(bomData.get(i).getStandardQty())) {
                errorMsg.append("基准数量不允许为空！");
            }
            if (Objects.isNull(bomData.get(i).getComponentLineNum())) {
                errorMsg.append("组件行号不允许为空！");
            }
            if (Strings.isEmpty(bomData.get(i).getComponentItemCode())) {
                errorMsg.append("组件编码不允许为空！");
            }
            if (Objects.isNull(bomData.get(i).getComponentLineNum())) {
                errorMsg.append("组件行号不允许为空！");
            }
            if (Objects.isNull(bomData.get(i).getBomUsage())) {
                errorMsg.append("组件单位用量不允许为空！");
            }
            String bomMsg = Strings.isEmpty(bomData.get(i).getMessage()) ? "" : bomData.get(i).getMessage();
            bomData.get(i).setMessage(bomMsg + errorMsg.toString());
        }
        return bomData;
    }

    /**
     * erp传的数据时分开的两个集合，需要整理合并数据
     *
     * @param headData
     * @param lineData
     * @param batchId
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/18 16:47
     */
    public List<ItfBomComponentIface> getBomData(List<ItfSapIfaceDTO> headData, List<ItfSapIfaceDTO> lineData, Long batchId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = Utils.getNowDate();
        String batchDate = format.format(date);

        List<ItfBomComponentIface> ifaces = new ArrayList<>();
        for (ItfSapIfaceDTO head : headData) {
            boolean isFlag = true;
            for (ItfSapIfaceDTO line : lineData) {
                String headPlant = Strings.isEmpty(head.getWERKS()) ? "" : head.getWERKS();
                String headBomCode = Strings.isEmpty(head.getSTLNR()) ? "" : head.getSTLNR();
                String headBomAlternate = Strings.isEmpty(head.getSTLAL()) ? "" : head.getSTLAL();
                String linePlant = Strings.isEmpty(line.getWERKS()) ? "" : line.getWERKS();
                String lineBomCode = Strings.isEmpty(line.getSTLNR()) ? "" : line.getSTLNR();
                String lineBomAlternate = Strings.isEmpty(line.getSTLAL()) ? "" : line.getSTLAL();
                // 查找头和行的关系
                if (headPlant.equals(linePlant) && headBomCode.equals(lineBomCode) && headBomAlternate.equals(lineBomAlternate)) {
                    ItfBomComponentIface bomComponentIface = new ItfBomComponentIface();
                    bomComponentIface.setTenantId(tenantId);
                    bomComponentIface.setBatchId(Double.valueOf(batchId));
                    bomComponentIface.setBatchDate(batchDate);
                    bomComponentIface.setCid(batchId);
                    bomComponentIface.setPlantCode(Strings.isEmpty(head.getWERKS()) ? "" : head.getWERKS());
                    bomComponentIface.setBomCode(Strings.isEmpty(head.getSTLNR()) ? "" : head.getSTLNR().replaceAll("^(0+)",""));
                    bomComponentIface.setBomAlternate(Strings.isEmpty(head.getSTLAL()) ? "" : head.getSTLAL());
                    bomComponentIface.setBomDescription(Strings.isEmpty(head.getSTKTX()) ? "" : head.getSTKTX());
                    bomComponentIface.setBomObjectType("MATERIAL");
                    bomComponentIface.setBomObjectCode(Strings.isEmpty(head.getMATNR()) ? "" : head.getMATNR().replaceAll("^(0+)",""));
                   // bomComponentIface.setStandardQty(Objects.isNull(head.getBMEND()) ? null : head.getBMEND());
                    bomComponentIface.setStandardQty(Objects.isNull(head.getBMENG()) ? null : head.getBMENG());

                    //
                    bomComponentIface.setBomStartDate(date);
                    bomComponentIface.setUpdateMethod("UPDATE");
                    bomComponentIface.setIfaceId("");
                    bomComponentIface.setComponentLineNum(Objects.isNull(line.getSTPOZ()) ? null : line.getSTPOZ());
                    bomComponentIface.setComponentItemCode(Strings.isEmpty(line.getIDNRK()) ? "" : line.getIDNRK().replaceAll("^(0+)",""));
                    bomComponentIface.setOperationSequence(Strings.isEmpty(line.getSORTF()) ? "" : line.getSORTF());
                    bomComponentIface.setBomUsage(Objects.isNull(line.getMENGE()) ? null : line.getMENGE());
                    bomComponentIface.setComponentShrinkage(Objects.isNull(line.getAUSCH()) ? null : line.getAUSCH());
                    bomComponentIface.setComponentStartDate(Objects.isNull(line.getDATUV()) ? null : line.getDATUV());
                    bomComponentIface.setComponentEndDate(Objects.isNull(line.getDATUB()) ? null : line.getDATUB());
                    bomComponentIface.setSubstituteGroup(Strings.isEmpty(line.getALPGR()) ? "" : line.getALPGR());
                    bomComponentIface.setBomUsage(Objects.isNull(line.getMENGE()) ? null : line.getMENGE());
                    bomComponentIface.setIssueLocatorCode(Strings.isEmpty(line.getLGPRO()) ? "" : line.getLGPRO());
                    bomComponentIface.setLineAttribute8(Strings.isEmpty(line.getDUMPS()) ? "" : line.getDUMPS());
                    bomComponentIface.setLineAttribute13(Strings.isEmpty(line.getPOSNR()) ? "" : line.getPOSNR());
                    bomComponentIface.setLineAttribute14(Strings.isEmpty(line.getSTLKN()) ? "" : line.getSTLKN());
                    bomComponentIface.setLineAttribute15(Strings.isEmpty(line.getALPST()) ? "" : line.getALPST());
                    bomComponentIface.setLineAttribute16(Strings.isEmpty(line.getALPRF()) ? "" : line.getALPRF());
                    bomComponentIface.setLineAttribute17(Strings.isEmpty(line.getEWAHR()) ? "" : line.getEWAHR());
                    bomComponentIface.setLineAttribute18(Strings.isEmpty(line.getMEINS()) ? "" : line.getMEINS());
                    bomComponentIface.setLineAttribute19(Strings.isEmpty(line.getPOTX1()) ? "" : line.getPOTX1());
                    bomComponentIface.setLineAttribute20(Strings.isEmpty(line.getPOTX2()) ? "" : line.getPOTX2());
                    bomComponentIface.setLineAttribute21(Strings.isEmpty(line.getKZKUP()) ? "" : line.getKZKUP());
                    // 添加字段
                    String assembleMethod = Strings.isEmpty(line.getRGEKZ()) ? "" : line.getRGEKZ();
                    String bomComponentType = Strings.isEmpty(line.getDUMPS()) ? "" : line.getDUMPS();
//                    if("X".equals(assembleMethod)){
//                        assembleMethod = "BACKFLASH";
//                    }else{
//                        assembleMethod = "ISSUE";
//                    }
                    if("X".equals(bomComponentType)){
                        bomComponentType = "PHANTOM";
                    } else {
                        bomComponentType = "ASSEMBLING";
                    }
                    bomComponentIface.setBomComponentType(bomComponentType);
                    //===========================
                    bomComponentIface.setErpCreatedBy(-1L);
                    bomComponentIface.setErpCreationDate(date);
                    bomComponentIface.setErpLastUpdatedBy(-1L);
                    bomComponentIface.setErpLastUpdateDate(date);
                    bomComponentIface.setObjectVersionNumber(1L);
                    bomComponentIface.setCreatedBy(-1L);
                    bomComponentIface.setCreationDate(date);
                    bomComponentIface.setLastUpdatedBy(-1L);
                    bomComponentIface.setLastUpdateDate(date);
                    bomComponentIface.setStatus("N");
                    ifaces.add(bomComponentIface);
                    isFlag = false;
                }
            }
            if (isFlag) {
                ItfBomComponentIface bomComponentIface = new ItfBomComponentIface();
                bomComponentIface.setTenantId(tenantId);
                bomComponentIface.setBatchId(Double.valueOf(batchId));
                bomComponentIface.setBatchDate(batchDate);
                bomComponentIface.setCid(batchId);
                bomComponentIface.setPlantCode(Strings.isEmpty(head.getWERKS()) ? "" : head.getWERKS());
                bomComponentIface.setBomCode(Strings.isEmpty(head.getSTLNR()) ? "" : head.getSTLNR().replaceAll("^(0+)",""));
                bomComponentIface.setBomAlternate(Strings.isEmpty(head.getSTLAL()) ? "" : head.getSTLAL());
                bomComponentIface.setBomDescription(Strings.isEmpty(head.getSTKTX()) ? "" : head.getSTKTX());
                bomComponentIface.setBomObjectType("MATERIAL");
                bomComponentIface.setBomObjectCode(Strings.isEmpty(head.getMATNR()) ? "" : head.getMATNR().replaceAll("^(0+)", ""));
                bomComponentIface.setStandardQty(Objects.isNull(head.getBMENG()) ? null : head.getBMENG());
                bomComponentIface.setIfaceId("");
                bomComponentIface.setErpCreatedBy(-1L);
                bomComponentIface.setErpCreationDate(date);
                bomComponentIface.setErpLastUpdatedBy(-1L);
                bomComponentIface.setErpLastUpdateDate(date);
                bomComponentIface.setObjectVersionNumber(1L);
                bomComponentIface.setCreatedBy(-1L);
                bomComponentIface.setCreationDate(date);
                bomComponentIface.setLastUpdatedBy(-1L);
                bomComponentIface.setLastUpdateDate(date);
                bomComponentIface.setStatus("N");
                bomComponentIface.setMessage("没有行数据！");
                ifaces.add(bomComponentIface);
            }
        }

        //V20210723 modify by penglin.sui for tianyang.xie 批量获取序列
        if(CollectionUtils.isNotEmpty(ifaces)){
            List<String> bomComponentIfaceList = customDbRepository.getNextKeys("mt_bom_component_iface_s", ifaces.size());
            int index = 0;
            for (ItfBomComponentIface iface : ifaces
                 ) {
                iface.setIfaceId(bomComponentIfaceList.get(index++));
            }
        }

        return ifaces;
    }
}
