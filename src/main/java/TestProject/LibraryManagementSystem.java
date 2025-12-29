package TestProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Objects;

public class LibraryManagementSystem {

    // --- 1. Book Class ---
    public static class Book {
        private String title;
        private String author;
        private String bookNum; 
        private boolean isAvailable;

        public Book(String title, String author, String bookNum) {
            this.title = title;
            this.author = author;
            this.bookNum = bookNum;
            this.isAvailable = true; 
        }

        public String getTitle() { return title; }
        public String getBookNum() { return bookNum; }
        public boolean isAvailable() { return isAvailable; }
        public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }
        
        @Override
        public String toString() {
            return "Title: " + title + " | Author: " + author + " | Book Number: " + bookNum + " | Availability: " + (isAvailable ? "Available" : "Borrowed");
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Book book = (Book) o;
            return Objects.equals(bookNum, book.bookNum);
        }

        @Override
        public int hashCode() { return Objects.hash(bookNum); }
    }

    // --- 2. Member Class ---
    public static class Member {
        private String name;
        private String memberId;
        private List<Book> borrowedBooks;

        public Member(String name, String memberId) {
            this.name = name;
            this.memberId = memberId;
            this.borrowedBooks = new ArrayList<>();
        }

        public String getName() { return name; }
        public String getMemberId() { return memberId; }
        public List<Book> getBorrowedBooks() { return borrowedBooks; }
        public void borrowBook(Book book) { borrowedBooks.add(book); }
        public void returnBook(Book book) { borrowedBooks.remove(book); }
    }

    // --- 3. Main System Logic ---
    // Static lists to be accessed via Reflection in tests
    public static List<Book> books = new ArrayList<>();
    public static List<Member> members = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Initial data
        addBook("Introduction to Programming", "Ahmed Ali", "BK001"); 
        
        int choice = 0;
        while (choice != 6) {
            displayMenu();
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); 
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1: promptAddBook(); break;
                case 2: viewAvailableBooks(); break;
                case 3: borrowBook(); break;
                case 4: returnBook(); break;
                case 5: promptAddMember(); break;
                case 6: System.out.println("Thank you for using the Library System. Goodbye!"); break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n*** Electronic Library Management System (CLI) ***");
        System.out.println("1. Add New Book");
        System.out.println("2. View Available Books");
        System.out.println("3. Borrow a Book");
        System.out.println("4. Return a Book");
        System.out.println("5. Add New Member");
        System.out.println("6. Exit");
        System.out.print("Please enter your choice: ");
    }

    // Pure logic methods (Called by Reflection in tests)
    public static void addBook(String title, String author, String bookNum) {
        books.add(new Book(title, author, bookNum));
    }

    public static void addMember(String name, String id) {
        members.add(new Member(name, id));
    }

    // UI methods (Handling Scanner input)
    private static void promptAddBook() {
        System.out.print("Enter book title: "); String t = scanner.nextLine();
        System.out.print("Enter author name: "); String a = scanner.nextLine();
        System.out.print("Enter Book Number: "); String n = scanner.nextLine();
        addBook(t, a, n);
        System.out.println("Book added successfully.");
    }

    private static void promptAddMember() {
        System.out.print("Enter member name: "); String n = scanner.nextLine();
        System.out.print("Enter member ID: "); String i = scanner.nextLine();
        addMember(n, i);
        System.out.println("Member registered successfully.");
    }

    public static Book findBookByBookNum(String bookNum) {
        for (Book b : books) {
            if (b.getBookNum().equals(bookNum)) return b;
        }
        return null;
    }

    private static Member findMemberById(String memberId) {
        for (Member m : members) {
            if (m.getMemberId().equals(memberId)) return m;
        }
        return null;
    }

    public static void viewAvailableBooks() {
        System.out.println("\n*** Available Books ***");
        boolean found = false;
        for (Book book : books) {
            if (book.isAvailable()) {
                System.out.println("- " + book);
                found = true;
            }
        }
        if (!found) System.out.println("No books available.");
    }

    public static void borrowBook() {
        System.out.print("Enter Book Number: "); String bn = scanner.nextLine();
        System.out.print("Enter Member ID: "); String mid = scanner.nextLine();
        Book b = findBookByBookNum(bn);
        Member m = findMemberById(mid);
        
        if (b != null && m != null && b.isAvailable()) {
            b.setAvailable(false);
            m.borrowBook(b);
            System.out.println("Borrowing successful!");
        } else {
            System.out.println("Cannot borrow book (Check availability or IDs).");
        }
    }

    public static void returnBook() {
        System.out.print("Enter Book Number: "); String bn = scanner.nextLine();
        System.out.print("Enter Member ID: "); String mid = scanner.nextLine();
        Book b = findBookByBookNum(bn);
        Member m = findMemberById(mid);
        
        if (b != null && m != null && !b.isAvailable()) {
            b.setAvailable(true);
            m.returnBook(b);
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Return failed.");
        }
    }
}