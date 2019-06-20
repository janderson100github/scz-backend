package credit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AppController {

    @GetMapping(value = {"/", ""})
    public String getIndex(HttpServletRequest request) {
        return "/index.html";
    }
}
