# 📚 Library Management System

A modern, full-stack library management system built with Java Spring Boot backend and responsive web frontend. This system provides comprehensive library operations including book management, member registration, and lending services through both REST API and web interface.

## 📋 Table of Contents
- [Features](#-features)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [API Documentation](#-api-documentation)
- [Technology Stack](#-technology-stack)
- [Project Structure](#-project-structure)
- [Development](#-development)
- [Database](#-database)
- [Testing](#-testing)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)

## ✨ Features

### Core Library Functions
- **Book Management**: Search, add, and manage book inventory
- **Member Management**: Register and manage library members
- **Lending System**: Complete book lending and return workflow
- **Member Types**: Support for different member categories (Undergraduate, Postgraduate, PhD, Teacher)
- **Suspension System**: Automatic member suspension for overdue returns

### Technical Features
- **RESTful API**: Clean, well-documented REST endpoints
- **Web Interface**: Responsive HTML/CSS/JavaScript frontend
- **Real-time Testing**: Interactive API testing through web UI
- **Database Integration**: SQLite database with sample data
- **CORS Support**: Cross-origin resource sharing for frontend integration
- **Comprehensive Logging**: Log4j2 integration for debugging and monitoring
- **Error Handling**: Robust exception handling with meaningful error messages

## 🏗️ Architecture

The system follows a modern three-tier architecture:

1. **Presentation Layer** (Frontend): HTML/CSS/JavaScript web interface
2. **Business Logic Layer** (Controllers): Spring Boot REST controllers
3. **Data Access Layer** (Backend Services): Java classes with SQLite integration

This design ensures separation of concerns, making the system maintainable, testable, and scalable.

## 🛠️ Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher ([Download OpenJDK](https://openjdk.org/install/))
- **Apache Maven 3.8+** ([Download Maven](https://maven.apache.org/download.cgi))
- **Git** for version control
- **Python 3** (for serving the frontend during development)
- **Modern web browser** (Chrome, Firefox, Safari, or Edge)

### Verify Installation
```bash
java -version    # Should show Java 17+
mvn -version     # Should show Maven 3.8+
python3 --version # Should show Python 3.x
```

## 🏗️ Project Structure

```
Library-System/
├── backend/                    # Java Spring Boot REST API
│   ├── src/
│   │   ├── main/java/
│   │   │   ├── classes/        # Core business logic (LibraryStoreManager, Member, Book)
│   │   │   ├── controllers/    # REST API controllers (BookController, MemberController)
│   │   │   ├── config/         # Configuration classes (CORS, Spring Boot)
│   │   │   ├── exceptions/     # Custom exceptions
│   │   │   ├── interfaces/     # Java interfaces
│   │   │   └── LibrarySystemApplication.java  # Spring Boot main class
│   │   ├── test/java/          # Unit and integration tests
│   │   └── resources/
│   │       ├── application.properties  # Spring Boot configuration
│   │       └── log4j2.xml             # Logging configuration
│   ├── pom.xml                 # Maven dependencies and build configuration
│   ├── setup_sqlite.sql        # Database schema and sample data
│   ├── library.db              # SQLite database file
│   └── perma_banned_members.txt # Permanently banned members list
├── frontend/                   # Web application
│   └── index.html              # Single-page application for API testing
└── README.md                   # Project documentation (this file)
```

## 🚀 Quick Start

### Step 1: Clone the Repository
```bash
git clone https://github.com/Johan-Artman/Library-System.git
cd Library-System
```

### Step 2: Start the Backend API
```bash
cd backend
mvn clean install          # Download dependencies and build
mvn spring-boot:run        # Start the Spring Boot application
```
✅ Backend server will be running at: **http://localhost:8080**

### Step 3: Start the Frontend
Open a new terminal window:
```bash
cd frontend
python3 -m http.server 3000    # Start simple HTTP server
# Alternative: python -m http.server 3000 (Python 2.x)
```
✅ Frontend will be available at: **http://localhost:3000**

### Step 4: Test the System
1. Open your browser and navigate to **http://localhost:3000**
2. Try the sample operations:
   - Search for book ISBN: `9780743273565` (The Great Gatsby)
   - Add a new member
   - Lend and return books

## 📡 API Documentation

The REST API provides comprehensive endpoints for all library operations:

### 📚 Book Management
| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| `GET` | `/api/books/{isbn}` | Retrieve book details by ISBN | `isbn` (path parameter) |
| `PUT` | `/api/books/{isbn}/copies` | Update book copy count | `isbn` (path), `copies` (query) |

**Example:**
```bash
# Get book details
curl http://localhost:8080/api/books/9780743273565

# Update book copies
curl -X PUT http://localhost:8080/api/books/9780743273565/copies?copies=5
```

### 👥 Member Management
| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|---------------|
| `POST` | `/api/members` | Register new member | `{firstname, lastname, memberType, socSecNr}` |
| `GET` | `/api/members/{memberId}` | Get member details | N/A |
| `DELETE` | `/api/members/{memberId}` | Remove member | N/A |
| `GET` | `/api/members/{memberId}/suspended` | Check suspension status | N/A |

**Example:**
```bash
# Register new member
curl -X POST http://localhost:8080/api/members \
  -H "Content-Type: application/json" \
  -d '{"firstname":"John","lastname":"Doe","memberType":"UNDERGRADUATE","socSecNr":"123456789"}'

# Get member details
curl http://localhost:8080/api/members/1
```

### 📖 Lending Operations
| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| `POST` | `/api/lending/lend` | Lend book to member | `memberId` & `isbn` (query) |
| `POST` | `/api/lending/return` | Return book from member | `memberId` & `isbn` (query) |
| `GET` | `/api/lending/member/{memberId}` | Get member's lending history | `memberId` (path) |

**Example:**
```bash
# Lend a book
curl -X POST "http://localhost:8080/api/lending/lend?memberId=1&isbn=9780743273565"

# Return a book
curl -X POST "http://localhost:8080/api/lending/return?memberId=1&isbn=9780743273565"
```

## 💻 Technology Stack

### Backend Technologies
| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17+ | Core programming language |
| **Spring Boot** | 2.7.18 | Web framework and dependency injection |
| **Spring Web** | 2.7.18 | REST API development |
| **Maven** | 3.8+ | Build automation and dependency management |
| **SQLite** | 3.42.0 | Lightweight embedded database |
| **Log4j2** | 2.17.1 | Logging framework |
| **JUnit 5** | 5.7.0 | Unit testing framework |
| **Mockito** | 5.3.1 | Mocking framework for tests |

### Frontend Technologies
| Technology | Purpose |
|------------|---------|
| **HTML5** | Semantic markup and structure |
| **CSS3** | Styling and responsive design |
| **JavaScript (ES6+)** | Interactive functionality |
| **Fetch API** | HTTP requests to backend |

### Development Tools
- **Git** - Version control
- **Maven** - Build automation
- **Spring Boot DevTools** - Hot reload during development

## 🗄️ Database

The system uses **SQLite** as an embedded database with pre-loaded sample data for immediate testing.

### Database Schema
- **Books Table**: ISBN, title, author, copies available
- **Members Table**: Member ID, name, type, contact information
- **Lending Records**: Transaction history, due dates, return status

### Sample Data

#### 📖 Books Available
| Title | ISBN | Author | Copies | Status |
|-------|------|--------|--------|--------|
| **The Great Gatsby** | `9780743273565` | F. Scott Fitzgerald | 3 | ✅ Available |
| To Kill a Mockingbird | `9780061120084` | Harper Lee | 2 | ✅ Available |
| 1984 | `9780451524935` | George Orwell | 4 | ✅ Available |
| Pride and Prejudice | `9780141439518` | Jane Austen | 2 | ✅ Available |

#### 👥 Registered Members
| ID | Name | Type | Status |
|----|------|------|--------|
| 1 | John Doe | Undergraduate | ✅ Active |
| 2 | Jane Smith | Postgraduate | ✅ Active |
| 3 | Bob Johnson | PhD | ✅ Active |
| 4 | Alice Brown | Teacher | ✅ Active |
| 5 | Charlie Davis | Undergraduate | ✅ Active |

### Database Operations
```bash
# View database contents (optional)
cd backend
sqlite3 library.db
.tables                 # List all tables
SELECT * FROM books;    # View all books
SELECT * FROM members;  # View all members
.exit                   # Exit SQLite
```

## 🧪 Testing

### Web Interface Testing
The included HTML frontend provides an intuitive interface for testing all API functionality:

1. **📚 Book Search**
   - Enter ISBN `9780743273565` to find "The Great Gatsby"
   - View real-time API responses
   - Test error handling with invalid ISBNs

2. **👥 Member Operations**
   - Register new library members
   - Select member types (Undergraduate, Postgraduate, PhD, Teacher)
   - View member details and suspension status

3. **📖 Lending System**
   - Lend books to registered members
   - Process book returns
   - Track lending history

4. **🔍 Real-time Results**
   - All operations display live API responses
   - JSON formatting for easy debugging
   - Color-coded success/error indicators

### Automated Testing
Run the complete test suite:
```bash
cd backend
mvn test                    # Run all unit tests
mvn test -Dtest=*Controller # Run controller tests only
mvn clean verify           # Run tests with code coverage
```

### API Testing with cURL
Quick API tests from command line:
```bash
# Test book search
curl http://localhost:8080/api/books/9780743273565

# Test member registration
curl -X POST http://localhost:8080/api/members \
  -H "Content-Type: application/json" \
  -d '{"firstname":"Test","lastname":"User","memberType":"UNDERGRADUATE","socSecNr":"123456789"}'

# Test book lending
curl -X POST "http://localhost:8080/api/lending/lend?memberId=1&isbn=9780743273565"
```

## 🛠️ Development

### Local Development Setup

#### Backend Development
```bash
cd backend
mvn spring-boot:run                    # Start with hot reload
mvn spring-boot:run -Dspring-boot.run.profiles=dev  # Development profile
```

#### Development Commands
```bash
mvn clean compile                      # Compile source code
mvn test                              # Run unit tests
mvn clean package                     # Build JAR file
mvn spring-boot:run -Dspring.profiles.active=dev  # Run with dev profile
```

#### Hot Reload
The application supports hot reload for rapid development:
- Java classes automatically recompile when saved
- Configuration changes are applied without restart
- Frontend changes are immediately visible with browser refresh

### Adding New Features

#### 1. Adding New API Endpoints
```java
@RestController
@RequestMapping("/api/your-endpoint")
public class YourController {
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        // Your implementation
        return ResponseEntity.ok(result);
    }
}
```

#### 2. Database Schema Changes
1. Modify `setup_sqlite.sql` for schema updates
2. Delete existing `library.db` 
3. Restart application to regenerate database with new schema

#### 3. Frontend Enhancements
- Edit `frontend/index.html` for UI changes
- Add new JavaScript functions for API calls
- Update CSS for styling improvements

### Code Quality
```bash
mvn checkstyle:check                   # Code style validation
mvn spotbugs:check                     # Static code analysis
mvn jacoco:report                      # Generate code coverage report
```

## 🔧 Troubleshooting

### Common Issues and Solutions

#### Backend Won't Start
**Problem**: `Port 8080 already in use`
```bash
# Find process using port 8080
lsof -i :8080
# Kill the process
kill -9 <PID>
# Or use different port
mvn spring-boot:run -Dserver.port=8081
```

**Problem**: `Java version mismatch`
```bash
# Check Java version
java -version
# Ensure Java 17+ is installed
# Update JAVA_HOME if necessary
export JAVA_HOME=/path/to/java17
```

#### Database Issues
**Problem**: `Database locked or corrupted`
```bash
cd backend
rm library.db                         # Remove existing database
mvn spring-boot:run                   # Restart to regenerate
```

**Problem**: `SQLite driver not found`
```bash
mvn clean install                     # Reinstall dependencies
```

#### Frontend Connection Issues
**Problem**: `CORS errors in browser console`
- Check that backend is running on port 8080
- Verify CORS configuration in `application.properties`
- Clear browser cache and cookies

**Problem**: `API calls fail with 404`
- Confirm backend server is running
- Check API endpoint URLs match documentation
- Verify request methods (GET, POST, etc.)

#### Build Issues
**Problem**: `Maven dependencies not downloading`
```bash
mvn clean install -U                  # Force update dependencies
rm -rf ~/.m2/repository               # Clear local Maven cache
```

### Debug Mode
Enable detailed logging for troubleshooting:
```bash
mvn spring-boot:run -Dlogging.level.root=DEBUG
```

### Performance Issues
Monitor application performance:
```bash
# View active threads and memory usage
jps                                   # Find Java process ID
jstack <PID>                         # Thread dump
jstat -gc <PID>                      # Garbage collection stats
```

## 📊 Advanced Configuration

### Environment Profiles
Configure different environments using Spring profiles:

**Development** (`application-dev.properties`):
```properties
logging.level.root=DEBUG
server.port=8080
```

**Production** (`application-prod.properties`):
```properties
logging.level.root=WARN
server.port=80
```

### Custom Database Location
```properties
# In application.properties
app.database.path=/custom/path/library.db
```

### CORS Configuration
Modify allowed origins in `CorsConfig.java`:
```java
.allowedOrigins("http://localhost:3000", "https://yourdomain.com")
```

## 🤝 Contributing

We welcome contributions to improve the Library Management System! Here's how you can contribute:

### 🚀 Getting Started
1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/your-username/Library-System.git
   cd Library-System
   ```
3. **Create a feature branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```

### 📝 Development Guidelines

#### Code Style
- Follow Java naming conventions (camelCase for variables, PascalCase for classes)
- Write meaningful commit messages
- Include JavaDoc comments for public methods
- Maintain consistent indentation (4 spaces)

#### Testing Requirements
- Write unit tests for new features
- Ensure existing tests pass: `mvn test`
- Aim for at least 80% code coverage
- Test both success and error scenarios

#### Pull Request Process
1. **Update documentation** if needed
2. **Add tests** for new functionality
3. **Ensure build passes**: `mvn clean install`
4. **Submit pull request** with clear description
5. **Address review feedback** promptly

### 🐛 Bug Reports
When reporting bugs, please include:
- Operating system and Java version
- Steps to reproduce the issue
- Expected vs actual behavior
- Error messages and stack traces
- Screenshots if applicable

### 💡 Feature Requests
For new features, please:
- Check existing issues first
- Describe the use case clearly
- Provide mockups or examples if helpful
- Consider backward compatibility

### 📚 Areas for Contribution
- **Frontend Enhancement**: Improve UI/UX design
- **API Documentation**: Add OpenAPI/Swagger integration
- **Testing**: Increase test coverage
- **Performance**: Database query optimization
- **Security**: Input validation and authentication
- **Mobile Support**: Responsive design improvements

## 📈 Roadmap

### Upcoming Features
- [ ] **Authentication System**: User login and role-based access
- [ ] **Angular Frontend**: Modern SPA implementation
- [ ] **Mobile App**: React Native or Flutter app
- [ ] **Advanced Search**: Full-text search capabilities
- [ ] **Reporting**: Library usage statistics and reports
- [ ] **Email Notifications**: Overdue book reminders
- [ ] **Barcode Integration**: Physical book scanning support

### System Improvements
- [ ] **Docker Support**: Containerized deployment
- [ ] **CI/CD Pipeline**: Automated testing and deployment
- [ ] **Performance Monitoring**: Application metrics and monitoring
- [ ] **API Documentation**: Interactive Swagger UI
- [ ] **Caching**: Redis integration for improved performance
- [ ] **Database Migration**: PostgreSQL support for production

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- SQLite for the lightweight database solution
- All contributors who help improve this project

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/Johan-Artman/Library-System/issues)
- **Discussions**: [GitHub Discussions](https://github.com/Johan-Artman/Library-System/discussions)
- **Wiki**: [Project Wiki](https://github.com/Johan-Artman/Library-System/wiki)

---

**Built with ❤️ by [Johan Artman](https://github.com/Johan-Artman)**

*Happy coding! 🎉*