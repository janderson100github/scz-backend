package credit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${documentation.swagger.display:true}")
    private boolean display;

    @Value("${documentation.swagger.package-to-be-documented:credit}")
    private String packageToBeDocumented;

    @Value("${documentation.swagger.paths-to-be-documented:/.*}")
    private String urlPathsToBeDocumented;

    @Value("${documentation.swagger.document-title:API}")
    private String documentTitle;

    @Value("${documentation.swagger.document-description:API}")
    private String documentDescription;

    @Value("${documentation.swagger.terms-of-credit.service-url:https://www.socialcreditzone.com}")
    private String termsOfServiceUrl;

    @Value("${documentation.swagger.team-name:API Team}")
    private String teamName;

    @Value("${documentation.swagger.team-url:}")
    private String teamUrl;

    @Value("${documentation.swagger.team-email:}")
    private String teamEmail;

    @Value("${documentation.swagger.license-description:Copyright 2018}")
    private String licenseDescription;

    @Value("${documentation.swagger.license-url:}")
    private String licenseUrl;

    @Bean
    public Docket buildDocket() {
        if (display) {
            List<ResponseMessage> responseMessages = responseMessages();

            return new Docket(DocumentationType.SWAGGER_2).select()
                    .apis(RequestHandlerSelectors.basePackage(packageToBeDocumented))
                    .paths(regex(urlPathsToBeDocumented))
                    .build()
                    .apiInfo(buildApiInfo())
                    .useDefaultResponseMessages(false)
                    .directModelSubstitute(ResponseEntity.class, Void.class)
                    .globalResponseMessage(RequestMethod.GET, responseMessages)
                    .globalResponseMessage(RequestMethod.POST, responseMessages)
                    .globalResponseMessage(RequestMethod.PUT, responseMessages)
                    .globalResponseMessage(RequestMethod.PATCH, responseMessages)
                    .globalResponseMessage(RequestMethod.DELETE, responseMessages);
        } else {
            return new Docket(DocumentationType.SWAGGER_2).select()
                    .apis(RequestHandlerSelectors.basePackage("/"))
                    .build();
        }
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfo(documentTitle, documentDescription, null, termsOfServiceUrl,
                           new Contact(teamName, teamUrl, teamEmail), licenseDescription, licenseUrl,
                           new ArrayList<>());
    }

    private List<ResponseMessage> responseMessages() {
        return Arrays.asList(new ResponseMessageBuilder().code(400)
                                     .message("Bad request.")
                                     .build(), new ResponseMessageBuilder().code(401)
                                     .message("Unauthorized.")
                                     .build(), new ResponseMessageBuilder().code(403)
                                     .message("Forbidden operation.")
                                     .build(), new ResponseMessageBuilder().code(404)
                                     .message("Not found.")
                                     .build(), new ResponseMessageBuilder().code(500)
                                     .message("Internal server error.")
                                     .build());
    }
}
