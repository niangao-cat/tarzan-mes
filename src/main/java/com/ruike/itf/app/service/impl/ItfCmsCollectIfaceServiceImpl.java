package com.ruike.itf.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.infra.mapper.HmeCmsEoSnRelMapper;
import com.ruike.itf.api.dto.ApCollectItfDTO1;
import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.CmsCollectItfDTO;
import com.ruike.itf.app.service.ItfCmsCollectIfaceService;
import com.ruike.itf.domain.entity.ItfCmsCollectIface;
import com.ruike.itf.domain.repository.ItfCmsCollectIfaceRepository;
import com.ruike.itf.infra.mapper.ItfCmsCollectIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.message.MessageClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * CMS数据采集接口表应用服务默认实现
 *
 * @author yonghui.zhu@hand-china.com 2020-08-04 19:51:39
 */

@Service
@Slf4j
public class ItfCmsCollectIfaceServiceImpl extends BaseServiceImpl<ItfCmsCollectIface> implements ItfCmsCollectIfaceService {

    private final ItfCmsCollectIfaceRepository itfCmsCollectIfaceRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private ItfCmsCollectIfaceMapper itfCmsCollectIfaceMapper;

    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private HmeCmsEoSnRelRepository hmeCmsEoSnRelRepository;
    @Autowired
    private HmeCmsEoSnRelMapper hmeCmsEoSnRelMapper;

    private Object List;


