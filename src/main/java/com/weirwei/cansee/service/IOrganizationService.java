package com.weirwei.cansee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fehead.lang.error.BusinessException;
import com.weirwei.cansee.controller.vo.organization.OrgPageVO;
import com.weirwei.cansee.controller.vo.organization.OrgVO;
import com.weirwei.cansee.controller.vo.user.UserSinglePageVO;
import com.weirwei.cansee.controller.vo.user.UserSingleVO;
import com.weirwei.cansee.mapper.dao.Organization;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 * 组织 服务类
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
public interface IOrganizationService extends IService<Organization> {

    OrgVO createOrg(String uid, String orgName);

    OrgPageVO getOrgPage(Pageable pageable, String uid, String search);

    void delOrg(String uid, String orgId) throws BusinessException;

    UserSinglePageVO getMemberPage(Pageable pageable, String orgId, String search);

    UserSingleVO addMember(String uid, String orgId, String addUid) throws BusinessException;

    void delMember(String uid, String orgId, String delUid) throws BusinessException;

    void appointMember(String uid, String orgId, String appointUid, int roleCode) throws BusinessException;
}
