# NGO Connect - Fullstack Web Application

NGO Connect is a comprehensive platform that bridges NGOs, donors, and volunteers using modern web technologies. Built with Java Spring Boot backend and Thymeleaf frontend, it provides a robust solution for managing NGO operations, donations, campaigns, and volunteer activities.

## Features

### Core Functionality
- **User Management**: Multi-role authentication system (Admin, NGO, Donor, Volunteer)
- **NGO Management**: Complete NGO profile management with verification system
- **Campaign Management**: Create, manage, and track fundraising campaigns
- **Donation System**: Secure donation processing with transparency tracking
- **Event Management**: Organize and manage volunteer events
- **Volunteer System**: Volunteer registration and event participation
- **Impact Reporting**: Transparent reporting of fund utilization
- **Dashboard System**: Role-based dashboards with analytics

### Technical Features
- **Responsive Design**: Mobile-first responsive web design
- **Security**: Spring Security with role-based access control
- **Database**: JPA/Hibernate with H2 database (easily configurable for MySQL/PostgreSQL)
- **File Upload**: Support for image and document uploads
- **Email Integration**: Email notifications and communications
- **Modern UI**: Bootstrap 5 with custom CSS and animations
- **Interactive Frontend**: JavaScript with jQuery for enhanced user experience

## Technology Stack

### Backend
- **Java 17+**
- **Spring Boot 3.2.0**
- **Spring Security 6.2.0**
- **Spring Data JPA**
- **H2 Database** (development) / MySQL/PostgreSQL (production)
- **Maven** (build tool)
- **JWT** for authentication tokens
- **JavaMail** for email functionality

### Frontend
- **Thymeleaf** (template engine)
- **HTML5 & CSS3**
- **Bootstrap 5.3.0**
- **JavaScript & jQuery**
- **Font Awesome** (icons)
- **Responsive Design**

## Project Structure

```
ngo-connect/
├── src/
│   ├── main/
│   │   ├── java/com/ngoconnect/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── model/          # JPA entities
│   │   │   ├── repository/     # Data repositories
│   │   │   ├── service/        # Business logic
│   │   │   ├── util/           # Utility classes
│   │   │   └── NgoConnectApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/        # Stylesheets
│   │       │   ├── js/         # JavaScript files
│   │       │   └── images/     # Static images
│   │       ├── templates/
│   │       │   ├── layout/     # Layout templates
│   │       │   ├── auth/       # Authentication pages
│   │       │   ├── public/     # Public pages
│   │       │   └── dashboard/  # Dashboard pages
│   │       └── application.properties
├── pom.xml
└── README.md
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Git

### Installation Steps

1. **Clone/Extract the Project**
   ```bash
   # If using Git
   git clone <repository-url>
   cd ngo-connect
   
   # If using ZIP file
   unzip ngo-connect.zip
   cd ngo-connect
   ```

2. **Build the Application**
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the Application**
   - Open your browser and navigate to: `http://localhost:8080`
   - The application will start with an H2 in-memory database

### Database Configuration

#### Development (H2 Database)
The application comes pre-configured with H2 database for development. No additional setup required.

#### Production (MySQL/PostgreSQL)
To use MySQL or PostgreSQL in production:

1. **Update `application.properties`:**
   ```properties
   # For MySQL
   spring.datasource.url=jdbc:mysql://localhost:3306/ngoconnect
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
   
   # For PostgreSQL
   spring.datasource.url=jdbc:postgresql://localhost:5432/ngoconnect
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   ```

2. **Add database dependency to `pom.xml`:**
   ```xml
   <!-- For MySQL -->
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <scope>runtime</scope>
   </dependency>
   
   <!-- For PostgreSQL -->
   <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
       <scope>runtime</scope>
   </dependency>
   ```

### Email Configuration

Update the email settings in `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### File Upload Configuration

Configure file upload settings:

```properties
# File upload settings
app.upload.dir=/path/to/upload/directory
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## User Roles and Access

### Admin
- Complete system access
- User management
- NGO verification
- System analytics
- Content moderation

### NGO
- NGO profile management
- Campaign creation and management
- Event organization
- Donation tracking
- Impact reporting

### Donor
- Browse NGOs and campaigns
- Make donations
- Track donation history
- View impact reports
- Manage profile

### Volunteer
- Browse events
- Register for events
- Track volunteer history
- Manage profile

## Default Login Credentials

After first run, you can create accounts through the registration page. The system supports:
- Email-based authentication
- Role-based access control
- Profile management

## API Endpoints

### Authentication
- `POST /auth/login` - User login
- `POST /auth/register` - User registration
- `GET /auth/logout` - User logout

### Public Endpoints
- `GET /` - Home page
- `GET /ngos` - NGO directory
- `GET /campaigns` - Campaign listing
- `GET /events` - Event listing

### Dashboard Endpoints
- `GET /dashboard/admin` - Admin dashboard
- `GET /dashboard/ngo` - NGO dashboard
- `GET /dashboard/donor` - Donor dashboard
- `GET /dashboard/volunteer` - Volunteer dashboard

## Development

### Running in Development Mode
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Building for Production
```bash
mvn clean package -Pprod
```

### Running Tests
```bash
mvn test
```

## Deployment

### JAR Deployment
```bash
mvn clean package
java -jar target/ngo-connect-1.0.0.jar
```

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/ngo-connect-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## Security Features

- **Authentication**: Spring Security with session management
- **Authorization**: Role-based access control
- **Password Security**: BCrypt password encoding
- **CSRF Protection**: Enabled for form submissions
- **Input Validation**: Server-side validation with Bean Validation
- **File Upload Security**: File type and size validation

## Performance Features

- **Database Optimization**: JPA query optimization
- **Caching**: Spring Cache abstraction
- **Lazy Loading**: Efficient data loading
- **Responsive Design**: Mobile-optimized interface
- **Asset Optimization**: Minified CSS and JS

## Browser Support

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+
- Mobile browsers (iOS Safari, Chrome Mobile)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation

## Changelog

### Version 1.0.0
- Initial release
- Complete NGO management system
- Multi-role authentication
- Campaign and donation management
- Event and volunteer system
- Responsive web interface
- Admin dashboard and analytics

---

**NGO Connect** - Bridging Help with Technology

