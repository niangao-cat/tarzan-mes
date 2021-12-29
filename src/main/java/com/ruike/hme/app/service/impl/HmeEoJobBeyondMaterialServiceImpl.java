package com.ruike.hme.app.service.impl;

import java.util.List;

import com.ruike.hme.domain.vo.HmeEoJobBeyondMaterialVO;
import com.ruike.hme.app.service.HmeEoJobBeyondMaterialService;
import com.ruike.hme.domain.entity.HmeEoJobBeyondMaterial;
import com.ruike.hme.domain.repository.HmeEoJobBeyondMaterialRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 工序作业平台-计划外物料应用服务默认实现
 *
 * @author liyuan.lv@hand-china.com 2020-07-15 15:27:06
 */
@Service
public class HmeEoJobBeyondMaterialServiceImpl extends BaseServiceImpl<HmeEoJobBeyondMaterial> implements HmeEoJobBeyondMaterialService {
    @Autowired
    private HmeEoJobBeyondMaterialRepository repository;

    @Override
    public List<HmeEoJobBeyondMaterial> listForUi(Long tenantId, HmeEoJobBeyondMaterialVO dto) {
        return repository.list(tenantId, dto);
    }
}
