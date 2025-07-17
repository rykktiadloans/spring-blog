package org.rl.frontendService.controllers;

import org.bouncycastle.math.raw.Mod;
import org.rl.shared.model.PostState;
import org.rl.shared.model.responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller responsible returning pages related to posts
 */
@Controller
public class PostController {
    /**
     * Return the page with a specific post
     * @param id ID of the post
     * @param model Model object
     * @return Page for a specific post
     */
    @GetMapping("/posts/{id}")
    public String getPostById(@PathVariable(name = "id") Integer id) {
        return "forward:/index.html";
    }

    /**
     * Return a page with a list of posts
     * @return Page with a list of posts
     */
    @GetMapping("/posts")
    public String getPosts() {
        return "forward:/index.html";
    }


}
