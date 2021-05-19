Booking-service is a simple application to which provides functionality 
for both, customers and hotel staff. The app will soon be available on aws.

Application generates startup data, you can use them to test the application:
- admin1: 
   login: admin1
   password: password

- admin2:
  login: admin2
  password: password

- user1:
  login: user1
  password: password

- user2:
  login: user2
  password: password 

or you can register your own account. 

Booking-service still requires many corrections and advantages, is still being developed, 
and this is current state.

Application allows clients to: 
1) creating own account, logging in and updating
2) browsing and filtering available rooms
3) making a reservation and issuing a review for the hotel
4) contact with the hotel staff through chat, for logged in and not logged in users.
5) application sends e-mails confirming the booking and account registration

The application backend was written in Java 15 using Spring framework and Spring Boot.
The application frontend was written in TypeScript with Angular.
Communication with database was implemented by Hibernate framework and Spring Boot Data.
I am using mysql in the application.
I use WebSocket for asynchronous messaging with STOMP. 
I also decided to use the Quartz library, and the security of the 
application was based on JSON Web Token.

Booking-service is being developed and will soon be expanded with the following functions:
1) the application will be uploaded to ec2 server on aws and frontend to s3. 
   The application will be implemented using the load balancer and autoscaling mechanism.
2) add swagger and flyway implementation.
3) chat security will be added and the czat broker will be 
   changed to rabbitmq broker started from docker.
4) writing unit tests.
5) development of the application frontend, creation of an administrator 
   panel to manage application services for hotel service.
6) expanding the possibilities of sorting and filtering rooms, adding pagination.

Thanks for your time.