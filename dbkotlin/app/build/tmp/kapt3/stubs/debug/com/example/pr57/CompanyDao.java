package com.example.pr57;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u000e\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\'J\b\u0010\t\u001a\u00020\nH\'J\b\u0010\u000b\u001a\u00020\nH\'J\n\u0010\f\u001a\u0004\u0018\u00010\u0005H\'J\n\u0010\r\u001a\u0004\u0018\u00010\u0005H\'J\b\u0010\u000e\u001a\u00020\u000fH\'J\u0016\u0010\u0010\u001a\u00020\u00032\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\'\u00a8\u0006\u0012"}, d2 = {"Lcom/example/pr57/CompanyDao;", "", "deleteBySubstring", "", "substring", "", "getAll", "", "Lcom/example/pr57/Company;", "getCountAboveAverage", "", "getEnglishCount", "getLongestNameCompany", "getMaxCapCompanyName", "getTotalCapitalization", "", "insertAll", "companies", "app_debug"})
@androidx.room.Dao()
public abstract interface CompanyDao {
    
    @androidx.room.Query(value = "SELECT * FROM companies ORDER BY capitalization DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.example.pr57.Company> getAll();
    
    @androidx.room.Insert()
    public abstract void insertAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.pr57.Company> companies);
    
    @androidx.room.Query(value = "DELETE FROM companies WHERE name LIKE \'%\' || :substring || \'%\'")
    public abstract void deleteBySubstring(@org.jetbrains.annotations.NotNull()
    java.lang.String substring);
    
    @androidx.room.Query(value = "SELECT SUM(capitalization) FROM companies")
    public abstract long getTotalCapitalization();
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM companies WHERE capitalization > (SELECT AVG(CAST(capitalization AS REAL)) FROM companies)")
    public abstract int getCountAboveAverage();
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM companies WHERE name < \'\u0410\'")
    public abstract int getEnglishCount();
    
    @androidx.room.Query(value = "SELECT name FROM companies WHERE capitalization = (SELECT MAX(capitalization) FROM companies) ORDER BY name ASC LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.String getMaxCapCompanyName();
    
    @androidx.room.Query(value = "SELECT name FROM companies ORDER BY LENGTH(name) DESC, name ASC LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.String getLongestNameCompany();
}