    @Autowired
    public ItfCmsCollectIfaceServiceImpl(ItfCmsCollectIfaceRepository itfCmsCollectIfaceRepository, MtErrorMessageRepository mtErrorMessageRepository) {
        this.itfCmsCollectIfaceRepository = itfCmsCollectIfaceRepository;
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
    private boolean validate(Long tenantId, List<ItfCmsCollectIface> itfList) {
        boolean validFlag = true;
        // 反射得到字段map
        Map<String, Field> fieldMap = Arrays.stream(ItfCmsCollectIface.class.getDeclaredFields()).collect(Collectors.toMap(Field::getName, rec -> rec, (key1, keys2) -> key1));
        for (ItfCmsCollectIface itf : itfList) {
            String processMessage = "";
            // 验证字段，不能为空的字段需要验证，设备编码，eo码，sn码。
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getEquipmentNum()), processMessage, "HME_CMSEOSN_IMPORT_001", "设备编码");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getIdentification()), processMessage, "HME_CMSEOSN_IMPORT_001", "光纤EO");
            processMessage = InterfaceUtils.processErrorMessage(tenantId, StringUtils.isBlank(itf.getSnNum()), processMessage, "HME_CMSEOSN_IMPORT_001", "盖板SN");
            // 验证eo码是否存在于MES系统
            List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).andWhere(Sqls.custom()
                    .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtEo.FIELD_IDENTIFICATION, itf.getIdentification())).build());
            Boolean eoFlag = false;
            if (CollectionUtils.isEmpty(mtEoList)) {
                eoFlag = true;
            }
            processMessage = InterfaceUtils.processErrorMessage(tenantId, eoFlag, processMessage, "HME_CMSEOSN_IMPORT_002", itf.getIdentification());
            itf.setEoId(mtEoList.get(0).getEoId());
            //如果返回的信息不为空，说明这边数据校验发现了上位机传来的数据有误，此时返回false
            if (StringUtils.isNotBlank(processMessage)) {
                itf.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                itf.setProcessMessage(processMessage);
                itf.setProcessDate(new Date());
                itfCmsCollectIfaceRepository.updateByPrimaryKeySelective(itf);
                validFlag = false;
            }
        }
        return validFlag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DataCollectReturnDTO> invoke(Long tenantId, List<CmsCollectItfDTO> collectList) {
        log.info("ItfCmsCollectIfaceServiceImpl.invoke.start");
        if (CollectionUtils.isEmpty(collectList)) {
            return new ArrayList<>();
        }
        // 插入接口表
        List<ItfCmsCollectIface> list = new ArrayList<>();
        Date nowDate = new Date();
        List<String> ifaceIdList = customSequence.getNextKeys("itf_cms_collect_iface_s", collectList.size());
        String batchId = customSequence.getNextKey("itf_cms_collect_iface_cid_s");
        int ifaceIdIndex = 0;
        for (CmsCollectItfDTO data : collectList) {
            ItfCmsCollectIface itf = new ItfCmsCollectIface();
            itf.setEquipmentNum(data.getEquipmentNum());
            itf.setIdentification(data.getIdentification());
            itf.setSnNum(data.getSnNum());
            itf.setCmsCollectIfaceId(ifaceIdList.get(ifaceIdIndex++));
            itf.setCid(Long.valueOf(batchId));
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
        log.info("ItfCmsCollectIfaceServiceImpl.insterIface.start");
        myBatchInsert(list);
        log.info("ItfCmsCollectIfaceServiceImpl.insterIface.end");

        // 验证接口表
        log.info("ItfFacCollectIfaceServiceImpl.check.start");
        boolean validFlag = this.validate(tenantId, list);
        log.info("ItfFacCollectIfaceServiceImpl.check.end");

        //插入业务表
        log.info("ItfFacCollectIfaceServiceImpl.batchImport.start");
        if (validFlag) {
            batchImport(tenantId , list);
        }
        log.info("ItfFacCollectIfaceServiceImpl.batchImport.end");

        // 返回处理结果
        return InterfaceUtils.getReturnList(list);
    }

    private void batchImport(Long tenantId, List<ItfCmsCollectIface> cmsCollectItfDTOList) {
        if (CollectionUtils.isNotEmpty(cmsCollectItfDTOList)) {
            //设备编码+光纤EO+盖板SN，将这三个字段组合起来
            Map<String, List<ItfCmsCollectIface>> cmsCollectItfDTOImportMap = cmsCollectItfDTOList.stream().collect(Collectors.groupingBy(fy -> spliceStr(fy)));
            List<HmeCmsEoSnRel> insertList = new ArrayList<>();
            List<HmeCmsEoSnRel> updateList = new ArrayList<>();
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            for (Map.Entry<String, List<ItfCmsCollectIface>> cmsEoSnEntry : cmsCollectItfDTOImportMap.entrySet()) {
                List<ItfCmsCollectIface> value = cmsEoSnEntry.getValue();
                if (value.size() > 1) {//导入的表格数据里面不能存在重复的行，即（设备编码+光纤EO+盖板SN）数据具有唯一性。
                    throw new MtException("HME_CMSEOSN_IMPORT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CMSEOSN_IMPORT_003", "HME", value.get(0).getEquipmentNum(), value.get(0).getIdentification(), value.get(0).getSnNum()));
                }
                ItfCmsCollectIface cmsCollectItfDTO = value.get(0);
                HmeCmsEoSnRel hmeCmsEoSnRel = new HmeCmsEoSnRel();
                //将接口表数据的属性复制到业务表中
                BeanUtils.copyProperties(cmsCollectItfDTO , hmeCmsEoSnRel);
                List<HmeCmsEoSnRel> hmeCmsEoSnRelList = hmeCmsEoSnRelRepository.select(new HmeCmsEoSnRel() {{
                    setTenantId(tenantId);
                    setEquipmentNum(hmeCmsEoSnRel.getEquipmentNum());
                    setEoId(hmeCmsEoSnRel.getEoId());
                    setSnNum(hmeCmsEoSnRel.getSnNum());
                }});
                if (CollectionUtils.isNotEmpty(hmeCmsEoSnRelList)) {
                    hmeCmsEoSnRel.setCmsEoSnRelId(hmeCmsEoSnRelList.get(0).getCmsEoSnRelId());
                    updateList.add(hmeCmsEoSnRel);
                } else {
                    hmeCmsEoSnRel.setTenantId(tenantId);
                    insertList.add(hmeCmsEoSnRel);
                }
            }

            try{
                if (CollectionUtils.isNotEmpty(updateList)) {
                    List<List<HmeCmsEoSnRel>> splitSqlList = InterfaceUtils.splitSqlList(updateList, SQL_ITEM_COUNT_LIMIT);
                    for (List<HmeCmsEoSnRel> domains : splitSqlList) {
                        hmeCmsEoSnRelMapper.myBatchUpdate(tenantId, userId, domains);
                    }

                }
                if (CollectionUtils.isNotEmpty(insertList)) {
                    List<List<HmeCmsEoSnRel>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
                    for (List<HmeCmsEoSnRel> domains : splitSqlList) {
                        hmeCmsEoSnRelRepository.batchInsert(domains);
                    }
                }
                // 更新接口表状态
                List<ItfCmsCollectIface> updateItfaceList = cmsCollectItfDTOList.stream().map(cms -> {
                    cms.setProcessDate(new Date());
                    cms.setProcessStatus("S");
                    cms.setProcessMessage("成功");
                    return cms;
                }).collect(Collectors.toList());
                itfCmsCollectIfaceMapper.myBatchUpdate(tenantId, userId, updateItfaceList);
            } catch (Exception e) {
                // 更新接口表状态
                List<ItfCmsCollectIface> updateItfaceList = cmsCollectItfDTOList.stream().map(cms -> {
                    cms.setProcessDate(new Date());
                    cms.setProcessStatus("E");
                    cms.setProcessMessage(e.getMessage());
                    return cms;
                }).collect(Collectors.toList());
                itfCmsCollectIfaceMapper.myBatchUpdate(tenantId, userId, updateItfaceList);
            }
        }
    }

    private String spliceStr(ItfCmsCollectIface cmsCollectItfDTO) {
        StringBuffer sb = new StringBuffer();
        sb.append(cmsCollectItfDTO.getEquipmentNum());
        sb.append(cmsCollectItfDTO.getEoId());
        sb.append(cmsCollectItfDTO.getSnNum());
        return sb.toString();
    }

    /**
     *@description 批量新增
     *@author wenzhang.yu@hand-china.com
     *@date 2020/10/7 16:42
     *@param insertList
     *@return void
     **/
    @Transactional(rollbackFor = Exception.class)
    public void myBatchInsert(List<ItfCmsCollectIface> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<ItfCmsCollectIface>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfCmsCollectIface> domains : splitSqlList) {
                itfCmsCollectIfaceMapper.insertIface(domains);
            }
        }
    }
}

