package jp.co.quocard.book_management_system.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jp.co.quocard.book_management_system.domain.Author
import jp.co.quocard.book_management_system.repository.AuthorRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.test.assertEquals

class AuthorServiceTest {
    private val authorRepository = mockk<AuthorRepository>()
    private val authorService = AuthorService(authorRepository)

    @Test
    fun `createAuthor should succeed when birthDate is today`() {
        val today = LocalDate.now()
        val author = Author(name = "Test Author", birthDate = today)
        every { authorRepository.save(any()) } returns author.copy(id = 1L)

        val result = authorService.createAuthor(author)

        assertEquals(1L, result.id)
        verify { authorRepository.save(author) }
    }

    @Test
    fun `createAuthor should throw exception when birthDate is in the future`() {
        val future = LocalDate.now().plusDays(1)
        val author = Author(name = "Future Author", birthDate = future)

        val exception = assertThrows<IllegalArgumentException> {
            authorService.createAuthor(author)
        }
        assertEquals("生年月日は現在日以前である必要があります。", exception.message)
    }
}
