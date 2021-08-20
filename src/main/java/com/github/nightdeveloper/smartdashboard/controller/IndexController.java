package com.github.nightdeveloper.smartdashboard.controller;

import com.github.nightdeveloper.smartdashboard.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class IndexController {

    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
        // do nothing
    }

    @GetMapping(value = "/")
    @ResponseBody
    public String index() {
        log.info("index requested");
        return "alive";
    }

    @GetMapping(value = Constants.ENDPOINT_LOGOUT_USER)
    @ResponseBody
    public RedirectView logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("logout requested");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);

        return new RedirectView("/");
    }
}
