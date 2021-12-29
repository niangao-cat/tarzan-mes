package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtAssembleGroupControl;
import tarzan.method.domain.vo.MtAssembleGroupControlVO1;

/**
 * 装配组控制，标识具体装配控制下对装配组的控制，装配组可安装的工作单元Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
public interface MtAssembleGroupControlMapper extends BaseMapper<MtAssembleGroupControl> {

    List<MtAssembleGroupControl> propertyLimitAssembleGroupControlPropertyQuery(
            @Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtAssembleGroupControlVO1 dto);
}
