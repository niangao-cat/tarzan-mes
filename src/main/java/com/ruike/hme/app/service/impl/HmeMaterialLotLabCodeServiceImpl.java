package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeMaterialLotLabCodeService;
import com.ruike.hme.domain.entity.HmeMaterialLotLabCode;
import com.ruike.hme.domain.vo.HmeMaterialLotLabCodeVO;
import com.ruike.hme.infra.mapper.HmeMaterialLotLabCodeMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 条码实验代码表应用服务默认实现
 *
 * @author penglin.sui@hand-china.com 2021-01-25 14:23:29
 */
@Service
public class HmeMaterialLotLabCodeServiceImpl implements HmeMaterialLotLabCodeService {

    @Autowired
    private HmeMaterialLotLabCodeMapper hmeMaterialLotLabCodeMapper;

    @Override
    public Page<HmeMaterialLotLabCodeVO> selectLabCode(PageRequest pageRequest, String materialLotId, Long tenantId) {
        Page<HmeMaterialLotLabCodeVO> page = PageHelper.doPage(pageRequest, () -> hmeMaterialLotLabCodeMapper.selectLabCode(tenantId, materialLotId));
        return page;
    }
}
