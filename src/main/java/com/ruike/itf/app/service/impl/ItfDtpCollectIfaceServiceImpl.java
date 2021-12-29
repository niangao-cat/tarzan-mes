package com.ruike.itf.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.infra.mapper.HmeEoJobDataRecordMapper;
import com.ruike.itf.api.dto.ApCollectItfDTO1;
import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.DtpCollectItfDTO;
import com.ruike.itf.app.service.ItfDtpCollectIfaceService;
import com.ruike.itf.domain.entity.ItfDtpCollectIface;
import com.ruike.itf.domain.entity.ItfFacCollectIface;
import com.ruike.itf.domain.repository.ItfDtpCollectIfaceRepository;
import com.ruike.itf.infra.mapper.ItfApCollectIfaceMapper;
import com.ruike.itf.infra.mapper.ItfDtpCollectIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.message.MessageClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 器件测试台数据采集接口表应用服务默认实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-25 19:00:41
 */
@Service
@Slf4j
public class ItfDtpCollectIfaceServiceImpl extends BaseServiceImpl<ItfDtpCollectIface> implements ItfDtpCollectIfaceService {

    private final ItfDtpCollectIfaceRepository itfDtpCollectIfaceRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;

    @Autowired
    private HmeEquipmentWkcRelRepository hmeEquipmentWkcRelRepository;

    @Autowired
    private ItfApCollectIfaceMapper itfApCollectIfaceMapper;

    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeEoJobDataRecordMapper hmeEoJobDataRecordMapper;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private ItfDtpCollectIfaceMapper itfDtpCollectIfaceMapper;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private HmeEoTestDataRecordRepository hmeEoTestDataRecordRepository;

    @Autowired
    public ItfDtpCollectIfaceServiceImpl(ItfDtpCollectIfaceRepository itfDtpCollectIfaceRepository, MtErrorMessageRepository mtErrorMessageRepository) {
        this.itfDtpCollectIfaceRepository = itfDtpCollectIfaceRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }


