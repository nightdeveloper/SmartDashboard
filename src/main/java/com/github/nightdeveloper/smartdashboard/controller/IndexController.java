package com.github.nightdeveloper.smartdashboard.controller;

import com.github.nightdeveloper.smartdashboard.common.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    private static final Logger logger = LogManager.getLogger(IndexController.class);


    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String index() {
        logger.info("index requested");
        return "alive";
    }

    @RequestMapping(value = Constants.ENDPOINT_LOGOUT_USER, method = RequestMethod.GET)
    @ResponseBody
    public RedirectView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("logout requested");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);

        return new RedirectView("/");
    }
}
