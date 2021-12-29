package io.tarzan.common.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtNumrangeRule;

/**
 * 号码段定义组合规则表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
public interface MtNumrangeRuleRepository extends BaseRepository<MtNumrangeRule>, AopProxy<MtNumrangeRuleRepository> {

}
