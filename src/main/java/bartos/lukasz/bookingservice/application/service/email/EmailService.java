package bartos.lukasz.bookingservice.application.service.email;

import bartos.lukasz.bookingservice.application.dto.events.EmailData;
import bartos.lukasz.bookingservice.application.dto.events.OrderEmailData;
import bartos.lukasz.bookingservice.application.dto.events.RegistrationEmailData;
import bartos.lukasz.bookingservice.application.exception.EmailServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.profiles.active}")
    private String applicationProfile;

    private final JavaMailSender javaMailSender;

    private final EmailHtmlReservationContentService emailHtmlReservationContentService;
    private final EmailHtmlRegistrationContentService emailHtmlRegistrationContentService;
    private final EmailPdfFileService emailPdfFileService;

    public void send(EmailData emailData) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(emailData.getRecipient());

            if (emailData instanceof OrderEmailData) {
                javaMailSender.send(prepareEmailWithOrder((OrderEmailData) emailData, mimeMessageHelper).getMimeMessage());
            } else if (emailData instanceof RegistrationEmailData) {
                javaMailSender.send(prepareRegistrationEmail(mimeMessageHelper).getMimeMessage());
            }
        } catch (MessagingException e) {
            throw new EmailServiceException(e.getMessage(), 500, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private MimeMessageHelper prepareEmailWithOrder(OrderEmailData orderEmailData, MimeMessageHelper mimeMessageHelper) throws MessagingException {
        mimeMessageHelper.setText(emailHtmlReservationContentService.renderHtmlContent(), true);
        emailPdfFileService.createOrderPdf(orderEmailData.getUserDto(), orderEmailData.getReservationDtos(), orderEmailData.getOrderNumber());
        mimeMessageHelper.setSubject("Order " + orderEmailData.getOrderNumber());
        mimeMessageHelper.addAttachment("order " + orderEmailData.getOrderNumber() + ".pdf",
                new File(getAttachmentPath(orderEmailData.getOrderNumber())));
        return mimeMessageHelper;
    }

    private MimeMessageHelper prepareRegistrationEmail(MimeMessageHelper mimeMessageHelper) throws MessagingException {
        mimeMessageHelper.setText(emailHtmlRegistrationContentService.renderHtmlContent(), true);
        mimeMessageHelper.setSubject("Reservation confirmation");

        return mimeMessageHelper;
    }

    private String getAttachmentPath(String orderNumber) {
        if (applicationProfile.equals("dev")) {
            return "src/main/resources/pdf/order " + orderNumber + ".pdf";
        } else {
            String pathToOrdersDirectory = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().getPath() + "/resources-orders";
            return pathToOrdersDirectory + "/order " + orderNumber + ".pdf";
        }
    }
}