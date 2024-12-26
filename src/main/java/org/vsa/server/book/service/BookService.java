package org.vsa.server.book.service;

import org.vsa.server.Notification.entity.Notification;
import org.vsa.server.Notification.repository.NotificationRepository;
import org.vsa.server.book.dto.request.BookCreateRequest;
import org.vsa.server.book.dto.request.BookUpdateRequest;
import org.vsa.server.book.dto.response.*;
import org.vsa.server.book.entity.Book;
import org.vsa.server.book.exception.BookException;
import org.vsa.server.book.message.ErrorMessage;
import org.vsa.server.book.repository.BookRepository;
import org.vsa.server.bookmark.repository.BookmarkRepository;
import org.vsa.server.member.dto.response.BookDto;
import org.vsa.server.member.entity.Keyword;
import org.vsa.server.member.entity.Member;
import org.vsa.server.member.repository.KeywordRepository;
import org.vsa.server.member.repository.MemberRepository;
import org.vsa.server.rent.entity.Rent;
import org.vsa.server.rent.repository.RentRepository;
import org.vsa.server.subscribe.repository.SubscribeRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@NoArgsConstructor
@Slf4j
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private JavaMailSender emailSender;

    @Value("${google.email}")
    private String FROM_ADDRESS;

    public BookCheckDto checkBook(Long bookId,Long memberId){

        Book book = bookRepository.findByBookId(bookId);
        int lastPage;
        if(rentRepository.existsByBookIdAndMemberId(bookId,memberId)){
            lastPage = rentRepository.findByBookIdAndMemberId(bookId,memberId).getLastPage();
        } else {
            lastPage = 1;
        }

        BookCheckDto dto = BookCheckDto.builder().
                bookId(book.getBookId()).
                title(book.getTitle()).
                author(book.getAuthor()).
                description(book.getDescription()).
                publisher(book.getPublisher()).
                category(book.getCategory()).
                pdfImage(book.getBookImage()).
                lastPage(lastPage).
                category(book.getCategory()).
                isMarked(bookmarkRepository.existsBookmarkByBook_BookIdAndMember_MemberId(book.getBookId(),memberId)).build();
        return dto;
    }

    public Page<BookDto> searchBook(String title, int pageNo, Long memberId){

        Member member = memberRepository.findByMemberId(memberId);

        Pageable pageable = PageRequest.of(pageNo,8);
        Page<Book> bookPage;

        if(title.equals("")){
            bookPage = bookRepository.findAll(pageable);
        }else{
            bookPage = bookRepository.findByTitleContaining(title, pageable);
        }

        Page<BookDto> bookDtoPage = bookPage.map(book -> new BookDto(
                book.getBookId(),
                book.getTitle(),
                book.getCategory(),
                book.getBookURL(),
                book.getBookImage(),
                bookmarkRepository.existsBookmarkByBook_BookIdAndMember_MemberId(book.getBookId(),member.getMemberId())));



        return bookDtoPage;
    }


    public BookWebViewDto bookWebView(Long bookId){
        Book book = bookRepository.findByBookId(bookId);


        return BookWebViewDto.builder().
                title(book.getTitle()).
                category(book.getCategory()).
                author(book.getAuthor()).
                pdfUrl(book.getBookURL()).
                build();
    }


    public void saveRentBook(Long bookId, String socialId){


        Member member = memberRepository.findBySocialId(socialId);


        if(!rentRepository.existsByBookIdAndMemberId(bookId,member.getMemberId())){
            Rent initRent = new Rent();
            initRent.setBookId(bookId);
            initRent.setProgress(Float.valueOf(0L));
            initRent.setLastPage(0);
            initRent.setRentDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            initRent.setMemberId(member.getMemberId());
            initRent.setReadAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

            rentRepository.save(initRent);
        }else{
            Rent updateRent = rentRepository.findByBookIdAndMemberId(bookId, member.getMemberId());
            updateRent.setReadAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

            rentRepository.save(updateRent);
        }

    }

    public void updateProgress(Long bookId, Long memberId, int lastPage){

        Book book = bookRepository.findByBookId(bookId);

        Rent updateRent = rentRepository.findByBookIdAndMemberId(bookId,memberId);

        int fullPage = book.getBookFullPage();

        float progress = ((float)lastPage /fullPage) * 100;

        updateRent.setProgress(progress);
        updateRent.setLastPage(lastPage);

        rentRepository.save(updateRent);
    }

    public Optional<Book> findById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    @Transactional
    public void updateBookFilePaths(Long bookId, String imagePath, String pdfPath) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException(ErrorMessage.NOT_FOUND_BOOK));

        book.update(
                book.getTitle(),
                book.getPublishDate(),
                book.getAuthor(),
                book.getPublisher(),
                book.getCategory(),
                book.getDescription(),
                imagePath,
                pdfPath,
                book.getBookChapter(),
                book.getBookFullPage(),
                book.getRent()
        );

        bookRepository.save(book);
    }

    @Transactional
    public BookInfoResponse createBook(BookCreateRequest request) throws MessagingException {
        Book book = Book.create(
                request.title(),
                request.publishDate(),
                request.author(),
                request.publisher(),
                request.category(),
                request.description(),
                request.bookImage(),
                request.bookURL(),
                request.bookChapter(),
                request.bookFullPage(),
                request.rent()
        );
        bookRepository.save(book);



        // 키워드로 관련 멤버 검색
        List<Keyword> matchingKeywords = keywordRepository.findMatchingKeywords(book.getTitle());

        if(matchingKeywords.isEmpty()){
            return toResponse(book);
        }

        List<Member> matchedMembers = matchingKeywords.stream()
                .map(Keyword::getMember)
                .collect(Collectors.toList());

        // 관련 멤버들의 socialId 가져오기
        List<String> socialIds = matchingKeywords.stream()
                .map(keyword -> keyword.getMember().getSocialId())
                .collect(Collectors.toList());

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        helper.setFrom(FROM_ADDRESS);
        helper.setBcc(socialIds.toArray(new String[0]));
        helper.setSubject("안녕하세요 9dokme입니다. 새로운 책이 입고 됐습니다! 사이트에 방문해 주셔서 확인해주세요!");
        helper.setText("저번에 검색하신 책 " + book.getTitle() + "이 입고 되었습니다. 확인해주세요");
        emailSender.send(mimeMessage);

        for (Member member : matchedMembers) {
            Notification notification = new Notification();
            notification.setType("BOOK"); // 알림 타입
            notification.setParamId(book.getBookId()); // 관련된 책 ID
            notification.setExpiredDate(LocalDateTime.now().plusDays(7)); // 7일 후 만료
            notification.setMessage("저번에 검색하신 책 '" + book.getTitle() + "'이 입고되었습니다. 확인해주세요!");
            notification.setIsRead(false); // 읽지 않음 상태로 저장
            notification.setMember(member); // 알림 받을 사용자

            notificationRepository.save(notification);
        }
        return toResponse(book);
    }

    @Transactional
    public BookInfoResponse updateBook(Long bookId, BookUpdateRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException(ErrorMessage.NOT_FOUND_BOOK));

        book.update(
                request.title(),
                request.publishDate(),
                request.author(),
                request.publisher(),
                request.category(),
                request.description(),
                request.bookImage(),
                request.bookURL(),
                request.bookChapter(),
                request.bookFullPage(),
                request.rent()
        );

        bookRepository.save(book);
        return toResponse(book);
    }

    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException(ErrorMessage.NOT_FOUND_BOOK));
        bookRepository.delete(book);
    }

    private BookInfoResponse toResponse(Book book) {
        return new BookInfoResponse(
                book.getBookId(),
                book.getTitle(),
                book.getPublishDate(),
                book.getAuthor(),
                book.getPublisher(),
                book.getCategory(),
                book.getDescription(),
                book.getBookImage(),
                book.getBookURL(),
                book.getBookChapter(),
                book.getBookFullPage(),
                book.getRent()
        );
    }


