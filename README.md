# transport-company-java

Java project containing several applications for a transport company with a graphical interface (GUI) developed in JavaFX.

- Application with a layered architecture: data access layer, persistence layer, business layer and presentation layer ([FlightCompanyApplication](https://github.com/Iri25/mpp-proiect-repository-Java-Iri25/tree/main/FlightCompanyApplication)).
  
- Application that implements the networking part using sockets and threads and notifies scientists when the data is updated ([FlightCompanyNetworking](https://github.com/Iri25/mpp-proiect-repository-Java-Iri25/tree/main/FlightCompanyNetworking)).
  
- Application that implements REST services ([FlightCompanyREST](https://github.com/Iri25/mpp-proiect-repository-Java-Iri25/tree/main/FlightCompanyREST)).
  
- Web application with React that uses REST services ([FlightCompanyREACT](https://github.com/Iri25/mpp-proiect-repository-Java-Iri25/tree/main/FlightCompanyREACT)).
  
- Client-server application developed with the Hibernate ORM tool to implement persistence.([FlightCompanyORM](https://github.com/Iri25/mpp-proiect-repository-Java-Iri25/tree/main/FlightCompanyORM)).
  
- Client-server application developed with Spring Remoting ([FlightComanySpringRemoting](https://github.com/Iri25/mpp-proiect-repository-Java-Iri25/tree/main/FlightComanySpringRemoting)).
  
- Client-server application developed with Protocol Buffers v3 ([FlightCompanyProtobuffv3](https://github.com/Iri25/mpp-proiect-repository-Java-Iri25/tree/main/FlightCompanyProtobuffv3)).

## Requirements:
Several travel agencies use a airline to transport customers to different destinations tourist. Agencies use a software system to buy tickets for tourists. Employees of agencies tourism use a desktop application with the following features:
1. Login - After successful authentication, a new window opens in which the information is displayed about flights (destination, date and time of departure, airport and available seat number).

2. Search - After successful authentication, the employee can search for a flight by entering the destination and date departure. The application will display in another list/other table/etc. all flights to that destination, departure time and the number of seats available.

3. Purchase - The employee can buy customer tickets for a specific destination, on a certain date, respectively departure time. When buying a ticket, the employee enters the customer's name, name tourists, customer address and number of seats. After buying a ticket, all employees of the agencies see the updated list of flights and the number of seats available. If for a certain flight there are no more seats available, that flight will no longer appear in the list of flights displayed on the interface.

4. Logout
