package classes;

import interfaces.IBook;

public class Book implements IBook{

        private long isbn;
        private String title;
        private int availableCopies;
        private String shelfLocation;
        private int floorLevel;

        public Book(long isbn, String title, int availableCopies) {
            this.isbn = isbn;
            this.title = title;
            this.availableCopies = availableCopies;
            this.shelfLocation = "General";
            this.floorLevel = 1;
        }

        public Book(long isbn, String title, int availableCopies, String shelfLocation, int floorLevel) {
            this.isbn = isbn;
            this.title = title;
            this.availableCopies = availableCopies;
            this.shelfLocation = shelfLocation;
            this.floorLevel = floorLevel;
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

        // Location getters and setters
        public String getShelfLocation() {
            return shelfLocation;
        }

        public void setShelfLocation(String shelfLocation) {
            this.shelfLocation = shelfLocation;
        }

        public int getFloorLevel() {
            return floorLevel;
        }

        public void setFloorLevel(int floorLevel) {
            this.floorLevel = floorLevel;
        }

        public void setAvailableCopies(int availableCopies) {
            this.availableCopies = availableCopies;
        }

    }

