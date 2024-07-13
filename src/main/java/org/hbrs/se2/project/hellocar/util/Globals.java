package org.hbrs.se2.project.hellocar.util;
import java.util.logging.Logger;

public abstract class Globals {

    public static String CURRENT_USER = "current_User";
    public static boolean IS_STUDENT_USER = false;

    public static class Pages {
        public static final String SEARCH = "search";
        public static final String REGISTER = "registration";

        public static final String LOGIN_VIEW = "login";
        public static final String MAIN_VIEW = "";
        public static final String PROFILE = "profile";
        public static final String PROFIL = "profil";
        public static final String STELLENANGEBOT = "stellenangebot";
        public static final String BEWERBUNG = "bewerbung";
        public static final String BEWERBEN = "bewerben";
    }

    public static class Roles {
        public static final String COMPANY = "company";
        public static final String USER = "user";

    }

    public static class Errors {
        public static final String NOUSERFOUND = "nouser";
        public static final String SQLERROR = "sql";
        public static final String DATABASE = "database";
    }

}
