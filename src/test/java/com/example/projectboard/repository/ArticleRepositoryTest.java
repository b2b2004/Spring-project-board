package com.example.projectboard.repository;

import com.example.projectboard.config.JpaConfig;
import com.example.projectboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class ArticleRepositoryTest {

    private ArticleRepository articleRepository;
    private ArticleCommentRepository articleCommentRepository;

    public ArticleRepositoryTest(@Autowired ArticleRepository articleRepository,
                                 @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select test")
    @Test
    void article_select_test(){
        // Given
        // When
        List<Article> articles = articleRepository.findAll();
        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert test")
    @Test
    void article_insert_test(){
        // Given
        long previousCount = articleRepository.count();

        // When
        Article savedArticle = articleRepository.save(Article.of("new article", "new content","#spring"));

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("update test")
    @Test
    void article_update_test(){
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        // When
        // 해당 테스트들은 Transactional이 걸려 있기 때문에 다 Rollback 되기 때문에 update는 flush까지 진행 해줘야 한다.
        Article savedArticle = articleRepository.saveAndFlush(article);

        // Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue( "hashtag", updatedHashtag);
    }

    @DisplayName("delete test")
    @Test
    void article_delete_test(){
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count(); // 게시글 수
        long previousArticleCommentcount = articleCommentRepository.count(); // 댓글 개수
        int deletedCommentsSize = article.getArticleComments().size(); // 해당 게시글의 댓글 개수

        // When
        articleRepository.delete(article);

        // Then
        assertThat(articleRepository.count()).isEqualTo( previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo( previousArticleCommentcount - deletedCommentsSize);
    }
}