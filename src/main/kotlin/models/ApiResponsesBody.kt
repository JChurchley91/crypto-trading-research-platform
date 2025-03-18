package models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object ApiResponsesBody : IntIdTable("raw.api_responses_bodies") {
    val apiResponseKey = varchar("api_response_key", 255)
    val task = varchar("task_name", 255)
    val articleTitle = varchar("article_title", 500)
    val articleLink = varchar("article_link", 500)
    val createdAt = datetime("created_at")
}
