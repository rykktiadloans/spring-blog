package org.rl.apiService.controllers.api.v1;

import com.rometools.rome.feed.rss.Channel;
import org.rl.apiService.services.RssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

@RestController
@RequestMapping(path = "/api/v1/rss")
public class RssController {
    @Autowired
    private RssService rssService;

    @GetMapping("")
    public Channel getFeed() {
        return this.rssService.getView();
    }
}
