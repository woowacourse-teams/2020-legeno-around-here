package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.domain.Image;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        Long postId = postService.createPost(postRequest).getId();
        return ResponseEntity
            .created(URI.create("/posts/" + postId))
            .build();
    }

    @PostMapping("/image")
    public ResponseEntity<PostResponse> createPostWithImage(PostRequest postRequest) {
        PostResponse postResponse = new PostResponse(1L, "글을 등록합니다.",
            Arrays.asList(new Image(), new Image()));

        return ResponseEntity
            .ok()
            .body(postResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findPost(@PathVariable Long id) {
        PostResponse post = postService.findPost(id);
        return ResponseEntity
            .ok()
            .body(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id,
        @RequestBody PostRequest postRequest) {
        postService.updatePost(id, postRequest);
        return ResponseEntity
            .ok()
            .build();
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAllPost() {
        List<PostResponse> posts = postService.findAllPost();
        return ResponseEntity
            .ok()
            .body(posts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity
            .noContent()
            .build();
    }
}
