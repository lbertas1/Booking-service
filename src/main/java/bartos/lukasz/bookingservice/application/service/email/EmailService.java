package bartos.lukasz.bookingservice.application.service.email;

import bartos.lukasz.bookingservice.application.enums.EmailContent;
import bartos.lukasz.bookingservice.application.exception.EmailServiceException;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationDto;
import bartos.lukasz.bookingservice.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailService {

    private String emailAddress;
    private String emailPassword;

    private final EmailHtmlReservationContentService emailHtmlReservationContentService;
    private final EmailHtmlRegistrationContentService emailHtmlRegistrationContentService;
    private final EmailPdfFileService emailPdfFileService;

    public void send(UserDto userDto, List<ReservationDto> reservationDtos, String orderNumber, EmailContent emailContent) throws EmailServiceException {
        try {
            String content;
            readEmailPassword();

            Session session = createSession();
            MimeMessage mimeMessage = new MimeMessage(session);

            if (emailContent.equals(EmailContent.RESERVATION)) {
                content = emailHtmlReservationContentService.renderHtmlContent();
                emailPdfFileService.createOrderPdf(userDto, reservationDtos, orderNumber);
                String attachmentPath = getAttachmentPath(orderNumber);
                String fileName = "order " + orderNumber + ".pdf";
                prepareEmailMessageWithFile(mimeMessage, prepareMimeBodyPartWithFile(attachmentPath, fileName, content), userDto.getEmail(), orderNumber);
            } else {
                content = emailHtmlRegistrationContentService.renderHtmlContent();
                prepareEmailMessage(mimeMessage, userDto.getEmail(), content);
            }

            Transport.send(mimeMessage);
        } catch (Exception e) {
            throw new EmailServiceException(e.getMessage(), 500, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void prepareEmailMessage(MimeMessage mimeMessage, String to, String content) throws EmailServiceException {
        try {
            mimeMessage.setContent(content, "text/html; charset=utf-8");
            mimeMessage.setFrom(new InternetAddress(emailAddress));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject("Registration");
        } catch (Exception e) {
            throw new EmailServiceException(e.getMessage() , 500, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void prepareEmailMessageWithFile(MimeMessage mimeMessage, Multipart multipart, String to, String orderNumber) throws EmailServiceException {
        try {
            mimeMessage.setContent(multipart);
            mimeMessage.setFrom(new InternetAddress(emailAddress));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject("order " + orderNumber);
        } catch (Exception e) {
            throw new EmailServiceException(e.getMessage(), 500, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Session createSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, emailPassword);
            }
        });
    }

    private Multipart prepareMimeBodyPartWithFile(String path, String fileName, String html) {
        try {
            BodyPart mimeBodyPart1 = new MimeBodyPart();
            mimeBodyPart1.setContent(html, "text/html; charset=utf-8");

            MimeBodyPart mimeBodyPart2 = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(path);
            mimeBodyPart2.setDataHandler(new DataHandler(dataSource));
            mimeBodyPart2.setFileName(fileName);

            return prepareMultipart(mimeBodyPart1, mimeBodyPart2);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Multipart prepareMultipart(BodyPart mimeBodyPart1, MimeBodyPart mimeBodyPart2) {
        try {
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart1);
            multipart.addBodyPart(mimeBodyPart2);
            return multipart;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void readEmailPassword() {
        try {
            List<String> strings = Files.readAllLines(Path.of("poczta.txt"));
            emailAddress = strings.get(0);
            emailPassword = strings.get(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getAttachmentPath(String orderNumber) {
        return "src/main/resources/pdf/order " + orderNumber + ".pdf";
    }
}
