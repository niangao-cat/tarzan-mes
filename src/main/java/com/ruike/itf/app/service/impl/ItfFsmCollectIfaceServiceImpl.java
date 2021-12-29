package com.ruike.itf.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.entity.HmeEquipmentWkcRel;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.repository.HmeEquipmentWkcRelRepository;
import com.ruike.hme.domain.repository.HmeMaterialTransferRepository;
import com.ruike.hme.infra.mapper.HmeEoJobDataRecordMapper;
import com.ruike.itf.api.dto.ApCollectItfDTO1;
import com.ruike.itf.api.dto.AtpCollectItfDTO;
import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.FsmCollectItfDTO;
import com.ruike.itf.app.service.ItfFsmCollectIfaceService;
import com.ruike.itf.domain.entity.ItfAtpCollectIface;
import com.ruike.itf.domain.entity.ItfFsmCollectIface;
import com.ruike.itf.domain.repository.ItfFsmCollectIfaceRepository;
import com.ruike.itf.infra.mapper.ItfApCollectIfaceMapper;
import com.ruike.itf.infra.mapper.ItfAtpCollectIfaceMapper;
import com.ruike.itf.infra.mapper.ItfFsmCollectIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.service.BaseServiceImpl;
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

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 熔接机数据采集接口表应用服务默认实现
 *
 * @author yonghui.zhu@hand-china.com 2020-07-13 18:36:01
 */
@Service
@Slf4j
public class ItfFsmCollectIfaceServiceImpl extends BaseServiceImpl<ItfFsmCollectIface> implements ItfFsmCollectIfaceService {

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;

    @Autowired
    private HmeEquipmentWkcRelRepository hmeEquipmentWkcRelRepository;

    @Autowired
    private ItfFsmCollectIfaceMapper itfFsmCollectIfaceMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeEoJobDataRecordMapper hmeEoJobDataRecordMapper;

    @Autowired
    private ItfApCollectIfaceMapper itfApCollectIfaceMapper;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;

