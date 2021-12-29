package com.ruike.itf.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.infra.mapper.HmeEoJobDataRecordMapper;
import com.ruike.itf.api.dto.ApCollectItfDTO1;
import com.ruike.itf.api.dto.CosCollectItfDTO;
import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.FacCollectItfDTO;
import com.ruike.itf.app.service.ItfFacCollectIfaceService;
import com.ruike.itf.domain.entity.ItfCosCollectIface;
import com.ruike.itf.domain.entity.ItfFacCollectIface;
import com.ruike.itf.domain.repository.ItfFacCollectIfaceRepository;
import com.ruike.itf.infra.mapper.ItfApCollectIfaceMapper;
import com.ruike.itf.infra.mapper.ItfFacCollectIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.CustomUserDetails;
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

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * FAC数据采集接口表应用服务默认实现
 *
 * @author yonghui.zhu@hand-china.com 2020-08-04 19:51:39
 */
@Service
@Slf4j
public class ItfFacCollectIfaceServiceImpl extends BaseServiceImpl<ItfFacCollectIface> implements ItfFacCollectIfaceService {

    private final ItfFacCollectIfaceRepository itfFacCollectIfaceRepository;
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
    private CustomSequence customSequence;

    @Autowired
    private ItfFacCollectIfaceMapper itfFacCollectIfaceMapper;

    @Autowired
    private HmeEoJobDataRecordMapper hmeEoJobDataRecordMapper;

    @Autowired
    private HmeEoTestDataRecordRepository hmeEoTestDataRecordRepository;

