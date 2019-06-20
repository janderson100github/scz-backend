package credit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public class AbstractRestController {

    final Logger logger = LoggerFactory.getLogger(getClass());
}
