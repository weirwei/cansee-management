package com.weirwei.cansee.controller;


import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.weirwei.cansee.service.IProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import com.fehead.lang.controller.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 项目表 前端控制器
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@RestController
@RequestMapping("/cansee/organization/{orgId}/proj")
@Slf4j
public class ProjectController extends BaseController {

    @Resource
    HttpServletRequest req;
    @Resource
    HttpServletResponse rsp;

    @Resource
    IProjectService projectService;

    @GetMapping("")
    public FeheadResponse getProj(@PageableDefault(size = 6, page = 1) Pageable pageable,
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
        return CommonReturnType.create(projectService.getProjPage(pageable, orgId, search));
    }

    @PostMapping("")
    public FeheadResponse addProj(@PathVariable("orgId") String orgId,
                                    @RequestParam("projName") String projName) throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&orgId=" + orgId +
                "&projName=" + projName
        );
        if (StringUtils.isEmpty(orgId)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "组织ID为空");
        }
        if (StringUtils.isEmpty(projName)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "项目名为空");
        }

        return CommonReturnType.create(projectService.addProj(uid, orgId, projName));
    }

    @DeleteMapping("/{projId}")
    public FeheadResponse delProj(@PathVariable("orgId") String orgId,
                                    @PathVariable("projId") String projId) throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&orgId=" + orgId +
                "&projId=" + projId
        );
        if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(projId)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "组织ID或项目ID为空");
        }
        projectService.delProj(uid, orgId, projId);

        return CommonReturnType.create(null);
    }

    @GetMapping("/{projId}/conf")
    public FeheadResponse getProjConf(@PathVariable("orgId") String orgId,
                                        @RequestParam("projId") String projId) throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&orgId=" + orgId +
                "&projId=" + projId
        );
        if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(projId)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "组织ID或项目ID为空");
        }
        projectService.getProjConf(uid, orgId, projId);

        return CommonReturnType.create(null);
    }
}

