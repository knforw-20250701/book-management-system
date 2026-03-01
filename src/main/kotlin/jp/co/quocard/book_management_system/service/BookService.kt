package jp.co.quocard.book_management_system.service

import jp.co.quocard.book_management_system.domain.Book
import jp.co.quocard.book_management_system.domain.BookStatus
import jp.co.quocard.book_management_system.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository) {

    fun getAllBooks(): List<Book> = bookRepository.findAll()

    fun getBookById(id: Long): Book? = bookRepository.findById(id)

    fun getBooksByAuthorId(authorId: Long): List<Book> = bookRepository.findByAuthorId(authorId)

    fun createBook(book: Book): Book {
        validateBook(book)
        return bookRepository.save(book)
    }

    fun updateBook(book: Book) {
        val existingBook = bookRepository.findById(book.id!!) 
            ?: throw IllegalArgumentException("書籍が見つかりません。 ID: ${book.id}")
        
        validateBook(book)
        
        // 出版済みステータスのものを未出版には変更できない
        if (existingBook.status == BookStatus.PUBLISHED && book.status == BookStatus.PRE_PUBLISHED) {
            throw IllegalArgumentException("出版済みステータスのものを未出版には変更できません。")
        }
        
        bookRepository.update(book)
    }

    private fun validateBook(book: Book) {
        if (book.price < 0) {
            throw IllegalArgumentException("価格は0以上である必要があります。")
        }
        if (book.authors.isEmpty()) {
            throw IllegalArgumentException("書籍は最低1人の著者を持つ必要があります。")
        }
    }
}
