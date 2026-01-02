package TestProject;

import TestProject.LibraryManagementSystem;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration Test for LibraryManagementSystem using Java Reflection.
 */
public class LibraryManagementSystemIT {

    @BeforeEach
    void setup() throws Exception {
        // Clear the static lists before each test to ensure test isolation
        clearList("books");
        clearList("members");
    }

    /**
     * Helper method to clear static lists in the main class using Reflection.
     */
    private void clearList(String fieldName) throws Exception {
        Field field = LibraryManagementSystem.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        // Cast the object to a List and clear it
        ((List<?>) field.get(null)).clear();
    }

    @Test
    void testAddBookAndSearch() throws Exception {
        // Add a book programmatically
        invokeAddBook("Java", "Author", "BK001");
        
        // Search for the book using its ID
        Object book = invokeFindBook("BK001");
        
        // Verify that the book was successfully added and found
        assertNotNull(book, "The book should not be null after being added.");
    }

    @Test
    void testBorrowBookSuccess() throws Exception {
        // Prepare data
        invokeAddBook("Java", "Author", "B002");
        invokeAddMember("Ali", "M001");

        // Locate the book
        Object book = invokeFindBook("B002");
        assertNotNull(book);
        
        // Use Reflection to call 'isAvailable' and check the book status
        Method isAvailableMethod = book.getClass().getMethod("isAvailable");
        boolean status = (boolean) isAvailableMethod.invoke(book);
        
        assertTrue(status, "A newly added book should be available by default.");
    }

    @Test
    void testBorrowUnavailableBook() throws Exception {
        invokeAddBook("Java", "Author", "B003");
        Object book = invokeFindBook("B003");
        
        // Manually change the book status to unavailable using Reflection
        Method setAvailableMethod = book.getClass().getMethod("setAvailable", boolean.class);
        setAvailableMethod.invoke(book, false);

        // Verify that 'isAvailable' now returns false
        Method isAvailableMethod = book.getClass().getMethod("isAvailable");
        assertFalse((boolean) isAvailableMethod.invoke(book), "The book should show as unavailable now.");
    }


    private void invokeAddBook(String title, String author, String bookNum) throws Exception {
        Method m = LibraryManagementSystem.class.getDeclaredMethod("addBook", String.class, String.class, String.class);
        m.setAccessible(true);
        m.invoke(null, title, author, bookNum); // Executes the method
    }

    private void invokeAddMember(String name, String id) throws Exception {
        Method m = LibraryManagementSystem.class.getDeclaredMethod("addMember", String.class, String.class);
        m.setAccessible(true);
        m.invoke(null, name, id);
    }

 
    private Object invokeFindBook(String bookNum) throws Exception {
        Method m = LibraryManagementSystem.class.getDeclaredMethod("findBookByBookNum", String.class);
        m.setAccessible(true);
        return m.invoke(null, bookNum);
    }
    @Test
    void testIntegrationMemberBorrowBook() throws Exception {
        
        invokeAddBook("Software Testing", "Naik", "BK-999");
        invokeAddMember("Abdalkareem", "M-5161");

        
        Object book = invokeFindBook("BK-999");
       
        Object member = invokeFindMember("M-5161");

        assertNotNull(book, "Book should exist");
        assertNotNull(member, "Member should exist");

        Method borrowMethod = member.getClass().getMethod("borrowBook", LibraryManagementSystem.Book.class);
        borrowMethod.invoke(member, book);

        Method getBorrowedBooks = member.getClass().getMethod("getBorrowedBooks");
        List<?> borrowedList = (List<?>) getBorrowedBooks.invoke(member);

        assertTrue(borrowedList.contains(book), "The book must be in the member's borrowed list");
    }

        private Object invokeFindMember(String memberId) throws Exception {
        Method m = LibraryManagementSystem.class.getDeclaredMethod("findMemberById", String.class);
        m.setAccessible(true);
        return m.invoke(null, memberId);
    }
}