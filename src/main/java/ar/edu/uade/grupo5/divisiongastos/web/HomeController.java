package ar.edu.uade.grupo5.divisiongastos.web;

import ar.edu.uade.grupo5.divisiongastos.application.service.GroupService;
import ar.edu.uade.grupo5.divisiongastos.domain.model.DomainException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final GroupService groupService;

    public HomeController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("groups", groupService.listGroups());
        return "index";
    }

    @PostMapping("/groups")
    public String createGroup(@RequestParam String name, RedirectAttributes redirectAttributes) {
        try {
            var group = groupService.createGroup(name);
            return "redirect:/groups/" + group.id().value();
        } catch (DomainException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/";
        }
    }
}
