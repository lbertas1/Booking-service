package bartos.lukasz.bookingservice.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiUsers() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("USERS")
                .select()
                .apis(RequestHandlerSelectors.basePackage("bartos.lukasz.bookingservice.infrastructure.controllers"))
                .paths(PathSelectors.ant("/users/**"))
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(
                        RequestMethod.GET,
                        newArrayList(
                                new ResponseMessageBuilder()
                                        .code(404)
                                        .message("No user found by given username :username")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(404)
                                        .message("User not found")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(404)
                                        .message("Reservation doesn't have an assigned user")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("Given username already exists")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("Given email is null")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("Given email already exists")
                                        .build()
                        )
                )
                .globalResponseMessage(
                        RequestMethod.POST,
                        newArrayList(
                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("Given user object is null")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("ChangePasswordDto object is null")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("Incorrect old password.")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("You should enter valid password to create account")
                                        .build()
                        )
                )
                .globalResponseMessage(
                        RequestMethod.PUT,
                        newArrayList(
                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("Given user object is null")
                                        .build()
                        )
                )
                .apiInfo(apiInfo());
    }

    @Bean
    public Docket apiRooms() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("ROOMS")
                .select()
                .apis(RequestHandlerSelectors.basePackage("bartos.lukasz.bookingservice.infrastructure.controllers"))
                .paths(PathSelectors.ant("/rooms/**"))
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(
                        RequestMethod.GET,
                        newArrayList(
                                new ResponseMessageBuilder()
                                        .code(404)
                                        .message("Room doesn't found")
                                        .build()
                        ))
                .apiInfo(apiInfo());
    }

    @Bean
    public Docket apiReservations() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("RESERVATIONS")
                .select()
                .apis(RequestHandlerSelectors.basePackage("bartos.lukasz.bookingservice.infrastructure.controllers"))
                .paths(PathSelectors.ant("/reservations/**"))
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(
                        RequestMethod.POST,
                        newArrayList(
                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("Given reservation object is null")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("The booking has already started. Beginning cannot be updated now")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("The booking start date cannot be later than its end date")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(404)
                                        .message("User not found")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(404)
                                        .message("Room not found")
                                        .build()
                                )
                )
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Booking-service documentation")
                .description("Booking-service is an application that allows you to handle both the client and the hotel staff.\n" +
                        "Customers can make a reservation or contact the service.\n" +
                        "The application also provides many useful hotel management functions for the staff.")
                .contact(new Contact("Łukasz Bartoś", "www.linkedin.com/in/łukasz-bartoś-5411731b3", "b.lukasz.1994@gmail.com"))
                .version("1.0")
                .build();
    }
}