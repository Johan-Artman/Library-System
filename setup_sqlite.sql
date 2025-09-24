-- SQLite database setup for Library System

-- Create books table
CREATE TABLE IF NOT EXISTS books (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titel TEXT NOT NULL,
    isbn INTEGER NOT NULL UNIQUE,
    availableCopies INTEGER DEFAULT 1
);

-- Create member table  
CREATE TABLE IF NOT EXISTS member (
    member_id INTEGER PRIMARY KEY,
    firstname TEXT,
    lastname TEXT,
    member_type INTEGER,
    soc_sec_nr TEXT
);

-- Create member_status table
CREATE TABLE IF NOT EXISTS member_status (
    member_id INTEGER PRIMARY KEY,
    late_book_count INTEGER DEFAULT 0,
    suspend_count INTEGER DEFAULT 0,
    isSuspended INTEGER DEFAULT 0,
    datestart DATE,
    dateend DATE,
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

-- Create borrowing_table
CREATE TABLE IF NOT EXISTS borrowing_table (
    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
    member_id INTEGER,
    isbn INTEGER,
    lane_datum DATE,
    utlanad INTEGER DEFAULT 1,
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (isbn) REFERENCES books(isbn)
);

-- Insert sample books
INSERT OR IGNORE INTO books (titel, isbn, availableCopies) VALUES 
('The Great Gatsby', 9780743273565, 3),
('To Kill a Mockingbird', 9780061120084, 2),
('1984', 9780451524935, 4),
('Pride and Prejudice', 9780141439518, 2),
('The Catcher in the Rye', 9780316769174, 1),
('Dune', 9780441172719, 2),
('The Lord of the Rings', 9780544003415, 3),
('Harry Potter and the Sorcerers Stone', 9780439708180, 5);

-- Insert sample members (member_type: 1=Undergraduate, 2=Postgraduate, 3=PHD, 4=Teacher)
INSERT OR IGNORE INTO member (member_id, firstname, lastname, member_type, soc_sec_nr) VALUES 
(1, 'John', 'Doe', 1, '199001011234'),
(2, 'Jane', 'Smith', 2, '199502152345'),
(3, 'Bob', 'Johnson', 3, '199803203456'),
(4, 'Alice', 'Brown', 4, '198512054567'),
(5, 'Charlie', 'Davis', 1, '200006106789');

-- Initialize member status for sample members
INSERT OR IGNORE INTO member_status (member_id) VALUES 
(1), (2), (3), (4), (5);