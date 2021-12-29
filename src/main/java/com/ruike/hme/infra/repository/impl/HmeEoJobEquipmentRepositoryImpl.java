package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO5;
import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO6;
import com.ruike.hme.domain.vo.HmeEoJobEquipmentVO;
import com.ruike.hme.domain.vo.HmeEoJobEquipmentVO2;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeEoJobEquipment;
import com.ruike.hme.domain.repository.HmeEoJobEquipmentRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SN进出站设备状态记录表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-06-28 16:52:11
 */
@Slf4j
@Component
public class HmeEoJobEquipmentRepositoryImpl extends BaseRepositoryImpl<HmeEoJobEquipment>
                implements HmeEoJobEquipmentRepository {

    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobEquipment> snInToSiteEquipmentRecord(Long tenantId, HmeWkcEquSwitchDTO5 dto) {
        String jobId = dto.getJobId();
        String workcellId = dto.getWorkcellId();
        List<HmeWkcEquSwitchDTO6> hmeWkcEquSwitchDto6List = dto.getHmeWkcEquSwitchDTO6List();

        List<HmeEoJobEquipment> hmeEoJobEquipmentList = new ArrayList<>();

        // UPDATE 20201023 YC 批量方式新增
        if (CollectionUtils.isNotEmpty(hmeWkcEquSwitchDto6List)) {
            // 批量查询已存在设备
            Map<HmeEoJobEquipmentVO, HmeEoJobEquipment> hmeEoJobEquipmentMap = selectByCondition(Condition
                            .builder(HmeEoJobEquipment.class)
                            .andWhere(Sqls.custom().andEqualTo(HmeEoJobEquipment.FIELD_TENANT_ID, tenantId)
                                            .andEqualTo(HmeEoJobEquipment.FIELD_JOB_ID, jobId)
                                            .andEqualTo(HmeEoJobEquipment.FIELD_WORKCELL_ID, workcellId)
                                            .andIn(HmeEoJobEquipment.FIELD_EQUIPMENT_ID,
                                                            hmeWkcEquSwitchDto6List.stream()
                                                                            .map(HmeWkcEquSwitchDTO6::getEquipmentId)
                                                                            .distinct().collect(Collectors.toList())))
                            .build()).stream().collect(Collectors.toMap(
                                            t -> new HmeEoJobEquipmentVO(t.getEquipmentId(), t.getEquipmentStatus()),
                                            t -> t));

            List<HmeWkcEquSwitchDTO6> insertDataList = new ArrayList<>(hmeWkcEquSwitchDto6List.size());
            for (HmeWkcEquSwitchDTO6 hmeWkcEquSwitchDto6 : hmeWkcEquSwitchDto6List) {
                // 增量设备
                /*HmeEoJobEquipment hmeEoJobEquipment = new HmeEoJobEquipment();
                hmeEoJobEquipment.setTenantId(tenantId);
                hmeEoJobEquipment.setJobId(jobId);
                hmeEoJobEquipment.setWorkcellId(workcellId);
                hmeEoJobEquipment.setEquipmentId(hmeWkcEquSwitchDto6.getEquipmentId());
                hmeEoJobEquipment.setEquipmentStatus(hmeWkcEquSwitchDto6.getEquipmentStatus());
                HmeEoJobEquipment hmeEoJobEquipmentDb = self().selectOne(hmeEoJobEquipment);
                if (hmeEoJobEquipmentDb == null) {
                    self().insertSelective(hmeEoJobEquipment);
                }
                hmeEoJobEquipmentList.add(hmeEoJobEquipment);*/
                
                HmeEoJobEquipment hmeEoJobEquipmentDb =
                                hmeEoJobEquipmentMap.get(new HmeEoJobEquipmentVO(hmeWkcEquSwitchDto6.getEquipmentId(),
                                                hmeWkcEquSwitchDto6.getEquipmentStatus()));
                if (hmeEoJobEquipmentDb == null) {
                    insertDataList.add(hmeWkcEquSwitchDto6);
                } else {
                    // 记录结果返回值
                    hmeEoJobEquipmentList.add(hmeEoJobEquipmentDb);
                }
            }
            
            if (CollectionUtils.isNotEmpty(insertDataList)) {
                List<String> sqlList = new ArrayList<>(insertDataList.size());
                Date now = new Date();
                Long userId = DetailsHelper.getUserDetails().getUserId();

                List<String> idS = mtCustomDbRepository.getNextKeys("hme_eo_job_equipment_s", insertDataList.size());
                List<String> cidS = mtCustomDbRepository.getNextKeys("hme_eo_job_equipment_cid_s", insertDataList.size());

                int count = 0;
                for (HmeWkcEquSwitchDTO6 insertData : insertDataList) {
                    HmeEoJobEquipment newData = new HmeEoJobEquipment();
                    newData.setTenantId(tenantId);
                    newData.setJobEquipmentId(idS.get(count));
                    newData.setJobId(jobId);
                    newData.setWorkcellId(workcellId);
                    newData.setEquipmentId(insertData.getEquipmentId());
                    newData.setEquipmentStatus(insertData.getEquipmentStatus());
                    newData.setCid(Long.valueOf(cidS.get(count)));
                    newData.setCreationDate(now);
                    newData.setCreatedBy(userId);
                    newData.setLastUpdateDate(now);
                    newData.setLastUpdatedBy(userId);
                    sqlList.addAll(mtCustomDbRepository.getInsertSql(newData));
                    count++;

                    // 记录结果返回值
                    hmeEoJobEquipmentList.add(newData);
                }

                if(CollectionUtils.isNotEmpty(sqlList)){
                    this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
                }
            }
        }

        return hmeEoJobEquipmentList;
    }

    /**
     *
     * @Description 出站批量新增设备数据
     *
     * @author yuchao.wang
     * @date 2020/11/19 0:23
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveEquipmentRecordForOutSite(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobEquipmentRepositoryImpl.batchSaveEquipmentRecordForOutSite tenantId={},dto={}", tenantId, dto);
        //筛选所有需要保存的设备信息
        List<String> jobIdList = new ArrayList<>();
        Map<String, List<HmeWkcEquSwitchDTO6>> jobEquipmentMap = new HashMap<>();
        List<HmeWkcEquSwitchDTO6> allEquipmentList = new ArrayList<>();
        for (HmeEoJobSnVO3 eoJobSn : dto.getSnLineList()) {
            if (CollectionUtils.isNotEmpty(eoJobSn.getEquipmentList())) {
                jobIdList.add(eoJobSn.getJobId());
                jobEquipmentMap.put(eoJobSn.getJobId(), eoJobSn.getEquipmentList());
                allEquipmentList.addAll(eoJobSn.getEquipmentList());
            }
        }
        if (CollectionUtils.isEmpty(jobIdList)) {
            return;
        }

        //查询当前设备是否存在
        List<HmeEoJobEquipment> hmeEoJobEquipments = selectByCondition(Condition.builder(HmeEoJobEquipment.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobEquipment.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobEquipment.FIELD_JOB_ID, dto.getWorkcellId())
                        .andIn(HmeEoJobEquipment.FIELD_WORKCELL_ID, jobIdList)
                        .andIn(HmeEoJobEquipment.FIELD_EQUIPMENT_ID, allEquipmentList.stream()
                                .map(HmeWkcEquSwitchDTO6::getEquipmentId)
                                .distinct().collect(Collectors.toList()))).build());
        Map<HmeEoJobEquipmentVO2, HmeEoJobEquipment> hmeEoJobEquipmentMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(hmeEoJobEquipments)) {
            hmeEoJobEquipments.forEach(item -> hmeEoJobEquipmentMap
                    .put(new HmeEoJobEquipmentVO2(item.getJobId(), item.getEquipmentId(), item.getEquipmentStatus()), item));
        }

        //构造插入数据
        List<HmeEoJobEquipmentVO2> insertDataList = new ArrayList<>();
        for (Map.Entry<String, List<HmeWkcEquSwitchDTO6>> jobEquipmentEntry : jobEquipmentMap.entrySet()) {
            for (HmeWkcEquSwitchDTO6 equipment : jobEquipmentEntry.getValue()) {
                //如果数据库中不存在则新增
                HmeEoJobEquipmentVO2 newEquipment = new HmeEoJobEquipmentVO2(jobEquipmentEntry.getKey(), equipment.getEquipmentId(), equipment.getEquipmentStatus());
                if (!hmeEoJobEquipmentMap.containsKey(newEquipment)) {
                    insertDataList.add(newEquipment);
                }
            }
        }

        //构造insert语句
        if (CollectionUtils.isNotEmpty(insertDataList)) {
            List<String> sqlList = new ArrayList<>(insertDataList.size());
            Date now = new Date();
            Long userId = DetailsHelper.getUserDetails().getUserId();

            List<String> idS = mtCustomDbRepository.getNextKeys("hme_eo_job_equipment_s", insertDataList.size());
            List<String> cidS = mtCustomDbRepository.getNextKeys("hme_eo_job_equipment_cid_s", insertDataList.size());

            int count = 0;
            for (HmeEoJobEquipmentVO2 insertData : insertDataList) {
                HmeEoJobEquipment newData = new HmeEoJobEquipment();
                newData.setTenantId(tenantId);
                newData.setJobEquipmentId(idS.get(count));
                newData.setJobId(insertData.getJobId());
                newData.setWorkcellId(dto.getWorkcellId());
                newData.setEquipmentId(insertData.getEquipmentId());
                newData.setEquipmentStatus(insertData.getEquipmentStatus());
                newData.setCid(Long.valueOf(cidS.get(count)));
                newData.setCreationDate(now);
                newData.setCreatedBy(userId);
                newData.setLastUpdateDate(now);
                newData.setLastUpdatedBy(userId);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(newData));
                count++;
            }

            //批量新增
            if(CollectionUtils.isNotEmpty(sqlList)){
                this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
        }
    }
}
