package jp.co.quocard.book_management_system.domain

data class Book(
    val id: Long? = null,
    val title: String,
    val price: Int,
    val status: BookStatus,
    val authors: List<Author> = emptyList()
)
