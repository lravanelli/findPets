package br.com.lravanelli.findpets.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


data class User(val id: Int, val email: String, val password: String?)


@Entity(tableName = "UserPers")
class UserPers {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
    var email: String = ""
    var keep: Boolean = false

    constructor(id: Int, email: String, keep: Boolean) {
        this.id = id
        this.email = email
        this.keep = keep
    }
}



