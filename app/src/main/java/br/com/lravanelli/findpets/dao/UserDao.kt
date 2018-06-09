package br.com.lravanelli.findpets.dao

import android.arch.persistence.room.*
import br.com.lravanelli.findpets.model.UserPers


@Dao
interface UserDao {
    @Insert
    fun insertUser(user: UserPers)

    @Query("SELECT * FROM UserPers")
    fun getUser(): UserPers

    @Update
    fun updateUser(user: UserPers)

    @Query("DELETE FROM UserPers")
    fun deleteUser()

}