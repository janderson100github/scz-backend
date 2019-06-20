package credit.core.email;

import credit.core.exception.CreditRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value(value = "${email.displayfrom.validation}")
    private String emailDisplayFromValidation;

    @Value(value = "${email.subject.validation}")
    private String emailSubjectValidation;

    @Value(value = "${email.msg.validation}")
    private String emailMsgValidation;

    @Value(value = "${host.base}")
    private String hostBase;

    private Environment environment;

    public EmailService(final Environment environment) {
        this.environment = environment;
    }

    public boolean sendValidationEmail(String poolPublicId, String code, String email) {
        if (!validateEmail(email)) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Email invalid.");
        }

        EmailContent emailContent = getValidationEmailContent(poolPublicId, code);
        EmailHost emailHost = getEmailHost(1);
        return sendValidationEmail(emailHost, emailContent, email);
    }

    private boolean sendValidationEmail(EmailHost emailHost, EmailContent emailContent, String emailAddress) {
        List<String> toList = new ArrayList<>();
        toList.add(emailAddress);

        try {
            Emailer.sendEmail(toList, emailContent.getSubject(), emailContent.getMsg(), emailHost.getFrom(),
                              emailContent.getDisplayFrom(), emailHost.getHost(), emailHost.getUser(),
                              emailHost.getPassword(), emailHost.getPort());
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Could not send email to " + emailAddress + ": " + e);
            return false;
        } finally {
            logger.info("email sent to: " + emailAddress);
        }

        return true;
    }

    private EmailContent getValidationEmailContent(String poolPublicId, String code) {
        String subject = emailSubjectValidation;
        String msg = emailMsgValidation;
        String displayFrom = emailDisplayFromValidation;

        msg = msg.replace("{link}", generateUrl(poolPublicId, code));

        return new EmailContent(subject, msg, displayFrom);
    }

    private EmailHost getEmailHost(final int i) {
        String host = environment.getProperty("email.host." + i);
        String port = environment.getProperty("email.port." + i);
        String user = environment.getProperty("email.user." + i);
        String password = environment.getProperty("email.password." + i);
        String from = environment.getProperty("email.from." + i);
        if (StringUtils.isEmpty(host)) {
            return null;
        }
        EmailHost emailHost = new EmailHost(from, host, user, password, port);
        return emailHost;
    }

    private String generateUrl(String poolPublicId, String code) {
        return hostBase + "/verification/email/" + poolPublicId + "/" + code;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                                                                            Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        if (!matcher.find()) {
            return false;
        }

        return true;
    }
}
