package bartos.lukasz.bookingservice.application.service.email;

import bartos.lukasz.bookingservice.application.exception.EmailServiceException;
import bartos.lukasz.bookingservice.domain.reservation.bookingStatus.dto.BookingStatusDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationDto;
import bartos.lukasz.bookingservice.domain.user.dto.UserDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class EmailPdfFileService {

    public void createOrderPdf(UserDto userDto, List<ReservationDto> reservationsDto, String orderNumber) {
        Rectangle page = new Rectangle(PageSize.A4);
        page.setBorder(Rectangle.BOX);
        Document order = new Document(page);

        try {
            Files.deleteIfExists(Paths.get("src/main/resources/pdf/order " + orderNumber + ".pdf"));
            PdfWriter.getInstance(order, new FileOutputStream(new File("src/main/resources/pdf/order " + orderNumber + ".pdf")));

            order.open();

            order.add(setTitle());

            order.add(setOrderNumber(orderNumber));

            Font userDataFont = new Font();
            userDataFont.setSize(10);
            userDataFont.setStyle(Font.BOLD);
            String name = "Name: " + userDto.getName();
            String surname = "Surname: " + userDto.getSurname();
            String age = "Birthdate: " + userDto.getBirthDate();
            String email = "Email: " + userDto.getEmail();

            order.add(setUserDataParagraphName(name, userDataFont));

            order.add(setParagraphWithStandardSpace(surname, userDataFont));

            order.add(setParagraphWithStandardSpace(age, userDataFont));

            order.add(setParagraphWithStandardSpace(email, userDataFont));

            order.add(setParagraphWithBiggerSpace("Price: " + countTotalAmount(reservationsDto), userDataFont));

            Font boldFont = new Font();
            boldFont.setStyle(Font.BOLD);

            order.add(setTableDescription(boldFont));

            order.add(setTableHeader(boldFont));

            AtomicInteger counter = new AtomicInteger(0);
            reservationsDto
                    .forEach(reservationDto -> {
                        try {
                            order.add(setTableContent(boldFont, reservationDto.getRoomDto().getRoomNumber().toString(), reservationDto, counter.incrementAndGet()));
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    });

            order.add(addClause());

            order.add(addRegards(boldFont));

            order.add(addQrCode(userDto, reservationsDto));

            order.close();

        } catch (DocumentException | IOException | EmailServiceException e) {
            e.printStackTrace();
        }
    }

    private String countTotalAmount(java.util.List<ReservationDto> reservationDtoList) throws EmailServiceException {
        return reservationDtoList
                .stream()
                .map(ReservationDto::getBookingStatusDto)
                .map(BookingStatusDto::getTotalAmountForReservation)
                .reduce(BigDecimal::add)
                .orElseThrow(() -> new EmailServiceException("Adding failed", 500, HttpStatus.INTERNAL_SERVER_ERROR))
                .toString();
    }

    private Paragraph setTitle() {
        Font title = new Font();
        title.setSize(20);
        title.setStyle(Font.UNDERLINE);
        Paragraph paragraph = new Paragraph("Pacific Hotel", title);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    private Paragraph setOrderNumber(String orderNumber) {
        Font ticketNumberFont = new Font();
        ticketNumberFont.setStyle(Font.BOLD);
        ticketNumberFont.setStyle(Font.ITALIC);
        ticketNumberFont.setSize(10);
        String ticket = "Order number: " + orderNumber;
        Paragraph ticketNumberParagraph = new Paragraph(ticket, ticketNumberFont);
        ticketNumberParagraph.setSpacingBefore(10);
        ticketNumberParagraph.setSpacingAfter(10);
        ticketNumberParagraph.setAlignment(Element.ALIGN_CENTER);
        return ticketNumberParagraph;
    }

    private Paragraph setUserDataParagraphName(String name, Font userDataFont) {
        Paragraph userDataParagraphName = new Paragraph(name, userDataFont);
        userDataParagraphName.setSpacingBefore(10);
        userDataParagraphName.setSpacingAfter(5);
        return userDataParagraphName;
    }

    private Paragraph setParagraphWithStandardSpace(String surname, Font userDataFont) {
        Paragraph userDataParagraphSurname = new Paragraph(surname, userDataFont);
        userDataParagraphSurname.setSpacingAfter(5);
        return userDataParagraphSurname;
    }

    private Paragraph setParagraphWithBiggerSpace(String email, Font userDataFont) {
        Paragraph userDataParagraphEmail = new Paragraph(email, userDataFont);
        userDataParagraphEmail.setSpacingAfter(15);
        return userDataParagraphEmail;
    }

    private Paragraph setTableDescription(Font font) {
        font.setSize(10);
        String regardsText = "Your orders:";
        Paragraph tableDescription = new Paragraph(regardsText, font);
        tableDescription.setAlignment(Element.ALIGN_CENTER);
        tableDescription.setSpacingAfter(5);
        return tableDescription;
    }

    private PdfPTable setTableHeader(Font font) throws DocumentException {
        PdfPTable tableHeader = new PdfPTable(5);
        tableHeader.setWidthPercentage(100);
        tableHeader.setSpacingBefore(15);
        tableHeader.setSpacingAfter(0);

        float[] columnWidths = {1f, 1f, 1f, 1f, 1f};
        tableHeader.setWidths(columnWidths);

        tableHeader.addCell(setCellProperties(new PdfPCell(new Paragraph("Lp.", font))));
        tableHeader.addCell(setCellProperties(new PdfPCell(new Paragraph("Room number", font))));
        tableHeader.addCell(setCellProperties(new PdfPCell(new Paragraph("Start of booking", font))));
        tableHeader.addCell(setCellProperties(new PdfPCell(new Paragraph("End of booking", font))));
        tableHeader.addCell(setCellProperties(new PdfPCell(new Paragraph("is paid", font))));

        return tableHeader;
    }

    private PdfPTable setTableContent(Font font, String roomNumber, ReservationDto reservationDto, int lp) throws DocumentException {
        PdfPTable tableValue = new PdfPTable(5);
        tableValue.setWidthPercentage(100);
        tableValue.setSpacingBefore(0);

        float[] valueColumnsWidths = {1f, 1f, 1f, 1f, 1f};
        tableValue.setWidths(valueColumnsWidths);

        tableValue.addCell(setCellProperties(new PdfPCell(new Paragraph(String.valueOf(lp), font))));
        tableValue.addCell(setCellProperties(new PdfPCell(new Paragraph(roomNumber, font))));
        tableValue.addCell(setCellProperties(new PdfPCell(new Paragraph(reservationDto.getStartOfBooking().toString(), font))));
        tableValue.addCell(setCellProperties(new PdfPCell(new Paragraph(reservationDto.getEndOfBooking().toString(), font))));
        tableValue.addCell(setCellProperties(new PdfPCell(new Paragraph(reservationDto.getBookingStatusDto().getPaymentStatus().toString(), font))));

        return tableValue;
    }

    private static PdfPCell setCellProperties(PdfPCell pCell) {
        pCell.setBorderColor(BaseColor.BLACK);
        pCell.setBorderWidth(1);
        pCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pCell.setUseBorderPadding(true);
        return pCell;
    }

    private static Paragraph addClause() {
        String someText = """
                Within the meaning of civil law, this document constitutes a proof of making a reservation and contains its detailed information. Along with the presentation of the ID card, it constitutes the basis for accommodation on the basis of the reservations presented therein, in the rooms shown. 
                In the event of destruction of the document, loss or change of data contained in it, the hotel staff should be notified immediately.
                """;
        Paragraph text = new Paragraph(someText);
        text.setFirstLineIndent(25);
        text.setSpacingBefore(15);
        text.setSpacingAfter(25);
        return text;
    }

    private Paragraph addRegards(Font font) {
        font.setSize(10);
        String regardsText = "We wish you a pleasant stay";
        Paragraph regards = new Paragraph(regardsText, font);
        regards.setAlignment(Element.ALIGN_CENTER);
        regards.setSpacingAfter(25);
        return regards;
    }

    private Image addQrCode(UserDto userDto, List<ReservationDto> reservationDtos) throws BadElementException, EmailServiceException {
        String qrCodeMessageUser = "Name: " + userDto.getName() + " surname: " + userDto.getSurname() + " birthdate: " + userDto.getBirthDate() +
                " email: " + userDto.getEmail() + " city: " + userDto.getCity() + " address: " + userDto.getAddress() +
                " country: " + userDto.getCountry() + " phone: " + userDto.getPhone() + " zip-code: " + userDto.getZipCode();

        AtomicInteger counter = new AtomicInteger(0);
        String qrCodeMessageReservation = reservationDtos
                .stream()
                .map(reservationDtoLongEntry -> "\nRESERVATION " + counter.incrementAndGet() + ": room number: " + reservationDtoLongEntry.getRoomDto().getRoomNumber()
                + " start of booking: " + reservationDtoLongEntry.getStartOfBooking() + " end of booking " + reservationDtoLongEntry.getEndOfBooking() +
                        " is paid " + reservationDtoLongEntry.getBookingStatusDto().getPaymentStatus() + " reservation cost: " + reservationDtoLongEntry.getBookingStatusDto().getTotalAmountForReservation())
                .collect(Collectors.joining());

        String qrCodeMessage = qrCodeMessageUser + qrCodeMessageReservation;

        BarcodeQRCode my_code = new BarcodeQRCode(qrCodeMessage, 200, 200, null);
        Image qr_image = my_code.getImage();
        qr_image.setAlignment(Element.ALIGN_RIGHT);
        return qr_image;
    }
}

