package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeEoJobEquipmentService;
import com.ruike.hme.domain.entity.HmeEoJobEquipment;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.repository.HmeEoJobEquipmentRepository;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.infra.mapper.HmeEoJobEquipmentMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.sys.CustomSequence;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.engine.TextElementName;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * SN进出站设备状态记录表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-06-28 16:52:11
 */
@Service
public class HmeEoJobEquipmentServiceImpl implements HmeEoJobEquipmentService {

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private HmeEoJobEquipmentMapper hmeEoJobEquipmentMapper;

    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void binndHmeEoJobEquipment(Long tenantId, List<String> eoJobSnIdList, String workcellId) {
        if(CollectionUtils.isEmpty(eoJobSnIdList))
        {
            return;
        }
        //获取userId
        Long userId = -1L;
        if (!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        List<HmeEoJobEquipment> hmeEoJobEquipments = hmeEoJobEquipmentMapper.binndHmeEoJobEquipment(tenantId, workcellId);
        List<HmeEoJobEquipment> insertList = new ArrayList<>();
        // 修改序列的大小
        Integer indexSize = CollectionUtils.isNotEmpty(hmeEoJobEquipments) ? eoJobSnIdList.size() * hmeEoJobEquipments.size() : eoJobSnIdList.size();
        List<String> eoJobEquipmentIdList = customSequence.getNextKeys("hme_eo_job_equipment_s", indexSize);
        List<String> eoJobEquipmentCidList = customSequence.getNextKeys("hme_eo_job_equipment_cid_s", indexSize);
        // 当前下标
        Integer currNum = 0;
        for (int i = 0; i < eoJobSnIdList.size(); i++) {
            for (HmeEoJobEquipment hmeEoJobEquipmentTemp :
                    hmeEoJobEquipments) {
                HmeEoJobEquipment hmeEoJobEquipment = new HmeEoJobEquipment();
                hmeEoJobEquipment.setTenantId(tenantId);
                hmeEoJobEquipment.setJobEquipmentId(eoJobEquipmentIdList.get(currNum));
                hmeEoJobEquipment.setJobId(eoJobSnIdList.get(i));
                hmeEoJobEquipment.setWorkcellId(workcellId);
                hmeEoJobEquipment.setEquipmentId(hmeEoJobEquipmentTemp.getEquipmentId());
                hmeEoJobEquipment.setEquipmentStatus(hmeEoJobEquipmentTemp.getEquipmentStatus());
                hmeEoJobEquipment.setCid(Long.valueOf(eoJobEquipmentCidList.get(currNum)));
                hmeEoJobEquipment.setObjectVersionNumber(1L);
                hmeEoJobEquipment.setCreatedBy(userId);
                hmeEoJobEquipment.setCreationDate(new Date());
                hmeEoJobEquipment.setLastUpdatedBy(userId);
                hmeEoJobEquipment.setLastUpdateDate(new Date());
                insertList.add(hmeEoJobEquipment);
                currNum++;
            }
        }
        if(CollectionUtils.isNotEmpty(insertList))
        {
            myBatchInsert(insertList);
        }
        
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindHmeEoJobEquipmentOfTimeProcess(Long tenantId, List<String> eoJobSnIdList, String workcellId, String equipmentId) {
        if (CollectionUtils.isNotEmpty(eoJobSnIdList)) {
            //获取userId
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            HmeEquipment hmeEquipment = hmeEquipmentRepository.selectByPrimaryKey(equipmentId);
            // 修改序列的大小
            List<String> eoJobEquipmentIdList = customSequence.getNextKeys("hme_eo_job_equipment_s", eoJobSnIdList.size());
            List<String> eoJobEquipmentCidList = customSequence.getNextKeys("hme_eo_job_equipment_cid_s", eoJobSnIdList.size());
            // 当前下标
            Integer currNum = 0;
            List<HmeEoJobEquipment> insertList = new ArrayList<>();
            for (String eoJobSnId : eoJobSnIdList) {
                HmeEoJobEquipment hmeEoJobEquipment = new HmeEoJobEquipment();
                hmeEoJobEquipment.setTenantId(tenantId);
                hmeEoJobEquipment.setJobEquipmentId(eoJobEquipmentIdList.get(currNum));
                hmeEoJobEquipment.setJobId(eoJobSnId);
                hmeEoJobEquipment.setWorkcellId(workcellId);
                hmeEoJobEquipment.setEquipmentId(hmeEquipment.getEquipmentId());
                hmeEoJobEquipment.setEquipmentStatus(hmeEquipment.getEquipmentStatus());
                hmeEoJobEquipment.setCid(Long.valueOf(eoJobEquipmentCidList.get(currNum)));
                hmeEoJobEquipment.setObjectVersionNumber(1L);
                hmeEoJobEquipment.setCreatedBy(userId);
                hmeEoJobEquipment.setCreationDate(new Date());
                hmeEoJobEquipment.setLastUpdatedBy(userId);
                hmeEoJobEquipment.setLastUpdateDate(new Date());
                insertList.add(hmeEoJobEquipment);
                currNum++;
            }
            if(CollectionUtils.isNotEmpty(insertList)) {
                myBatchInsert(insertList);
            }
        }
    }

    private void myBatchInsert(List<HmeEoJobEquipment> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<HmeEoJobEquipment>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeEoJobEquipment> domains : splitSqlList) {
                hmeEoJobEquipmentMapper.batchInsert(domains);
            }
        }
    }

}
