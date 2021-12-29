package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeCmsEoSnRel;
import com.ruike.hme.domain.repository.HmeCmsEoSnRelRepository;
import com.ruike.hme.domain.vo.HmeCmsEoSnRelImportVO;
import com.ruike.hme.infra.mapper.HmeCmsEoSnRelMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * CMS-光纤EO和盖板SN关系表导入数据库
 *
 *@author qinxia.huang@raycus-china.com 2021/9/28 14:45
 */
@ImportService(templateCode = "HME.CMS_EO_SN_REL")
public class HmeCmsEoSnRelImportServiceImpl implements IBatchImportService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private HmeCmsEoSnRelRepository hmeCmsEoSnRelRepository;
    @Autowired
    private HmeCmsEoSnRelMapper hmeCmsEoSnRelMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeCmsEoSnRelImportVO> hmeCmsEoSnRelImportVOList = new ArrayList<>();
            for (String vo : data) {
                HmeCmsEoSnRelImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeCmsEoSnRelImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                // 设备编码
                if (StringUtils.isBlank(importVO.getEquipmentNum())) {
                    throw new MtException("HME_CMSEOSN_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CMSEOSN_IMPORT_001", "HME", "设备编码"));
                }

                // 光纤EO
                if (StringUtils.isBlank(importVO.getIdentification())) {
                    throw new MtException("HME_CMSEOSN_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CMSEOSN_IMPORT_001", "HME", "光纤EO"));
                }
                List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).andWhere(Sqls.custom()
                        .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtEo.FIELD_IDENTIFICATION, importVO.getIdentification())).build());
                if (CollectionUtils.isEmpty(mtEoList)) {
                    throw new MtException("HME_CMSEOSN_IMPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CMSEOSN_IMPORT_002", "HME", importVO.getIdentification()));
                }

                importVO.setEoId(mtEoList.get(0).getEoId());
                // 盖板SN
                if (StringUtils.isBlank(importVO.getSnNum())) {
                    throw new MtException("HME_CMSEOSN_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CMSEOSN_IMPORT_001", "HME", "盖板SN"));
                }
                // 导入数据
                hmeCmsEoSnRelImportVOList.add(importVO);
            }
            this.batchImportCmsEoSn(tenantId, hmeCmsEoSnRelImportVOList);
        }
        return true;
    }
    /**
     * 批量导入数据
     *
     * @param tenantId
     * @param hmeCmsEoSnRelImportVOList
     * @return void
     * @author qinxia.huang@raycus-china.com 2021/9/28 16:35
     */
    private void batchImportCmsEoSn(Long tenantId, List<HmeCmsEoSnRelImportVO> hmeCmsEoSnRelImportVOList) {
        if (CollectionUtils.isNotEmpty(hmeCmsEoSnRelImportVOList)) {//导入的数据不为空
            //设备编码+光纤EO+盖板SN，将这三个字段组合起来
            Map<String, List<HmeCmsEoSnRelImportVO>> hmeCmsEoSnImportMap = hmeCmsEoSnRelImportVOList.stream().collect(Collectors.groupingBy(fy -> spliceStr(fy)));
            List<HmeCmsEoSnRel> insertList = new ArrayList<>();
            List<HmeCmsEoSnRel> updateList = new ArrayList<>();
            for (Map.Entry<String, List<HmeCmsEoSnRelImportVO>> cmsEoSnEntry : hmeCmsEoSnImportMap.entrySet()) {
                List<HmeCmsEoSnRelImportVO> value = cmsEoSnEntry.getValue();
                if (value.size() > 1) {
                    throw new MtException("HME_CMSEOSN_IMPORT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CMSEOSN_IMPORT_003", "HME", value.get(0).getEquipmentNum(), value.get(0).getEoId(), value.get(0).getSnNum()));
                }
                HmeCmsEoSnRelImportVO hmeCmsEoSnRelImportVO = value.get(0);
                HmeCmsEoSnRel hmeCmsEoSnRel = new HmeCmsEoSnRel();
                //将导入到数据库的数据复制到cms_eo_sn关系表中
                BeanUtils.copyProperties(hmeCmsEoSnRelImportVO, hmeCmsEoSnRel);//可以复制A对象中的属性值到B对象的同名同类型值中。一般用于把普通对象的值复制到VO对象中。

                List<HmeCmsEoSnRel> hmeCmsEoSnRelList = hmeCmsEoSnRelRepository.select(new HmeCmsEoSnRel() {{
                    setTenantId(tenantId);
                    setEquipmentNum(hmeCmsEoSnRel.getEquipmentNum());
                    setEoId(hmeCmsEoSnRel.getEoId());
                    setSnNum(hmeCmsEoSnRel.getSnNum());
                }});
                if (CollectionUtils.isNotEmpty(hmeCmsEoSnRelList)) {//cms_eo_sn的关系表不为空
                    hmeCmsEoSnRel.setCmsEoSnRelId(hmeCmsEoSnRelList.get(0).getCmsEoSnRelId());
                    updateList.add(hmeCmsEoSnRel);//将cms_eo_sn关系表中的数据加进updateList列表中
                } else {//cms_eo_sn的关系表为空， insertList列表中写入以前的数据
                    hmeCmsEoSnRel.setTenantId(tenantId);
                    insertList.add(hmeCmsEoSnRel);
                }
            }
            if (CollectionUtils.isNotEmpty(updateList)) {//updateList列表的数据不为空
                Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
                List<List<HmeCmsEoSnRel>> splitSqlList = InterfaceUtils.splitSqlList(updateList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeCmsEoSnRel> domains : splitSqlList) {
                    hmeCmsEoSnRelMapper.myBatchUpdate(tenantId, userId, domains);
                }

            }
            if (CollectionUtils.isNotEmpty(insertList)) {//插入数据不为空
                List<List<HmeCmsEoSnRel>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeCmsEoSnRel> domains : splitSqlList) {
                    hmeCmsEoSnRelRepository.batchInsert(domains);//批量插入
                }
            }
        }
    }

    private String spliceStr(HmeCmsEoSnRelImportVO importVO) {
        StringBuffer sb = new StringBuffer();
        sb.append(importVO.getEquipmentNum());
        sb.append(importVO.getEoId());
        sb.append(importVO.getSnNum());
        return sb.toString();
    }


}
