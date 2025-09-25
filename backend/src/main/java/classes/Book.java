package classes;

import interfaces.IBook;

public class Book implements IBook{



        private long isbn;
        private String title;
        private int availableCopies;

        public Book(long isbn, String title, int availableCopies) {
            this.isbn = isbn;
            this.title = title;
            this.availableCopies = availableCopies;
        }

        @Override
        public long getISBN() {

            return isbn;

        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public int getAvailableCopies() {
            return availableCopies;
        }

    }

