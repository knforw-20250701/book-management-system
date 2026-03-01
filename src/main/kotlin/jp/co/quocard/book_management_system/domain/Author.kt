package jp.co.quocard.book_management_system.domain

import java.time.LocalDate

data class Author(
    val id: Long? = null,
    val name: String,
    val birthDate: LocalDate
)
