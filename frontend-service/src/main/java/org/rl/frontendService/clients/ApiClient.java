package org.rl.frontendService.clients;

import org.rl.shared.model.responses.PostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "api-service", url = "${services.api}")
public interface ApiClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/posts/{id}")
    PostResponse getPost(@PathVariable(name = "id") Integer id);
}
