package bartos.lukasz.bookingservice.application.service.email;

import org.springframework.stereotype.Service;

import static j2html.TagCreator.*;

@Service
public class EmailHtmlRegistrationContentService {

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
                        title("Registration")
                ),
                body(
                        div(attrs(".email-page"),
                                div(attrs(".container"),
                                        div(attrs(".container-header"),
                                                h1("Pacific Hotel Welcome")),

                                        div(attrs(".container-body-one"),
                                                p("""
                                                        Thank you for registering.
                                                                                                                
                                                        We would like to kindly inform you that all personal data entered 
                                                        are protected by us in accordance with the rules of the regulations 
                                                        available on our website.
                                                        """)),
                                        hr(),

                                        div(attrs(".container-body-two"),
                                                p(
                                                        """
                                                                Registration greatly simplifies the use of our website,
                                                                thanks to this you can: 
                                                                """
                                                ),
                                                ul(
                                                        li("""
                                                                browse our offer and current promotions
                                                                """),
                                                        li("""
                                                                see the price list of services
                                                                """),
                                                        li("""
                                                                check your account balance and the amount due
                                                                """),
                                                        li("""
                                                                browse the menu of our restaurant and bar
                                                                """),
                                                        li("""
                                                                see the attractions and events of our hotel
                                                                """),
                                                        li("""
                                                                manage your bookings
                                                                """),
                                                        li("""
                                                                contact service quickly
                                                                """),
                                                        li("""
                                                                evaluate the quality of our services
                                                                """),
                                                        li("""
                                                                order additional services
                                                                """),
                                                        li("""
                                                               and many others
                                                                """)
                                                )),
                                        hr(),

                                        div(attrs(".container-footer"),
                                                p("""
                                                        If you have any questions, you can use the form on the website 
                                                        to quickly contact the hotel staff.
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
