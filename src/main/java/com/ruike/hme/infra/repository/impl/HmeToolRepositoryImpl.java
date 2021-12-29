package com.ruike.hme.infra.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ruike.hme.api.dto.HmeToolDTO;
import com.ruike.hme.domain.entity.HmeToolHis;
import com.ruike.hme.domain.repository.HmeToolHisRepository;
import com.ruike.hme.domain.vo.HmeToolVO;
import com.ruike.hme.domain.vo.HmeToolVO2;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeToolMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeTool;
import com.ruike.hme.domain.repository.HmeToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.method.domain.util.Constant;
import tarzan.modeling.domain.entity.MtModEnterprise;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

/**
 * 工装基础数据表 资源库实现
 *
 * @author li.zhang13@hand-china.com 2021-01-07 10:06:45
 */
@Component
public class HmeToolRepositoryImpl extends BaseRepositoryImpl<HmeTool> implements HmeToolRepository {

    @Autowired
    private HmeToolMapper hmeToolMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeToolHisRepository hmeToolHisRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    /**
     * @param tenantId
     * @param hmeTooldto
     * @Description 根据查询条件得出工装基础数据
     * @Author li.zhang13@hand-china.com
     */
    @Override
    @ProcessLovValue
    public Page<HmeToolVO> selectHmeTOOLs(PageRequest pageRequest, Long tenantId, HmeToolDTO hmeTooldto) {
        return PageHelper.doPage(pageRequest, () -> hmeToolMapper.selectHmeTOOLs(tenantId,hmeTooldto));
    }

    @Override
    public MtModWorkcell workcellScan(Long tenantId, String workcellCode) {
        if(StringUtils.isBlank(workcellCode)){
            //工位不能为空!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        MtModWorkcell mtModWorkcellPara = new MtModWorkcell();
        mtModWorkcellPara.setTenantId(tenantId);
        mtModWorkcellPara.setWorkcellCode(workcellCode);
        mtModWorkcellPara.setEnableFlag(HmeConstants.ConstantValue.YES);
        mtModWorkcellPara.setWorkcellType("STATION");
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectOne(mtModWorkcellPara);
        if(Objects.isNull(mtModWorkcell)){
            //当前工位无效,请检查工位条码
            throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_001", "HME"));
        }
        return mtModWorkcell;
    }

    @Override
    @ProcessLovValue
    public Page<HmeToolVO2> hmeToolQuery(Long tenantId, String workcellId, PageRequest pageRequest) {
        Page<HmeToolVO2> hmeTools = PageHelper.doPage(pageRequest, () -> hmeToolMapper.selectToolByWorkcellId(tenantId,workcellId));
        return hmeTools;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeTool> hmeToolSave(Long tenantId, List<HmeTool> dtoList) {
        if(CollectionUtils.isEmpty(dtoList)){
            //没有需要保存的数据, 请确认!
            throw new MtException("HME_EXCEPTION_MAN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_MAN_002", "HME"));
        }
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        List<String> cidS = customDbRepository.getNextKeys("hme_tool_cid_s", dtoList.size());
        List<String> updateSnDataSqlList = new ArrayList<>();
        int count = 0;
        for (HmeTool updateData : dtoList) {
            HmeTool hmeTool = new HmeTool();
            hmeTool.setToolId(updateData.getToolId());
            hmeTool.setQty(updateData.getQty());
            hmeTool.setCid(Long.valueOf(cidS.get(count)));
            hmeTool.setLastUpdatedBy(userId);
            hmeTool.setLastUpdateDate(CommonUtils.currentTimeGet());
            updateSnDataSqlList.addAll(customDbRepository.getUpdateSql(hmeTool));
            count++;
        }
        jdbcTemplate.batchUpdate(updateSnDataSqlList.toArray(new String[updateSnDataSqlList.size()]));
        //新增历史
        List<HmeToolHis> hmeToolHisList = hmeToolHisRepository.insertHmeToolHis(tenantId,dtoList);
        return dtoList;
    }

    @Override
    @ProcessLovValue
    public List<HmeToolVO> listExport(Long tenantId, HmeToolDTO dto) {
        return hmeToolMapper.selectHmeTOOLs(tenantId,dto);
    }
}
