package credit.core.email;

public class EmailContent {

    private String subject;

    private String msg;

    private String displayFrom;

    public EmailContent(final String subject, final String msg, final String displayFrom) {
        this.subject = subject;
        this.msg = msg;
        this.displayFrom = displayFrom;
    }

    public String getSubject() {
        return subject;
    }

    public String getMsg() {
        return msg;
    }

    public String getDisplayFrom() {
        return displayFrom;
    }
}
