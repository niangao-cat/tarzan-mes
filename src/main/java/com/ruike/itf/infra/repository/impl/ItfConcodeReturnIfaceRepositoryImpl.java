package com.ruike.itf.infra.repository.impl;


import com.ruike.itf.api.dto.ItfConcodeReturnIfaceDTO;
import com.ruike.itf.api.dto.ItfConcodeReturnIfaceDTO1;
import com.ruike.itf.domain.entity.ItfConcodeReturnIface;
import com.ruike.itf.domain.entity.ItfConcodeReturnLineIface;
import com.ruike.itf.domain.repository.ItfConcodeReturnIfaceRepository;
import com.ruike.itf.infra.mapper.ItfConcodeReturnIfaceMapper;
import com.ruike.itf.infra.mapper.ItfConcodeReturnLineIfaceMapper;
import com.ruike.itf.infra.mapper.ModLocatorIfaceMapper;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtContainerMapper;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 成品出入库容器信息返回接口资源库
 *
 * @author taowen.wang@hand-china.com 2021/6/30 13:40
 */
@Component
public class ItfConcodeReturnIfaceRepositoryImpl extends BaseRepositoryImpl<ItfConcodeReturnIface> implements ItfConcodeReturnIfaceRepository {
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private ItfConcodeReturnIfaceMapper itfConcodeReturnIfaceMapper;
    @Autowired
    private ItfConcodeReturnLineIfaceMapper itfConcodeReturnLineIfaceMapper;
    @Autowired
    private MtContainerMapper mtContainerMapper;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;
    @Autowired
    private ModLocatorIfaceMapper modLocatorIfaceMapper;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;


    @Override
    public ItfConcodeReturnIfaceDTO itfConcodeReturnIfaceByContainerCode(Long tenantId,ItfConcodeReturnIfaceDTO1 itfConcodeReturnIfaceDTO1) {
        String containerCode = itfConcodeReturnIfaceDTO1.getContainerCode();
        String type = itfConcodeReturnIfaceDTO1.getType();
        List<String> materialLotCodeList = itfConcodeReturnIfaceDTO1.getMaterialLotCodeList();
        //参数校验
        ItfConcodeReturnIfaceDTO returnIfaceDTO = new ItfConcodeReturnIfaceDTO();
        if (!StringUtils.isNotEmpty(containerCode)) {
            returnIfaceDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            returnIfaceDTO.setProcessMessage("容器号为空");
            return returnIfaceDTO;
        }
        if (!StringUtils.isNotEmpty(type)) {
            returnIfaceDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            returnIfaceDTO.setProcessMessage("库位类型为空");
            return returnIfaceDTO;
        }
        if (CollectionUtils.isEmpty(materialLotCodeList)) {
            returnIfaceDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            returnIfaceDTO.setProcessMessage("物料批清单为空");
            return returnIfaceDTO;
        }
        //API逻辑
        returnIfaceDTO = logicCheck(tenantId,containerCode,type,materialLotCodeList);
        return returnIfaceDTO;
    }


