package com.ruike.itf.app.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.infra.mapper.HmeCosFunctionMapper;
import com.ruike.hme.infra.mapper.HmeMaterialLotLoadMapper;
import com.ruike.hme.infra.mapper.HmeMaterialLotNcLoadMapper;
import com.ruike.hme.infra.mapper.HmeMaterialLotNcRecordMapper;
import com.ruike.itf.api.dto.CosCollectItfDTO;
import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.app.service.ItfCosCollectIfaceService;
import com.ruike.itf.domain.entity.ItfCosCollectIface;
import com.ruike.itf.domain.repository.ItfCosCollectIfaceRepository;
import com.ruike.itf.infra.mapper.ItfCosCollectIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.repository.MtNcCodeRepository;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * cos测试数据采集接口表应用服务默认实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-28 11:18:23
 */
@Service
@Slf4j
public class ItfCosCollectIfaceServiceImpl extends BaseServiceImpl<ItfCosCollectIface> implements ItfCosCollectIfaceService {

    private final ItfCosCollectIfaceRepository itfCosCollectIfaceRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeCosFunctionRepository hmeCosFunctionRepository;

    @Autowired
    private ItfCosCollectIfaceMapper itfCosCollectIfaceMapper;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;

    @Autowired
    private HmeMaterialLotNcLoadRepository  hmeMaterialLotNcLoadRepository;

    @Autowired
    private HmeMaterialLotNcRecordRepository hmeMaterialLotNcRecordRepository;

    @Autowired
    private HmeMaterialLotNcLoadMapper hmeMaterialLotNcLoadMapper;

    @Autowired
    private HmeCosFunctionMapper hmeCosFunctionMapper;

    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;

    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;

    @Autowired
    private HmeCosNcRecordRepository hmeCosNcRecordRepository;

