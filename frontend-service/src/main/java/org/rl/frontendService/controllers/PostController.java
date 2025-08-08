package org.rl.frontendService.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "post_controller", description = "API for fetching pages related to posts")
public class PostController {
    /**
     * Return the page with a specific post
     * @param id ID of the post
     * @return Page for a specific post
     */
    @GetMapping("/posts/{id}")
    @Operation(summary = "Returns the frontend")
    public String getPostById(@PathVariable(name = "id") Integer id) {
        return "forward:/index.html";
    }

    /**
     * Return a page with a list of posts
     * @return Page with a list of posts
     */
    @GetMapping("/posts")
    @Operation(summary = "Returns the frontend")
    public String getPosts() {
        return "forward:/index.html";
    }


}
