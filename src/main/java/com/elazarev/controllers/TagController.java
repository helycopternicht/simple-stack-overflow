package com.elazarev.controllers;

import com.elazarev.domain.Tag;
import com.elazarev.domain.User;
import com.elazarev.exceptions.ResourceNotFoundException;
import com.elazarev.service.TagsService;
import com.elazarev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 21.02.18
 */
@Controller
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagsService service;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String index(@RequestParam Optional<Integer> page, Model model) throws ResourceNotFoundException {

        Page<Tag> tags = service.findAll(page.orElse(1));
        if (tags.getSize() == 0) {
            throw new ResourceNotFoundException();
        }

        Map<String, String> pager = new HashMap<>();
        pager.put("prevUrl", "/tags&page=" +(page.orElse(1) - 1));
        pager.put("nextUrl", "/tags&page=" +(page.orElse(1) + 1));

        model.addAttribute("page", tags);
        return "tag/tags";
    }

    @GetMapping("/{name}")
    public String details(@PathVariable String name, Model model) throws ResourceNotFoundException {
        Tag tag = service.findByName(name);
        if (tag == null) {
            throw new ResourceNotFoundException();
        }
        model.addAttribute("tag", tag);
        return "tag/details";
    }

    @GetMapping("/subscribe/{id}")
    public String subscribe(@PathVariable Long id, Principal principal) throws ResourceNotFoundException {
        Optional<Tag> tag = service.findById(id);
        tag.orElseThrow(() -> new ResourceNotFoundException());

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findUserByLogin(principal.getName());
        user.getTags().add(tag.get());
        userService.save(user);
        return "redirect:/tags/" + tag.get().getName();
    }

}
