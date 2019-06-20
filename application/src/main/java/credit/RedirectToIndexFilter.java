package credit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class RedirectToIndexFilter implements Filter {

    private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();

        if (requestURI.startsWith("/api") || requestURI.contains("swagger") || requestURI.contains("doc")) {
            if (!response.isCommitted()) {
                chain.doFilter(request, response);
            }
            return;
        }

        if (requestURI.contains("main.js") || requestURI.matches(IMAGE_PATTERN)) {
            if (!response.isCommitted()) {
                chain.doFilter(request, response);
            }
            return;
        }

        // all requests not api or static will be forwarded to index page.
        request.getRequestDispatcher("/")
                .forward(request, response);
    }
}
