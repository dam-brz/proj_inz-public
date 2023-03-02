package com.projekt.zaliczeniowy.penny_pincher.dao;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.projekt.zaliczeniowy.penny_pincher.converter.type.BigDecimalConverter;
import com.projekt.zaliczeniowy.penny_pincher.converter.type.TimestampConverter;
import com.projekt.zaliczeniowy.penny_pincher.model.Category;
import com.projekt.zaliczeniowy.penny_pincher.model.Expense;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseComponent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Expense.class, Category.class, ExpenseComponent.class}, version = 1, exportSchema = false)
@TypeConverters({TimestampConverter.class, BigDecimalConverter.class})
public abstract class PennypincherDatabase extends RoomDatabase {

    abstract ExpenseDao expenseDao();

    private static volatile PennypincherDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PennypincherDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PennypincherDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(
                                    context.getApplicationContext(),
                                    PennypincherDatabase.class,
                                    "expense_database")
                            .addCallback(addSampleDataExpenseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static final PennypincherDatabase.Callback addSampleDataExpenseCallback = new PennypincherDatabase.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            db.execSQL("DELETE FROM categories");
            db.execSQL("DELETE FROM expense_components");
            db.execSQL("DELETE FROM expenses");

            if (Locale.getDefault().getLanguage().equals("pl")) {
                db.execSQL("insert into categories(category_id, category_name) \n" +
                        "values(1, 'Wybierz kategorię'), (2, 'Inne'),\n" +
                        " (3, 'Samochód'),\n" +
                        " (4, 'Dom'),\n" +
                        " (5, 'Zainteresowania')");

                db.execSQL("insert into expenses(expense_id, category_id, expense_name, expense_date, expense_total_value, settled) \n" +
                        "--czerwiec\n" +
                        "values (1, 2,'Biwak', '1654984800000', 250, 1), -- 50, 60, 80, 60 \n" +
                        "(2, 2,'Mandat', '1654812000000', 150, 0), --150\n" +
                        "(3, 2,'Bilet miesięczny', '1654984800000', 90, 1), --90\n" +
                        "(4, 2,'Kosmetyki', '1654380000000', 250, 1), --150, 20, 30, 50\n" +
                        "(5, 2,'Zakup ubrań', '1655762400000', 600, 1), --250, 50, 200, 100\n" +
                        "(6, 3,'Opłaty', '1654984800000', 650, 1), --150\n" +
                        "(7, 3,'Naprawa', '1654207200000', 700, 1), -- 450, 250\n" +
                        "(8, 4,'Rachunki', '1654380000000', 1600, 1), --850, 150, 150, 450 \n" +
                        "(9, 4,'Środku czystości', '1655848800000', 150, 1), --30, 50, 20, 25, 25 \n" +
                        "(10, 4,'Meble', '1656453600000', 4500, 1), --3500, 1500\n" +
                        "(11, 5,'Siłownia', '1654207200000', 100, 1), --100\n" +
                        "(12, 5,'Rower', '1655244000000', 150, 1), --50, 50, 50\n" +
                        "(13, 5,'Gry', '1656021600000', 250, 1), -- 150, 50, 50\n" +
                        "(14, 5,'Koncert', '1655503200000', 250, 1), --100, 50, 50\n" +
                        "\n" +
                        "--lipiec\n" +
                        "(15, 2,'Bilet miesięczny', '1657576800000', 90, 1), --90\n" +
                        "(16, 2,'Kosmetyki', '1656972000000', 150, 1), --50, 20, 30, 50\n" +
                        "(17, 2,'Zakup ubrań', '1658440800000', 350, 1), --250, 100\n" +
                        "(19, 3,'Wymiana opon', '1657576800000', 2500, 1), --2300, 200  \n" +
                        "(20, 4,'Rachunki', '1656972000000', 1600, 1), --850, 150, 150, 450 \n" +
                        "(21, 4,'Środku czystości', '1658440800000', 100, 1), --30, 20, 25, 25\n" +
                        "(22, 5,'Siłownia', '1656799200000', 100, 1), --100\n" +
                        "(24, 5,'Kino', '1658008800000', 100, 1), --100, 50, 50\n" +
                        "\n" +
                        "--sierpien\n" +
                        "(25, 2,'Mandat', '1660082400000', 150, 0), --150\n" +
                        "(26, 2,'Bilet miesięczny', '1660255200000', 90, 1), --90\n" +
                        "(27, 2,'Kosmetyki', '1659650400000', 150, 1), --50, 20, 30, 50\n" +
                        "(28, 2,'Zakup ubrań', '1661032800000', 150, 1), --50, 50, 50  \n" +
                        "(29, 3,'Czyszczenie', '1659650400000', 250, 1), --250\n" +
                        "(30, 4,'Rachunki', '1659650400000', 1600, 1), --850, 150, 150, 450 \n" +
                        "(31, 4,'Środku czystości', '1661119200000', 95, 1), -- 50, 20, 25\n" +
                        "(32, 5,'Siłownia', '1659477600000', 100, 1), --100\n" +
                        "(33, 5,'Wycieczka', '1661292000000', 650, 1), -- 450, 50, 50, 100\n" +
                        "(34, 5,'Wycieczka', '1660687200000', 650, 1) --450, 50, 50, 100");

                db.execSQL("insert into expense_components (component_id, expense_id, description, amount)\n" +
                        "values (1, 1, 'Jedzenie', 50),(2, 1, 'Opłata za pole', 80),(3, 1, 'Kajak', 60),(4, 1, 'Dojazd', 50),\n" +
                        "(39, 2, 'Wartość', 150),\n" +
                        "(5, 3, 'Wartość', 90),\n" +
                        "(6, 4, 'Antyperspirant', 20),(7, 4, 'Maszynka do golonia', 60),(8, 4, 'Perfumy A', 100),(9, 4, 'Perfumy B', 100),\n" +
                        "(10, 5, 'Kurtka', 250),(11, 5, 'Koszulka', 50),(12, 5, 'Buty', 200),(13, 5, 'Koszula', 100),\n" +
                        "(14, 6, 'Przegląd', 150),(15, 6, 'Ubezpieczenia', 500),\n" +
                        "(16, 7, 'Robocizna', 250),(17, 7, 'Części', 450),\n" +
                        "(18, 8, 'Kredyt', 850),(40, 8, 'Prąd', 150), (19, 8, 'Śmieci', 150),(20, 8, 'Czynsz', 450),\n" +
                        "(21, 9, 'Podłogi', 30),(22, 9, 'Szyby', 50), (23, 9, 'Meble', 20),(24, 9, 'Kuchnia', 25),(25, 9, 'Toaleta', 25),\n" +
                        "(26, 10, 'Robocizna', 3500),(27, 10, 'Części', 1500),\n" +
                        "(28, 11, 'Siłownia', 100),\n" +
                        "(29, 12, 'Łańcuch', 50),(30, 12, 'Opona', 50), (31, 12, 'Robocizna', 50),\n" +
                        "(32, 33, 'GOD OF WAR', 150),(34, 13, 'Abonament', 50), (35, 13, 'Wir. waluta', 50),\n" +
                        "(36, 14, 'Bilet', 150),(37, 14, 'Dojazd', 15), (38, 14, 'Napije itp.', 85),\n" +
                        "--CZE\n" +
                        "--LIP\n" +
                        "(60, 15, 'Wartość', 90),\n" +
                        "(61, 16, 'Maszynka do golenia', 50),(41, 16, 'Pianka do golenia', 20),(42, 16, 'Żel', 30),(43, 16, 'Płyn do komp.', 50),\n" +
                        "(44, 17, 'Spodnie', 250),(45, 17, 'Koszulka', 100),\n" +
                        "(46, 19, 'Opony', 2300),(47, 19, 'Robocizna', 200),\n" +
                        "(48, 20, 'Kredyt', 850),(49, 20, 'Prąd', 150), (50, 20, 'Śmieci', 150),(51, 20, 'Czynsz', 450),\n" +
                        "(52, 21, 'Podłogi', 30), (53, 21, 'Kuchnia', 20),(54, 21, 'Toaleta', 25),(55, 21, 'Szyby', 25),\n" +
                        "(56, 22, 'Siłownia', 100),\n" +
                        "(57, 24, '2x Bilet ', 80),(58, 24, 'Popcorn', 20), (59, 24, 'Kolacja.', 100),\n" +
                        "\n" +
                        "\n" +
                        "--SIER\n" +
                        "(62, 25, 'Wartość', 150),\n" +
                        "(63, 26, 'Wartość', 90),\n" +
                        "(64, 27, 'Maszynka do golenia', 50),(65, 27, 'Pianka do golenia', 20),(66, 27, 'Żel', 30),(67, 27, 'Płyn do komp.', 50),\n" +
                        "(68, 28, 'Majtki', 50),(69, 28, 'Skarpetki', 50),(70, 28, 'Kapcie', 50),\n" +
                        "(71, 29, 'Pranie tap.', 250),\n" +
                        "(72, 30, 'Kredyt', 850),(73, 30, 'Prąd', 150), (74, 30, 'Śmieci', 150),(75, 30, 'Czynsz', 450),\n" +
                        "(76, 31, 'Podłogi', 50), (77, 31, 'Kuchnia', 20),(78, 31, 'Toaleta', 25),\n" +
                        "(79, 32, 'Siłownia', 100),\n" +
                        "(80, 33, 'Bilet', 450),(81, 33, 'Zwiedzanie', 50), (82, 33, 'Kolacja.', 50), (83, 33, 'Pamiątki', 100),\n" +
                        "(84, 34, 'Bilet ', 450),(85, 34, 'Zwiedzanie', 50), (86, 34, 'Kolacja.', 50), (87, 34, 'Pamiątki', 100)");

            } else {
                db.execSQL("insert into categories(category_id, category_name) \n" +
                        "values(1, 'Select category'), (2, 'Others'),\n" +
                        " (3, 'Car'),\n" +
                        " (4, 'Home'),\n" +
                        " (5, 'Hobby')");

                db.execSQL("insert into expenses(expense_id, category_id, expense_name, expense_date, expense_total_value, settled) \n" +
                        "--czerwiec\n" +
                        "values (1, 2,'Camping', '1654984800000', 250, 1), -- 50, 60, 80, 60 \n" +
                        "(2, 2,'Penalty', '1654812000000', 150, 0), --150\n" +
                        "(3, 2,'Monthly ticket', '1654984800000', 90, 1), --90\n" +
                        "(4, 2,'Beauty products', '1654380000000', 250, 1), --150, 20, 30, 50\n" +
                        "(5, 2,'Buying clothes', '1655762400000', 600, 1), --250, 50, 200, 100\n" +
                        "(6, 3,'Fees', '1654984800000', 650, 1), --150\n" +
                        "(7, 3,'Repair', '1654207200000', 700, 1), -- 450, 250\n" +
                        "(8, 4,'Bills', '1654380000000', 1600, 1), --850, 150, 150, 450 \n" +
                        "(9, 4,'Cleaning products', '1655848800000', 150, 1), --30, 50, 20, 25, 25 \n" +
                        "(10, 4,'Furnitures', '1656453600000', 4500, 1), --3500, 1500\n" +
                        "(11, 5,'Gym', '1654207200000', 100, 1), --100\n" +
                        "(12, 5,'Bike', '1655244000000', 150, 1), --50, 50, 50\n" +
                        "(13, 5,'Comuter games', '1656021600000', 250, 1), -- 150, 50, 50\n" +
                        "(14, 5,'Concert', '1655503200000', 250, 1), --100, 50, 50\n" +
                        "\n" +
                        "--lipiec\n" +
                        "(15, 2,'Monthly ticket', '1657576800000', 90, 1), --90\n" +
                        "(16, 2,'Beauty products', '1656972000000', 150, 1), --50, 20, 30, 50\n" +
                        "(17, 2,'Buying clothes', '1658440800000', 350, 1), --250, 100\n" +
                        "(19, 3,'Tire replacement', '1657576800000', 2500, 1), --2300, 200  \n" +
                        "(20, 4,'Bills', '1656972000000', 1600, 1), --850, 150, 150, 450 \n" +
                        "(21, 4,'Cleaning products', '1658440800000', 100, 1), --30, 20, 25, 25\n" +
                        "(22, 5,'Gym', '1656799200000', 100, 1), --100\n" +
                        "(24, 5,'Cinema', '1658008800000', 100, 1), --100, 50, 50\n" +
                        "\n" +
                        "--sierpien\n" +
                        "(25, 2,'Penalty', '1660082400000', 150, 0), --150\n" +
                        "(26, 2,'Monthly ticket', '1660255200000', 90, 1), --90\n" +
                        "(27, 2,'Beauty products', '1659650400000', 150, 1), --50, 20, 30, 50\n" +
                        "(28, 2,'Buying clothes', '1661032800000', 150, 1), --50, 50, 50  \n" +
                        "(29, 3,'Cleaning', '1659650400000', 250, 1), --250\n" +
                        "(30, 4,'Bills', '1659650400000', 1600, 1), --850, 150, 150, 450 \n" +
                        "(31, 4,'Cleaning products', '1661119200000', 95, 1), -- 50, 20, 25\n" +
                        "(32, 5,'Gym', '1659477600000', 100, 1), --100\n" +
                        "(33, 5,'Trip', '1661292000000', 650, 1), -- 450, 50, 50, 100\n" +
                        "(34, 5,'Trip', '1660687200000', 650, 1) --450, 50, 50, 100");

                db.execSQL("insert into expense_components (component_id, expense_id, description, amount)\n" +
                        "values (1, 1, 'Food', 50),(2, 1, 'Field fee', 80),(3, 1, 'Kayak', 60),(4, 1, 'Acces fee', 50),\n" +
                        "(39, 2, 'Value', 150),\n" +
                        "(5, 3, 'Value', 90),\n" +
                        "(6, 4, 'Antiperspirant', 20),(7, 4, 'Shaver', 60),(8, 4, 'Perfume A', 100),(9, 4, 'Perfume B', 100),\n" +
                        "(10, 5, 'Jacket', 250),(11, 5, 'T-shirt', 50),(12, 5, 'Shoes', 200),(13, 5, 'Shirt', 100),\n" +
                        "(14, 6, 'Technical overview', 150),(15, 6, 'Insurance', 500),\n" +
                        "(16, 7, 'Labor', 250),(17, 7, 'Parts', 450),\n" +
                        "(18, 8, 'Credit', 850),(40, 8, 'Electicity', 150), (19, 8, 'Garbage', 150),(20, 8, 'Rent', 450),\n" +
                        "(21, 9, 'Floors', 30),(22, 9, 'Glasses', 50), (23, 9, 'Furnitures', 20),(24, 9, 'Kitchen', 25),(25, 9, 'Toilet', 25),\n" +
                        "(26, 10, 'Labor', 3500),(27, 10, 'Parts', 1500),\n" +
                        "(28, 11, 'Gym', 100),\n" +
                        "(29, 12, 'Chain', 50),(30, 12, 'Tyre', 50), (31, 12, 'Labor', 50),\n" +
                        "(32, 33, 'GOD OF WAR', 150),(34, 13, 'Subscription', 50), (35, 13, 'Vir. currency', 50),\n" +
                        "(36, 14, 'Ticket', 150),(37, 14, 'Acces fee', 15), (38, 14, 'Drinks etc', 85),\n" +
                        "--CZE\n" +
                        "--LIP\n" +
                        "(60, 15, 'Value', 90),\n" +
                        "(61, 16, 'Razor', 50),(41, 16, 'Shaving foam', 20),(42, 16, 'Shower gel', 30),(43, 16, 'Bath lotion', 50),\n" +
                        "(44, 17, 'Pants', 250),(45, 17, 'Shirt', 100),\n" +
                        "(46, 19, 'Tyers', 2300),(47, 19, 'Labor', 200),\n" +
                        "(48, 20, 'Credit', 850),(49, 20, 'Electicity', 150), (50, 20, 'Garbage', 150),(51, 20, 'Rent', 450),\n" +
                        "(52, 21, 'Floors', 30), (53, 21, 'kitchen', 20),(54, 21, 'Toilet', 25),(55, 21, 'Glasses', 25),\n" +
                        "(56, 22, 'Gym', 100),\n" +
                        "(57, 24, '2x Ticket ', 80),(58, 24, 'Popcorn', 20), (59, 24, 'Dinner', 100),\n" +
                        "\n" +
                        "\n" +
                        "--SIER\n" +
                        "(62, 25, 'Value', 150),\n" +
                        "(63, 26, 'Value', 90),\n" +
                        "(64, 27, 'Razor', 50),(65, 27, 'Shaving foam', 20),(66, 27, 'Shower gel', 30),(67, 27, 'Bath lotion', 50),\n" +
                        "(68, 28, 'Uderpants', 50),(69, 28, 'Socks', 50),(70, 28, 'Slippers', 50),\n" +
                        "(71, 29, 'Upholstery wash', 250),\n" +
                        "(72, 30, 'Credit', 850),(73, 30, 'Electicity', 150), (74, 30, 'Garbage', 150),(75, 30, 'Rent', 450),\n" +
                        "(76, 31, 'Floors', 50), (77, 31, 'kitchen', 20),(78, 31, 'Toilet', 25),\n" +
                        "(79, 32, 'Gym', 100),\n" +
                        "(80, 33, 'Ticket', 450),(81, 33, 'Visit', 50), (82, 33, 'Dinner', 50), (83, 33, 'Souvenirs', 100),\n" +
                        "(84, 34, 'Ticket ', 450),(85, 34, 'Visit', 50), (86, 34, 'Dinner', 50), (87, 34, 'Souvenirs', 100)");

            }



            db.execSQL(
                    new StringBuilder()
                            .append("CREATE TRIGGER IF NOT EXISTS update_expense_total_value_after_insert_component")
                            .append("AFTER INSERT ON expense_components ")
                            .append(" BEGIN ")
                            .append("  UPDATE expenses SET expense_total_value = ifnull((SELECT (SUM(amount) + new.amount) FROM expense_components WHERE expense_id = new.expense_id), new.amount) WHERE expense_id = new.expense_id;")
                            .append(" END;")
                            .toString());
            db.execSQL(
                    new StringBuilder()
                            .append("CREATE TRIGGER IF NOT EXISTS update_expense_total_value_after_delete_component")
                            .append("AFTER DELETE ON expense_components ")
                            .append(" BEGIN ")
                            .append("  UPDATE expenses SET expense_total_value = ifnull((SELECT (SUM(amount) - old.amount) FROM expense_components WHERE expense_id = old.expense_id), 0) WHERE expense_id = old.expense_id;")
                            .append(" END;").toString());

        }
    };
}
