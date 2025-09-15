package com.galdovich.security.controller;

import com.galdovich.security.service.InfoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @Autowired
    private InfoConfig infoConfig;

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('VIEW_INFO')")
    public String getInfoPage(Model model) {
        model.addAttribute("appDescription", infoConfig.getApplicationDescription());
        model.addAttribute("appVersion", infoConfig.getApplicationVersion());
        model.addAttribute("openapiInfo", infoConfig.getOpenapiInfo());
        return "info";
    }

    @GetMapping("/about")
    public String about() {
        return "Public information";
    }
}
