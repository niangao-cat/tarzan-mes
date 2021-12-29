package com.ruike.itf.infra.repository.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.api.dto.*;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanLotDetailIface;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanOrPostIface;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanSnDetailIface;
import com.ruike.itf.domain.entity.ItfSoDeliveryLineChanIface;
import com.ruike.itf.domain.repository.ItfSoDeliveryChanLotDetailIfaceRepository;
import com.ruike.itf.domain.repository.ItfSoDeliveryChanOrPostIfaceRepository;
import com.ruike.itf.domain.repository.ItfSoDeliveryChanSnDetailIfaceRepository;
import com.ruike.itf.domain.repository.ItfSoDeliveryLineChanIfaceRepository;
import com.ruike.itf.domain.vo.*;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfSoDeliveryChanOrPostIfaceMapper;
import com.ruike.itf.utils.SendESBConnect;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO5;
import tarzan.instruction.domain.vo.MtInstructionVO2;
import tarzan.instruction.infra.mapper.MtInstructionDocMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 交货单修改过账接口头表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-09 16:58:10
 */
@Component
@Slf4j
public class ItfSoDeliveryChanOrPostIfaceRepositoryImpl extends BaseRepositoryImpl<ItfSoDeliveryChanOrPostIface> implements ItfSoDeliveryChanOrPostIfaceRepository {

    @Autowired
    private ItfSoDeliveryChanOrPostIfaceMapper itfSoDeliveryChanOrPostIfaceMapper;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private SendESBConnect sendESBConnect;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private ItfSoDeliveryLineChanIfaceRepository itfSoDeliveryLineChanIfaceRepository;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private ItfSoDeliveryChanLotDetailIfaceRepository itfSoDeliveryChanLotDetailIfaceRepository;
    @Autowired
    private ItfSoDeliveryChanSnDetailIfaceRepository itfSoDeliveryChanSnDetailIfaceRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtInstructionDocMapper mtInstructionDocMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ItfSoDeliveryChanOrPostDTO> soDeliveryChangeOrPostIface(Long tenantId, List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList) {
        List<ItfSoDeliveryChanOrPostDTO> resultList = new ArrayList<>();
        // 根据处理类型 分修改和过账
        Optional<ItfSoDeliveryChanOrPostDTO> firstOpt = itfSoDeliveryChanOrPostList.stream().filter(vo -> StringUtils.equals(vo.getType(), HmeConstants.ConstantValue.CHANGE)).findFirst();
        if (firstOpt.isPresent()) {
            resultList = this.modifyDelivery(tenantId, itfSoDeliveryChanOrPostList);
        } else {
            resultList = this.postDelivery(tenantId, itfSoDeliveryChanOrPostList);
        }
        return resultList;
    }

