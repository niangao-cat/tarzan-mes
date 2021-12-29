package io.tarzan.common.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtNumrangeRuleHis;

/**
 * 号码段定义组合规则历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
public interface MtNumrangeRuleHisRepository
                extends BaseRepository<MtNumrangeRuleHis>, AopProxy<MtNumrangeRuleHisRepository> {

}
