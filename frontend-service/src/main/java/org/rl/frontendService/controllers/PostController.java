package org.rl.frontendService.controllers;

import org.bouncycastle.math.raw.Mod;
import org.rl.frontendService.clients.ApiClient;
import org.rl.frontendService.services.PostService;
import org.rl.shared.model.responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable(name = "id") Integer id, Model model) {
        PostResponse postResponse = this.postService.getPostById(id);
        model.addAttribute("post", postResponse);
        return "post";
    }
}
