package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.AdminAuditLogDao
import com.example.data.dao.MissionDao
import com.example.data.dao.TransactionDao
import com.example.data.dao.UserDao
import com.example.data.model.AdminAuditLogEntity
import com.example.data.model.MissionEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TransactionEntity::class,
        MissionEntity::class,
        AdminAuditLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TobiGtDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun missionDao(): MissionDao
    abstract fun adminAuditLogDao(): AdminAuditLogDao

    companion object {
        @Volatile
        private var INSTANCE: TobiGtDatabase? = null

        fun getDatabase(context: Context): TobiGtDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TobiGtDatabase::class.java,
                    "tobi_gt_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