    @Autowired
    public ItfCosCollectIfaceServiceImpl(ItfCosCollectIfaceRepository itfCosCollectIfaceRepository, MtErrorMessageRepository mtErrorMessageRepository) {
        this.itfCosCollectIfaceRepository = itfCosCollectIfaceRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    private boolean validate(Long tenantId, List<ItfCosCollectIface> itfList) {

        boolean validFlag = true;
        // 反射得到字段map
        Map<String, Field> fieldMap = Arrays.stream(ItfCosCollectIface.class.getDeclaredFields()).collect(Collectors.toMap(Field::getName, rec -> rec, (key1, keys2) -> key1));
        for (ItfCosCollectIface itf : itfList) {
            String processMessage = "";
            // 验证字段
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getAssetEncoding()), processMessage, "ITF_DATA_COLLECT_0001");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getSn()), processMessage, "ITF_DATA_COLLECT_0003");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getCosLocation()), processMessage, "ITF_DATA_COLLECT_0007", fieldMap.get(ItfCosCollectIface.FIELD_COS_LOCATION).getAnnotation(ApiModelProperty.class).value());

            if (StringUtils.isNotBlank(processMessage)) {
                itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                itf.setProcessMessage(processMessage);
                itf.setProcessDate(new Date());
                itfCosCollectIfaceRepository.updateByPrimaryKeySelective(itf);
                validFlag = false;
            }
        }
        return validFlag;
    }

    @Override
    public List<DataCollectReturnDTO> invoke(Long tenantId, List<CosCollectItfDTO> collectList) {
        log.info("<====ItfCosCollectIfaceServiceImpl-invoke.start:{},{}", tenantId, collectList.toString());

        if (CollectionUtils.isEmpty(collectList)) {
            return new ArrayList<>();
        }
        // 插入接口表
        List<ItfCosCollectIface> list = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(CosCollectItfDTO.class, ItfCosCollectIface.class, false);
        log.info("<====ItfCosCollectIfaceServiceImpl-invoke.1");

        Date nowDate = new Date();
        for (CosCollectItfDTO data : collectList) {
            ItfCosCollectIface itf = new ItfCosCollectIface();
            copier.copy(data, itf, null);
            itf.setProcessDate(nowDate);
            itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_NEW);
            list.add(itf);
        }
        log.info("<====ItfCosCollectIfaceServiceImpl-invoke.2");
        list.forEach(this::insertSelective);
        log.info("<====ItfCosCollectIfaceServiceImpl-invoke.icae");

        // 验证接口表
        boolean validFlag = this.validate(tenantId, list);
        log.info("<====ItfCosCollectIfaceServiceImpl-check");
        List<String> ncCodeList = new ArrayList<>();

        if (validFlag) {

            log.info("<====ItfCosCollectIfaceServiceImpl-select.1");
            MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, list.get(0).getSn());
            log.info("<====ItfCosCollectIfaceServiceImpl-select.2" + materialLot);
            if (ObjectUtils.isEmpty(materialLot)) {
                for (ItfCosCollectIface data : list) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SERVICE_DATA_RECORD_001", "HME", data.getSn()));
                    this.updateByPrimaryKeySelective(data);
                }
            } else {
                HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                hmeMaterialLotLoad.setLoadRow(getNewLoadRow(list.get(0).getCosLocation().substring(0, 1)));
                hmeMaterialLotLoad.setLoadColumn(Long.valueOf(list.get(0).getCosLocation().substring(1, 2)));
                hmeMaterialLotLoad.setMaterialLotId(materialLot.getMaterialLotId());
                List<HmeMaterialLotLoad> select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
                log.info("<====ItfCosCollectIfaceServiceImpl-select.3" + select);
                if (CollectionUtils.isEmpty(select)) {
                    for (ItfCosCollectIface data : list) {

                        data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                        data.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "ITF_DATA_COLLECT_0015", "ITF", data.getSn()));
                        this.updateByPrimaryKeySelective(data);
                    }
                } else {
                    HmeCosFunction hmeCosFunction2 = new HmeCosFunction();
                    hmeCosFunction2.setLoadSequence(select.get(0).getLoadSequence());
                    List<HmeCosFunction> select1 = hmeCosFunctionRepository.select(hmeCosFunction2);
                    // 执行插入业务表逻辑，如果有必要
                    //处理到性能表
                    List<String> IdList = customSequence.getNextKeys("hme_cos_function_s", list.size());
                    List<String> cidList = customSequence.getNextKeys("hme_cos_function_cid_s", list.size());
                    int loadIndex = 0;
                    List<HmeCosFunction> hmeCosFunctions = new ArrayList<>();
                    for (ItfCosCollectIface data : list) {
                        if (StringUtils.isNotEmpty(data.getCosNcCode())) {
                            String[] array = data.getCosNcCode().split(",");
                            ncCodeList.addAll(Arrays.asList(array));
                        }
                        HmeCosFunction hmeCosFunction = new HmeCosFunction();
                        hmeCosFunction.setLoadSequence(select.get(0).getLoadSequence());
                        hmeCosFunction.setCurrent(data.getCosCurrent().toString());
                        if (!CollectionUtils.isEmpty(select1)) {
                            List<HmeCosFunction> collect = select1.stream().filter(t -> t.getCurrent().equals(data.getCosCurrent().toString())).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(collect)) {
                                hmeCosFunction.setCosFunctionId(collect.get(0).getCosFunctionId());
                            }
                        }
                        if (StringUtils.isNotBlank(hmeCosFunction.getCosFunctionId())) {
                            hmeCosFunction.setA01(data.getCosPowerLevel());
                            hmeCosFunction.setA02(data.getCosPower());
                            hmeCosFunction.setA03(data.getCosWavelengthLevel());
                            hmeCosFunction.setA04(data.getCosCenterWavelength());
                            hmeCosFunction.setA05(data.getCosWavelengthDiffer());
                            hmeCosFunction.setA06(data.getCosVoltage());
                            hmeCosFunction.setA08(data.getAssetEncoding());
                            hmeCosFunction.setA09(data.getCosModel());
                            hmeCosFunction.setA010(data.getCosThrescholdCurrent());
                            hmeCosFunction.setA011(data.getCosThrescholdVoltage());
                            hmeCosFunction.setA012(data.getCosSe());
                            hmeCosFunction.setA013(data.getCosLinewidth());
                            hmeCosFunction.setA014(data.getCosIpce());
                            hmeCosFunction.setA15(data.getCosPolarization());
                            hmeCosFunction.setA16(data.getCosFwhmX());
                            hmeCosFunction.setA17(data.getCosFwhmY());
                            hmeCosFunction.setA18(data.getCos86x());
                            hmeCosFunction.setA19(data.getCos86y());
                            hmeCosFunction.setA20(data.getCos95x());
                            hmeCosFunction.setA21(data.getCos95y());
                            hmeCosFunction.setA22(data.getCosLensPower());
                            hmeCosFunction.setA23(data.getCosPbsPower());
                            hmeCosFunction.setA24(data.getCosNcCode());
                            hmeCosFunction.setA25(data.getCosOperator());
                            hmeCosFunction.setA26(data.getCosRemark());
                            hmeCosFunction.setA27(data.getCosVoltageLevel());
                            hmeCosFunction.setAttribute1(data.getCosAttribute1());
                            hmeCosFunction.setAttribute2(data.getCosAttribute2());
                            hmeCosFunction.setAttribute3(data.getCosAttribute3());
                            hmeCosFunction.setAttribute4(data.getCosAttribute4());
                            hmeCosFunction.setAttribute5(data.getCosAttribute5());
                            hmeCosFunction.setAttribute6(data.getCosAttribute6());
                            hmeCosFunction.setAttribute7(data.getCosAttribute7());
                            hmeCosFunction.setAttribute8(data.getCosAttribute8());
                            hmeCosFunction.setAttribute9(data.getCosAttribute9());
                            hmeCosFunction.setAttribute10(data.getCosAttribute10());
                            hmeCosFunctionMapper.updateByPrimaryKeySelective(hmeCosFunction);
                        } else {
                            hmeCosFunction.setTenantId(tenantId);
                            hmeCosFunction.setCosFunctionId(IdList.get(loadIndex));
                            hmeCosFunction.setSiteId(materialLot.getSiteId());
                            hmeCosFunction.setA01(data.getCosPowerLevel());
                            hmeCosFunction.setA02(data.getCosPower());
                            hmeCosFunction.setA03(data.getCosWavelengthLevel());
                            hmeCosFunction.setA04(data.getCosCenterWavelength());
                            hmeCosFunction.setA05(data.getCosWavelengthDiffer());
                            hmeCosFunction.setA06(data.getCosVoltage());
                            hmeCosFunction.setA08(data.getAssetEncoding());
                            hmeCosFunction.setA09(data.getCosModel());
                            hmeCosFunction.setA010(data.getCosThrescholdCurrent());
                            hmeCosFunction.setA011(data.getCosThrescholdVoltage());
                            hmeCosFunction.setA012(data.getCosSe());
                            hmeCosFunction.setA013(data.getCosLinewidth());
                            hmeCosFunction.setA014(data.getCosIpce());
                            hmeCosFunction.setA15(data.getCosPolarization());
                            hmeCosFunction.setA16(data.getCosFwhmX());
                            hmeCosFunction.setA17(data.getCosFwhmY());
                            hmeCosFunction.setA18(data.getCos86x());
                            hmeCosFunction.setA19(data.getCos86y());
                            hmeCosFunction.setA20(data.getCos95x());
                            hmeCosFunction.setA21(data.getCos95y());
                            hmeCosFunction.setA22(data.getCosLensPower());
                            hmeCosFunction.setA23(data.getCosPbsPower());
                            hmeCosFunction.setA24(data.getCosNcCode());
                            hmeCosFunction.setA25(data.getCosOperator());
                            hmeCosFunction.setA26(data.getCosRemark());
                            hmeCosFunction.setA27(data.getCosVoltageLevel());
                            hmeCosFunction.setCid(Long.valueOf(cidList.get(loadIndex++)));
                            hmeCosFunction.setObjectVersionNumber(1L);
                            hmeCosFunction.setCreatedBy(-1L);
                            hmeCosFunction.setCreationDate(new Date());
                            hmeCosFunction.setLastUpdatedBy(-1L);
                            hmeCosFunction.setLastUpdateDate(new Date());
                            hmeCosFunction.setAttribute1(data.getCosAttribute1());
                            hmeCosFunction.setAttribute2(data.getCosAttribute2());
                            hmeCosFunction.setAttribute3(data.getCosAttribute3());
                            hmeCosFunction.setAttribute4(data.getCosAttribute4());
                            hmeCosFunction.setAttribute5(data.getCosAttribute5());
                            hmeCosFunction.setAttribute6(data.getCosAttribute6());
                            hmeCosFunction.setAttribute7(data.getCosAttribute7());
                            hmeCosFunction.setAttribute8(data.getCosAttribute8());
                            hmeCosFunction.setAttribute9(data.getCosAttribute9());
                            hmeCosFunction.setAttribute10(data.getCosAttribute10());
                            hmeCosFunctions.add(hmeCosFunction);
                        }
                    }
                    if (!CollectionUtils.isEmpty(hmeCosFunctions)) {
                        log.info("<====ItfCosCollectIfaceServiceImpl-insert.start");
                        myBatchInsert(hmeCosFunctions);
                        log.info("<====ItfCosCollectIfaceServiceImpl-insert.end");
                    }
                    //2021-05-08 11:17 add by chaonan.hu for zhenyong.ban 更新HmeMaterialLotLoad的ATTRIBUTE15为Y
                    HmeMaterialLotLoad hmeMaterialLotLoad2 = select.get(0);
                    if(!"Y".equals(hmeMaterialLotLoad2.getAttribute15())){
                        hmeMaterialLotLoad2.setAttribute15("Y");
                        //2021-07-14 10:30 edit by chaonan.hu for peng.zhao 将当前时间赋值为测试时间
                        hmeMaterialLotLoad2.setTestDate(new Date());
                    }
                    //2021-08-02 15:02 edit by chaonan.hu for peng.zhao 将测试机台记录到装载表的ATTRIBUTE16上
                    hmeMaterialLotLoad2.setAttribute16(list.get(0).getAssetEncoding());
                    hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(hmeMaterialLotLoad2);
                    //不良代码为空删除原来的不良，不为空更新
                    if (!CollectionUtils.isEmpty(ncCodeList)) {
                        String ncLoadId;
                        HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
                        hmeMaterialLotNcLoad.setLoadSequence(select.get(0).getLoadSequence());
                        List<HmeMaterialLotNcLoad> select2 = hmeMaterialLotNcLoadRepository.select(hmeMaterialLotNcLoad);
                        if (CollectionUtils.isEmpty(select2)) {
                            hmeMaterialLotNcLoad.setLoadNum("1");
                            hmeMaterialLotNcLoadRepository.insertSelective(hmeMaterialLotNcLoad);
                            ncLoadId = hmeMaterialLotNcLoad.getNcLoadId();
                        } else {
                            HmeMaterialLotNcLoad hmeMaterialLotNcLoad1 = select2.get(0);
                            hmeMaterialLotNcLoad1.setLoadNum("1");
                            hmeMaterialLotNcLoadMapper.updateByPrimaryKeySelective(hmeMaterialLotNcLoad1);
                            ncLoadId = hmeMaterialLotNcLoad1.getNcLoadId();
                            HmeMaterialLotNcRecord hmeMaterialLotNcRecord1 = new HmeMaterialLotNcRecord();
                            hmeMaterialLotNcRecord1.setNcLoadId(ncLoadId);
                            List<HmeMaterialLotNcRecord> deleteList = hmeMaterialLotNcRecordRepository.select(hmeMaterialLotNcRecord1);
                            if (!CollectionUtils.isEmpty(deleteList)) {
                                hmeMaterialLotNcRecordRepository.batchDeleteByPrimaryKey(deleteList);
                            }
                            //2021-05-21 09:39 add by chaonan.hu for zhenyong.ban 更新时先删除HmeCosNcRecord表
                            if(StringUtils.isNotBlank(select.get(0).getLoadSequence())){
                                List<HmeCosNcRecord> hmeCosNcRecordList = hmeCosNcRecordRepository.select(new HmeCosNcRecord() {{
                                    setTenantId(tenantId);
                                    setLoadSequence(select.get(0).getLoadSequence());
                                }});
                                if(CollectionUtil.isNotEmpty(hmeCosNcRecordList)){
                                    hmeCosNcRecordRepository.batchDeleteByPrimaryKey(hmeCosNcRecordList);
                                }
                            }
                        }
                        List<String> ncCodeListTemp = ncCodeList.stream().distinct().collect(Collectors.toList());
                        for (String ncCode :
                                ncCodeListTemp) {
                            HmeMaterialLotNcRecord hmeMaterialLotNcRecord = new HmeMaterialLotNcRecord();
                            hmeMaterialLotNcRecord.setNcLoadId(ncLoadId);
                            hmeMaterialLotNcRecord.setNcCode(ncCode);
                            hmeMaterialLotNcRecordRepository.insertSelective(hmeMaterialLotNcRecord);
                            //2021-05-21 09:39 add by chaonan.hu for zhenyong.ban 新增HmeCosNcRecord表
                            MtNcCode mtNcCode = mtNcCodeRepository.selectOne(new MtNcCode() {{
                                setTenantId(tenantId);
                                setNcCode(ncCode);
                            }});
                            if(Objects.nonNull(mtNcCode)){
                                HmeCosNcRecord hmeCosNcRecord = new HmeCosNcRecord();
                                hmeCosNcRecord.setTenantId(tenantId);
                                hmeCosNcRecord.setSiteId(materialLot.getSiteId());
                                hmeCosNcRecord.setUserId(-1L);
                                hmeCosNcRecord.setMaterialLotId(materialLot.getMaterialLotId());
                                hmeCosNcRecord.setDefectCount(BigDecimal.valueOf(1));
                                hmeCosNcRecord.setNcCodeId(mtNcCode.getNcCodeId());
                                hmeCosNcRecord.setNcType(mtNcCode.getNcType());
                                hmeCosNcRecord.setComponentMaterialId(materialLot.getMaterialId());
                                hmeCosNcRecord.setOperationId("-1");
                                hmeCosNcRecord.setWorkcellId("-1");
                                hmeCosNcRecord.setLoadSequence(select.get(0).getLoadSequence());
                                hmeCosNcRecord.setHotSinkCode(select.get(0).getHotSinkCode());
                                hmeCosNcRecord.setWorkOrderId(select.get(0).getAttribute3());
                                hmeCosNcRecord.setWaferNum(select.get(0).getAttribute2());
                                hmeCosNcRecord.setCosType(select.get(0).getAttribute1());
                                hmeCosNcRecord.setNcLoadRow(select.get(0).getLoadRow());
                                hmeCosNcRecord.setNcLoadColumn(select.get(0).getLoadColumn());
                                hmeCosNcRecord.setStatus("Y");
                                hmeCosNcRecord.setLoadNum("1");
                                hmeCosNcRecordRepository.insertSelective(hmeCosNcRecord);
                            }
                        }
                    } else {
                        HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
                        hmeMaterialLotNcLoad.setLoadSequence(select.get(0).getLoadSequence());
                        List<HmeMaterialLotNcLoad> select2 = hmeMaterialLotNcLoadRepository.select(hmeMaterialLotNcLoad);
                        if (!CollectionUtils.isEmpty(select2)) {
                            HmeMaterialLotNcRecord hmeMaterialLotNcRecord1 = new HmeMaterialLotNcRecord();
                            hmeMaterialLotNcRecord1.setNcLoadId(select2.get(0).getNcLoadId());
                            List<HmeMaterialLotNcRecord> deleteList = hmeMaterialLotNcRecordRepository.select(hmeMaterialLotNcRecord1);
                            if (!CollectionUtils.isEmpty(deleteList)) {
                                hmeMaterialLotNcRecordRepository.batchDeleteByPrimaryKey(deleteList);
                            }
                            hmeMaterialLotNcLoadRepository.batchDeleteByPrimaryKey(select2);
                        }
                    }
                }
            }
        }

        // 返回处理结果
        return InterfaceUtils.getReturnList(list);
    }

    /**
     *@description 获取对应的位置
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/7 16:43
     *@param substring
     *@return java.lang.Long
     **/
    private Long getNewLoadRow(String substring) {
        long row = 0L;
        switch (substring) {
            case "A":
                row = 1L;
                break;
            case "B":
                row = 2L;
                break;
            case "C":
                row = 3L;
                break;
            case "D":
                row = 4L;
                break;
            case "E":
                row = 5L;
                break;
            case "F":
                row = 6L;
                break;
            case "G":
                row = 7L;
                break;
        }
        return row;
    }

    /**
     *@description 批量新增
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/7 16:42
     *@param insertList
     *@return void
     **/
    public void myBatchInsert(List<HmeCosFunction> insertList) {
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(insertList)) {
            List<List<HmeCosFunction>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeCosFunction> domains : splitSqlList) {
                itfCosCollectIfaceMapper.insertFunction(domains);

            }
        }
    }
}
