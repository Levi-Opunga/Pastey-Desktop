package entities;

import kotlinx.serialization.Serializable

@Serializable
data class ClipNote(val id: Long, var text: String?, var date: String?, val username: String?, val hidden: Boolean?) {
 constructor() : this(0, "", "", "", false)
}
