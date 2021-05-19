package bartos.lukasz.bookingservice.application.service.email;

import org.springframework.stereotype.Service;

import static j2html.TagCreator.*;

@Service
public class EmailHtmlReservationContentService {

    public String renderHtmlContent() {
        return html(
                style("""
                        .email-page { background-color: #AABABA; display: flex; justify-content: center; }
                        .container { width: 600px; border: 1px solid black; background-color: white; padding: 25px; }
                        .container-header { font-size: 25px; text-align: center; text-decoration: underline; }
                        .container-body-one { font-size: 25px; text-indent: 20px; }
                        .container-body-two { font-size: 25px; text-indent: 20px; }
                        .container-footer { font-size: 25px; text-indent: 20px; }
                        """
                ),
                head(
                        title("Order")
                ),
                body(
                        div(attrs(".email-page"),
                                div(attrs(".container"),
                                        div(attrs(".container-header"),
                                                h1("Pacific Hotel Welcome")),

                                        div(attrs(".container-body-one"),
                                                p("""
                                                        Thank you for making a reservation at our hotel.
                                                        We are happy that you will be our guest.
                                                        During your stay, a lot of attractions and service of the highest standard await you.
                                                        We will make every effort to make your stay in our hotel a very enjoyable one.
                                                        """)),
                                        hr(),

                                        div(attrs(".container-body-two"),
                                                p(
                                                        """
                                                                In the attachment, we send a proof of registration in pdf form with a QR code. 
                                                                Printing the document will allow for more efficient verification of your booking.
                                                                """
                                                )),
                                        hr(),

                                        div(attrs(".container-footer"),
                                                p("""
                                                        Thank you for your trust in our hotel
                                                        """),
                                                hr(),
                                                p("Sincerely"),
                                                p("Pacific Hotel team")),

                                        div(attrs(".container-footer-img"),
                                                img().withSrc("https://www.kajware.pl/wp-content/uploads/2015/07/Booking-com-Logo-EPS-vector-image.png")))
                        ))
        ).renderFormatted();
    }
}
