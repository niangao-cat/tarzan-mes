package com.ruike.hme.app.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ruike.hme.app.service.HmeTagFormulaHeadService;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.entity.HmeEquipmentWkcRel;
import com.ruike.hme.domain.entity.HmeEquipmentWkcRelHis;
import com.ruike.hme.domain.entity.HmeTagFormulaHead;
import com.ruike.hme.domain.repository.HmeTagFormulaHeadRepository;
import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;
import com.ruike.hme.domain.vo.HmeTagFormulaVO;
import com.ruike.hme.infra.constant.HmeConstants;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.choerodon.core.domain.Page;
import tarzan.general.domain.vo.MtEventCreateVO;

import java.util.Calendar;
import java.util.List;

/**
 * 数据采集项公式头表应用服务默认实现
 *
 * @author guiming.zhou@hand-china.com 2020-09-21 19:50:40
 */
@Service
public class HmeTagFormulaHeadServiceImpl implements HmeTagFormulaHeadService {

    @Autowired
    private HmeTagFormulaHeadRepository hmeTagFormulaHeadRepository;

    /**
     * 查询公式头
     *
     * @param tenantId
     * @param vo
     * @param pageRequest
     * @author guiming.zhou@hand-china.com 2020/9/25 13:55
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeTagFormulaHeadVO>
     */
    @Override
    public Page<HmeTagFormulaHeadVO> getTagHeadList(Long tenantId, HmeTagFormulaVO vo, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> hmeTagFormulaHeadRepository.getHeadList(tenantId, vo.getOperationId(), vo.getTagGroupId(), vo.getTagId(), pageRequest));
    }

    /**
     * 删除公式头
     *
     * @param tenantId
     * @param tagFormulaHeadId
     * @author guiming.zhou@hand-china.com 2020/9/25 13:55
     * @return void
     */
    @Override
    public void deleteHmeTagFormulaHead(Long tenantId, String tagFormulaHeadId) {
        //根据头ID查询 公式头
        HmeTagFormulaHead hmeEquipment = hmeTagFormulaHeadRepository.selectOne(new HmeTagFormulaHead() {{
            setTenantId(tenantId);
            setTagFormulaHeadId(tagFormulaHeadId);
        }});
        //删除公式头
        hmeTagFormulaHeadRepository.deleteByPrimaryKey(tagFormulaHeadId);

    }
}