//    public MyPageDto getMypageBookList(Long memberId, int pageNo) {
//        Pageable pageable = PageRequest.of(pageNo, 8); // 페이지 번호와 크기 설정
//
//        // 회원 조회
//        Member member = memberRepository.findByMemberId(memberId);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        // 프로필 정보 생성
//        ProfileDto profileDto = new ProfileDto(
//                member.getMemberId(),
//                member.getNickName(),
//                member.getSubscribe().getExpiredAt().format(formatter)
//        );
//
//        // 북마크된 책들 페이지 처리하여 가져오기
//        Page<Book> page = bookRepository.findBooksByMember(memberId, pageable);
//
//        // BookDto로 변환
//        Page<BookDto> bookDtoPage = page.map(book -> new BookDto(
//                book.getBookId(),
//                book.getTitle(),
//                book.getCategory(),
//                book.getBookURL(),
//                book.getBookImage(),
//                bookmarkRepository.existsBookmarkByBook_BookIdAndMember_MemberId(book.getBookId(), memberId)));
//
//        // MyPageDto 반환
//        return new MyPageDto(profileDto, bookDtoPage);
//    }

    public MyPageDto getMypageBookList(Long memberId, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 8); // 페이지 번호와 크기 설정

        // 회원 조회
        Member member = memberRepository.findByMemberId(memberId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 구독 여부 확인 및 프로필 정보 생성
        boolean isSubscribed = member.getSubscribe() != null;
        String expirationDate = isSubscribed ? member.getSubscribe().getExpiredAt().format(formatter) : null;

        ProfileDto profileDto = new ProfileDto(
                member.getMemberId(),
                member.getNickName(),
                expirationDate,
                isSubscribed
        );

        // 북마크된 책들 페이지 처리하여 가져오기
        Page<Book> page = bookRepository.findBooksByMember(memberId, pageable);

        // BookDto로 변환
        Page<BookDto> bookDtoPage = page.map(book -> new BookDto(
                book.getBookId(),
                book.getTitle(),
                book.getCategory(),
                book.getBookURL(),
                book.getBookImage(),
                bookmarkRepository.existsBookmarkByBook_BookIdAndMember_MemberId(book.getBookId(), memberId)));

        // MyPageDto 반환
        return new MyPageDto(profileDto, bookDtoPage);
    }


}