    @Autowired
    public ItfFacCollectIfaceServiceImpl(ItfFacCollectIfaceRepository itfFacCollectIfaceRepository, MtErrorMessageRepository mtErrorMessageRepository) {
        this.itfFacCollectIfaceRepository = itfFacCollectIfaceRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    /**
     * 接口数据验证
     *
     * @param itfList  接口数据
     * @param tenantId 租户
     * @return boolean
     * @author Aidan.Zhu yonghui.zhu@hand-china.com
     * @date 2020/7/13 7:56 下午
     */
    private boolean validate(Long tenantId, List<ItfFacCollectIface> itfList) {
        boolean validFlag = true;
        // 反射得到字段map
        Map<String, Field> fieldMap = Arrays.stream(ItfFacCollectIface.class.getDeclaredFields()).collect(Collectors.toMap(Field::getName, rec -> rec, (key1, keys2) -> key1));
        for (ItfFacCollectIface itf : itfList) {
            String processMessage = "";
            // 验证字段
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getAssetEncoding()), processMessage, "ITF_DATA_COLLECT_0001");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getSn()), processMessage, "ITF_DATA_COLLECT_0003");

            if (StringUtils.isNotBlank(processMessage)) {
                itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                itf.setProcessMessage(processMessage);
                itf.setProcessDate(new Date());
                itfFacCollectIfaceRepository.updateByPrimaryKeySelective(itf);
                validFlag = false;
            }
        }
        return validFlag;
    }

    @Override
    public List<DataCollectReturnDTO> invoke(Long tenantId, List<FacCollectItfDTO> collectList) {
        log.info("ItfFacCollectIfaceServiceImpl.invoke.start");
        if (CollectionUtils.isEmpty(collectList)) {
            return new ArrayList<>();
        }
        // 插入接口表
        List<ItfFacCollectIface> list = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(FacCollectItfDTO.class, ItfFacCollectIface.class, false);
        Date nowDate = new Date();
        List<String> ifaceIdList = customSequence.getNextKeys("itf_fac_collect_iface_s", collectList.size());
        int ifaceIdIndex = 0;
        for (FacCollectItfDTO data : collectList) {
            ItfFacCollectIface itf = new ItfFacCollectIface();
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
        log.info("ItfFacCollectIfaceServiceImpl.insterIface.start");
        myBatchInsert(list);
        log.info("ItfFacCollectIfaceServiceImpl.insterIface.end");

        // 验证接口表
        log.info("ItfFacCollectIfaceServiceImpl.check.start");
        boolean validFlag = this.validate(tenantId, list);
        log.info("ItfFacCollectIfaceServiceImpl.check.end");


        if (validFlag) {
            // 执行插入业务表逻辑，如果有必要
            //根据设备找工位
            log.info("ItfFacCollectIfaceServiceImpl.select.start");
            HmeEquipment hmeEquipmentFirst = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
                setTenantId(tenantId);
                setAssetEncoding(list.get(0).getAssetEncoding());
            }});
            log.info("ItfFacCollectIfaceServiceImpl.hmeEquipmentFirst :"+hmeEquipmentFirst);
            if (ObjectUtil.isEmpty(hmeEquipmentFirst)) {
                for (ItfFacCollectIface data : list) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_DATA_COLLECT_0011", "ITF", data.getAssetEncoding()));
                    this.updateByPrimaryKeySelective(data);
                }
                return InterfaceUtils.getReturnList(list);
            }

            List<HmeEquipmentWkcRel> select = hmeEquipmentWkcRelRepository.select(new HmeEquipmentWkcRel() {{
                setEquipmentId(hmeEquipmentFirst.getEquipmentId());
            }});
            if (CollectionUtils.isEmpty(select)) {
                for (ItfFacCollectIface data : list) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_DATA_COLLECT_0012", "ITF", data.getAssetEncoding()));
                    this.updateByPrimaryKeySelective(data);
                }
                return InterfaceUtils.getReturnList(list);

            }
            String wkcId = select.get(0).getStationId();
            log.info("ItfFacCollectIfaceServiceImpl.wkcId :"+wkcId);

            //根据工位找工艺
            List<String> OperationIds = itfApCollectIfaceMapper.selectOperation(tenantId, wkcId);
            if (CollectionUtils.isEmpty(OperationIds)) {
                for (ItfFacCollectIface data : list) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_021", "HME"));
                    this.updateByPrimaryKeySelective(data);
                }
                return InterfaceUtils.getReturnList(list);
            }
            log.info("ItfFacCollectIfaceServiceImpl.OperationIds :"+OperationIds);
            List<HmeEoJobDataRecord> jobDataRecordList = new ArrayList<>();
            //查找
            MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, list.get(0).getSn());
            log.info("ItfFacCollectIfaceServiceImpl.materialLot :"+materialLot);

            if (ObjectUtils.isEmpty(materialLot)) {
                for (ItfFacCollectIface data : list) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SERVICE_DATA_RECORD_001", "HME", data.getSn()));
                    this.updateByPrimaryKeySelective(data);
                }
                return InterfaceUtils.getReturnList(list);
            }
