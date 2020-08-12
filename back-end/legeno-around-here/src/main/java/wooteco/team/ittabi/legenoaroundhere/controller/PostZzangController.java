package wooteco.team.ittabi.legenoaroundhere.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.team.ittabi.legenoaroundhere.dto.PostZzangResponse;
import wooteco.team.ittabi.legenoaroundhere.service.PostZzangService;

@RestController
@RequestMapping("/posts/{postId}/likes")
@AllArgsConstructor
public class PostZzangController {

    private final PostZzangService postZzangService;

    @GetMapping
    public ResponseEntity<PostZzangResponse> pressPostZzang(@PathVariable Long postId) {
        PostZzangResponse postZzangResponse = postZzangService.pressPostZzang(postId);
        return ResponseEntity
            .ok()
            .body(postZzangResponse);
    }
}
