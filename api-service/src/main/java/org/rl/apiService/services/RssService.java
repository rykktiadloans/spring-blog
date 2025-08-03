package org.rl.apiService.services;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Item;
import lombok.val;
import org.rl.apiService.model.Post;
import org.rl.shared.model.PostState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class RssService {

    @Autowired
    private PostService postService;

    public Channel getView() {
        List<Post> posts = this.postService.getByPageWhereState(PostState.PUBLISHED, Pageable.ofSize(20));

        val channel = new Channel();
        channel.setFeedType("rss_2.0");
        channel.setTitle("rykktiadLoans Feed");
        channel.setDescription("RSS feed for a rykktiadLoans blog");
        channel.setLink("");

        Date pubDate = posts.isEmpty() ? new Date() : Date.from(posts.get(0).getCreationDate().atZone(ZoneId.systemDefault()).toInstant());
        channel.setPubDate(pubDate);

        List<Item> postItems = posts.stream()
                .map(post -> {
                    Item item = new Item();
                    item.setAuthor("rykktiadLoans");
                    item.setLink("/posts/%d".formatted(post.getId()));
                    item.setTitle(post.getTitle());
                    item.setPubDate(Date.from(post.getCreationDate().atZone(ZoneId.systemDefault()).toInstant()));
                    return item;
                })
                .toList();


        channel.setItems(postItems);
        //Like more Entries here about different new topics
        return channel;

    }
}