    @Override
    public void myBatchInsertSelective(List<ItfSoDeliveryChanOrPostIface> ifaceList) {
        List<String> sqlList = new ArrayList<>();
        for (ItfSoDeliveryChanOrPostIface iface : ifaceList) {
            sqlList.addAll(customDbRepository.getInsertSql(iface));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = CommonUtils.splitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
    }

    @Override
    public void myBatchUpdateSelective(List<ItfSoDeliveryChanOrPostIface> ifaceList) {
        List<String> sqlList = new ArrayList<>();
        for (ItfSoDeliveryChanOrPostIface iface : ifaceList) {
            sqlList.addAll(customDbRepository.getUpdateSql(iface));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = CommonUtils.splitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void postIfaceToESB(Long tenantId) {
        // 查询处理类型为POST 处理状态为N的交货单
        List<ItfSoDeliveryChanOrPostVO5> itfSoDeliveryChanOrPostVO5List = itfSoDeliveryChanOrPostIfaceMapper.queryPostSoDeliveryList(tenantId);
        List<ItfSoDeliveryChanOrPostVO4> headerDeliveryList = new ArrayList();
        for (ItfSoDeliveryChanOrPostVO5 itfSoDeliveryChanOrPostVO5 : itfSoDeliveryChanOrPostVO5List) {
            ItfSoDeliveryChanOrPostVO4 chanOrPostVO4 = new ItfSoDeliveryChanOrPostVO4();
            ItfSoDeliveryChanOrPostIface iface = new ItfSoDeliveryChanOrPostIface();
            iface.setHeaderId(itfSoDeliveryChanOrPostVO5.getHeaderId());
            iface.setDocNum(itfSoDeliveryChanOrPostVO5.getDocNum());
            iface.setSiteCode(itfSoDeliveryChanOrPostVO5.getSiteCode());
            iface.setType(itfSoDeliveryChanOrPostVO5.getType());
            chanOrPostVO4.setIface(iface);
            if (CollectionUtils.isNotEmpty(itfSoDeliveryChanOrPostVO5.getLineList())) {
                chanOrPostVO4.setLineList(itfSoDeliveryChanOrPostVO5.getLineList());
            }
            if (CollectionUtils.isNotEmpty(itfSoDeliveryChanOrPostVO5.getSnDetailIfaceList())) {
                chanOrPostVO4.setSnDetailIfaceList(itfSoDeliveryChanOrPostVO5.getSnDetailIfaceList());
            }
            if (CollectionUtils.isNotEmpty(itfSoDeliveryChanOrPostVO5.getLotDetailIfaceList())) {
                chanOrPostVO4.setLotDetailIfaceList(itfSoDeliveryChanOrPostVO5.getLotDetailIfaceList());
            }
            headerDeliveryList.add(chanOrPostVO4);
        }
        if (CollectionUtils.isNotEmpty(headerDeliveryList)) {
            // 组装数据 同步接口
            this.assemblySynchronousESB(tenantId, headerDeliveryList);
        }
    }

    /**
     * 修改单据头及行
     *
     * @param tenantId
     * @param itfSoDeliveryChanOrPostList
     * @return java.util.List<com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO>
     * @author sanfeng.zhang@hand-china.com 2021/7/12
     */
    private List<ItfSoDeliveryChanOrPostDTO> modifyDelivery (Long tenantId, List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList) {
        List<ItfSoDeliveryChanOrPostDTO> resultList = new ArrayList<>();
        // 分单据头取消和单据行修改
        List<ItfSoDeliveryChanOrPostDTO> changeInstructionDocList = itfSoDeliveryChanOrPostList.stream().filter(vo -> HmeConstants.ConstantValue.CANCEL.equals(vo.getHReturnStatus())).collect(Collectors.toList());
        List<ItfSoDeliveryChanOrPostDTO> changeInstructionList = itfSoDeliveryChanOrPostList.stream().filter(vo -> !HmeConstants.ConstantValue.CANCEL.equals(vo.getHReturnStatus()) && StringUtils.isNotBlank(vo.getInstructionId())).collect(Collectors.toList());
        Long batchId = Long.valueOf(this.customDbRepository.getNextKey("itf_so_delivery_chan_or_post_iface_cid_s"));
        if (CollectionUtils.isNotEmpty(changeInstructionDocList)) {
            resultList = this.changeInstructionDocList(tenantId, changeInstructionDocList, batchId);
        }

        if (CollectionUtils.isNotEmpty(changeInstructionList)) {
            resultList = this.changeInstructionList(tenantId, changeInstructionList, batchId);
        }
        return resultList;
    }

    /**
     * 修改单据头
     * @param tenantId
     * @param itfSoDeliveryChanOrPostList
     * @return java.util.List<com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO>
     * @author sanfeng.zhang@hand-china.com 2021/7/12
     */
    private List<ItfSoDeliveryChanOrPostDTO> changeInstructionDocList(Long tenantId, List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList, Long batchId) {
        // 记录接口表ITF_SO_DELIVERY_CHAN_OR_POST_IFACE
        // 批量获取主键和cid
        List<String> ids = customDbRepository.getNextKeys("itf_so_delivery_chan_or_post_iface_s", itfSoDeliveryChanOrPostList.size());
        List<String> cids = customDbRepository.getNextKeys("itf_so_delivery_chan_or_post_iface_cid_s", itfSoDeliveryChanOrPostList.size());
        Integer indexNum = 0;
        Date currentDate = CommonUtils.currentTimeGet();
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        List<ItfSoDeliveryChanOrPostIface> ifaceList = new ArrayList<>();
        // 根据id批量获取单据信息
        List<String> instructionDocIdList = itfSoDeliveryChanOrPostList.stream().map(ItfSoDeliveryChanOrPostDTO::getInstructionDocId).distinct().collect(Collectors.toList());
        Map<String, List<ItfSoDeliveryChanOrPostDTO2>> mtInstructionDocMap = this.batchQueryInstruction(tenantId, instructionDocIdList);
        for (ItfSoDeliveryChanOrPostDTO itfSoDeliveryChanOrPostDTO : itfSoDeliveryChanOrPostList) {
            ItfSoDeliveryChanOrPostIface iface = new ItfSoDeliveryChanOrPostIface();
            iface.setHeaderId(ids.get(indexNum));
            List<ItfSoDeliveryChanOrPostDTO2> instructionDocs = mtInstructionDocMap.getOrDefault(itfSoDeliveryChanOrPostDTO.getInstructionDocId(), Collections.emptyList());
            if (CollectionUtils.isNotEmpty(instructionDocs)) {
                iface.setDocNum(instructionDocs.get(0).getInstructionDocNum());
                iface.setSiteCode(instructionDocs.get(0).getSiteCode());
            }
            iface.sethReturnStatus(itfSoDeliveryChanOrPostDTO.getHReturnStatus());
            iface.setType(itfSoDeliveryChanOrPostDTO.getType());
            iface.setTenantId(tenantId);
            iface.setCid(Long.valueOf(cids.get(indexNum++)));
            iface.setCreationDate(currentDate);
            iface.setCreatedBy(userId);
            iface.setLastUpdateDate(currentDate);
            iface.setObjectVersionNumber(1L);
            iface.setLastUpdatedBy(userId);
            iface.setBatchId(batchId);
            iface.setProcessDate(currentDate);
            iface.setProcessStatus(HmeConstants.ConstantValue.NO);
            ifaceList.add(iface);
        }
        this.myBatchInsertSelective(ifaceList);

        List<ItfSoDeliveryChanOrPostIface> updateList = new ArrayList<>();
        List<LovValueDTO> internalFlagList = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_INTERNAL_FLAG, tenantId);
        if (CollectionUtils.isEmpty(internalFlagList)){
            throw new CommonException(ItfConstant.LovCode.ITF_INTERNAL_FLAG + "值集没有维护\n含义值为Y或N【Y为实时发送，N为不实时发送】");
        }
        Optional<LovValueDTO> lovFlag = internalFlagList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "TIMELY_INTERFACE_FLAG")).findFirst();
        String interfaceFlag = lovFlag.isPresent() ? lovFlag.get().getMeaning() : "";
        List<ItfSoDeliveryChanOrPostVO4> chanOrPostVO4List = new ArrayList<>();
        for (ItfSoDeliveryChanOrPostIface iface : ifaceList) {
            ItfSoDeliveryChanOrPostVO4 chanOrPostVO4 = new ItfSoDeliveryChanOrPostVO4();
            chanOrPostVO4.setIface(iface);
            chanOrPostVO4List.add(chanOrPostVO4);
        }
        if (HmeConstants.ConstantValue.YES.equals(interfaceFlag)) {
            Map<String, Object> requestInfo = batchHandleRequestInfo(chanOrPostVO4List);
            // 调用SAP发货通知单修改回传&拣配接口
            Map<String, Object> map = sendESBConnect.sendEsb(requestInfo, "",
                    "ItfSoDeliveryChanOrPostIfaceRepositoryImpl.soDeliveryChangeOrPostIface", ItfConstant.InterfaceCode.ESB_DELIVERY_ORDER_PICK_UPDATE);
            log.info("sendESBConnect return data:{}", JSON.toJSONString(map));
            String aReturn = JSON.toJSON(map.get("RETURN")).toString();
            List<ItfSoDeliveryChanOrPostDTO4> resultDTOs = JSONArray.parseArray(aReturn, ItfSoDeliveryChanOrPostDTO4.class);
            Map<String, List<ItfSoDeliveryChanOrPostIface>> headerMap = ifaceList.stream().collect(Collectors.groupingBy(ItfSoDeliveryChanOrPostIface::getDocNum));
            if (CollectionUtils.isNotEmpty(resultDTOs)) {
                for (ItfSoDeliveryChanOrPostDTO4 resultDTO : resultDTOs) {
                    String docNum = resultDTO.getVBELN().replaceAll("^(0+)", "");
                    List<ItfSoDeliveryChanOrPostIface> ifaces = headerMap.get(docNum);
                    if (CollectionUtils.isNotEmpty(ifaces)) {
                        ItfSoDeliveryChanOrPostIface updateIface = new ItfSoDeliveryChanOrPostIface();
                        updateIface.setHeaderId(ifaces.get(0).getHeaderId());
                        updateIface.setProcessStatus(resultDTO.getTYPE());
                        updateIface.setProcessMessage(resultDTO.getMESSAGE());
                        updateList.add(updateIface);
                        MtInstructionDoc doc = mtInstructionDocMapper.selectByNum(tenantId,docNum);
                        itfSoDeliveryChanOrPostList.stream().filter(item ->item.getInstructionDocId().equals(doc.getInstructionDocId())).collect(Collectors.toList()).get(0).setStatus(resultDTO.getTYPE());
                        itfSoDeliveryChanOrPostList.stream().filter(item ->item.getInstructionDocId().equals(doc.getInstructionDocId())).collect(Collectors.toList()).get(0).setMessage(resultDTO.getMESSAGE());
                    }
                }
            }
        }
        // 根据sap返回结果 同步接口表STATUS、MESSAGE
        if (CollectionUtils.isNotEmpty(updateList)) {
            List<List<ItfSoDeliveryChanOrPostIface>> lists = CommonUtils.splitSqlList(updateList, 500);
            for (List<ItfSoDeliveryChanOrPostIface> splitList : lists) {
                this.myBatchUpdateSelective(splitList);
            }
        }
        return itfSoDeliveryChanOrPostList;
    }

