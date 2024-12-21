package org.vsa.server.question.repository;

import org.vsa.server.question.entity.Comment;
import org.vsa.server.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    //commentcount 리턴
    int countByQuestion_QuestionId(int questionId);
    List<Comment> findAllByQuestion_QuestionId(int questionId);

    Boolean existsByQuestionAndCommentId(Question question, Long commentId);

    void deleteCommentByQuestion_QuestionIdAndCommentId(Integer question, Integer commentId);

    Optional<Comment> findByCommentId(Integer commentId);
}
