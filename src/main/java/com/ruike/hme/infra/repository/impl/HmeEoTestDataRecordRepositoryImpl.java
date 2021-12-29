package com.ruike.hme.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import com.ruike.hme.infra.mapper.HmeEoTestDataRecordMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeEoTestDataRecord;
import com.ruike.hme.domain.repository.HmeEoTestDataRecordRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 数据采集回测对比记录信息表 资源库实现
 *
 * @author penglin.sui@hand-china.com 2020-09-20 16:35:24
 */
@Component
public class HmeEoTestDataRecordRepositoryImpl extends BaseRepositoryImpl<HmeEoTestDataRecord> implements HmeEoTestDataRecordRepository {
    @Autowired
    private HmeEoTestDataRecordMapper hmeEoTestDataRecordMapper;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存数据采集记录
     *
     * @param tenantId 租户ID
     * @param hmeEoTestDataRecord  数据采集参数
     * @return
     */
    @Override
    public void saveTestDataRecord(Long tenantId, HmeEoTestDataRecord hmeEoTestDataRecord){
        MtExtendSettings extendAttr = new MtExtendSettings();
        extendAttr.setAttrName("PROCESS_FLAG");
        List<MtExtendAttrVO> processFlagAttr = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_tag_attr", "TAG_ID", hmeEoTestDataRecord.getTagId(),
                Collections.singletonList(extendAttr));
        if (CollectionUtils.isNotEmpty(processFlagAttr)) {
            if (HmeConstants.ConstantValue.YES.equals(processFlagAttr.get(0).getAttrValue())) {
                //校验数据是否存在
                HmeEoTestDataRecord hmeEoTestDataRecordPara = new HmeEoTestDataRecord();
                hmeEoTestDataRecordPara.setTenantId(tenantId);
                hmeEoTestDataRecordPara.setEoId(hmeEoTestDataRecord.getEoId());
                hmeEoTestDataRecordPara.setMaterialLotId(hmeEoTestDataRecord.getMaterialLotId());
                hmeEoTestDataRecordPara.setOperationId(hmeEoTestDataRecord.getOperationId());
                hmeEoTestDataRecordPara.setTagGroupId(hmeEoTestDataRecord.getTagGroupId());
                hmeEoTestDataRecordPara.setTagId(hmeEoTestDataRecord.getTagId());
                hmeEoTestDataRecordPara.setReworkFlag(StringUtils.isBlank(hmeEoTestDataRecord.getReworkFlag()) ? HmeConstants.ConstantValue.NO : hmeEoTestDataRecord.getReworkFlag());
                List<HmeEoTestDataRecord> hmeEoTestDataRecordList = hmeEoTestDataRecordMapper.select(hmeEoTestDataRecordPara);

                MtExtendSettings extendAttr2 = new MtExtendSettings();
                extendAttr.setAttrName("STANDARD");
                List<MtExtendAttrVO> standardAttr = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                        "mt_tag_attr", "TAG_ID", hmeEoTestDataRecord.getTagId(),
                        Collections.singletonList(extendAttr));

                if(CollectionUtils.isNotEmpty(hmeEoTestDataRecordList)){
                    for (HmeEoTestDataRecord hmeEoTestDataRecord2:hmeEoTestDataRecordList
                    ) {
                        //更新
                        HmeEoTestDataRecord updateHmeEoTestDataRecord = new HmeEoTestDataRecord();
                        BeanUtils.copyProperties(hmeEoTestDataRecord2,updateHmeEoTestDataRecord);

                        updateHmeEoTestDataRecord.setMinValue(hmeEoTestDataRecord.getMinValue());
                        updateHmeEoTestDataRecord.setMaxValue(hmeEoTestDataRecord.getMaxValue());
                        if(CollectionUtils.isNotEmpty(standardAttr)){
                            if(StringUtils.isNotBlank(standardAttr.get(0).getAttrValue())) {
                                updateHmeEoTestDataRecord.setStandardValue(new BigDecimal(standardAttr.get(0).getAttrValue()));
                            }
                        }
                        updateHmeEoTestDataRecord.setResult(hmeEoTestDataRecord.getResult());
                        hmeEoTestDataRecordMapper.updateByPrimaryKey(updateHmeEoTestDataRecord);
                    }
                }else{
                    //新增
                    HmeEoTestDataRecord insertHmeEoTestDataRecord = new HmeEoTestDataRecord();
                    insertHmeEoTestDataRecord.setTenantId(tenantId);
                    insertHmeEoTestDataRecord.setEoId(hmeEoTestDataRecord.getEoId());
                    insertHmeEoTestDataRecord.setMaterialLotId(hmeEoTestDataRecord.getMaterialLotId());
                    insertHmeEoTestDataRecord.setOperationId(hmeEoTestDataRecord.getOperationId());
                    insertHmeEoTestDataRecord.setTagGroupId(hmeEoTestDataRecord.getTagGroupId());
                    insertHmeEoTestDataRecord.setTagId(hmeEoTestDataRecord.getTagId());
                    insertHmeEoTestDataRecord.setReworkFlag(hmeEoTestDataRecord.getReworkFlag());
                    insertHmeEoTestDataRecord.setMinValue(hmeEoTestDataRecord.getMinValue());
                    insertHmeEoTestDataRecord.setMaxValue(hmeEoTestDataRecord.getMaxValue());
                    if(CollectionUtils.isNotEmpty(standardAttr)){
                        if(StringUtils.isNotBlank(standardAttr.get(0).getAttrValue())) {
                            insertHmeEoTestDataRecord.setStandardValue(new BigDecimal(standardAttr.get(0).getAttrValue()));
                        }
                    }
                    insertHmeEoTestDataRecord.setResult(hmeEoTestDataRecord.getResult());
                    this.insertSelective(insertHmeEoTestDataRecord);
                }
            }
        }
    }

    @Override
    public void batchSaveTestDataRecord(Long tenantId, List<HmeEoTestDataRecord> hmeEoTestDataRecordList) {

        if(CollectionUtils.isEmpty(hmeEoTestDataRecordList)){
            return;
        }

        List<String> tagIdList = hmeEoTestDataRecordList.stream().map(HmeEoTestDataRecord::getTagId).distinct()
                .collect(Collectors.toList());

        //extendAttrNameList顺序不能改变
        List<String> extendAttrNameList = new ArrayList<>();
        extendAttrNameList.add("PROCESS_FLAG");
        extendAttrNameList.add("STANDARD");
        List<MtExtendAttrVO1> extendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_tag_attr", "TAG_ID"
                , tagIdList, extendAttrNameList);

        if (CollectionUtils.isNotEmpty(extendAttrVO1List)) {

            List<String> eoIdList = hmeEoTestDataRecordList.stream().map(HmeEoTestDataRecord::getEoId).distinct()
                    .collect(Collectors.toList());
            List<String> materialLotIdList = hmeEoTestDataRecordList.stream().map(HmeEoTestDataRecord::getMaterialLotId).distinct()
                    .collect(Collectors.toList());
            List<String> operationIdList = hmeEoTestDataRecordList.stream().map(HmeEoTestDataRecord::getOperationId).distinct()
                    .collect(Collectors.toList());
            List<String> tagGroupIdList = hmeEoTestDataRecordList.stream().map(HmeEoTestDataRecord::getTagGroupId).distinct()
                    .collect(Collectors.toList());

            List<HmeEoTestDataRecord> hmeEoTestDataRecordList2 = this.selectByCondition(Condition.builder(HmeEoTestDataRecord.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(HmeEoTestDataRecord.FIELD_TENANT_ID, tenantId)
                            .andIn(HmeEoTestDataRecord.FIELD_EO_ID, eoIdList)
                            .andIn(HmeEoTestDataRecord.FIELD_MATERIAL_LOT_ID, materialLotIdList)
                            .andIn(HmeEoTestDataRecord.FIELD_OPERATION_ID, operationIdList)
                            .andIn(HmeEoTestDataRecord.FIELD_TAG_GROUP_ID, tagGroupIdList)
                            .andIn(HmeEoTestDataRecord.FIELD_TAG_ID, tagIdList))
                    .build());
            Map<String,List<HmeEoTestDataRecord>> hmeEoTestDataRecordMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(hmeEoTestDataRecordList2)){
                hmeEoTestDataRecordMap = hmeEoTestDataRecordList2.stream().collect(Collectors.groupingBy(e ->
                        e.getTenantId() + "#" +
                        e.getEoId() + "#" +
                        e.getMaterialLotId() + "#" +
                        e.getOperationId() + "#" +
                        e.getTagGroupId() + "#" +
                        e.getTagId()));
            }

            List<String> updateHmeEoTestDataRecordSqlList = new ArrayList<>();
            List<HmeEoTestDataRecord> insertHmeEoTestDataRecordList = new ArrayList<>();
            List<String> insertHmeEoTestDataRecordSqlList = new ArrayList<>();

            Map<String , MtExtendAttrVO1> extendAttrMap = extendAttrVO1List.stream().collect(Collectors.toMap(item -> item.getKeyId() + "#" + item.getAttrName(), t -> t));
            for (HmeEoTestDataRecord hmeEoTestDataRecord : hmeEoTestDataRecordList
                 ) {
                MtExtendAttrVO1 processFlagExtendAttrVO1 = extendAttrMap.getOrDefault(hmeEoTestDataRecord.getTagId() + "#" + extendAttrNameList.get(0) , null);
                if(Objects.isNull(processFlagExtendAttrVO1)){
                    continue;
                }
                if (!HmeConstants.ConstantValue.YES.equals(processFlagExtendAttrVO1.getAttrValue())){
                    continue;
                }

                MtExtendAttrVO1 standardExtendAttrVO1 = extendAttrMap.getOrDefault(hmeEoTestDataRecord.getTagId() + "#" + extendAttrNameList.get(1) , null);

                List<HmeEoTestDataRecord> hmeEoTestDataRecordList1 = hmeEoTestDataRecordMap.getOrDefault(
                        hmeEoTestDataRecord.getTenantId() + "#" +
                        hmeEoTestDataRecord.getEoId() + "#" +
                        hmeEoTestDataRecord.getMaterialLotId() + "#" +
                        hmeEoTestDataRecord.getOperationId() + "#" +
                        hmeEoTestDataRecord.getTagGroupId() + "#" +
                        hmeEoTestDataRecord.getTagId(), new ArrayList<>());

                if(CollectionUtils.isNotEmpty(hmeEoTestDataRecordList1)){
                    for (HmeEoTestDataRecord hmeEoTestDataRecord2 : hmeEoTestDataRecordList1
                         ) {
                        //更新
                        HmeEoTestDataRecord updateHmeEoTestDataRecord = new HmeEoTestDataRecord();
                        BeanUtils.copyProperties(hmeEoTestDataRecord2,updateHmeEoTestDataRecord);

                        updateHmeEoTestDataRecord.setMinValue(hmeEoTestDataRecord.getMinValue());
                        updateHmeEoTestDataRecord.setMaxValue(hmeEoTestDataRecord.getMaxValue());

                        if(Objects.nonNull(standardExtendAttrVO1)){
                            if(StringUtils.isNotBlank(standardExtendAttrVO1.getAttrValue())) {
                                updateHmeEoTestDataRecord.setStandardValue(new BigDecimal(standardExtendAttrVO1.getAttrValue()));
                            }
                        }
                        updateHmeEoTestDataRecord.setResult(hmeEoTestDataRecord.getResult());

                        updateHmeEoTestDataRecordSqlList.addAll(customDbRepository.getUpdateSql(updateHmeEoTestDataRecord));
                    }
                }else{
                    //新增
                    HmeEoTestDataRecord insertHmeEoTestDataRecord = new HmeEoTestDataRecord();
                    insertHmeEoTestDataRecord.setTenantId(tenantId);
                    insertHmeEoTestDataRecord.setEoId(hmeEoTestDataRecord.getEoId());
                    insertHmeEoTestDataRecord.setMaterialLotId(hmeEoTestDataRecord.getMaterialLotId());
                    insertHmeEoTestDataRecord.setOperationId(hmeEoTestDataRecord.getOperationId());
                    insertHmeEoTestDataRecord.setTagGroupId(hmeEoTestDataRecord.getTagGroupId());
                    insertHmeEoTestDataRecord.setTagId(hmeEoTestDataRecord.getTagId());
                    insertHmeEoTestDataRecord.setReworkFlag(hmeEoTestDataRecord.getReworkFlag());
                    insertHmeEoTestDataRecord.setMinValue(hmeEoTestDataRecord.getMinValue());
                    insertHmeEoTestDataRecord.setMaxValue(hmeEoTestDataRecord.getMaxValue());
                    if(Objects.nonNull(standardExtendAttrVO1)){
                        if(StringUtils.isNotBlank(standardExtendAttrVO1.getAttrValue())) {
                            insertHmeEoTestDataRecord.setStandardValue(new BigDecimal(standardExtendAttrVO1.getAttrValue()));
                        }
                    }
                    insertHmeEoTestDataRecord.setResult(hmeEoTestDataRecord.getResult());
                    insertHmeEoTestDataRecordList.add(insertHmeEoTestDataRecord);
                }
            }

            if(CollectionUtils.isNotEmpty(insertHmeEoTestDataRecordList)){
                // 获取当前用户
                CustomUserDetails curUser = DetailsHelper.getUserDetails();
                Long userId = curUser == null ? -1L : curUser.getUserId();

                List<String> eoTestDataRecordIdS = customDbRepository.getNextKeys("hme_eo_test_data_record_s", insertHmeEoTestDataRecordList.size());
                List<String> eoTestDataRecordCidS = customDbRepository.getNextKeys("hme_eo_test_data_record_cid_s", insertHmeEoTestDataRecordList.size());
                int count = 0;
                List<String> eoTestDataRecordSqlList = new ArrayList<>();
                for (HmeEoTestDataRecord hmeEoTestDataRecord : insertHmeEoTestDataRecordList) {
                    hmeEoTestDataRecord.setTestId(eoTestDataRecordIdS.get(count));
                    hmeEoTestDataRecord.setCid(Long.valueOf(eoTestDataRecordCidS.get(count)));
                    hmeEoTestDataRecord.setObjectVersionNumber(1L);
                    hmeEoTestDataRecord.setCreatedBy(userId);
                    hmeEoTestDataRecord.setLastUpdatedBy(userId);
                    Date date = CommonUtils.currentTimeGet();
                    hmeEoTestDataRecord.setCreationDate(date);
                    hmeEoTestDataRecord.setLastUpdateDate(date);
                    eoTestDataRecordSqlList.addAll(customDbRepository.getInsertSql(hmeEoTestDataRecord));
                    count++;
                }

                jdbcTemplate.batchUpdate(eoTestDataRecordSqlList.toArray(new String[eoTestDataRecordSqlList.size()]));
            }

            if(CollectionUtils.isNotEmpty(updateHmeEoTestDataRecordSqlList)){
                jdbcTemplate.batchUpdate(updateHmeEoTestDataRecordSqlList.toArray(new String[updateHmeEoTestDataRecordSqlList.size()]));
            }
        }
    }
}
