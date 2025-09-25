# 📚 Library Management System

A full-stack library management system with Java Spring Boot backend and web frontend.

## 🏗️ Project Structure

```
Library-System/
├── backend/                    # Java Spring Boot REST API
│   ├── src/
│   │   ├── main/java/
│   │   │   ├── classes/        # Your existing business logic
│   │   │   ├── controllers/    # REST API controllers
│   │   │   ├── exceptions/     # Custom exceptions
│   │   │   ├── interfaces/     # Interfaces
│   │   │   └── LibrarySystemApplication.java  # Spring Boot main class
│   │   └── resources/
│   │       ├── application.properties
│   │       └── log4j2.xml
│   ├── pom.xml                 # Maven dependencies
│   ├── setup_sqlite.sql        # Database setup with sample data
│   └── library.db              # SQLite database
├── frontend/                   # Web application
│   └── index.html              # HTML frontend for testing API
└── README.md                   # This file
```

## 🚀 Quick Start

### Prerequisites
- ✅ Java 21 (installed)
- ✅ Maven 3.8+ (installed)
- ✅ Python 3 (for serving HTML frontend)

### Running the Application

1. **Start Backend (Java Spring Boot API):**
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   Backend runs on: http://localhost:8080

2. **Start Frontend:**
   ```bash
   cd frontend
   python3 -m http.server 3000
   ```
   Frontend runs on: http://localhost:3000

3. **Open your browser and go to:** http://localhost:3000

## 🔌 API Endpoints

The REST API provides the following endpoints:

### 📚 Books
- `GET /api/books/{isbn}` - Get book by ISBN
- `PUT /api/books/{isbn}/copies?copies=X` - Update book copies

### 👥 Members  
- `POST /api/members` - Add new member
- `GET /api/members/{memberId}` - Get member by ID
- `DELETE /api/members/{memberId}` - Remove member
- `GET /api/members/{memberId}/suspended` - Check if member is suspended

### 📖 Lending
- `POST /api/lending/lend?memberId=X&isbn=Y` - Lend book to member
- `POST /api/lending/return?memberId=X&isbn=Y` - Return book from member

## 🗄️ Database

The backend uses **SQLite** with your existing schema and sample data:

### Sample Books (with ISBN `9780743273565` highlighted)
- **The Great Gatsby** (ISBN: `9780743273565`) - 3 copies
- To Kill a Mockingbird (ISBN: 9780061120084) - 2 copies  
- 1984 (ISBN: 9780451524935) - 4 copies
- Pride and Prejudice (ISBN: 9780141439518) - 2 copies
- And more...

### Sample Members
- John Doe (ID: 1) - Undergraduate
- Jane Smith (ID: 2) - Postgraduate
- Bob Johnson (ID: 3) - PhD
- Alice Brown (ID: 4) - Teacher
- Charlie Davis (ID: 5) - Undergraduate

## 💻 Technology Stack

### Backend
- **Java 21** - Programming language
- **Spring Boot 2.7.18** - Web framework 
- **SQLite** - Database
- **Maven** - Build tool
- **Log4j2** - Logging

### Frontend  
- **HTML5** - Markup
- **JavaScript (ES6+)** - Frontend logic
- **CSS3** - Styling
- **Fetch API** - HTTP requests

## 🧪 Testing the API

The included HTML frontend provides a user-friendly interface to test all API functionality:

1. **Search Books** - Find books by ISBN (try `9780743273565`)
2. **Member Management** - Add, view, and manage library members
3. **Book Lending** - Lend and return books
4. **Real-time Results** - See API responses in real-time

## 🔄 How It Works

1. **Your Existing Business Logic** - All your original Java classes (`LibraryStoreManager`, `Member`, `Book`, etc.) remain unchanged
2. **REST API Layer** - Spring Boot controllers provide HTTP endpoints on top of your business logic
3. **Database Connection** - Uses your existing SQLite database and connection handling
4. **Web Interface** - HTML frontend communicates with the Java backend via REST API calls

## 📈 Next Steps: Angular Frontend

To create a full Angular frontend:

```bash
cd frontend
npx @angular/cli@latest new library-angular-app --routing --style=css
cd library-angular-app
ng serve
```

This will create a complete Angular application that can consume your REST API.

## 🌟 Benefits of This Architecture

- ✅ **Preserves existing code** - Your business logic remains untouched
- ✅ **Modern web interface** - Replace console input with web UI
- ✅ **API-first approach** - Can support mobile apps, other frontends
- ✅ **Scalable** - Easy to add new features and endpoints  
- ✅ **Testable** - Clear separation between frontend and backend
- ✅ **Maintainable** - Modern, industry-standard architecture

## 🛠️ Development

### Backend Development
```bash
cd backend
mvn spring-boot:run      # Run with hot reload
mvn test                 # Run tests
mvn clean package        # Build JAR
```

### Adding New API Endpoints
1. Add methods to existing controllers in `src/main/java/controllers/`
2. Use your existing `LibraryStoreManager` for business logic
3. Spring Boot will automatically expose the endpoints

### Database Changes
- Modify `setup_sqlite.sql` for schema changes
- Your existing database connection handling will work automatically

## 🎯 Key Features Working

- ✅ **Book Search** - Search books by ISBN using your existing `BookStore`
- ✅ **Member Management** - Add/remove members using your existing `MemberStore`  
- ✅ **Lending System** - Lend/return books using your existing `LendingStore`
- ✅ **Exception Handling** - Your custom exceptions are handled properly
- ✅ **Database Integration** - Uses your existing SQLite database
- ✅ **Logging** - Your Log4j2 configuration continues to work

Your library system is now web-enabled while preserving all existing functionality! 🎉