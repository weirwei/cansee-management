package com.weirwei.cansee.service;

import com.fehead.lang.error.BusinessException;
import com.weirwei.cansee.controller.vo.organization.OrgPageVO;
import com.weirwei.cansee.controller.vo.organization.OrgVO;
import com.weirwei.cansee.controller.vo.user.UserSinglePageVO;
import com.weirwei.cansee.controller.vo.user.UserSingleVO;
import com.weirwei.cansee.mapper.dao.Organization;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;

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

    OrgPageVO getOrgPage(@PageableDefault(size = 6, page = 1) Pageable pageable, String uid, String search);

    void delOrg(String uid, String orgId) throws BusinessException;

    UserSinglePageVO getMemberPage(@PageableDefault(size = 6, page = 1) Pageable pageable, String orgId, String search);

    UserSingleVO addMember(String uid, String orgId, String addUid) throws BusinessException;

    void delMember(String uid, String orgId, String delUid) throws BusinessException;

    void appointMember(String uid, String orgId, String appointUid, int roleCode) throws BusinessException;
}
