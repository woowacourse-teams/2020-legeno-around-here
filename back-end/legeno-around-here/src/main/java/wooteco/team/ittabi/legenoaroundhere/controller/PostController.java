package wooteco.team.ittabi.legenoaroundhere.controller;

import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.PageRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PageableAssembler;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostSearchRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.service.PostService;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(PostCreateRequest postCreateRequest) {
        Long postId = postService.createPost(postCreateRequest).getId();

        return ResponseEntity
            .created(URI.create("/posts/" + postId))
            .build();
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
        @RequestBody PostUpdateRequest postUpdateRequest) {
        postService.updatePost(id, postUpdateRequest);

        return ResponseEntity
            .ok()
            .build();
    }

    @GetMapping
    public ResponseEntity<Page<PostWithCommentsCountResponse>> searchAllPost(
        PageRequest pageRequest, PostSearchRequest postSearchRequest) {
        Page<PostWithCommentsCountResponse> posts
            = postService.searchAllPost(PageableAssembler.assemble(pageRequest), postSearchRequest);

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

    @PostMapping("/{postId}/zzangs")
    public ResponseEntity<Void> pressPostZzang(@PathVariable Long postId) {
        postService.pressZzang(postId);

        return ResponseEntity
            .noContent()
            .build();
    }

    @PostMapping("/{postId}/report")
    public ResponseEntity<Void> createPostReport(@PathVariable Long postId) {

        return ResponseEntity
            .created(URI.create("/posts/" + postId))
            .build();
    }
}
