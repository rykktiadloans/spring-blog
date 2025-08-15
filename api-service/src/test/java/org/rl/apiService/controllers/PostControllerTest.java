package org.rl.apiService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rl.apiService.controllers.api.v1.PostController;
import org.rl.apiService.model.Post;
import org.rl.apiService.model.api_dto.requests.PostRequest;
import org.rl.apiService.services.PostService;
import org.rl.apiService.utils.Comparators;
import org.rl.shared.exceptions.MissingEntityException;
import org.rl.shared.model.PostState;
import org.rl.shared.model.responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.rl.apiService.utils.ExamplePosts.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc()
@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @Test
    public void canCreatePost() throws Exception {
        when(this.postService.save(ArgumentMatchers.any(Post.class)))
                .thenAnswer(invocationOnMock -> {
                    Post post = invocationOnMock.getArgument(0, Post.class);
                    post.setId(5);
                    return post;
                });

        Post post = buildExamplePost1();
        PostRequest postRequest = new PostRequest(null, post.getTitle(), post.getContent(),
                post.getSummary(), post.getState());

        ResultActions resultActions = this.mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(postRequest)));

        String content = resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        PostResponse postResponse = this.objectMapper.readValue(content, PostResponse.class);

        assertThat(postResponse)
                .usingRecursiveComparison()
                .ignoringFields(PostResponse.Fields.id)
                .withEqualsForFields(Comparators.areEqual(), PostResponse.Fields.creationDate)
                .isEqualTo(post.toResponse());
    }

    @Test
    public void errorIfAnEmptyField() throws Exception {
        when(this.postService.save(ArgumentMatchers.any(Post.class)))
                .thenAnswer(invocationOnMock -> {
                    Post post = invocationOnMock.getArgument(0, Post.class);
                    post.setId(5);
                    return post;
                });

        Post post = buildExamplePost1();
        PostRequest postRequest = new PostRequest(null, null, "",
                post.getSummary(), post.getState());

        ResultActions resultActions = this.mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(postRequest)));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void canUpdatePost() throws Exception {
        when(this.postService.save(ArgumentMatchers.any(Post.class)))
                .thenAnswer(invocationOnMock -> {
                    Post post = invocationOnMock.getArgument(0, Post.class);
                    post.setId(1);
                    return post;
                });

        when(this.postService.getById(ArgumentMatchers.any(Integer.class)))
                .thenAnswer(invocationOnMock ->
                        switch (invocationOnMock.getArgument(0, Integer.class)) {
                            case 1 -> {
                                Post post = buildExamplePost1();
                                post.setId(1);
                                yield post;
                            }
                            default -> throw new MissingEntityException();
                        }
                );

        Post post = buildExamplePost1();
        post.setTitle("New Title");
        post.setContent("New content");
        PostRequest postRequest = new PostRequest(1, post.getTitle(), post.getContent(),
                post.getSummary(), post.getState());

        ResultActions resultActions = this.mockMvc.perform(put("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(postRequest)));

        String content = resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        PostResponse postResponse = this.objectMapper.readValue(content, PostResponse.class);

        assertThat(postResponse)
                .usingRecursiveComparison()
                .ignoringFields(PostResponse.Fields.id)
                .withEqualsForFields(Comparators.areEqual(), PostResponse.Fields.creationDate)
                .isEqualTo(post.toResponse());
    }

    @Test
    public void willErrorIfTheRequestLacksId() throws Exception {
        when(this.postService.save(ArgumentMatchers.any(Post.class)))
                .thenAnswer(invocationOnMock -> {
                    Post post = invocationOnMock.getArgument(0, Post.class);
                    post.setId(1);
                    return post;
                });

        when(this.postService.getById(ArgumentMatchers.any(Integer.class)))
                .thenAnswer(invocationOnMock ->
                        switch (invocationOnMock.getArgument(0, Integer.class)) {
                            case 1 -> {
                                Post post = buildExamplePost1();
                                post.setId(1);
                                yield post;
                            }
                            default -> throw new MissingEntityException();
                        }
                );

        Post post = buildExamplePost1();
        post.setTitle("New Title");
        post.setContent("New content");
        PostRequest postRequest = new PostRequest(null, post.getTitle(), post.getContent(),
                post.getSummary(), post.getState());

        ResultActions resultActions = this.mockMvc.perform(put("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(postRequest)));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void willErrorIfTheIdCannotBeFound() throws Exception {
        when(this.postService.save(ArgumentMatchers.any(Post.class)))
                .thenAnswer(invocationOnMock -> {
                    Post post = invocationOnMock.getArgument(0, Post.class);
                    post.setId(1);
                    return post;
                });

        when(this.postService.getById(ArgumentMatchers.any(Integer.class)))
                .thenAnswer(invocationOnMock ->
                        switch (invocationOnMock.getArgument(0, Integer.class)) {
                            case 1 -> {
                                Post post = buildExamplePost1();
                                post.setId(1);
                                yield post;
                            }
                            default -> throw new MissingEntityException();
                        }
                );

        Post post = buildExamplePost1();
        post.setTitle("New Title");
        post.setContent("New content");
        PostRequest postRequest = new PostRequest(2, post.getTitle(), post.getContent(),
                post.getSummary(), post.getState());

        ResultActions resultActions = this.mockMvc.perform(put("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(postRequest)));

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void canGetPublishedPost() throws Exception {
        when(this.postService.getByIdWhenPublished(ArgumentMatchers.any(Integer.class)))
                .thenAnswer(invocationOnMock ->
                    switch (invocationOnMock.getArgument(0, Integer.class)) {
                        case 1 -> {
                            Post post = buildExamplePost1();
                            post.setId(1);
                            yield post;
                        }
                        case 2 -> throw new MissingEntityException(); // Assume that buildExamplePost2() is here, which is drafted
                        case 3 -> {
                            Post post = buildExamplePost3();
                            post.setId(3);
                            yield post;
                        }
                        default -> throw new MissingEntityException();
                    }
                );

        ResultActions resultActions = this.mockMvc.perform(get("/api/v1/posts/1"));

        String content = resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        PostResponse postResponse = this.objectMapper.readValue(content, PostResponse.class);

        assertThat(postResponse)
                .usingRecursiveComparison()
                .ignoringFields(PostResponse.Fields.id)
                .withEqualsForFields(Comparators.areEqual(), PostResponse.Fields.creationDate)
                .isEqualTo(buildExamplePost1().toResponse());
    }
    @Test
    public void willErrorIfItTriesToGetADraftedPostFromDefaultPath() throws Exception {
        when(this.postService.getByIdWhenPublished(ArgumentMatchers.any(Integer.class)))
                .thenAnswer(invocationOnMock ->
                        switch (invocationOnMock.getArgument(0, Integer.class)) {
                            case 1 -> {
                                Post post = buildExamplePost1();
                                post.setId(1);
                                yield post;
                            }
                            case 2 -> throw new MissingEntityException(); // Assume that buildExamplePost2() is here, which is drafted
                            case 3 -> {
                                Post post = buildExamplePost3();
                                post.setId(3);
                                yield post;
                            }
                            default -> throw new MissingEntityException();
                        }
                );

        ResultActions resultActions = this.mockMvc.perform(get("/api/v1/posts/2"));

        String content = resultActions.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void canGetAnyPost() throws Exception {
        when(this.postService.getById(ArgumentMatchers.any(Integer.class)))
                .thenAnswer(invocationOnMock ->
                        switch (invocationOnMock.getArgument(0, Integer.class)) {
                            case 1 -> {
                                Post post = buildExamplePost1();
                                post.setId(1);
                                yield post;
                            }
                            case 2 -> {
                                Post post = buildExamplePost2();
                                post.setId(2);
                                yield post;
                            }
                            case 3 -> {
                                Post post = buildExamplePost3();
                                post.setId(3);
                                yield post;
                            }
                            default -> throw new MissingEntityException();
                        }
                );

        ResultActions resultActions = this.mockMvc.perform(post("/api/v1/posts/anystate/2"));

        String content = resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        PostResponse postResponse = this.objectMapper.readValue(content, PostResponse.class);

        assertThat(postResponse)
                .usingRecursiveComparison()
                .ignoringFields(PostResponse.Fields.id)
                .withEqualsForFields(Comparators.areEqual(), PostResponse.Fields.creationDate)
                .isEqualTo(buildExamplePost2().toResponse());
    }

    @Test
    public void canGetAPageOfPublishedPosts() throws Exception {
        when(this.postService.getByPageWhereState(ArgumentMatchers.any(PostState.class), ArgumentMatchers.any(Pageable.class)))
                .thenAnswer(invocationOnMock -> {
                    PostState postState = invocationOnMock.getArgument(0, PostState.class);
                    Pageable pageable = invocationOnMock.getArgument(1, Pageable.class);
                    List<Post> resp = List.of(buildExamplePost1(), buildExamplePost2(), buildExamplePost3()).stream()
                            .filter(post -> post.getState() == PostState.PUBLISHED)
                            .sorted(Comparator.comparing(Post::getCreationDate).reversed())
                            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                            .limit(pageable.getPageSize())
                            .toList();
                    return resp;
                });

        ResultActions resultActions = this.mockMvc.perform(get("/api/v1/posts"));
        String content = resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<PostResponse> fetched = Arrays.asList(this.objectMapper.readValue(content, Post[].class))
                .stream().map(Post::toResponse).toList();

        assertThat(fetched)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Post.Fields.resources)
                .containsExactly(buildExamplePost3().toResponse(), buildExamplePost1().toResponse());
    }

    @Test
    public void canGetAPageOfAnyPosts() throws Exception {
        when(this.postService.getByPageWhereState(ArgumentMatchers.any(PostState.class), ArgumentMatchers.any(Pageable.class)))
                .thenAnswer(invocationOnMock -> {
                    PostState postState = invocationOnMock.getArgument(0, PostState.class);
                    Pageable pageable = invocationOnMock.getArgument(1, Pageable.class);
                    List<Post> resp = List.of(buildExamplePost1(), buildExamplePost2(), buildExamplePost3()).stream()
                            .sorted(Comparator.comparing(Post::getCreationDate).reversed())
                            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                            .limit(pageable.getPageSize())
                            .toList();
                    return resp;
                });

        ResultActions resultActions = this.mockMvc.perform(get("/api/v1/posts"));
        String content = resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<PostResponse> fetched = Arrays.asList(this.objectMapper.readValue(content, Post[].class))
                .stream().map(Post::toResponse).toList();

        assertThat(fetched)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Post.Fields.resources)
                .containsExactly(buildExamplePost3().toResponse(), buildExamplePost2().toResponse(), buildExamplePost1().toResponse());
    }
}
