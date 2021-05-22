package com.weirwei.cansee.controller;


import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.weirwei.cansee.controller.vo.organization.OrgVO;
import com.weirwei.cansee.mapper.dao.Role;
import com.weirwei.cansee.service.IOrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 组织 前端控制器
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@RestController
@RequestMapping("/cansee/organization")
@Slf4j
public class OrganizationController extends BaseController {

    @Resource
    HttpServletRequest req;
    @Resource
    HttpServletResponse rsp;

    @Resource
    IOrganizationService organizationService;

    @PostMapping("/org")
    public FeheadResponse createOrg(@RequestParam("orgName") String orgName) throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&org_name=" + orgName
        );
        if (StringUtils.isEmpty(orgName)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "组织名不能为空");
        }

        OrgVO orgVO = organizationService.createOrg(uid, orgName);

        return CommonReturnType.create(orgVO);
    }

    @GetMapping("/org")
    public FeheadResponse getOrg(@PageableDefault(size = 6, page = 1) Pageable pageable,
                                 @RequestParam(value = "search", defaultValue = "") String search)  {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&pageable=" + pageable+
                "&search=" + search
        );

        return CommonReturnType.create(organizationService.getOrgPage(pageable, uid, search));
    }

    /**
     * todo
     * 删除组织
     *
     * @param orgId orgId
     * @return FeheadResponse
     */
    @DeleteMapping("/org")
    public FeheadResponse delOrg(@RequestParam("orgId") String orgId) throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&orgId=" + orgId
        );
        organizationService.delOrg(uid, orgId);

        return CommonReturnType.create(null);
    }

    @GetMapping("/{orgId}/member")
    public FeheadResponse getMember(@PageableDefault(size = 6, page = 1) Pageable pageable,
                                    @PathVariable("orgId") String orgId,
                                    @RequestParam(value = "search", defaultValue = "") String search) throws BusinessException {
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "orgId=" + orgId +
                "&pageable=" + pageable +
                "&search=" + search
        );
        if (StringUtils.isEmpty(orgId)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "组织ID为空");
        }
        return CommonReturnType.create(organizationService.getMemberPage(pageable, orgId, search));
    }

    @PostMapping("/{orgId}/member/{uid}")
    public FeheadResponse addMember(@PathVariable("orgId") String orgId,
                                    @PathVariable("uid") String addUid) throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&orgId=" + orgId +
                "&addUid=" + addUid
        );
        if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(addUid)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "组织ID或用户ID为空");
        }

        return CommonReturnType.create(organizationService.addMember(uid, orgId, addUid));
    }

    @DeleteMapping("/{orgId}/member/{uid}")
    public FeheadResponse delMember(@PathVariable("orgId") String orgId,
                                    @PathVariable("uid") String addUid) throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&orgId=" + orgId +
                "&addUid=" + addUid
        );
        if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(addUid)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "组织ID或用户ID为空");
        }
        organizationService.delMember(uid, orgId, addUid);

        return CommonReturnType.create(null);
    }

    @PutMapping("/{orgId}/member/{uid}")
    public FeheadResponse appointMember(@PathVariable("orgId") String orgId,
                                        @PathVariable("uid") String appointUid,
                                        @RequestParam("appointment") int appointment) throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&orgId=" + orgId +
                "&addUid=" + appointUid +
                "&appointment=" + appointment
        );
        if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(appointUid)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "组织ID或用户ID为空");
        }
        if (appointment != Role.ORG_ADMINISTRATOR && appointment != Role.ORG_CREATOR && appointment != Role.ORG_MEMBER) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "角色代码错误");
        }
        if (uid.equals(appointUid)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "自己任命自己，真逗");
        }
        organizationService.appointMember(uid, orgId, appointUid, appointment);

        return CommonReturnType.create(null);
    }
}

