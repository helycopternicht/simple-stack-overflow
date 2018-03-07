package com.elazarev.controllers;

import com.elazarev.Paths;
import com.elazarev.service.TagsService;
import com.elazarev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 21.02.18
 */
@Controller
public class TagController {

    private TagsService tagService;

    private UserService userService;

    @Autowired
    public TagController(TagsService service,UserService userService) {
        this.tagService = service;
        this.userService = userService;
    }

    @GetMapping(Paths.PATH_TAGS_ALL)
    public String index(@RequestParam Optional<Integer> page, Model model) {
        Map<String, String> pager = new HashMap<>();
        pager.put("prevUrl", "/tags&page=" +(page.orElse(1) - 1));
        pager.put("nextUrl", "/tags&page=" +(page.orElse(1) + 1));

        model.addAttribute("page", tagService.getTagPage(page));
        return "tag/tags";
    }

    @GetMapping(Paths.PATH_TAGS_SHOW)
    public String details(@PathVariable String name, Model model) {
        model.addAttribute("tag", tagService.getTagByName(name));
        return "tag/details";
    }

    @GetMapping(Paths.PATH_TAGS_SUBSCRIBE)
    public String subscribe(@PathVariable String name, Principal principal, Model model) {
        userService.subscribeToTag(principal, name);
        model.addAttribute("tag", tagService.getTagByName(name));
        return "tag/details";
    }
}
