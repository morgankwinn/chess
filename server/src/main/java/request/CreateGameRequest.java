package request;

import java.util.Objects;

public final class CreateGameRequest {
    private String authToken;
    private final String gameName;

    public CreateGameRequest(String authToken, String gameName) {
        this.authToken = authToken;
        this.gameName = gameName;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String authToken() {
        return authToken;
    }

    public String gameName() {
        return gameName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (CreateGameRequest) obj;
        return Objects.equals(this.authToken, that.authToken) &&
                Objects.equals(this.gameName, that.gameName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, gameName);
    }

    @Override
    public String toString() {
        return "CreateGameRequest[" +
                "authToken=" + authToken + ", " +
                "gameName=" + gameName + ']';
    }

}
