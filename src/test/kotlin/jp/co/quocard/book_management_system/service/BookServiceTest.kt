package jp.co.quocard.book_management_system.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jp.co.quocard.book_management_system.domain.Author
import jp.co.quocard.book_management_system.domain.Book
import jp.co.quocard.book_management_system.domain.BookStatus
import jp.co.quocard.book_management_system.repository.BookRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.test.assertEquals

class BookServiceTest {
    private val bookRepository = mockk<BookRepository>()
    private val bookService = BookService(bookRepository)

    private val sampleAuthor = Author(id = 1L, name = "Author", birthDate = LocalDate.of(1990, 1, 1))

    @Test
    fun `createBook should throw exception when price is negative`() {
        val book = Book(title = "Negative Price", price = -1, status = BookStatus.PRE_PUBLISHED, authors = listOf(sampleAuthor))

        val exception = assertThrows<IllegalArgumentException> {
            bookService.createBook(book)
        }
        assertEquals("価格は0以上である必要があります。", exception.message)
    }

    @Test
    fun `createBook should throw exception when no authors provided`() {
        val book = Book(title = "No Author", price = 1000, status = BookStatus.PRE_PUBLISHED, authors = emptyList())

        val exception = assertThrows<IllegalArgumentException> {
            bookService.createBook(book)
        }
        assertEquals("書籍は最低1人の著者を持つ必要があります。", exception.message)
    }

    @Test
    fun `updateBook should throw exception when transition from PUBLISHED to PRE_PUBLISHED`() {
        val existingBook = Book(id = 1L, title = "Old", price = 1000, status = BookStatus.PUBLISHED, authors = listOf(sampleAuthor))
        val newBook = existingBook.copy(status = BookStatus.PRE_PUBLISHED)
        
        every { bookRepository.findById(1L) } returns existingBook

        val exception = assertThrows<IllegalArgumentException> {
            bookService.updateBook(newBook)
        }
        assertEquals("出版済みステータスのものを未出版には変更できません。", exception.message)
    }

    @Test
    fun `updateBook should succeed when transition from PRE_PUBLISHED to PUBLISHED`() {
        val existingBook = Book(id = 1L, title = "Old", price = 1000, status = BookStatus.PRE_PUBLISHED, authors = listOf(sampleAuthor))
        val newBook = existingBook.copy(status = BookStatus.PUBLISHED)
        
        every { bookRepository.findById(1L) } returns existingBook
        every { bookRepository.update(any()) } returns Unit

        bookService.updateBook(newBook)

        verify { bookRepository.update(newBook) }
    }
}
