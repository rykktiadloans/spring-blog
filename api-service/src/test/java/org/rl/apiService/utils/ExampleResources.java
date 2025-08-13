package org.rl.apiService.utils;

import org.rl.apiService.model.Resource;

import java.util.HashSet;

public class ExampleResources {

    public static Resource buildExampleResource1() {
        return Resource.builder()
                .name("name.png")
                .posts(new HashSet<>())
                .build();
    }
}
