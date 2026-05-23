package com.example.pr57

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Company::class], version = 1)
abstract class CompanyDatabase : RoomDatabase() {

    abstract fun companyDao(): CompanyDao

    companion object {

        @Volatile
        private var INSTANCE: CompanyDatabase? = null

        val initialCompanies = listOf(
            Company(name = "Газпром", capitalization = 68012),
            Company(name = "НК «Роснефть»", capitalization = 62534),
            Company(name = "НОВАТЭК", capitalization = 51630),
            Company(name = "Норильский никель", capitalization = 50604),
            Company(name = "ЛУКОЙЛ", capitalization = 48601),
            Company(name = "Сбербанк", capitalization = 47830),
            Company(name = "Полюс", capitalization = 27738),
            Company(name = "Яндекс", capitalization = 22122),
            Company(name = "Газпром нефть", capitalization = 20406),
            Company(name = "Сургутнефтегаз", capitalization = 17405),
            Company(name = "НЛМК", capitalization = 16941),
            Company(name = "Татнефть", capitalization = 15176),
            Company(name = "Северсталь", capitalization = 15029),
            Company(name = "Транснефть", capitalization = 11234),
            Company(name = "Полиметалл", capitalization = 11142),
            Company(name = "En+ Group", capitalization = 10890),
            Company(name = "ММК", capitalization = 10234),
            Company(name = "OZON", capitalization = 12345),
            Company(name = "TCS Group", capitalization = 9876),
            Company(name = "Алроса", capitalization = 9876),
            Company(name = "ВТБ", capitalization = 8901),
            Company(name = "Fix Price", capitalization = 8765),
            Company(name = "Mail.Ru Group", capitalization = 8234),
            Company(name = "HeadHunter", capitalization = 7654),
            Company(name = "Etalon Group", capitalization = 7123),
            Company(name = "X5 Retail Group", capitalization = 6789),
            Company(name = "Башнефть", capitalization = 6543),
            Company(name = "МТС", capitalization = 6234),
            Company(name = "Магнит", capitalization = 5987),
            Company(name = "Globaltrans", capitalization = 5432),
            Company(name = "Интер РАО", capitalization = 5432),
            Company(name = "ФосАгро", capitalization = 5123),
            Company(name = "Россети", capitalization = 4876),
            Company(name = "РусАгро", capitalization = 4567),
            Company(name = "Softline", capitalization = 4321),
            Company(name = "Русгидро", capitalization = 4321),
            Company(name = "Аэрофлот", capitalization = 3456),
            Company(name = "Детский мир", capitalization = 3456),
            Company(name = "Распадская", capitalization = 3210),
            Company(name = "Сегежа", capitalization = 2987),
            Company(name = "Лента", capitalization = 2890),
            Company(name = "Россети Северный Кавказ (МРСК Северного Кавказа)", capitalization = 1234)
        )

        fun getDatabase(context: Context): CompanyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CompanyDatabase::class.java,
                    "company_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
