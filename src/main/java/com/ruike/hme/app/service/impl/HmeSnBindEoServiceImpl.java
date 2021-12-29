package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.app.service.HmeSnBindEoService;
import com.ruike.hme.app.service.HmeWorkOrderManagementService;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.repository.HmeWorkOrderManagementRepository;
import com.ruike.hme.domain.vo.HmeSnBindEoVO3;
import com.ruike.hme.domain.vo.HmeWoReleaseVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.app.service.ItfWorkOrderIfaceService;
import com.ruike.itf.infra.constant.ItfConstant;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.vo.MtMaterialLotVO19;
import tarzan.method.api.dto.MtBomDTO5;
import tarzan.method.app.service.MtBomService;
import tarzan.method.domain.entity.MtBom;
import tarzan.order.api.dto.MtEoDTO11;
import tarzan.order.app.service.MtEoService;
import tarzan.order.app.service.MtWorkOrderService;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtEoVO6;
import tarzan.order.domain.vo.MtWorkOrderVO50;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 基于工单生成SN绑定E
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/06 20:14
 */
@Service
@Slf4j
public class HmeSnBindEoServiceImpl implements HmeSnBindEoService {

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;

    @Autowired
    private MtEoService mtEoService;

    @Autowired
    private MtBomService mtBomService;

    @Autowired
    private HmeWorkOrderManagementService hmeWorkOrderManagementService;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtWorkOrderService mtWorkOrderService;

