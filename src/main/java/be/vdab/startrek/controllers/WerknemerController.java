package be.vdab.startrek.controllers;

import be.vdab.startrek.services.WerknemerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("werknemer")
public class WerknemerController {
    private final WerknemerService werknemerService;

    public WerknemerController(WerknemerService werknemerService) {
        this.werknemerService = werknemerService;
    }
    @GetMapping("{id}")
    public ModelAndView zoekWerknemer(@PathVariable long id) {
        var modelAndView = new ModelAndView("werknemer");
        werknemerService.findById(id).ifPresent(werknemer ->
                modelAndView.addObject("werknemer", werknemer));
        return modelAndView;
    }
}