    private boolean validate(Long tenantId, List<ItfDtpCollectIface> itfList) {
        boolean validFlag = true;
        // 反射得到字段map
        Map<String, Field> fieldMap = Arrays.stream(ItfDtpCollectIface.class.getDeclaredFields()).collect(Collectors.toMap(Field::getName, rec -> rec, (key1, keys2) -> key1));
        for (ItfDtpCollectIface itf : itfList) {
            String processMessage = "";
            // 验证字段
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getAssetEncoding()), processMessage, "ITF_DATA_COLLECT_0001");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getSn()), processMessage, "ITF_DATA_COLLECT_0003");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getDtpCurrent()), processMessage, "ITF_DATA_COLLECT_0007", fieldMap.get(ItfDtpCollectIface.FIELD_DTP_CURRENT).getAnnotation(ApiModelProperty.class).value());

            if (StringUtils.isNotBlank(processMessage)) {
                itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                itf.setProcessMessage(processMessage);
                itf.setProcessDate(new Date());
                itfDtpCollectIfaceRepository.updateByPrimaryKeySelective(itf);
                validFlag = false;
            }
        }
        return validFlag;
    }

    @Override
    public List<DataCollectReturnDTO> invoke(Long tenantId, List<DtpCollectItfDTO> collectList) {
        log.info("DataCollectReturnDTO :"+collectList);

        if (CollectionUtils.isEmpty(collectList)) {
            return new ArrayList<>();
        }
        // 插入接口表
        List<ItfDtpCollectIface> list = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(DtpCollectItfDTO.class, ItfDtpCollectIface.class, false);
        Date nowDate = new Date();
        List<String> ifaceIdList = customSequence.getNextKeys("itf_dtp_collect_iface_s", collectList.size());
        int ifaceIdIndex = 0;
        for (DtpCollectItfDTO data : collectList) {
            ItfDtpCollectIface itf = new ItfDtpCollectIface();
            copier.copy(data, itf, null);
            itf.setInterfaceId(ifaceIdList.get(ifaceIdIndex++));
            itf.setTenantId(tenantId);
            itf.setObjectVersionNumber(1L);
            itf.setCreatedBy(-1L);
            itf.setCreationDate(new Date());
            itf.setLastUpdatedBy(-1L);
            itf.setLastUpdateDate(new Date());
            itf.setProcessDate(nowDate);
            itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_NEW);
            list.add(itf);
        }
        myBatchInsert(list);

        // 验证接口表
        boolean validFlag = this.validate(tenantId, list);

        if (validFlag) {
            ////根据设备找工位
            //HmeEquipment hmeEquipmentFirst = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
            //    setTenantId(tenantId);
            //    setAssetEncoding(list.get(0).getAssetEncoding());
            //}});
            //if (ObjectUtil.isEmpty(hmeEquipmentFirst)) {
            //    for (ItfDtpCollectIface data : list) {
            //
            //        data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            //        data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
            //                "ITF_DATA_COLLECT_0011", "ITF", data.getAssetEncoding()));
            //        this.updateByPrimaryKeySelective(data);
            //    }
            //    return InterfaceUtils.getReturnList(list);
            //}
            //log.info("ItfDtpCollectIfaceServiceImpl.hmeEquipmentFirst :" + hmeEquipmentFirst);
            //
            //List<HmeEquipmentWkcRel> select = hmeEquipmentWkcRelRepository.select(new HmeEquipmentWkcRel() {{
            //    setEquipmentId(hmeEquipmentFirst.getEquipmentId());
            //}});
            //if (CollectionUtils.isEmpty(select)) {
            //    for (ItfDtpCollectIface data : list) {
            //
            //        data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            //        data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
            //                "ITF_DATA_COLLECT_0012", "ITF", data.getAssetEncoding()));
            //        this.updateByPrimaryKeySelective(data);
            //    }
            //    return InterfaceUtils.getReturnList(list);
            //}
            //String wkcId = select.get(0).getStationId();
            //log.info("ItfDtpCollectIfaceServiceImpl.wkcId :" + wkcId);
            //
            ////根据工位找工艺
            //List<String> OperationIds = itfApCollectIfaceMapper.selectOperation(tenantId, wkcId);
            //if (CollectionUtils.isEmpty(OperationIds)) {
            //    for (ItfDtpCollectIface data : list) {
            //
            //        data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            //        data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
            //                "HME_EO_JOB_SN_021", "HME"));
            //        this.updateByPrimaryKeySelective(data);
            //    }
            //    return InterfaceUtils.getReturnList(list);
            //}
            //log.info("ItfDtpCollectIfaceServiceImpl.OperationIds :" + OperationIds);
            List<MtOperation> mtOperations = mtOperationRepository.selectByCondition(Condition.builder(MtOperation.class)
                    .andWhere(Sqls.custom().andEqualTo(MtOperation.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtOperation.FIELD_OPERATION_NAME, list.get(0).getDtpOperationName()))
                    .build());
            if(CollectionUtils.isEmpty(mtOperations))
            {
                    for (ItfDtpCollectIface data : list) {

                        data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                        data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_021", "HME"));
                        this.updateByPrimaryKeySelective(data);
                    }
                    return InterfaceUtils.getReturnList(list);
            }
            String operationId=mtOperations.get(0).getOperationId();
            //查找
            MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, list.get(0).getSn());
            if (ObjectUtils.isEmpty(materialLot)) {
                for (ItfDtpCollectIface data : list) {

                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SERVICE_DATA_RECORD_001", "HME", data.getSn()));
                    this.updateByPrimaryKeySelective(data);
                }
                return InterfaceUtils.getReturnList(list);
            }
            log.info("ItfDtpCollectIfaceServiceImpl.materialLot :" + materialLot);

            String reWorkFlag = "N";
            MtExtendSettings mtExtendSettings = new MtExtendSettings();
            mtExtendSettings.setAttrName("REWORK_FLAG");
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLot.getMaterialLotId(), Collections.singletonList(mtExtendSettings));
            for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOList) {
                if ("REWORK_FLAG".equals(mtExtendAttrVO.getAttrName())) {
                    if (StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())) {
                        reWorkFlag = mtExtendAttrVO.getAttrValue();
                    }
                }
            }
            log.info("ItfDtpCollectIfaceServiceImpl.reWorkFlag :" + reWorkFlag);

            List<HmeEoJobSn> hmeEoJobSns = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLot.getMaterialLotId())
                            .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, operationId)
                            .andEqualTo(HmeEoJobSn.FIELD_REWORK_FLAG, reWorkFlag)
                            .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                    .orderBy(HmeEoJobSn.FIELD_LAST_UPDATE_DATE)
                    .build());
            if (ObjectUtils.isEmpty(hmeEoJobSns)) {
                for (ItfDtpCollectIface data : list) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_DATA_COLLECT_0014", "ITF", data.getSn()));
                    this.updateByPrimaryKeySelective(data);
                }
                return InterfaceUtils.getReturnList(list);

            }
            log.info("ItfDtpCollectIfaceServiceImpl.hmeEoJobSns :" + hmeEoJobSns);
            List<ApCollectItfDTO1> apCollectItfDTO1s = itfApCollectIfaceMapper.selectcode(tenantId, hmeEoJobSns.get(0).getJobId());
            List<HmeEoJobDataRecord> jobDataRecordList = new ArrayList<>();

            //2021-07-28 18:34 edit by chaonan.hu for tianyang.xie 增加表HmeEoTestDataRecord的处理逻辑
            List<HmeEoTestDataRecord> hmeEoTestDataRecordList = new ArrayList<>();
            HmeEoJobSn hmeEoJobSn = hmeEoJobSns.get(0);
            for (ItfDtpCollectIface data : list) {
                for (ApCollectItfDTO1 temp :
                        apCollectItfDTO1s) {
                    String valueField = getResult(temp, data);
                    if (!StringUtils.isBlank(valueField)) {
                        String result = getFieldValueByFieldName(valueField, data);
                        HmeEoJobDataRecord eoJobDataRecord = new HmeEoJobDataRecord();
                        eoJobDataRecord.setJobRecordId(temp.getJobRecordId());
                        eoJobDataRecord.setResult(result);
                        jobDataRecordList.add(eoJobDataRecord);
                        HmeEoTestDataRecord hmeEoTestDataRecord = new HmeEoTestDataRecord();
                        hmeEoTestDataRecord.setTenantId(tenantId);
                        hmeEoTestDataRecord.setEoId(temp.getEoId());
                        hmeEoTestDataRecord.setMaterialLotId(hmeEoJobSn.getMaterialLotId());
                        hmeEoTestDataRecord.setOperationId(hmeEoJobSn.getOperationId());
                        hmeEoTestDataRecord.setTagGroupId(temp.getTagGroupId());
                        hmeEoTestDataRecord.setTagId(temp.getTagId());
                        hmeEoTestDataRecord.setReworkFlag(hmeEoJobSn.getReworkFlag());
                        hmeEoTestDataRecord.setMinValue(temp.getMinimumValue());
                        hmeEoTestDataRecord.setMaxValue(temp.getMaximalValue());
                        hmeEoTestDataRecord.setResult(result);
                        hmeEoTestDataRecordList.add(hmeEoTestDataRecord);
                    }
                }

            }
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(jobDataRecordList)) {
                batchUpdateResult(tenantId, jobDataRecordList);
            }
            if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(hmeEoTestDataRecordList)){
                hmeEoTestDataRecordRepository.batchSaveTestDataRecord(tenantId, hmeEoTestDataRecordList);
            }
        }

        // 返回处理结果
        return InterfaceUtils.getReturnList(list);
    }

    /**
     * @param temp
     * @param data
     * @return java.lang.String
     * @description 获取最终的结果只
     * @author wenzhang.yu@hand-china.com
     * @date 2020/10/19 14:56
     **/
    private String getResult(ApCollectItfDTO1 temp, ItfDtpCollectIface data) {
        if (!StringUtils.isBlank(temp.getLimitCond1())) {
            String limit1 = getFieldValueByFieldName(temp.getLimitCond1(), data);
            if (!limit1.equals(temp.getCond1Value())) {
                return null;
            }
        }
        if (!StringUtils.isBlank(temp.getLimitCond2())) {
            String limit2 = getFieldValueByFieldName(temp.getLimitCond2(), data);
            if (!limit2.equals(temp.getCond2Value())) {
                return null;
            }
        }
        return temp.getValueField();
    }

    /**
     * @param fieldName
     * @param object
     * @return java.lang.String
     * @description 获取相应参数的值
     * @author wenzhang.yu@hand-china.com
     * @date 2020/10/19 14:57
     **/
    private static String getFieldValueByFieldName(String fieldName, Object object) {
        try {
            String fieldName1 = camelName(fieldName);
            Field field = object.getClass().getDeclaredField(fieldName1);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            Object o = field.get(object);
            return o.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param name
     * @return java.lang.String
     * @description 获取对应参数的正确格式
     * @author wenzhang.yu@hand-china.com
     * @date 2020/10/19 14:57
     **/
    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.toLowerCase();
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * @param tenantId
     * @param dataRecordList
     * @return void
     * @description
     * @author wenzhang.yu@hand-china.com
     * @date 2020/10/16 14:21
     **/
    public void batchUpdateResult(Long tenantId, List<HmeEoJobDataRecord> dataRecordList) {
        int batchNum = 500;
        Long userId = (Objects.nonNull(DetailsHelper.getUserDetails()) && Objects.nonNull(DetailsHelper.getUserDetails().getUserId()))
                ? DetailsHelper.getUserDetails().getUserId() : -1L;
        List<List<HmeEoJobDataRecord>> list = InterfaceUtils.splitSqlList(dataRecordList, batchNum);
        list.forEach(item -> hmeEoJobDataRecordMapper.batchUpdateResult(tenantId, userId, item));
    }

    /**
     *@description 批量新增
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/7 16:42
     *@param insertList
     *@return void
     **/
    public void myBatchInsert(List<ItfDtpCollectIface> insertList) {
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(insertList)) {
            List<List<ItfDtpCollectIface>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfDtpCollectIface> domains : splitSqlList) {
                itfDtpCollectIfaceMapper.insertIface(domains);
            }
        }
    }
}
