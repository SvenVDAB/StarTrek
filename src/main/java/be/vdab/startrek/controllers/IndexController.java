package be.vdab.startrek.controllers;

import be.vdab.startrek.services.WerknemerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    private final WerknemerService werknemerService;

    public IndexController(WerknemerService werknemerService) {
        this.werknemerService = werknemerService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index", "werknemers", werknemerService.findAll());
    }
}