//            if (ObjectUtils.isEmpty(materialLot.getEoId())) {
//                for (ItfFacCollectIface data : list) {
//                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
//                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "ITF_DATA_COLLECT_0013", "ITF", data.getSn()));
//                    this.updateByPrimaryKeySelective(data);
//                }
//                return InterfaceUtils.getReturnList(list);
//            }
            String reWorkFlag = "N";
            MtExtendSettings mtExtendSettings = new MtExtendSettings();
            mtExtendSettings.setAttrName("REWORK_FLAG");
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLot.getMaterialLotId(), Collections.singletonList(mtExtendSettings));
            log.info("ItfFacCollectIfaceServiceImpl.REWORK_FLAG :"+mtExtendAttrVOList);
            for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOList) {
                if ("REWORK_FLAG".equals(mtExtendAttrVO.getAttrName())) {
                    if (StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())) {
                        reWorkFlag = mtExtendAttrVO.getAttrValue();
                    }
                }
            }

            List<HmeEoJobSn> hmeEoJobSns = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLot.getMaterialLotId())
                            .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, OperationIds.get(0))
                            .andEqualTo(HmeEoJobSn.FIELD_REWORK_FLAG, reWorkFlag)
                            .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                    .orderBy(HmeEoJobSn.FIELD_LAST_UPDATE_DATE)
                    .build());
            log.info("ItfFacCollectIfaceServiceImpl.hmeEoJobSns :"+hmeEoJobSns);
            if (ObjectUtils.isEmpty(hmeEoJobSns)) {
                for (ItfFacCollectIface data : list) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_DATA_COLLECT_0014", "ITF", data.getSn()));
                    this.updateByPrimaryKeySelective(data);
                }
                return InterfaceUtils.getReturnList(list);
            }
            List<ApCollectItfDTO1> apCollectItfDTO1s = itfApCollectIfaceMapper.selectcode(tenantId, hmeEoJobSns.get(0).getJobId());
            log.info("ItfFacCollectIfaceServiceImpl.dataType"+apCollectItfDTO1s);
            log.info("ItfFacCollectIfaceServiceImpl.select.end");

            //2021-07-28 18:34 edit by chaonan.hu for tianyang.xie 增加表HmeEoTestDataRecord的处理逻辑
            List<HmeEoTestDataRecord> hmeEoTestDataRecordList = new ArrayList<>();
            HmeEoJobSn hmeEoJobSn = hmeEoJobSns.get(0);
            for (ItfFacCollectIface data : list) {
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
            log.info("ItfFacCollectIfaceServiceImpl.data.end");

            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(jobDataRecordList)) {
                batchUpdateResult(tenantId, jobDataRecordList);
            }
            if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(hmeEoTestDataRecordList)){
                hmeEoTestDataRecordRepository.batchSaveTestDataRecord(tenantId, hmeEoTestDataRecordList);
            }
            log.info("ItfFacCollectIfaceServiceImpl.batchUpdate.end");

        }

        // 返回处理结果
        return InterfaceUtils.getReturnList(list);
    }

    /**
     * @param temp
     * @param data
     * @return java.lang.String
     * @description 返回要取值的字段
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/29 20:54
     **/
    private String getResult(ApCollectItfDTO1 temp, ItfFacCollectIface data) {
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
     *@description 获取对应的值
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/15 20:14
     *@param fieldName
     *@param object
     *@return java.lang.String
     **/
    private static String getFieldValueByFieldName(String fieldName, Object object) {
        try {
            String fieldName1 = camelName(fieldName);
            Field field = object.getClass().getDeclaredField(fieldName1);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            Object o = field.get(object);
            if (o instanceof Date) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(o);
            }
            return o.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *@description 命名转化
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/15 20:15
     *@param name
     *@return java.lang.String
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
     *@description
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/16 14:21
     *@param tenantId
     *@param dataRecordList
     *@return void
     **/
    public void batchUpdateResult(Long tenantId, List<HmeEoJobDataRecord> dataRecordList) {
        int batchNum = 500;
//        Long userId = (Objects.nonNull(DetailsHelper.getUserDetails()) && Objects.nonNull(DetailsHelper.getUserDetails().getUserId()))
//                ? DetailsHelper.getUserDetails().getUserId() : -1L;

        //V20211129 modify by penglin.sui 将获取用户提取到循环外
        Long userId = -1L;
        CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
        if(Objects.nonNull(customUserDetails)
                && Objects.nonNull(customUserDetails.getUserId())){
            userId = customUserDetails.getUserId();
        }
        List<List<HmeEoJobDataRecord>> list = InterfaceUtils.splitSqlList(dataRecordList, batchNum);
        for (List<HmeEoJobDataRecord> item : list
             ) {
            hmeEoJobDataRecordMapper.batchUpdateResult(tenantId, userId, item);
        }
    }

    /**
     *@description 批量新增
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/7 16:42
     *@param insertList
     *@return void
     **/
    public void myBatchInsert(List<ItfFacCollectIface> insertList) {
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(insertList)) {
            List<List<ItfFacCollectIface>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfFacCollectIface> domains : splitSqlList) {
                itfFacCollectIfaceMapper.insertIface(domains);
            }
        }
    }
}
