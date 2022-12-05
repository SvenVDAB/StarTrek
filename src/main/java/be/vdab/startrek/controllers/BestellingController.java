package be.vdab.startrek.controllers;

import be.vdab.startrek.domain.Bestelling;
import be.vdab.startrek.exceptions.BudgetTooLowException;
import be.vdab.startrek.exceptions.WerknemerNietGevondenException;
import be.vdab.startrek.services.BestellingService;
import be.vdab.startrek.services.WerknemerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("werknemer")
public class BestellingController {
    private final BestellingService bestellingService;
    private final WerknemerService werknemerService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BestellingController(BestellingService bestellingService, WerknemerService werknemerService) {
        this.bestellingService = bestellingService;
        this.werknemerService = werknemerService;
    }

    @GetMapping("{id}/bestellingen")
    public ModelAndView toonBestellingen(@PathVariable long id) {
        var modelAndView = new ModelAndView("bestellingen");
        modelAndView.addObject("bestellingen", bestellingService.findAll(id));
        werknemerService.findById(id).ifPresent(werknemer -> modelAndView.addObject("werknemer", werknemer));
        return modelAndView;
    }

    @GetMapping("{id}/nieuweBestelling")
    public ModelAndView toonBestellingForm(@PathVariable long id) {
        var modelAndView = new ModelAndView("nieuweBestelling");
        modelAndView.addObject(new Bestelling(0, id, null, null));
        werknemerService.findById(id).ifPresent(werknemer -> modelAndView.addObject("werknemer", werknemer));
        return modelAndView;
    }

    @PostMapping("{id}/nieuweBestelling")
    public ModelAndView voegBestellingToe(@Valid Bestelling bestelling,
                                    Errors errors,
                                    @PathVariable long id,
                                    RedirectAttributes redirect) {
        if (errors.hasErrors()) {
            var modelAndView = new ModelAndView("nieuweBestelling");
            werknemerService.findById(id).ifPresent(werknemer -> modelAndView.addObject("werknemer", werknemer));
            //modelAndView.addObject(bestelling); //// waarom wordt dit niet automatisch meegegeven !!!???
            //modelAndView.addObject(errors);     ///// zelfde vraag !!!
            return modelAndView;
        }
        try {
            bestellingService.bestel(bestelling);
        } catch (BudgetTooLowException ex) {
            logger.error("budget te laag", ex);
            redirect.addAttribute("budgetFout", "Onvoldoende budget");
            return new ModelAndView("redirect:/werknemer/{id}");
        } catch (WerknemerNietGevondenException ex) {
            logger.error("werknemer niet gevonden", ex);
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/werknemer/{id}/bestellingen");
    }
}
