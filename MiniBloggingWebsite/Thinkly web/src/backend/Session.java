package backend;

public class Session {
    public static int userId = -1;
    public static String username = null;
    public static String email = null;

    public static void set(int id, String uname, String em) {
        userId = id; username = uname; email = em;
    }

    public static void clear() { userId = -1; username = null; email = null; }
}
