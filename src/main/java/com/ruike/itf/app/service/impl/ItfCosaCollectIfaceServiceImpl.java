package com.ruike.itf.app.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ruike.hme.api.dto.HmePreSelectionDTO6;
import com.ruike.hme.api.dto.HmePreSelectionReturnDTO4;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.domain.repository.HmePreSelectionRepository;
import com.ruike.hme.domain.repository.HmeVirtualNumRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeMaterialLotLoadMapper;
import com.ruike.hme.infra.mapper.HmePreSelectionMapper;
import com.ruike.hme.infra.mapper.HmeSelectionDetailsMapper;
import com.ruike.hme.infra.mapper.HmeVirtualNumMapper;
import com.ruike.itf.api.dto.AtpCollectItfDTO;
import com.ruike.itf.api.dto.CosaCollectItfDTO;
import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.app.service.ItfCosaCollectIfaceService;
import com.ruike.itf.domain.entity.ItfApCollectIface;
import com.ruike.itf.domain.entity.ItfAtpCollectIface;
import com.ruike.itf.domain.entity.ItfCosCollectIface;
import com.ruike.itf.domain.entity.ItfCosaCollectIface;
import com.ruike.itf.infra.mapper.ItfAtpCollectIfaceMapper;
import com.ruike.itf.infra.mapper.ItfCosaCollectIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import parsii.tokenizer.Char;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.modeling.domain.entity.MtModLocator;

import javax.print.DocFlavor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * 芯片转移接口表应用服务默认实现
 *
 * @author wenzhang.yu@hand-china.com 2021-01-21 14:53:19
 */
@Service
@Slf4j
public class ItfCosaCollectIfaceServiceImpl extends BaseServiceImpl<ItfCosaCollectIface> implements ItfCosaCollectIfaceService {

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private ItfCosaCollectIfaceMapper itfCosaCollectIfaceMapper;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeVirtualNumRepository hmeVirtualNumRepository;

    @Autowired
    private HmePreSelectionMapper hmePreSelectionMapper;

    @Autowired
    private HmeVirtualNumMapper hmeVirtualNumMapper;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeSelectionDetailsMapper hmeSelectionDetailsMapper;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;

    @Autowired
    private ProfileClient profileClient;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private HmePreSelectionRepository hmePreSelectionRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DataCollectReturnDTO> invoke(Long tenantId, List<CosaCollectItfDTO> collectList) {
        log.info("ItfCosaCollectIfaceServiceImpl.invoke.start");
        if (CollectionUtils.isEmpty(collectList)) {
            return new ArrayList<>();
        }
        // 插入接口表
        List<ItfCosaCollectIface> list = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(CosaCollectItfDTO.class, ItfCosaCollectIface.class, false);
        Date nowDate = new Date();
        List<String> ifaceIdList = customSequence.getNextKeys("itf_cosa_collect_iface_s", collectList.size());
        int ifaceIdIndex = 0;
        for (CosaCollectItfDTO data : collectList) {
            ItfCosaCollectIface itf = new ItfCosaCollectIface();
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
        log.info("ItfCosaCollectIfaceServiceImpl.insterIface.start");
        myBatchInsert(list);
        log.info("ItfCosaCollectIfaceServiceImpl.insterIface.end");

        // 验证接口表
        log.info("ItfCosaCollectIfaceServiceImpl.check.start");
        boolean validFlag = this.validate(tenantId, list);
        log.info("ItfCosaCollectIfaceServiceImpl.check.end");

        if (validFlag) {
            try {
                self().loadNew(tenantId, list);
            }catch(Exception ex) {
                for (ItfCosaCollectIface data : list) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(ex.getMessage());
                    this.updateByPrimaryKeySelective(data);
                }
            }
        }
        return InterfaceUtils.getReturnList(list);
    }


