package com.example.firstproject.api;

import com.example.firstproject.dto.CommentDTO;
import com.example.firstproject.entity.Comment;
import com.example.firstproject.repository.CommentRepository;
import com.example.firstproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentApiController {
    private final CommentService commentService;

    // 1. 댓글 조회
    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<List<CommentDTO>> comments(@PathVariable Long articleId) {
        // 서비스에 위임
        List<CommentDTO> dtos = commentService.comments(articleId);
        // 결과 응답

        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 2. 댓글 생성
    @PostMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentDTO> create(@PathVariable Long articleId,
                                             @RequestBody CommentDTO dto) {
        // 서비스에 위임
        CommentDTO createdDTO = commentService.create(articleId, dto);

        // 결과 응답

        return ResponseEntity.status(HttpStatus.OK).body(createdDTO);
    }

    // 3. 댓글 수정
    @PatchMapping("/api/comments/{id}")
    public ResponseEntity<CommentDTO> update(@PathVariable Long id,
                                             @RequestBody CommentDTO dto) {
        // 서비스에 위임
        CommentDTO updateDTO = commentService.update(id, dto);

        // 결과 응답

        return ResponseEntity.status(HttpStatus.OK).body(updateDTO);
    }

    // 4. 댓글 삭제
}
