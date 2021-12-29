package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtDispositionGroupMember;

/**
 * 处置组分配资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:47
 */
public interface MtDispositionGroupMemberRepository
                extends BaseRepository<MtDispositionGroupMember>, AopProxy<MtDispositionGroupMemberRepository> {

    /**
     * dispositionGroupMemberQuery-获取处置组内处置方法清单
     *
     * @param tenantId
     * @param dispositionGroupId
     * @author guichuan.li
     * @date 2019/4/1
     */
    List<MtDispositionGroupMember> dispositionGroupMemberQuery(Long tenantId, String dispositionGroupId);

    /**
     * 获取处置方法所属处置组/sen.luo 2018-04-01
     *
     * @param tenantId
     * @param dispositionFunctionId
     * @return
     */
    List<MtDispositionGroupMember> dispositionFunctionLimitDispositionGroupQuery(Long tenantId,
                    String dispositionFunctionId);
}
