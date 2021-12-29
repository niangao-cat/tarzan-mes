package com.ruike.itf.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.infra.mapper.HmeEoJobDataRecordMapper;
import com.ruike.itf.api.dto.ApCollectItfDTO;
import com.ruike.itf.api.dto.ApCollectItfDTO1;
import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.app.service.ItfApCollectIfaceService;
import com.ruike.itf.domain.entity.ItfApCollectIface;
import com.ruike.itf.domain.repository.ItfApCollectIfaceRepository;
import com.ruike.itf.infra.mapper.ItfApCollectIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
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

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 老化台数据采集接口表应用服务默认实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-25 19:00:42
 */
@Service
@Slf4j
public class ItfApCollectIfaceServiceImpl extends BaseServiceImpl<ItfApCollectIface> implements ItfApCollectIfaceService {

    private final ItfApCollectIfaceRepository itfApCollectIfaceRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MessageClient messageClient;

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
    private HmeEoTestDataRecordRepository hmeEoTestDataRecordRepository;

    //@Autowired
    //private HmeEquipmentSnRelRepository hmeEquipmentSnRelRepository;


    @Autowired
    public ItfApCollectIfaceServiceImpl(ItfApCollectIfaceRepository itfApCollectIfaceRepository, MtErrorMessageRepository mtErrorMessageRepository) {
        this.itfApCollectIfaceRepository = itfApCollectIfaceRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }


    private boolean validate(Long tenantId, List<ItfApCollectIface> itfList) {
        boolean validFlag = true;
        // 反射得到字段map
        Map<String, Field> fieldMap = Arrays.stream(ItfApCollectIface.class.getDeclaredFields()).collect(Collectors.toMap(Field::getName, rec -> rec, (key1, keys2) -> key1));
        for (ItfApCollectIface itf : itfList) {
            String processMessage = "";
            // 验证字段
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getAssetEncoding()), processMessage, "ITF_DATA_COLLECT_0001");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getSn()), processMessage, "ITF_DATA_COLLECT_0003");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getApDuration()), processMessage, "ITF_DATA_COLLECT_0007", fieldMap.get(ItfApCollectIface.FIELD_AP_DURATION).getAnnotation(ApiModelProperty.class).value());

            if (StringUtils.isNotBlank(processMessage)) {
                itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                itf.setProcessMessage(processMessage);
                itf.setProcessDate(new Date());
                itfApCollectIfaceRepository.updateByPrimaryKeySelective(itf);
                validFlag = false;
            }
        }
        return validFlag;
    }

    @Override
    public List<DataCollectReturnDTO> invoke(Long tenantId, List<ApCollectItfDTO> collectList) {
        log.info("ApCollectItfDTo :"+collectList);

        if (CollectionUtils.isEmpty(collectList)) {
            return new ArrayList<>();
        }
        // 插入接口表
        List<ItfApCollectIface> list = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(ApCollectItfDTO.class, ItfApCollectIface.class, false);
        Date nowDate = new Date();
        for (ApCollectItfDTO data : collectList) {
            ItfApCollectIface itf = new ItfApCollectIface();
            copier.copy(data, itf, null);
            itf.setProcessDate(nowDate);
            itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_NEW);
            list.add(itf);
        }
        list.forEach(this::insertSelective);

        // 验证接口表
        boolean validFlag = this.validate(tenantId, list);

        if (validFlag) {
            // 执行插入业务表逻辑，如果有必要
            for (ItfApCollectIface data : list) {
                //根据设备找工位
                HmeEquipment hmeEquipmentFirst = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
                    setTenantId(tenantId);
                    setAssetEncoding(data.getAssetEncoding());
                }});
                if (ObjectUtil.isEmpty(hmeEquipmentFirst)) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_DATA_COLLECT_0011", "ITF",data.getAssetEncoding()));
                    itfApCollectIfaceMapper.updateByPrimaryKeySelective(data);
                    continue;
                }
                log.info("ItfApCollectIface.hmeEquipmentFirst:"+hmeEquipmentFirst);
                List<HmeEquipmentWkcRel> select = hmeEquipmentWkcRelRepository.select(new HmeEquipmentWkcRel() {{
                    setEquipmentId(hmeEquipmentFirst.getEquipmentId());
                }});
                if (CollectionUtils.isEmpty(select)) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_DATA_COLLECT_0012", "ITF",data.getAssetEncoding()));
                    itfApCollectIfaceMapper.updateByPrimaryKeySelective(data);
                    continue;
                }
                String wkcId = select.get(0).getStationId();
                log.info("ItfApCollectIface.wkcId:"+wkcId);

                //根据工位找工艺
                List<String> OperationIds=itfApCollectIfaceMapper.selectOperation(tenantId,wkcId);
                if (CollectionUtils.isEmpty(OperationIds)) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_021", "HME"));
                    itfApCollectIfaceMapper.updateByPrimaryKeySelective(data);
                    continue;
                }
                log.info("ItfApCollectIface.OperationIds:"+OperationIds);

                //查找
                MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, data.getSn());
                if(ObjectUtils.isEmpty(materialLot))
                {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SERVICE_DATA_RECORD_001", "HME",data.getSn()));
                    itfApCollectIfaceMapper.updateByPrimaryKeySelective(data);
                    continue;
                }
                log.info("ItfApCollectIface.materialLot:"+materialLot);