    /**
     * 接口数据验证
     *
     * @param itfList  接口数据
     * @param tenantId 租户
     * @return boolean
     * @author Aidan.Zhu yonghui.zhu@hand-china.com
     * @date 2020/7/13 7:56 下午
     */
    private boolean validate(Long tenantId, List<ItfFsmCollectIface> itfList) {
        boolean validFlag = true;
        List<ItfFsmCollectIface> updateList=new ArrayList<>();

        for (ItfFsmCollectIface itf : itfList) {
            String processMessage = "";

            // 验证字段
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getAssetEncoding()), processMessage, "ITF_DATA_COLLECT_0001");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getEquipmentCategory()), processMessage, "ITF_DATA_COLLECT_0002");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getSn()), processMessage, "ITF_DATA_COLLECT_0003");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getFsmFusionSpot()), processMessage, "ITF_DATA_COLLECT_0004");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getFsmCutangleLeft()), processMessage, "ITF_DATA_COLLECT_0005");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getFsmCutangleRight()), processMessage, "ITF_DATA_COLLECT_0006");
            if (StringUtils.isNotBlank(processMessage)) {
                itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                itf.setProcessMessage(processMessage);
                itf.setProcessDate(new Date());
                updateList.add(itf);
                validFlag = false;
            }
        }
        if(!CollectionUtils.isEmpty(updateList)) {
            myBatchUpdate(updateList);
        }
        return validFlag;
    }

    @Override
    public List<DataCollectReturnDTO> invoke(Long tenantId, List<FsmCollectItfDTO> collectList) {
        // 插入接口表
        List<ItfFsmCollectIface> list = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(FsmCollectItfDTO.class, ItfFsmCollectIface.class, false);
        Date nowDate = new Date();
        List<String> ifaceIdList = customSequence.getNextKeys("itf_fsm_collect_iface_s", collectList.size());
        int ifaceIdIndex = 0;
        for (FsmCollectItfDTO data : collectList) {
            ItfFsmCollectIface itf = new ItfFsmCollectIface();
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
        log.info("ItfFsmCollectIfaceServiceImpl.insterIface.start");
        myBatchInsert(list);
        log.info("ItfFsmCollectIfaceServiceImpl.insterIface.end");

        // 验证接口表
        log.info("ItfFsmCollectIfaceServiceImpl.check.start");
        boolean validFlag = this.validate(tenantId, list);
        log.info("ItfFsmCollectIfaceServiceImpl.check.end");


        if (validFlag) {
            List<ItfFsmCollectIface> updateList=new ArrayList<>();

            // 执行插入业务表逻辑，如果有必要
            //根据设备找工位
            log.info("ItfFsmCollectIfaceServiceImpl.select.start");
            HmeEquipment hmeEquipmentFirst = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
                setTenantId(tenantId);
                setAssetEncoding(list.get(0).getAssetEncoding());
            }});
            log.info("ItfFsmCollectIfaceServiceImpl.hmeEquipmentFirst :"+hmeEquipmentFirst);
            if (ObjectUtil.isEmpty(hmeEquipmentFirst)) {
                for (ItfFsmCollectIface data : list) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_DATA_COLLECT_0011", "ITF", data.getAssetEncoding()));
                    updateList.add(data);
                }
                if(!CollectionUtils.isEmpty(updateList)) {
                    myBatchUpdate(updateList);
                }
                return InterfaceUtils.getReturnList(list);
            }

            //检查设备是否工位共用 1：共用；2：专用
            List<String> OperationIds = new ArrayList<>();
            if(hmeEquipmentFirst.getApplyType().equals("2")) {
                List<HmeEquipmentWkcRel> select = hmeEquipmentWkcRelRepository.select(new HmeEquipmentWkcRel() {{
                    setEquipmentId(hmeEquipmentFirst.getEquipmentId());
                }});
                if (CollectionUtils.isEmpty(select)) {
                    for (ItfFsmCollectIface data : list) {
                        data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                        data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "ITF_DATA_COLLECT_0012", "ITF", data.getAssetEncoding()));
                        updateList.add(data);
                    }
                    if (!CollectionUtils.isEmpty(updateList)) {
                        myBatchUpdate(updateList);
                    }
                    return InterfaceUtils.getReturnList(list);

                }
                String wkcId = select.get(0).getStationId();
                log.info("ItfFsmCollectIfaceServiceImpl.wkcId :" + wkcId);

                //根据工位找工艺
                OperationIds = itfApCollectIfaceMapper.selectOperation(tenantId, wkcId);
                if (CollectionUtils.isEmpty(OperationIds)) {
                    for (ItfFsmCollectIface data : list) {
                        data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                        data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_021", "HME"));
                        updateList.add(data);
                    }
                    if (!CollectionUtils.isEmpty(updateList)) {
                        myBatchUpdate(updateList);
                    }
                    return InterfaceUtils.getReturnList(list);
                }
                log.info("ItfFsmCollectIfaceServiceImpl.OperationIds :" + OperationIds);
            }
            List<HmeEoJobDataRecord> jobDataRecordList = new ArrayList<>();
            //查找
            MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, list.get(0).getSn());
            log.info("ItfFsmCollectIfaceServiceImpl.materialLot :"+materialLot);

            if (ObjectUtils.isEmpty(materialLot)) {
                for (ItfFsmCollectIface data : list) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SERVICE_DATA_RECORD_001", "HME", data.getSn()));
                    updateList.add(data);
                }
                if(!CollectionUtils.isEmpty(updateList)) {
                    myBatchUpdate(updateList);
                }
                return InterfaceUtils.getReturnList(list);
            }
//            if (ObjectUtils.isEmpty(materialLot.getEoId())) {
//                for (ItfFsmCollectIface data : list) {
//                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
//                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "ITF_DATA_COLLECT_0013", "ITF", data.getSn()));
//                    updateList.add(data);
//                }
//                if(!CollectionUtils.isEmpty(updateList)) {
//                    myBatchUpdate(updateList);
//                }
//                return InterfaceUtils.getReturnList(list);
//            }
            String reWorkFlag = "N";
            MtExtendSettings mtExtendSettings = new MtExtendSettings();
            mtExtendSettings.setAttrName("REWORK_FLAG");
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLot.getMaterialLotId(), Collections.singletonList(mtExtendSettings));
            log.info("ItfFsmCollectIfaceServiceImpl.REWORK_FLAG :"+mtExtendAttrVOList);
            for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOList) {
                if ("REWORK_FLAG".equals(mtExtendAttrVO.getAttrName())) {
                    if (StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())) {
                        reWorkFlag = mtExtendAttrVO.getAttrValue();
                    }
                }
            }

            //工位共用的设备，根据设备找工位，根据工位找工艺逻辑就有问题了 修改 BY 田欣 2021-11-05 品质部王进反馈
            List<HmeEoJobSn> hmeEoJobSns = new ArrayList<>();
            if (hmeEquipmentFirst.getApplyType().equals("2")) {
                hmeEoJobSns = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)

                                //返修作业使用原SN进站作业，但记录的EO为返修新EO，用原EO查不到返修作业记录，导致无法采集数据
                                // 修改 By 田欣 2020-10-08 品质部王进反馈返修作业无法采集数据
