package jp.co.quocard.book_management_system.controller

import jp.co.quocard.book_management_system.domain.Book
import jp.co.quocard.book_management_system.service.BookService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class BookController(private val bookService: BookService) {

    @GetMapping("/books")
    fun getAllBooks(): List<Book> = bookService.getAllBooks()

    @GetMapping("/books/{id}")
    fun getBookById(@PathVariable id: Long): Book? = bookService.getBookById(id)

    @GetMapping("/authors/{authorId}/books")
    fun getBooksByAuthorId(@PathVariable authorId: Long): List<Book> = bookService.getBooksByAuthorId(authorId)

    @PostMapping("/books")
    fun createBook(@RequestBody book: Book): Book = bookService.createBook(book)

    @PutMapping("/books/{id}")
    fun updateBook(@PathVariable id: Long, @RequestBody book: Book) {
        bookService.updateBook(book.copy(id = id))
    }
}
