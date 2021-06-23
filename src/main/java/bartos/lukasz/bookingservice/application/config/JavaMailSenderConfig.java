package bartos.lukasz.bookingservice.application.config;

import bartos.lukasz.bookingservice.application.exception.EmailServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class JavaMailSenderConfig {

    @Value("${spring.profiles.active}")
    private String applicationProfile;

    private final Environment environment;

    @Bean
    public JavaMailSender createJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        Properties props = setPassword(mailSender).getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

    private JavaMailSenderImpl setPassword(JavaMailSenderImpl javaMailSender) {
        if (applicationProfile.equals("dev")) {
            List<String> strings = null;

            try {
                strings = Files.readAllLines(Path.of("poczta.txt"));
            } catch (IOException e) {
                throw new EmailServiceException("Incorrect file with email pass", 500, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            javaMailSender.setUsername(strings.get(0));
            javaMailSender.setPassword(strings.get(1));
        } else if (applicationProfile.equals("prod")) {
            javaMailSender.setUsername(environment.getProperty("email.address"));
            javaMailSender.setPassword(environment.getProperty("email.password"));
        }
        return javaMailSender;
    }
}