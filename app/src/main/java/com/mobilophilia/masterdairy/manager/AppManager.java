package com.mobilophilia.masterdairy.manager;

/**
 * Created by Hanji on 7/21/2017.
 */

public class AppManager {
    private static final AppManager ourInstance = new AppManager();

    private LoginManager loginManager;
    private EntryManager entryManager;
    private ExpenseManager expenseManager;
    private DownloadManager downloadManager;

    private AppManager() {
        loginManager = new LoginManager();
        entryManager = new EntryManager();
        expenseManager = new ExpenseManager();
        downloadManager = new DownloadManager();
    }

    public static AppManager getInstance() {
        return ourInstance;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public EntryManager getEntryManager() {
        return entryManager;
    }

    public ExpenseManager getExpenseManager() {
        return expenseManager;
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }
}
