package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO;
import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO2;
import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO3;
import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO4;
import com.ruike.qms.app.service.QmsMaterialInspExemptService;
import com.ruike.qms.domain.entity.QmsMaterialInspExempt;
import com.ruike.qms.domain.repository.QmsMaterialInspExemptRepository;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsMaterialInspExemptMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;

/**
 * 物料免检表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:06:18
 */
@Service
public class QmsMaterialInspExemptServiceImpl extends BaseServiceImpl<QmsMaterialInspExempt> implements QmsMaterialInspExemptService {

    @Autowired
    private QmsMaterialInspExemptMapper qmsMaterialInspExemptMapper;
    @Autowired
    private QmsMaterialInspExemptRepository qmsMaterialInspExemptRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LovAdapter lovAdapter;

    @Override
    @ProcessLovValue
    public Page<QmsMaterialInspExemptDTO> listMaterialInspExemptForUi(Long tenantId, QmsMaterialInspExemptDTO2 dto,
                                                                      PageRequest pageRequest) {

        Page<QmsMaterialInspExemptDTO> exemptDTOList = PageHelper.doPage(pageRequest, () -> qmsMaterialInspExemptMapper.selectByConditionForUi(tenantId, dto));

        //V20211129 modify by penglin.sui 将值集查询提取到循环外
        List<LovValueDTO> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(exemptDTOList)){
            //取免检类型名称
            list = lovAdapter.queryLovValue("QMS.EXEMPTION_TYPE", tenantId);
        }

        for (QmsMaterialInspExemptDTO e : exemptDTOList
             ) {
            List<LovValueDTO> collect = list.stream().filter(f -> StringUtils.equals(f.getValue(), e.getExemptionType())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(collect)){
                e.setExemptionTypeName(collect.get(0).getMeaning());
            }
        }
        return exemptDTOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveMaterialInspExemptForUi(Long tenantId, QmsMaterialInspExemptDTO4 dto) {
        if (dto == null) {
            return null;
        }
        // 唯一性校验
        QmsMaterialInspExempt oldExempt = new QmsMaterialInspExempt();
        oldExempt.setTenantId(tenantId);
        oldExempt.setSiteId(dto.getSiteId());
        oldExempt.setMaterialId(dto.getMaterialId());
        oldExempt.setSupplierId(dto.getSupplierId());
        oldExempt = qmsMaterialInspExemptRepository.selectOne(oldExempt);
        if (oldExempt != null) {
            if (StringUtils.isEmpty(dto.getExemptionId())
                    || !dto.getExemptionId().equals(oldExempt.getExemptionId())) {
                throw new MtException(QmsConstants.ErrorCode.QMS_MATERIAL_INSP_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        QmsConstants.ErrorCode.QMS_MATERIAL_INSP_0001, QmsConstants.ConstantValue.QMS));
            }
        }
        QmsMaterialInspExempt exempt = new QmsMaterialInspExempt();
        if (StringUtils.isNotEmpty(dto.getExemptionId())) {
            oldExempt = qmsMaterialInspExemptRepository.selectByPrimaryKey(dto.getExemptionId());
        }
        if (oldExempt != null){
            BeanUtils.copyProperties(oldExempt, exempt);
        }
        BeanUtils.copyProperties(dto, exempt);
        exempt.setTenantId(tenantId);

        //2020-07-11 add by sanfeng.zahng 增加免检类型
        exempt.setType(dto.getExemptionType());

        // 保存
        if (StringUtils.isNotEmpty(exempt.getExemptionId())) {
            qmsMaterialInspExemptMapper.updateByPrimaryKey(exempt);
        } else {
            self().insertSelective(exempt);
        }
        return exempt.getExemptionId();
    }

    @Override
    public void removeMaterialInspExemptForUi(Long tenantId, List<QmsMaterialInspExemptDTO3> list) {
        List<String> sqlList = new ArrayList<String>();
        // 获取批量删除语句
        list.forEach(dto -> {
            QmsMaterialInspExempt exempt = new QmsMaterialInspExempt();
            exempt.setTenantId(tenantId);
            exempt.setExemptionId(dto.getExemptionId());
            sqlList.addAll(customDbRepository.getDeleteSql(exempt));
        });
        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }
}
