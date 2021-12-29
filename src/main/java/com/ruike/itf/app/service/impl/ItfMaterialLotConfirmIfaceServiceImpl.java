package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ruike.itf.api.dto.ItfMaterialLotConfirmIfaceDTO;
import com.ruike.itf.app.service.ItfMaterialLotConfirmIfaceService;
import com.ruike.itf.domain.entity.ItfMaterialLotConfirmIface;
import com.ruike.itf.domain.repository.ItfMaterialLotConfirmIfaceRepository;
import com.ruike.itf.domain.vo.*;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfMaterialLotConfirmIfaceMapper;
import com.ruike.itf.infra.util.JsonUtils;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO10;
import tarzan.inventory.domain.vo.MtContLoadDtlVO4;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 立库入库复核接口表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-07-13 19:40:25
 */
@Service
@Slf4j
public class ItfMaterialLotConfirmIfaceServiceImpl implements ItfMaterialLotConfirmIfaceService {

    @Autowired
    private ItfMaterialLotConfirmIfaceMapper itfMaterialLotConfirmIfaceMapper;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private ItfMaterialLotConfirmIfaceRepository itfMaterialLotConfirmIfaceRepository;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public List<ItfMaterialLotConfirmIfaceVO4> itfMaterialLotConfirmOrChangeIface(Long tenantId, ItfMaterialLotConfirmIfaceDTO dto) {
        if (CollectionUtils.isEmpty(dto.getBarcodeList())) {
            throw new CommonException("输入参数BarcodeList不允许为空");
        }
        //校验传入的条码，并获得对应的物料批ID
        ItfMaterialLotConfirmIfaceVO3 itfMaterialLotConfirmIfaceVO3 = validateBarCode(tenantId, dto);
        //根据物料批ID集合查询数据，并将数据插入到接口表中
        String batchId = itfMaterialLotConfirmIfaceRepository.insertMaterialLotConfirmIface(tenantId, itfMaterialLotConfirmIfaceVO3.getMaterialLotIdList());
        List<LovValueDTO> lovValueList = lovAdapter.queryLovValue("ITF.INTERNAL_FLAG", tenantId);
        if (CollectionUtils.isEmpty(lovValueList)){
            throw new CommonException("ITF.INTERNAL_FLAG值集没有维护\n含义值为Y或N【Y为实时发送，N为不实时发送】");
        }
        Optional<LovValueDTO> lovFlag = lovValueList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "TIMELY_INTERFACE_FLAG")).findFirst();
        String interfaceFlag = lovFlag.isPresent() ? lovFlag.get().getMeaning() : "";
        List<ItfMaterialLotConfirmIfaceVO4> resultList = new ArrayList<>();
        if (ItfConstant.ConstantValue.YES.equals(interfaceFlag)) {
            //根据批次ID查询传输的数据
            List<ItfMaterialLotConfirmIface> ifaceSendList = itfMaterialLotConfirmIfaceMapper.ifaceSendDataQuery(tenantId, batchId);
            if(CollectionUtils.isEmpty(ifaceSendList)){
                throw new CommonException("接口MES方无传输数据");
            }
            List<String> ifaceIdList = ifaceSendList.stream().map(ItfMaterialLotConfirmIface::getIfaceId).collect(Collectors.toList());
            List<ItfMaterialLotConfirmIfaceVO> materialLotConfirmIfaceVOList = new ArrayList<>();
            List<Map<String, Object>> materialLotConfirmIfaceVOList1 = new ArrayList<>();
            for (ItfMaterialLotConfirmIface iface : ifaceSendList) {
                Map<String, Object> map = new HashMap<>(100);
                map.put("MATERIAL_LOT_CODE",iface.getMaterialLotCode());
                map.put("MATERIAL_CODE",iface.getMaterialCode());
                map.put("MATERIAL_VERSION",StringUtils.isNotBlank(iface.getMaterialVersion()) ? iface.getMaterialVersion() : "");
                map.put("UOMCODE",iface.getUomCode());
                map.put("WAREHOUSE_CODE",StringUtils.isNotBlank(iface.getWarehouseCode()) ? iface.getWarehouseCode() : "");
                map.put("STATUS",StringUtils.isNotBlank(iface.getStatus()) ? iface.getStatus() : "");
                map.put("QUALITY_STATUS",StringUtils.isNotBlank(iface.getQualityStatus()) ? iface.getQualityStatus() : "");
                map.put("QTY",Objects.nonNull(iface.getQty()) ? iface.getQty().toString() : "");
                map.put("SO_NUM",StringUtils.isNotBlank(iface.getSoNum()) ? iface.getSoNum() : "");
                map.put("SO_LINE_NUM",StringUtils.isNotBlank(iface.getSoLineNum()) ? iface.getSoLineNum() : "");
                map.put("LOT_CODE",StringUtils.isNotBlank(iface.getLotCode()) ? iface.getLotCode() : "");
                map.put("PRODUCTION_DATE",StringUtils.isNotBlank(iface.getProductionDate()) ? iface.getProductionDate() : "");
                map.put("CONTAINER_CODE",StringUtils.isNotBlank(iface.getContainerCode()) ? iface.getContainerCode() : "");
                materialLotConfirmIfaceVOList1.add(map);
//                ItfMaterialLotConfirmIfaceVO materialLotConfirmIfaceVO = new ItfMaterialLotConfirmIfaceVO();
//                materialLotConfirmIfaceVO.setMATERIAL_LOT_CODE(iface.getMaterialLotCode());
//                materialLotConfirmIfaceVO.setMATERIAL_CODE(iface.getMaterialCode());
//                materialLotConfirmIfaceVO.setMATERIAL_VERSION(StringUtils.isNotBlank(iface.getMaterialVersion()) ? iface.getMaterialVersion() : "");
//                materialLotConfirmIfaceVO.setUOMCODE(iface.getUomCode());
//                materialLotConfirmIfaceVO.setWAREHOUSE_CODE(StringUtils.isNotBlank(iface.getWarehouseCode()) ? iface.getWarehouseCode() : "");
//                materialLotConfirmIfaceVO.setSTATUS(StringUtils.isNotBlank(iface.getStatus()) ? iface.getStatus() : "");
//                materialLotConfirmIfaceVO.setQUALITY_STATUS(StringUtils.isNotBlank(iface.getQualityStatus()) ? iface.getQualityStatus() : "");
//                materialLotConfirmIfaceVO.setQTY(Objects.nonNull(iface.getQty()) ? iface.getQty().toString() : "");
//                materialLotConfirmIfaceVO.setSO_NUM(StringUtils.isNotBlank(iface.getSoNum()) ? iface.getSoNum() : "");
//                materialLotConfirmIfaceVO.setSO_LINE_NUM(StringUtils.isNotBlank(iface.getSoLineNum()) ? iface.getSoLineNum() : "");
//                materialLotConfirmIfaceVO.setLOT_CODE(StringUtils.isNotBlank(iface.getLotCode()) ? iface.getLotCode() : "");
//                materialLotConfirmIfaceVO.setPRODUCTION_DATE(StringUtils.isNotBlank(iface.getProductionDate()) ? iface.getProductionDate() : "");
//                materialLotConfirmIfaceVO.setCONTAINER_CODE(StringUtils.isNotBlank(iface.getContainerCode()) ? iface.getContainerCode() : "");
//                materialLotConfirmIfaceVOList.add(materialLotConfirmIfaceVO);
            }
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();

            //调用接口
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("requestInfo", JSONArray.toJSONString(materialLotConfirmIfaceVOList1));
            ResponsePayloadDTO responsePayload = null;
            try {
                responsePayload = itfMaterialLotConfirmIfaceRepository.sendWcs(requestMap, "ItfMaterialLotConfirmIface",
                        ItfConstant.InterfaceCode.WCS_MATERIAL_LOT_CONFIRM);
            }catch (Exception e){
                itfMaterialLotConfirmIfaceRepository.updateIfaceData(tenantId, ifaceIdList, e.getMessage(), userId);
                throw e;
            }
            //解析返回报文,如果有异常情况，需要更新为E
            ItfWcsResponseVO itfWcsResponseVO = itfMaterialLotConfirmIfaceRepository.validateResponsePayload(tenantId, responsePayload, ifaceIdList, userId);
            List<ItfMaterialLotConfirmIfaceVO2> header = itfWcsResponseVO.getHeader();
            //根据正常返回的报文去更新接口数据,如果有报错条码，则封装报错数据
            resultList = itfMaterialLotConfirmIfaceRepository.updateIface(tenantId, header, batchId, userId, ifaceIdList);
            //如果条码输入的是容器条码，则需要对上一步封装的报错数据整合
            if("CONTAINER".equals(itfMaterialLotConfirmIfaceVO3.getBarcodeType())){
                String errorMessage = resultList.get(0).getErrorMessage();
                Boolean success = resultList.get(0).getSuccess();
                resultList = new ArrayList<>();
                //因为输入容器条码的情况下，只会输入一个容器条码，且报错消息返回任意报错条码的报错消息即可
                ItfMaterialLotConfirmIfaceVO4 result = new ItfMaterialLotConfirmIfaceVO4();
                result.setBarcode(dto.getBarcodeList().get(0));
                result.setErrorMessage(errorMessage);
                result.setSuccess(success);
                resultList.add(result);
            }
        }
        return resultList;
    }

    ItfMaterialLotConfirmIfaceVO3 validateBarCode(Long tenantId, ItfMaterialLotConfirmIfaceDTO dto) {
        ItfMaterialLotConfirmIfaceVO3 result = new ItfMaterialLotConfirmIfaceVO3();
        //判断输入的条码是物料批条码还是容器条码
        String barcodeType = null;
        List<String> barcodeList = dto.getBarcodeList();
        List<String> materialLotIdList = new ArrayList<>();
        List<String> containerIdList = new ArrayList<>();
        List<MtMaterialLot> mtMaterialLotList = itfMaterialLotConfirmIfaceMapper.materialLotBatchQuery(tenantId, barcodeList);
        if (CollectionUtils.isNotEmpty(mtMaterialLotList) && mtMaterialLotList.size() == barcodeList.size()) {
            barcodeType = "MATERIAL_LOT";
            materialLotIdList = mtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
        } else {
            List<MtContainer> mtContainerList = itfMaterialLotConfirmIfaceMapper.containerBatchQuery(tenantId, barcodeList);
            if (CollectionUtils.isNotEmpty(mtContainerList) && mtContainerList.size() == barcodeList.size()) {
                barcodeType = "CONTAINER";
                containerIdList = mtContainerList.stream().map(MtContainer::getContainerId).collect(Collectors.toList());
            }
        }
        if (StringUtils.isEmpty(barcodeType)) {
            throw new CommonException("条码不存在");
        }
        //如果是容器条码，则根据容器获取容器下所有条码,一般来说，容器条码只会传一个
        if ("CONTAINER".equals(barcodeType)) {
            for (String containerId : containerIdList) {
                MtContLoadDtlVO10 mtContLoadDtlVO10 = new MtContLoadDtlVO10();
                mtContLoadDtlVO10.setContainerId(containerId);
                List<MtContLoadDtlVO4> mtContLoadDtlVO4s = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, mtContLoadDtlVO10);
                if (CollectionUtils.isNotEmpty(mtContLoadDtlVO4s)) {
                    materialLotIdList.addAll(mtContLoadDtlVO4s.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList()));
                }
            }
        }
        result.setBarcodeType(barcodeType);
        result.setMaterialLotIdList(materialLotIdList);
        return result;
    }
}
