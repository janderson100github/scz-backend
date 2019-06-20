package credit.core.email;

public class EmailHost {

    private String from;

    private String host;

    private String user;

    private String password;

    private String port;

    public EmailHost(final String from,
                     final String host,
                     final String user,
                     final String password,
                     final String port) {
        this.from = from;
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    public String getFrom() {
        return from;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getPort() {
        return port;
    }
}
