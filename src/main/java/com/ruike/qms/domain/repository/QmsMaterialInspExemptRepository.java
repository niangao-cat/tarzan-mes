package com.ruike.qms.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.qms.domain.entity.QmsMaterialInspExempt;
import tarzan.modeling.domain.entity.MtModSite;

/**
 * 物料免检表资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:06:18
 */
public interface QmsMaterialInspExemptRepository extends BaseRepository<QmsMaterialInspExempt>, AopProxy<QmsMaterialInspExemptRepository> {

}
