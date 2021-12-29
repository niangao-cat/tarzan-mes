package tarzan.instruction.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.vo.MtInstructionDocVO4;

/**
 * 指令单据头表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
public interface MtInstructionDocMapper extends BaseMapper<MtInstructionDoc> {

    /**
     * select max num
     * 
     * @author benjamin
     * @date 2019-06-18 15:58
     * @param siteDateStr condition
     * @return Integer
     */
    Integer selectMaxNum(@Param(value = "tenantId") Long tenantId, @Param(value = "siteDateStr") String siteDateStr);

    /**
     * select by id list
     * 
     * @author benjamin
     * @date 2019-06-21 10:10
     * @param instructionDocIdList Id List
     * @return List
     */
    List<MtInstructionDoc> selectByIdList(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "instructionDocIdList") List<String> instructionDocIdList);

    /**
     * select by unique fields
     * 
     * condition: docNum or identification
     * 
     * @author benjamin
     * @date 2019-07-18 11:21
     * @param mtLogisticInstructionDoc MtLogisticInstructionDoc
     * @return List
     */
    List<MtInstructionDoc> selectByUnique(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "instructionDoc") MtInstructionDoc mtLogisticInstructionDoc);

    /**
     * 根据指定属性查询指令单据
     * @param tenantId
     * @param mtLogisticInstructionDoc
     * @return
     */
    List<String> selectMtInstructionDocIds(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "property") MtInstructionDocVO4 mtLogisticInstructionDoc);

    List<MtInstructionDoc> selectDocByTypes(MtInstructionDoc mtInstructionDoc);

    String selectAttrValue(@Param(value = "tenantId")Long tenantId,
                           @Param(value = "attrName")String attrName,
                           @Param(value = "instructionDocId")String instructionDocId);

    MtInstructionDoc selectByNum(@Param(value = "tenantId")Long tenantId,
                                 @Param(value = "docNum")String docNum);
}
