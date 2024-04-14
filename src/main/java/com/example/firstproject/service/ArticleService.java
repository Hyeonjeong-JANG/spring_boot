package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();
        if (article.getId() != null) {
            return null;
        }
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {
        // 1. DTO -> 엔티티 변환하기
        Article article = dto.toEntity();
        log.info("id: {}, article: {}", id, article.toString());

        // 2. 타깃 조회하기
        Article target = articleRepository.findById(id)
                .orElse(null);

        // 3. 잘못된 요청 처리하기
        if (target == null || id != article.getId()) {
            log.info("잘못된 요청! id: {}, article: {}", id, article.toString());
            return null;
        }

        // 4. 업데이트 및 정상 응답(200)하기
        target.patch(article);
        Article updated = articleRepository.save(target);
        return updated;
    }

    public Article delete(Long id) {
        // 1. DB에서 대상 엔티티가 있는지 조회하기
        Article target = articleRepository.findById(id)
                .orElse(null);

        // 2. 대상 엔티티가 없어서 요청 자체가 잘못됐을 경우 처리하기
        if (target == null) {
            return null;
        }

        // 3. 대상 엔티티가 있으면 삭제하고 정상 응답(200) 반환하기
        articleRepository.delete(target);
        log.info(target.toString());
        return target;
        /**
         * delete 메소드가 호출되어 엔티티가 데이터베이스에서 삭제되더라도, 
         * target 변수가 가리키는 객체 자체는 메모리상에 그대로 유지되고, 
         * 이 메소드를 호출한 측에서는 반환된 target 객체를 통해 삭제되었던 엔티티의 정보를 확인할 수 있다.
         *
         * 결론적으로, 삭제가 성공적으로 이루어졌다면, 
         * target은 null이 아닌 삭제된 엔티티의 정보를 담고 있는 객체를 가리킨다. 
         * 이는 삭제 작업이 성공했음을 나타내는 동시에, 
         * 삭제된 엔티티에 대한 정보를 사용할 수 있게 한다(예를 들어, 로깅이나 추가적인 처리를 위해).
         */
    }

    @Transactional
    public List<Article> createArticles(List<ArticleForm> dtos) {
        // 1. dto 묶음을 엔티티 묶음으로 변환하기
        List<Article> articleList = dtos.stream()
                .map(dto -> dto.toEntity())
                .toList();

        // 2. 엔티티 묶음을 DB에 저장하기
        articleList.stream()
                .forEach(article -> articleRepository.save(article));

        // 3. 강제 예외 발생시키기
        articleRepository.findById(-1L)
                .orElseThrow(() -> new IllegalArgumentException("결제 실패"));

        // 4. 결과 값 반환하기

        return articleList;
    }
}