    public void myBatchInsert(List<ItfCosaCollectIface> insertList) {
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(insertList)) {
            List<List<ItfCosaCollectIface>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfCosaCollectIface> domains : splitSqlList) {
                itfCosaCollectIfaceMapper.insertIface(domains);
            }
        }
    }

    private boolean validate(Long tenantId, List<ItfCosaCollectIface> itfList) {
        boolean validFlag = true;
        // 反射得到字段map
        Map<String, Field> fieldMap = Arrays.stream(ItfCosaCollectIface.class.getDeclaredFields()).collect(Collectors.toMap(Field::getName, rec -> rec, (key1, keys2) -> key1));
        List<ItfCosaCollectIface> updateList=new ArrayList<>();
        for (ItfCosaCollectIface itf : itfList) {
            String processMessage = "";
            // 验证字段
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getTargetLoad()), processMessage, "ITF_DATA_COLLECT_0007", fieldMap.get(ItfCosaCollectIface.FIELD_TARGET_LOAD).getAnnotation(ApiModelProperty.class).value());
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getTargetMaterialLotCode()), processMessage, "ITF_DATA_COLLECT_0007", fieldMap.get(ItfCosaCollectIface.FIELD_TARGET_MATERIAL_LOT_CODE).getAnnotation(ApiModelProperty.class).value());
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getSourceLoad()), processMessage, "ITF_DATA_COLLECT_0007", fieldMap.get(ItfCosaCollectIface.FIELD_SOURCE_LOAD).getAnnotation(ApiModelProperty.class).value());
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getSourceMaterialLotCode()), processMessage, "ITF_DATA_COLLECT_0007", fieldMap.get(ItfCosaCollectIface.FIELD_SOURCE_MATERIAL_LOT_CODE).getAnnotation(ApiModelProperty.class).value());
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getTargetCosPos()), processMessage, "ITF_DATA_COLLECT_0007", fieldMap.get(ItfCosaCollectIface.FIELD_TARGET_COS_POS).getAnnotation(ApiModelProperty.class).value());


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
    /**
     *@description 批量更新
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/7 16:42
     *@param updateList
     *@return void
     **/
    public void myBatchUpdate(List<ItfCosaCollectIface> updateList) {
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(updateList)) {
            List<List<ItfCosaCollectIface>> splitSqlList = InterfaceUtils.splitSqlList(updateList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfCosaCollectIface> domains : splitSqlList) {
                itfCosaCollectIfaceMapper.updateIface(domains);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public void loadNew(Long tenantId, List<ItfCosaCollectIface> dtos) {
        long startDate = System.currentTimeMillis();
        List<String> sourceMaterialLotCodes = dtos.stream().map(ItfCosaCollectIface::getSourceMaterialLotCode).distinct().collect(Collectors.toList());
        List<MtMaterialLot> sourceMaterialLots = mtMaterialLotRepository.materialLotByCodeBatchGet(tenantId, sourceMaterialLotCodes);
        if (CollectionUtils.isEmpty(sourceMaterialLots)) {
            throw new MtException("所有来源条码不存在请重新检查");
        }
        List<String> otherMaterialLotCodes = sourceMaterialLotCodes.stream().filter(t ->
                !sourceMaterialLots.stream().map(MtMaterialLot::getMaterialLotCode).collect(Collectors.toList()).contains(t)
        ).collect(Collectors.toList());
        if (sourceMaterialLots.size() != sourceMaterialLotCodes.size()) {
            throw new MtException("来源条码" + otherMaterialLotCodes.toString() + "不存在");
        }
        long endDate = System.currentTimeMillis();
        log.info("<====查找{}个来源条码信息，总耗时：{}毫秒", sourceMaterialLotCodes.size(),  (endDate - startDate));

        //获取新条码信息
        long startDate2 = System.currentTimeMillis();
        MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
        materialLotVo3.setMaterialLotCode(dtos.get(0).getTargetMaterialLotCode());
        List<String> newMaterialLotIds =
                mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(newMaterialLotIds)) {
            throw new MtException("HME_COS_022", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_022", "HME", dtos.get(0).getTargetMaterialLotCode()));
        }
        MtMaterialLot newMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, newMaterialLotIds.get(0));
        //获取来源物料批
        //验证失败，容器装载对象数量超出容器容量限制
        List<LovValueDTO> typeLov = lovAdapter.queryLovValue("HME.CONTAINER_CAPACITY", tenantId);
        if ((newMaterialLot.getPrimaryUomQty() + (double) dtos.size()) > Integer.parseInt(typeLov.get(0).getValue())) {
            throw new MtException("MT_MATERIAL_LOT_0043", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0043", "MATERIAL_LOT", typeLov.get(0).getValue()));
        }
        long endDate2 = System.currentTimeMillis();
        log.info("<====查找新条码信息，总耗时：{}毫秒", (endDate2 - startDate2));

        //2021-11-16 By 田欣 for 王利娟 增加目标盒子和来源盒子的单位一致的校验
        sourceMaterialLots.forEach(e->{
            if(!e.getPrimaryUomId().equals(newMaterialLot.getPrimaryUomId())){
                throw new MtException("HME_COS_065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_065", "HME",e.getIdentification()));
            }
        });

        //更新虚拟号表
        long startDate3 = System.currentTimeMillis();
        String virtualNum = dtos.get(0).getVirtualNum();
        //获取虚拟号信息
        List<HmeVirtualNum> hmeVirtualNums = hmeVirtualNumRepository.selectByCondition(Condition.builder(HmeVirtualNum.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmeVirtualNum.FIELD_VIRTUAL_NUM, virtualNum))
                .build());
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(hmeVirtualNums)) {
            if (org.apache.commons.lang3.StringUtils.isEmpty(hmeVirtualNums.get(0).getMaterialLotId())) {
                HmeVirtualNum hmeVirtualNum = hmeVirtualNums.get(0);
                hmeVirtualNum.setMaterialLotId(newMaterialLotIds.get(0));
                if (org.apache.commons.lang3.StringUtils.isNotBlank(newMaterialLot.getLocatorId())) {
                    hmeVirtualNum.setLocatorId(newMaterialLot.getLocatorId());
                    MtModLocator mtModLocator = hmePreSelectionMapper.getWarehouse(tenantId, newMaterialLot.getLocatorId());
                    if (!ObjectUtils.isEmpty(mtModLocator)) {
                        hmeVirtualNum.setWarehouseId(mtModLocator.getLocatorId());
                    }
                }
                if (hmeVirtualNums.get(0).getQuantity() != dtos.size()) {
                    throw new MtException("HME_SELECT_025", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_SELECT_025", "HME", hmeVirtualNums.get(0).getVirtualNum()));
                }
                hmeVirtualNumMapper.updateByPrimaryKeySelective(hmeVirtualNum);
            } else {
                if (!hmeVirtualNums.get(0).getMaterialLotId().equals(newMaterialLotIds.get(0))) {
                    throw new MtException("HME_SELECT_001", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_SELECT_001", "HME", hmeVirtualNums.get(0).getVirtualNum()));
                }

            }
        }
        long endDate3 = System.currentTimeMillis();
        log.info("<====更新虚拟号信息，总耗时：{}毫秒", (endDate3 - startDate3));
        HmePreSelection hmePreSelection = new HmePreSelection();

        long startDate9 = System.currentTimeMillis();
        log.info("<==========================================开始循环遍历来源条码");
        //请求事件
        String receiptRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PRODUCT_RECEIPT_CREATE");
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("COS_CHIP_SELECT_IN");
        // 创建事件
        eventCreateVO.setEventRequestId(receiptRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        MtEventCreateVO eventCreateVO1 = new MtEventCreateVO();
        eventCreateVO1.setEventTypeCode("COS_CHIP_SELECT_OUT");
        eventCreateVO1.setEventRequestId(receiptRequestId);
        String eventId1 = mtEventRepository.eventCreate(tenantId, eventCreateVO1);

        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        List<MtMaterialLotVO20> sourceMaterialLotVO20List = new ArrayList<>();
        List<MtCommonExtendVO7> mtCommonExtendVO7List = new ArrayList<>();
        int i = 0;
        int cosNumSum = 0;
        for (MtMaterialLot sourceMaterialLot :
                sourceMaterialLots) {
            long startDate4 = System.currentTimeMillis();
            log.info("<====当前在遍历第{}个来源条码", i);
            // 校验有效性
            if (!YES.equals(sourceMaterialLot.getEnableFlag())) {
                throw new MtException("HME_COS_022", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "HME_COS_022", HmeConstants.ConstantValue.HME, sourceMaterialLot.getMaterialLotCode()));
            }
            //校验在制品
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
            mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtExtendAttrVOS) && "Y".equals(mtExtendAttrVOS.get(0).getAttrValue())) {
                throw new MtException("HME_COS_052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_052", "HME",sourceMaterialLot.getMaterialLotCode()));
            }

            MtMaterialLot newMaterialLotTemp = mtMaterialLotRepository.materialLotPropertyGet(tenantId, newMaterialLotIds.get(0));
            List<ItfCosaCollectIface> collect = dtos.stream().filter(t -> t.getSourceMaterialLotCode().equals(sourceMaterialLot.getMaterialLotCode())).collect(Collectors.toList());
            log.info("<====当前在遍历第{}个来源条码,共找到{}条接口数据", i, collect.size());
            cosNumSum += collect.size();

            List<String> targetSelectionLotList = new ArrayList<>();
            if(i == 0){
                //根据目标条码查询挑选批次
                long startDate10 = System.currentTimeMillis();
                targetSelectionLotList = itfCosaCollectIfaceMapper.targerSelectionLotQuery(tenantId, newMaterialLotTemp.getMaterialLotId());
                long endDate10 = System.currentTimeMillis();
                log.info("<====当前在遍历第{}个来源条码,根据目标条码查询挑选批次,耗时：{}毫秒", i, (endDate10 - startDate10));
            }

            long startDate5 = System.currentTimeMillis();
            int j = 0;
            List<String> sourceSelectionLotList = new ArrayList<>();
            for (ItfCosaCollectIface dto : collect) {
                long startDate10 = System.currentTimeMillis();
                HmeSelectionDetails hmeSelectionDetails = new HmeSelectionDetails();
                hmeSelectionDetails.setOldMaterialLotId(sourceMaterialLot.getMaterialLotId());
                char[] split = dto.getSourceLoad().toCharArray();
                hmeSelectionDetails.setOldLoad((((int) split[0]) - 64) + "," + dto.getSourceLoad().substring(1, 2));
                List<HmeSelectionDetails> hmeSelectionDetails1s = hmeSelectionDetailsMapper.select(hmeSelectionDetails);
                long endDate10 = System.currentTimeMillis();
                log.info("<====当前在遍历第{}个来源条码,根据旧盒号{},旧位置{}查询HmeSelectionDetails数据, 耗时：{}毫秒", i,
                        hmeSelectionDetails.getOldMaterialLotId(), hmeSelectionDetails.getOldLoad(), (endDate10 - startDate10));

                if(CollectionUtils.isEmpty(hmeSelectionDetails1s))
                {throw new MtException("HME_COS_053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_053", "HME",sourceMaterialLot.getMaterialLotCode(),dto.getSourceLoad()));}

                HmeSelectionDetails hmeSelectionDetails1 = hmeSelectionDetails1s.get(0);
                //2021-04-02 17:32 add by chaonan.hu for zhenyong.ban 如果虚拟号已装载，则报错
                if("LOADED".equals(hmeSelectionDetails1.getAttribute1())){
                    throw new MtException("HME_COS_055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_055", "HME"));
                }
                HmePreSelection hmePreSelection1 = hmePreSelectionRepository.selectByPrimaryKey(hmeSelectionDetails1.getPreSelectionId());
                if(Objects.nonNull(hmePreSelection1) && StringUtils.isNotBlank(hmePreSelection1.getAttribute1())){
                    sourceSelectionLotList.add(hmePreSelection1.getAttribute1());
                }
                hmeSelectionDetails1.setNewMaterialLotId(newMaterialLotIds.get(0));
                char[] split1 = dto.getTargetLoad().toCharArray();
                hmeSelectionDetails1.setNewLoad((((int) split1[0]) - 64) +","+ dto.getTargetLoad().substring(1, 2));
                hmeSelectionDetails1.setAttribute1("LOADED");
                hmeSelectionDetails1.setAttribute2(dto.getTargetCosPos());
                hmeSelectionDetailsMapper.updateByPrimaryKeySelective(hmeSelectionDetails1);
                long endDate11 = System.currentTimeMillis();
                log.info("<====当前在遍历第{}个来源条码,根据旧盒号{},旧位置{}更新HmeSelectionDetails数据, 耗时：{}毫秒", i,
                        hmeSelectionDetails.getOldMaterialLotId(), hmeSelectionDetails.getOldLoad(), (endDate11 - endDate10));

                hmePreSelection = hmePreSelectionMapper.selectbyDetails(hmeSelectionDetails1.getSelectionDetailsId());
                long endDate12 = System.currentTimeMillis();
                log.info("<====当前在遍历第{}个来源条码,根据SelectionDetailsId{}查询hmePreSelection数据, 耗时：{}毫秒", i,
                        hmeSelectionDetails1.getSelectionDetailsId(), (endDate12 - endDate11));

                //更新装载表
                HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                hmeMaterialLotLoad.setLoadSequence(hmeSelectionDetails1.getLoadSequence());
                HmeMaterialLotLoad select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad).get(0);
                long endDate13 = System.currentTimeMillis();
                log.info("<====当前在遍历第{}个来源条码,根据LoadSequence{}查询hmeMaterialLotLoad数据, 耗时：{}毫秒", i,
                        hmeSelectionDetails1.getLoadSequence(), (endDate13 - endDate12));
                select.setSourceLoadColumn(select.getLoadColumn());
                select.setSourceLoadRow(select.getLoadRow());
                select.setSourceMaterialLotId(select.getMaterialLotId());
                select.setMaterialLotId(newMaterialLotIds.get(0));
                select.setLoadRow(((long) split1[0]) - 64);
                select.setLoadColumn(Long.valueOf(split1[1]+""));
                //2021-05-08 17:27 add by chaonan.hu for zhenyong.ban 增加material_lot_id+load_row+load_column唯一性校验
                int count = hmeMaterialLotLoadRepository.selectCount(new HmeMaterialLotLoad() {{
                    setTenantId(tenantId);
                    setMaterialLotId(select.getMaterialLotId());
                    setLoadRow(select.getLoadRow());
                    setLoadColumn(select.getLoadColumn());
                }});
                if(count > 0){
                    throw new MtException("HME_SELECT_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_SELECT_007", "HME"));
                }
                hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(select);
                long endDate14 = System.currentTimeMillis();
                log.info("<====当前在遍历第{}个来源条码,根据LoadSequence{}更新hmeMaterialLotLoad数据, 耗时：{}毫秒", i,
                        hmeSelectionDetails1.getLoadSequence(), (endDate14 - endDate13));
                j++;
            }
            long endDate5 = System.currentTimeMillis();
            log.info("<====当前在遍历第{}个来源条码,更新装载表数据总耗时：{}毫秒", i, (endDate5 - startDate5));

            if(i == 0){
                //只需要第一次循环时候进行此校验即可
                //只有当目标条码挑选批次不为空，来源条码挑选批次为空或者不为空，但两者并不完全相同，则报错
                if(CollectionUtil.isNotEmpty(targetSelectionLotList)){
                    targetSelectionLotList = targetSelectionLotList.stream().distinct().collect(Collectors.toList());
                    if(CollectionUtil.isEmpty(sourceSelectionLotList)){
                        throw new MtException("HME_COS_061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_061", "HME"));
                    }
                    sourceSelectionLotList = sourceSelectionLotList.stream().distinct().collect(Collectors.toList());
                    if(targetSelectionLotList.size() != sourceSelectionLotList.size()){
                        throw new MtException("HME_COS_061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_061", "HME"));
                    }
                    targetSelectionLotList.removeAll(sourceSelectionLotList);
                    if(CollectionUtil.isNotEmpty(targetSelectionLotList)){
                        throw new MtException("HME_COS_061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_061", "HME"));
                    }
                }
            }

            long startDate6 = System.currentTimeMillis();

            if (YES.equals(newMaterialLotTemp.getEnableFlag())) {
                if (!sourceMaterialLot.getMaterialId().equals(newMaterialLotTemp.getMaterialId())) {
                    throw new MtException("HME_CHIP_TRANSFER_023", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_CHIP_TRANSFER_023", "HME"));
                }
                if (!sourceMaterialLot.getLocatorId().equals(newMaterialLotTemp.getLocatorId())) {
                    throw new MtException("HME_COS_062", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_COS_062", "HME"));
                }
                if (!sourceMaterialLot.getLot().equals(newMaterialLotTemp.getLot())) {
                    throw new MtException("HME_COS_004", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_COS_004", "HME"));
                }

                MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
                mtCommonExtendVO7.setKeyId(newMaterialLotIds.get(0));
                List<MtCommonExtendVO4> mtCommonExtendVO4List = new ArrayList<>();
                MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
                mtCommonExtendVO4.setAttrName("ORIGINAL_ID");
                mtCommonExtendVO4.setAttrValue(sourceMaterialLot.getMaterialLotId());
                mtCommonExtendVO4List.add(mtCommonExtendVO4);
                mtCommonExtendVO7.setAttrs(mtCommonExtendVO4List);
                mtCommonExtendVO7List.add(mtCommonExtendVO7);

//                List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
//                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//                mtExtendVO5.setAttrName("ORIGINAL_ID");
//                mtExtendVO5.setAttrValue(sourceMaterialLot.getMaterialLotId());
//                mtExtendVO5List.add(mtExtendVO5);
//
//                MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
//                mtExtendVO10.setKeyId(newMaterialLotIds.get(0));
//                mtExtendVO10.setEventId(eventId);
//                mtExtendVO10.setAttrs(mtExtendVO5List);
//                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);

                //2021-04-22 10:32 edit by chaonan.hu for kang.wang 更新物料批及扩展属性改为批量
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(newMaterialLotTemp.getMaterialLotId());
                mtMaterialLotVO20.setPrimaryUomQty(newMaterialLotTemp.getPrimaryUomQty() + (double) collect.size());
                mtMaterialLotVO20List.add(mtMaterialLotVO20);

//                MtMaterialLotVO2 mtLotUpdate = new MtMaterialLotVO2();
//                mtLotUpdate.setMaterialLotId(newMaterialLotTemp.getMaterialLotId());
//                mtLotUpdate.setPrimaryUomQty(newMaterialLotTemp.getPrimaryUomQty() + (double) collect.size());
//                mtLotUpdate.setEventId(eventId);
//                mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate, HmeConstants.ConstantValue.NO);
            } else {
                //获取编码 更新lot
                List<MtExtendSettings> mtExtendSettingss = new ArrayList<>();
                MtExtendSettings mtExtendSettings = new MtExtendSettings();
                mtExtendSettings.setAttrName("CONTAINER_TYPE");
                mtExtendSettingss.add(mtExtendSettings);
                MtExtendSettings mtExtendSettings1 = new MtExtendSettings();
                mtExtendSettings1.setAttrName("LOCATION_ROW");
                mtExtendSettingss.add(mtExtendSettings1);
                MtExtendSettings mtExtendSettings2 = new MtExtendSettings();
                mtExtendSettings2.setAttrName("LOCATION_COLUMN");
                mtExtendSettingss.add(mtExtendSettings2);
                MtExtendSettings mtExtendSettings3 = new MtExtendSettings();
                mtExtendSettings3.setAttrName("CHIP_NUM");
                mtExtendSettingss.add(mtExtendSettings3);
                MtExtendSettings mtExtendSettings4 = new MtExtendSettings();
                mtExtendSettings4.setAttrName("MF_FLAG");
                mtExtendSettingss.add(mtExtendSettings4);
                MtExtendSettings mtExtendSettings5 = new MtExtendSettings();
                mtExtendSettings5.setAttrName("STATUS");
                mtExtendSettingss.add(mtExtendSettings5);
                MtExtendSettings mtExtendSettings6 = new MtExtendSettings();
                mtExtendSettings6.setAttrName("COS_TYPE");
                mtExtendSettingss.add(mtExtendSettings6);
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                        "mt_material_lot_attr", "MATERIAL_LOT_ID", sourceMaterialLots.get(0).getMaterialLotId(), mtExtendSettingss);

                MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
                mtCommonExtendVO7.setKeyId(newMaterialLotIds.get(0));
                List<MtCommonExtendVO4> mtCommonExtendVO4List = new ArrayList<>();
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                    mtExtendAttrVOList.forEach(item -> {
                        List<String> name = mtExtendSettingss.stream().map(MtExtendSettings::getAttrName).collect(Collectors.toList());
                        if (name.contains(item.getAttrName())) {
                            MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
                            mtCommonExtendVO4.setAttrName(item.getAttrName());
                            mtCommonExtendVO4.setAttrValue(item.getAttrValue());
                            mtCommonExtendVO4List.add(mtCommonExtendVO4);
                        }
                    });
                }
                MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
                mtCommonExtendVO4.setAttrName("ORIGINAL_ID");
                mtCommonExtendVO4.setAttrValue(sourceMaterialLot.getMaterialLotId());
                mtCommonExtendVO7.setAttrs(mtCommonExtendVO4List);
                mtCommonExtendVO7List.add(mtCommonExtendVO7);

//                List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
//                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
//                    mtExtendAttrVOList.forEach(item -> {
//                        List<String> name = mtExtendSettingss.stream().map(MtExtendSettings::getAttrName).collect(Collectors.toList());
//                        if (name.contains(item.getAttrName())) {
//                            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//                            mtExtendVO5.setAttrName(item.getAttrName());
//                            mtExtendVO5.setAttrValue(item.getAttrValue());
//                            mtExtendVO5List.add(mtExtendVO5);
//                        }
//                    });
//                }
//                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//                mtExtendVO5.setAttrName("ORIGINAL_ID");
//                mtExtendVO5.setAttrValue(sourceMaterialLot.getMaterialLotId());
//                mtExtendVO5List.add(mtExtendVO5);
//
//                MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
//                mtExtendVO10.setKeyId(newMaterialLotIds.get(0));
//                mtExtendVO10.setEventId(eventId);
//                mtExtendVO10.setAttrs(mtExtendVO5List);
//                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);

                String lot = profileClient.getProfileValueByOptions("HME_COS_MATERIAL_LOT_LOT");
                if (org.apache.commons.lang3.StringUtils.isEmpty(lot)) {
                    throw new MtException("HME_COS_006", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_COS_006", "HME"));
                }

                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(newMaterialLotTemp.getMaterialLotId());
                mtMaterialLotVO20.setPrimaryUomQty((double) collect.size());
                mtMaterialLotVO20.setMaterialId(sourceMaterialLot.getMaterialId());
                mtMaterialLotVO20.setEnableFlag("Y");
                mtMaterialLotVO20.setLot(lot);
                mtMaterialLotVO20.setQualityStatus("OK");
                mtMaterialLotVO20.setLocatorId(sourceMaterialLot.getLocatorId());
                mtMaterialLotVO20.setSupplierId(sourceMaterialLot.getSupplierId());
                mtMaterialLotVO20.setSupplierSiteId(sourceMaterialLot.getSupplierSiteId());
                mtMaterialLotVO20List.add(mtMaterialLotVO20);

//                MtMaterialLotVO2 mtLotUpdate = new MtMaterialLotVO2();
//                mtLotUpdate.setMaterialLotId(newMaterialLotTemp.getMaterialLotId());
//                mtLotUpdate.setPrimaryUomQty((double) collect.size());
//                mtLotUpdate.setMaterialId(sourceMaterialLot.getMaterialId());
//                mtLotUpdate.setEnableFlag("Y");
//                mtLotUpdate.setLot(lot);
//                mtLotUpdate.setQualityStatus("OK");
//                mtLotUpdate.setLocatorId(sourceMaterialLot.getLocatorId());
//                mtLotUpdate.setSupplierId(sourceMaterialLot.getSupplierId());
//                mtLotUpdate.setSupplierSiteId(sourceMaterialLot.getSupplierSiteId());
//                mtLotUpdate.setEventId(eventId);
//                mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate, HmeConstants.ConstantValue.NO);
            }
            long endDate6 = System.currentTimeMillis();
            log.info("<====当前在遍历第{}个来源条码,更新目标条码及扩展属性总耗时：{}毫秒", i, (endDate6 - startDate6));

            //修改来源物料批表
            long startDate7 = System.currentTimeMillis();

            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
            mtMaterialLotVO20.setTrxPrimaryUomQty((double) (collect.size() * -1));
            mtMaterialLotVO20.setPrimaryUomQty(sourceMaterialLot.getPrimaryUomQty() - (double) collect.size());
            if (BigDecimal.valueOf(mtMaterialLotVO20.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) == 0) {
                mtMaterialLotVO20.setEnableFlag("N");
            }
            mtMaterialLotVO20.setMaterialId(sourceMaterialLot.getMaterialId());
            sourceMaterialLotVO20List.add(mtMaterialLotVO20);

//            MtMaterialLotVO2 mtLotUpdate1 = new MtMaterialLotVO2();
//            mtLotUpdate1.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
//            mtLotUpdate1.setPrimaryUomQty(sourceMaterialLot.getPrimaryUomQty() - (double) collect.size());
//            if (sourceMaterialLot.getPrimaryUomQty().compareTo(0.0) == 0) {
//                mtLotUpdate1.setEnableFlag("N");
//            }
//            mtLotUpdate1.setMaterialId(sourceMaterialLot.getMaterialId());
//            mtLotUpdate1.setEventId(eventId1);
//            mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate1, HmeConstants.ConstantValue.NO);
            long endDate7 = System.currentTimeMillis();
            log.info("<====当前在遍历第{}个来源条码,更新来源条码总耗时：{}毫秒", i, (endDate7 - startDate7));
            long endDate4 = System.currentTimeMillis();
            log.info("<====当前在遍历第{}个来源条码,总耗时：{}毫秒", i, (endDate4 - startDate4));
            i++;
        }

        long startDate10 = System.currentTimeMillis();
        //批量更新新物料批 业务场景下此时只会找到同一个新物料批
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
            String materialLotId = mtMaterialLotVO20List.get(0).getMaterialLotId();
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
            if(YES.equals(mtMaterialLot.getEnableFlag())){
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                mtMaterialLotVO20.setPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() + (double) cosNumSum);
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, Collections.singletonList(mtMaterialLotVO20), eventId, HmeConstants.ConstantValue.NO);
            }else{
                MtMaterialLotVO20 mtMaterialLotVO20 = mtMaterialLotVO20List.get(mtMaterialLotVO20List.size() - 1);
                mtMaterialLotVO20.setPrimaryUomQty((double) cosNumSum);
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, Collections.singletonList(mtMaterialLotVO20), eventId, HmeConstants.ConstantValue.NO);
            }
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(sourceMaterialLotVO20List)){
            //对重复的数据进行合并
            List<MtMaterialLotVO20> sourceMaterialLotVO20s = new ArrayList<>();
            Map<String, List<MtMaterialLotVO20>> sourceMaterialLotMap = sourceMaterialLotVO20List.stream().collect(Collectors.groupingBy(MtMaterialLotVO20::getMaterialLotId));
            for(Map.Entry<String, List<MtMaterialLotVO20>> entry:sourceMaterialLotMap.entrySet()){
                List<MtMaterialLotVO20> value = entry.getValue();
                if(value.size() > 1){
                    BigDecimal qty = value.stream().collect(CollectorsUtil
                            .summingBigDecimal(item -> BigDecimal.valueOf(item.getTrxPrimaryUomQty())));
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    mtMaterialLotVO20.setMaterialLotId(entry.getKey());
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(entry.getKey());
                    //这里的qty是负数，实际是减数量
                    mtMaterialLotVO20.setPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() + qty.doubleValue());
                    mtMaterialLotVO20.setMaterialId(value.get(value.size() - 1).getMaterialId());
                    if (new BigDecimal(mtMaterialLotVO20.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) == 0) {
                        mtMaterialLotVO20.setEnableFlag("N");
                    }
                    sourceMaterialLotVO20s.add(mtMaterialLotVO20);
                }else{
                    value.get(0).setTrxPrimaryUomQty(null);
                    sourceMaterialLotVO20s.addAll(value);
                }
            }
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, sourceMaterialLotVO20s, eventId1, HmeConstants.ConstantValue.NO);
        }
        //批量更新物料批扩展属性
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtCommonExtendVO7List)){
            mtCommonExtendVO7List = Collections.singletonList(mtCommonExtendVO7List.get(mtCommonExtendVO7List.size() - 1));
            mtExtendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", eventId, mtCommonExtendVO7List);
        }
        long endDate10 = System.currentTimeMillis();
        log.info("<====批量更新物料批和扩展属性耗时：{}毫秒", (endDate10 - startDate10));

        long endDate9 = System.currentTimeMillis();
        log.info("<====结束循环遍历来源条码，总耗时：{}毫秒", (endDate9 - startDate9));

        long startDate8 = System.currentTimeMillis();
        List<HmeSelectionDetails> hmeSelectionDetails1 = hmeSelectionDetailsMapper.selectByCondition(Condition.builder(HmeSelectionDetails.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmeSelectionDetails.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeSelectionDetails.FIELD_PRE_SELECTION_ID, hmePreSelection.getPreSelectionId()))
                .build());
        long loaded = hmeSelectionDetails1.stream().filter(t -> t.getAttribute1().equals("LOADED")).count();
        if (loaded == hmeSelectionDetails1.size()) {
            hmePreSelection.setStatus("LOADED");
        } else {
            hmePreSelection.setStatus("LOADING");
        }
        hmePreSelectionMapper.updateByPrimaryKeySelective(hmePreSelection);
        long endDate8 = System.currentTimeMillis();
        log.info("<====更新PreSelection总耗时：{}毫秒",(endDate8 - startDate8));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DataCollectReturnDTO> invokeb(Long tenantId, List<CosaCollectItfDTO> collectList) {
        log.info("ItfCosaCollectIfaceServiceImpl.invokeb.start");
        if (CollectionUtils.isEmpty(collectList)) {
            return new ArrayList<>();
        }
        // 插入接口表
        List<ItfCosaCollectIface> list = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(CosaCollectItfDTO.class, ItfCosaCollectIface.class, false);
        Date nowDate = new Date();
        List<String> ifaceIdList = customSequence.getNextKeys("itf_cosa_collect_iface_s", collectList.size());
        int ifaceIdIndex = 0;
        for (CosaCollectItfDTO data : collectList) {
            ItfCosaCollectIface itf = new ItfCosaCollectIface();
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
        log.info("ItfCosaCollectIfaceServiceImpl.insterIface.start");
        myBatchInsert(list);
        log.info("ItfCosaCollectIfaceServiceImpl.insterIface.end");

        // 验证接口表
        log.info("ItfCosaCollectIfaceServiceImpl.check.start");
        boolean validFlag = this.validate2(tenantId, list);
        log.info("ItfCosaCollectIfaceServiceImpl.check.end");

        if (validFlag) {
            try {
                this.loadNew2(tenantId, list);
            }catch(Exception ex) {
                for (ItfCosaCollectIface data : list) {
                    data.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    data.setProcessMessage(ex.getMessage());
                    this.updateByPrimaryKeySelective(data);
                }
            }
        }
        return InterfaceUtils.getReturnList(list);
    }

    private boolean validate2(Long tenantId, List<ItfCosaCollectIface> itfList) {
        boolean validFlag = true;
        // 反射得到字段map
        Map<String, Field> fieldMap = Arrays.stream(ItfCosaCollectIface.class.getDeclaredFields()).collect(Collectors.toMap(Field::getName, rec -> rec, (key1, keys2) -> key1));
        List<ItfCosaCollectIface> updateList=new ArrayList<>();
        for (ItfCosaCollectIface itf : itfList) {
            String processMessage = "";
            // 验证字段
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getTargetLoad()), processMessage, "ITF_DATA_COLLECT_0007", fieldMap.get(ItfCosaCollectIface.FIELD_TARGET_LOAD).getAnnotation(ApiModelProperty.class).value());
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getTargetMaterialLotCode()), processMessage, "ITF_DATA_COLLECT_0007", fieldMap.get(ItfCosaCollectIface.FIELD_TARGET_MATERIAL_LOT_CODE).getAnnotation(ApiModelProperty.class).value());
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getSourceLoad()), processMessage, "ITF_DATA_COLLECT_0007", fieldMap.get(ItfCosaCollectIface.FIELD_SOURCE_LOAD).getAnnotation(ApiModelProperty.class).value());
            processMessage = InterfaceUtils.processErrorMessage(tenantId, Objects.isNull(itf.getSourceMaterialLotCode()), processMessage, "ITF_DATA_COLLECT_0007", fieldMap.get(ItfCosaCollectIface.FIELD_SOURCE_MATERIAL_LOT_CODE).getAnnotation(ApiModelProperty.class).value());

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

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public void loadNew2(Long tenantId, List<ItfCosaCollectIface> dtos) {
        long startDate = System.currentTimeMillis();
        List<String> sourceMaterialLotCodes = dtos.stream().map(ItfCosaCollectIface::getSourceMaterialLotCode).distinct().collect(Collectors.toList());
        List<MtMaterialLot> sourceMaterialLots = mtMaterialLotRepository.materialLotByCodeBatchGet(tenantId, sourceMaterialLotCodes);
        if (CollectionUtils.isEmpty(sourceMaterialLots)) {
            throw new MtException("所有来源条码不存在请重新检查");
        }
        List<String> otherMaterialLotCodes = sourceMaterialLotCodes.stream().filter(t ->
                !sourceMaterialLots.stream().map(MtMaterialLot::getMaterialLotCode).collect(Collectors.toList()).contains(t)
        ).collect(Collectors.toList());
        if (sourceMaterialLots.size() != sourceMaterialLotCodes.size()) {
            throw new MtException("来源条码" + otherMaterialLotCodes.toString() + "不存在");
        }
        long endDate = System.currentTimeMillis();
        log.info("<====查找{}个来源条码信息，总耗时：{}毫秒", sourceMaterialLotCodes.size(),  (endDate - startDate));

        //获取新条码信息
        long startDate2 = System.currentTimeMillis();
        MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
        materialLotVo3.setMaterialLotCode(dtos.get(0).getTargetMaterialLotCode());
        List<String> newMaterialLotIds =
                mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(newMaterialLotIds)) {
            throw new MtException("HME_COS_022", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_022", "HME", dtos.get(0).getTargetMaterialLotCode()));
        }
        MtMaterialLot newMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, newMaterialLotIds.get(0));
        //获取来源物料批
        //验证失败，容器装载对象数量超出容器容量限制
        List<LovValueDTO> typeLov = lovAdapter.queryLovValue("HME.CONTAINER_CAPACITY", tenantId);
        if ((newMaterialLot.getPrimaryUomQty() + (double) dtos.size()) > Integer.parseInt(typeLov.get(0).getValue())) {
            throw new MtException("MT_MATERIAL_LOT_0043", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0043", "MATERIAL_LOT", typeLov.get(0).getValue()));
        }
        long endDate2 = System.currentTimeMillis();
        log.info("<====查找新条码信息，总耗时：{}毫秒", (endDate2 - startDate2));

        //2021-11-16 By 田欣 for 王利娟 增加目标盒子和来源盒子的单位一致的校验
        //2021-11-17 By 田欣 for 周仁杰 增加来源盒子和目标盒子物料一致的校验 原校验只针对来源和目标盒子都有效的情形
        sourceMaterialLots.forEach(e->{
            if(!e.getPrimaryUomId().equals(newMaterialLot.getPrimaryUomId())){
                throw new MtException("HME_COS_065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_065", "HME",e.getIdentification()));
            }
            if (!e.getMaterialId().equals(newMaterialLot.getMaterialId())) {
                throw new MtException("HME_CHIP_TRANSFER_023", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_CHIP_TRANSFER_023", "HME"));
            }
        });

        long startDate9 = System.currentTimeMillis();
        log.info("<==========================================开始循环遍历来源条码");
        //请求事件
        String receiptRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PRODUCT_RECEIPT_CREATE");
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("COS_CHIP_SELECT_IN");
        // 创建事件
        eventCreateVO.setEventRequestId(receiptRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        MtEventCreateVO eventCreateVO1 = new MtEventCreateVO();
        eventCreateVO1.setEventTypeCode("COS_CHIP_SELECT_OUT");
        eventCreateVO1.setEventRequestId(receiptRequestId);
        String eventId1 = mtEventRepository.eventCreate(tenantId, eventCreateVO1);

        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        List<MtMaterialLotVO20> sourceMaterialLotVO20List = new ArrayList<>();
        List<MtCommonExtendVO7> mtCommonExtendVO7List = new ArrayList<>();
        int i = 0;
        int cosNumSum = 0;
        for (MtMaterialLot sourceMaterialLot :
                sourceMaterialLots) {
            long startDate4 = System.currentTimeMillis();
            log.info("<====当前在遍历第{}个来源条码", i);
            // 校验有效性
            if (!YES.equals(sourceMaterialLot.getEnableFlag())) {
                throw new MtException("HME_COS_022", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "HME_COS_022", HmeConstants.ConstantValue.HME, sourceMaterialLot.getMaterialLotCode()));
            }
            //校验在制品
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
            mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtExtendAttrVOS) && "Y".equals(mtExtendAttrVOS.get(0).getAttrValue())) {
                throw new MtException("HME_COS_052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_052", "HME",sourceMaterialLot.getMaterialLotCode()));
            }

            MtMaterialLot newMaterialLotTemp = mtMaterialLotRepository.materialLotPropertyGet(tenantId, newMaterialLotIds.get(0));
            List<ItfCosaCollectIface> collect = dtos.stream().filter(t -> t.getSourceMaterialLotCode().equals(sourceMaterialLot.getMaterialLotCode())).collect(Collectors.toList());
            log.info("<====当前在遍历第{}个来源条码,共找到{}条接口数据", i, collect.size());
            cosNumSum += collect.size();

            long startDate5 = System.currentTimeMillis();
            for (ItfCosaCollectIface dto : collect) {

                char[] split1 = dto.getTargetLoad().toCharArray();
                //更新装载表
                long endDate12 = System.currentTimeMillis();
                HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                if(StringUtils.isNotEmpty(dto.getCosaAttribute1())){
                    hmeMaterialLotLoad.setLoadSequence(dto.getCosaAttribute1());
                }else{
                    hmeMaterialLotLoad.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
                    char[] split2 = dto.getSourceLoad().toCharArray();
                    hmeMaterialLotLoad.setLoadRow(((long) split2[0]) - 64);
                    hmeMaterialLotLoad.setLoadColumn(Long.valueOf(split2[1]+""));
                }
                HmeMaterialLotLoad select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad).get(0);
                long endDate13 = System.currentTimeMillis();
                log.info("<====当前在遍历第{}个来源条码,根据LoadSequence{}查询hmeMaterialLotLoad数据, 耗时：{}毫秒", i,
                        select.getLoadSequence(), (endDate13 - endDate12));
                select.setSourceLoadColumn(select.getLoadColumn());
                select.setSourceLoadRow(select.getLoadRow());
                select.setSourceMaterialLotId(select.getMaterialLotId());
                select.setMaterialLotId(newMaterialLotIds.get(0));
                select.setLoadRow(((long) split1[0]) - 64);
                select.setLoadColumn(Long.valueOf(split1[1]+""));
                //2021-05-08 17:27 add by chaonan.hu for zhenyong.ban 增加material_lot_id+load_row+load_column唯一性校验
                int count = hmeMaterialLotLoadRepository.selectCount(new HmeMaterialLotLoad() {{
                    setTenantId(tenantId);
                    setMaterialLotId(select.getMaterialLotId());
                    setLoadRow(select.getLoadRow());
                    setLoadColumn(select.getLoadColumn());
                }});
                if(count > 0){
                    throw new MtException("HME_SELECT_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_SELECT_007", "HME"));
                }
                hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(select);
                long endDate14 = System.currentTimeMillis();
                log.info("<====当前在遍历第{}个来源条码,根据LoadSequence{}更新hmeMaterialLotLoad数据, 耗时：{}毫秒", i,
                        select.getLoadSequence(), (endDate14 - endDate13));
            }
            long endDate5 = System.currentTimeMillis();
            log.info("<====当前在遍历第{}个来源条码,更新装载表数据总耗时：{}毫秒", i, (endDate5 - startDate5));

            long startDate6 = System.currentTimeMillis();

            if (YES.equals(newMaterialLotTemp.getEnableFlag())) {
                if (!sourceMaterialLot.getMaterialId().equals(newMaterialLotTemp.getMaterialId())) {
                    throw new MtException("HME_CHIP_TRANSFER_023", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_CHIP_TRANSFER_023", "HME"));
                }
                if (!sourceMaterialLot.getLocatorId().equals(newMaterialLotTemp.getLocatorId())) {
                    throw new MtException("HME_COS_062", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_COS_062", "HME"));
                }
                if (!sourceMaterialLot.getLot().equals(newMaterialLotTemp.getLot())) {
                    throw new MtException("HME_COS_004", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_COS_004", "HME"));
                }

                MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
                mtCommonExtendVO7.setKeyId(newMaterialLotIds.get(0));
                List<MtCommonExtendVO4> mtCommonExtendVO4List = new ArrayList<>();
                MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
                mtCommonExtendVO4.setAttrName("ORIGINAL_ID");
                mtCommonExtendVO4.setAttrValue(sourceMaterialLot.getMaterialLotId());
                mtCommonExtendVO4List.add(mtCommonExtendVO4);
                mtCommonExtendVO7.setAttrs(mtCommonExtendVO4List);
                mtCommonExtendVO7List.add(mtCommonExtendVO7);

                //2021-04-22 10:32 edit by chaonan.hu for kang.wang 更新物料批及扩展属性改为批量
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(newMaterialLotTemp.getMaterialLotId());
                mtMaterialLotVO20.setPrimaryUomQty(newMaterialLotTemp.getPrimaryUomQty() + (double) collect.size());
                mtMaterialLotVO20List.add(mtMaterialLotVO20);
            } else {
                //获取编码 更新lot
                List<MtExtendSettings> mtExtendSettingss = new ArrayList<>();
                MtExtendSettings mtExtendSettings = new MtExtendSettings();
                mtExtendSettings.setAttrName("CONTAINER_TYPE");
                mtExtendSettingss.add(mtExtendSettings);
                MtExtendSettings mtExtendSettings1 = new MtExtendSettings();
                mtExtendSettings1.setAttrName("LOCATION_ROW");
                mtExtendSettingss.add(mtExtendSettings1);
                MtExtendSettings mtExtendSettings2 = new MtExtendSettings();
                mtExtendSettings2.setAttrName("LOCATION_COLUMN");
                mtExtendSettingss.add(mtExtendSettings2);
                MtExtendSettings mtExtendSettings3 = new MtExtendSettings();
                mtExtendSettings3.setAttrName("CHIP_NUM");
                mtExtendSettingss.add(mtExtendSettings3);
                MtExtendSettings mtExtendSettings4 = new MtExtendSettings();
                mtExtendSettings4.setAttrName("MF_FLAG");
                mtExtendSettingss.add(mtExtendSettings4);
                MtExtendSettings mtExtendSettings5 = new MtExtendSettings();
                mtExtendSettings5.setAttrName("STATUS");
                mtExtendSettingss.add(mtExtendSettings5);
                MtExtendSettings mtExtendSettings6 = new MtExtendSettings();
                mtExtendSettings6.setAttrName("COS_TYPE");
                mtExtendSettingss.add(mtExtendSettings6);
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                        "mt_material_lot_attr", "MATERIAL_LOT_ID", sourceMaterialLots.get(0).getMaterialLotId(), mtExtendSettingss);

                MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
                mtCommonExtendVO7.setKeyId(newMaterialLotIds.get(0));
                List<MtCommonExtendVO4> mtCommonExtendVO4List = new ArrayList<>();
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                    mtExtendAttrVOList.forEach(item -> {
                        List<String> name = mtExtendSettingss.stream().map(MtExtendSettings::getAttrName).collect(Collectors.toList());
                        if (name.contains(item.getAttrName())) {
                            MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
                            mtCommonExtendVO4.setAttrName(item.getAttrName());
                            mtCommonExtendVO4.setAttrValue(item.getAttrValue());
                            mtCommonExtendVO4List.add(mtCommonExtendVO4);
                        }
                    });
                }
                MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
                mtCommonExtendVO4.setAttrName("ORIGINAL_ID");
                mtCommonExtendVO4.setAttrValue(sourceMaterialLot.getMaterialLotId());
                mtCommonExtendVO7.setAttrs(mtCommonExtendVO4List);
                mtCommonExtendVO7List.add(mtCommonExtendVO7);

                String lot = profileClient.getProfileValueByOptions("HME_COS_MATERIAL_LOT_LOT");
                if (org.apache.commons.lang3.StringUtils.isEmpty(lot)) {
                    throw new MtException("HME_COS_006", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_COS_006", "HME"));
                }

                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(newMaterialLotTemp.getMaterialLotId());
                mtMaterialLotVO20.setPrimaryUomQty((double) collect.size());
                mtMaterialLotVO20.setMaterialId(sourceMaterialLot.getMaterialId());
                mtMaterialLotVO20.setEnableFlag("Y");
                mtMaterialLotVO20.setLot(lot);
                mtMaterialLotVO20.setQualityStatus("OK");
                mtMaterialLotVO20.setLocatorId(sourceMaterialLot.getLocatorId());
                mtMaterialLotVO20.setSupplierId(sourceMaterialLot.getSupplierId());
                mtMaterialLotVO20.setSupplierSiteId(sourceMaterialLot.getSupplierSiteId());
                mtMaterialLotVO20List.add(mtMaterialLotVO20);
            }
            long endDate6 = System.currentTimeMillis();
            log.info("<====当前在遍历第{}个来源条码,更新目标条码及扩展属性总耗时：{}毫秒", i, (endDate6 - startDate6));

            //修改来源物料批表
            long startDate7 = System.currentTimeMillis();

            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
            mtMaterialLotVO20.setTrxPrimaryUomQty((double) (collect.size() * -1));
            mtMaterialLotVO20.setPrimaryUomQty(sourceMaterialLot.getPrimaryUomQty() - (double) collect.size());
            if (BigDecimal.valueOf(mtMaterialLotVO20.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) == 0) {
                mtMaterialLotVO20.setEnableFlag("N");
            }
            mtMaterialLotVO20.setMaterialId(sourceMaterialLot.getMaterialId());
            sourceMaterialLotVO20List.add(mtMaterialLotVO20);
            long endDate7 = System.currentTimeMillis();
            log.info("<====当前在遍历第{}个来源条码,更新来源条码总耗时：{}毫秒", i, (endDate7 - startDate7));
            long endDate4 = System.currentTimeMillis();
            log.info("<====当前在遍历第{}个来源条码,总耗时：{}毫秒", i, (endDate4 - startDate4));
            i++;
        }

        long startDate10 = System.currentTimeMillis();
        //批量更新新物料批 业务场景下此时只会找到同一个新物料批
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
            String materialLotId = mtMaterialLotVO20List.get(0).getMaterialLotId();
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
            if(YES.equals(mtMaterialLot.getEnableFlag())){
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                mtMaterialLotVO20.setPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() + (double) cosNumSum);
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, Collections.singletonList(mtMaterialLotVO20), eventId, HmeConstants.ConstantValue.NO);
            }else{
                MtMaterialLotVO20 mtMaterialLotVO20 = mtMaterialLotVO20List.get(mtMaterialLotVO20List.size() - 1);
                mtMaterialLotVO20.setPrimaryUomQty((double) cosNumSum);
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, Collections.singletonList(mtMaterialLotVO20), eventId, HmeConstants.ConstantValue.NO);
            }
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(sourceMaterialLotVO20List)){
            //对重复的数据进行合并
            List<MtMaterialLotVO20> sourceMaterialLotVO20s = new ArrayList<>();
            Map<String, List<MtMaterialLotVO20>> sourceMaterialLotMap = sourceMaterialLotVO20List.stream().collect(Collectors.groupingBy(MtMaterialLotVO20::getMaterialLotId));
            for(Map.Entry<String, List<MtMaterialLotVO20>> entry:sourceMaterialLotMap.entrySet()){
                List<MtMaterialLotVO20> value = entry.getValue();
                if(value.size() > 1){
                    BigDecimal qty = value.stream().collect(CollectorsUtil
                            .summingBigDecimal(item -> BigDecimal.valueOf(item.getTrxPrimaryUomQty())));
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    mtMaterialLotVO20.setMaterialLotId(entry.getKey());
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(entry.getKey());
                    //这里的qty是负数，实际是减数量
                    mtMaterialLotVO20.setPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() + qty.doubleValue());
                    mtMaterialLotVO20.setMaterialId(value.get(value.size() - 1).getMaterialId());
                    if (new BigDecimal(mtMaterialLotVO20.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) == 0) {
                        mtMaterialLotVO20.setEnableFlag("N");
                    }
                    sourceMaterialLotVO20s.add(mtMaterialLotVO20);
                }else{
                    value.get(0).setTrxPrimaryUomQty(null);
                    sourceMaterialLotVO20s.addAll(value);
                }
            }
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, sourceMaterialLotVO20s, eventId1, HmeConstants.ConstantValue.NO);
        }
        //批量更新物料批扩展属性
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtCommonExtendVO7List)){
            mtCommonExtendVO7List = Collections.singletonList(mtCommonExtendVO7List.get(mtCommonExtendVO7List.size() - 1));
            mtExtendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", eventId, mtCommonExtendVO7List);
        }
        long endDate10 = System.currentTimeMillis();
        log.info("<====批量更新物料批和扩展属性耗时：{}毫秒", (endDate10 - startDate10));

        long endDate9 = System.currentTimeMillis();
        log.info("<====结束循环遍历来源条码，总耗时：{}毫秒", (endDate9 - startDate9));
    }

}