//                if(ObjectUtils.isEmpty(materialLot.getEoId()))
//                {
//                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
//                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "ITF_DATA_COLLECT_0013", "ITF",data.getSn()));
//                    itfApCollectIfaceMapper.updateByPrimaryKeySelective(data);
//                    continue;
//                }
                String reWorkFlag="N";
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
                log.info("ItfApCollectIface.reWorkFlag:"+mtExtendAttrVOList);
                List<HmeEoJobSn> hmeEoJobSns = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLot.getMaterialLotId())
                                .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, OperationIds.get(0))
                                .andEqualTo(HmeEoJobSn.FIELD_REWORK_FLAG, reWorkFlag)
                                .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                        .orderBy(HmeEoJobSn.FIELD_LAST_UPDATE_DATE)
                        .build());
                if(ObjectUtils.isEmpty(hmeEoJobSns))
                {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_DATA_COLLECT_0014", "ITF",data.getSn()));
                    itfApCollectIfaceMapper.updateByPrimaryKeySelective(data);
                    continue;
                }
                log.info("ItfApCollectIface.hmeEoJobSns:"+hmeEoJobSns);
                List<ApCollectItfDTO1> apCollectItfDTO1s=itfApCollectIfaceMapper.selectcode(tenantId,hmeEoJobSns.get(0).getJobId());

                //2021-07-28 18:34 edit by chaonan.hu for tianyang.xie 增加表HmeEoTestDataRecord的处理逻辑
                List<HmeEoTestDataRecord> hmeEoTestDataRecordList = new ArrayList<>();
                HmeEoJobSn hmeEoJobSn = hmeEoJobSns.get(0);
                for (ApCollectItfDTO1 temp:
                     apCollectItfDTO1s) {
                    String valueField=getResult(temp,data);
                    log.info("ItfApCollectIface.data :"+temp);
                    if(!StringUtils.isBlank(valueField))
                    {
                        String result = getFieldValueByFieldName(valueField, data);
                        HmeEoJobDataRecord eoJobDataRecord = new HmeEoJobDataRecord();
                        eoJobDataRecord.setJobRecordId(temp.getJobRecordId());
                        eoJobDataRecord.setResult(result);
                        hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(eoJobDataRecord);
                        log.info("ItfApCollectIface.result :"+eoJobDataRecord);
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
                if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(hmeEoTestDataRecordList)){
                    hmeEoTestDataRecordRepository.batchSaveTestDataRecord(tenantId, hmeEoTestDataRecordList);
                }
                //if(data.getApDuration().compareTo(BigDecimal.ZERO)==0) {
                //    List<HmeEquipmentSnRel> hmeEquipmentSnRels = hmeEquipmentSnRelRepository.selectByCondition(Condition.builder(HmeEquipmentSnRel.class)
                //            .andWhere(Sqls.custom().andEqualTo(HmeEquipmentSnRel.FIELD_TENANT_ID, tenantId)
                //                    .andEqualTo(HmeEquipmentSnRel.FIELD_ASSET_ENCODING, data.getAssetEncoding())
                //                    .andEqualTo(HmeEquipmentSnRel.FIELD_SN, data.getSn())).build());
                //    if (CollectionUtils.isEmpty(hmeEquipmentSnRels)) {
                //        HmeEquipmentSnRel hmeEquipmentSnRel = new HmeEquipmentSnRel();
                //        hmeEquipmentSnRel.setAssetEncoding(data.getAssetEncoding());
                //        hmeEquipmentSnRel.setTenantId(tenantId);
                //        hmeEquipmentSnRel.setSn(data.getSn());
                //        hmeEquipmentSnRelRepository.insertSelective(hmeEquipmentSnRel);
                //    }
                //}
            }
        }
        // 返回处理结果
        return InterfaceUtils.getReturnList(list);
    }

    /**
     *@description 返回取值字段
     *@author wenzhang.yu@hand-china.com
     *@date 2020/9/29 20:54
     *@param temp
     *@param data
     *@return java.lang.String
     **/
    private String getResult(ApCollectItfDTO1 temp, ItfApCollectIface data) {
        if(!StringUtils.isBlank(temp.getLimitCond1()))
        {
            String limit1 = getFieldValueByFieldName(temp.getLimitCond1(), data);
            if(!limit1.equals(temp.getCond1Value()))
            {
               return null;
            }
        }
        if(!StringUtils.isBlank(temp.getLimitCond2()))
        {
            String limit2 = getFieldValueByFieldName(temp.getLimitCond2(), data);
            if(!limit2.equals(temp.getCond2Value()))
            {
                return null;
            }
        }
        return temp.getValueField();

    }

    private static String getFieldValueByFieldName(String fieldName, Object object) {
        try {
            String fieldName1= camelName(fieldName);
            Field field = object.getClass().getDeclaredField(fieldName1);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            Object o = field.get(object);
            return o.toString() ;
        } catch (Exception e) {
            return null;
        }
    }

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
        for (String camel :  camels) {
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


}
