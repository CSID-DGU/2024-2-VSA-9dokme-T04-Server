package org.vsa.server.question.service;

import org.vsa.server.book.repository.BookRepository;
import org.vsa.server.member.repository.MemberRepository;
import org.vsa.server.question.dto.request.CreateCommentDto;
import org.vsa.server.question.dto.request.CreateQuestionDto;
import org.vsa.server.question.dto.response.CommentDto;
import org.vsa.server.question.dto.response.QuestionDetailDto;
import org.vsa.server.question.dto.response.QuestionDto;
import org.vsa.server.question.dto.response.QuestionListDto;
import org.vsa.server.question.entity.Comment;
import org.vsa.server.question.entity.Question;
import org.vsa.server.question.repository.CommentRepository;
import org.vsa.server.question.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
@NoArgsConstructor
@Service
public class QuestionService{

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BookRepository bookRepository;

    public QuestionListDto getQuestionList(Long bookId, Integer chapter, Integer bookPage){
        List<Question> questions;

        if (chapter != null && bookPage != null) {
            questions = questionRepository.findAllByBook_BookIdAndChapterAndBookPage(bookId, chapter, bookPage);
        } else if (chapter != null) {
            questions = questionRepository.findAllByBook_BookIdAndChapter(bookId, chapter);
        } else if (bookPage != null) {
            questions = questionRepository.findAllByBook_BookIdAndBookPage(bookId, bookPage);
        } else {
            questions = questionRepository.findAllByBook_BookId(bookId);
        }

        List<QuestionDto> questionDtoList = questions.stream()
                .map(question -> QuestionDto.builder()
                        .questionId(question.getQuestionId())
                        .title(question.getTitle())
                        .content(question.getContent())
                        .chapter(question.getChapter())
                        .bookPage(question.getBookPage())
                        .commentCount(commentRepository.countByQuestion_QuestionId(question.getQuestionId()))
                        .createdAt(question.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return QuestionListDto.builder()
                .questionList(questionDtoList)
                .build();
    }

    public QuestionDetailDto getQuestionDetail(int questionId){
        Question question = questionRepository.findByQuestionId(questionId);

        if (question == null) {
            throw new RuntimeException("Question not found with ID: " + questionId);
        }

        QuestionDto questionDto = QuestionDto.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .content(question.getContent())
                .commentCount(commentRepository.countByQuestion_QuestionId(question.getQuestionId()))
                .createdAt(question.getCreatedAt())
                .build();

        List<Comment> commentList = commentRepository.findAllByQuestion_QuestionId(questionId);

        List<CommentDto> commentDtoList = commentList.stream()
                .map(comment -> CommentDto.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .nickName(comment.getNickName())
                        .memberId(memberRepository.findBySocialId(comment.getEmail()).getMemberId())
                        .build())
                .collect(Collectors.toList());

        return new QuestionDetailDto(questionDto, commentDtoList);
    }

    @Transactional
    public void createQuestion(Long bookId, CreateQuestionDto dto, Long memberId){


        Question question = Question.builder()
                .book(bookRepository.findByBookId(bookId))
                .nickName(memberRepository.findByMemberId(memberId).getNickName())
                .email(memberRepository.findByMemberId(memberId).getSocialId())
                .chapter(dto.getBookChapter())
                .bookPage(dto.getBookPage())
                .title(dto.getTitle())
                .content(dto.getContent()).build();

        questionRepository.save(question);
    }


    @Transactional
    public void createComment(Integer questionId , Long memberId, CreateCommentDto dto){

        Comment comment = new Comment();

        // Optional<Question>을 처리합니다.
        Question question = questionRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        comment.setQuestion(question);
        comment.setContent(dto.getContent());
        comment.setNickName(memberRepository.findByMemberId(memberId).getNickName());
        comment.setEmail(memberRepository.findByMemberId(memberId).getSocialId());

        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Integer questionId, Integer commentId,String email){


        String authorizedDelete = commentRepository.findByCommentId(commentId).get().getEmail();

        if(authorizedDelete.equals(email)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"삭제 권한이 없습니다");
        }


        commentRepository.deleteCommentByQuestion_QuestionIdAndCommentId(questionId,commentId);
    }
}
