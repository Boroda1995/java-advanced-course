package com.galdovich.security.controller;

import com.galdovich.security.service.InfoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @Autowired
    private InfoConfig infoConfig;

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('VIEW_INFO')")
    public InfoConfig getRandomStats() {
        return infoConfig;
    }
}
