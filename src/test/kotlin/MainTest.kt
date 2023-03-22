@file:Suppress("JSON_FORMAT_REDUNDANT")

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.TemporalUnit
import kotlin.time.DurationUnit

class MainTest {
    @Test
    fun import() {
        val url = "https://sessionize.com/api/v2/72i2tw4v/view/All"

        var start = LocalDateTime.of(2023, 4, 27, 9, 0).atOffset(ZoneOffset.of("+01:00"))

        val seData = Request.Builder()
            .get()
            .url(url)
            .build()
            .let {
                OkHttpClient()
                    .newCall(it)
                    .execute()
            }.let {
                Json {
                    ignoreUnknownKeys = true
                }.decodeFromString(SeData.serializer(), it.body!!.string())
            }

        val ofData = OfData(
            sessions = seData.sessions.map {
                val end = start + Duration.ofMinutes(30)
                OfSession(
                    id = it.id,
                    title = it.title,
//                    startTime = it.startsAt.withOffset(),
//                    endTime = it.endsAt.withOffset(),
                    startTime = start.toString(),
                    endTime = end.toString(),
                    speakers = it.speakers
                ).also {
                    start = end
                }
            }.associateBy { it.id },
            speakers = seData.speakers.map {
                OfSpeaker(
                    id = it.id,
                    name = it.fullName,
                    socials = it.links.map {
                        OfSocial(name = it.title, link = it.url)
                    },
                    photoUrl = it.profilePicture
                )
            }.associateBy { it.id }
        )

        println(Json.encodeToString(ofData))
    }
}

fun String.withOffset(): String {
    return LocalDateTime.parse(this).atOffset(ZoneOffset.of("+01:00")).toString()
}

@Serializable
class SeData(
    val sessions: List<SeSession>,
    val speakers: List<SeSpeaker>,
)

@Serializable
class SeSpeaker(
    val id: String,
    val fullName: String,
    val profilePicture: String? = null,
    val links: List<SeLink>
)

@Serializable
class SeLink(
    val title: String,
    val url: String
)

@Serializable
class SeSession(
    val id: String,
    val title: String,
    val startsAt: String? = null,
    val endsAt: String? = null,
    val speakers: List<String>,
)


@Serializable
class OfData(
    val sessions: Map<String, OfSession>,
    val speakers: Map<String, OfSpeaker>
)

@Serializable
class OfSession(
    val id: String,
    val title: String,
    val startTime: String,
    val endTime: String,
    val speakers: List<String>
)

@Serializable
class OfSpeaker(
    val id: String,
    val name: String,
    val photoUrl: String? = null,
    val socials: List<OfSocial>
)


@Serializable
class OfSocial(
    val name: String,
    val link: String
)