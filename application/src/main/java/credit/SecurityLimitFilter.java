package credit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class SecurityLimitFilter implements Filter {

    private static final long PERIOD_MILLIS = 1 * 60 * 60 * 1000;

    private static final int MAX_PER_PERIOD = 5;

    private Map<String, IpCount> ipCountMap = new HashMap<String, IpCount>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();

        if (req.getMethod()
                    .equalsIgnoreCase("POST") && (requestURI.startsWith(Paths.API + Paths.ACCOUNT) ||
                                                  requestURI.startsWith(Paths.API + Paths.POOL))) {
            String ip = req.getRemoteAddr();
            IpCount ipCount = ipCountMap.get(ip);
            if (ipCount == null) {
                ipCountMap.put(ip, new IpCount(ip));
            } else {
                ipCount.count = ipCount.count + 1;
                if (ipCount.count > MAX_PER_PERIOD) {
                    HttpServletResponse res = (HttpServletResponse) response;
                    res.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }
        }

        if (!response.isCommitted()) {
            chain.doFilter(request, response);
        }
    }

    @Scheduled(fixedDelay = PERIOD_MILLIS)
    void clearIpCounts() {
        long nowMinusTime = new Date().getTime() - PERIOD_MILLIS;
        Set<String> ips = new HashSet<>();
        for (IpCount ipCount : this.ipCountMap.values()) {
            if (ipCount.date.getTime() < nowMinusTime) {
                ips.add(ipCount.ip);
            }
        }
        for (String ip : ips) {
            this.ipCountMap.remove(ip);
        }
    }

    private static class IpCount {

        public String ip;

        public Integer count = 1;

        public Date date = new Date();

        public IpCount(final String ip) {
            this.ip = ip;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final IpCount ipCount = (IpCount) o;

            return ip.equals(ipCount.ip);
        }

        @Override
        public int hashCode() {
            return ip.hashCode();
        }
    }
}
