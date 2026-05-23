package com.example.pr57;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00052\u00020\u0001:\u0001\u0005B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0006"}, d2 = {"Lcom/example/pr57/CompanyDatabase;", "Landroidx/room/RoomDatabase;", "()V", "companyDao", "Lcom/example/pr57/CompanyDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.example.pr57.Company.class}, version = 1)
public abstract class CompanyDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.example.pr57.CompanyDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.example.pr57.Company> initialCompanies = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.pr57.CompanyDatabase.Companion Companion = null;
    
    public CompanyDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.pr57.CompanyDao companyDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\fR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\r"}, d2 = {"Lcom/example/pr57/CompanyDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/example/pr57/CompanyDatabase;", "initialCompanies", "", "Lcom/example/pr57/Company;", "getInitialCompanies", "()Ljava/util/List;", "getDatabase", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.example.pr57.Company> getInitialCompanies() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.pr57.CompanyDatabase getDatabase(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}