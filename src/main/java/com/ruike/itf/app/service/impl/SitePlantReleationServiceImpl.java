package com.ruike.itf.app.service.impl;

import org.hzero.core.base.BaseAppService;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import com.ruike.itf.app.service.SitePlantReleationService;
import com.ruike.itf.domain.entity.SitePlantReleation;
import com.ruike.itf.domain.repository.SitePlantReleationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * ERP工厂与站点映射关系应用服务默认实现
 *
 * @author taowen.wang@hand-china.com 2021-07-06 14:14:34
 */
@Service
public class SitePlantReleationServiceImpl extends BaseAppService implements SitePlantReleationService {

}