//                            .andEqualTo(HmeEoJobSn.FIELD_EO_ID, materialLot.getEoId())
                                .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLot.getMaterialLotId())
                                .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, OperationIds.get(0))
                                .andEqualTo(HmeEoJobSn.FIELD_REWORK_FLAG, reWorkFlag)
                                .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                        .orderBy(HmeEoJobSn.FIELD_LAST_UPDATE_DATE)
                        .build());
            }else {
                hmeEoJobSns = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLot.getMaterialLotId())
                                .andEqualTo(HmeEoJobSn.FIELD_REWORK_FLAG, reWorkFlag)
                                .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                        .orderBy(HmeEoJobSn.FIELD_LAST_UPDATE_DATE)
                        .build());
                //检查当前作业记录的工位和设备有无关联
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(hmeEoJobSns)) {
                    String workcellId = hmeEoJobSns.get(0).getWorkcellId();
                    List<HmeEquipmentWkcRel> select = hmeEquipmentWkcRelRepository.select(new HmeEquipmentWkcRel() {{
                        setEquipmentId(hmeEquipmentFirst.getEquipmentId());
                        setStationId(workcellId);
                    }});
                    if (select == null){
                        hmeEoJobSns = null;
                    }
                }
            }
            log.info("ItfFsmCollectIfaceServiceImpl.hmeEoJobSns :"+hmeEoJobSns);
            if (ObjectUtils.isEmpty(hmeEoJobSns)) {
                for (ItfFsmCollectIface data : list) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_DATA_COLLECT_0014", "ITF", data.getSn()));
                    updateList.add(data);
                }
                if(!CollectionUtils.isEmpty(updateList)) {
                    myBatchUpdate(updateList);
                }
                return InterfaceUtils.getReturnList(list);
            }
            List<ApCollectItfDTO1> apCollectItfDTO1s = itfApCollectIfaceMapper.selectcode(tenantId, hmeEoJobSns.get(0).getJobId());
            log.info("ItfFsmCollectIfaceServiceImpl.dataType"+apCollectItfDTO1s);
            log.info("ItfFsmCollectIfaceServiceImpl.select.end");

            for (ItfFsmCollectIface data : list) {
                for (ApCollectItfDTO1 temp :
                        apCollectItfDTO1s) {
                    String valueField = getResult(temp, data);
                    if (!StringUtils.isBlank(valueField)) {
                        String result = getFieldValueByFieldName(valueField, data);
                        HmeEoJobDataRecord eoJobDataRecord = new HmeEoJobDataRecord();
                        eoJobDataRecord.setJobRecordId(temp.getJobRecordId());
                        eoJobDataRecord.setResult(result);
                        jobDataRecordList.add(eoJobDataRecord);
                    }
                }
            }
            log.info("ItfFsmCollectIfaceServiceImpl.data.end");

            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(jobDataRecordList)) {
                batchUpdateResult(tenantId, jobDataRecordList);
            }
            log.info("ItfFsmCollectIfaceServiceImpl.batchUpdate.end");

        }

        // 返回处理结果
        return InterfaceUtils.getReturnList(list);
    }

    public void myBatchInsert(List<ItfFsmCollectIface> insertList) {
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(insertList)) {
            List<List<ItfFsmCollectIface>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfFsmCollectIface> domains : splitSqlList) {
                itfFsmCollectIfaceMapper.insertIface(domains);
            }
        }
    }

    /**
     * @param temp
     * @param data
     * @return java.lang.String
     * @description 返回要取值的字段
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/29 20:54
     **/
    private String getResult(ApCollectItfDTO1 temp, ItfFsmCollectIface data) {
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
            return name.substring(0, 1).toLowerCase() + name.substring(1);
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
        Long userId = (Objects.nonNull(DetailsHelper.getUserDetails()) && Objects.nonNull(DetailsHelper.getUserDetails().getUserId()))
                ? DetailsHelper.getUserDetails().getUserId() : -1L;
        List<List<HmeEoJobDataRecord>> list = InterfaceUtils.splitSqlList(dataRecordList, batchNum);
        list.forEach(item -> hmeEoJobDataRecordMapper.batchUpdateResult(tenantId, userId, item));
    }

    /**
     *@description 批量更新
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/7 16:42
     *@param updateList
     *@return void
     **/
    public void myBatchUpdate(List<ItfFsmCollectIface> updateList) {
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(updateList)) {
            List<List<ItfFsmCollectIface>> splitSqlList = InterfaceUtils.splitSqlList(updateList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfFsmCollectIface> domains : splitSqlList) {
                itfFsmCollectIfaceMapper.updateIface(domains);
            }
        }
    }

}
