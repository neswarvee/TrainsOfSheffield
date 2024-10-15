package trainsofsheffield.models;

/**
 * The Session class represents a user session in the application.
 * It manages information about the user's login state.
 */
public class Session {
    /** The user associated with the current session. */
    private User user;

    /** A boolean flag indicating whether the user is currently logged in. */
    private boolean isLoggedIn;

    /**
     * Constructs a new Session object with the specified user.
     * Initializes the user and sets the isLoggedIn flag to true.
     * @param user The user associated with the session.
     */
    public Session(User user) {
        this.user = user;
        this.isLoggedIn = true;
    }

    /**
     * Gets the user associated with the current session.
     * @return The user object.
     */
    public User getUser() {
        return user;
    }

    /**
     * Checks if the user is currently logged in.
     * @return True if the user is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Logs out the user by setting the isLoggedIn flag to false.
     */
    public void logout() {
        isLoggedIn = false;
    }
}