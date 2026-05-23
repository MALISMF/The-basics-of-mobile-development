package com.example.pr57

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CompanyDao {

    @Query("SELECT * FROM companies ORDER BY capitalization DESC")
    fun getAll(): List<Company>

    @Insert
    fun insertAll(companies: List<Company>)

    @Query("DELETE FROM companies WHERE name LIKE '%' || :substring || '%'")
    fun deleteBySubstring(substring: String)

    @Query("SELECT SUM(capitalization) FROM companies")
    fun getTotalCapitalization(): Long

    @Query("SELECT COUNT(*) FROM companies WHERE capitalization > (SELECT AVG(CAST(capitalization AS REAL)) FROM companies)")
    fun getCountAboveAverage(): Int

    @Query("SELECT COUNT(*) FROM companies WHERE name < 'А'")
    fun getEnglishCount(): Int

    @Query("SELECT name FROM companies WHERE capitalization = (SELECT MAX(capitalization) FROM companies) ORDER BY name ASC LIMIT 1")
    fun getMaxCapCompanyName(): String?

    @Query("SELECT name FROM companies ORDER BY LENGTH(name) DESC, name ASC LIMIT 1")
    fun getLongestNameCompany(): String?
}
