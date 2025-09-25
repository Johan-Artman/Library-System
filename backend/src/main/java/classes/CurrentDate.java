package classes;

import java.time.LocalDate;

public class CurrentDate {
    private LocalDate currentDate;

    public CurrentDate() {

    }

    public LocalDate getCurrentDate() {

        if (currentDate == null) {
            throw new IllegalStateException("Current date has not been set.");
        }
        return currentDate;
    }

    public void setCurrentDate(LocalDate newDate) {
        if (newDate == null) {
            throw new IllegalArgumentException("New date cannot be null.");
        }
        this.currentDate = newDate;
    }
}
