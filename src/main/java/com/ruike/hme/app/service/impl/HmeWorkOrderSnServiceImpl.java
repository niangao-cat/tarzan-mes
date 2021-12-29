package com.ruike.hme.app.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruike.hme.app.service.HmeWorkOrderSnService;
import com.ruike.hme.domain.repository.HmeWorkOrderSnRepository;
import com.ruike.hme.domain.vo.HmeWorkOrderSnVO;
import com.ruike.hme.domain.vo.HmeWorkOrderSnVO2;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.app.service.impl.WmsCommonServiceComponent;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.core.base.AopProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * HmeWorkOrderSnServiceImpl
 *
 * @author liyuan.lv@hand-china.com 2020/06/09 14:36
 */
@Component
@ImportService(templateCode = "HME_WO_SN")
public class HmeWorkOrderSnServiceImpl implements HmeWorkOrderSnService, IBatchImportService, AopProxy<HmeWorkOrderSnServiceImpl> {
    private static final Logger logger = LoggerFactory.getLogger(HmeWorkOrderSnServiceImpl.class);
    @Autowired
    private CustomSequence customSequence;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private HmeWorkOrderSnRepository hmeWorkOrderSnRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private WmsCommonServiceComponent wmsCommonServiceComponent;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;


    @Override
    public int getSize(){
        return 500;
    }

    @Override
    public Boolean doImport(List<String> data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (CollectionUtils.isEmpty(data)) {
            throw new MtException("HME_WORK_ORDER_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WORK_ORDER_SN_001", "HME"));
        } else {
            // 循环导入list获取
            logger.info("工单产品序列号执行导入");
            List<HmeWorkOrderSnVO> hmeWorkOrderSnVOList = new ArrayList<>(64);
            data.forEach(lineData -> {
                JSONObject jsonObject = JSON.parseObject(lineData);
                HmeWorkOrderSnVO hmeWorkOrderSnVO = new HmeWorkOrderSnVO();
                hmeWorkOrderSnVO.setLineNumber(jsonObject.getLong("lineNumber"));
                hmeWorkOrderSnVO.setWorkOrderNum(jsonObject.getString("workOrderNum"));
                hmeWorkOrderSnVO.setSnNumber(jsonObject.getString("snNumber"));

                hmeWorkOrderSnVOList.add(hmeWorkOrderSnVO);
            });

            return importData(tenantId, hmeWorkOrderSnVOList);
        }

    }

    private boolean importData(Long tenantId, List<HmeWorkOrderSnVO> voList) {
        String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);
        String eventId = wmsCommonServiceComponent.generateEvent(tenantId, WmsConstant.EVENT_REQUEST_CODE_BARCODE);

        Map<String, List<HmeWorkOrderSnVO>> mapWorkOrderNumValid = voList.stream().collect(Collectors.groupingBy(HmeWorkOrderSnVO::getWorkOrderNum));
        // 校验工单号是否不唯一
        if (mapWorkOrderNumValid.size() > 1) {
            throw new MtException("HME_WORK_ORDER_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WORK_ORDER_SN_002", "HME"));
        }
        // 校验产品序列号唯一不能重复
        Map<String, Long> mapSnNumberValid = voList.stream().
                collect(Collectors.groupingBy(HmeWorkOrderSnVO::getSnNumber,Collectors.counting()));
        if (mapSnNumberValid.size() != voList.size()) {
            throw new MtException("HME_WORK_ORDER_SN_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WORK_ORDER_SN_003", "HME"));
        }

        String workOrderId = mtWorkOrderRepository.numberLimitWoGet(tenantId, voList.get(0).getWorkOrderNum());
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, workOrderId);
        if (voList.size() != mtWorkOrder.getQty().intValue()) {
            throw new MtException("HME_WORK_ORDER_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WORK_ORDER_SN_004", "HME"));
        }

        List<HmeWorkOrderSnVO2> eoList = hmeWorkOrderSnRepository.selectEoByWoNum(tenantId, voList.get(0).getWorkOrderNum());
        List<HmeWorkOrderSnVO2> sortedEoList = eoList.stream().sorted(Comparator.comparing(HmeWorkOrderSnVO2::getEoId)).collect(Collectors.toList());
        int index = 0;
        for(HmeWorkOrderSnVO itemVO : voList) {
            MtMaterialLotVO2 materialLotVO = new MtMaterialLotVO2();
            materialLotVO.setTenantId(tenantId);
            materialLotVO.setEventId(eventId);
            materialLotVO.setSiteId(defaultSiteId);
            materialLotVO.setEnableFlag(HmeConstants.ConstantValue.YES);
            materialLotVO.setQualityStatus("OK");
            materialLotVO.setMaterialLotCode(itemVO.getSnNumber());
            materialLotVO.setMaterialId(eoList.get(0).getMaterialId());
            materialLotVO.setPrimaryUomId(eoList.get(0).getUomId());
            materialLotVO.setPrimaryUomQty(1d);
            materialLotVO.setEoId(sortedEoList.get(index).getEoId());
            materialLotVO.setLoadTime(new Date());
            materialLotVO.setCreateReason("INITIALIZE");
            materialLotVO.setIdentification(itemVO.getSnNumber());
            materialLotVO.setCid(Long.valueOf(customSequence.getNextKey("mt_material_lot_cid_s")));
            mtMaterialLotRepository.materialLotUpdate(tenantId, materialLotVO, HmeConstants.ConstantValue.NO);

            index++;
        }

        logger.info("工单产品序列号导入完成");
        return true;
    }


}
