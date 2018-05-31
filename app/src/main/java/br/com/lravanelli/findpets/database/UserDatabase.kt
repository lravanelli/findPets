package br.com.lravanelli.findpets.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import br.com.lravanelli.findpets.dao.UserDao
import br.com.lravanelli.findpets.model.UserPers

@Database(entities = arrayOf(UserPers::class), version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase? {

            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserDatabase::class.java,
                        "findDB")
                        .build()
            }
            return INSTANCE
        }
    }

}