    @Autowired
    private ItfWorkOrderIfaceService itfWorkOrderIfaceService;

    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeEoAndBomStatus(Long tenantId, List<String> eoIdList) {
        //获取EO对应的BOM
        long startDate = System.currentTimeMillis();
        List<MtBom> bomIdList = hmeSnBindEoRepository.queryBomListByEoIds(tenantId, eoIdList);
        log.info("=================================>工单管理平台-工单下达-handleEoRelated-获取EO对应的BOM总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //更改BOM为可下达
        startDate = System.currentTimeMillis();
        bomIdList.forEach(e -> {
            if (StringUtils.equals(e.getBomStatus(), ItfConstant.BomStatus.NEW)) {
                MtBomDTO5 bomDto5 = new MtBomDTO5();
                BeanUtils.copyProperties(e, bomDto5);
                bomDto5.setBomStatus("CAN_RELEASE");
                mtBomService.saveBomForUi(tenantId, bomDto5);
            }
        });
        log.info("=================================>工单管理平台-工单下达-handleEoRelated-更改BOM为可下达总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        // 更改EO状态为可下达
        MtEoDTO11 mtEoDto11 = new MtEoDTO11();
        mtEoDto11.setOperationType("RELEASE");
        mtEoDto11.setEoIds(eoIdList);
        startDate = System.currentTimeMillis();
        mtEoService.eoStatusUpdateForUi(tenantId, mtEoDto11);
        log.info("=================================>工单管理平台-工单下达-handleEoRelated-更改EO状态为可下达总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
    }

    @Override
    public Integer queryOperationComponentCount(Long tenantId, String workOrderId) {
        return hmeSnBindEoRepository.queryOperationComponentCount(tenantId,workOrderId);
    }

    @Override
    public Integer queryBomComponentCount(Long tenantId, String workOrderId) {
        return hmeSnBindEoRepository.queryBomComponentCount(tenantId,workOrderId);
    }

    @Override
    public void handleEoRelated(Long tenantId, List<HmeWoReleaseVO> releaseVOList) {
        List<String> eoIdList = new ArrayList<>();
        long startDate = System.currentTimeMillis();
        for (HmeWoReleaseVO releaseVO : releaseVOList) {
            if(CollectionUtils.isNotEmpty(releaseVO.getEoIdList())){
                // 2020-07-06 add by sanfeng.zhang
                // 基于工单生成SN绑定EO
                List<MtEo> mtEoList = mtEoRepository.eoPropertyBatchGet(tenantId, releaseVO.getEoIdList());
                startDate = System.currentTimeMillis();
                hmeSnBindEoRepository.createSnBindEo(mtEoList,releaseVO.getMtWorkOrder());
                log.info("=================================>工单管理平台-工单下达-handleEoRelated-createSnBindEo总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
                eoIdList.addAll(releaseVO.getEoIdList());
            }
        }
        //2020-07-20 add by sanfeng.zhang  更改EO及BOM状态为下达
        startDate = System.currentTimeMillis();
        this.changeEoAndBomStatus(tenantId,eoIdList);
        log.info("=================================>工单管理平台-工单下达-handleEoRelated-更改EO及BOM状态为下达总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleWoAndEoRelease(Long tenantId, List<MtWorkOrderVO50> mtWorkOrder) {
        Map<String,MtWorkOrder> workOrderMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(mtWorkOrder)){
            List<String> workOrderIdList = mtWorkOrder.stream().map(MtWorkOrderVO50::getWorkOrderId).distinct()
                    .collect(Collectors.toList());
            List<MtWorkOrder> workOrderList = mtWorkOrderRepository.selectByCondition(Condition.builder(MtWorkOrder.class)
                    .andWhere(Sqls.custom().andIn(MtWorkOrder.FIELD_WORK_ORDER_ID, workOrderIdList))
                    .build());
            if(CollectionUtils.isEmpty(workOrderList) || workOrderIdList.size() != workOrderList.size()){
                //工单${1}不存在,请检查!
                throw new MtException("HME_CHIP_DATA_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0001", "HME",""));
            }
            // 增加校验 判断当bom接口未同步完成时报错
            List<HmeSnBindEoVO3> hmeSnBindEoVO3List = hmeSnBindEoRepository.queryBomComponentCountByWorkOrderIds(tenantId, workOrderIdList);
            Map<String, List<HmeSnBindEoVO3>> hmeSnBindEoVO3Map = new HashMap<>();
            if (CollectionUtils.isNotEmpty(hmeSnBindEoVO3List)) {
                hmeSnBindEoVO3Map = hmeSnBindEoVO3List.stream().collect(Collectors.groupingBy(HmeSnBindEoVO3::getWorkOrderId));
            }
            for (MtWorkOrder workOrder : workOrderList) {
                List<HmeSnBindEoVO3> hmeSnBindEoVO3s = hmeSnBindEoVO3Map.get(workOrder.getWorkOrderId());
                Integer countNum = CollectionUtils.isNotEmpty(hmeSnBindEoVO3s) ? hmeSnBindEoVO3s.get(0).getCountNum() : 0;
                if (countNum.compareTo(0) == 0) {
                    throw new MtException("HME_WORK_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_WORK_ORDER_0001", "HME", workOrder.getWorkOrderNum()));
                }
            }
            workOrderMap = workOrderList.stream().collect(Collectors.toMap(item -> item.getWorkOrderId(), t -> t));
        }
        long startDate = System.currentTimeMillis();
        for (MtWorkOrderVO50 mtWorkOrderVO50 : mtWorkOrder) {
            //加锁
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("工单管理平台");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.WO);
            hmeObjectRecordLockDTO.setObjectRecordId(mtWorkOrderVO50.getWorkOrderId());
            hmeObjectRecordLockDTO.setObjectRecordCode(workOrderMap.get(mtWorkOrderVO50.getWorkOrderId()).getWorkOrderNum());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            //加锁
            hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);
        }
        log.info("=================================>工单管理平台-工单下达-加锁总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        try {
            startDate = System.currentTimeMillis();
            List<HmeWoReleaseVO> releaseVOList = hmeWorkOrderManagementService.woReleaseForUi(tenantId, mtWorkOrder);
            log.info("=================================>工单管理平台-工单下达-woReleaseForUi总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            //2020-09-24 add by sanfeng.zhang EO及BOM状态为下达 和 生成SN绑定EO
            startDate = System.currentTimeMillis();
            this.handleEoRelated(tenantId, releaseVOList);
            log.info("=================================>工单管理平台-工单下达-EO及BOM状态为下达 和 生成SN绑定EO总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            // add by kejin.liu01@hand-china.com 工单管理平台-创建sn时自动绑定eo 写入生成的eo的扩展字段：扩展字段名：'REWORK_MATERIAL_LOT'
            if(CollectionUtils.isNotEmpty(releaseVOList)){
                startDate = System.currentTimeMillis();
                this.bindEoAttrReworkMaterialLot(tenantId, releaseVOList);
                log.info("=================================>工单管理平台-工单下达-bindEoAttrReworkMaterialLot总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            }
            // add by jiangling.zheng@hand-china.com 新增工单状态回传接口实时调用
            List<String> workOrderIds = mtWorkOrder.stream().map(MtWorkOrderVO50::getWorkOrderId).collect(Collectors.toList());
            startDate = System.currentTimeMillis();
            itfWorkOrderIfaceService.erpWorkOrderStatusReturnRestProxy(tenantId, workOrderIds);
            log.info("=================================>工单管理平台-工单下达-工单状态回传接口实时调用总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            startDate = System.currentTimeMillis();
            for (MtWorkOrderVO50 mtWorkOrderVO50 : mtWorkOrder) {
                HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
                hmeObjectRecordLockDTO.setFunctionName("工单管理平台");
                hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
                hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.WO);
                hmeObjectRecordLockDTO.setObjectRecordId(mtWorkOrderVO50.getWorkOrderId());
                hmeObjectRecordLockDTO.setObjectRecordCode(workOrderMap.get(mtWorkOrderVO50.getWorkOrderId()).getWorkOrderNum());
                HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
                //解锁
                hmeObjectRecordLockRepository.releaseLock(hmeObjectRecordLock, HmeConstants.ConstantValue.YES);
            }
            log.info("=================================>工单管理平台-工单下达-解锁总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }
    }

    /**
     * <strong>Title : bindEoAttrReworkMaterialLot</strong><br/>
     * <strong>Description : 工单管理平台-SN创建，如果SN_NUM 在hme_repair_wo_sn_rel 存在就写入生成的mt_eo_attr表的扩展字段：扩展字段名：'REWORK_MATERIAL_LOT' </strong><br/>
     * <strong>Create on : 2021/3/1 11:44 上午</strong><br/>
     * 
     * @param tenantId
     * @param releaseVOList
     * @return void
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>         
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    private void bindEoAttrReworkMaterialLot(Long tenantId, List<HmeWoReleaseVO> releaseVOList) {
        // 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventTypeCode = "REPAIR_EO_SN_BIND";
        eventCreateVO.setEventTypeCode(eventTypeCode);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        // 更新扩展表
        releaseVOList.forEach(vo -> {
            MtWorkOrder mtWorkOrder = vo.getMtWorkOrder();
            if(BigDecimal.valueOf(mtWorkOrder.getQty()).compareTo(BigDecimal.ONE) == 0) {
                String snNum = hmeWorkOrderManagementService.selectHmeRepairWoSnRelByWorkOrderNum(tenantId, mtWorkOrder.getWorkOrderNum());
                if (Strings.isNotBlank(snNum)) {
                    List<String> eoIdList = vo.getEoIdList();
                    List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
                    List<MtCommonExtendVO5> mtCommonExtendVO5s = new ArrayList<>();
                    MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
                    mtCommonExtendVO5.setAttrName("REWORK_MATERIAL_LOT");
                    mtCommonExtendVO5.setAttrValue(snNum);
                    mtCommonExtendVO5s.add(mtCommonExtendVO5);
                    for (String eoId : eoIdList) {
                        //物料批扩展属性
                        MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
                        mtCommonExtendVO6.setKeyId(eoId);
                        mtCommonExtendVO6.setAttrs(mtCommonExtendVO5s);
                        attrPropertyList.add(mtCommonExtendVO6);
                    }
                    mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_eo_attr", eventId, attrPropertyList);
                }
            }
        });
        
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String woLimitEoCreate(Long tenantId, MtEoVO6 vo) {
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, vo.getWorkOrderId());
        String eoId = mtEoRepository.woLimitEoCreate(tenantId, vo);
        // 2020-07-06 add by sanfeng.zhang
        // 基于工单生成SN绑定EO
        List<MtEo> mtEoList = new ArrayList<>();
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(eoId);
        mtEoList.add(mtEo);
        hmeSnBindEoRepository.createSnBindEo(mtEoList,mtWorkOrder);
        return eoId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoCreateForUi(Long tenantId, MtWorkOrderVO50 dto) {
        List<String> eoIdList = mtWorkOrderService.eoCreateForUi(tenantId, dto);

        //add by sanfeng.zhang 创建eo绑定sn 2020/9/24 for panfang
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        List<MtEo> mtEoList = mtEoRepository.eoPropertyBatchGet(tenantId, eoIdList);
        hmeSnBindEoRepository.createSnBindEo(mtEoList,mtWorkOrder);
    }
}
