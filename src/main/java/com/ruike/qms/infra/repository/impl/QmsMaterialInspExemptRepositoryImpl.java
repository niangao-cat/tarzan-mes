package com.ruike.qms.infra.repository.impl;

import com.ruike.wms.app.service.impl.WmsCommonServiceComponent;
import lombok.extern.slf4j.Slf4j;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.qms.domain.entity.QmsMaterialInspExempt;
import com.ruike.qms.domain.repository.QmsMaterialInspExemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;

/**
 * 物料免检表 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:06:18
 */
@Slf4j
@Component
public class QmsMaterialInspExemptRepositoryImpl extends BaseRepositoryImpl<QmsMaterialInspExempt> implements QmsMaterialInspExemptRepository {



}
