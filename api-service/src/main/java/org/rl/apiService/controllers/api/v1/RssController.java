package org.rl.apiService.controllers.api.v1;

import com.rometools.rome.feed.rss.Channel;
import org.rl.apiService.services.RssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

/**
 * A controller responsible for serving RSS feeds.
 */
@RestController
@RequestMapping(path = "/api/v1/rss")
public class RssController {
    @Autowired
    private RssService rssService;

    /**
     * Return the RSS feed
     * @return RSS feed
     */
    @GetMapping("")
    public Channel getFeed() {
        return this.rssService.getView();
    }
}