    private Map<String, List<ItfSoDeliveryChanOrPostDTO2>> batchQueryInstruction(Long tenantId, List<String> instructionDocIdList) {
        Map<String, List<ItfSoDeliveryChanOrPostDTO2>> mtInstructionDocMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(instructionDocIdList)) {
            List<ItfSoDeliveryChanOrPostDTO2> instructionDocs = itfSoDeliveryChanOrPostIfaceMapper.instructionDocPropertyBatchGet(tenantId, instructionDocIdList);
            mtInstructionDocMap = instructionDocs.stream().collect(Collectors.groupingBy(ItfSoDeliveryChanOrPostDTO2::getInstructionDocId));
        }
        return mtInstructionDocMap;
    }

    private Map<String, List<ItfSoDeliveryChanOrPostDTO3>> instructionPropertyBatchGet(Long tenantId, List<String> instructionIdList) {
        Map<String, List<ItfSoDeliveryChanOrPostDTO3>> mtInstructionMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(instructionIdList)) {
            List<ItfSoDeliveryChanOrPostDTO3> instructionList = itfSoDeliveryChanOrPostIfaceMapper.instructionPropertyBatchGet(tenantId, instructionIdList);
            mtInstructionMap = instructionList.stream().collect(Collectors.groupingBy(ItfSoDeliveryChanOrPostDTO3::getInstructionId));
        }
        return mtInstructionMap;
    }

    private Map<String, List<MtModLocator>> locatorPropertyBatchGet(Long tenantId, List<String> locatorIdList) {
        Map<String, List<MtModLocator>> resultMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(locatorIdList)) {
            List<MtModLocator> mtModLocators = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class).select(MtModLocator.FIELD_LOCATOR_ID, MtModLocator.FIELD_LOCATOR_CODE).andWhere(Sqls.custom()
                    .andIn(MtModLocator.FIELD_LOCATOR_ID, locatorIdList)).build());
            resultMap = mtModLocators.stream().collect(Collectors.groupingBy(MtModLocator::getLocatorId));
        }
        return resultMap;
    }


    /**
     * 修改单据行
     * @param tenantId
     * @param itfSoDeliveryChanOrPostList
     * @return java.util.List<com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO>
     * @author sanfeng.zhang@hand-china.com 2021/7/12
     */
    private List<ItfSoDeliveryChanOrPostDTO> changeInstructionList(Long tenantId, List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList, Long batchId) {
        // 按单据头进行分组
        Map<String, List<ItfSoDeliveryChanOrPostDTO>> instructionDocDeliveryMap = itfSoDeliveryChanOrPostList.stream().collect(Collectors.groupingBy(ItfSoDeliveryChanOrPostDTO::getInstructionDocId));

        // 批量获取主键和cid
        List<String> headerIds = customDbRepository.getNextKeys("itf_so_delivery_chan_or_post_iface_s", instructionDocDeliveryMap.size());
        List<String> headerCids = customDbRepository.getNextKeys("itf_so_delivery_chan_or_post_iface_cid_s", instructionDocDeliveryMap.size());
        List<String> lineIds = customDbRepository.getNextKeys("itf_so_delivery_line_chan_iface_s", itfSoDeliveryChanOrPostList.size());
        List<String> lineCids = customDbRepository.getNextKeys("itf_so_delivery_line_chan_iface_cid_s", itfSoDeliveryChanOrPostList.size());
        // 批量查询单据头
        List<String> instructionDocIdList = itfSoDeliveryChanOrPostList.stream().map(ItfSoDeliveryChanOrPostDTO::getInstructionDocId).distinct().collect(Collectors.toList());
        Map<String, List<ItfSoDeliveryChanOrPostDTO2>> mtInstructionDocMap = this.batchQueryInstruction(tenantId, instructionDocIdList);
        // 批量查询单据行
        List<String> instructionIdList = itfSoDeliveryChanOrPostList.stream().map(ItfSoDeliveryChanOrPostDTO::getInstructionId).distinct().collect(Collectors.toList());
        Map<String, List<ItfSoDeliveryChanOrPostDTO3>> mtInstructionMap = this.instructionPropertyBatchGet(tenantId, instructionIdList);
        // 批量查询仓库
        List<String> locatorIdList = itfSoDeliveryChanOrPostList.stream().map(ItfSoDeliveryChanOrPostDTO::getWarehouseId).distinct().collect(Collectors.toList());
        Map<String, List<MtModLocator>> locatorMap = this.locatorPropertyBatchGet(tenantId, locatorIdList);
        Integer docIndex = 0;
        Integer lineIndex = 0;
        Date currentDate = CommonUtils.currentTimeGet();
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        List<ItfSoDeliveryChanOrPostIface> ifaceList = new ArrayList<>();
        List<ItfSoDeliveryLineChanIface> lineIfaceList = new ArrayList<>();
        for (Map.Entry<String, List<ItfSoDeliveryChanOrPostDTO>> instructionDocDeliveryEntry : instructionDocDeliveryMap.entrySet()) {
            List<ItfSoDeliveryChanOrPostDTO> lineValueList = instructionDocDeliveryEntry.getValue();
            // 插入单据头信息
            ItfSoDeliveryChanOrPostIface iface = new ItfSoDeliveryChanOrPostIface();
            iface.setHeaderId(headerIds.get(docIndex));
            List<ItfSoDeliveryChanOrPostDTO2> instructionDocs = mtInstructionDocMap.getOrDefault(instructionDocDeliveryEntry.getKey(), Collections.emptyList());
            if (CollectionUtils.isNotEmpty(instructionDocs)) {
                iface.setDocNum(instructionDocs.get(0).getInstructionDocNum());
                iface.setSiteCode(instructionDocs.get(0).getSiteCode());
            }
            iface.setType(lineValueList.get(0).getType());
            iface.setTenantId(tenantId);
            iface.setCid(Long.valueOf(headerCids.get(docIndex++)));
            iface.setCreationDate(currentDate);
            iface.setCreatedBy(userId);
            iface.setLastUpdateDate(currentDate);
            iface.setLastUpdatedBy(userId);
            iface.setObjectVersionNumber(1L);
            iface.setBatchId(batchId);
            iface.setProcessDate(currentDate);
            iface.setProcessStatus(HmeConstants.ConstantValue.NO);
            ifaceList.add(iface);

            // 插入行信息
            for (ItfSoDeliveryChanOrPostDTO itfSoDeliveryChanOrPostDTO : lineValueList) {
                ItfSoDeliveryLineChanIface lineChanIface = new ItfSoDeliveryLineChanIface();
                lineChanIface.setLineId(lineIds.get(lineIndex));
                lineChanIface.setHeaderId(iface.getHeaderId());
                lineChanIface.setDocNum(iface.getDocNum());
                List<ItfSoDeliveryChanOrPostDTO3> lineList = mtInstructionMap.getOrDefault(itfSoDeliveryChanOrPostDTO.getInstructionId(), Collections.emptyList());
                if (CollectionUtils.isNotEmpty(lineList)) {
                    lineChanIface.setDocLineNum(lineList.get(0).getInstructionLineNum());
                    lineChanIface.setMaterialCode(lineList.get(0).getMaterialCode());
                    lineChanIface.setUomCode(lineList.get(0).getUomCode());
                }
                List<MtModLocator> mtModLocators = locatorMap.get(itfSoDeliveryChanOrPostDTO.getWarehouseId());
                if (CollectionUtils.isNotEmpty(mtModLocators)) {
                    lineChanIface.setWarehouseCode(mtModLocators.get(0).getLocatorCode());
                }
                lineChanIface.setChangeQuantity(itfSoDeliveryChanOrPostDTO.getChangeQty());
                lineChanIface.setlReturnStatus(itfSoDeliveryChanOrPostDTO.getLReturnStatus());
                lineChanIface.setSiteCode(iface.getSiteCode());
                lineChanIface.setTenantId(tenantId);
                lineChanIface.setCid(Long.valueOf(lineCids.get(lineIndex++)));
                lineChanIface.setCreationDate(currentDate);
                lineChanIface.setCreatedBy(userId);
                lineChanIface.setLastUpdateDate(currentDate);
                lineChanIface.setLastUpdatedBy(userId);
                lineChanIface.setObjectVersionNumber(1L);
                lineChanIface.setBatchId(batchId);
                lineChanIface.setProcessDate(currentDate);
                lineChanIface.setProcessStatus(HmeConstants.ConstantValue.NO);
                lineIfaceList.add(lineChanIface);
            }
        }
        // 接口表ITF_SO_DELIVERY_CHAN_OR_POST_IFACE记录
        if (CollectionUtils.isNotEmpty(ifaceList)) {
            List<List<ItfSoDeliveryChanOrPostIface>> lists = CommonUtils.splitSqlList(ifaceList, 500);
            for (List<ItfSoDeliveryChanOrPostIface> splitList : lists) {
                this.myBatchInsertSelective(splitList);
            }
        }
        // 接口表ITF_SO_DELIVERY_LINE_CHAN_IFACE记录
        if (CollectionUtils.isNotEmpty(lineIfaceList)) {
            List<List<ItfSoDeliveryLineChanIface>> lists = CommonUtils.splitSqlList(lineIfaceList, 500);
            for (List<ItfSoDeliveryLineChanIface> splitList : lists) {
                itfSoDeliveryLineChanIfaceRepository.myBatchInsert(splitList);
            }
        }
        List<ItfSoDeliveryChanOrPostIface> updateHeaderList = new ArrayList();
        List<ItfSoDeliveryLineChanIface> updateLineList = new ArrayList<>();
        List<LovValueDTO> internalFlagList = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_INTERNAL_FLAG, tenantId);
        if (CollectionUtils.isEmpty(internalFlagList)){
            throw new CommonException(ItfConstant.LovCode.ITF_INTERNAL_FLAG + "值集没有维护\n含义值为Y或N【Y为实时发送，N为不实时发送】");
        }
        Optional<LovValueDTO> lovFlag = internalFlagList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "TIMELY_INTERFACE_FLAG")).findFirst();
        String interfaceFlag = lovFlag.isPresent() ? lovFlag.get().getMeaning() : "";
        // 1.2.2.3 调用SAP发货通知单修改回传&拣配接口
        List<ItfSoDeliveryChanOrPostVO4> chanOrPostVO4List = new ArrayList<>();
        if (HmeConstants.ConstantValue.YES.equals(interfaceFlag)) {
            Map<String, List<ItfSoDeliveryLineChanIface>> lineChanIfaceMap = lineIfaceList.stream().collect(Collectors.groupingBy(ItfSoDeliveryLineChanIface::getHeaderId));
            for (ItfSoDeliveryChanOrPostIface iface : ifaceList) {
                ItfSoDeliveryChanOrPostVO4 chanOrPostVO4 = new ItfSoDeliveryChanOrPostVO4();
                chanOrPostVO4.setIface(iface);
                List<ItfSoDeliveryLineChanIface> lineList = lineChanIfaceMap.getOrDefault(iface.getHeaderId(), Collections.emptyList());
                chanOrPostVO4.setLineList(lineList);
                chanOrPostVO4List.add(chanOrPostVO4);
            }
            // 调用SAP发货通知单修改回传&拣配接口
            Map<String, Object> requestInfo = this.batchHandleRequestInfo(chanOrPostVO4List);
            Map<String, Object> map = sendESBConnect.sendEsb(requestInfo, "",
                    "ItfSoDeliveryChanOrPostIfaceRepositoryImpl.soDeliveryChangeOrPostIface", ItfConstant.InterfaceCode.ESB_DELIVERY_ORDER_PICK_UPDATE);
            String aReturn = JSON.toJSON(map.get("RETURN")).toString();
            List<ItfSoDeliveryChanOrPostDTO4> resultDTOs = JSONArray.parseArray(aReturn, ItfSoDeliveryChanOrPostDTO4.class);
            Map<String, List<ItfSoDeliveryChanOrPostIface>> headerMap = ifaceList.stream().collect(Collectors.groupingBy(ItfSoDeliveryChanOrPostIface::getDocNum));
            if (CollectionUtils.isNotEmpty(resultDTOs)) {
                for (ItfSoDeliveryChanOrPostDTO4 resultDTO : resultDTOs) {
                    // sap 单据固定了位置  不足会补0 去掉多余的0
                    String docNum = resultDTO.getVBELN().replaceAll("^(0+)", "");
                    List<ItfSoDeliveryChanOrPostIface> ifaces = headerMap.get(docNum);
                    if (CollectionUtils.isNotEmpty(ifaces)) {
                        ItfSoDeliveryChanOrPostIface updateIface = new ItfSoDeliveryChanOrPostIface();
                        updateIface.setHeaderId(ifaces.get(0).getHeaderId());
                        updateIface.setProcessStatus(resultDTO.getTYPE());
                        updateIface.setProcessMessage(resultDTO.getMESSAGE());
                        updateHeaderList.add(updateIface);
                        MtInstructionDoc doc = mtInstructionDocMapper.selectByNum(tenantId,docNum);
                        itfSoDeliveryChanOrPostList.stream().filter(item ->item.getInstructionDocId().equals(doc.getInstructionDocId())).collect(Collectors.toList()).get(0).setStatus(updateIface.getProcessStatus());
                        itfSoDeliveryChanOrPostList.stream().filter(item ->item.getInstructionDocId().equals(doc.getInstructionDocId())).collect(Collectors.toList()).get(0).setMessage(updateIface.getProcessMessage());
                        List<ItfSoDeliveryLineChanIface> lineList = lineChanIfaceMap.getOrDefault(ifaces.get(0).getHeaderId(), Collections.emptyList());
                        lineList.stream().forEach(line -> {
                            line.setProcessStatus(updateIface.getProcessStatus());
                            line.setProcessMessage(updateIface.getProcessMessage());
                        });
                        if (CollectionUtils.isNotEmpty(lineList)) {
                            updateLineList.addAll(lineList);
                        }
                    }
                }
            }
        }
        // ITF_SO_DELIVERY_CHAN_OR_POST_IFACE和ITF_SO_DELIVERY_LINE_CHAN_IFACE的STATUS、MESSAGE中
        if (CollectionUtils.isNotEmpty(updateHeaderList)) {
            List<List<ItfSoDeliveryChanOrPostIface>> lists = CommonUtils.splitSqlList(updateHeaderList, 500);
            for (List<ItfSoDeliveryChanOrPostIface> splitList : lists) {
                this.myBatchUpdateSelective(splitList);
            }
        }
        if (CollectionUtils.isNotEmpty(updateLineList)) {
            List<List<ItfSoDeliveryLineChanIface>> lists = CommonUtils.splitSqlList(updateLineList, 500);
            for (List<ItfSoDeliveryLineChanIface> splitList : lists) {
                itfSoDeliveryLineChanIfaceRepository.myBatchUpdateSelective(splitList);
            }
        }
        return itfSoDeliveryChanOrPostList;
    }
    
    /** 
     * 过账
     *
     * @param tenantId
     * @param itfSoDeliveryChanOrPostList
     * @return java.util.List<com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO>
     * @author sanfeng.zhang@hand-china.com 2021/7/12  
     */
    private List<ItfSoDeliveryChanOrPostDTO> postDelivery(Long tenantId, List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList) {
        List<ItfSoDeliveryChanOrPostDTO> resultList = new ArrayList<>();
        Long batchId = Long.valueOf(this.customDbRepository.getNextKey("itf_so_delivery_chan_or_post_iface_cid_s"));

        // 批量获取主键和cid
        List<String> headerIds = customDbRepository.getNextKeys("itf_so_delivery_chan_or_post_iface_s", itfSoDeliveryChanOrPostList.size());
        List<String> headerCids = customDbRepository.getNextKeys("itf_so_delivery_chan_or_post_iface_cid_s", itfSoDeliveryChanOrPostList.size());

        // 批量查询单据头
        List<String> instructionDocIdList = itfSoDeliveryChanOrPostList.stream().map(ItfSoDeliveryChanOrPostDTO::getInstructionDocId).distinct().collect(Collectors.toList());
        Map<String, List<ItfSoDeliveryChanOrPostDTO2>> mtInstructionDocMap = this.batchQueryInstruction(tenantId, instructionDocIdList);
        List<ItfSoDeliveryChanOrPostIface> headerList = new ArrayList<>();
        List<ItfSoDeliveryLineChanIface> lineIfaceList = new ArrayList();
        List<ItfSoDeliveryChanLotDetailIface> lotDetailIfaceList = new ArrayList();
        List<ItfSoDeliveryChanSnDetailIface> snDetailIfaceList = new ArrayList();
        List<ItfSoDeliveryChanOrPostVO4> headerDeliveryList = new ArrayList<>();
        Integer docIndex = 0;
        Date currentDate = CommonUtils.currentTimeGet();
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        for (ItfSoDeliveryChanOrPostDTO itfSoDeliveryChanOrPostDTO : itfSoDeliveryChanOrPostList) {
            // 根据单据获取单据行信息，调用API：instructionDocLimitInstructionAndActualQuery
            MtInstructionDocVO5 mtInstructionDocVO5 = mtInstructionDocRepository.instructionDocLimitInstructionAndActualQuery(tenantId, itfSoDeliveryChanOrPostDTO.getInstructionDocId());
            //筛选掉取消状态的行
            List<MtInstructionVO2> instructionMessageList = mtInstructionDocVO5.getInstructionMessageList();
            List<MtInstructionVO2> mtInstructionVO2List = instructionMessageList.stream().filter(item ->!item.getInstructionStatus().equals("CANCEL")).collect(Collectors.toList());
            mtInstructionDocVO5.setInstructionMessageList(mtInstructionVO2List);
            ItfSoDeliveryChanOrPostVO4 chanOrPostVO4 = new ItfSoDeliveryChanOrPostVO4();
            // 记录接口表ITF_SO_DELIVERY_CHAN_OR_POST_IFACE
            ItfSoDeliveryChanOrPostIface iface = new ItfSoDeliveryChanOrPostIface();
            iface.setHeaderId(headerIds.get(docIndex));
            List<ItfSoDeliveryChanOrPostDTO2> instructionDocs = mtInstructionDocMap.getOrDefault(itfSoDeliveryChanOrPostDTO.getInstructionDocId(), Collections.emptyList());
            if (CollectionUtils.isNotEmpty(instructionDocs)) {
                iface.setDocNum(instructionDocs.get(0).getInstructionDocNum());
                iface.setSiteCode(instructionDocs.get(0).getSiteCode());
            }
            iface.setType(HmeConstants.ConstantValue.POST);
            iface.setProcessStatus(HmeConstants.ConstantValue.NO);
            iface.setTenantId(tenantId);
            iface.setCid(Long.valueOf(headerCids.get(docIndex++)));
            iface.setCreationDate(currentDate);
            iface.setCreatedBy(userId);
            iface.setLastUpdateDate(currentDate);
            iface.setLastUpdatedBy(userId);
            iface.setObjectVersionNumber(1L);
            iface.setBatchId(batchId);
            iface.setProcessDate(currentDate);
            iface.setProcessStatus(HmeConstants.ConstantValue.NO);
            headerList.add(iface);
            chanOrPostVO4.setIface(iface);

            // 根据物料id和站点id获取物料类型
            List<String> materialIdList = mtInstructionDocVO5.getInstructionMessageList().stream().map(MtInstructionVO2::getMaterialId).distinct().collect(Collectors.toList());
            Map<String, List<ItfSoDeliveryChanOrPostVO>> materialTypeMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(materialIdList)) {
                List<ItfSoDeliveryChanOrPostVO> materialTypeList = itfSoDeliveryChanOrPostIfaceMapper.queryMaterialTypeByMaterialIdsAndSiteId(tenantId, materialIdList, mtInstructionDocVO5.getSiteId());
                materialTypeMap = materialTypeList.stream().collect(Collectors.groupingBy(ItfSoDeliveryChanOrPostVO::getMaterialId));
            }
            // 批量查询单据行
            List<String> instructionIdList = mtInstructionDocVO5.getInstructionMessageList().stream().map(MtInstructionVO2::getInstructionId).distinct().collect(Collectors.toList());
            Map<String, List<ItfSoDeliveryChanOrPostDTO3>> mtInstructionMap = this.instructionPropertyBatchGet(tenantId, instructionIdList);
            // 拆分批次 从00001开始
            Integer instructionIndex = 1;
            // 获取指令实际数量
            Map<String, List<ItfSoDeliveryChanOrPostVO3>> actualQtyMap = this.instructionActualQtyBatchGet(tenantId, instructionIdList);
            // 交货单修改&过账头 行及明细
            List<ItfSoDeliveryLineChanIface> headerLineList = new ArrayList<>();
            List<ItfSoDeliveryChanLotDetailIface> headerLotDetailIfaceList = new ArrayList<>();
            List<ItfSoDeliveryChanSnDetailIface> headerSnDetailIfaceList = new ArrayList<>();
            for (MtInstructionVO2 mtInstructionVO2 : mtInstructionDocVO5.getInstructionMessageList()) {
                List<ItfSoDeliveryChanOrPostDTO3> lineList = mtInstructionMap.getOrDefault(mtInstructionVO2.getInstructionId(), Collections.emptyList());
                ItfSoDeliveryChanOrPostDTO3 instructionInfo = lineList.get(0);
                List<ItfSoDeliveryChanOrPostVO3> actualQtyList = actualQtyMap.get(mtInstructionVO2.getInstructionId());
                BigDecimal actualQty = CollectionUtils.isNotEmpty(actualQtyList) ? actualQtyList.get(0).getActualQty() : BigDecimal.ZERO;
                List<ItfSoDeliveryChanOrPostVO> materialTypes = materialTypeMap.get(mtInstructionVO2.getMaterialId());
                String materialType = CollectionUtils.isNotEmpty(materialTypes) ? materialTypes.get(0).getItemType() : "";
                // 物料类型分为RK06和非RK06
                ItfSoDeliveryChanOrPostVO2 itfSoDeliveryChanOrPostVO2 = new ItfSoDeliveryChanOrPostVO2();
                if (HmeConstants.ItemGroup.RK06.equals(materialType)) {
                    itfSoDeliveryChanOrPostVO2 = this.handleLineChanIface(tenantId, iface, instructionInfo, instructionIndex, userId, currentDate, actualQty, batchId, HmeConstants.ConstantValue.STRING_ZERO, mtInstructionDocVO5.getInstructionDocType());
                } else {
                    // LOT_FLAG 等于X及非X
                    String lotFlag = CollectionUtils.isNotEmpty(lineList) ? lineList.get(0).getLotFlag() : "";
                    if (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(lotFlag)) {
                        itfSoDeliveryChanOrPostVO2 = this.handleLineChanIface(tenantId, iface, instructionInfo, instructionIndex, userId, currentDate, actualQty, batchId, HmeConstants.ConstantValue.STRING_ONE, mtInstructionDocVO5.getInstructionDocType());
                    } else {
                        itfSoDeliveryChanOrPostVO2 = this.handleLineChanIface(tenantId, iface, instructionInfo, instructionIndex, userId, currentDate, actualQty, batchId, HmeConstants.ConstantValue.STRING_TWO, mtInstructionDocVO5.getInstructionDocType());
                    }
                }
                // 批次起始赋值(实参 形参问题)
                instructionIndex = itfSoDeliveryChanOrPostVO2.getInstructionIndex();
                if (itfSoDeliveryChanOrPostVO2.getLineChanIface() != null) {
                    lineIfaceList.add(itfSoDeliveryChanOrPostVO2.getLineChanIface());
                    headerLineList.add(itfSoDeliveryChanOrPostVO2.getLineChanIface());
                }
                if (CollectionUtils.isNotEmpty(itfSoDeliveryChanOrPostVO2.getLotDetailIfaceList())){
                    lotDetailIfaceList.addAll(itfSoDeliveryChanOrPostVO2.getLotDetailIfaceList());
                    headerLotDetailIfaceList.addAll(itfSoDeliveryChanOrPostVO2.getLotDetailIfaceList());
                }
                if (CollectionUtils.isNotEmpty(itfSoDeliveryChanOrPostVO2.getSnDetailIfaceList())){
                    snDetailIfaceList.addAll(itfSoDeliveryChanOrPostVO2.getSnDetailIfaceList());
                    headerSnDetailIfaceList.addAll(itfSoDeliveryChanOrPostVO2.getSnDetailIfaceList());
                }
            }
            chanOrPostVO4.setLineList(headerLineList);
            chanOrPostVO4.setLotDetailIfaceList(headerLotDetailIfaceList);
            chanOrPostVO4.setSnDetailIfaceList(headerSnDetailIfaceList);
            headerDeliveryList.add(chanOrPostVO4);
        }
        if (CollectionUtils.isNotEmpty(headerList)) {
            List<List<ItfSoDeliveryChanOrPostIface>> lists = CommonUtils.splitSqlList(headerList, 500);
            for (List<ItfSoDeliveryChanOrPostIface> splitList : lists) {
                this.myBatchInsertSelective(splitList);
            }
        }
        if (CollectionUtils.isNotEmpty(lineIfaceList)) {
            List<List<ItfSoDeliveryLineChanIface>> lists = CommonUtils.splitSqlList(lineIfaceList, 500);
            for (List<ItfSoDeliveryLineChanIface> splitList : lists) {
                itfSoDeliveryLineChanIfaceRepository.myBatchInsert(splitList);
            }
        }
        if (CollectionUtils.isNotEmpty(lotDetailIfaceList)) {
            List<List<ItfSoDeliveryChanLotDetailIface>> lists = CommonUtils.splitSqlList(lotDetailIfaceList, 500);
            for (List<ItfSoDeliveryChanLotDetailIface> splitList : lists) {
                itfSoDeliveryChanLotDetailIfaceRepository.myBatchInsert(splitList);
            }
        }
        if (CollectionUtils.isNotEmpty(snDetailIfaceList)) {
            for(ItfSoDeliveryChanSnDetailIface itfSoDeliveryChanSnDetailIface:snDetailIfaceList){
                String[] codeArray = itfSoDeliveryChanSnDetailIface.getSn().split(" ");
                if(codeArray.length != 1){
                    itfSoDeliveryChanSnDetailIface.setSn(codeArray[1]);
                }
            }
            List<List<ItfSoDeliveryChanSnDetailIface>> lists = CommonUtils.splitSqlList(snDetailIfaceList, 500);
            for (List<ItfSoDeliveryChanSnDetailIface> splitList : lists) {
                itfSoDeliveryChanSnDetailIfaceRepository.myBatchInsert(splitList);
            }
        }
        // 过账同步SRM接口
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG, tenantId);
        if (CollectionUtils.isEmpty(lovValueDTOS)) {
            throw new CommonException(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG + "值集没有维护\n含义值为Y或N【Y为实时发送，N为不实时发送】");
        }
        if (HmeConstants.ConstantValue.YES.equals(lovValueDTOS.get(0).getMeaning())) {
            // 组装数据 同步接口
            resultList = this.assemblySynchronousESB(tenantId, headerDeliveryList);
        }
        return resultList;
    }

    private List<ItfSoDeliveryChanOrPostDTO> assemblySynchronousESB(Long tenantId, List<ItfSoDeliveryChanOrPostVO4> headerDeliveryList) {
        List<ItfSoDeliveryChanOrPostDTO> resultList = new ArrayList<>();
        Map<String, Object> requestInfoMap = batchHandleRequestInfo(headerDeliveryList);
        // 先调拣配 全部报错也不调过账接口 两边都报错也拼接错误消息 只要有错误 则处理状态为E
        // 调用拣配接口，将拣配数据同步至SAP
        Map<String, Object> changeMap = sendESBConnect.sendEsb(requestInfoMap, "", "ItfSoDeliveryChanOrPostIfaceRepositoryImpl.soDeliveryChangeOrPostIface", ItfConstant.InterfaceCode.ESB_DELIVERY_ORDER_PICK_UPDATE);
        String aReturn = JSON.toJSON(changeMap.get("RETURN")).toString();
        List<ItfSoDeliveryChanOrPostDTO4> changeResult = JSONArray.parseArray(aReturn, ItfSoDeliveryChanOrPostDTO4.class);
        Optional<ItfSoDeliveryChanOrPostDTO4> successOpt = changeResult.stream().filter(ps -> StringUtils.equals(ps.getTYPE(), "S")).findFirst();
        List<ItfSoDeliveryChanOrPostDTO4> allReturnMessageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(changeResult)) {
            changeResult.forEach(cr -> {
                cr.setRequestType(HmeConstants.ConstantValue.CHANGE);
            });
            allReturnMessageList.addAll(changeResult);
        }
        if (successOpt.isPresent()) {
            // 调用过账接口，将拣配数据同步至SAP
            Map<String, Object> postRequestMap = batchHandlePostRequestInfo(headerDeliveryList);
            Map<String, Object> postMap = sendESBConnect.sendEsb(postRequestMap, "", "ItfSoDeliveryChanOrPostIfaceRepositoryImpl.soDeliveryChangeOrPostIface", ItfConstant.InterfaceCode.ESB_DELIVERY_ORDER_POST);
            String postReturn = JSON.toJSON(postMap.get("DATA")).toString();
            List<ItfSoDeliveryChanOrPostDTO4> postResult = JSONArray.parseArray(postReturn, ItfSoDeliveryChanOrPostDTO4.class);
            if (CollectionUtils.isNotEmpty(postResult)) {
                postResult.forEach(pr -> {
                    pr.setRequestType(HmeConstants.ConstantValue.POST);
                });
                allReturnMessageList.addAll(postResult);
            }
        }
        resultList = this.handleReturnMessage(tenantId, allReturnMessageList, headerDeliveryList);
        return resultList;
    }

    private List<ItfSoDeliveryChanOrPostDTO> handleReturnMessage(Long tenantId, List<ItfSoDeliveryChanOrPostDTO4> allReturnMessageList, List<ItfSoDeliveryChanOrPostVO4> headerDeliveryList) {
        List<ItfSoDeliveryChanOrPostDTO> resultList = new ArrayList<>();
        // 按单据进行分组 多个则消息进行拼接
        Map<String, List<ItfSoDeliveryChanOrPostDTO4>> messageMap = allReturnMessageList.stream().map(ms -> {
            String docNum = ms.getVBELN().replaceAll("^(0+)", "");
            ms.setVBELN(docNum);
            return ms;
        }).collect(Collectors.groupingBy(ItfSoDeliveryChanOrPostDTO4::getVBELN));
        List<ItfSoDeliveryChanOrPostIface> headerList = new ArrayList<>();
        List<ItfSoDeliveryLineChanIface> lineIfaceList = new ArrayList<>();
        List<ItfSoDeliveryChanLotDetailIface> lotDetailIfaceList = new ArrayList();
        List<ItfSoDeliveryChanSnDetailIface> snDetailIfaceList = new ArrayList<>();
        for (ItfSoDeliveryChanOrPostVO4 chanOrPostVO4 : headerDeliveryList) {
            ItfSoDeliveryChanOrPostIface iface = chanOrPostVO4.getIface();

            List<ItfSoDeliveryChanOrPostDTO4> messageList = messageMap.getOrDefault(iface.getDocNum(), Collections.EMPTY_LIST);
            String processStatus = "";
            // 拣配&过账 有异常则显示异常 多个消息拼接 只有一个消息 就是拣配的
            StringBuffer sb = new StringBuffer();
            int listSize = messageList.size();
            for (ItfSoDeliveryChanOrPostDTO4 message : messageList) {
                if (!StringUtils.equals(processStatus, "E")) {
                    processStatus = message.getTYPE();
                }
                if (listSize > 1) {
                    if (HmeConstants.ConstantValue.CHANGE.equals(message.getRequestType())) {
                        sb.append("拣配-");
                    } else if (HmeConstants.ConstantValue.POST.equals(message.getRequestType())) {
                        sb.append("过账-");
                    }
                    sb.append(message.getMESSAGE());
                    sb.append(" ");
                } else {
                    sb.append("拣配-");
                    sb.append(message.getMESSAGE());
                }
            }
            iface.setProcessStatus(processStatus);
            iface.setProcessMessage(sb.toString());
            headerList.add(iface);
            // 查询对应的行
            if (CollectionUtils.isNotEmpty(chanOrPostVO4.getLineList())) {
                for (ItfSoDeliveryLineChanIface lineChanIface : chanOrPostVO4.getLineList()) {
                    lineChanIface.setProcessStatus(processStatus);
                    lineChanIface.setProcessMessage(sb.toString());
                    lineIfaceList.add(lineChanIface);
                }
            }
            // 序列明细
            if (CollectionUtils.isNotEmpty(chanOrPostVO4.getSnDetailIfaceList())) {
                for (ItfSoDeliveryChanSnDetailIface snDetailIface : chanOrPostVO4.getSnDetailIfaceList()) {
                    snDetailIface.setProcessStatus(processStatus);
                    snDetailIface.setProcessMessage(sb.toString());
                    snDetailIfaceList.add(snDetailIface);
                }
            }
            // 批次明细
            if (CollectionUtils.isNotEmpty(chanOrPostVO4.getLotDetailIfaceList())) {
                for (ItfSoDeliveryChanLotDetailIface lotDetailIface : chanOrPostVO4.getLotDetailIfaceList()) {
                    lotDetailIface.setProcessStatus(processStatus);
                    lotDetailIface.setProcessMessage(sb.toString());
                    lotDetailIfaceList.add(lotDetailIface);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(headerList)) {
            List<List<ItfSoDeliveryChanOrPostIface>> lists = CommonUtils.splitSqlList(headerList, 500);
            for (List<ItfSoDeliveryChanOrPostIface> splitList : lists) {
                this.myBatchUpdateSelective(splitList);
            }
        }
        if (CollectionUtils.isNotEmpty(lineIfaceList)) {
            List<List<ItfSoDeliveryLineChanIface>> lists = CommonUtils.splitSqlList(lineIfaceList, 500);
            for (List<ItfSoDeliveryLineChanIface> splitList : lists) {
                itfSoDeliveryLineChanIfaceRepository.myBatchUpdateSelective(splitList);
            }
        }
        if (CollectionUtils.isNotEmpty(lotDetailIfaceList)) {
            List<List<ItfSoDeliveryChanLotDetailIface>> lists = CommonUtils.splitSqlList(lotDetailIfaceList, 500);
            for (List<ItfSoDeliveryChanLotDetailIface> splitList : lists) {
                itfSoDeliveryChanLotDetailIfaceRepository.myBatchUpdateSelective(splitList);
            }
        }
        if (CollectionUtils.isNotEmpty(snDetailIfaceList)) {
            List<List<ItfSoDeliveryChanSnDetailIface>> lists = CommonUtils.splitSqlList(snDetailIfaceList, 500);
            for (List<ItfSoDeliveryChanSnDetailIface> splitList : lists) {
                itfSoDeliveryChanSnDetailIfaceRepository.myBatchUpdateSelective(splitList);
            }
        }
        if(CollectionUtils.isNotEmpty(headerList)){
            for(ItfSoDeliveryChanOrPostIface itfSoDeliveryChanOrPostIface:headerList){
                ItfSoDeliveryChanOrPostDTO itfSoDeliveryChanOrPostDTO = new ItfSoDeliveryChanOrPostDTO();
                itfSoDeliveryChanOrPostDTO.setStatus(itfSoDeliveryChanOrPostIface.getProcessStatus());
                itfSoDeliveryChanOrPostDTO.setMessage(itfSoDeliveryChanOrPostIface.getProcessMessage());
                resultList.add(itfSoDeliveryChanOrPostDTO);
            }
        }
        return resultList;
    }

    private Map<String, Object> batchHandlePostRequestInfo(List<ItfSoDeliveryChanOrPostVO4> chanOrPostVO4List) {
        Map<String, Object> requestInfoMap = new HashMap<>();
        List<ItfSoDeliveryChanOrPostDTO4> requestList = new ArrayList<>();
        for (ItfSoDeliveryChanOrPostVO4 chanOrPostVO4 : chanOrPostVO4List) {
            ItfSoDeliveryChanOrPostDTO4 postDTO4 = new ItfSoDeliveryChanOrPostDTO4();
            ItfSoDeliveryChanOrPostIface iface = chanOrPostVO4.getIface();
            postDTO4.setVBELN(iface.getDocNum());
            requestList.add(postDTO4);
        }
        requestInfoMap.put("DATA", requestList);
        return requestInfoMap;
    }

    private Map<String, Object> batchHandleRequestInfo(List<ItfSoDeliveryChanOrPostVO4> chanOrPostVO4List) {
        Map<String, Object> requestInfoMap = new HashMap<>();
        requestInfoMap.put("header", this.handleHeaderChanIface(chanOrPostVO4List));
        // 如果行存在批次详情,根据批次详情拆分成对应的行
        requestInfoMap.put("item", this.batchHandleLineChanIfaceList(chanOrPostVO4List));
        requestInfoMap.put("serial", this.batchHandleSnDetailsChanIface(chanOrPostVO4List));
        return requestInfoMap;
    }

    private List<ItfSoDeliveryChanOrPostDTO7> handleHeaderChanIface (List<ItfSoDeliveryChanOrPostVO4> chanOrPostVO4List) {
        List<ItfSoDeliveryChanOrPostDTO7> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(chanOrPostVO4List)) {
            for (ItfSoDeliveryChanOrPostVO4 chanOrPostVO4 : chanOrPostVO4List) {
                ItfSoDeliveryChanOrPostIface iface = chanOrPostVO4.getIface();
                ItfSoDeliveryChanOrPostDTO7 chanOrPostDTO7 = new ItfSoDeliveryChanOrPostDTO7();
                chanOrPostDTO7.setVBELN(iface.getDocNum());
                if (StringUtils.equals(chanOrPostVO4.getIface().gethReturnStatus(), HmeConstants.ConstantValue.CANCEL)) {
                    chanOrPostDTO7.setH_CANCEL("X");
                }
                resultList.add(chanOrPostDTO7);
            }
        }
        return resultList;
    }

    private List<ItfSoDeliveryChanOrPostDTO6> batchHandleSnDetailsChanIface (List<ItfSoDeliveryChanOrPostVO4> chanOrPostVO4List) {
        List<ItfSoDeliveryChanOrPostDTO6> resultList = new ArrayList<>();
        for (ItfSoDeliveryChanOrPostVO4 chanOrPostVO4 : chanOrPostVO4List) {
            if (CollectionUtils.isNotEmpty(chanOrPostVO4.getSnDetailIfaceList())) {
                for (ItfSoDeliveryChanSnDetailIface itfSoDeliveryChanSnDetailIface : chanOrPostVO4.getSnDetailIfaceList()) {
                    ItfSoDeliveryChanOrPostDTO6 chanOrPostDTO6 = new ItfSoDeliveryChanOrPostDTO6();
                    chanOrPostDTO6.setVBELN(itfSoDeliveryChanSnDetailIface.getDocNum());
                    chanOrPostDTO6.setPOSNR(itfSoDeliveryChanSnDetailIface.getDocLineNum());
                    chanOrPostDTO6.setSERNR(itfSoDeliveryChanSnDetailIface.getSn());
                    resultList.add(chanOrPostDTO6);
                }
            }
        }
        return resultList;
    }

    private List<ItfSoDeliveryChanOrPostDTO5> batchHandleLineChanIfaceList(List<ItfSoDeliveryChanOrPostVO4> chanOrPostVO4List) {
        List<ItfSoDeliveryChanOrPostDTO5> resultList = new ArrayList<>();
        for (ItfSoDeliveryChanOrPostVO4 chanOrPostVO4 : chanOrPostVO4List) {
            if (CollectionUtils.isNotEmpty(chanOrPostVO4.getLineList())) {
                for (ItfSoDeliveryLineChanIface lineChanIface : chanOrPostVO4.getLineList()) {
                    List<ItfSoDeliveryChanLotDetailIface> detailsList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(chanOrPostVO4.getLotDetailIfaceList())) {
                        detailsList = chanOrPostVO4.getLotDetailIfaceList().stream().filter(vo -> StringUtils.equals(vo.getLineId(), lineChanIface.getLineId())).collect(Collectors.toList());
                    }
                    if (CollectionUtils.isNotEmpty(detailsList)) {
                        for (ItfSoDeliveryChanLotDetailIface lotDetailIface : detailsList) {
                            ItfSoDeliveryChanOrPostDTO5 deliveryChanOrPostDTO5 = new ItfSoDeliveryChanOrPostDTO5();
                            deliveryChanOrPostDTO5.setVBELN(lineChanIface.getDocNum());
                            deliveryChanOrPostDTO5.setPOSNR(lineChanIface.getDocLineNum());
                            deliveryChanOrPostDTO5.setWERKS(lineChanIface.getSiteCode());
                            deliveryChanOrPostDTO5.setMATNR(lineChanIface.getMaterialCode());
                            deliveryChanOrPostDTO5.setLGORT(lineChanIface.getWarehouseCode());
                            deliveryChanOrPostDTO5.setLFIMG(lotDetailIface.getQuantity());
                            deliveryChanOrPostDTO5.setQTY(lotDetailIface.getQuantity());
                            deliveryChanOrPostDTO5.setMEINS(lineChanIface.getUomCode());
                            deliveryChanOrPostDTO5.setCHARG(lotDetailIface.getLot());
                            deliveryChanOrPostDTO5.setDELIV_ITEM(lotDetailIface.getAttribute1());
                            resultList.add(deliveryChanOrPostDTO5);
                        }
                    } else {
                        ItfSoDeliveryChanOrPostDTO5 deliveryChanOrPostDTO5 = new ItfSoDeliveryChanOrPostDTO5();
                        deliveryChanOrPostDTO5.setVBELN(lineChanIface.getDocNum());
                        deliveryChanOrPostDTO5.setPOSNR(lineChanIface.getDocLineNum());
                        deliveryChanOrPostDTO5.setWERKS(lineChanIface.getSiteCode());
                        deliveryChanOrPostDTO5.setMATNR(lineChanIface.getMaterialCode());
                        deliveryChanOrPostDTO5.setLGORT(lineChanIface.getWarehouseCode());
                        deliveryChanOrPostDTO5.setLFIMG(lineChanIface.getChangeQuantity());
                        deliveryChanOrPostDTO5.setQTY(lineChanIface.getQuantity());
                        deliveryChanOrPostDTO5.setMEINS(lineChanIface.getUomCode());
                        if (StringUtils.equals(lineChanIface.getlReturnStatus(), HmeConstants.ConstantValue.CANCEL)) {
                            deliveryChanOrPostDTO5.setL_CANCEL("X");
                        }
                        resultList.add(deliveryChanOrPostDTO5);
                    }
                }
            }
        }
        return resultList;
    }

    private Map<String, List<ItfSoDeliveryChanOrPostVO3>> instructionActualQtyBatchGet(Long tenantId, List<String> instructionIdList) {
        Map<String, List<ItfSoDeliveryChanOrPostVO3>> resultMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(instructionIdList)) {
            List<ItfSoDeliveryChanOrPostVO3> itfSoDeliveryChanOrPostVO3s = itfSoDeliveryChanOrPostIfaceMapper.instructionActualQtyBatchGet(tenantId, instructionIdList);
            resultMap = itfSoDeliveryChanOrPostVO3s.stream().collect(Collectors.groupingBy(ItfSoDeliveryChanOrPostVO3::getInstructionId));
        }
        return resultMap;
    }

    private ItfSoDeliveryChanOrPostVO2 handleLineChanIface(Long tenantId, ItfSoDeliveryChanOrPostIface iface, ItfSoDeliveryChanOrPostDTO3 instructionInfo, Integer instructionIndex, Long userId, Date currentDate, BigDecimal actualQty, Long batchId, String type, String instructionDocType) {
        ItfSoDeliveryChanOrPostVO2 itfSoDeliveryChanOrPostVO2 = new ItfSoDeliveryChanOrPostVO2();
        // 记录接口表ITF_ITF_SO_DELIVERY_LINE_CHAN_IFACE
        String lineId = customDbRepository.getNextKey("itf_so_delivery_line_chan_iface_s");
        String lineCid = customDbRepository.getNextKey("itf_so_delivery_line_chan_iface_cid_s");
        ItfSoDeliveryLineChanIface lineChanIface = new ItfSoDeliveryLineChanIface();
        lineChanIface.setLineId(lineId);
        lineChanIface.setHeaderId(iface.getHeaderId());
        lineChanIface.setDocNum(iface.getDocNum());
        lineChanIface.setDocLineNum(instructionInfo.getInstructionLineNum());
        lineChanIface.setMaterialCode(instructionInfo.getMaterialCode());
        lineChanIface.setUomCode(instructionInfo.getUomCode());
        lineChanIface.setSiteCode(iface.getSiteCode());
        lineChanIface.setTenantId(tenantId);
        lineChanIface.setCid(Long.valueOf(lineCid));
        lineChanIface.setCreationDate(currentDate);
        lineChanIface.setCreatedBy(userId);
        lineChanIface.setLastUpdateDate(currentDate);
        lineChanIface.setLastUpdatedBy(userId);
        lineChanIface.setObjectVersionNumber(1L);
        lineChanIface.setProcessDate(currentDate);
        lineChanIface.setProcessStatus(HmeConstants.ConstantValue.NO);
        lineChanIface.setBatchId(batchId);
        lineChanIface.setChangeQuantity(instructionInfo.getQuantity());
        //若为发货单则去locatorCode,退料单取toLocatorCode
        if("SO_DELIVERY".equals(instructionDocType)){
            lineChanIface.setWarehouseCode(instructionInfo.getLocatorCode());
        }else if("SALES_RETURN".equals(instructionDocType)){
            lineChanIface.setWarehouseCode(instructionInfo.getToLocatorCode());
        }
        // 根据指令ID获取指令实绩明细的条码id
        List<ItfSoDeliveryChanOrPostVO3> instructionActualDetailsList = itfSoDeliveryChanOrPostIfaceMapper.instructionActualDetailQuery(tenantId, instructionInfo.getInstructionId());
        if (HmeConstants.ConstantValue.STRING_ZERO.equals(type)) {
            // 将指令对应的实绩条码记录到接口表ITF_SO_DELIVERY_LINE_CHAN_SN_DETAIL_IFACE
            List<ItfSoDeliveryChanSnDetailIface> snDetailIfaceList = new ArrayList<>();
            for (ItfSoDeliveryChanOrPostVO3 instructionActualDetail : instructionActualDetailsList) {
                String detailsId = customDbRepository.getNextKey("itf_so_delivery_chan_sn_detail_iface_s");
                String detailsCid = customDbRepository.getNextKey("itf_so_delivery_chan_sn_detail_iface_cid_s");
                ItfSoDeliveryChanSnDetailIface detailIface = new ItfSoDeliveryChanSnDetailIface();
                detailIface.setLineId(lineChanIface.getLineId());
                detailIface.setLineSnDetailId(detailsId);
                detailIface.setDocNum(lineChanIface.getDocNum());
                detailIface.setDocLineNum(lineChanIface.getDocLineNum());
                detailIface.setSn(instructionActualDetail.getMaterialLotCode());
                detailIface.setProcessStatus(HmeConstants.ConstantValue.NO);
                detailIface.setTenantId(tenantId);
                detailIface.setCid(Long.valueOf(detailsCid));
                detailIface.setCreationDate(currentDate);
                detailIface.setCreatedBy(userId);
                detailIface.setLastUpdateDate(currentDate);
                detailIface.setLastUpdatedBy(userId);
                detailIface.setObjectVersionNumber(1L);
                detailIface.setBatchId(batchId);
                detailIface.setProcessDate(currentDate);
                detailIface.setProcessStatus(HmeConstants.ConstantValue.NO);
                snDetailIfaceList.add(detailIface);
            }
            itfSoDeliveryChanOrPostVO2.setSnDetailIfaceList(snDetailIfaceList);
        } else {
            if (HmeConstants.ConstantValue.STRING_ONE.equals(type)) {
                List<ItfSoDeliveryChanLotDetailIface> lotDetailIfaceList = new ArrayList<>();
                // 将指令对实绩条码批次记录到接口表ITF_SO_DELIVERY_LINE_CHAN_LOT_DETAIL_IFACE
                Map<String, List<ItfSoDeliveryChanOrPostVO3>> lotActualDetailsMap = instructionActualDetailsList.stream().collect(Collectors.groupingBy(ItfSoDeliveryChanOrPostVO3::getLot));
                for (Map.Entry<String, List<ItfSoDeliveryChanOrPostVO3>> lotActualDetailsEntry : lotActualDetailsMap.entrySet()) {
                    String detailsId = customDbRepository.getNextKey("itf_so_delivery_chan_lot_detail_iface_s");
                    String detailsCid = customDbRepository.getNextKey("itf_so_delivery_chan_lot_detail_iface_cid_s");
                    ItfSoDeliveryChanLotDetailIface detailIface = new ItfSoDeliveryChanLotDetailIface();
                    detailIface.setLineId(lineChanIface.getLineId());
                    detailIface.setLineLotDetailId(detailsId);
                    detailIface.setDocNum(lineChanIface.getDocNum());
                    detailIface.setDocLineNum(lineChanIface.getDocLineNum());
                    detailIface.setLot(lotActualDetailsEntry.getKey());
                    detailIface.setProcessStatus(HmeConstants.ConstantValue.NO);
                    Double actualDetailsQty = lotActualDetailsEntry.getValue().stream().map(ItfSoDeliveryChanOrPostVO3::getActualQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                    detailIface.setQuantity(BigDecimal.valueOf(actualDetailsQty));
                    detailIface.setTenantId(tenantId);
                    detailIface.setCid(Long.valueOf(detailsCid));
                    detailIface.setCreationDate(currentDate);
                    detailIface.setCreatedBy(userId);
                    detailIface.setLastUpdateDate(currentDate);
                    detailIface.setLastUpdatedBy(userId);
                    detailIface.setObjectVersionNumber(1L);
                    detailIface.setBatchId(batchId);
                    detailIface.setProcessDate(currentDate);
                    detailIface.setProcessStatus(HmeConstants.ConstantValue.NO);
                    // 9+五位流水号
                    detailIface.setAttribute1(this.spliceLot(instructionIndex++));
                    lotDetailIfaceList.add(detailIface);
                }
                itfSoDeliveryChanOrPostVO2.setLotDetailIfaceList(lotDetailIfaceList);
            }
        }
        lineChanIface.setQuantity(actualQty);
        itfSoDeliveryChanOrPostVO2.setLineChanIface(lineChanIface);
        itfSoDeliveryChanOrPostVO2.setInstructionIndex(instructionIndex);
        return itfSoDeliveryChanOrPostVO2;
    }

    private String spliceLot(Integer instructionIndex) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(5);
        formatter.setGroupingUsed(false);
        return "9" + formatter.format(instructionIndex);
    }
}