    /**
     * API逻辑
     *
     * @param containerCode
     * @param type
     * @param materialLotCodeList
     * @return
     */
    public ItfConcodeReturnIfaceDTO logicCheck(Long tenantId,String containerCode, String type,List<String> materialLotCodeList) {
        ItfConcodeReturnIfaceDTO returnIfaceDTO = new ItfConcodeReturnIfaceDTO();
        try {
            // requestTypeCode默认为WAREHOUSE_IN
            String requestTypeCode = "WAREHOUSE_IN";
            // eventTypeCode默认为WAREHOUSE_IN
            String eventTypeCode = "WAREHOUSE_IN";
            List<String> containerCodes = new ArrayList<>();
            containerCodes.add(containerCode);
            // 获取容器接口表信息 (由于ContainerCode容器编码是一个唯一标识符，所以只会查询出一条数据)containerCodes
            List<MtContainer> mtContainers = mtContainerMapper.selectByContainerCodes(tenantId, containerCodes);
            if (CollectionUtils.isEmpty(mtContainers)) {
                throw new MtException("容器" + containerCode + "不存在!");
            }
            MtContainer mtContainer = mtContainers.get(0);
            // 准备API: containerLimitMaterialLotQuery参数
            MtContLoadDtlVO10 mtContLoadDtlVO10 = new MtContLoadDtlVO10();
            mtContLoadDtlVO10.setContainerId(mtContainer.getContainerId());
            // 调用API: containerLimitMaterialLotQuery
            List<MtContLoadDtlVO4> mtContLoadDtlVO4s = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(mtContainer.getTenantId(), mtContLoadDtlVO10);
            List<String> materialLotIds = new ArrayList<>();
            for (MtContLoadDtlVO4 item : mtContLoadDtlVO4s) {
                materialLotIds.add(item.getMaterialLotId());
            }
            if(CollectionUtils.isEmpty(materialLotIds)){
                throw new MtException("容器" + containerCode + "物料批不存在!");
            }
            List<MtMaterialLot> mtMaterialLots = mtMaterialLotMapper.selectByMaterialLotId(tenantId, materialLotIds);
            List<String> materialLotCodes = new ArrayList<>();

            for (MtMaterialLot item:mtMaterialLots) {
                materialLotCodes.add(item.getMaterialLotCode());
            }
            // 校验WCS返回的容器与SN关系与WMS是否一致
            if (CollectionUtils.isEmpty(mtContLoadDtlVO4s) || mtContLoadDtlVO4s.size() != materialLotCodeList.size()) {
                throw new MtException("容器"+containerCode+"与条码关系不一致!");
            }
            if (!materialLotCodes.containsAll(materialLotCodeList)) {
                throw new MtException("容器"+containerCode+"与条码关系不一致!");
            }
            // 调用API : eventRequestCreate 获取 eventRequestId
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, requestTypeCode);
            if (StringUtils.isEmpty(eventRequestId)){
                throw new MtException("API:eventRequestCreate调用失败!");
            }
            // 调用API : eventCreate 获取 eventId
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode(eventTypeCode);
            mtEventCreateVO.setEventRequestId(eventRequestId);
            String eventId  = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
            // 准备API: containerTransfer的参数信息
            MtContainerVO7 mtContainerVO7 = new MtContainerVO7();
            mtContainerVO7.setContainerId(mtContainer.getContainerId());
            mtContainerVO7.setTargetSiteId(mtContainer.getSiteId());
            mtContainerVO7.setEventRequestId(eventRequestId);
            String parentLocatorId = modLocatorIfaceMapper.selectByParentLocatorId(mtContainer.getLocatorId());
            if(StringUtils.isEmpty(parentLocatorId)){
                throw new MtException("立库货位维护错误!");
            }
            // 获取targetLocatorId
            if ("IN".equals(type)) {
                List<String> locatorid = modLocatorIfaceMapper.selectByLocatorId(parentLocatorId, "AUTO");
                if (locatorid.size() != 1) {
                    throw new MtException("立库货位维护错误!");
                }
                mtContainerVO7.setTargetLocatorId(locatorid.get(0));
                // 调用API: containerTransfer
                mtContainerRepository.containerTransfer(mtContainer.getTenantId(), mtContainerVO7);
                // 根据物料批ID获取扩展表主键
                List<String> attrId = itfConcodeReturnIfaceMapper.selectByAttrId(tenantId, materialLotIds);
                // 将容器下所有物料批状态更新为INSTOCK-已入库
                // 调用 API : attrPropertyBatchUpdate
                ArrayList<MtCommonExtendVO5> mtCommonExtendVO5s = new ArrayList<>();
                MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
                mtCommonExtendVO5.setAttrName("STATUS");
                mtCommonExtendVO5.setAttrValue("INSTOCK");
                mtCommonExtendVO5s.add(mtCommonExtendVO5);
                List<MtCommonExtendVO6> mtCommonExtendVO6s = new ArrayList<>();
                for (int i = 0, x = materialLotIds.size(); i < x; i++) {
                    MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
                    mtCommonExtendVO6.setKeyId(materialLotIds.get(i));
                    mtCommonExtendVO6.setAttrs(mtCommonExtendVO5s);
                    mtCommonExtendVO6s.add(mtCommonExtendVO6);
                }
                mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, mtCommonExtendVO6s);
            } else if ("OUT".equals(type)) {
                List<String> locatorid = modLocatorIfaceMapper.selectByLocatorId(parentLocatorId, "AOUT");
                if (locatorid.size() != 1) {
                    throw new MtException("立库出库货位维护错误!");
                }
                mtContainerVO7.setTargetLocatorId(locatorid.get(0));
                // 调用API: containerTransfer
                mtContainerRepository.containerTransfer(mtContainer.getTenantId(), mtContainerVO7);
            } else {
                throw new MtException("立库出库货位维护错误!");
            }
            // 将容器编码和类型记录到接口表中，并将处理结果和返回消息记录到ITF_CONCODE_RETURN_IFACE表中，将容器和条码关系记录到
            List<String> headerId = customDbRepository.getNextKeys("itf_concode_return_iface_s", 1);
            List<String> cid = customDbRepository.getNextKeys("itf_concode_return_iface_cid_s", 1);
            ItfConcodeReturnIface itfConcodeReturnIface = new ItfConcodeReturnIface();
            itfConcodeReturnIface.setHeaderId(headerId.get(0));
            itfConcodeReturnIface.setContainerCode(containerCode);
            itfConcodeReturnIface.setType(type);
            itfConcodeReturnIface.setBatchId(Calendar.getInstance().getTimeInMillis());
            itfConcodeReturnIface.setCid(Long.parseLong(cid.get(0)));
            itfConcodeReturnIface.setStatus(WmsConstant.KEY_IFACE_STATUS_SUCCESS);
            itfConcodeReturnIface.setMessage(WmsConstant.KEY_IFACE_MESSAGE_SUCCESS);
            // itf_concode_return_iface接口表(新增信息记录)
            int insertByItfConcodeReturnIface = itfConcodeReturnIfaceMapper.insertByItfConcodeReturnIface(itfConcodeReturnIface);
            if (insertByItfConcodeReturnIface == 1) {
                List<ItfConcodeReturnLineIface> itfConcodeReturnLineIfaces = new ArrayList<>();
                List<String> lineId = customDbRepository.getNextKeys("itf_concode_return_line_iface_s", materialLotCodeList.size());
                List<String> linecid = customDbRepository.getNextKeys("itf_concode_return_line_iface_cid_s", materialLotCodeList.size());
                if(linecid.size()!=materialLotCodeList.size()||lineId.size()!=materialLotCodeList.size()){
                    throw new MtException("获取主键和cid失败!");
                }
                for (int i = 0, x = materialLotCodeList.size(); i < x; i++) {
                    ItfConcodeReturnLineIface itfConcodeReturnLineIface = new ItfConcodeReturnLineIface();
                    itfConcodeReturnLineIface.setLineId(lineId.get(i));
                    itfConcodeReturnLineIface.setHeaderId(headerId.get(0));
                    itfConcodeReturnLineIface.setCid(Long.parseLong(linecid.get(i)));
                    itfConcodeReturnLineIface.setMaterialLotCode(materialLotCodeList.get(i));
                    itfConcodeReturnLineIfaces.add(itfConcodeReturnLineIface);
                }
                // itf_concode_return_line_iface记录容器和条码关系记录(批量新增信息记录)
                itfConcodeReturnLineIfaceMapper.batchInsertItfConcodeReturnLineIface(itfConcodeReturnLineIfaces);
            }
            // API调用成功
            returnIfaceDTO.setContainerCode(containerCode);
            returnIfaceDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_SUCCESS);
            returnIfaceDTO.setProcessMessage(WmsConstant.KEY_IFACE_MESSAGE_SUCCESS);
            return returnIfaceDTO;
        } catch (Exception var1) {
            // 将容器编码和类型记录到接口表中，并将处理结果和返回消息记录到ITF_CONCODE_RETURN_IFACE表中，将容器和条码关系记录到
            List<String> headerId = customDbRepository.getNextKeys("itf_concode_return_iface_s", 1);
            List<String> cid = customDbRepository.getNextKeys("itf_concode_return_iface_cid_s", 1);
            ItfConcodeReturnIface itfConcodeReturnIface = new ItfConcodeReturnIface();
            itfConcodeReturnIface.setHeaderId(headerId.get(0));
            itfConcodeReturnIface.setContainerCode(containerCode);
            itfConcodeReturnIface.setType(type);
            itfConcodeReturnIface.setBatchId(Calendar.getInstance().getTimeInMillis());
            itfConcodeReturnIface.setCid(Long.parseLong(cid.get(0)));
            itfConcodeReturnIface.setStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            if(var1.getMessage().length()>140){
                itfConcodeReturnIface.setMessage(WmsConstant.KEY_IFACE_MESSAGE_ERROR);
            }else {
                itfConcodeReturnIface.setMessage(var1.getMessage());
            }
            // itf_concode_return_iface接口表(新增信息记录)
            int insertByItfConcodeReturnIface = itfConcodeReturnIfaceMapper.insertByItfConcodeReturnIface(itfConcodeReturnIface);
            if (insertByItfConcodeReturnIface == 1) {
                List<ItfConcodeReturnLineIface> itfConcodeReturnLineIfaces = new ArrayList<>();
                List<String> lineId = customDbRepository.getNextKeys("itf_concode_return_line_iface_s", materialLotCodeList.size());
                List<String> linecid = customDbRepository.getNextKeys("itf_concode_return_line_iface_cid_s", materialLotCodeList.size());
                for (int i = 0, x = materialLotCodeList.size(); i < x; i++) {
                    ItfConcodeReturnLineIface itfConcodeReturnLineIface = new ItfConcodeReturnLineIface();
                    itfConcodeReturnLineIface.setLineId(lineId.get(i));
                    itfConcodeReturnLineIface.setHeaderId(headerId.get(0));
                    itfConcodeReturnLineIface.setCid(Long.parseLong(linecid.get(i)));
                    itfConcodeReturnLineIface.setMaterialLotCode(materialLotCodeList.get(i));
                    itfConcodeReturnLineIfaces.add(itfConcodeReturnLineIface);
                }
                // itf_concode_return_line_iface记录容器和条码关系记录(批量新增信息记录)
                itfConcodeReturnLineIfaceMapper.batchInsertItfConcodeReturnLineIface(itfConcodeReturnLineIfaces);
            }
            returnIfaceDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            returnIfaceDTO.setProcessMessage(var1.getMessage());
            return returnIfaceDTO;
        }

    }
}
